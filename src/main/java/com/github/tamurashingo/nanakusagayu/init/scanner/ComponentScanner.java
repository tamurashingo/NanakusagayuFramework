package com.github.tamurashingo.nanakusagayu.init.scanner;

import com.github.tamurashingo.nanakusagayu.init.InitializerException;

public interface ComponentScanner {

    void componentScan(Class<?> cls) throws InitializerException;

}
