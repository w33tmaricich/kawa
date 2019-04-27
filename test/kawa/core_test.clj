(ns kawa.core-test
  (:require [clojure.test :refer :all]
            [kawa.core :refer :all]))

(deftest formatting
  (testing "format ffmpeg command"
    (is (= (fmt-cmd FFPLAY :disable-audio)
           ["ffplay" "-an"]))
    (is (= (fmt-cmd FFMPEG :this "is" :very 2)
           ["ffmpeg" "-this" "is" "-very" "2"]))))

;(deftest ffplay
  ;(testing "playing a video"
    ;(is (= 0
           ;(ffplay! :disable-video :disable-audio :no-display :duration 10 "rtsp://admin:robot@172.28.137.102:554/media/video1")))))
