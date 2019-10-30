package com.mylibrary.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用前需要先初始化，传入日志所在文件夹地址
 */
public class MyLog {
    private final static int INFO = 4;
    private final static int DEBUG = 3;
    private final static int WARNING = 2;
    private final static int ERROR = 1;
    public static int logLevel = 5;//控制日志的开关,等于0全部关闭

    //    public MyLog(String foldPath) {
//        fold_path = foldPath;
//    }
    public static void init(String path) {
//        fold_path = Environment.getExternalStorageDirectory().toString() +
//                File.separator + "anjian" + File.separator + "log";
        fold_path = path;
        File logdir = new File(fold_path);
        if (!logdir.exists()) {
            logdir.mkdirs();
        }
    }

    static private String fold_path;

    private String getFunctionName() {

        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {

            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":"
                    + st.getLineNumber() + " ]";
        }
        return null;
    }

    /**
     * 路径
     *
     * @return
     * @throws
     * @return：String
     */
    static private String updatePath() {
        long curS = System.currentTimeMillis();
        Date date = new Date(curS);
        String curDate = ConverToStringDate(date);

        File logdir = new File(fold_path);
        if (!logdir.exists()) {
            logdir.mkdirs();
        }
        String filename = fold_path + "/" + curDate + ".txt";
        File logFile = new File(filename);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
                filename = "";
            }
        }

        return filename;

    }

    /**
     * sdcard/log.txt
     *
     * @param text
     * @return
     * @throws
     */
    static private void appendLog(String text) {

        String filename = updatePath();
        if (!"".equals(filename)) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(filename, true);
                BufferedWriter buf = new BufferedWriter(fw);
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    static public void i(String TAG, String message) {

        // Printing the message to LogCat console
        if (logLevel >= INFO && new File(fold_path).exists()) {
            final String s = message;
            Log.i(TAG, s);
            //add date and time
            Date date = new Date(System.currentTimeMillis());
            String strTime = ConverToString(date);

            // Write the log message to the file
            appendLog(strTime + "   I" + "    " + TAG + "    " + message + "\n");
        }
    }

    static public void d(String TAG, String message) {
        Log.d(TAG, message);
        if (logLevel >= DEBUG && new File(fold_path).exists()) {
            Date date = new Date(System.currentTimeMillis());
            String strTime = ConverToString(date);

            appendLog(strTime + "   D" + "    " + TAG + "    " + message + "\n");
        }

    }

    static public void w(String TAG, String message) {
        Log.w(TAG, message);
        if (logLevel >= WARNING && new File(fold_path).exists()) {
            Date date = new Date(System.currentTimeMillis());
            String strTime = ConverToString(date);

            appendLog(strTime + "   D" + "    " + TAG + "    " + message + "\n");
        }

    }

    static public void e(String TAG, String message) {
        if (message == null) {
            Log.e(TAG, "null");
        } else {
            Log.e(TAG, message);
        }
        if (logLevel >= ERROR && new File(fold_path).exists()) {
            Date date = new Date(System.currentTimeMillis());
            String strTime = ConverToString(date);

            appendLog(strTime + "   E" + "    " + TAG + "    " + message + "\n");
        }

    }

    static private String ConverToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = null;
        try {
            strDate = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    // 把日期转为字符串
    static public String ConverToStringDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = null;
        try {
            strDate = df.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

}
