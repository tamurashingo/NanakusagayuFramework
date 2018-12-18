package com.github.tamurashingo.nanakusagayu.init.classnamecrawler;

import com.github.tamurashingo.nanakusagayu.init.InitializerException;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.impl.ClassnameCrawlerFromFile;
import com.github.tamurashingo.nanakusagayu.init.classnamecrawler.impl.ClassnameCrawlerFromJar;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.net.URL;

public class ClassnameCrawlerFactoryTest {

    @Test
    public void testFile() throws Exception {
        AbstractClassnameCrawler crawler = ClassnameCrawlerFactory.create(this.getClass(), new URL("file://test"));
        assertThat(crawler).isInstanceOf(ClassnameCrawlerFromFile.class);
    }

    @Test
    public void testJar() throws Exception {
        AbstractClassnameCrawler crawler = ClassnameCrawlerFactory.create(this.getClass(), new URL("jar:file://test.jar!/"));
        assertThat(crawler).isInstanceOf(ClassnameCrawlerFromJar.class);
    }

    @Test
    public void testUnsupport() throws Exception {
        assertThatThrownBy(() -> ClassnameCrawlerFactory.create(this.getClass(), new URL("http://www.example.com"))).isInstanceOf(InitializerException.class);
    }
}
