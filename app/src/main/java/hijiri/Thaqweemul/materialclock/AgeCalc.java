package hijiri.Thaqweemul.materialclock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class AgeCalc extends AppCompatActivity {

    boolean run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calc);
        getSupportActionBar().hide();

        TextView textView = findViewById(R.id.hijiriHeding);
        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Age Calculator In Hijri");
        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7e4397"),
                        Color.parseColor("#8e3c81"),
                        Color.parseColor("#ad3366"),
                        Color.parseColor("#cb2940"),
                        Color.parseColor("#d7222d"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        ImageView imageView = findViewById(R.id.imgLogin);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AgeCalc.this, LoginActivity.class );
                startActivity(intent);
            }
        });
        bottombar();

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Choose Your Date Of Birth");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        CircularProgressButton btn = (CircularProgressButton) findViewById(R.id.btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.startAnimation();
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendar.setTimeInMillis((Long) selection);
                        SimpleDateFormat formatd = new SimpleDateFormat("dd");
                        SimpleDateFormat formatm = new SimpleDateFormat("M");
                        SimpleDateFormat formaty = new SimpleDateFormat("yyyy");
                        String day  = formatd.format(calendar.getTime());
                        String month  = formatm.format(calendar.getTime());
                        String year  = formaty.format(calendar.getTime());
                        run = true;
                        try {
                            getDate(year,month,day);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        btn.stopAnimation();
                        btn.revertAnimation();
                    }
                });


    }

    public void bottombar()
    {
        final FlareBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setBackground(getResources().getDrawable(R.drawable.bottom_box));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_home_24),"Home","#FFECB3"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_calendar_month_24),"Calender","#FFECB3"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_birth),"Age Calc","#FFECB3"));
        bottomBar.setSelectedIndex(2);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                //Toast.makeText(MainActivity.this,"Tab "+ selectedIndex+" Selected.",Toast.LENGTH_SHORT).show();
                if(selectedIndex==1)
                {
                    Intent i = new Intent(AgeCalc.this, MainActivity.class);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

                if(selectedIndex==0)
                {
                    Intent i = new Intent(AgeCalc.this, MainActivity2.class);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(AgeCalc.this);
    }

    private String readJSONDataFromFile() throws IOException {

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


    public void getDate(String y,String m,String d) throws IOException {
        String jsonString=readJSONDataFromFile();
        String hjrDate = "";
         try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 0; i < valarray.length(); i++) {

                String str = valarray.getJSONObject(i).getString("ggdate");
                Log.d("fdfdfdfd",y+"-"+m+"-"+d);
                if(str.equals(y+"-"+m+"-"+d))
                {
                    if(run)
                    {
                        String year = valarray.getJSONObject(i).getString("year");
                        String month = valarray.getJSONObject(i).getString("month");
                        String day = valarray.getJSONObject(i).getString("hijridate");
                        hjrDate = day+"-"+month+"-"+year;
                        LinearLayout linearLayout = findViewById(R.id.llout);
                        TextView hgdate = findViewById(R.id.hjdate);
                        TextView ggdate = findViewById(R.id.ggdate);
                        hgdate.setText(hjrDate);
                        ggdate.setText(d+"-"+m+"-"+y);

                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-M-yyyy");
                        Date date = null;
                        try {
                            date = inFormat.parse(d+"-"+m+"-"+y);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                        String week = outFormat.format(date);
                        TextView ggday = findViewById(R.id.ggday);
                        ggday.setText(week);

                        String tdydate = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                        int age = Integer.parseInt(tdydate)-Integer.parseInt(y);

                        TextView txtage = findViewById(R.id.age);
                        txtage.setText("Your Age : "+age);

                        linearLayout.setVisibility(View.VISIBLE);
                        run=false;
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }

    }
}