package xyz.vedant.liveweather;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import xyz.vedant.liveweather.data.ForecastData;

public class TemperatureActivity extends AppCompatActivity {

    String curr_temp;
    String curr_city;

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


        try {
            // getSupportActionBar().setTitle(getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        try {
            ForecastData forecastData = new ForecastData(TemperatureActivity.this, recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurr_temp("" + System.currentTimeMillis());

                Toast.makeText(getApplicationContext(), "" + live_api + " " + forecast_api, Toast.LENGTH_SHORT).show();
                setToolbarText("26" + getString(R.string.degree_str));
                Snackbar.make(view, "Replace with your own action" + getCurr_temp(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    }

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

    public void setToolbarText(String text) {
        try {
            getSupportActionBar().setTitle(text);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
