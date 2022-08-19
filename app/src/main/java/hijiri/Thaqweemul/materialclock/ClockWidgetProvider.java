package hijiri.Thaqweemul.materialclock;

//import android.app.PendingIntent;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import static android.content.Context.MODE_PRIVATE;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ClockWidgetProvider extends AppWidgetProvider implements WidgetUpdatedInterface {
    public static final String CLICK_ACTION = "xyz.yoav.materialclock.CLICK_ACTION";
    public static final String EXTRA_ITEM = "xyz.yoav.materialclock.EXTRA_ITEM";
    public static final String APPWIDGET_UPDATE_OPTIONS = "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS";
    public static final String APPWIDGET_ENABLED = "android.appwidget.action.APPWIDGET_ENABLED";

    WidgetViewCreator widgetViewCreator;


    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }

    private void redrawWidgetFromData(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        widgetViewCreator = new WidgetViewCreator(this,context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sp_main),MODE_PRIVATE);
        widgetViewCreator.onSharedPreferenceChanged(sharedPreferences,"");
        RemoteViews views = widgetViewCreator.createWidgetRemoteView();
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        ComponentName componentName = new ComponentName(context,ClockWidgetProvider.class);
        int appWidgetIds[] = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
        if (appWidgetIds.length == 0) {
            Log.e("onEnabled","hello");
            appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));
        }
        Log.e("onEnabled","hello");
        updateClockWithDynamicTextSizes(context, appWidgetIds);

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                ClockWidgetProvider.class);
        updateClockWithDynamicTextSizes(context,appWidgetIds);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            redrawWidgetFromData(context, appWidgetManager, widgetId);
            // Register an onClickListener
            /*Intent intent = new Intent(context, ClockWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.clock, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);*/
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        // This function is called when widget is resized
        int[] appWidgetIds = {appWidgetId};
        updateClockWithDynamicTextSizes(context, appWidgetIds);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

    }

        @Override
    public void widgetDataUpdated() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void updateClockWithDynamicTextSizes(Context context, int[] appWidgetIds) {
        AlarmHandler alarmHandler = new AlarmHandler(context);
        alarmHandler.cancelAlarmManager();
        alarmHandler.setAlaramManager();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("sp_main", MODE_PRIVATE);
            String fontName = sharedPreferences.getString(context.getString(R.string.sp_font),"default");
            String format = sharedPreferences.getString(context.getString(R.string.hr_date_format),"dd-MMMM");
            Log.d("tttttttttttttttttttt",format);
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
        }

        catch (Exception e){
            e.printStackTrace();
        }
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