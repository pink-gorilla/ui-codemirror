(ns ui.codemirror.highlight
  (:require
   [reagent.core :as r]
   ["codemirror" :as CodeMirror]))

(defn colored-text [text style]
  (let [pre-el (atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (when-let [node @pre-el]
          ((aget CodeMirror "colorize") #js[node] "clojure")))
      :reagent-render
      (fn [_]
        [:pre {:style (merge {:padding 0 :margin 0} style)
               :ref #(reset! pre-el %)}
         text])})))