package hijiri.Thaqweemul.materialclock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    private Calendar calendar;
    public Calendar getCalendar() {
        // This method creates an instance of calender.
        if (this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }
        return this.calendar;
    }
    DatabaseReference reff;


    private RecyclerView mRecyclerView;
    private List<HijiriModel> viewItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        SharedPreferences prefs = getSharedPreferences("datevr", MODE_PRIVATE);
        String name = prefs.getString("datevr", "No name defined");
       // Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(MainActivity2.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new HijriAdapter(this, viewItems);
        mRecyclerView.setAdapter(mAdapter);
        addItemsFromJSON();
        bottombar();

        ImageView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity2.this.recreate();

            }
        });

    }


    private void addItemsFromJSON() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i=0; i<jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String hijridate = itemObj.getString("hijridate");
                String ggdate = itemObj.getString("ggdate");
                String month = itemObj.getString("month");
                String year = itemObj.getString("year");
                String time = itemObj.getString("time");
                String fq = itemObj.getString("fq");
                String fm = itemObj.getString("fm");
                String lq = itemObj.getString("lq");

                HijiriModel holidays = new HijiriModel(hijridate,ggdate,month,year,time,fq,fm,lq);
                viewItems.add(holidays);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }



        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ApplicationClass globalVariable = (ApplicationClass) getApplicationContext();
               mRecyclerView.getLayoutManager().scrollToPosition(globalVariable.getPosition1()-10);
                final Handler handler1 = new Handler(Looper.getMainLooper());
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(globalVariable.getPosition1()+2);
                    }
                }, 500);
            }
        }, 500);
    }

    private String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.hrdat);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }

    public void bottombar()
    {
        final FlareBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setBackground(getResources().getDrawable(R.drawable.bottom_box));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_home_24),"Home","#FFECB3"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_calendar_month_24),"Calender","#FFECB3"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_birth),"Age Calc","#FFECB3"));
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                //Toast.makeText(MainActivity.this,"Tab "+ selectedIndex+" Selected.",Toast.LENGTH_SHORT).show();
                if(selectedIndex==2)
                {
                    Intent i = new Intent(MainActivity2.this, AgeCalc.class);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

                if(selectedIndex==1)
                {
                    Intent i = new Intent(MainActivity2.this, MainActivity.class);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(MainActivity2.this);
    }







}