package hijiri.Thaqweemul.materialclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class AgeCalc extends Fragment {

    public AgeCalc() {
        // Required empty public constructor
    }


    boolean run = true;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_age_calc, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        v = getView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        ImageView imageView = v.findViewById(R.id.imgLogin);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), LoginActivity.class );
                startActivity(intent);
            }
        });

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Choose Your Date Of Birth");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        CircularProgressButton btn = (CircularProgressButton) v.findViewById(R.id.btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.startAnimation();
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendar.setTimeInMillis((Long) selection);
                        SimpleDateFormat formatd = new SimpleDateFormat("dd");
                        SimpleDateFormat formatm = new SimpleDateFormat("M");
                        SimpleDateFormat formatm1 = new SimpleDateFormat("MM");
                        SimpleDateFormat formaty = new SimpleDateFormat("yyyy");
                        String day  = formatd.format(calendar.getTime());
                        String month  = formatm.format(calendar.getTime());
                        String month1  = formatm1.format(calendar.getTime());
                        String year  = formaty.format(calendar.getTime());
                        run = true;
                        try {
                            getDate(year,month,day,month1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        btn.stopAnimation();
                        btn.revertAnimation();
                    }
                });


    }




    private String readJSONDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.hrdat);
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


    public void getDate(String y,String m,String d,String m1) throws IOException {
        String jsonString=readJSONDataFromFile();
        String hjrDate = "";
         try {
            JSONArray valarray = new JSONArray(jsonString);
            for (int i = 0; i < valarray.length(); i++) {

                String str = valarray.getJSONObject(i).getString("ggdate");
                Log.d("fdfdfdfd",y+"-"+m+"-"+d);
                if(str.equals(y+"-"+m+"-"+d))
                {
                    if(run)
                    {
                        String year = valarray.getJSONObject(i).getString("year");
                        String month = valarray.getJSONObject(i).getString("month");
                        String day = valarray.getJSONObject(i).getString("hijridate");
                        hjrDate = day+"-"+month+"-"+year;
                        LinearLayout linearLayout = v.findViewById(R.id.llout);
                        TextView hgdate = v.findViewById(R.id.hjdate);
                        TextView ggdate = v.findViewById(R.id.ggdate);
                        hgdate.setText(hjrDate);
                        ggdate.setText(d+"-"+m+"-"+y);

                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-M-yyyy");
                        Date date = null;
                        try {
                            date = inFormat.parse(d+"-"+m+"-"+y);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                        String week = outFormat.format(date);
                        TextView ggday = v.findViewById(R.id.ggday);
                        ggday.setText(week);

                        String tdydate = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                        int age = Integer.parseInt(tdydate)-Integer.parseInt(y);

                        TextView txtage = v.findViewById(R.id.age);
                        txtage.setText("Your Age : "+age);

                        linearLayout.setVisibility(View.VISIBLE);
                        run=false;
                    }
                }
                else if(str.equals(y+"-"+m1+"-"+d))
                {
                    if(run)
                    {
                        String year = valarray.getJSONObject(i).getString("year");
                        String month = valarray.getJSONObject(i).getString("month");
                        String day = valarray.getJSONObject(i).getString("hijridate");
                        hjrDate = day+"-"+month+"-"+year;
                        LinearLayout linearLayout = v.findViewById(R.id.llout);
                        TextView hgdate = v.findViewById(R.id.hjdate);
                        TextView ggdate = v.findViewById(R.id.ggdate);
                        hgdate.setText(hjrDate);
                        ggdate.setText(d+"-"+m1+"-"+y);

                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-M-yyyy");
                        Date date = null;
                        try {
                            date = inFormat.parse(d+"-"+m+"-"+y);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                        String week = outFormat.format(date);
                        TextView ggday = v.findViewById(R.id.ggday);
                        ggday.setText(week);

                        String tdydate = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                        int age = Integer.parseInt(tdydate)-Integer.parseInt(y);

                        TextView txtage = v.findViewById(R.id.age);
                        txtage.setText("Your Age : "+age);

                        linearLayout.setVisibility(View.VISIBLE);
                        run=false;
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }

    }
}