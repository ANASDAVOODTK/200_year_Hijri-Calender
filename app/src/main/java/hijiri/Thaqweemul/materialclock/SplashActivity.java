package hijiri.Thaqweemul.materialclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;




@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final long delayTime = 1500;
    Handler handler = new Handler();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);


        handler.postDelayed(postTask, delayTime);

       this.context = SplashActivity.this;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(postTask);
        super.onDestroy();
    }

    Runnable postTask = new Runnable() {
        @Override
        public void run() {



                Intent intent;
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

        }
    };




}