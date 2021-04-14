package club.tushar.tourplancost.model;

import club.tushar.tourplancost.db.TourEventCost;

public class TourDetails {
    private int sectionPosition;
    private int viewType;
    private int total;
    private String date;
    private TourEventCost tourEventCost;

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TourEventCost getTourEventCost() {
        return tourEventCost;
    }

    public void setTourEventCost(TourEventCost tourEventCost) {
        this.tourEventCost = tourEventCost;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
