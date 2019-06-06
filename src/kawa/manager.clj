(ns kawa.manager
  (:require [me.raynes.conch.low-level :as sh]))

(def ^:private default-state (atom {}))

(defn- atom? [x]
  (= (type x) clojure.lang.Atom))

(defn- uuid [] (-> (java.util.UUID/randomUUID)
                   str
                   keyword))

(defn ls
 "Returns a list of currently registered processes."
 ([]
  (ls default-state))
 ([custom-state]
  {:pre [(atom? custom-state)]}
  @custom-state))

(defn register
  "Registers a process to the manager to be watched.

  Can be optionally passed a :keyword to be registered as the id of the process.
  If one is not supplied, one will be generated automatically."
  ([process-info]
   (register default-state (uuid) process-info))
  ([custom-state process-info]
   (if (atom? custom-state)
     (register custom-state (uuid) process-info)
     (register default-state custom-state process-info)))
  ([custom-state process-id process-info]
   {:pre [(atom? custom-state)
          (keyword? process-id)
          (map? process-info)]}
   (swap! custom-state assoc process-id process-info)
   process-id))

(defn kill
  "Forcibly stops a running process.

  Returns a future that holds the exit code of the killed process."
  ([process-id]
   (kill default-state process-id))
  ([custom-state process-id]
   {:pre [(atom? custom-state)
          (keyword? process-id)]}
   (let [state @custom-state
         process (process-id state)
         command (:cmd process)
         conch-process (:process process)
         exit-code (future (sh/exit-code conch-process))]
     (sh/destroy conch-process)
     (swap! custom-state dissoc process-id)
     exit-code)))
