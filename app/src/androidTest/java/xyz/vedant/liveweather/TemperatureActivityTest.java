package xyz.vedant.liveweather;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TemperatureActivityTest {

    private static final String TAG = "TemperatureActivityTest";

    @Rule
    public ActivityTestRule<TemperatureActivity> activityTestRule = new ActivityTestRule<TemperatureActivity>(TemperatureActivity.class);

    private TemperatureActivity temperatureActivity = null;

    @Before
    public void setUp() throws Exception {
        temperatureActivity = activityTestRule.getActivity();
    }

    @Test
    public void loadData() {
        FirebaseDatabase.getInstance().getReference().child("live_weather")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        assertNotNull(dataSnapshot);

                        DataSnapshot ds_city = dataSnapshot.child("city");
                        DataSnapshot ds_forecasting_api = dataSnapshot.child("forecasting_api");
                        DataSnapshot ds_live_api = dataSnapshot.child("live_api");
                        assertNotNull(ds_city);
                        assertNotNull(ds_forecasting_api);
                        assertNotNull(ds_live_api);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Error: " + databaseError.getMessage());
                    }
                });


    }

    @After
    public void tearDown() throws Exception {
        temperatureActivity = null;
    }


}
