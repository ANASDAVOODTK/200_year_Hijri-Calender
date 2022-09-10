package hijiri.Thaqweemul.materialclock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends Fragment {

    public MainActivity() {
        // Required empty public constructor
    }

    int[] textViewIds = new int[] {R.id.txt0,R.id.txt1,R.id.txt2,R.id.txt3,R.id.txt4,R.id.txt5,R.id.txt6,R.id.txt7,R.id.txt8,
            R.id.txt9,R.id.txt10, R.id.txt11,R.id.txt12,R.id.txt13,R.id.txt14,R.id.txt15,
            R.id.txt16,R.id.txt17,R.id.txt18,R.id.txt19,R.id.txt20,R.id.txt21,R.id.txt22,
            R.id.txt23,R.id.txt24,R.id.txt25,R.id.txt26,R.id.txt27,R.id.txt28,R.id.txt29,
            R.id.txt30,R.id.txt31,R.id.txt32,R.id.txt33,R.id.txt34,R.id.txt35};


    String islamicDate;
    String islamicDate1;
    TextView monthName;
    ImageView front,back;
    int clear;
    int monthn;
    int yearn;
    DatabaseReference reff;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        v = getView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        SharedPreferences prefs = getActivity().getSharedPreferences("datevr", Context.MODE_PRIVATE);
        String year = prefs.getString("year", "");
        String month = prefs.getString("month", "");
        islamicDate = prefs.getString("day", "");
        islamicDate1 = year+"_"+month;

        monthName = v.findViewById(R.id.monthName);
        front=v.findViewById(R.id.front);
        back=v.findViewById(R.id.back);



        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthn=monthn+1;
                if(monthn==13)
                {
                    monthn=1;
                    yearn =yearn+1;
                    islamicDate1= yearn+"_"+monthn;
                }
                else
                {
                    islamicDate1= yearn+"_"+monthn;
                }

                getdata(1);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)v.findViewById(textViewIds[clear])).setTextColor(getResources().getColor(R.color.black));
                        ((TextView)v.findViewById(textViewIds[clear])).setBackgroundColor(getResources().getColor(R.color.background));

                    }
                }, 1000);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthn=monthn-1;
                if(monthn==0)
                {
                    monthn=12;
                    yearn =yearn-1;
                    islamicDate1= yearn+"_"+monthn;
                }
                else
                {
                    islamicDate1= yearn+"_"+monthn;
                }

                getdata(1);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)v.findViewById(textViewIds[clear])).setTextColor(getResources().getColor(R.color.black));
                        ((TextView)v.findViewById(textViewIds[clear])).setBackgroundColor(getResources().getColor(R.color.background));

                    }
                }, 1000);
            }
        });


        getdata(0);
    }






    public void getdata(int check){

        reff= FirebaseDatabase.getInstance().getReference().child("Calender_Data").child(islamicDate1);
        reff.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                int x= (int) snapshot.getChildrenCount();
                if(x>0)
                {
                    String startday = Objects.requireNonNull(snapshot.child("startday").getValue()).toString();
                    String endday = Objects.requireNonNull(snapshot.child("endday").getValue()).toString();
                    String month = Objects.requireNonNull(snapshot.child("month").getValue()).toString();
                    String monthN = Objects.requireNonNull(snapshot.child("mn").getValue()).toString();
                    String year = Objects.requireNonNull(snapshot.child("year").getValue()).toString();

                    monthn = Integer.parseInt(monthN);
                    yearn = Integer.parseInt(year);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate start = LocalDate.parse(startday,formatter);
                    LocalDate end = LocalDate.parse(endday,formatter);

                    SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    try {
                        date = inFormat.parse(startday);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String week = outFormat.format(date);

                    int dayNumber = Math.toIntExact(ChronoUnit.DAYS.between(start, end));

                    monthName.setText(month+"-"+yearn);


                    //-------------If Friday

                    if(week.equals("Monday"))
                    {

                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }
                        for(int i = 1; i< dayNumber+1; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i));
                            if(islamicDate.equals(String.valueOf(i))&&check==0)
                            {
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));


                            }



                        }

                    }
                    else if(week.equals("Tuesday"))
                    {

                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }
                        for(int i = 2; i< dayNumber+2; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-1));
                            if(islamicDate.equals(String.valueOf(i-1))&&check==0)
                            {
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-1));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));
                            }


                        }
                    }
                    else if(week.equals("Wednesday"))
                    {

                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }
                        for(int i = 3; i< dayNumber+3; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-2));
                            if(islamicDate.equals(String.valueOf(i-2))&&check==0)
                            {
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-2));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));
                            }



                        }
                    }
                    else if(week.equals("Thursday"))
                    {
                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }
                        for(int i = 4; i< dayNumber +4; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-3));
                            if(islamicDate.equals(String.valueOf(i-3))&&check==0)
                            {
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-3));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));
                            }

                        }
                    }
                    else if(week.equals("Friday"))
                    {
                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }
                        for(int i = 5; i< dayNumber +5; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-4));
                            if(islamicDate.equals(String.valueOf(i-4))&&check==0)
                            {
                                //Toast.makeText(MainActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-4));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));
                            }

                        }
                    }
                    else if(week.equals("Saturday"))
                    {
                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }

                        for(int i = 6; i< dayNumber +6; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-5));
                            ((TextView)v.findViewById(textViewIds[i])).setVisibility(View.VISIBLE);
                            ((TextView)v.findViewById(textViewIds[0])).setVisibility(View.INVISIBLE);
                            if(islamicDate.equals(String.valueOf(i-5))&&check==0)
                            {
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i-5));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));
                            }

                        }
                    }
                    else if(week.equals("Sunday"))
                    {
                        for(int i=0;i<36;i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.gray));
                            ((TextView)v.findViewById(textViewIds[i])).setBackgroundColor(getResources().getColor(R.color.background));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf("*"));

                        }
                        for(int i = 0; i< dayNumber; i++ )
                        {
                            ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.black));
                            ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i+1));
                            if(islamicDate.equals(String.valueOf(i+1))&&check==0)
                            {
                                clear=i;
                                ((TextView)v.findViewById(textViewIds[i])).setText(String.valueOf(i+1));
                                ((TextView)v.findViewById(textViewIds[i])).setTextColor(getResources().getColor(R.color.white));
                                ((TextView)v.findViewById(textViewIds[i])).setBackground(getResources().getDrawable(R.drawable.layout_cr));
                            }


                        }

                    }



                }
                else
                {
                    Toast.makeText(getActivity(), "This year or month Not added from admin", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}