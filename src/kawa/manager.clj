(ns kawa.manager
  (:require [me.raynes.conch.low-level :as sh]))

(def STATE (atom {}))

(defn uuid [] (-> (java.util.UUID/randomUUID)
                  keyword))

(defn ls []
  @STATE)

(defn register
  "Add a process to the manager."
  ([process-info]
   (let [process-id (uuid)]
    (swap! STATE assoc process-id process-info)))
  ([process-id process-info]
   (swap! STATE assoc process-id process-info)))

(defn kill [process-id]
  (let [state @STATE
        process (process-id state)
        command (:cmd process)
        conch-process (:process process)
        exit-code (future (sh/exit-code conch-process))]
    (println conch-process)
    (sh/destroy conch-process)
    (println "Stopping" process-id)
    (swap! STATE dissoc process-id)
    exit-code))
