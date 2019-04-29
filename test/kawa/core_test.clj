(ns kawa.core-test
  (:require [clojure.test :refer :all]
            [kawa.core :refer :all]))

;(ffplay! :duration 11 "rtsp://admin:robot@172.28.137.102:554/media/video1")

(deftest formatting
  (testing "format commands based off apps"
    (is (= (fmt-cmd FFPLAY :disable-audio)
           ["ffplay" "-an"]))
    (is (= (fmt-cmd FFMPEG :this "is" :very 2)
           ["ffmpeg" "-this" "is" "-very" "2"]))))

(deftest ffplay
  (testing "playing a video"
    (is (= "\n"
           (ffplay! :autoexit :duration 10 "rtsp://admin:robot@172.28.137.102:554/media/video1")))))
