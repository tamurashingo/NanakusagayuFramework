package com.github.tamurashingo.nanakusagayu.init.classnamecrawler.impl;

import com.github.tamurashingo.nanakusagayu.init.InitializerException;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.AbstractClassnameCrawler;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassnameCrawlerFromFile extends AbstractClassnameCrawler {

    /** 基準クラスが所属するパッケージをディレクトリ名にしたもの */
    private String basePackageDir;

    public ClassnameCrawlerFromFile(Class<?> baseClass, URL baseUrl) {
        super(baseClass, baseUrl);
        this.basePackageDir = baseClass.getPackage().getName().replace(".", "/");
    }

    /**
     * ファイル名の拡張子が .class であるかをチェックする
     */
    BiPredicate<Path, BasicFileAttributes> isClassfile = (path, attr) -> path.toFile().getName().endsWith(".class");
    /**
     * 基準クラスが所属するパッケージのディレクトリと同じかどうかをチェックする
     * (基準クラスの所属するパッケージ配下かどうかをチェックする)
     */
    Predicate<Path> hasPackage = path -> path.toString().indexOf(basePackageDir) != -1;

    /**
     * Path(com/github/xxxx/xxx/XXXX.class)をクラス名(com.github.xxxx.xxx.XXXX)に変換する
     */
    Function<Path, String> convertFileToClassname = path -> {
        String p = path.toString();
        // /path/to/classdir/com/github/xxxx/XXXX.class -> com/github/xxxx/XXXX.class
        p = p.substring(p.indexOf(basePackageDir));
        // com/github/xxxx/XXXX.class -> com/github/xxxx/XXXX
        p = p.substring(0, p.lastIndexOf(".class"));
        // com/github/xxxx/XXXX -> com.github.xxxx.XXXX
        return p.replace("/", ".");
    };

    @Override
    public List<String> getClassnameList() throws InitializerException {

        try {
            Path path = Paths.get(baseUrl.toURI()).getParent();
            return Files.find(path, Integer.MAX_VALUE, isClassfile)
                    .filter(hasPackage)
                    .map(convertFileToClassname)
                    .collect(Collectors.toList());
        } catch (IOException | java.net.URISyntaxException ex) {
            throw new InitializerException("ファイル読み込みエラー", ex);
        }
    }
}
