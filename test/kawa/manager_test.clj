(ns kawa.manager-test
  (:require [clojure.test :refer :all]
            [kawa.manager :refer :all]
            [clojure.core.async :refer [chan]]))

(deftest manager-read-writes
  (testing "Reading from the manager."
    (let [process-info {:id "fun" :desc "great"}]
      (is (empty? (ls)))
      (register process-info)
    (is (= process-info (second (first (ls))))))))
