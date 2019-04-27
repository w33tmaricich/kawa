# 川 - kawa

A clojure wrapper around ffmpeg command line tools.

## Installation

This is a wrapper library. Make sure you have ffmpeg installed on your system
and runnable through your system path.

I will be adding the project to [Clojars](http://clojars.org/) once I get it
to my expectations for version 0.1.0. Until then,
you will just have to download the source, run a ```lein install``` and include
it in your project.clj as you see it below.

```clojure
[kawa "0.0.1"]
```

## The goal.

ffmpeg is an awesome tool. Video streaming solutions is what we do. Lets stop
just using ```sh``` to control it. How about we write a robust system that
can:

 - Control the many tools included in ffmpeg's echosystem.
 - Keep track of what processes we have spun up.
 - Navigate ffmpeg's flag hell with readable sane flags.

## Documentation

### Quick start

TODO: add quickstart documentation.

### Detailed Docs

[Detailed documentation can be found here](doc) 

## License

Copyright © 2019 Alexander Maricich

Distributed under the Eclipse Public License version 1.0
