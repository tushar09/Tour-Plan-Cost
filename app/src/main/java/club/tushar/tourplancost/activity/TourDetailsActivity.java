package club.tushar.tourplancost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Binder;
import android.os.Bundle;
import android.view.View;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tour_details);

        id = getIntent().getLongExtra("id", 0);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(new TourEventCostAdapter(this, Constant.getDbHelper(this).getTourEventCosts(id)));

        List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);
        binding.rv.setAdapter(new TourEventCostAdapter(TourDetailsActivity.this, list));
        int money = 0;
        for (int i = 0; i < list.size(); i++) {
            money += list.get(i).getCost();
        }
        binding.total.setText("BDT" + money);

        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.etEventName.getText().toString().equals("") && !binding.etEventCost.getText().toString().equals("")){

                }
                TourEventCost cost = new TourEventCost();
                cost.setCost(Integer.parseInt(binding.etEventCost.getText().toString()));
                cost.setName(binding.etEventName.getText().toString());
                cost.setDate(System.currentTimeMillis());
                cost.setTourId(id);
                Constant.getDbHelper(TourDetailsActivity.this).addTourEventCost(cost);
                List<TourEventCost> list = Constant.getDbHelper(TourDetailsActivity.this).getTourEventCosts(id);
                binding.rv.setAdapter(new TourEventCostAdapter(TourDetailsActivity.this, list));
                int money = 0;
                for (int i = 0; i < list.size(); i++) {
                    money += list.get(i).getCost();
                }
                binding.total.setText("Total BDT" + money);
                binding.etEventCost.setText("");
                binding.etEventName.setText("");
            }
        });
    }
}
