# Nanakusagayu Framework
[![Build Status](https://travis-ci.org/tamurashingo/NanakusagayuFramework.svg?branch=master)](https://travis-ci.org/tamurashingo/NanakusagayuFramework)
[![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg)](https://opensource.org/licenses/MIT)


## usage

download and install library.
(this library is not available on Maven Centrary Repository)

```sh
git clone https://github.com/tamurashingo/NanakusagayuFramework.git
cd NanakusagayuFramework
mvn install
```

create controller

```java
@Controller("/test")
public class TestController {

    @GET("/say")
    public void hello(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        out.println("<h1>hello world</h1>");
    }
}
```


create entry point

```java
public class Main {
    public static void main(String...args) throws Exception {
        NanakusagayuApplication.run(Main.class, args);
    }
}
```

## Author
- tamura shingo

## Copyright
Copyright (c) 2018 tamura shingo

## LICENSE
License under the MIT License.

