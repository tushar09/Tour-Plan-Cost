package club.tushar.tourplancost.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.adapter.TourAdapter;
import club.tushar.tourplancost.databinding.ActivityMainBinding;
import club.tushar.tourplancost.databinding.AddTourDialogBinding;
import club.tushar.tourplancost.db.Tour;
import club.tushar.tourplancost.db.TourEventCost;
import club.tushar.tourplancost.utils.Constant;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<Tour> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTour();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvPlan.setLayoutManager(linearLayoutManager);
        binding.rvPlan.setAdapter(new TourAdapter(this, list));

        Tour t = Constant.getDbHelper(MainActivity.this).getTourByName(getMonth());
        Tour t2 = Constant.getDbHelper(MainActivity.this).getTourByName(getMonth2());
        if(t == null){
            t = new Tour();
            t.setStartDate(System.currentTimeMillis());
            t.setName(getMonth());
            t.setDescription("");
        }
        if(t2 == null){
            t2 = new Tour();
            t2.setStartDate(System.currentTimeMillis());
            t2.setName(getMonth2());
            t2.setDescription("");
        }
        Constant.getDbHelper(MainActivity.this).addTour(t);
        Constant.getDbHelper(MainActivity.this).addTour(t2);

        setTourList();
    }

    private void setTourList() {
        //Constant.getDbHelper(this).getTourList();
        list.clear();
        list.addAll(Constant.getDbHelper(this).getTourList());
        binding.rvPlan.getAdapter().notifyDataSetChanged();
    }

    private void addTour(){
        final Tour t = Constant.getDbHelper(this).getTourByName(getMonth());
        final AddTourDialogBinding addTourDialogBinding = AddTourDialogBinding.inflate(getLayoutInflater());
        AlertDialog builder = new MaterialAlertDialogBuilder(this,
                R.style.AlertDialogTheme)
                .setView(addTourDialogBinding.getRoot())
                .setTitle(R.string.add_tour)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        TourEventCost cost = new TourEventCost();
                        cost.setCost(Integer.parseInt(addTourDialogBinding.etExpenseAmount.getEditText().getText().toString()));
                        cost.setEventName(addTourDialogBinding.etExpenseName.getEditText().getText().toString());
                        cost.setDate(System.currentTimeMillis());
                        cost.setTourId(t.getId());
                        Constant.getDbHelper(MainActivity.this).addTourEventCost(cost);
                        setTourList();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
    private String getMonth(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private String getMonth2(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return "April 2022";
    }
}
