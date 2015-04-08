package com.num.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.python27.BackgroundScriptService;
import com.android.python27.ScriptService;
import com.android.python27.config.GlobalConstants;
import com.android.python27.process.MyScriptProcess;
import com.android.python27.support.Utils;
import com.googlecode.android_scripting.AndroidProxy;
import com.googlecode.android_scripting.FileUtils;
import com.num.R;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class CensorshipActivity extends ActionBarActivity {
    ProgressDialog myProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censorship);

        boolean installNeeded = isInstallNeeded();

        if(installNeeded) {
            setContentView(R.layout.install);
            new InstallAsyncTask().execute();
        }
        else {
            runScriptService();
            finish();
        }
    }

    public class InstallAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i(GlobalConstants.LOG_TAG, "Installing...");

            createOurExternalStorageRootDir();

            // Copy all resources
            copyResourcesToLocal();

            // TODO
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Boolean installStatus) {
            runScriptService();
            finish();
        }

    }

    private void runScriptService() {
        if(GlobalConstants.IS_FOREGROUND_SERVICE) {
            startService(new Intent(this, ScriptService.class));
        }
        else {
            startService(new Intent(this, BackgroundScriptService.class));
        }
    }

    private void createOurExternalStorageRootDir() {
        Utils.createDirectoryOnExternalStorage(this.getPackageName());
    }

    // quick and dirty: only test a file
    private boolean isInstallNeeded() {
        File testedFile = new File(this.getFilesDir().getAbsolutePath()+ "/" + GlobalConstants.PYTHON_MAIN_SCRIPT_NAME);
        if(!testedFile.exists()) {
            return true;
        }
        return false;
    }


    private void copyResourcesToLocal() {
        String name, sFileName;
        InputStream content;

        R.raw a = new R.raw();
        java.lang.reflect.Field[] t = R.raw.class.getFields();
        Resources resources = getResources();

        boolean succeed = true;

        for (int i = 0; i < t.length; i++) {
            try {
                name = resources.getText(t[i].getInt(a)).toString();
                sFileName = name.substring(name.lastIndexOf('/') + 1, name.length());
                content = getResources().openRawResource(t[i].getInt(a));
                content.reset();

                // python project
                if(sFileName.endsWith(GlobalConstants.PYTHON_PROJECT_ZIP_NAME)) {
                    succeed &= Utils.unzip(content, this.getFilesDir().getAbsolutePath()+ "/", true);
                }
                // python -> /data/data/com.android.python27/files/python
                else if (sFileName.endsWith(GlobalConstants.PYTHON_ZIP_NAME)) {
                    succeed &= Utils.unzip(content, this.getFilesDir().getAbsolutePath()+ "/", true);
                    FileUtils.chmod(new File(this.getFilesDir().getAbsolutePath() + "/python/bin/python"), 0755);
                }
                // python extras -> /sdcard/com.android.python27/extras/python
                else if (sFileName.endsWith(GlobalConstants.PYTHON_EXTRAS_ZIP_NAME)) {
                    Utils.createDirectoryOnExternalStorage( this.getPackageName() + "/" + "extras");
                    Utils.createDirectoryOnExternalStorage( this.getPackageName() + "/" + "extras" + "/" + "tmp");
                    succeed &= Utils.unzip(content, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/extras/", true);
                }

            } catch (Exception e) {
                Log.e(GlobalConstants.LOG_TAG, "Failed to copyResourcesToLocal", e);
                succeed = false;
            }
        } // end for all files in res/raw

    }

    @Override
    protected void onStart() {
        super.onStart();

        String s = "System infos:";
        s += " OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += " | OS API Level: " + android.os.Build.VERSION.SDK;
        s += " | Device: " + android.os.Build.DEVICE;
        s += " | Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";

        Log.i(GlobalConstants.LOG_TAG, s);

        //finish();
    }
}
