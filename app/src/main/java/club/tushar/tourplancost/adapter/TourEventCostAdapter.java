package club.tushar.tourplancost.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.activity.MainActivity;
import club.tushar.tourplancost.activity.TourDetailsActivity;
import club.tushar.tourplancost.databinding.AddTourDialogBinding;
import club.tushar.tourplancost.databinding.RowTourBinding;
import club.tushar.tourplancost.databinding.RowTourDetailsBinding;
import club.tushar.tourplancost.db.Tour;
import club.tushar.tourplancost.db.TourEventCost;
import club.tushar.tourplancost.utils.Constant;

public class TourEventCostAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<TourEventCost> tours;
    private SimpleDateFormat sdf;

    private boolean multiSelect;

    public TourEventCostAdapter(Context context, List<TourEventCost> tours) {
        this.context = context;
        this.tours = tours;
        sdf = new SimpleDateFormat("dd MMM hh:mm aa");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        RowTourDetailsBinding binding = RowTourDetailsBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new RowTour(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        RowTour h = (RowTour) holder;
        h.binding.tvName.setText(tours.get(position).getEventName());
        h.binding.tvDate.setText(sdf.format(new Date(tours.get(position).getDate())));
        h.binding.tvCost.setText(tours.get(position).getCost() + "");


        h.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TourDetailsActivity)context).backToNormalState();
            }
        });

        h.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((TourDetailsActivity)context).backToNormalState();
                PopupMenu pm = new PopupMenu(context, h.binding.v);
                try {
                    Field[] fields = pm.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(pm);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper
                                    .getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod(
                                    "setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu());

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.edit){
                            editItem(tours.get(position));
                        }else if(item.getItemId() == R.id.delete){
                            deleteItem(tours.get(position));
                        }
                        return false;
                    }
                });

                pm.show();
                return false;
            }
        });
    }


    private void editItem(TourEventCost tourEventCost) {
        final AddTourDialogBinding addTourDialogBinding = AddTourDialogBinding.inflate(LayoutInflater.from(context));
        addTourDialogBinding.etExpenseName.getEditText().setText(tourEventCost.getEventName());
        addTourDialogBinding.etExpenseAmount.getEditText().setText(tourEventCost.getCost() + "");
        AlertDialog builder = new MaterialAlertDialogBuilder(context,
                R.style.AlertDialogTheme)
                .setView(addTourDialogBinding.getRoot())
                .setTitle("Edit " + tourEventCost.getEventName())
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(addTourDialogBinding.etExpenseName.getEditText().toString().equals("")){
                            return;
                        }
                        tourEventCost.setEventName(addTourDialogBinding.etExpenseName.getEditText().getText().toString());
                        tourEventCost.setCost(Integer.parseInt(addTourDialogBinding.etExpenseAmount.getEditText().getText().toString()));
                        Constant.getDbHelper(context).updateTourEventCost(tourEventCost);
                        Constant.getDbHelper(context).setTourSyncFalseById(tourEventCost.getTourId());
                        notifyDataSetChanged();
                        ((TourDetailsActivity)context).editItem();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void deleteItem(TourEventCost tourEventCost) {
        new AlertDialog.Builder(context)
                .setTitle("Delete " + tourEventCost.getEventName())
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.getDbHelper(context).deleteTourEventCost(tourEventCost);
                        Constant.getDbHelper(context).setTourSyncFalseById(tourEventCost.getTourId());
                        tours.remove(tourEventCost);
                        notifyDataSetChanged();
                        ((TourDetailsActivity)context).updateCount();
                        ((TourDetailsActivity)context).editItem();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
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
