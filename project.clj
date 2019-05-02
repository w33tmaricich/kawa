(defproject w33t/kawa "0.1.1"
  :description "Wrapper for ffmpeg command line tools."
  :url "https://github.com/w33tmaricich/kawa/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[me.raynes/conch "0.8.0"]
                 [org.clojure/clojure "1.10.0"]]
  :repl-options {:init-ns kawa.core})
