package com.example.weatherapp.Activitis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.weatherapp.InternetConnectivityChecker;
import com.example.weatherapp.MapsActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.XmlParser.WeatherList;
import com.example.weatherapp.XmlParser.XmlPullParserHandler;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.google.android.gms.maps.MapFragment;
import com.jakewharton.threetenabp.AndroidThreeTen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{

    private String xmlresult;
    private String activePlace;
    private String url1="";
    private WeatherList homeData;
    private ImageView homeWeatherImg;
    private TextView visibility;
    private TextView sunset;
    private  TextView sunrise;
    private  TextView pollution;
    private TextView Uvrisk;
    private TextView pressure;
    private TextView homeWeatherTxt;
    private TextView homeDate;
    private TextView homeDayTxt;
    private TextView homeTemparatureTxt;
    private TextView homeBothTempTxt;
    private TextView homeRainTxt;
    private TextView homeWindTxt;
    private TextView homeHumidityTxt;
    private TextView winddirection;
    public static List<WeatherList> weatherLists;

    public static String activeCity;
    private TextView next;
    private ArrayAdapter<String> adapter;

    private AutoCompleteTextView autoCompleteTextView;
    private static final Map<String, ZoneId> ZONE_ID_HASH_MAP = new HashMap<>();
    HashMap<String, String> stringCityIdMap = new HashMap<>();

    private String stringCityID;
    private int index = 0;
    List<String> cities ;
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643123";
    private MapFragment mapFragment;
    private boolean isMapVisible = false;
    private RecyclerView recyclerView;
    private ActivityMainBinding binding;
    private static final int MAX_INDEX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //A List of city and city codes
        stringCityIdMap.put("glasgow", "2648579");
        stringCityIdMap.put("london", "2643743");
        stringCityIdMap.put("newyork", "5128581");
        stringCityIdMap.put("oman", "287286");
        stringCityIdMap.put("mauritius", "934154");
        stringCityIdMap.put("bangladesh", "1185241");

        //List of all zone id
        ZONE_ID_HASH_MAP.put("london", ZoneId.of("Europe/London"));
        ZONE_ID_HASH_MAP.put("oman", ZoneId.of("Asia/Muscat"));
        ZONE_ID_HASH_MAP.put("bangladesh", ZoneId.of("Asia/Dhaka"));
        ZONE_ID_HASH_MAP.put("mauritius", ZoneId.of("Indian/Mauritius"));
        ZONE_ID_HASH_MAP.put("glasgow", ZoneId.of("Europe/London")); // Assuming same as London
        ZONE_ID_HASH_MAP.put("new york", ZoneId.of("America/New_York"));


        // Set up the raw links to the graphical components

        homeWeatherImg = findViewById(R.id.homeWeatherImg);
        homeDayTxt = findViewById(R.id.viewDay);
        homeDate = findViewById(R.id.homeDate);
        homeWeatherTxt = findViewById(R.id.homeWeatherTxt);
        homeTemparatureTxt = findViewById(R.id.homeTemparatureTxt);
        homeBothTempTxt = findViewById(R.id.homeBothTempTxt);
      //  homeRainTxt = findViewById(R.id.homeRainTxt);
        homeWindTxt = findViewById(R.id.homeWindTxt);
        homeHumidityTxt = findViewById(R.id.homeHumidityTxt);
        autoCompleteTextView= findViewById(R.id.auto_complete_text);
        visibility = findViewById(R.id.visibility);
        Uvrisk = findViewById(R.id.UvRisk);
        pollution = findViewById(R.id.pollution);
        pressure = findViewById(R.id.pressure);
        winddirection = findViewById(R.id.winddirection);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);

        startProgress("2648579");
        activePlace = "glasgow";

        // Extract city names from the map
         cities = new ArrayList<>(stringCityIdMap.keySet());


         //Calander data

        CalendarView calendarView = findViewById(R.id.calendarView);

      
        // Disable dates outside the range of today, tomorrow, and the day after tomorrow
        Calendar minDate = Calendar.getInstance();
        minDate.setTime(new Date());
        calendarView.setMinDate(minDate.getTimeInMillis());

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 3); // Adding 2 days to get the day after tomorrow
        calendarView.setMaxDate(maxDate.getTimeInMillis());

        // Set a listener to handle date selection
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Get the selected date as a Calendar object
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Get the current date
                Calendar currentDate = Calendar.getInstance();

                // Calculate the difference in days between the selected date and the current date
                long differenceInMillis = selectedDate.getTimeInMillis() - currentDate.getTimeInMillis();
                int daysDifference = (int) (differenceInMillis / (1000 * 60 * 60 * 24));

                // Check if the selected date is within the allowed range (0 to 2 days from the current date)
                if (daysDifference == 0) {
                    index = 0;  // Current date
                } else if (daysDifference == 1) {
                    index = 1;  // Next two days after today
                }else if ( daysDifference == 2){
                    index = 2;
                } else {
                    // Do nothing for dates other than today and the next two days
                    return;
                }

                data(index);
                findViewById(R.id.scroll).scrollTo(0,0);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
        autoCompleteTextView.setAdapter(adapter);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                activeCity = item;
                startProgress(stringCityIdMap.get(item.toLowerCase()));
            }
        });

        findViewById(R.id.scroll).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }


        });

        findViewById(R.id.scroll).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                ScrollView scrollView = findViewById(R.id.scroll);
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int topDetector = scrollView.getScrollY();
                int bottomDetector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());

                if (bottomDetector == 0) {
                    Toast.makeText(getApplicationContext(), "Scrolling Maps", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }


            }
        });

        //back and next btn

        ImageView nextButton = findViewById(R.id.next);
        ImageView backButton = findViewById(R.id.back);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (index + 1) % (MAX_INDEX + 1);
                data(index);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (index - 1 + (MAX_INDEX + 1)) % (MAX_INDEX + 1);
                data(index);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Convert List<String> cities to String[]
        String[] citiesArray = cities.toArray(new String[0]);

        // Save the entire array of cities
        outState.putStringArray("cities", citiesArray);

        // Save the current text of AutoCompleteTextView
        outState.putString("autoCompleteText", autoCompleteTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the entire array of cities
        String[] restoredCities = savedInstanceState.getStringArray("cities");
        if (restoredCities != null) {
            cities = new ArrayList<>(Arrays.asList(restoredCities));
        }

        // Restore the text of AutoCompleteTextView
        String autoCompleteText = savedInstanceState.getString("autoCompleteText");
        if (autoCompleteText != null) {
            autoCompleteTextView.setText(autoCompleteText);
        }

        // Reset the adapter with the restored array of cities
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
        autoCompleteTextView.setAdapter(adapter);
    }



    public void startProgress(String cityId)
    {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        if (InternetConnectivityChecker.isInternetAvailable(getApplicationContext())) {
            // Internet connection is available
            // Run network access on a separate thread;
            urlSource =  "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + cityId;
            new Thread(new MainActivity.Task(urlSource)).start();
        } else {
            Toast.makeText(MainActivity.this, "No internet Connection!!!", Toast.LENGTH_SHORT).show();
            // No internet connection
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 4000);
        }


    } //

    public static LocalDate getForecastDateForLocation(int index, String location) {
        ZoneId locationTimeZone = ZONE_ID_HASH_MAP.get(location);
        if (locationTimeZone == null) {
            throw new IllegalArgumentException("Unknown location: " + location);
        }

        // Get the current date in the location's time zone
        LocalDate currentDate = LocalDate.now(locationTimeZone);

        // Calculate the forecast date based on the index
        LocalDate forecastDate = currentDate.plusDays(index);

        return forecastDate;
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            xmlresult = "";

            try
            {
                Log.e("Url ",url);
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    if(inputLine.contains("dc:") ){
                        xmlresult = xmlresult + inputLine.replaceAll("dc\\:","");
                        Log.e("MyTag",inputLine.replaceAll("dc\\:",""));
                    }else if(inputLine.contains("georss:point")){
                        Log.e("MyTag",inputLine.replace("georss\\:",""));
                    }
                    else if(inputLine.contains("atom:")){
                        Log.e("MyTag",inputLine.replace("atom:",""));
                    }
                    else{
                        xmlresult = xmlresult + inputLine;
                        Log.e("MyTag",inputLine);}

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = xmlresult.indexOf(">");
            xmlresult = xmlresult.substring(i+1);

            Log.e("MyTag - cleaned", xmlresult);

            xmlresult.replaceAll("dc\\:","");
            xmlresult.replaceAll("</rss>","");
            xmlresult.replaceAll("xmlns\\:","");
            xmlresult.replaceAll("atom\\:","");


            System.out.println(""+ xmlresult);

            XmlPullParserHandler parser =new XmlPullParserHandler();
            InputStream inputStream = new ByteArrayInputStream(xmlresult.getBytes(StandardCharsets.UTF_8));

            weatherLists = parser.parse(inputStream);

            System.out.println(weatherLists.size());

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

                    data(index);
                }
            });
        }

    }

    public void data(int index) {

        if( weatherLists.size() > 0){
            Log.d("UI thread", "I am the UI thread");
            LocalDate currentDate =  getForecastDateForLocation(index, activePlace.toLowerCase());

            // Format to get the date in the format: day, month, date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMMM dd", Locale.ENGLISH);
            String formattedDate = currentDate.format(dateFormatter);

            String minimuTemperature = weatherLists.get(index).getMinimumTemperature();
            String maximumTemparature = weatherLists.get(index).getMaximumTemparature();

            int intMinimumnTemp =  0;
            int intMaximumTemp = 0;
            int temp = 0;


            if(weatherLists.get(0).getMinimumTemperature() == null){
                minimuTemperature = "";
            }else{
                intMinimumnTemp = Integer.parseInt(minimuTemperature.replaceAll("[^0-9]", ""));
            }

            if(weatherLists.get(0).getMaximumTemparature() == null){
                maximumTemparature = "";
            }else{
                intMaximumTemp = Integer.parseInt(maximumTemparature.replaceAll("[^0-9]", ""));
            }


            if(intMaximumTemp == 0 || intMinimumnTemp == 0){
                temp = intMinimumnTemp + intMaximumTemp;
            }else{
                temp = (intMinimumnTemp + intMaximumTemp)/2;
            }

            homeDayTxt.setText(weatherLists.get(index).getDay());

            homeWeatherTxt.setText(weatherLists.get(index).getCondition());
            homeTemparatureTxt.setText(""+temp+"°C");
            homeBothTempTxt.setText("Max: "+(weatherLists.get(index).getMaximumTemparature() != null ? weatherLists.get(index).getMaximumTemparature() : "~~") +" "+"Min: "+(weatherLists.get(index).getMinimumTemperature() != null ? weatherLists.get(index).getMinimumTemperature() : "~~"));
            homeDate.setText(formattedDate);
            homeWindTxt.setText(weatherLists.get(index).getWind());
            homeHumidityTxt.setText(weatherLists.get(index).getHumidity());
            sunrise.setText(weatherLists.get(index).getSunrise());
            sunset.setText(weatherLists.get(index).getSunrise());
            winddirection.setText(weatherLists.get(index).getWindDirection());
            pollution.setText(weatherLists.get(index).getPollution());
            Uvrisk.setText(weatherLists.get(index).getUvRisk());
            visibility.setText(weatherLists.get(index).getVisibility());


            if(weatherLists.get(index).getCondition().toLowerCase().contains("light cloudy") || weatherLists.get(index).getCondition().toLowerCase().contains("partly cloudy")){
                homeWeatherImg.setImageResource(R.drawable.cloudy_3);
            }else if(weatherLists.get(index).getCondition().toLowerCase().contains("clear sky")){
                    homeWeatherImg.setImageResource(R.drawable.cloudy_3);


            }else if(weatherLists.get(index).getCondition().toLowerCase().contains("cloud")){
                homeWeatherImg.setImageResource(R.drawable.cloudy);
            }else if(weatherLists.get(index).getCondition().toLowerCase().contains("sun")){
                homeWeatherImg.setImageResource(R.drawable.sun);
            }else if(weatherLists.get(index).getCondition().toLowerCase().contains("wind")){
                homeWeatherImg.setImageResource(R.drawable.wind);
            } else if(weatherLists.get(index).getCondition().toLowerCase().contains("snow")){
                homeWeatherImg.setImageResource(R.drawable.snowy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("showers") || weatherLists.get(index).getCondition().toLowerCase().contains("rain")) {
                homeWeatherImg.setImageResource(R.drawable.rainy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("thunder")) {
                homeWeatherImg.setImageResource(R.drawable.rainy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("drizzle")) {
                homeWeatherImg.setImageResource(R.drawable.snowy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("fog")) {
                homeWeatherImg.setImageResource(R.drawable.snowy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("mist")) {
                homeWeatherImg.setImageResource(R.drawable.snowy);
            }  else if (weatherLists.get(index).getCondition().toLowerCase().contains("blizzard")) {
                homeWeatherImg.setImageResource(R.drawable.snowy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("hail")) {
                homeWeatherImg.setImageResource(R.drawable.snowy);
            } else if (weatherLists.get(index).getCondition().toLowerCase().contains("sleet")) {
                homeWeatherImg.setImageResource(R.drawable.snowy);
            }
        }

        findViewById(R.id.loading).setVisibility(View.GONE);

    }
}
