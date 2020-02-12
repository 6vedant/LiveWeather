package xyz.vedant.liveweather.data;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.vedant.liveweather.adapter.ForecastAdapter;
import xyz.vedant.liveweather.model.ForecastModel;

public class ForecastData {
    Context context;
    RecyclerView recyclerView;

    public ForecastData(Context context, RecyclerView recyclerView, ArrayList<ForecastModel> forecastModels) {
        this.context = context;
        this.recyclerView = recyclerView;
        if (forecastModels.size() > 0) {
            //attach it to adapter
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(true);
            ForecastAdapter forecastAdapter = new ForecastAdapter(context, forecastModels);
            recyclerView.setAdapter(forecastAdapter);
        } else {
            //no data found erro
            Toast.makeText(context, "No forecast data found now", Toast.LENGTH_SHORT).show();
        }
    }

}
