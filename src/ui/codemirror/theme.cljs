(ns ui.codemirror.theme)

;; styles for theme

(defn style-codemirror-inline1 []
  ; Set height, width, borders, and global font properties here
  ; height: 200px;
  ; height: auto;    auto = adjust height to fit content 
  ; height: 100%
  [:style ".my-codemirror > .CodeMirror { 
              font-family: monospace;
              height: auto;
            }"])


(defn style-codemirror-inline []
  ; Set height, width, borders, and global font properties here
  ; height: 200px;
  ; height: auto;    auto = adjust height to fit content 
  ; height: 100%
  [:style "@import url(\"https://fonts.googleapis.com/css2?family=Fira+Code:wght@300..700&display=swap\");
           .my-codemirror > .CodeMirror { 
              font-family: \"Fira Code\", monospace;
              height: auto;
            }"])


(defn style-codemirror-fullscreen1 []
  ; height: auto; "400px" "100%"  height: auto;
  ; auto will make the editor resize to fit its content (i
  [:style ".my-codemirror > .CodeMirror { 
              font-family: monospace;
              height: 100%;
              min-height: 100%;
              max-height: 100%;
            }"])

(defn style-codemirror-fullscreen []
  ; height: auto; "400px" "100%"  height: auto;
  ; auto will make the editor resize to fit its content (i
  [:style "
           .my-codemirror > .CodeMirror { 
              font-family: \"Fira Code\", monospace;
              height: 100%;
              min-height: 100%;
              max-height: 100%;
            }"])


