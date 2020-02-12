package xyz.vedant.liveweather;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import xyz.vedant.liveweather.data.ForecastData;
import xyz.vedant.liveweather.model.ForecastModel;

public class TemperatureActivity extends AppCompatActivity {

    String curr_temp;
    String curr_city;

    String live_api_url, forecast_api_url;

    public String getLive_api_url() {
        return live_api_url;
    }

    public void setLive_api_url(String live_api_url) {
        this.live_api_url = live_api_url;
    }

    public String getForecast_api_url() {
        return forecast_api_url;
    }

    public void setForecast_api_url(String forecast_api_url) {
        this.forecast_api_url = forecast_api_url;
    }

    public String getCurr_city() {
        return curr_city;
    }

    public void setCurr_city(String curr_city) {
        this.curr_city = curr_city;
    }

    public String getCurr_temp() {
        return curr_temp;
    }

    public void setCurr_temp(String curr_temp) {
        this.curr_temp = curr_temp;
    }

    RecyclerView recyclerView;
    TextView tv_temperature, tv_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setCurr_temp("0");
        setCurr_city("Bengaluru");

        final String live_api = getIntent().getStringExtra("live_api");
        final String forecast_api = getIntent().getStringExtra("forecast_api");
        final String curr_city = getIntent().getStringExtra("city");
        setLive_api_url(live_api);
        setForecast_api_url(forecast_api);
        setCurr_city(curr_city);

        try {
            // getSupportActionBar().setTitle(getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_city = (TextView) findViewById(R.id.tv_city);

        tv_city.setText(getCurr_city());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refresing data", Snackbar.LENGTH_SHORT).show();
                loadData();
            }
        });

        // set the title of toolbar
        // when scrolled up
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("" + getCurr_temp() + getString(R.string.degree_str) + " - " + getCurr_city());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


        //first time loading
        loadData();
    }

    public void loadData() {
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getLive_api_url(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONObject json_obj = obj.getJSONObject("main");
                            double curr_temp = json_obj.getDouble("temp");
                            //set temp
                            setCurr_temp(getTempCelcius(curr_temp));
                            tv_temperature.setText(getCurr_temp() + "" + getString(R.string.degree_str));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage() + " error at line 200", Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

        loadForecastingData();
    }

    public void loadForecastingData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getForecast_api_url(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            ArrayList<ForecastModel> forecastModels = new ArrayList<>();
                            forecastModels.clear();
                            JSONObject obj = new JSONObject(response);
                            JSONArray forecast_arr = obj.getJSONArray("list");
                            long today_time = System.currentTimeMillis() / 1000;
                            long increment = 84600;
                            long min = 0, max = 84600;
                            int count = 1;
                            int curr_count = 1;
                            double min_global = 500.0, max_global = 0.0;

                            for (int i = 0; i < forecast_arr.length(); i++) {

                                JSONObject curr_obj = forecast_arr.getJSONObject(i);
                                JSONObject main_data_obj = curr_obj.getJSONObject("main");
                                JSONArray weather_data_obj_arr = curr_obj.getJSONArray("weather");
                                JSONObject weather_data_obj = weather_data_obj_arr.getJSONObject(0);
                                long sys_time = curr_obj.getLong("dt");
                                long diff = Math.abs(sys_time - today_time);
                                double curr_min_temp = main_data_obj.getDouble("temp_min");
                                double curr_max_temp = main_data_obj.getDouble("temp_max");

                                //set the global minimum
                                if (curr_min_temp < min_global) {
                                    min_global = curr_min_temp;
                                }
                                //set the global maximum
                                if (curr_max_temp > max_global) {
                                    max_global = curr_max_temp;
                                }

                                //check for the dates change from epoch diff
                                if (diff > min && diff < max) {

                                    String curr_status_desc = weather_data_obj.getString("main");
                                    String curr_date = curr_obj.getString("dt_txt");

                                    if (count == 1) {
                                        curr_date = "Today";
                                    } else if (count == 2) {
                                        curr_date = "Tommorow";
                                    } else {
                                        curr_date = getFormatDate(curr_date);
                                    }

                                    String min_max_temp = getTempCelcius(min_global) + "" + getString(R.string.degree_str)
                                            + "/" + getTempCelcius(max_global) + getString(R.string.degree_str);

                                    ForecastModel forecastModel = new ForecastModel(curr_date, curr_status_desc, min_max_temp, R.drawable.sunny_icon);
                                    forecastModels.add(forecastModel);
                                    count++;
                                    min = max;
                                    max = max + increment;

                                    //reset the min and max temperature here
                                    min_global = 500.0;
                                    max_global = 0.0;

                                }
                            }

                            if (forecastModels.size() > 0) {
                                // load again the forecast recyclerview
                                try {
                                    ForecastData forecastData = new ForecastData(TemperatureActivity.this, recyclerView, forecastModels);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                // no data here
                                //show no forecast data available
                                Toast.makeText(getApplicationContext(), "No forecast data available", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "error ", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage() + " error at line 200", Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public String getTempCelcius(double tempKelvin) {
        double temp_cel = tempKelvin - 273.15;
        return String.format(Locale.US, "%.1f", temp_cel);
    }

    public String getFormatDate(String date_str) {
        String result = "";
        try {


            String curr_year = date_str.substring(0, 4);
            String curr_month = date_str.substring(5, 7);
            String curr_day = date_str.substring(8, 10);

            int curr_day_int = Integer.parseInt(curr_day);
            int curr_month_int = Integer.parseInt(curr_month);
            result = curr_day_int + " " + getMonth(curr_month_int) + " " + curr_year;

        } catch (Exception e) {
            result = date_str;
        }

        return result;
    }


    public String getMonth(int month) {
        switch (month) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";


            case 8:
                return "Aug";

            case 9:
                return "Sep";


            case 10:
                return "Oct";


            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }
        return "";
    }


    //overriden methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void exitApp(){

    }

}
