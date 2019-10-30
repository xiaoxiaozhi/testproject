package com.mylibrary.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by asus on 2016/9/30 0030.
 */

public class MyUtils {
    /**
     * 后台传的的不是JSon或者XML我这边转成json
     *
     * @param s
     * @return
     */
    public String getJsonstr(String s) {
        return s.replace(" ", "").replace("anyType", "").replace("{", "{\"").replace("=",
                "\":\"").replace
                (";", "\",\"").replace(",\"}", "}");
    }

    /**
     * 加引号
     *
     * @param s
     * @return
     */
    public String getJsonstr1(String s) {
        return s.replace(" ", "").replace("{", "{\"").replace(":", "\":\"").replace(",",
                "\",\"").replace("}", "\"}").replace("\"[", "[").replace("]\"", "]");
    }

    public String getJsonstr2(String s) {
        return s.replace(" ", "").replace("{", "{\"").replace(":", "\":\"").replace(",",
                "\",\"").replace("}", "\"}").replace("\"[", "[").replace("]\"", "]").replace("}\"", "}")
                .replace("\"{", "{");
    }

    /**
     * 去除字符串中的文字
     *
     * @return
     */
    public String getNum(String s) {
        return s.replaceAll("(\\s[\u4E00-\u9FA5]+)|([\u4E00-\u9FA5]+\\s)",
                "");
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 32位MD5
     *
     * @param plain
     * @return
     */
    public String encryption(String plain) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plain.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return re_md5;
        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 补充：计算两点之间真实距离
     *
     * @return 米
     */
    public double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;
        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }

    public String getMediaId(String uri) {
        if (uri.contains("%")) {
            return uri.split("%")[1];
        } else if (uri.substring(9).contains(":")) {
            return uri.split(":")[1];
        } else {
            return uri.substring(uri.lastIndexOf("/") + 1);
        }
    }

    public static void closeStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStream(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStream(Reader isr) {
        if (isr != null) {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStream(Writer isr) {
        if (isr != null) {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public HashMap<String, Integer> getScreenInfo(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.jquery）
        int dpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        HashMap<String, Integer> map = new HashMap<>();
        map.put("width", width);
        map.put("height", height);
        map.put("dpi", dpi);
        int rid = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (rid != 0) {
            int statusHeight = activity.getResources().getDimensionPixelSize(activity.getResources().getIdentifier("status_bar_height", "dimen", "android"));
            map.put("statusHeight", statusHeight);
        }
        int resourceId = 0;
        int rid1 = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid1 != 0) {
            resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            map.put("navigationHeight", activity.getResources().getDimensionPixelSize(resourceId));
        }
        return map;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public int getDaoHangHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            Log.i("导航栏 ", "高度：" + resourceId);
            Log.i("导航栏 ", "高度：" + context.getResources().getDimensionPixelSize(resourceId) + "");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    public byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     * @author ：shijing
     * 2016年12月5日下午4:34:46
     */
    public boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     * @author ：shijing
     * 2016年12月5日下午4:34:21
     */
    public boolean isPhone(final String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /* * 验证号码 手机号 固话均可
     * */
    public boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        String expression = "^((1[3,8][0-9])|(14[5,7])|(15[0,1,2,3,5,6,7,8,9])|(17[0,1,4,3,5,6,7,8]))\\d{8}$";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;

    }

    public int getVoiceTime(String path) {
        String curAudioFile = path;
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(curAudioFile);
            mediaPlayer.prepare();
            int nDuration0 = mediaPlayer.getDuration();// 单位毫秒
            Log.i("录音时长", nDuration0 + "");
            return nDuration0 / 1000;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 在特效中展开
     *
     * @param parentView 为了获取父控件的MeasureSpace
     * @param childView
     */
    public void heightExtended(View parentView, View childView) {
        childView.measure(parentView.getMeasuredWidth(), parentView.getMeasuredHeight());
        int viewHeigh = View.MeasureSpec.getSize(childView.getMeasuredHeight());
        if (childView.getLayoutParams() == null) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 0);
            childView.setLayoutParams(lp);
        } else {
            childView.getLayoutParams().height = 0;
        }
        childView.setVisibility(View.VISIBLE);
        ValueAnimator va = ValueAnimator.ofInt(0, viewHeigh);
        va.start();
        va.addUpdateListener(animation -> {
            animation.getAnimatedValue();
            int currentValue = (Integer) animation.getAnimatedValue();
//            System.out.println(currentValue);
            childView.getLayoutParams().height = currentValue;
            childView.requestLayout();
        });
    }

    /**
     * 在特效中收缩
     *
     * @param childview
     */
    public void heightshinked(View childview) {
        ValueAnimator va = ValueAnimator.ofInt(View.MeasureSpec.getSize(childview.getMeasuredHeight()), 0);
        va.start();
        va.addUpdateListener(animation -> {
            animation.getAnimatedValue();
            int currentValue = (Integer) animation.getAnimatedValue();
//            System.out.println(currentValue);
            childview.getLayoutParams().height = currentValue;
            childview.requestLayout();
            if (currentValue == 0) {
                childview.setVisibility(View.GONE);
            }
        });
    }
}
