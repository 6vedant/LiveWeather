package xyz.vedant.liveweather.model;

public class ForecastModel {
    String date_forecast, status_forecast, temperature_forecast;
    int status_image_forecast;

    public ForecastModel(String date_forecast, String status_forecast, String temperature_forecast, int status_image_forecast) {
        this.date_forecast = date_forecast;
        this.status_forecast = status_forecast;
        this.temperature_forecast = temperature_forecast;
        this.status_image_forecast = status_image_forecast;
    }

    public String getDate_forecast() {
        return date_forecast;
    }

    public void setDate_forecast(String date_forecast) {
        this.date_forecast = date_forecast;
    }

    public String getStatus_forecast() {
        return status_forecast;
    }

    public void setStatus_forecast(String status_forecast) {
        this.status_forecast = status_forecast;
    }

    public String getTemperature_forecast() {
        return temperature_forecast;
    }

    public void setTemperature_forecast(String temperature_forecast) {
        this.temperature_forecast = temperature_forecast;
    }

    public int getStatus_image_forecast() {
        return status_image_forecast;
    }

    public void setStatus_image_forecast(int status_image_forecast) {
        this.status_image_forecast = status_image_forecast;
    }
}
