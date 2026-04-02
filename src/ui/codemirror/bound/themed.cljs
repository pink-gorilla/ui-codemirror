(ns ui.codemirror.bound.themed
  (:require
   [reagent.core :as r]
   [frontend.css :refer [theme-a]]
   [ui.codemirror.options :refer [ensure-initialized]]
   [ui.codemirror.bound.core :refer [codemirror-reagent]]))

(defn codemirror-themed [id fun cm-opt]
  (r/with-let [_ (ensure-initialized)]
    (let [theme (or (get-in @theme-a [:current :codemirror])
                    "mdn-like")
          cm-opt-themed (merge cm-opt {:theme theme})]
      [:div.my-codemirror
       [codemirror-reagent id fun cm-opt-themed]])))


