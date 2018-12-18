package com.github.tamurashingo.nanakusagayu.init;

import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.AbstractClassnameCrawler;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.ClassnameCrawlerFactory;

import java.net.URL;
import java.util.List;

/**
 * 基準となるクラスが属するパッケージ配下のクラス一覧を取得する
 *
 */
public class Initializer {

    /**
     * 基準となるクラスを指定して、クラス一覧を取得する。
     *
     * @param className 基準となるクラス名
     * @return クラス一覧
     * @throws InitializerException クラス一覧の取得に失敗
     */
    public List<String> getClassList(String className) throws InitializerException {
        return getClassnames(className);
    }

    /**
     * クラス情報から
     * @param cls
     * @return
     */
    private String getPathnameFromClass(Class<?> cls) {
        return cls.getCanonicalName().replace(".", "/") + ".class";
    }

    private List<String> getClassnames(String className) throws InitializerException {
        try {
            // クラス名からクラスがある場所を取得する
            Class<?> baseClass = Class.forName(className);
            ClassLoader cl = baseClass.getClassLoader();
            String pathname = getPathnameFromClass(baseClass);
            URL url = cl.getResource(pathname);

            if (url == null) {
                throw new InitializerException("not found class:" + baseClass.getCanonicalName());
            }

            // クラスとその場所からクラス名クローラを取得する
            AbstractClassnameCrawler parser = ClassnameCrawlerFactory.create(baseClass, url);
            // クラス名の一覧を取得する
            return parser.getClassnameList();
        } catch (ClassNotFoundException ex) {
            throw new InitializerException("初期化失敗", ex);
        }
    }
}
