(ns ui.codemirror.bound.themed
  (:require
   [re-frame.core :as rf]
   [ui.codemirror.options :refer [ensure-initialized]]
   [ui.codemirror.bound.core :refer [codemirror-reagent]]))

(defn codemirror-themed [id fun cm-opt]
  (ensure-initialized)
  (let [theme (rf/subscribe [:css/theme-component :codemirror])]
    (fn [id fun cm-opt]
      (let [theme (or @theme "mdn-like")
            cm-opt-themed (merge cm-opt {:theme theme})]
        [:div.my-codemirror
         [codemirror-reagent id fun cm-opt-themed]]))))


