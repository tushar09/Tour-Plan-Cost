package club.tushar.tourplancost.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import club.tushar.tourplancost.views.StickyHeaderItemDecorator;
import club.tushar.tourplancost.adapter.TourEventCostAdapter;
import club.tushar.tourplancost.databinding.ActivityTourDetailsBinding;
import club.tushar.tourplancost.db.Tour;
import club.tushar.tourplancost.db.TourEventCost;
import club.tushar.tourplancost.model.TourDetails;
import club.tushar.tourplancost.utils.Constant;

public class TourDetailsActivity extends AppCompatActivity {

    private ActivityTourDetailsBinding binding;
    private Long id = 0L;

    private FirebaseFirestore db;
    private List<TourDetails> finalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        id = getIntent().getLongExtra("id", 0);
        List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(linearLayoutManager);

//        DividerItemDecoration dividerDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        binding.rv.addItemDecoration(dividerDecorator);

        TourEventCostAdapter adapter = new TourEventCostAdapter(this, finalList);
        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);
        decorator.attachToRecyclerView(binding.rv);
        binding.rv.setAdapter(adapter);
        populateData(list);

        //binding.rv.setAdapter(new TourEventCostAdapter(TourDetailsActivity.this, list));
        int money = 0;
        for (int i = 0; i < list.size(); i++) {
            money += list.get(i).getCost();
        }
        binding.total.setText(money + "");

        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.etEventName.getEditText().getText().toString().equals("")){
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(0)
                            .playOn(binding.etEventName);
                    return;
                }

                if(binding.etEventCost.getEditText().getText().toString().equals("")){
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(0)
                            .playOn(binding.etEventCost);
                    return;
                }

                TourEventCost cost = new TourEventCost();
                cost.setCost(Integer.parseInt(binding.etEventCost.getEditText().getText().toString()));
                cost.setEventName(binding.etEventName.getEditText().getText().toString());
                cost.setDate(System.currentTimeMillis());
                cost.setTourId(id);
                Constant.getDbHelper(TourDetailsActivity.this).addTourEventCost(cost);
                Constant.getDbHelper(TourDetailsActivity.this).setTourSyncFalseById(id);
                List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);
                populateData(list);
                int money = 0;
                for (int i = 0; i < list.size(); i++) {
                    money += list.get(i).getCost();
                }
                binding.total.setText(money + "");
                binding.etEventCost.getEditText().setText("");
                binding.etEventName.getEditText().requestFocus();
                binding.etEventName.getEditText().setText("");
                editItem();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndView(view, binding.llAddItemHolder);
            }
        });
    }

    public void populateData(List<TourEventCost> mainList) {
        LinkedHashMap<String, List<TourEventCost>> map = new LinkedHashMap<>();
        finalList.clear();

        for (int i = 0; i < mainList.size(); i++) {
            String date = Constant.compareDate(mainList.get(i).getDate());
            List<TourEventCost> list;
            if(map.containsKey(date)){
                list = map.get(date);
            }else {
                list = new ArrayList<>();
            }
            list.add(mainList.get(i));
            map.put(date, list);
        }

        int sectionPosition = 0;
        for (Map.Entry<String, List<TourEventCost>> entry : map.entrySet()) {

            int total = 0;

            TourDetails tourDetails = new TourDetails();
            tourDetails.setDate(entry.getKey());
            tourDetails.setViewType(0);
            tourDetails.setTotal(total);
            tourDetails.setSectionPosition(sectionPosition);
            finalList.add(tourDetails);
            sectionPosition = finalList.indexOf(tourDetails);
            tourDetails.setSectionPosition(sectionPosition);


            for (int i = 0; i < entry.getValue().size(); i++) {
                total += entry.getValue().get(i).getCost();
                TourDetails tourDetails1 = new TourDetails();
                tourDetails1.setDate(Constant.longToAmPm(entry.getValue().get(i).getDate()));
                tourDetails1.setViewType(1);

                tourDetails1.setSectionPosition(sectionPosition);
                TourEventCost tc = entry.getValue().get(i);
                tourDetails1.setTourEventCost(tc);

                finalList.add(tourDetails1);
            }

            tourDetails.setTotal(total);

            Log.e(entry.getKey(), entry.getValue().size() + "");
        }

        binding.rv.getAdapter().notifyDataSetChanged();
        updateCount();
    }

    public void editItem(){
        new SyncData().execute();
    }

    public void backToNormalState(){
        if(binding.llAddItemHolder.getVisibility() == View.VISIBLE){
            showEndView(binding.llAddItemHolder, binding.fab);
        }
    }

    public void updateCount(){
        List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);
        int money = 0;
        for (int i = 0; i < list.size(); i++) {
            money += list.get(i).getCost();
        }
        binding.total.setText(money + "");

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

    private class SyncData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            List<Tour> list = Constant.getDbHelper(TourDetailsActivity.this).getTourListNotSynced();
            DocumentReference d = db.collection(Constant.FIRESTORE_MAIN_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            for (int i = 0; i < list.size(); i++) {
                Tour t = list.get(i);
                HashMap<String, String> data = new HashMap<>();
                List<TourEventCost> tourEventCosts = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(t.getId());
                if(tourEventCosts.size() != 0){
                    data.put(t.getName(), new Gson().toJson(tourEventCosts));
                    d.set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    t.setSynced(true);
                                    Constant.getDbHelper(TourDetailsActivity.this).updateTour(t);
                                }
                            });
                }

            }
            return null;
        }
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
