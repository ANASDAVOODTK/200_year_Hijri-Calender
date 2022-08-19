package hijiri.Thaqweemul.materialclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    EditText un,psw;
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        un = findViewById(R.id.uName);
        psw = findViewById(R.id.etpsw);
        loginBtn= findViewById(R.id.loginbtn);

        TextView textView = (TextView) findViewById(R.id.textView);
        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Login to Admin");
        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#7e4397"),
                        Color.parseColor("#8e3c81"),
                        Color.parseColor("#ad3366"),
                        Color.parseColor("#cb2940"),
                        Color.parseColor("#d7222d"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = un.getText().toString();
                String password = psw.getText().toString();

                if(userName == null || userName.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Your User Name", Toast.LENGTH_SHORT).show();
                }
                else if(password == null || password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(userName.equals("admin")&&password.equals("1234"))
                    {
                        Intent intent = new Intent(LoginActivity.this,DataFeedingMain.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Incorrect User Name or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}