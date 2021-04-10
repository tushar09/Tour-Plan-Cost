package club.tushar.tourplancost.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPreferences{

    protected final static String AD_NETWORK = "adNetwork";
    protected final static String IS_CALLERD_ENABLED = "callerId";
    protected final static String TOKEN = "token";
    protected final static String NUMBER = "number";
    protected final static String IS_LOGGED_IN = "login";
    protected final static String SYSTEM_ALERT_OVERLAY = "systemAlertOverlay";
    protected final static String IS_AGREE = "isAgree";
    protected final static String IS_FIRST_TIME = "isFirstTime";
    protected final static String AD_TYPE = "ad_type";
    protected final static String FIRESTORE_DATA_PULL = "firestoreDataPull";

    protected SharedPreferences sp;

    private Context context;

    public SPreferences(Context context){
        this.context = context;
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void setAdNetwork(int network){
        sp.edit().putInt(AD_NETWORK, network).commit();
    }

    public int getAdNetwork(){
        return sp.getInt(AD_NETWORK, 1);
    }

    public void setIsCallerdEnabled(boolean enabled){
        sp.edit().putBoolean(IS_CALLERD_ENABLED, enabled).commit();
    }

    public boolean isCallerIdEnabled(){
        return sp.getBoolean(IS_CALLERD_ENABLED, true);
    }

    public void setToken(String token){
        sp.edit().putString(TOKEN, token).commit();
    }

    public String getToken(){
        return sp.getString(TOKEN, "n/a");
    }

    public void setNumber(String number){
        sp.edit().putString(NUMBER, number).commit();
    }

    public String getNumber(){
        return sp.getString(NUMBER, null);
    }


    public void setSystemAlertOverlayDntShow(boolean b){
        sp.edit().putBoolean(SYSTEM_ALERT_OVERLAY, b).commit();
    }

    public boolean getSystemAlertOverlayDntShow(){
        return sp.getBoolean(SYSTEM_ALERT_OVERLAY, false);
    }

    public void setIsAgree(boolean b){
        sp.edit().putBoolean(IS_AGREE, b).commit();
    }

    public boolean isAgree(){
        return sp.getBoolean(IS_AGREE, false);
    }


    public void setIsFirstTime(boolean b){
        sp.edit().putBoolean(IS_FIRST_TIME, b).commit();
    }

    public boolean isFirstTime(){
        return sp.getBoolean(IS_FIRST_TIME, true);
    }

    public void setIsLoggedIn(boolean b){
        sp.edit().putBoolean(IS_LOGGED_IN, b).commit();
    }

    public boolean isLoggedIn(){
        return sp.getBoolean(IS_LOGGED_IN, false);
    }

    public void setFirestoreDataPull(boolean b){
        sp.edit().putBoolean(FIRESTORE_DATA_PULL, b).commit();
    }

    public boolean isFirestoreDataPullDone(){
        return sp.getBoolean(FIRESTORE_DATA_PULL, false);
    }

}

