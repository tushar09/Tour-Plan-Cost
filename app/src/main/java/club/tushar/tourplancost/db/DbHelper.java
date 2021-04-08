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
        //QueryBuilder<Tour> qb = tourDao.queryBuilder();
        //qb.join(TourEventCost.class, TourEventCostDao.Properties.TourId);
        //qb.and(WrongAnswersDao.Properties.FlowId.eq(flowId));
        //return qb.list();
        //String query = " LEFT JOIN TOUR_EVENT_COST ON T._id = TOUR_EVENT_COST.TOUR_ID";
        //String query = "SELECT " + TourDao.Properties.Name.columnName + " FROM " + TourDao.TABLENAME + "  LEFT JOIN TOUR_EVENT_COST ON " + TourDao.Properties.Id.columnName + " = " + TourEventCostDao.Properties.TourId.columnName;
        //String query = "SELECT *, a._id as iddd, sum(" + TourEventCostDao.Properties.Cost.columnName + ")" + " FROM " + TourDao.TABLENAME + " a  LEFT JOIN TOUR_EVENT_COST ON a." + TourDao.Properties.Id.columnName + " = " + TourEventCostDao.Properties.TourId.columnName;
        String query = "SELECT *,sum(COST),  a._id as iddd  FROM TOUR a left join TOUR_EVENT_COST on TOUR_ID = a._id  group by NAME";
        Log.e("quesr", query);
        Cursor c = daoSession.getDatabase().rawQuery(query, null);
        for (int i = 0; i < c.getColumnNames().length; i++) {
            Log.e("name", c.getColumnName(i));
        }

        List<Tour> tours = new ArrayList<>();

        try{
            if (c.moveToFirst()) {
                do {
                    Tour t = new Tour();
                    t.setId(c.getLong(c.getColumnIndex("iddd")));
                    t.setName(c.getString(c.getColumnIndex("NAME")));
                    t.setStartDate(c.getLong(c.getColumnIndex("START_DATE")));
                    t.setTotal(c.getLong(c.getColumnIndex("sum(COST)")));
                    //Log.e(c.getString(c.getColumnIndex("NAME")), c.getInt(c.getColumnIndex("sum(COST)")) + "");
                    tours.add(t);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        //return tourDao.queryRaw(query);
        return tours;
    }

    public void addTour(Tour t){
        tourDao.insertOrReplace(t);
    }

    public Tour getTourByName(String name){
        QueryBuilder<Tour> qb = tourDao.queryBuilder();
        return qb.where(TourDao.Properties.Name.eq(name)).unique();
    }

    public List<TourEventCost> getTourEventCosts(long id){
        QueryBuilder<TourEventCost> qb = tourEventCostDao.queryBuilder();
        //qb.and(WrongAnswersDao.Properties.FlowId.eq(flowId));
        //return qb.list();
        return qb.where(TourEventCostDao.Properties.TourId.eq(id)).orderDesc(TourEventCostDao.Properties.Id).list();
    }

    public void addTourEventCost(TourEventCost cost){
        tourEventCostDao.insertOrReplaceInTx(cost);
    }

}
