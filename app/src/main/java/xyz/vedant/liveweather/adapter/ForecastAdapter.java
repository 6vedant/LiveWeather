package xyz.vedant.liveweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.vedant.liveweather.R;
import xyz.vedant.liveweather.model.ForecastModel;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;
    public ArrayList<ForecastModel> forecastModels;

    public ForecastAdapter(Context context, ArrayList<ForecastModel> forecastModels) {
        this.context = context;
        this.forecastModels = forecastModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ForecastModel forecastModel = forecastModels.get(position);
        holder.tv_date.setText(forecastModel.getDate_forecast());
        holder.status_tv.setText(forecastModel.getStatus_forecast());
        holder.tv_temperature.setText(forecastModel.getTemperature_forecast());

        //change the image of forecast status
        String status_lowercase = forecastModel.getStatus_forecast().toLowerCase();
        int drawable_res;

        if (status_lowercase.equals("clouds") || status_lowercase.equals("cloud")) {
            drawable_res = R.drawable.clouds_icon;

        } else if (status_lowercase.equals("rain")) {
            drawable_res = R.drawable.rain_icon;

        } else if (status_lowercase.equals("clear")) {
            drawable_res = R.drawable.clear_icon;

        } else if (status_lowercase.equals("storm")) {
            drawable_res = R.drawable.storm_icon;

        } else if (status_lowercase.equals("sun") || status_lowercase.equals("sunny")) {
            drawable_res = R.drawable.sunny_icon;
        } else {
            drawable_res = R.drawable.clear_icon;
        }

        holder.status_iv.setImageResource(drawable_res);
    }

    @Override
    public int getItemCount() {
        return forecastModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_temperature, status_tv;
        ImageView status_iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.date_forecast_tv);
            tv_temperature = (TextView) itemView.findViewById(R.id.temp_forecast_tv);
            status_tv = (TextView) itemView.findViewById(R.id.status_forecast_tv);
            status_iv = (ImageView) itemView.findViewById(R.id.image_forecast_iv);
        }
    }
}
