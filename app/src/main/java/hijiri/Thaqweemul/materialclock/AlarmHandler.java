package hijiri.Thaqweemul.materialclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmHandler {
    private Context context;
    public  AlarmHandler(Context context)
    {
        this.context = context;
    }

    public void setAlaramManager()
    {
        Log.d("ddddddddddddd","called2");
        Intent intent =new Intent(context, ExexutableTask.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(am!=null)
        {
            long trigerAfter = 3*60*60*1000;
            long trigerEvery = 2*60*60*1000;
            am.setRepeating(AlarmManager.RTC_WAKEUP,trigerAfter,trigerEvery,sender);

        }
    }

    public void  cancelAlarmManager()
    {
        Log.d("ddddddddddddd","called1");
        Intent intent =new Intent(context, ExexutableTask.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(am!=null)
        {
            am.cancel(sender);
        }
    }
}
