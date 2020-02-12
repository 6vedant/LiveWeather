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
        holder.status_iv.setImageResource(forecastModel.getStatus_image_forecast());
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
