package club.tushar.tourplancost.utils;

import android.content.Context;

import club.tushar.tourplancost.db.DbHelper;

public class Constant{

    public static DbHelper getDbHelper(Context context){
        return new DbHelper(context);
    }

    public static SPreferences sPreferences;

    public static final SPreferences getSharedPreferences(Context context){
        if(sPreferences == null){
            sPreferences = new SPreferences(context);
        }
        return sPreferences;
    }
}
