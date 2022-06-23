package hijiri.Thaqweemul.materialclock;

//import android.app.PendingIntent;
import android.annotation.SuppressLint;
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
    private Calendar calendar;
    public Calendar getCalendar() {
        // This method creates an instance of calender.
        if (this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }
        return this.calendar;
    }



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
            String day1 = DateFormat.format("dd", getCalendar()).toString();
            String month1 = DateFormat.format("MM", getCalendar()).toString();
            String year1 = DateFormat.format("yyyy", getCalendar()).toString();

            int dayOfMonth = Integer.parseInt(day1);
            int monthOfYear = Integer.parseInt(month1);;
            int year = Integer.parseInt(year1);;

            LocalDate dt = LocalDate.of(year, monthOfYear, dayOfMonth);
            HijrahDate hijrahDate = HijrahDate.from(dt);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String islamicDate = formatter.format(hijrahDate); // 07/03/1439
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
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
}