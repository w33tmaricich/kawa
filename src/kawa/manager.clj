(ns kawa.manager
  (:require [me.raynes.conch.low-level :as sh]))

(def STATE (atom {}))

(defn- uuid [] (-> (java.util.UUID/randomUUID)
                  keyword))

(defn ls
 "Returns a list of currently registered processes."
 []
  @STATE)

(defn register
  "Registers a process to the manager to be watched.
  
  Can be optionally passed a :keyword to be registered as the id of the process.
  If one is not supplied, one will be generated automatically."
  ([process-info]
   (let [process-id (uuid)]
    (swap! STATE assoc process-id process-info)))
  ([process-id process-info]
   (swap! STATE assoc process-id process-info)))

(defn kill
  "Forcibly stops a running process.
  
  Returns a future that holds the exit code of the killed process."
  [process-id]
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
