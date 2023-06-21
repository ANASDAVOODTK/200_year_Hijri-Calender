package hijiri.Thaqweemul.materialclock;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
import java.util.concurrent.TimeUnit;

public class ScheduledTask extends Worker implements WidgetUpdatedInterface{
    private final Context context;
    WidgetViewCreator widgetViewCreator;

    public ScheduledTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        widgetViewCreator = new WidgetViewCreator(this, context);
    }


        @NonNull
    @Override
    public Result doWork() {

        Log.e("onEnabled", "hello5");
        ComponentName componentName = new ComponentName(context, ClockWidgetProvider.class);
        int appWidgetIds[] = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
        if (appWidgetIds.length == 0) {

            appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass()));
        }

        updateClockWithDynamicTextSizes(context, appWidgetIds);

        return Result.success();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void updateClockWithDynamicTextSizes(Context context, int[] appWidgetIds) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("sp_main", MODE_PRIVATE);
            String fontName = sharedPreferences.getString(context.getString(R.string.sp_font), "default");
            String format = sharedPreferences.getString(context.getString(R.string.hr_date_format), "dd-MMMM");
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            @SuppressLint("RemoteViewLayout") RemoteViews views = new RemoteViews(context.getPackageName(), getLayoutResource(fontName));

            String Date = getDate(context);
            int dayOfMonth = Integer.parseInt(Date.substring(0, 2));
            int monthOfYear = Integer.parseInt(Date.substring(2, 4));
            ;
            int year = Integer.parseInt(Date.substring(4, 8));
            Log.d("rrrrrrrrr", dayOfMonth + " " + monthOfYear + " " + year);


            HijrahDate hijrahDate = HijrahDate.of(year, monthOfYear, dayOfMonth);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

            String islamicDate = formatter.format(hijrahDate); // 07/03/1439
            Log.e("onEnabled", islamicDate);
            for (int id : appWidgetIds) {

                views.setTextViewText(R.id.ardate, islamicDate);
                manager.updateAppWidget(id, views);
            }
           // redrawWidgetFromData(context, manager, appWidgetIds);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getLayoutResource(String fontName) {
        Log.e("onEnabled", "hello6");

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
        widgetViewCreator = new WidgetViewCreator((WidgetUpdatedInterface) this, context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sp_main), MODE_PRIVATE);
        widgetViewCreator.onSharedPreferenceChanged(sharedPreferences, "");
        RemoteViews views = widgetViewCreator.createWidgetRemoteView();
        appWidgetManager.updateAppWidget(widgetId, views);
        Log.e("onEnabled", "hello7");
    }

    private String readJSONDataFromFile(Context context) throws IOException {
        Log.e("onEnabled", "hello8");
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
        Log.e("onEnabled", "hello9");
        String jsonString = readJSONDataFromFile(context);
        String hjrDate = "";
        String date = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(new Date());
        String date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 44320; i < valarray.length(); i++) {

                String str = valarray.getJSONObject(i).getString("ggdate");
                if (str.equals(date)) {
                    String year = valarray.getJSONObject(i).getString("year");
                    String month = valarray.getJSONObject(i).getString("month");
                    String day = valarray.getJSONObject(i).getString("hijridate");
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    hjrDate = day + getMonthNo(month) + year;
                } else if (str.equals(date1)) {
                    String year = valarray.getJSONObject(i).getString("year");
                    String month = valarray.getJSONObject(i).getString("month");
                    String day = valarray.getJSONObject(i).getString("hijridate");
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    hjrDate = day + getMonthNo(month) + year;
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }
        return hjrDate;
    }

    public String getMonthNo(String month) {
        String m = "0";
        Log.e("onEnabled", "hello10");
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

    @Override
    public void widgetDataUpdated() {

    }
}
