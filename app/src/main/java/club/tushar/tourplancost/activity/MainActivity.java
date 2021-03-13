package club.tushar.tourplancost.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

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

        setTourList();
    }

    private void setTourList() {
        list.clear();
        list.addAll(Constant.getDbHelper(this).getTourList());
        binding.rvPlan.getAdapter().notifyDataSetChanged();
    }

    private void addTour(){
        final AddTourDialogBinding addTourDialogBinding = AddTourDialogBinding.inflate(getLayoutInflater());
        AlertDialog builder = new MaterialAlertDialogBuilder(this,
                R.style.AlertDialogTheme)
                .setView(addTourDialogBinding.getRoot())
                .setTitle(R.string.add_tour)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Tour t = new Tour();
                        t.setStartDate(System.currentTimeMillis());
                        t.setName(addTourDialogBinding.etTourName.getEditText().getText().toString());
                        t.setDescription(addTourDialogBinding.etTourDescription.getEditText().toString());
                        Constant.getDbHelper(MainActivity.this).addTour(t);
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
}
