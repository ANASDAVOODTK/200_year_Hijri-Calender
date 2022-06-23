package hijiri.Thaqweemul.materialclock;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    MaterialHijriCalendarView materialHijriCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView.setText("Thaqweemul Hijri".toUpperCase());
        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Thaqweemul Hijri");
        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7e4397"),
                        Color.parseColor("#8e3c81"),
                        Color.parseColor("#ad3366"),
                        Color.parseColor("#cb2940"),
                        Color.parseColor("#d7222d"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);


        textView2.setText("Add Hijri Date Widgets To Your HomeScreen");
        TextPaint paint2 = textView.getPaint();
        float width1 = paint2.measureText("Add Hijri Date Widgets To Your HomeScreen");
        Shader textShader1 = new LinearGradient(0, 0, width1, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7e4397"),
                        Color.parseColor("#8e3c81"),
                        Color.parseColor("#ad3366"),
                        Color.parseColor("#cb2940"),
                        Color.parseColor("#d7222d"),
                }, null, Shader.TileMode.CLAMP);
        textView2.getPaint().setShader(textShader1);


        materialHijriCalendarView = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        materialHijriCalendarView.setSelectedDate(calendar.getTime());

    }
}