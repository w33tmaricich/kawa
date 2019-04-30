(ns kawa.manager
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

(defn manager-init
  "Create stateful atom and messaging channel."
  []
  (list (atom {}) (chan)))
