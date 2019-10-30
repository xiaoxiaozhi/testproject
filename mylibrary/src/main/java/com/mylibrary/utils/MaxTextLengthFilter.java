package com.mylibrary.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

public class MaxTextLengthFilter implements InputFilter {
    private Context context;
    private int max;
    //    private int mMaxLength;
    private Toast toast;
    EditText et;

    public MaxTextLengthFilter(EditText et, int max) {
//        this.context = c;
        this.max = max;
        this.et = et;
//        toast = Toast.makeText(context, "字符不能超过" + max + "个", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 235);
    }

//    public MaxTextLengthFilter setMaxTextNum(int max) {
//
//        return this;
//    }


//    public MaxTextLengthFilter(int max) {
//        mMaxLength = max - 1;
//        toast = Toast.makeText(context, "字符不能超过" + max + "个", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 235);
//    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = max - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
//            toast.show();
            et.setError("字符不能超过" + max + "个");
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}
