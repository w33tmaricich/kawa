(ns kawa.core-test
  (:require [clojure.test :refer :all]
            [me.raynes.conch.low-level :as sh]
            [kawa.core :refer :all]))

;(ffplay! :duration 11 "rtsp://admin:robot@172.28.137.102:554/media/video1")

(defn application-format [app]
  (and (contains? app :cmd)
       (contains? app :flags)))

(defn common-flags
  "Ensure all flags are mapped the same in all applications."
  [app1 app2]
  (let [flags1 (app1 :flags)
        flags2 (app2 :flags)]
    (loop [flag-map (first flags1)
           newflags (rest flags1)
           missmatched {}]
      (if (empty? newflags)
        missmatched
        (if (or (nil? ((first flag-map) flags2))
                (= ((first flag-map) flags2)
                   (second flag-map)))
          (recur (first newflags)
                 (rest newflags)
                 missmatched)
          (recur (first newflags)
                 (rest newflags)
                 (assoc missmatched (first flag-map)
                        [(second flag-map)
                         ((first flag-map) flags2)])))))))

(deftest applications-defined
  (testing "Constants exist."
    (is @#'kawa.core/ffmpeg)
    (is @#'kawa.core/ffplay)
    (is @#'kawa.core/ffprobe))
  (testing "Constants match format."
    (is (application-format @#'kawa.core/ffmpeg))
    (is (application-format @#'kawa.core/ffplay))
    (is (application-format @#'kawa.core/ffprobe)))
  (testing "Flags are common between applications."
    (is (= (common-flags @#'kawa.core/ffmpeg @#'kawa.core/ffplay) {}))
    (is (= (common-flags @#'kawa.core/ffmpeg @#'kawa.core/ffprobe) {}))
    (is (= (common-flags @#'kawa.core/ffplay @#'kawa.core/ffprobe) {}))))


(deftest formatting
  (testing "format commands based off apps"
    (is (= (@#'kawa.core/fmt-cmd @#'kawa.core/ffplay :disable-audio)
           ["ffplay" "-an"]))
    (is (= (@#'kawa.core/fmt-cmd @#'kawa.core/ffmpeg :this "is" :very 2)
           ["ffmpeg" "-this" "is" "-very" "2"]))))

(deftest ffmpeg
  (testing "Generating a test stream"
    (let [filename "testsrc.mp4"
          wait 10]
      (ffmpeg! :f "lavfi"
               :i "testsrc"
               :duration wait
               :pix_fmt "yuv420p"
               filename))))

;(deftest ffplay
  ;(testing "playing a video"
    ;(is (= "\n"
           ;(ffplay! :autoexit :duration 10 "rtsp://admin:robot@172.28.137.102:554/media/video1")))))
