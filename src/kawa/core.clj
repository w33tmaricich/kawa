(ns kawa.core
  (:use [clojure.java.shell :only [sh]]))

(def FFMPEG {:cmd "ffmpeg"
             :flags {}})

(def FFPLAY {:cmd"ffplay"
             :flags {:disable-audio :an
                     :disable-subtitles :sn
                     :disable-video :vn
                     :duration :t
                     :exit-on-finished :autoexit
                     :force-format :f
                     :force-height :y
                     :force-width :x
                     :fullscreen :fs
                     :input-url :i
                     :no-display :nodisp
                     :seek :ss
                     :window-x-pos :left
                     :window-y-pos :top}})

(defn fmt-cmd
  "Creates a vector that is ready to be passed to sh.
  app: a map with a :cmd and :flags
  argv: any number of arguments to be passed to sh
  
  1. All keywords in argv are converted to a app:flags keyword if one exists.
  2. All keywords are converted to - prefixed strings.
  3. A vector is created with app:cmd + newly formatted args."
  [app & argv]
  (->> argv
       (map #(if (and (keyword? %) ((app :flags) %)) ((app :flags) %) %))
       (map #(if (keyword? %) (str "-" (name %)) (str %)))
       (into [(app :cmd)])))

(defn sh-apply!
  "Takes an application map and a list of arguments. Applies the formatted
  application and arguments to sh."
  [app & args]
  (let [cmd! (apply sh (apply fmt-cmd app args))
        {:keys [exit out err]} cmd!]
    (when-not (zero? exit)
      (throw (Exception. err)))
    out))

(defn ffmpeg! [& args]
  (sh-apply! FFMPEG args))

(defn ffplay! [& args]
  (sh-apply! FFPLAY args))
