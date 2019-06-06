(ns kawa.manager-test
  (:require [clojure.test :refer :all]
            [kawa.manager :refer :all]))

(deftest manager-read-writes
  (testing "Return values"
    (let [process-info {:id "fun" :desc "great"}
          register-return (register process-info)]
      (println register-return)
      (is (map? (ls)))
      (println (register-return (ls)))
      (is (keyword? register-return))))
  (testing "Reading from the manager."
    (let [process-info {:id "fun" :desc "great"}
          custom-bank (atom {})]
      (is (empty? (ls custom-bank)))
      (register process-info)
      (register :funkey process-info)
      (register custom-bank process-info)
      (register custom-bank :funkey process-info)
    (is (= process-info (second (first (ls))))))))
