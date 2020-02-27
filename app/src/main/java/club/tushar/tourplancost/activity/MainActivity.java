package club.tushar.tourplancost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.adapter.TourAdapter;
import club.tushar.tourplancost.databinding.ActivityMainBinding;
import club.tushar.tourplancost.utils.Constant;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTourActivity.class));
            }
        });

        Log.e("size", Constant.getDbHelper(this).getTourList() + "");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvPlan.setLayoutManager(linearLayoutManager);
        binding.rvPlan.setAdapter(new TourAdapter(this, Constant.getDbHelper(this).getTourList()));

    }
}
