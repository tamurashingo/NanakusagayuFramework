package com.github.tamurashingo.nanakusagayu.init.classnamecrawler.impl;

import com.github.tamurashingo.nanakusagayu.init.InitializerException;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.AbstractClassnameCrawler;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassnameCrawlerFromJar extends AbstractClassnameCrawler {

    /** 基準クラスが所属するパッケージ */
    private String basePackageName;

    public ClassnameCrawlerFromJar(Class<?> baseClass, URL baseUrl) {
        super(baseClass, baseUrl);
        this.basePackageName = baseClass.getPackage().getName();
    }

    /**
     * ファイルかつ拡張子が .class かどうか
     */
    Predicate<JarEntry> isClassfile = jarFile -> !jarFile.isDirectory() && jarFile.getName().endsWith(".class");

    /**
     * 基準クラスが所属するパッケージ（配下）かどうか
     */
    Predicate<JarEntry> hasPackage = jarFile -> jarFile.getName().replace("/", ".").startsWith(basePackageName);

    /**
     * JarEntry(com/github/xxxx/xxx/XXXX.class)をクラス名(com.github.xxxx.xxx.XXXX)に変換する
     */
    Function<JarEntry, String> convertFilename = jarFile -> {
        String filename = jarFile.getName();
        // com/github/xxxx/xxx/XXXX.class -> com/github/xxxx/xxx/XXXX
        filename = filename.substring(0, filename.lastIndexOf(".class"));
        // com/github/xxxx/xxx/XXXX -> com.github.xxxx.xxx.XXXX
        return filename.replace("/", ".");
    };

    @Override
    public List<String> getClassnameList() throws InitializerException {
        String path = baseUrl.getPath(); // file:/path/to/jarfile!/path/to/class
        String jarPath = path.substring(5, path.indexOf("!")); // /path/to/jarfile
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
            Enumeration<JarEntry> entries = jar.entries();
            return Collections.list(entries).stream()
                    .filter(isClassfile)
                    .filter(hasPackage)
                    .map(convertFilename)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new InitializerException("ファイル読み込みエラー:" + jarPath, ex);
        }
    }
}
