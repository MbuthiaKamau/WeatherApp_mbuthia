package com.example.weatherapp.XmlParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlPullParserHandler {
    List<WeatherList> weatherLists = new ArrayList<WeatherList>();

    public List<WeatherList> getData(){
        return weatherLists;
    }
    public List<WeatherList> parse(InputStream is) {
        List<WeatherList> dataList = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);
            int eventType = parser.getEventType();

            WeatherList data = null;
            String text = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName != null && tagName.equalsIgnoreCase("item")) {
                            data = new WeatherList();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName != null) {
                            if (tagName.equalsIgnoreCase("item") && data != null) {
                                dataList.add(data);
                            } else if (tagName.equalsIgnoreCase("title") && data != null) {
                                data.setDay(text.split(",")[0].split(":")[0]);
                                data.setCondition(text.split(",")[0].split(":")[1]);
                            } else if (tagName.equalsIgnoreCase("description") && data != null) {
                                // Define regular expressions to match the desired data
                                String maxTempRegex = "Maximum Temperature: (\\d+°?C)";
                                String minTempRegex = "Minimum Temperature: (\\d+°?C)";
                                String windRegex = "Wind Speed: (\\d+mph)";
                                String windDirectionRegex = "Wind Direction: ([\\p{L} ]+)";
                                String pressureRegex = "Pressure: (\\d+mb)";
                                String humidityRegex = "Humidity: (\\d+%)";
                                String sunriseRegex = "Sunrise: (\\d+:\\d+ \\w+)";
                                String sunsetRegex = "Sunset: (\\d+:\\d+ \\w+)";
                                String uvriskRegex = "UV Risk: (\\d)";
                                String visibilityRegex = "Visibility: ([\\p{L} ]+)";
                                String pollutionRegex = "Pollution: ([\\p{L}]+)";

                                // Compile the regular expressions
                                Pattern maxTempPattern = Pattern.compile(maxTempRegex);
                                Pattern minTempPattern = Pattern.compile(minTempRegex);
                                Pattern windPattern = Pattern.compile(windRegex);
                                Pattern windDirectionPattern = Pattern.compile(windDirectionRegex);
                                Pattern pressurePattern = Pattern.compile(pressureRegex);
                                Pattern humidityPattern = Pattern.compile(humidityRegex);
                                Pattern sunrisePattern = Pattern.compile(sunriseRegex);
                                Pattern sunsetPattern = Pattern.compile(sunsetRegex);
                                Pattern uvriskPattern = Pattern.compile(uvriskRegex);
                                Pattern visibilityPattern = Pattern.compile(visibilityRegex);
                                Pattern pollutionPattern = Pattern.compile(pollutionRegex);

                                // Match the patterns against the description text
                                Matcher maxTempMatcher = maxTempPattern.matcher(text);
                                Matcher minTempMatcher = minTempPattern.matcher(text);
                                Matcher windMatcher = windPattern.matcher(text);
                                Matcher windDirectionMatcher = windDirectionPattern.matcher(text);
                                Matcher pressureMatcher = pressurePattern.matcher(text);
                                Matcher humidityMatcher = humidityPattern.matcher(text);
                                Matcher sunriseMatcher = sunrisePattern.matcher(text);
                                Matcher sunsetMatcher = sunsetPattern.matcher(text);
                                Matcher uvriskMatcher = uvriskPattern.matcher(text);
                                Matcher visibilityMatcher = visibilityPattern.matcher(text);
                                Matcher pollutionMatcher = pollutionPattern.matcher(text);

                                // Extract data based on matches
                                if (maxTempMatcher.find()) {
                                    String maxTemp = maxTempMatcher.group(1);
                                    data.setMaxTemp(maxTemp);
                                }

                                if (minTempMatcher.find()) {
                                    String minTemp = minTempMatcher.group(1);
                                    data.setMinTemp(minTemp);
                                }

                                if (windMatcher.find()) {
                                    String wind = windMatcher.group(1);
                                    data.setWind(wind);
                                }

                                if (windDirectionMatcher.find()) {
                                    String windDirection = windDirectionMatcher.group(1);
                                    data.setWindDirection(windDirection);
                                }

                                if (pressureMatcher.find()) {
                                    String pressure = pressureMatcher.group(1);
                                    data.setPressure(pressure);
                                }

                                if (humidityMatcher.find()) {
                                    String humidity = humidityMatcher.group(1);
                                    data.setHumidity(humidity);
                                }

                                if (sunriseMatcher.find()) {
                                    String sunrise = sunriseMatcher.group(1);
                                    data.setSunrise(sunrise);
                                }

                                if (sunsetMatcher.find()) {
                                    String sunset = sunsetMatcher.group(1);
                                    data.setSunset(sunset);
                                }

                                if (uvriskMatcher.find()) {
                                    String uvrisk = uvriskMatcher.group(1);
                                    data.setUvRisk(uvrisk);
                                }

                                if (visibilityMatcher.find()) {
                                    String visibility = visibilityMatcher.group(1);
                                    data.setVisibility(visibility);
                                }

                                if (pollutionMatcher.find()) {
                                    String pollution = pollutionMatcher.group(1);
                                    data.setPollution(pollution);
                                }

                            }
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

}