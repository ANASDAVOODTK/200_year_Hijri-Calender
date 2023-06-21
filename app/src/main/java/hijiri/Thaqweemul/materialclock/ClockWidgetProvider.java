package hijiri.Thaqweemul.materialclock;

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
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

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
import java.util.concurrent.TimeUnit;


public class ClockWidgetProvider extends AppWidgetProvider implements WidgetUpdatedInterface {


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
            Log.e("onEnabled","hello1");
            appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));
        }
        Log.e("onEnabled","hello2");
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
        Log.e("onEnabled","hello3");
        updateClockWithDynamicTextSizes(context, appWidgetIds);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

    }

        @Override
    public void widgetDataUpdated() {

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

            Log.d("tttttttttttttttttt",Date);
            int dayOfMonth = Integer.parseInt(Date.substring(0,2));
            int monthOfYear = Integer.parseInt(Date.substring(2,4));;
            int year = Integer.parseInt(Date.substring(4,8));



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
        String date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 44320; i < valarray.length(); i++) {

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
        switch (month) {
            case "Muharram":
                m = "01";
                break;
            case "Safar":
                m = "02";
                break;
            case "R-Awwal":
                m = "03";
                break;
            case "R-Aakhir":
                m = "04";
                break;
            case "J-Awwal":
                m = "05";
                break;
            case "J-Aakhir":
                m = "06";
                break;
            case "Rajab":
                m = "07";
                break;
            case "Sha-Ban":
                m = "08";
                break;
            case "Ramadan":
                m = "09";
                break;
            case "Shawwal":
                m = "10";
                break;
            case "Dhul Qa-Dha":
                m = "11";
                break;
            case "Dhul Hijjah":
                m = "12";
                break;
        }
        return m;
    }


}