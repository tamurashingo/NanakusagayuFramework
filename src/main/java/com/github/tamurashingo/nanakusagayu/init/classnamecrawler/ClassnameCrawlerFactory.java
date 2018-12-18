package com.github.tamurashingo.nanakusagayu.init.classnamecrawler;

import com.github.tamurashingo.nanakusagayu.init.InitializerException;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.impl.ClassnameCrawlerFromFile;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.impl.ClassnameCrawlerFromJar;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * クラスとそのクラスの場所からクローラを得る
 */
public class ClassnameCrawlerFactory {

    /**
     * protocol名からクラスを得る
     *
     * @param protocol プロトコル名
     * @return クラス
     * @throws InitializerException プロトコル名がfile、jar以外
     */
    private static Class<? extends AbstractClassnameCrawler> getClassnameParser(String protocol) throws InitializerException {
        if (protocol.equals("file")) {
            return ClassnameCrawlerFromFile.class;
        } else if (protocol.equals("jar")) {
            return ClassnameCrawlerFromJar.class;
        } else {
            throw new InitializerException("unsupported type:" + protocol);
        }
    }

    /**
     * クローラをインスタンス化する
     *
     * @param baseClass 基準クラス
     * @param url 基準クラスの場所
     * @return クローラ
     * @throws InitializerException クローラの取得に失敗
     */
    public static AbstractClassnameCrawler create(Class<?> baseClass, URL url) throws InitializerException {
        Class<? extends AbstractClassnameCrawler> parserClass = getClassnameParser(url.getProtocol());
        try {
            return parserClass.getConstructor(Class.class, URL.class).newInstance(baseClass, url);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            throw new InitializerException(ex);
        }
    }
}
