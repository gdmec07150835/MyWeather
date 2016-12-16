package cn.edu.gdmec.w07150837.weather;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weiruibo on 12/15/16.
 */

public class SaxHandler extends DefaultHandler {

    private Map<String, List<String>> cityMap = new HashMap<String, List<String>>();

    String cityName = "";
    String provinceName = "";

    List<String> cityList = new ArrayList<>();
    List<String> proList = new ArrayList<>();


    public Map<String, List<String>> getCityMap() {
        return cityMap;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if ("Province".equals(qName)) {
            provinceName = attributes.getValue("name");
            cityMap.put(provinceName, new ArrayList<String>());
        } else if ("City".equals(qName)) {
            cityName = attributes.getValue("name");
        }
        Log.d("ttt", provinceName);
        Log.d("ttt", cityName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        //Log.d("test",uri);
        //Log.d("test",localName);
        //Log.d("test",qName);


        if ("City".equals(qName)) {
            // Log.d("test2",cityMap.get(provinceName).add(cityName)+"");
            // Log.d("test3",provinceName);

            //Log.d("test2",cityName);

//            cityMap.put(provinceName, proList);
//            cityMap.put(cityName, cityList);
            cityMap.get(provinceName).add(cityName);
            // cityMap.add(cityName);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
