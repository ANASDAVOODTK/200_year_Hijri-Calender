package hijiri.Thaqweemul.materialclock;

import android.Manifest;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.TwoStatePreference;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.jaredrummler.android.colorpicker.ColorPreferenceCompat;

import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity implements WidgetUpdatedInterface {

    private static final String TAG = "SettingsActivity";
    int appWidgetId;
    private WidgetViewCreator widgetViewCreator;


    FrameLayout preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        preview = findViewById(R.id.preview_view);

        widgetViewCreator = new WidgetViewCreator(this, this);
        Toast.makeText(SettingsActivity.this, "Loading ....", Toast.LENGTH_SHORT).show();
        setupPreviewFrame();
        widgetSetup();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "## listen");
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sp_main), MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(widgetViewCreator);
        widgetViewCreator.onSharedPreferenceChanged(sharedPreferences, "");
    }

    @Override
    public void onPause() {
        super.onPause();
        getSharedPreferences(getString(R.string.sp_main), MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(widgetViewCreator);
    }

    @Override
    public void widgetDataUpdated() {
        widgetSetup();
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        ListPreference hrdatePreff;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sp_main), MODE_PRIVATE);

            ColorPreferenceCompat colorPreference = findPreference(getString(R.string.sp_clock_color));
            colorPreference.setDefaultValue(sharedPref.getInt(getString(R.string.sp_clock_color), Color.WHITE));
            colorPreference.setOnPreferenceChangeListener(listener);
            TwoStatePreference show_time = findPreference(getString(R.string.sp_show_time));
            show_time.setChecked(sharedPref.getBoolean(getString(R.string.sp_show_time), true));
            show_time.setOnPreferenceChangeListener(listener);
            EditTextPreference timeFormat = findPreference(getString(R.string.sp_time_format));
            timeFormat.setOnPreferenceChangeListener(listener);

            ListPreference fontList = findPreference(getString(R.string.sp_font));
            fontList.setOnPreferenceChangeListener(listener);
            SeekBarPreference clockSize = findPreference(getString(R.string.sp_time_size));
            clockSize.setOnPreferenceChangeListener(listener);
            ListPreference timeAlign = findPreference(getString(R.string.sp_time_align));
            timeAlign.setOnPreferenceChangeListener(listener);

            ListPreference dateAlign = findPreference(getString(R.string.sp_date_align));
            dateAlign.setOnPreferenceChangeListener(listener);
            SeekBarPreference dateSize = findPreference(getString(R.string.sp_date_size));
            dateSize.setOnPreferenceChangeListener(listener);
            ColorPreferenceCompat dateColorPreference = findPreference(getString(R.string.sp_date_color));
            dateColorPreference.setDefaultValue(sharedPref.getInt(getString(R.string.sp_date_color), Color.WHITE));
            dateColorPreference.setOnPreferenceChangeListener(listener);
            TwoStatePreference show_date = findPreference(getString(R.string.sp_show_date));
            show_date.setChecked(sharedPref.getBoolean(getString(R.string.sp_show_date), true));
            show_date.setOnPreferenceChangeListener(listener);
            EditTextPreference dateFormat = findPreference(getString(R.string.sp_date_format));
            dateFormat.setOnPreferenceChangeListener(listener);


            ListPreference hrdateAlign = findPreference(getString(R.string.hr_date_align));
            hrdateAlign.setOnPreferenceChangeListener(listener);
            SeekBarPreference hrdateSize = findPreference(getString(R.string.hr_date_size));
            hrdateSize.setOnPreferenceChangeListener(listener);
            ColorPreferenceCompat hrdateColorPreference = findPreference(getString(R.string.hr_date_color));
            hrdateColorPreference.setDefaultValue(sharedPref.getInt(getString(R.string.hr_date_color), Color.WHITE));
            hrdateColorPreference.setOnPreferenceChangeListener(listener);
            TwoStatePreference hrshow_date = findPreference(getString(R.string.hr_show_date));
            hrshow_date.setChecked(sharedPref.getBoolean(getString(R.string.hr_show_date), true));
            hrshow_date.setOnPreferenceChangeListener(listener);

            hrdatePreff = findPreference(getString(R.string.datePref));
            hrdatePreff.setOnPreferenceChangeListener(listener);

            ListPreference widgetOrientation = findPreference(getString(R.string.sp_layout));
            widgetOrientation.setOnPreferenceChangeListener(listener);
        }

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sp_main), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (getString(R.string.sp_clock_color).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_clock_color), (int) newValue);
                if (getString(R.string.sp_show_time).equals(preference.getKey()))
                    editor.putBoolean(getString(R.string.sp_show_time), (boolean) newValue);
                if (getString(R.string.sp_font).equals(preference.getKey()))
                    editor.putString(getString(R.string.sp_font), (String) newValue);
                if (getString(R.string.sp_time_format).equals(preference.getKey()))
                    editor.putString(getString(R.string.sp_time_format), (String) newValue);
                if (getString(R.string.sp_time_size).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_time_size), (int) newValue);
                if (getString(R.string.sp_time_align).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_time_align), Integer.parseInt((String) newValue));
                if (getString(R.string.sp_date_align).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_date_align), Integer.parseInt((String) newValue));
                if (getString(R.string.sp_date_size).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_date_size), (int) newValue);
                if (getString(R.string.sp_date_color).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_date_color), (int) newValue);
                if (getString(R.string.sp_show_date).equals(preference.getKey()))
                    editor.putBoolean(getString(R.string.sp_show_date), (boolean) newValue);
                if (getString(R.string.sp_date_format).equals(preference.getKey()))
                    editor.putString(getString(R.string.sp_date_format), (String) newValue);
                if (getString(R.string.hr_date_align).equals(preference.getKey()))
                    editor.putInt(getString(R.string.hr_date_align), Integer.parseInt((String) newValue));
                if (getString(R.string.hr_date_size).equals(preference.getKey()))
                    editor.putInt(getString(R.string.hr_date_size), (int) newValue);
                if (getString(R.string.hr_date_color).equals(preference.getKey()))
                    editor.putInt(getString(R.string.hr_date_color), (int) newValue);
                if (getString(R.string.hr_show_date).equals(preference.getKey()))
                    editor.putBoolean(getString(R.string.hr_show_date), (boolean) newValue);

                if (getString(R.string.sp_layout).equals(preference.getKey()))
                    editor.putInt(getString(R.string.sp_layout), Integer.parseInt((String) newValue));

                if (getString(R.string.datePref).equals(preference.getKey()))
                    editor.putString(getString(R.string.hr_date_format), getDateHr((String) newValue));

                editor.apply();
                return true;
            }
        };

    }

    public static String getDateHr(String i)
    {
        String date = "";
        switch (i) {
            case "1":
                date = "dd,MM,yyyy";
                break;
            case "2":
                date = "dd-MM-yyyy";
                break;
            case "3":
                date = "dd/MM/yyyy";
                break;
            case "4":
                date = "dd,MMMM";
                break;
            case "5":
                date = "dd-MMMM";
                break;
            case "6":
                date = "dd/MMMM";
                break;
            case "7":
                date = "dd,MMMM,yyyy";
                break;
            case "8":
                date = "dd-MMMM-yyyy";
                break;
            case "9":
                date = "dd/MMMM/yyyy";
                break;
        }


        return date;
    }

    private void setupPreviewFrame() {
        ImageView preview = findViewById(R.id.bg);
        if (isReadStoragePermissionGranted()) {
            final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
            preview.setImageDrawable(wallpaperDrawable);
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setupPreviewFrame(); //thats here to setup the preview window after permission to fetch the user wallpaper has being approved
    }

    private void widgetSetup() {
        Log.d(TAG,"## widgetSetup");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        RemoteViews views = widgetViewCreator.createWidgetRemoteView();
        preview.removeAllViews();
        View previewView = views.apply(this,preview);
        preview.addView(previewView);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.savebtn) {
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(ScheduledTask.class,3, TimeUnit.HOURS)
                    .addTag("Strted periodic req")
                    .setInitialDelay(1,TimeUnit.SECONDS)
                    .build();
            WorkManager.getInstance(this).enqueue(periodicWorkRequest);
            finish(); //thats enough. finishing this activity will activate the widget
        }
        return super.onOptionsItemSelected(item);
    }

}