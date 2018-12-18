package com.github.tamurashingo.nanakusagayu.init.classnamecrawler;

import com.github.tamurashingo.nanakusagayu.init.InitializerException;

import java.net.URL;
import java.util.List;

/**
 * 基準クラス配下のクラス一覧を取得する。
 */
public abstract class AbstractClassnameCrawler {

    /**
     * 基準クラス。
     * このクラスが格納されている場所から下をスキャン対象とする。
     */
    protected Class<?> baseClass;
    /**
     * 基準クラスが格納されている場所。
     */
    protected URL baseUrl;

    /**
     * コンストラクタ
     * @param baseClass 基準クラス
     * @param baseUrl 基準クラスの場所
     */
    public AbstractClassnameCrawler(Class<?> baseClass, URL baseUrl) {
        this.baseClass = baseClass;
        this.baseUrl = baseUrl;
    }

    /**
     * 基準クラス配下のパッケージに所属するクラス一覧を取得する。
     *
     * @return クラス名のリスト
     * @throws InitializerException クラスのスキャンに失敗した場合
     */
    abstract public List<String> getClassnameList() throws InitializerException;

}
