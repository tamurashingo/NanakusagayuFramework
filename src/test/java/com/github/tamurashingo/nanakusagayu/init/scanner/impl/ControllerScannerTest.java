package com.github.tamurashingo.nanakusagayu.init.scanner.impl;

import com.github.tamurashingo.nanakusagayu.annotation.Controller;
import com.github.tamurashingo.nanakusagayu.annotation.GET;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class ControllerScannerTest {

    /**
     * パスを指定しないコントローラは / となる
     */
    @Controller
    public static class TestController {
        /**
         * パスを指定していないメソッドはコントローラの定義を引き継ぐ
         */
        @GET
        public String getMethod() {
            return "method";
        }
    }

    /**
     * 基本的な確認
     */
    @Test
    public void test() throws Exception {
        ControllerScanner scanner = new ControllerScanner();
        scanner.componentScan(ControllerScannerTest.TestController.class);
        Map<String, Object[]> routing = scanner.getRoute();

        /* ルーティングが登録されていること */
        assertThat(routing.get("/")).isNotEmpty();

        /* インスタンス化したクラスがscanしたクラスであること */
        assertThat(routing.get("/")[0].getClass().getCanonicalName()).isEqualTo("com.github.tamurashingo.nanakusagayu.init.scanner.impl.ControllerScannerTest.TestController");

        /* メソッド名が@GETで指定したメソッドであること */
        assertThat((String)routing.get("/")[1]).isEqualTo("getMethod");

        /* 実行結果が定義したメソッドの戻りであること */
        Object inst = routing.get("/")[0];
        Method method = inst.getClass().getMethod((String)routing.get("/")[1]);
        String result = (String)method.invoke(inst);

        assertThat(result).isEqualTo("method");
    }

    /**
     * パスを指定しないコントローラは / となる
     */
    @Controller
    public static class TestControllerWithMethod {
        /**
         * パスを指定したメソッド
         */
        @GET("/getpath")
        public String getMethod() {
            return "method";
        }
    }

    @Test
    public void testWithPath() throws Exception {
        ControllerScanner scanner = new ControllerScanner();
        scanner.componentScan(com.github.tamurashingo.nanakusagayu.init.scanner.impl.ControllerScannerTest.TestControllerWithMethod.class);
        Map<String, Object[]> routing = scanner.getRoute();

        /* ルーティングが登録されていること */
        assertThat(routing.get("/getpath")).isNotEmpty();

        /* インスタンス化したクラスがscanしたクラスであること */
        assertThat(routing.get("/getpath")[0].getClass().getCanonicalName()).isEqualTo("com.github.tamurashingo.nanakusagayu.init.scanner.impl.ControllerScannerTest.TestControllerWithMethod");

        /* メソッド名が@GETで指定したメソッドであること */
        assertThat(((Method)(routing.get("/getpath")[1])).getName()).isEqualTo("getMethod");
    }

    /**
     * パスを指定したコントローラ
     */
    @Controller("/controllerpath")
    public static class TestControllerWithController {
        /**
         * パスを指定していないメソッドはコントローラの定義を引き継ぐ
         */
        @GET()
        public String getMethod() {
            return "method";
        }
    }

    @Test
    public void testWithController() throws Exception {
        ControllerScanner scanner = new ControllerScanner();
        scanner.componentScan(ControllerScannerTest.TestControllerWithController.class);
        Map<String, Object[]> routing = scanner.getRoute();

        /* ルーティングが登録されていること */
        assertThat(routing.get("/controllerpath")).isNotEmpty();

        /* インスタンス化したクラスがscanしたクラスであること */
        assertThat(routing.get("/controllerpath")[0].getClass().getCanonicalName()).isEqualTo("com.github.tamurashingo.nanakusagayu.init.scanner.impl.ControllerScannerTest.TestControllerWithController");

        /* メソッド名が@GETで指定したメソッドであること */
        assertThat(((Method)routing.get("/controllerpath")[1]).getName()).isEqualTo("getMethod");
    }

    /**
     * パスを指定したコントローラ
     */
    @Controller("/controllerpath")
    public static class TestControllerWithControllerAndMethod {
        /**
         * パスを指定したメソッド
         */
        @GET("method")
        public String getMethod() {
            return "method";
        }
    }

    @Test
    public void testWithControllerPath() throws Exception {
        ControllerScanner scanner = new ControllerScanner();
        scanner.componentScan(ControllerScannerTest.TestControllerWithControllerAndMethod.class);
        Map<String, Object[]> routing = scanner.getRoute();

        /* ルーティングが登録されていること */
        assertThat(routing.get("/controllerpath/method")).isNotEmpty();

        /* インスタンス化したクラスがscanしたクラスであること */
        assertThat(routing.get("/controllerpath/method")[0].getClass().getCanonicalName()).isEqualTo("com.github.tamurashingo.nanakusagayu.init.scanner.impl.ControllerScannerTest.TestControllerWithControllerAndMethod");

        /* メソッド名が@GETで指定したメソッドであること */
        assertThat(((Method)routing.get("/controllerpath/method")[1]).getName()).isEqualTo("getMethod");
    }

    /**
     * パスを指定したコントローラ
     */
    @Controller("/controller")
    static class TestControllerMethod2 {
        @GET("/method1")
        public String getMethod1() {
            return "method1";
        }
        @GET("/method2")
        public String getMethod2() {
            return "method2";
        }
    }

    /**
     * GETが２つ以上あった場合の動き
     */
    @Test
    public void testMethod2() throws Exception {
        ControllerScanner scanner = new ControllerScanner();
        scanner.componentScan(ControllerScannerTest.TestControllerMethod2.class);
        Map<String, Object[]> routing = scanner.getRoute();

        /* ルーティングが登録されていること */
        assertThat(routing.get("/controller/method1")).isNotEmpty();
        /* ルーティングが登録されていること */
        assertThat(routing.get("/controller/method2")).isNotEmpty();

    }

}
