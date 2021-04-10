package club.tushar.tourplancost.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import club.tushar.tourplancost.db.DbHelper;

public class Constant{

    public static DbHelper getDbHelper(Context context){
        return new DbHelper(context);
    }

    public static SPreferences sPreferences;

    public static String FIRESTORE_MAIN_COLLECTION = "users";

    public static final SPreferences getSharedPreferences(Context context){
        if(sPreferences == null){
            sPreferences = new SPreferences(context);
        }
        return sPreferences;
    }

    public static long dateToLong(String date){
        SimpleDateFormat f = new SimpleDateFormat("MMMM yyyy");
        try {
            Date d = f.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }
}
