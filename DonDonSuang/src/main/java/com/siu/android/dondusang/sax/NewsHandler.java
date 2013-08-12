package com.siu.android.dondusang.sax;

import android.util.Log;
import com.siu.android.dondusang.dao.model.News;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class NewsHandler extends DefaultHandler {

    private List<News> newsList = new ArrayList<News>();
    private News news;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    private boolean inItem, inTitle, inContent, inDate;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("item")) {
            inItem = true;
            news = new News();
        } else if (localName.equals("title")) {
            inTitle = true;
        } else if (localName.equals("encoded")) {
            inContent = true;
        } else if (localName.equals("pubDate")) {
            inDate = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!inItem) {
            return;
        }

        String value = new String(ch, start, length).trim();

        if (inTitle) {
            if (null != news.getTitle()) {
                value = news.getTitle() + value;
            }
            news.setTitle(value);

        } else if (inContent) {
            if (null != news.getContent()) {
                value = news.getContent() + value;
            }
            news.setContent(value);

        } else if (inDate) {
            try {
                news.setDate(dateFormat.parse(value));
            } catch (Exception e) {
                Log.e(getClass().getName(), "Invalid date format : " + value, e);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("item")) {
            inItem = false;
            newsList.add(news);
        } else if (localName.equals("title")) {
            inTitle = false;
        } else if (localName.equals("encoded")) {
            inContent = false;
        } else if (localName.equals("pubDate")) {
            inDate = false;
        }
    }

    public List<News> getNewsList() {
        return newsList;
    }
}
