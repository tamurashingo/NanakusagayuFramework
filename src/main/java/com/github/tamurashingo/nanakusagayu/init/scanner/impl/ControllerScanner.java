package com.github.tamurashingo.nanakusagayu.init.scanner.impl;

import com.github.tamurashingo.nanakusagayu.annotation.Controller;
import com.github.tamurashingo.nanakusagayu.annotation.GET;
import com.github.tamurashingo.nanakusagayu.init.InitializerException;
import com.github.tamurashingo.nanakusagayu.init.scanner.ComponentScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerScanner implements ComponentScanner {

    /**
     * ルーティグ情報
     * String: /path
     * Object[0]: Controller instance
     * Object[1]: method instance
     */
    private Map<String, Object[]> pathMethodMap = new HashMap<>();

    public Map<String, Object[]> getRoute() {
        return this.pathMethodMap;
    }

    @Override
    public void componentScan(Class<?> cls) throws InitializerException {
        Controller controller = cls.getAnnotation(Controller.class);
        if (controller == null) {
            return;
        } else {
            createController(cls, controller);
        }
    }

    /**
     * ルーティング情報を作成する
     *
     * @param cls
     * @param controller
     * @param <T>
     * @throws InitializerException
     */
    private <T> void createController(Class<?> cls, Controller controller) throws InitializerException {
        T inst = createInst(cls);
        getPathAndMethod(inst, controller.value());
    }

    /**
     * クラスをインスタンス化する。
     * (今は)デフォルトコンストラクタのみ対応。
     *
     * @param cls クラス情報
     * @param <T> ダミーパラメータ
     * @return インスタンス
     * @throws InitializerException インスタンス化に失敗
     */
    private <T> T createInst(Class<?> cls) throws InitializerException {
        try {
            return (T)cls.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new InitializerException("コントローラ作成に失敗:" + cls.getCanonicalName(), ex);
        }
    }

    /**
     * GETメソッドを取得し、ルーティングを作成する
     * @param inst     Controllerのインスタンス
     * @param basePath Controllerで定義したパス
     * @param <T>      ダミー情報
     * @throws InitializerException
     */
    private <T> void getPathAndMethod(T inst, final String basePath) throws InitializerException {
        Class<?> cls = inst.getClass();

        for (Method method: cls.getDeclaredMethods()) {
            GET get = method.getAnnotation(GET.class);
            if (get == null) {
                continue;
            }
            String path = get.value();

            StringBuilder buf = new StringBuilder();
            if (basePath.isEmpty()) {
                if (path.isEmpty()) {
                    buf.append("/");
                } else if (!path.startsWith("/")) {
                    buf.append("/").append(path);
                } else {
                    buf.append(path);
                }

            } else {
                if (!basePath.startsWith("/")) {
                    buf.append("/");
                }
                buf.append(basePath);
                if (!path.isEmpty()) {
                    if (!path.startsWith("/")) {
                        buf.append("/");
                    }
                    buf.append(path);
                }
            }

            pathMethodMap.put(buf.toString(), new Object[]{ inst, method });
        }
    }
}
