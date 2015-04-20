package edu.berkeley.icsi.netalyzr.tests;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.num.Constants;


/**
 * Collection of methods required by Netalyzr tests
 * 
 **/
public class Utils {
    public static final String TAG = "NETALYZR";
    

    private static int printParseIntCount = 0;

    
    /**
     * Gets string representation of UTC current time. This is
     * complicated because of the following reasons: (1) we cannot
     * rely on Java inferring the machine's local timezone correctly
     * (it has a history of issues there) and (2) looking up specific
     * timezones may actually trigger security exceptions.
     * 
     * @return
     */
    public static String utcTime() {
        TimeZone tz = TimeZone.getDefault();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date ret = new Date((new Date()).getTime() - tz.getRawOffset());

        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());
            if (tz.inDaylightTime(dstDate))
                ret = dstDate;
        }

        return df.format(ret) + " UTC";
    }
    
    public static int parseInt(String input) {
        if (input == null) {
            if (printParseIntCount < 20) {
                Log.d(Constants.LOG_TAG, "Reporting null as integer -1");
            }
            if (printParseIntCount == 20) {
                Log.d(Constants.LOG_TAG, "Supressing error");
            }
            printParseIntCount += 1;
            return -1;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (printParseIntCount < 20) {
                Log.d(Constants.LOG_TAG, "Reporting \"" + input + "\" as integer -1");
            }
            if (printParseIntCount == 20) {
                Log.d(Constants.LOG_TAG, "Supressing error");
            }
            printParseIntCount += 1;
        }
        return -1;
    }

}
