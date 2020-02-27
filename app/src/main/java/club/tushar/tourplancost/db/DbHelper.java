package club.tushar.tourplancost.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import org.greenrobot.greendao.query.QueryBuilder;

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
        QueryBuilder<Tour> qb = tourDao.queryBuilder();
        //qb.and(WrongAnswersDao.Properties.FlowId.eq(flowId));
        //return qb.list();
        return qb.build().list();
    }

    public void addTour(Tour t){
        tourDao.insertOrReplaceInTx(t);
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
