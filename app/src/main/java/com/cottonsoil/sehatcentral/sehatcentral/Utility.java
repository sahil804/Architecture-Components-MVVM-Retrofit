package com.cottonsoil.sehatcentral.sehatcentral;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sahil on 6/28/2018.
 */
public class Utility {
    public static final String TAG = Utility.class.getSimpleName();

    public static String parserTime(String time) {
        String formattedTime = time;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            Date d = sdf.parse(time);
            formattedTime = output.format(d);
            Log.d(TAG,"formattedTime "+formattedTime);
            Log.d(TAG,"not formattedTime "+time);
        } catch (Exception e)  {
            e.printStackTrace();
            return time;
        }
        return formattedTime;
    }


    public static Date getTodaysDate(){
        // Get Current Date
        Date c = Calendar.getInstance().getTime();
        return c;
    }

    public static String getTodayDateInString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        String formattedFromDate = simpleDateFormat.format(c);
        return formattedFromDate;
    }

    public static Date getDateFromString(String stringDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateAfterXdaysInString(int x) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTodaysDate());
        cal.add(Calendar.DATE, x);
        String formattedFromDate = simpleDateFormat.format(cal.getTime());
        return formattedFromDate;
    }

    public static String getNextDayDateInString(String  date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date c = getDateFromString(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(c);
        cal.add(Calendar.DATE, 1);
        String formattedFromDate = simpleDateFormat.format(cal.getTime());
        Log.d(TAG,"getNextDayDateInString: "+formattedFromDate);
        return formattedFromDate;
    }

    public static String getActualValue(String value) {
        String[] tokens = value.split(":");
        if(tokens != null && tokens.length >1) {
            return tokens[1];
        }
        return null;
    }

}