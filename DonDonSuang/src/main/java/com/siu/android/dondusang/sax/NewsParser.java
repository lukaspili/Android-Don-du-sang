package com.siu.android.dondusang.sax;

import android.util.Log;
import com.siu.android.dondusang.dao.model.News;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class NewsParser {

    private NewsHandler entryHandler;
    private XMLReader xmlReader;

    public NewsParser() {

        entryHandler = new NewsHandler();

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            xmlReader = sp.getXMLReader();
            xmlReader.setContentHandler(entryHandler);

        } catch (Exception e) {
            Log.e(getClass().getName(), "Entry parser creation failed", e);
            throw new RuntimeException(e);
        }
    }

    public List<News> parse(String content) {

        if (null == content || content.trim().equals("")) {
            Log.w(getClass().getName(), "Content to parse is empty");
            return null;
        }

        return parse(new InputSource(new StringReader(content)));
    }

    public List<News> parse(InputStream is) {

        if (null == is) {
            return new ArrayList<News>();
        }

        return parse(new InputSource(is));
    }

    public List<News> parse(InputSource inputSource) {
        Log.d(getClass().getName(), "Start parsing...");
        long time = System.currentTimeMillis();

        try {
            xmlReader.parse(inputSource);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Parsing failed", e);
            return null;
        }

        Log.d(getClass().getName(), "Content parsed in " + (System.currentTimeMillis() - time) + " ms");
        return entryHandler.getNewsList();
    }
}
