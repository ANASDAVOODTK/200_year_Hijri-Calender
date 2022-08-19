package hijiri.Thaqweemul.materialclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final long delayTime = 1500;
    Handler handler = new Handler();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        try {
            getDate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler.postDelayed(postTask, delayTime);
        this.context = SplashActivity.this;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(postTask);
        super.onDestroy();
    }

    Runnable postTask = new Runnable() {
        @Override
        public void run() {



               Intent intent;
               intent = new Intent(SplashActivity.this, MainActivity2.class);
               startActivity(intent);
                finish();

        }
    };


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


    public void getDate() throws IOException {
        String jsonString=readJSONDataFromFile();
        String date = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(new Date());
        try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 0; i < valarray.length(); i++) {

                String str = valarray.getJSONObject(i).getString("ggdate");
                if(str.equals(date))
                {
                    String year = valarray.getJSONObject(i).getString("year");
                    String month = valarray.getJSONObject(i).getString("month");
                    String day = valarray.getJSONObject(i).getString("hijridate");
                    //Toast.makeText(SplashActivity.this, day+"-"+month+"-"+year, Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getSharedPreferences("datevr", MODE_PRIVATE).edit();
                    editor.putString("dateToday", day+"-"+month+"-"+year);
                    editor.putString("day", day);
                    editor.putString("month", String.valueOf(getMonthNo(month)));
                    editor.putString("year", year);
                    editor.apply();
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }
    }


    public int getMonthNo(String month)
    {
        int m = 0;
        if(month.equals("Muharram"))
        {
            m=1;
        }
        else if(month.equals("Safar"))
        {
            m=2;
        }
        else if(month.equals("R-Awwal"))
        {
            m=3;
        }
        else if(month.equals("R-Aakhir"))
        {
            m=4;
        }
        else if(month.equals("J-Awwal"))
        {
            m=5;
        }
        else if(month.equals("J-Aakhir"))
        {
            m=6;
        }
        else if(month.equals("Rajab"))
        {
            m=7;
        }
        else if(month.equals("Sha-Ban"))
        {
            m=8;
        }
        else if(month.equals("Ramadan"))
        {
            m=9;
        }
        else if(month.equals("Shawwal"))
        {
            m=10;
        }
        else if(month.equals("Dhul Qa-Dha"))
        {
            m=11;
        }
        else if(month.equals("Dhul Hijjah"))
        {
            m=12;
        }
        return m;
    }





}