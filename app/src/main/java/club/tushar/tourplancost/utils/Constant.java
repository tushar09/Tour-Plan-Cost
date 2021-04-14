package club.tushar.tourplancost.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String dateToLongWithYear(long date){
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy");
        return f.format(date);
    }

    public static String longToDate(long date){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return f.format(date);
    }

    public static String compareDate(long date){
        Calendar today = Calendar.getInstance(); // today
        //c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar myDate = Calendar.getInstance();
        myDate.setTimeInMillis(date); // your date

        if (today.get(Calendar.YEAR) == myDate.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == myDate.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        }

        today.add(Calendar.DAY_OF_YEAR, -1);
        if (today.get(Calendar.YEAR) == myDate.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == myDate.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        }

        today.add(Calendar.DAY_OF_YEAR, -2);
        if (today.get(Calendar.YEAR) == myDate.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == myDate.get(Calendar.DAY_OF_YEAR)) {
            return "2 Days ago";
        }

        return dateToLongWithYear(date);
    }
}
