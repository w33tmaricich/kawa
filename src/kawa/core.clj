(ns kawa.core
  (:use [clojure.java.shell :only [sh]]))

(def FFMPEG {:cmd "ffmpeg"
             :flags {
                     :audio-frames :aframes
                     :audio-sampling-frequency :ar
                     :audio-quality :aq
                     :audio-sync :async
                     :aspect-ratio :aspect
                     :audio-codec :acodec
                     :audio-sample-format :sample_fmt
                     :audio-filtergraph :af
                     :audio-force-tag :atag
                     :copy-timebase :copytb
                     :calculate-psnr :psnr
                     :duration :t
                     :disable-audio :an
                     :disable-video :vn
                     :data-frames :dframes
                     :dump-payload :hex
                     :disable-subtitles :sn
                     :enable-interaction :stdin
                     :frame-rate :r
                     :frame-size :s
                     :format :f
                     :file-size-limit :fs
                     :hardware-acceleration :hwaccel
                     :hardware-acceleration-device :hwaccel_device
                     :hardware-accelleration-list :hwaccels
                     :intra-dc-precision :dc
                     :initial-demux-decode-delay :muxpreload
                     :input-url :i
                     :input-time-offset :itsoffset
                     :interlacing :ilme
                     :maximum-demux-decode-delay :muxdelay
                     :never-overwrite-output :n
                     :no-filter :dn
                     :no-auto-selection :dn
                     :no-auto-map :dn
                     :overwrite-output :y
                     :preset :pre
                     :print-timestamps :debug_ts
                     :pixel-format :pix_fmt
                     :quality-scale :q
                     :rate-control-override :rc_override
                     :read-at-native-framerate :re
                     :rescale-input-timestamps :itsscale
                     :subtitle-codec :scodec
                     :subtitle-offset :fix_sub_duration
                     :stop-at :to
                     :seek-to :ss
                     :seek-to-from-eof :sseof
                     :show-license :L
                     :show-bitstream-filters :bsfs
                     :swscaler-flags :sws_flags
                     :show-qp-histogram :qphist
                     :subtitle-canvas-size :canvas_size
                     :set-bitstream-filters :bsf
                     :stop-on-error :xerror
                     :video-frames :vframes
                     :video-filtergraph :vf
                     :video-sync :vsync
                     }})

(def FFPLAY {:cmd "ffplay"
             :flags {:disable-audio :an
                     :disable-subtitles :sn
                     :disable-video :vn
                     :duration :t
                     :exit-on-finished :autoexit
                     :force-format :f
                     :force-height :y
                     :force-width :x
                     :fullscreen :fs
                     :input-url :i
                     :no-display :nodisp
                     :seek :ss
                     :window-x-pos :left
                     :window-y-pos :top}})

(def FFPROBE {:cmd "ffprobe"
              :flags {
                      :bitstream-filters :bsfs
                      :force-format :f
                      :input-url :i
                      :license :L
                      :output-format :of
                      :pixel-formats :pix_fmts
                      }})

(defn fmt-cmd
  "Creates a vector that is ready to be passed to sh.
  app: a map with a :cmd and :flags
  argv: any number of arguments to be passed to sh
  
  1. All keywords in argv are converted to a app:flags keyword if one exists.
  2. All keywords are converted to - prefixed strings.
  3. A vector is created with app:cmd + newly formatted args."
  [app & argv]
  (->> argv
       (map #(if (and (keyword? %) ((app :flags) %)) ((app :flags) %) %))
       (map #(if (keyword? %) (str "-" (name %)) (str %)))
       (into [(app :cmd)])))

(defn sh-apply!
  "Takes an application map and a list of arguments. Applies the formatted
  application and arguments to sh."
  [app & args]
  (let [cmd! (apply sh (apply fmt-cmd app args))
        {:keys [exit out err]} cmd!]
    (when-not (zero? exit)
      (throw (Exception. err)))
    out))

(defn ffmpeg! [& args]
  (apply sh-apply! FFMPEG args))

(defn ffplay! [& args]
  (apply sh-apply! FFPLAY args))

(defn ffprobe! [& args]
  (apply sh-apply! FFPROBE args))
