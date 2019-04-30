(ns kawa.manager-test
  (:require [clojure.test :refer :all]
            [kawa.manager :refer :all]
            [clojure.core.async :refer [chan]]))

(deftest initialize-manager
  (testing "Initialize the process manager."
    (let [[state channel] (manager-init)]
      (is (= (type state) (type (atom {}))))
      (is (= (type channel) (type (chan)))))))
