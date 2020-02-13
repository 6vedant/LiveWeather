package xyz.vedant.liveweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    LinearLayout loading_layout, error_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        error_layout = (LinearLayout) findViewById(R.id.error_layout);

        loadData();

        findViewById(R.id.error_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loadData();
            }
        });

    }

    public void loadData() {
        loading_layout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference().child("live_weather")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String live_api_url = (String) dataSnapshot.child("live_api").getValue();
                        String forecast_api_url = (String) dataSnapshot.child("forecasting_api").getValue();
                        String curr_city = (String) dataSnapshot.child("city").getValue();
                        Intent intent = new Intent(Splash.this, TemperatureActivity.class);
                        intent.putExtra("live_api", live_api_url);
                        intent.putExtra("forecast_api", forecast_api_url);
                        intent.putExtra("city", curr_city);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loading_layout.setVisibility(View.GONE);
                        error_layout.setVisibility(View.VISIBLE);
                    }
                });

    }

}
