package hijiri.Thaqweemul.materialclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class DataFeedingMain extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextInputEditText etyear;
    String mn;
    String spMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_feeding_main);
        getSupportActionBar().hide();
        etyear=findViewById(R.id.year);
       TextView textView = (TextView) findViewById(R.id.textView);
        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Create Hijri Calender");
        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7e4397"),
                        Color.parseColor("#8e3c81"),
                        Color.parseColor("#ad3366"),
                        Color.parseColor("#cb2940"),
                        Color.parseColor("#d7222d"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        MaterialSpinner spinnerm = (MaterialSpinner) findViewById(R.id.spinnerm);
        spinnerm.setItems("Muharram","Safar","Rabi al-Awwal","Rabi al-Thani","Jumada al-Awwal","Jumada al-Thani","Rajab","Shaban","Ramadan","Shawwal","Dhu al-Qadah","Dhu al-Hijjah");
        spinnerm.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selected " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        MaterialSpinner spinnerw = (MaterialSpinner) findViewById(R.id.spinnerw);
        spinnerw.setItems("Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday");
        spinnerw.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selected " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        MaterialSpinner spinnerd = (MaterialSpinner) findViewById(R.id.spinnerd);
        spinnerd.setItems("28","29","30","31");
        spinnerd.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Selected " + item, Snackbar.LENGTH_LONG).show();
            }
        });


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String year = etyear.getText().toString();
                String month = spinnerm.getText().toString();
                String week = spinnerw.getText().toString();
                String day = spinnerd.getText().toString();


                if (year==null || year.isEmpty())
                {
                    Toast.makeText(DataFeedingMain.this, "Please Enter the Year", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getMonthNumber(month);
                    Senddata(year,month,week,day);

                }
            }
        });


        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = findViewById(R.id.datevr);
                String datevr = editText.getText().toString();
                if (datevr==null || datevr.isEmpty())
                {
                    Toast.makeText(DataFeedingMain.this, "Please enter the data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendDatevr(datevr);
                }
            }
        });
    }


    public void Senddata(String year,String month,String week,String day)
    {


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Calender_Data");

        DataModel dataModel = new DataModel(year,month,week,day,mn);
        reference.child(year+"_"+mn).setValue(dataModel);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    public void getMonthNumber(String month)
    {
        if(month.equals("Muharram"))
        {
            mn="1";
        }
        else if(month.equals("Safar"))
        {
            mn="2";
        }
        else if(month.equals("Rabi al-Awwal"))
        {
            mn="3";
        }
        else if(month.equals("Rabi al-Thani"))
        {
            mn="4";
        }
        else if(month.equals("Jumada al-Awwal"))
        {
            mn="5";
        }
        else if(month.equals("Jumada al-Thani"))
        {
            mn="6";
        }
        else if(month.equals("Rajab"))
        {
            mn="7";
        }
        else if(month.equals("Shaban"))
        {
            mn="8";
        }
        else if(month.equals("Ramadan"))
        {
            mn="9";
        }
        else if(month.equals("Shawwal"))
        {
            mn="10";
        }
        else if(month.equals("Dhu al-Qadah"))
        {
            mn="11";
        }
        else if(month.equals("Dhu al-Hijjah"))
        {
            mn="12";
        }

    }

    public void sendDatevr(String datevr)
    {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Date variable");

        reference.child("datevr").setValue(datevr);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }



}