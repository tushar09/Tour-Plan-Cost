package club.tushar.tourplancost.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.gson.Gson;

import java.util.List;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.adapter.TourAdapter;
import club.tushar.tourplancost.adapter.TourEventCostAdapter;
import club.tushar.tourplancost.databinding.ActivityTourDetailsBinding;
import club.tushar.tourplancost.db.Tour;
import club.tushar.tourplancost.db.TourEventCost;
import club.tushar.tourplancost.utils.Constant;

public class TourDetailsActivity extends AppCompatActivity {

    private ActivityTourDetailsBinding binding;
    private Long id = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getIntent().getLongExtra("id", 0);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(new TourEventCostAdapter(this, Constant.getDbHelper(this).getTourEventCosts(id)));

        List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);
        binding.rv.setAdapter(new TourEventCostAdapter(TourDetailsActivity.this, list));
        int money = 0;
        for (int i = 0; i < list.size(); i++) {
            money += list.get(i).getCost();
        }
        Log.e("log", id + new Gson().toJson(list));
        binding.total.setText(money + "");

        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.etEventName.getEditText().getText().toString().equals("") && !binding.etEventCost.getEditText().getText().toString().equals("")){

                }
                TourEventCost cost = new TourEventCost();
                cost.setCost(Integer.parseInt(binding.etEventCost.getEditText().getText().toString()));
                cost.setEventName(binding.etEventName.getEditText().getText().toString());
                cost.setDate(System.currentTimeMillis());
                cost.setTourId(id);
                Constant.getDbHelper(TourDetailsActivity.this).addTourEventCost(cost);
                List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);
                binding.rv.setAdapter(new TourEventCostAdapter(TourDetailsActivity.this, list));
                int money = 0;
                for (int i = 0; i < list.size(); i++) {
                    money += list.get(i).getCost();
                }
                binding.total.setText(money + "");
                binding.etEventCost.getEditText().setText("");
                binding.etEventName.getEditText().setText("");
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndView(view, binding.llAddItemHolder);
            }
        });
    }

    private void showEndView(View startView, View endView) {
        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.setScrimColor(Color.TRANSPARENT);
        transition.setPathMotion(new MaterialArcMotion());
        transition.setInterpolator(new FastOutSlowInInterpolator());
        transition.setDuration(500);
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                //transition.
            }
        });
        //set the duration....

        //Define the start and the end view
        transition.setStartView(startView);
        transition.setEndView(endView);
        transition.addTarget(endView);

        // Trigger the container transform transition.

        TransitionManager.beginDelayedTransition(binding.root, transition);
        startView.setVisibility(View.GONE);
        endView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (binding.llAddItemHolder.getVisibility() == View.VISIBLE) {
            showEndView(binding.llAddItemHolder, binding.fab);
        } else {
            super.onBackPressed();
        }
    }
}
