package club.tushar.tourplancost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.databinding.RowTourBinding;
import club.tushar.tourplancost.databinding.RowTourDetailsBinding;
import club.tushar.tourplancost.db.Tour;
import club.tushar.tourplancost.db.TourEventCost;

public class TourEventCostAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<TourEventCost> tours;
    private SimpleDateFormat sdf;

    public TourEventCostAdapter(Context context, List<TourEventCost> tours) {
        this.context = context;
        this.tours = tours;
        sdf = new SimpleDateFormat("dd MMM hh:mm aa");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        //RowTourDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_tour_details, viewGroup, false);
        return new RowTour(null);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        RowTour h = (RowTour) holder;
        h.binding.tvName.setText(tours.get(position).getName());
        h.binding.tvDate.setText(sdf.format(new Date(tours.get(position).getDate())));
        h.binding.tvCost.setText(tours.get(position).getCost() + "");

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

        RowTourDetailsBinding binding;

        public RowTour(RowTourDetailsBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
