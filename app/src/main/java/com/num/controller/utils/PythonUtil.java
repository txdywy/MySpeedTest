package com.num.controller.utils;

import android.content.Context;

import com.num.Constants;
import com.num.R;

import java.io.InputStream;

public class PythonUtil {
    private static String pythonPath = Constants.DATA_DIRECTORY + "/extras/python" + ":" +
            Constants.DATA_DIRECTORY + "/python/lib/python2.7/lib-dynload" + ":" +
            Constants.DATA_DIRECTORY + "/python/lib/python2.7";
    private static String temp = Constants.DATA_DIRECTORY + "/extras/tmp";
    private static String pythonHome = Constants.DATA_DIRECTORY + "/python";
    private static String libraryPath = Constants.DATA_DIRECTORY + "/python/lib" + ":" +
            Constants.DATA_DIRECTORY + "/python/lib/python2.7/lib-dynload";

    public static boolean isInstalled() {
        return FileUtil.exists(pythonHome + "/bin/python");
    }

    public static void installExecutable(Context context) {
        InputStream pythonZip = context.getResources().openRawResource(R.raw.python_27);
        InputStream pythonExtrasZip = context.getResources().openRawResource(R.raw.python_extras_27);
        FileUtil.unzip(pythonZip, Constants.DATA_DIRECTORY + "/", true);
        FileUtil.chmod(pythonHome + "/bin/python", "0755");
        //FileUtil.chmod();
        //FileUtil.unzip(pythonExtrasZip, pythonHome, true);
    }
}
