package club.tushar.tourplancost.db;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TOUR_EVENT_COST".
*/
public class TourEventCostDao extends AbstractDao<TourEventCost, Long> {

    public static final String TABLENAME = "TOUR_EVENT_COST";

    /**
     * Properties of entity TourEventCost.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TourId = new Property(1, Long.class, "tourId", false, "TOUR_ID");
        public final static Property EventName = new Property(2, String.class, "eventName", false, "EVENT_NAME");
        public final static Property Date = new Property(3, Long.class, "date", false, "DATE");
        public final static Property Cost = new Property(4, Integer.class, "cost", false, "COST");
    }

    private Query<TourEventCost> tour_TourTotalCostQuery;

    public TourEventCostDao(DaoConfig config) {
        super(config);
    }
    
    public TourEventCostDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TOUR_EVENT_COST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TOUR_ID\" INTEGER," + // 1: tourId
                "\"EVENT_NAME\" TEXT," + // 2: eventName
                "\"DATE\" INTEGER," + // 3: date
                "\"COST\" INTEGER);"); // 4: cost
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TOUR_EVENT_COST\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TourEventCost entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long tourId = entity.getTourId();
        if (tourId != null) {
            stmt.bindLong(2, tourId);
        }
 
        String eventName = entity.getEventName();
        if (eventName != null) {
            stmt.bindString(3, eventName);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date);
        }
 
        Integer cost = entity.getCost();
        if (cost != null) {
            stmt.bindLong(5, cost);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TourEventCost entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long tourId = entity.getTourId();
        if (tourId != null) {
            stmt.bindLong(2, tourId);
        }
 
        String eventName = entity.getEventName();
        if (eventName != null) {
            stmt.bindString(3, eventName);
        }
 
        Long date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date);
        }
 
        Integer cost = entity.getCost();
        if (cost != null) {
            stmt.bindLong(5, cost);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TourEventCost readEntity(Cursor cursor, int offset) {
        TourEventCost entity = new TourEventCost( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // tourId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // eventName
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // date
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // cost
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TourEventCost entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTourId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setEventName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setCost(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TourEventCost entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TourEventCost entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TourEventCost entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "TourTotalCost" to-many relationship of Tour. */
    public List<TourEventCost> _queryTour_TourTotalCost(Long tourId) {
        synchronized (this) {
            if (tour_TourTotalCostQuery == null) {
                QueryBuilder<TourEventCost> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TourId.eq(null));
                queryBuilder.orderRaw("T.'DATE' DESC");
                tour_TourTotalCostQuery = queryBuilder.build();
            }
        }
        Query<TourEventCost> query = tour_TourTotalCostQuery.forCurrentThread();
        query.setParameter(0, tourId);
        return query.list();
    }

}
