package club.tushar.tourplancost.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.utils.Constant;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.MaterialTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Constant.getSharedPreferences(StarterActivity.this).isLoggedIn()){
                    startActivity(new Intent(StarterActivity.this, MainActivity.class));
                }else {
                    startActivity(new Intent(StarterActivity.this, LoginActivity.class));
                }
                finishAffinity();
            }
        }, 1000);
    }
}