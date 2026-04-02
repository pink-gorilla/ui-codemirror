(ns ui.codemirror.bound.core
  (:require
   [taoensso.timbre :refer-macros [debug debugf info infof warn error]]
   [reagent.core :as r]
   ["codemirror" :as CodeMirror]
   ["codemirror/addon/edit/closebrackets"]
   ["codemirror/addon/edit/matchbrackets"]
   ["codemirror/addon/hint/show-hint"]
   ["codemirror/addon/runmode/runmode"]
   ["codemirror/addon/runmode/colorize"]
   ["codemirror/mode/clojure/clojure"]
   ["codemirror/mode/markdown/markdown"]
   ; [cljsjs.codemirror.mode.xml]
   ; [cljsjs.codemirror.mode.javascript]
   ; ["parinfer-codemirror"]
   ; [cljsjs.codemirror.mode.clojure-parinfer]
   ;["codemirror/keymap/vim"]
   [ui.codemirror.codemirror :refer [active-editor-atom get-editor]]
   [ui.codemirror.theme]
   [ui.codemirror.options :refer [cm-default-opts ensure-initialized]]
   [ui.codemirror.highlight]
   [ui.codemirror.cm-events.change :refer [editor-load-content on-change]]
   [ui.codemirror.cm-events.key :refer [on-key-down on-key-up]]
   [ui.codemirror.cm-events.mouse :refer [on-mousedown]]))

(defn focus-cm!
  [cm]
  (when cm
    (.focus cm)))

(defn blur-cm!
  [cm]
  (when cm
    (let [input (.getInputField cm)]
      (.blur input))))

(defn focus-active [id {:keys [active? view-only] :as cm-opts} cm]
  (when (and active? (not view-only))
    (debugf "focusing cm %s .." id "active:" active? "view-only: " view-only)
    (focus-cm! cm)))

(defn blur-inactive [id {:keys [active? view-only] :as cm-opts} cm]
  (when (or (not active?) view-only)
    (debugf "blurring cm %s" id "active:" active? "view-only: " view-only)
    (blur-cm! cm)))

(defn create-editor [id el opts-js]
  (info "creating codemirror-js id: " id)
  (ensure-initialized)
  ;cm_ (CodeMirror. el opts-js)
  (let [cm (.fromTextArea CodeMirror el opts-js)]
    (swap! active-editor-atom assoc id cm)
    cm))

(defn destroy-editor [id]
  (info "destroying codemirror-js id: " id)
  (if-let [cm (get-editor id)]
    (do (.toTextArea cm)
        (swap! active-editor-atom dissoc id))
    (warn "Could not kill CodeMirror instance id: " id)))

(defn codemirror-reagent
  "code-mirror editor"
  [id {:keys [get-data] :as fun} cm-opts]
  (let [opts (merge
              cm-default-opts
              cm-opts
              {:readOnly (:view-only cm-opts)})
        ;_ (warn "opts: " opts)
        textarea-el (atom nil)
        make-event-handler (fn [f]
                             (fn [s evt]
                               ;(info "cm event - evt: " evt " cm:" s)
                               (f {:id id
                                   :cm-opts opts
                                   :fun fun
                                   :cm (get-editor id)} s evt)))]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (when-let [el @textarea-el]
          (let [opts-js (clj->js opts)
                ;_ (info "component-did-mount: cm")
                cm_ (create-editor id el opts-js)
                code (get-data id)]
            (.setValue cm_ code)

            ; theme - already set in cm constructor
            ;(.setOption inst "theme" (:theme opts))

            (.on cm_ "change" (make-event-handler on-change))
            (.on cm_ "keydown" (make-event-handler on-key-down))
            (.on cm_ "keyup" (make-event-handler on-key-up))
            (.on cm_ "mousedown" (make-event-handler on-mousedown))

            (blur-inactive id opts cm_)
            (focus-active id opts cm_)

            ;(when on-cm-init (on-cm-init inst))
            )))

      :component-will-unmount
      (fn [this]
        (debug "cm component-will-unmount")
        (destroy-editor id))

      :component-did-update
      (fn [this old-argv]
        (let [[_ id fun opts] (r/argv this)]
          ;(info "component-did-update: current buffer: " eval-result9         
          (when-let [cm (get-editor id)]
            (editor-load-content cm (get-data id))
            (blur-inactive id opts cm)
            (focus-active id opts cm))
          ;
          ))

      :reagent-render
      (fn []
        (let [{:keys [readOnly]} opts]
          (if readOnly
            [:textarea {:read-only true
                        :ref #(reset! textarea-el %)}]
            [:textarea {:ref #(reset! textarea-el %)}])))})))



