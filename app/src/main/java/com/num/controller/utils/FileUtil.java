package com.num.controller.utils;

import android.content.Context;
import android.util.Log;

import com.num.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

    public static void copyFromAsset(Context context, String src, String dst) {
        try {
            InputStream inputstream = context.getAssets().open(src);
            int i = inputstream.available();
            byte buf[] = new byte[i];
            inputstream.read(buf);
            FileOutputStream fileoutputstream = new FileOutputStream(new File(dst));
            fileoutputstream.write(buf, 0, i);
            inputstream.close();
            fileoutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chmod(String file, String mode) {
        try {
            CommandLineUtil.runCommand("chmod " + mode + " " + file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(String file) {
        try {
            Process process = Runtime.getRuntime().exec(file);
            process.waitFor();
            process.destroy();
        }
        catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static void unzip(InputStream inputStream, String dest, boolean replaceIfExists) {
        final int BUFFER_SIZE = 4096;
        BufferedOutputStream bufferedOutputStream = null;
        boolean succeed = false;
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null){
                String zipEntryName = zipEntry.getName();
                // file exists ? delete ?
                File file2 = new File(dest + zipEntryName);
                if(file2.exists()) {
                    if (replaceIfExists) {
                        try {
                            boolean b = deleteDir(file2);
                            if(!b) {
                                Log.e(Constants.LOG_TAG, "Unzip failed to delete " + dest + zipEntryName);
                            }
                            else {
                                Log.d(Constants.LOG_TAG, "Unzip deleted " + dest + zipEntryName);
                            }
                        } catch (Exception e) {
                            Log.e(Constants.LOG_TAG, "Unzip failed to delete " + dest + zipEntryName, e);
                        }
                    }
                }

                // extract
                File file = new File(dest + zipEntryName);

                if (file.exists()){

                } else {
                    if(zipEntry.isDirectory()){
                        file.mkdirs();
                        FileUtil.chmod(file.getAbsolutePath(), "0755");

                    }else{

                        // create parent file folder if not exists yet
                        if(!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                            chmod(file.getParentFile().toString(), "0755");
                        }

                        byte buffer[] = new byte[BUFFER_SIZE];
                        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
                        int count;

                        while ((count = zipInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            bufferedOutputStream.write(buffer, 0, count);
                        }

                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    }
                }

                // enable standalone python
                if(file.getName().endsWith(".so") || file.getName().endsWith(".xml") || file.getName().endsWith(".py") || file.getName().endsWith(".pyc") || file.getName().endsWith(".pyo")) {
                    FileUtil.chmod(file.toString(), "0755");
                }

                Log.d(Constants.LOG_TAG,"Unzip extracted " + dest + zipEntryName);
            }

            zipInputStream.close();

        } catch (FileNotFoundException e) {
            Log.e(Constants.LOG_TAG,"Unzip error, file not found", e);
            succeed = false;
        }catch (Exception e) {
            Log.e(Constants.LOG_TAG,"Unzip error: ", e);
            succeed = false;
        }
    }

    public static boolean deleteDir(File dir) {
        try {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i=0; i<children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

            // The directory is now empty so delete it
            return dir.delete();

        } catch (Exception e) {
            Log.e(Constants.LOG_TAG,"Failed to delete " + dir + " : " + e);
            return false;
        }
    }
}
