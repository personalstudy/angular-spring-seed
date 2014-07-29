package org.personal.mason.ass.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mason on 7/7/14.
 */
public class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT);

    public static Date toDate(String dateStr){
        if(dateStr == null || dateStr.isEmpty()){
            return null;
        }

        try {
            Date date = DATE_FORMAT.parse(dateStr);
            return date;
        } catch (ParseException e) {

        }
        return null;
    }

    public static String toString(Date date){
        if(date == null){
            return null;
        }

        return DATE_FORMAT.format(date);
    }

}
