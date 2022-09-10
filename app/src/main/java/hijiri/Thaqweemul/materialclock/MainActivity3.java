package hijiri.Thaqweemul.materialclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;

import java.util.ArrayList;

import hijiri.Thaqweemul.materialclock.databinding.ActivityMainBinding;

public class MainActivity3 extends AppCompatActivity {
    //ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main3);
        bottombar();
        replaceFragemt(new MainActivity2());

    }

    public void bottombar()
    {
        final FlareBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setBackground(getResources().getDrawable(R.drawable.bottom_box));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_home_24),"Home","#d1cffc"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_calendar_month_24),"Calender","#d1cffc"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_birth),"Age Calc","#d1cffc"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_baseline_settings_24),"Settings","#d1cffc"));
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                //Toast.makeText(MainActivity.this,"Tab "+ selectedIndex+" Selected.",Toast.LENGTH_SHORT).show();
                if(selectedIndex==0)
                {
                    replaceFragemt(new MainActivity2());
                }

                if(selectedIndex==1)
                {
                    replaceFragemt(new MainActivity());
                }

                if(selectedIndex==2)
                {
                    replaceFragemt(new AgeCalc());
                }
                if(selectedIndex==3)
                {
                    Intent intent = new Intent(MainActivity3.this , SettingsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(MainActivity3.this);
    }


    public void replaceFragemt(Fragment fragments)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragments);
       // transaction.addToBackStack(null);
        transaction.commit();
    }
}