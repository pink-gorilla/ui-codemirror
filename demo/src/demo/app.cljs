(ns demo.app
  (:require
   [reitit.frontend.easy :as rfe]
   [frontend.css :refer [css-loader set-theme-component theme-a]]
   [shadowx.core :refer [get-resource-path]]))

(defn create-link [page & opts]
  (try (if opts
         (apply rfe/href page opts)
         (rfe/href page))
       (catch :default ex ; js/Exception ex
         (println "error in link for: " page opts)
         "")))

(defn link [[page & opts] text]
  (let [href (apply create-link page opts)]
    [:a {:href href
         :style {:background "yellow"}}
     [:span {:style {:margin "2px"}}
      " [ " text " ] "]]))

(defn set-theme [theme]
  (set-theme-component :codemirror theme))

(defn codemirror-theme-names [theme-state]
  (->> (get-in theme-state [:available :codemirror])
       keys
       (filter string?)
       sort))

(defn menu []
  (let [theme-state @theme-a
        themes (codemirror-theme-names theme-state)
        current (get-in theme-state [:current :codemirror])]
    [:div.bg-blue-300.p-5.w-screen.h-screen

     [:div
      [link ['page.codemirror1/codemirror-unbound-page] "codemirror1"]
      [link ['page.codemirror2/codemirror-demo-page2] "codemirror2"]
      [link ['page.codemirror3/codemirror-demo-page3] "codemirror3"]]
     [:br]

     [:label.mr-2 "CodeMirror theme"]
     [:select.border.border-round.p-1.bg-white
      {:value (str current)
       :on-change #(set-theme (-> % .-target .-value))}
      (for [t themes]
        [:option {:key t :value t} t])]]))

(defn wrap [page match]
  [:div
   [css-loader (get-resource-path)]
   [menu]
   [page match]])

(def routes
  [["/" {:name 'page.codemirror1/codemirror-unbound-page}]
   ["/2" {:name 'page.codemirror2/codemirror-demo-page2}]
   ["/3" {:name 'page.codemirror3/codemirror-demo-page3}]])

