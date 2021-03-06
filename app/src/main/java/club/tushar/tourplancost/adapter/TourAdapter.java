package club.tushar.tourplancost.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.activity.TourDetailsActivity;
import club.tushar.tourplancost.databinding.RowTourBinding;
import club.tushar.tourplancost.db.Tour;

public class TourAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<Tour> tours;

    public TourAdapter(Context context, List<Tour> tours) {
        this.context = context;
        this.tours = tours;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        RowTourBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_tour, viewGroup, false);
        return new RowTour(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
        RowTour h = (RowTour) holder;
        h.binding.tvName.setText(tours.get(position).getName());
        h.binding.tvDate.setText(tours.get(position).getStartDate() + "");
        h.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TourDetailsActivity.class)
                        .putExtra("id", tours.get(position).getId())
                );
            }
        });
    }

    @Override
    public int getItemCount(){
        return tours.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class RowTour extends RecyclerView.ViewHolder{

        RowTourBinding binding;

        public RowTour(RowTourBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
