package club.tushar.tourplancost.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MaterialTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(Constant.getSharedPreferences(this).isLoggedIn()){
            start();
        }else {
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        }

    }

    private void start(){
        db = FirebaseFirestore.getInstance();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTour();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvPlan.setLayoutManager(linearLayoutManager);
        binding.rvPlan.setAdapter(new TourAdapter(this, list));

        if(Constant.getSharedPreferences(this).isFirestoreDataPullDone()){
            setTourList();
        }else {
            pullFirestoreData();
        }

    }

    private void pullFirestoreData() {
        new PullFirestoreData().execute();
    }

    private void setTourList() {
        Tour t = Constant.getDbHelper(MainActivity.this).getTourByName(getMonth());
        if(t == null){
            t = new Tour();
            t.setStartDate(System.currentTimeMillis());
            t.setName(getMonth());
            t.setDescription("");
            t.setSynced(false);
        }
        Constant.getDbHelper(MainActivity.this).addTour(t);

        list.clear();
        list.addAll(Constant.getDbHelper(MainActivity.this).getTourList());
        binding.rvPlan.getAdapter().notifyDataSetChanged();
        syncData(list);
    }

    private void syncData(List<Tour> list) {
        new SyncData(list).execute();
    }

    private void addTour(){
        final Tour t = Constant.getDbHelper(this).getTourByName(getMonth());
        final AddTourDialogBinding addTourDialogBinding = AddTourDialogBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new MaterialAlertDialogBuilder(this,
                R.style.AlertDialogTheme)
                .setView(addTourDialogBinding.getRoot())
                .setTitle(R.string.add_tour)
                .setPositiveButton(R.string.add, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(addTourDialogBinding.etExpenseName.getEditText().getText().toString().equals("")){
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(0)
                                    .playOn(addTourDialogBinding.etExpenseName);
                            return;
                        }

                        if(addTourDialogBinding.etExpenseAmount.getEditText().getText().toString().equals("")){
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(0)
                                    .playOn(addTourDialogBinding.etExpenseAmount);
                            return;
                        }

                        TourEventCost cost = new TourEventCost();
                        cost.setCost(Integer.parseInt(addTourDialogBinding.etExpenseAmount.getEditText().getText().toString()));
                        cost.setEventName(addTourDialogBinding.etExpenseName.getEditText().getText().toString());
                        cost.setDate(System.currentTimeMillis());
                        cost.setTourId(t.getId());
                        Constant.getDbHelper(MainActivity.this).addTourEventCost(cost);
                        t.setSynced(false);
                        Constant.getDbHelper(MainActivity.this).updateTour(t);
                        setTourList();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }
    private String getMonth(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private class SyncData extends AsyncTask<String, String, String>{

        private List<Tour> list;

        public SyncData(List<Tour> list) {
            this.list = list;
        }

        @Override
        protected String doInBackground(String... strings) {
            List<Tour> list = Constant.getDbHelper(MainActivity.this).getTourListNotSynced();
            DocumentReference d = db.collection(Constant.FIRESTORE_MAIN_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            for (int i = 0; i < list.size(); i++) {
                Tour t = list.get(i);
                HashMap<String, String> data = new HashMap<>();
                List<TourEventCost> tourEventCosts = Constant.getDbHelper(MainActivity.this).getTourEventCosts(t.getId());
                if(tourEventCosts.size() != 0){
                    data.put(t.getName(), new Gson().toJson(tourEventCosts));
                    d.set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    t.setSynced(true);
                                    Constant.getDbHelper(MainActivity.this).updateTour(t);
                                }
                            });
                }

            }
            return null;
        }
    }

    private class PullFirestoreData extends AsyncTask<String, String, String>{

        private ProgressDialog progressDialog;

        public PullFirestoreData() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setTourList();
            Constant.getSharedPreferences(MainActivity.this).setFirestoreDataPull(true);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            list.clear();
            list.addAll(Constant.getDbHelper(MainActivity.this).getTourList());
            binding.rvPlan.getAdapter().notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {
            DocumentReference d = db.collection(Constant.FIRESTORE_MAIN_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            List<TourEventCost> list = new Gson().fromJson(entry.getValue().toString(), new TypeToken<List<TourEventCost>>(){}.getType());
                            Log.e(entry.getKey(), entry.getValue().toString());
                            Tour t = new Tour();
                            t.setSynced(true);
                            t.setName(entry.getKey());
                            t.setDescription("");
                            t.setStartDate(Constant.dateToLong(entry.getKey()));
                            t.setId(list.get(0).getTourId());
                            Constant.getDbHelper(MainActivity.this).addTour(t);
                            Constant.getDbHelper(MainActivity.this).addTourEventCostList(list);
                            publishProgress();
                        }


                    } else {
                        Log.e("pull", "No such document");
                    }
                }
            });
            return null;
        }
    }
}
