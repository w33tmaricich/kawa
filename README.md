# 川 - kawa
[![Clojars Project](https://img.shields.io/clojars/v/w33t/kawa.svg)](https://clojars.org/w33t/kawa)

A clojure wrapper around ffmpeg command line tools.

## The goal.

ffmpeg is an awesome tool. Lets stop just using `sh` to control it. How
about we write a robust system that can:

 - Control the many tools included in ffmpeg's ecosystem.
 - Keep track of what processes we have spun up.
 - Navigate ffmpeg's flag hell with readable sane flags.

## Installation

This is a wrapper library. Make sure you have ffmpeg installed on your system
and runnable through your system path.

[![version](https://clojars.org/w33t/kawa/latest-version.svg)](https://clojars.org/w33t/kawa)

## Usage

### Quick start

Kawa provides both ffmpeg command line tool wrappers as well as a process
manager.

To include the command line tools:
```clojure
kawa.core=> (require '[kawa.core :refer [ffmpeg! ffplay! ffprobe!]])
;nil
```

To include the process manager:
```clojure
kawa.core=> (require `[kawa.manager :as manager])
;nil
```

`ffmpeg!`, `ffplay!`, and `ffprobe!` will all launch an instance of each
application. As a simple example, lets generate a test video and store it
in a file on your system.
```clojure
kawa.core=> (ffmpeg! :f "lavfi" :i "testsrc" :t 10 :pix_fmt "yuv420p" "testsrc.mp4")
;{:cmd ["ffmpeg" "-f" "lavfi" "-i" "testsrc" "-t" "10" "-pix_fmt" "yuv460p" "testsrc.mp4"], :process {:out #object[java.lang.UNIXProcess$ProcessPipeInputStream 0x33b2f029 "java.lang.UNIXProcess$ProcessPipeInputStream@33b2f029"], :in #object[java.lang.UNIXProcess$ProcessPipeOutputStream 0x134ec85c "java.lang.UNIXProcess$ProcessPipeOutputStream@134ec85c"], :err #object[java.lang.UNIXProcess$ProcessPipeInputStream 0x375941a4 "java.lang.UNIXProcess$ProcessPipeInputStream@375941a4"], :process #object[java.lang.UNIXProcess 0x3c319941 "java.lang.UNIXProcess@3c319941"]}}
```
The command returns a map that contains `:cmd` which holds the exact command
run. It also contains `:process` which holds the return value of
[conch's sh/proc implementation](https://github.com/Raynes/conch). The `:process`
value is used by the manager to kill running processes.

As you can see, flags are represented by keywords. There are many quality of
life keywords that improve the readability of your code. For example, the below
code does the same as above.
```clojure
kawa.core=> (ffmpeg! :format "lavfi" :input-url "testsrc" :duration 10
                     :pixel-format "yuv420p" "testsrc.mp4")
```

### Process manager
To check the currently registered processes, you can run:
```clojure
kawa.core=> (manager/ls)
;{}
```

The manager is using a namespaced `(atom {})` to store information registered.
You may find yourself in a situation where you wish to have multiple registries.
If this is the case, all 3 kawa.manager functions have an optional first
parameter that takes an `atom`.
```clojure
kawa.core=> (def my-registry (atom {}))
; #'kawa.core/my-registry
kawa.core=> (manager/ls my-registry)
;{}
kawa.core=> (manager/ls)
;{:test {:cmd ["ffmpeg" "-i" "rtsp://admin:robot@172.28.137.102:554/media/video1" "-t" "100" "-pix_fmt" "yuv420p" "testsrc.mp4"], :process {:out #object[java.lang.UNIXProcess$ProcessPipeInputStream 0x53d266d "java.lang.UNIXProcess$ProcessPipeInputStream@53d266d"], :in #object[java.lang.UNIXProcess$ProcessPipeOutputStream 0x5eba57f5 "java.lang.UNIXProcess$ProcessPipeOutputStream@5eba57f5"], :err #object[java.lang.UNIXProcess$ProcessPipeInputStream 0x40de6630 "java.lang.UNIXProcess$ProcessPipeInputStream@40de6630"], :process #object[java.lang.UNIXProcess 0x538f1277 "java.lang.UNIXProcess@538f1277"]}}}
```

In this example, we have no processes registered yet. Lets spin up an ffmpeg
process and register it in one go.

```clojure
kawa.core=> (manager/register :test (ffmpeg! :i "rtsp://admin:robot@172.28.137.102:554/media/video1" :duration 100 :pix_fmt "yuv420p" "testsrc.mp4"))
; :test
```

You can then use `ls` to ensure the process has been successfully registered.
If you do not provide an id to register it as, a unique id will be
generated for you. `register` returns the ID of process you just registered.

To kill the process that has been registered, simply run:
```clojure
kawa.core=> (manager/kill :test)
;Stopping :test
;#future[{:status :ready, :val 0} 0x7549316]
```
`kill` returns a future whose value is the exit value that is returned from the
shell.


### Detailed Docs

[Detailed documentation can be found here](doc)

## License

Copyright © 2019 Alexander Maricich

Distributed under the Eclipse Public License version 1.0
