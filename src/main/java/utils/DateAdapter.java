package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public static boolean isAdapting(String s, String dataPattern){
        SimpleDateFormat parser = new SimpleDateFormat(dataPattern);
        try {
            parser.parse(s);
        }
        catch(ParseException ex){
            return false;
        }
        return true;
    }
    public static Date adapt(String s, String dataPattern){
        SimpleDateFormat parser = new SimpleDateFormat(dataPattern);
        try {
            return parser.parse(s);
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
        return new Date();
    }
    public static String dateToString(Date date){
        return dateFormat.format(date);
    }
    public static Date dateFromString(String dataStr){
        try {
            return dateFormat.parse(dataStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
