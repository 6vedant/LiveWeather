package xyz.vedant.liveweather.data;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.vedant.liveweather.R;
import xyz.vedant.liveweather.adapter.ForecastAdapter;
import xyz.vedant.liveweather.model.ForecastModel;

public class ForecastData {
    Context context;
    public static ArrayList<ForecastModel> forecastModels = new ArrayList<>();
    RecyclerView recyclerView;

    public ForecastData(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        forecastModels.clear();

        for (int i = 0; i < 5; i++) {
            String curr_date = getDate();
            String curr_status = getStatus();
            String curr_temp = getTemp();
            ForecastModel forecastModel = new ForecastModel(curr_date, curr_status, curr_temp, R.drawable.sunny_icon);

            forecastModels.add(forecastModel);
        }

        if (forecastModels.size() > 0) {
            //attach it to adapter
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(true);
            ForecastAdapter forecastAdapter = new ForecastAdapter(context, forecastModels);
            recyclerView.setAdapter(forecastAdapter);
        }else{
            //no data found erro
            Toast.makeText(context, "No forecast data found now", Toast.LENGTH_SHORT).show();
        }
    }


    public String getDate() {
        return "Today";
    }

    public String getStatus() {
        return "Clear";
    }

    public String getTemp() {
        return "23" + context.getString(R.string.degree_str) + "/30" + context.getString(R.string.degree_str);
    }

}
