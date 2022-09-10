package hijiri.Thaqweemul.materialclock;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class WidgetViewCreator implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "WidgetViewCreator";
    static int clockColor = Color.WHITE,
            dateColor = Color.WHITE,
            hrdateColor = Color.WHITE;
    static boolean showTime=true,
                showDate,
                hrshowDate;
    static String timeFormat="h:mm",
                dateFormat="EEE, MMM d",hrdateFormat="EEE, MMM d";
    static String fontName = "default";
    static int timeTextSize=42,
                dateTextSize=22, hrdateTextSize=22;
    static int timeAlign = Gravity.CENTER,
            dateAlign = Gravity.CENTER,hrdateAlign = Gravity.CENTER;
    static final int VERTICAL=0, VERTICAL_FLIPPED=1, HORIZONTAL=2, HORIZONTAL_FLIPPED=3; //this need to be the same as the corresponding array in arrays.xml
    static int widgetOrientation = VERTICAL;
    private Context context;
    private WidgetUpdatedInterface widgetUpdatedInterface;

    public WidgetViewCreator(WidgetUpdatedInterface widgetUpdatedInterface, Context context) {
        this.context = context;
        this.widgetUpdatedInterface = widgetUpdatedInterface;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //if (key.equals(getString(R.string.sp_clock_color)))
        clockColor = sharedPreferences.getInt(context.getString(R.string.sp_clock_color), Color.WHITE);
        dateColor = sharedPreferences.getInt(context.getString(R.string.sp_date_color), Color.WHITE);
        hrdateColor = sharedPreferences.getInt(context.getString(R.string.hr_date_color), Color.WHITE);

        //else if (key.equals(getString(R.string.sp_show_hour)))
        showTime = sharedPreferences.getBoolean(context.getString(R.string.sp_show_time),true);
        showDate = sharedPreferences.getBoolean(context.getString(R.string.sp_show_date),true);
        hrshowDate = sharedPreferences.getBoolean(context.getString(R.string.hr_show_date),true);

        timeFormat = sharedPreferences.getString(context.getString(R.string.sp_time_format),"h:mm");
        dateFormat = sharedPreferences.getString(context.getString(R.string.sp_date_format),"EEE, MMM d");
        hrdateFormat = sharedPreferences.getString(context.getString(R.string.hr_date_format),"dd-MMMM");

        fontName = sharedPreferences.getString(context.getString(R.string.sp_font),"default");
        timeTextSize = sharedPreferences.getInt(context.getString(R.string.sp_time_size),42);
        dateTextSize = sharedPreferences.getInt(context.getString(R.string.sp_date_size),22);
        hrdateTextSize = sharedPreferences.getInt(context.getString(R.string.hr_date_size),22);

        timeAlign = sharedPreferences.getInt(context.getString(R.string.sp_time_align),Gravity.CENTER);
        dateAlign = sharedPreferences.getInt(context.getString(R.string.sp_date_align),Gravity.CENTER);
        hrdateAlign = sharedPreferences.getInt(context.getString(R.string.hr_date_align),Gravity.CENTER);

        widgetOrientation = sharedPreferences.getInt(context.getString(R.string.sp_layout),VERTICAL);

        widgetUpdatedInterface.widgetDataUpdated();
    }

    private int getLayoutResource() {
        switch (fontName) {
            case "warnes":
                return R.layout.widget_layout_warnes;
            case "latoreg":
                return R.layout.widget_layout_latoreg;
            case "latolight":
                return R.layout.widget_layout_latolight;
            case "latothin":
                return R.layout.widget_layout_latothin;
            case "arizonia":
                return R.layout.widget_layout_arizonia;
            case "imprima":
                return R.layout.widget_layout_imprima;
            case "notosans":
                return R.layout.widget_layout_notosans;
            case "rubik":
                return R.layout.widget_layout_rubik;
            case "jollylodger":
                return R.layout.widget_layout_jollylodger;
            case "archivoblack":
                return R.layout.widget_layout_archivoblack;
            case "bungeeshade":
                return R.layout.widget_layout_bungeeshade;
            case "coda":
                return R.layout.widget_layout_coda;
            case "ubuntulight":
                return R.layout.widget_layout_ubuntulight;
            case "handlee":
                return R.layout.widget_layout_handlee;
            default:
                return R.layout.widget_layout_default;
        }
    }

    private int getCorrectTimeView() {
        if (widgetOrientation==HORIZONTAL) return R.id.clockLeft;
        else if (widgetOrientation==HORIZONTAL_FLIPPED) return R.id.clockRight;
        else if (widgetOrientation==VERTICAL) {
            switch (timeAlign) {
                case Gravity.LEFT: return R.id.clockLeft;
                case Gravity.RIGHT: return R.id.clockRight;
                default: return R.id.clock; //center
            }
        } else { //VERTICAL_FLIPPED
            switch (timeAlign) {
                case Gravity.LEFT: return R.id.dateLeft;
                case Gravity.RIGHT: return R.id.dateRight;
                default: return R.id.date; //center
            }
        }
    }

    private int getCorrectDateView() {
        if (widgetOrientation==HORIZONTAL) return R.id.clockRight;
        else if (widgetOrientation==HORIZONTAL_FLIPPED) return R.id.clockLeft;
        else if (widgetOrientation==VERTICAL) {
            switch (dateAlign) {
                case Gravity.LEFT: return R.id.dateLeft;
                case Gravity.RIGHT: return R.id.dateRight;
                default: return R.id.date; //center
            }
        } else { //VERTICAL_FLIPPED
            switch (dateAlign) {
                case Gravity.LEFT: return R.id.clockLeft;
                case Gravity.RIGHT: return R.id.clockRight;
                default: return R.id.clock; //center
            }
        }
    }

    private int getCorrectHrDateView() {
     return R.id.ardate; //center
    }

    public RemoteViews createWidgetRemoteView() {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                getLayoutResource());

        //set clock alignment
        views.setViewVisibility(R.id.clockLeft, View.GONE);
        views.setViewVisibility(R.id.clock, View.GONE);
        views.setViewVisibility(R.id.clockRight, View.GONE);
        //set date alignment
        views.setViewVisibility(R.id.dateLeft, View.GONE);
        views.setViewVisibility(R.id.date, View.GONE);
        views.setViewVisibility(R.id.dateRight, View.GONE);
        if (showTime)
            views.setViewVisibility(getCorrectTimeView(), View.VISIBLE);
        if (showDate)
            views.setViewVisibility(getCorrectDateView(), View.VISIBLE);
        if(!hrshowDate)
            views.setViewVisibility(getCorrectHrDateView(), View.GONE);
        //set clock format
        views.setCharSequence(getCorrectTimeView(),"setFormat24Hour",timeFormat);
        views.setCharSequence(getCorrectTimeView(),"setFormat12Hour",timeFormat);
        //set date format
        views.setCharSequence(getCorrectDateView(),"setFormat24Hour",dateFormat);
        views.setCharSequence(getCorrectDateView(),"setFormat12Hour",dateFormat);
        //set time size
        views.setTextViewTextSize(getCorrectTimeView(), TypedValue.COMPLEX_UNIT_SP,timeTextSize);
        //set date size
        views.setTextViewTextSize(getCorrectDateView(), TypedValue.COMPLEX_UNIT_SP,dateTextSize);
        views.setTextViewTextSize(getCorrectHrDateView(), TypedValue.COMPLEX_UNIT_SP,hrdateTextSize);
        //set clock color
        views.setTextColor(getCorrectTimeView(),clockColor);
        //set date color
        views.setTextColor(getCorrectDateView(),dateColor);
        views.setTextColor(getCorrectHrDateView(),hrdateColor);
        try {
            views.setTextViewText(getCorrectHrDateView(),getHrDate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setListenerOnDate(context, views);

        return views;
    }

    public String getHrDate() throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_main", MODE_PRIVATE);
        String format = sharedPreferences.getString(context.getString(R.string.hr_date_format),"dd-MMMM");
       String Date=getDate(context) ;

        Log.d("tttttttttttttttttt",Date);
        int dayOfMonth = Integer.parseInt(Date.substring(0,2));
        int monthOfYear = Integer.parseInt(Date.substring(2,4));;
        int year = Integer.parseInt(Date.substring(4,8));



        HijrahDate hijrahDate = HijrahDate.of(year,monthOfYear,dayOfMonth);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String islamicDate = formatter.format(hijrahDate);
        return islamicDate;
    }

    protected void setListenerOnDate(Context context, RemoteViews views) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

            Intent calendarIntent = new Intent();
            calendarIntent.setComponent(new ComponentName("hijiri.Thaqweemul.materialclock", "hijiri.Thaqweemul.materialclock.SettingsActivity"));
            views.setOnClickPendingIntent(R.id.touchid, PendingIntent.getActivity(context, 0, calendarIntent, PendingIntent.FLAG_MUTABLE));
        }
        else
        {
            Intent calendarIntent = new Intent();
            calendarIntent.setComponent(new ComponentName("hijiri.Thaqweemul.materialclock", "hijiri.Thaqweemul.materialclock.SettingsActivity"));
            views.setOnClickPendingIntent(R.id.touchid, PendingIntent.getActivity(context, 0, calendarIntent, 0));
        }

    }


    private String readJSONDataFromFile(Context context) throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = context.getResources().openRawResource(R.raw.hrdat);
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


    public String getDate(Context context) throws IOException {
        String jsonString=readJSONDataFromFile(context);
        String hjrDate = "";
        String date = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(new Date());
        String date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 0; i < valarray.length(); i++) {

                String str = valarray.getJSONObject(i).getString("ggdate");
                if(str.equals(date))
                {
                    String year = valarray.getJSONObject(i).getString("year");
                    String month = valarray.getJSONObject(i).getString("month");
                    String day = valarray.getJSONObject(i).getString("hijridate");
                    if(day.length()==1)
                    {
                        day = "0"+day;
                    }
                    hjrDate = day+getMonthNo(month)+year;
                }
                else if(str.equals(date1))
                {
                    String year = valarray.getJSONObject(i).getString("year");
                    String month = valarray.getJSONObject(i).getString("month");
                    String day = valarray.getJSONObject(i).getString("hijridate");
                    if(day.length()==1)
                    {
                        day = "0"+day;
                    }
                    hjrDate = day+getMonthNo(month)+year;
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }
        return hjrDate;
    }

    public String getMonthNo(String month)
    {
        String m = "0";
        if(month.equals("Muharram"))
        {
            m="01";
        }
        else if(month.equals("Safar"))
        {
            m="02";
        }
        else if(month.equals("R-Awwal"))
        {
            m="03";
        }
        else if(month.equals("R-Aakhir"))
        {
            m="04";
        }
        else if(month.equals("J-Awwal"))
        {
            m="05";
        }
        else if(month.equals("J-Aakhir"))
        {
            m="06";
        }
        else if(month.equals("Rajab"))
        {
            m="07";
        }
        else if(month.equals("Sha-Ban"))
        {
            m="08";
        }
        else if(month.equals("Ramadan"))
        {
            m="09";
        }
        else if(month.equals("Shawwal"))
        {
            m="10";
        }
        else if(month.equals("Dhul Qa-Dha"))
        {
            m="11";
        }
        else if(month.equals("Dhul Hijjah"))
        {
            m="12";
        }
        return m;
    }

}
