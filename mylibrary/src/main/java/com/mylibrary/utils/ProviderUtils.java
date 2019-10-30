package com.mylibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mylibrary.model.Media;

/**
 * Created by zhixun on 2017/1/4 0004.
 */

public class ProviderUtils {
    private Context context;

    public ProviderUtils(Context context) {
        this.context = context;
    }

    /**
     * @param mediaUri MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media.EXTRA_MAX_BYTES
     * @return
     */
    public List<Media> getList(Uri mediaUri) {
        List<Media> list = null;
        if (context != null) {
            Cursor cursor;
            if (mediaUri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                cursor = context.getContentResolver().query(mediaUri, null, null,
                        null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
            } else {
                cursor = context.getContentResolver().query(mediaUri, null, null,
                        null, "datetaken DESC");
            }

            if (cursor != null) {
                list = new ArrayList<Media>();
                while (cursor.moveToNext()) {
                    int id = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow("_id"));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow("title"));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow("_data"));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow("_display_name"));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow("mime_type"));
                    if ("image/gif".equals(mimeType)) {
//                        Log.i("TAG", mimeType);
                        continue;
                    }
                    String date;
                    if (mediaUri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                        date = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED));
                    } else {
                        date = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                    }

                    if (date == null) {
                        continue;
                    }
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    Media audio = new Media(id, title, displayName, mimeType,
                            path, date, size);
                    list.add(audio);
                }
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 根据名称找 文件地址
     *
     * @param name
     * @return
     */
    public String getMediaByName(String name) {

        Uri uri = null;
        if (name != null && (name.endsWith(".png") || name.endsWith(".jpg"))) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        if (name != null && name.endsWith(".amr")) {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        if (name != null && name.endsWith(".mp4")) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        if (name != null && (name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx") ||
                name.endsWith(".xls") || name.endsWith(".xlsx"))) {
            uri = MediaStore.Files.getContentUri("external");
        }
        String[] projection = {"_display_name","_data"};
        Cursor cursor = context.getContentResolver().query(uri, projection
                , "_display_name =?", new String[]{name}, null);

        if (!cursor.moveToFirst()) {
            return "";
        }

        return cursor
                .getString(cursor
                        .getColumnIndexOrThrow("_data"));
    }

    /**
     * 查找特定的文件
     *
     * @param context
     * @param name
     * @return
     */
    public String getSpecialFile(Context context, String name) {
        List<Media> lists = new ArrayList<>();
        //从外存中获取
        Uri fileUri = MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和不含后缀的文件名
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE,

        };
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        //获取内容解析器对象
        ContentResolver resolver = context.getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(fileUri, projection, "title = ?", new String[]{name}, sortOrder);

        //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
        if (cursor.moveToLast()) {
            do {
                //输出文件的完整路径
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
//                Log.i("tag", title);
                Media media = new Media();
                media.setPath(data);
                media.setTitle(new File(data).getName());
                lists.add(media);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (lists.size() > 0 && lists.get(0) != null && lists.get(0).getPath() != null && !"".equals(lists.get(0).getPath())) {
            return lists.get(0).getPath();
        } else {
            return "";
        }

    }
}
