package com.example.weatherapp.XmlParser;

/*Name:     ID: */
public class WeatherList {
    //variables
    private String date;
    private String minTemp;
    private String maxTemp;
    private String humidity;
    private String rain;
    private String wind;
    private String day;
    private String pressure;

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    private String sunrise;
    private String sunset;

    public String getVisibility() {
        return Visibility;
    }

    public void setVisibility(String visibility) {
        Visibility = visibility;
    }

    public String getPollution() {
        return Pollution;
    }

    public void setPollution(String pollution) {
        Pollution = pollution;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    public String getUvRisk() {
        return UvRisk;
    }

    public void setUvRisk(String uvRisk) {
        UvRisk = uvRisk;
    }

    private String Visibility;
    private String Pollution;
    private String WindDirection;

    private String UvRisk;
    private String condition;

    public String getDate() {
        return date;
    }
    public String getSunset(){return  sunset;}

    public String getSunrise(){return  sunrise;}

    public void setSunrise(String sunrise){ this.sunrise = sunrise;}
    public void setSunset(String sunset){this.sunset = sunset;}

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinimumTemperature() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaximumTemparature() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}