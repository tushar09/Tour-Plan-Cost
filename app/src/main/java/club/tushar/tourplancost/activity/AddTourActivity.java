package club.tushar.tourplancost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.databinding.ActivityAddTourBinding;
import club.tushar.tourplancost.db.Tour;
import club.tushar.tourplancost.utils.Constant;

public class AddTourActivity extends AppCompatActivity {

    private ActivityAddTourBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_tour);

        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTour();
            }
        });
    }

    private void addTour() {
        Tour t = new Tour();
        t.setStartDate(System.currentTimeMillis());
        t.setName(binding.etName.getText().toString());
        t.setDescription(binding.etDescription.getText().toString());
        Constant.getDbHelper(this).addTour(t);
        finish();
    }
}
