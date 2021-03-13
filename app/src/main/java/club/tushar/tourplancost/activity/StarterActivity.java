package club.tushar.tourplancost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import club.tushar.tourplancost.utils.Constant;

public class StarterActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        if(Constant.getSharedPreferences(this).isLoggedIn()){
            startActivity(new Intent(this, MainActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finishAffinity();
    }
}