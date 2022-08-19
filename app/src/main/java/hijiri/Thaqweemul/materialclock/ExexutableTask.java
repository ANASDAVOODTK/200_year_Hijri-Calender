package hijiri.Thaqweemul.materialclock;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExexutableTask extends BroadcastReceiver {
    private Calendar calendar;
    WidgetViewCreator widgetViewCreator;
    public Calendar getCalendar() {
        // This method creates an instance of calender.
        if (this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }
        return this.calendar;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName = new ComponentName(context,ClockWidgetProvider.class);
        int appWidgetIds[] = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
        if (appWidgetIds.length == 0) {
            Log.e("onEnabled","hello");
            appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));
        }

        updateClockWithDynamicTextSizes(context, appWidgetIds);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void updateClockWithDynamicTextSizes(Context context, int[] appWidgetIds) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("sp_main", MODE_PRIVATE);
            String fontName = sharedPreferences.getString(context.getString(R.string.sp_font),"default");
            String format = sharedPreferences.getString(context.getString(R.string.hr_date_format),"dd-MMMM");
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            @SuppressLint("RemoteViewLayout") RemoteViews views = new RemoteViews(context.getPackageName(), getLayoutResource(fontName));

            String Date=getDate(context) ;
            int dayOfMonth = Integer.parseInt(Date.substring(0,2));
            int monthOfYear = Integer.parseInt(Date.substring(2,3));;
            int year = Integer.parseInt(Date.substring(3,7));;
            Log.d("rrrrrrrrr",dayOfMonth+" "+monthOfYear+" "+year);


            HijrahDate hijrahDate = HijrahDate.of(year,monthOfYear,dayOfMonth);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

            String islamicDate = formatter.format(hijrahDate); // 07/03/1439
            for (int id : appWidgetIds) {

                views.setTextViewText(R.id.ardate, islamicDate);
                manager.updateAppWidget(id, views);
            }
            redrawWidgetFromData(context, manager, appWidgetIds);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();
        alarmHandler.setAlaramManager();
    }

    private int getLayoutResource(String fontName) {

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

    private void redrawWidgetFromData(Context context, AppWidgetManager appWidgetManager, int[] widgetId) {
        widgetViewCreator = new WidgetViewCreator((WidgetUpdatedInterface) this,context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sp_main),MODE_PRIVATE);
        widgetViewCreator.onSharedPreferenceChanged(sharedPreferences,"");
        RemoteViews views = widgetViewCreator.createWidgetRemoteView();
        appWidgetManager.updateAppWidget(widgetId, views);
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
        try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 0; i < valarray.length(); i++) {

                String str = valarray.getJSONObject(i).getString("ggdate");
                if(str.equals(date))
                {
                    String year = valarray.getJSONObject(i).getString("year");
                    String month = valarray.getJSONObject(i).getString("month");
                    String day = valarray.getJSONObject(i).getString("hijridate");
                    hjrDate = day+getMonthNo(month)+year;
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }
        return hjrDate;
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
