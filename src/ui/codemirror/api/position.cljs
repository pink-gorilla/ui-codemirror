(ns ui.codemirror.api.position)

(defn cursor-position [cm]
  (let [pos (.getCursor cm)
        line (.-line pos)
        ch (.-ch pos)]
    {:line line
     :col ch}))

(defn move-to-last-line [cm]
  (let [last-line (.lastLine cm)
        last-ch (count (.getLine cm last-line))]
    (.setCursor cm last-line last-ch)))

; navigate buffer up/down

(defn first-line-number [cm]
  (.firstLine cm))

(defn last-line-number [cm]
  (.lastLine cm))

(defn current-line-number [cm]
  (let [pos (.getCursor cm)]
    (.-line pos)))

(defn first-line? [cm]
  (let [f (first-line-number cm)
        c (current-line-number cm)]
    (= f c)))

(defn last-line? [cm]
  (let [l (last-line-number cm)
        c (current-line-number cm)]
    (= l c)))

(defn cursor-coords [cm]
  (let [pos (.getCursor cm)]
    (-> (.cursorCoords cm)
        (js->clj))))

; cm.cursorCoords(where: boolean|{line, ch}, mode: string) → {left, top, bottom}
; Returns an {left, top, bottom} object containing the coordinates of the cursor 
; position. If mode is "local", they will be relative to the top-left corner of
; the editable document. If it is "page" or not given, they are relative to the 
; top-left corner of the page. If mode is "window", the coordinates are relative 
; to the top-left corner of the currently visible (scrolled) window. where can be
; a boolean indicating whether you want the start (true) or the end (false) of the
; selection, or, if a {line, ch} object is given, it specifies the precise position
; at which you want to measure.

; cm.cursorCoords (Pos (1, 1))
; cm.cursorCoords (null, "window") .bottom;