# Nanakusagayu Framework

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

# Author
- tamura shingo

# Copyright
Copyright (c) 2018 tamura shingo

# LICENSE
License under the MIT License.

