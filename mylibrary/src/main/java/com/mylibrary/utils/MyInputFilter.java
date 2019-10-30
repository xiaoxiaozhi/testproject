package com.mylibrary.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhixun on 2018/5/22 0022.
 */

public class MyInputFilter implements InputFilter {
    Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()<>{}\\[\\]=%&$|\\/♀♂#￥￡￠€\"^` ，。？！：；……～" +
            "“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．]");
    EditText et;
    Pattern pattern2 = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？§№☆★○●◎◇◇◆□△▲■※＃→←↑-]");

    public MyInputFilter(EditText et) {
        this.et = et;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        Matcher matcher = pattern.matcher(charSequence);
        Matcher matcher1 = pattern2.matcher(charSequence);
        if (!matcher.find()) {
            if (matcher1.find()) {
                et.setError("请不要输入特殊字符");
                return "";
            }
            return null;
        } else {
            et.setError("请不要输入特殊字符");
            return "";
        }


    }
}
