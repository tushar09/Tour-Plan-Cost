package club.tushar.tourplancost.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuhart.stickyheader.StickyAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;

import club.tushar.tourplancost.R;
import club.tushar.tourplancost.activity.TourDetailsActivity;
import club.tushar.tourplancost.databinding.AddTourDialogBinding;
import club.tushar.tourplancost.databinding.RowHeaderBinding;
import club.tushar.tourplancost.databinding.RowTourDetailsBinding;
import club.tushar.tourplancost.db.TourEventCost;
import club.tushar.tourplancost.model.TourDetails;
import club.tushar.tourplancost.utils.Constant;

public class TourEventCostAdapterV2 extends StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder> {

    private Context context;
    private List<TourDetails> tours;
    private SimpleDateFormat sdf;

    private boolean multiSelect;

    public TourEventCostAdapterV2(Context context, List<TourDetails> tours) {
        this.context = context;
        this.tours = tours;
        sdf = new SimpleDateFormat("dd MMM hh:mm aa");
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        return tours.get(itemPosition).getSectionPosition();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int headerPosition) {
        RowTourHeader h = (RowTourHeader) holder;
        h.binding.tvDate.setText(tours.get(headerPosition).getDate());
        h.binding.tvCost.setText(tours.get(headerPosition).getTotal() + "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return createViewHolder(parent, 0);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            RowHeaderBinding binding = RowHeaderBinding.inflate(LayoutInflater.from(context), parent, false);
            return new RowTourHeader(binding);
        }else {
            RowTourDetailsBinding binding = RowTourDetailsBinding.inflate(LayoutInflater.from(context), parent, false);
            return new RowTour(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(tours.get(position).getViewType() == 0){
            RowTourHeader h = (RowTourHeader) holder;
            h.binding.tvDate.setText(tours.get(position).getDate());
            h.binding.tvCost.setText(tours.get(position).getTotal() + "");
        }else {
            RowTour h = (RowTour) holder;
            h.binding.tvName.setText(position + " view " + getItemViewType(position));
            h.binding.tvName.setText(tours.get(position).getTourEventCost().getEventName());
            h.binding.tvDate.setText(tours.get(position).getDate());
            h.binding.tvCost.setText(tours.get(position).getTourEventCost().getCost() + "");

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

    }

    private void editItem(TourDetails tourDetails) {
        final AddTourDialogBinding addTourDialogBinding = AddTourDialogBinding.inflate(LayoutInflater.from(context));
        addTourDialogBinding.etExpenseName.getEditText().setText(tourDetails.getTourEventCost().getEventName());
        addTourDialogBinding.etExpenseAmount.getEditText().setText(tourDetails.getTourEventCost().getCost() + "");
        AlertDialog dialog = new MaterialAlertDialogBuilder(context,
                R.style.AlertDialogTheme)
                .setView(addTourDialogBinding.getRoot())
                .setTitle("Edit " + tourDetails.getTourEventCost().getEventName())
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null).create();
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

                        tourDetails.getTourEventCost().setEventName(addTourDialogBinding.etExpenseName.getEditText().getText().toString());
                        tourDetails.getTourEventCost().setCost(Integer.parseInt(addTourDialogBinding.etExpenseAmount.getEditText().getText().toString()));
                        Constant.getDbHelper(context).updateTourEventCost(tourDetails.getTourEventCost());
                        Constant.getDbHelper(context).setTourSyncFalseById(tourDetails.getTourEventCost().getTourId());
                        notifyDataSetChanged();
                        ((TourDetailsActivity)context).editItem();
                        ((TourDetailsActivity)context).populateData(Constant.getDbHelper(context).getTourEventCosts(tourDetails.getTourEventCost().getTourId()));
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void deleteItem(TourDetails tourDetails) {
        new AlertDialog.Builder(context)
                .setTitle("Delete " + tourDetails.getTourEventCost().getEventName())
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.getDbHelper(context).deleteTourEventCost(tourDetails.getTourEventCost());
                        Constant.getDbHelper(context).setTourSyncFalseById(tourDetails.getTourEventCost().getTourId());
                        tours.remove(tourDetails);
                        notifyDataSetChanged();
                        ((TourDetailsActivity)context).populateData(Constant.getDbHelper(context).getTourEventCosts(tourDetails.getTourEventCost().getTourId()));
                        ((TourDetailsActivity)context).updateCount();
                        ((TourDetailsActivity)context).editItem();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tours.get(position).getViewType();
    }


    public class RowTour extends RecyclerView.ViewHolder{

        RowTourDetailsBinding binding;

        public RowTour(RowTourDetailsBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class RowTourHeader extends RecyclerView.ViewHolder{

        RowHeaderBinding binding;

        public RowTourHeader(RowHeaderBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
