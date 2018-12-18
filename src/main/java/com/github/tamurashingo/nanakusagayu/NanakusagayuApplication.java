package com.github.tamurashingo.nanakusagayu;

import com.github.tamurashingo.nanakusagayu.init.Initializer;
import com.github.tamurashingo.nanakusagayu.init.InitializerException;
import com.github.tamurashingo.nanakusagayu.init.Router;
import com.github.tamurashingo.nanakusagayu.init.scanner.ComponentScanner;
import com.github.tamurashingo.nanakusagayu.init.scanner.impl.ControllerScanner;
import org.eclipse.jetty.server.Server;

import java.util.List;

public class NanakusagayuApplication {

    private static ControllerScanner controllerScanner = new ControllerScanner();

    public static void run(Class<?> cls, String[] args) throws Exception {
        Initializer init = new Initializer();

        // クラス一覧の取得
        List<String> classList = init.getClassList(cls.getCanonicalName());
        init(classList);

        startServer();
    }

    private static void init(List<String> classList) throws InitializerException {
        ComponentScanner[] scannerList = new ComponentScanner[] {
                controllerScanner
        };

        try {
            for (String clsName : classList) {
                Class<?> cls = Class.forName(clsName);
                for (ComponentScanner scanner : scannerList) {
                    scanner.componentScan(cls);
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new InitializerException(ex);
        }
    }


    private static void startServer() throws Exception {
        Server server = new Server(3344);
        server.setHandler(new Router(controllerScanner.getRoute()));
        server.start();
        server.join();
    }
}
