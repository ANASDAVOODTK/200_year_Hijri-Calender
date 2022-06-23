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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExexutableTask extends BroadcastReceiver {
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
        Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAA","hello");
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
            String day1 = DateFormat.format("dd", getCalendar()).toString();
            String month1 = DateFormat.format("MM", getCalendar()).toString();
            String year1 = DateFormat.format("yyyy", getCalendar()).toString();

            Log.d("tttttttttttttt",day1+" "+month1+" "+ year1);

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
}
