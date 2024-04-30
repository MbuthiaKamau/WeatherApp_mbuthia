package com.example.weatherapp.XmlParser;

/*
Name: Sumaiya Juma
ID: s2110905
*/

import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlPullParser {
    List<Data> dataList = new ArrayList<Data>();


    public List<Data> getData(){
        return dataList;
    }
    public Data parse(InputStream is, String locations) {
       Data data = null;
       String text = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setFeature(org.xmlpull.v1.XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            org.xmlpull.v1.XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);
            int eventType = parser.getEventType();

            boolean isFirstItemParsed = false;

            while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case org.xmlpull.v1.XmlPullParser.START_TAG:
                        if (tagName != null && tagName.equalsIgnoreCase("item")) {
                            // Only parse the first <item> element
                            if (!isFirstItemParsed) {
                                data = new Data();
                                isFirstItemParsed = true;
                            } else {
                                // If already parsed the first item, return it
                                return data;
                            }
                        }
                        break;
                    case org.xmlpull.v1.XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case org.xmlpull.v1.XmlPullParser.END_TAG:
                        if (tagName != null && data != null) {
                            if (tagName.equalsIgnoreCase("title")) {
                                // Extract condition from the title
                                data.setCondition(text.split(",")[0].split(":")[1]);
                            } else if (tagName.equalsIgnoreCase("point")) {
                                String[] coordinates = text.split(" ");
                                System.out.println(text);
                                    double latitude = Double.parseDouble(coordinates[0]);
                                    double longitude = Double.parseDouble(coordinates[1]);
                                    data.setLatT(latitude);
                                    data.setLongT(longitude);
                                    data.setLocationName(locations);

                            }
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}