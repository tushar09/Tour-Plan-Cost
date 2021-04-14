package club.tushar.tourplancost.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    private final String DB_NAME = "tour-list-db";

    private final DaoSession daoSession;
    private DaoMaster daoMaster;

    private TourDao tourDao;
    private TourEventCostDao tourEventCostDao;

    private SQLiteOpenHelper sqLiteOpenHelper;

    public DbHelper(Context context){
        sqLiteOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        daoMaster = new DaoMaster(sqLiteOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();

        tourDao = daoSession.getTourDao();
        tourEventCostDao = daoSession.getTourEventCostDao();
    }


    public List<Tour> getTourList(){
        String query = "SELECT *,sum(COST),  a._id as iddd  FROM TOUR a left join TOUR_EVENT_COST on TOUR_ID = a._id  group by NAME";
        Cursor c = daoSession.getDatabase().rawQuery(query, null);
        List<Tour> tours = new ArrayList<>();
        try{
            if (c.moveToFirst()) {
                do {
                    Tour t = new Tour();
                    t.setId(c.getLong(c.getColumnIndex("iddd")));
                    t.setName(c.getString(c.getColumnIndex("NAME")));
                    t.setStartDate(c.getLong(c.getColumnIndex("START_DATE")));
                    t.setTotal(c.getLong(c.getColumnIndex("sum(COST)")));
                    tours.add(t);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        //return tourDao.queryRaw(query);
        return tours;
    }

    public void updateTour(Tour t){
        tourDao.update(t);
    }

    public List<Tour> getTourListNotSynced(){
        QueryBuilder<Tour> qb = tourDao.queryBuilder();
        return qb.where(TourDao.Properties.Synced.eq(0)).list();
    }

    public long addTour(Tour t){
        return tourDao.insertOrReplace(t);
    }

    public Tour getTourByName(String name){
        QueryBuilder<Tour> qb = tourDao.queryBuilder();
        return qb.where(TourDao.Properties.Name.eq(name)).unique();
    }

    public Tour getTourById(long id){
        QueryBuilder<Tour> qb = tourDao.queryBuilder();
        return qb.where(TourDao.Properties.Id.eq(id)).unique();
    }

    public void setTourSyncFalseById(long id){
        QueryBuilder<Tour> qb = tourDao.queryBuilder();
        Tour t = qb.where(TourDao.Properties.Id.eq(id)).unique();
        t.setSynced(false);
        tourDao.update(t);
    }

    public List<TourEventCost> getTourEventCosts(long id){
        QueryBuilder<TourEventCost> qb = tourEventCostDao.queryBuilder();
        //qb.and(WrongAnswersDao.Properties.FlowId.eq(flowId));
        //return qb.list();
        return qb.where(TourEventCostDao.Properties.TourId.eq(id)).orderDesc(TourEventCostDao.Properties.Date).list();
    }

    public void addTourEventCost(TourEventCost cost){
        tourEventCostDao.insertOrReplaceInTx(cost);
    }

    public void addTourEventCostList(List<TourEventCost> cost){
        tourEventCostDao.insertOrReplaceInTx(cost);
    }

    public void updateTourEventCost(TourEventCost cost){
        tourEventCostDao.update(cost);
    }

    public void updateTourEventCostAsList(List<TourEventCost> cost){
        tourEventCostDao.updateInTx(cost);
    }

    public void deleteTourEventCost(TourEventCost cost){
        tourEventCostDao.delete(cost);
    }
}
