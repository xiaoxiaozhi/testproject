package com.mylibrary.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VideoProvider {
    private Context context;

    public VideoProvider(Context context) {
        this.context = context;
    }


    /**
     * 获取视频缩略图
     *
     * @param id
     * @return
     */
    String getImg(String id) {

        String[] projection = {MediaStore.Video.Thumbnails.VIDEO_ID,
                MediaStore.Video.Thumbnails.DATA};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, projection
                , MediaStore.Video.Thumbnails.VIDEO_ID + "=?", new String[]{id}, null);

        if (!cursor.moveToFirst()) {
            return "";
        }
        return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
    }


    private void getColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(MediaStore.Video.Thumbnails.VIDEO_ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Video.Thumbnails.DATA);

            do {
                _id = cur.getInt(_idColumn);
                image_path = cur.getString(dataColumn);

                HashMap hash = new HashMap();
                hash.put("image_id", _id + "");
                hash.put("path", image_path);

            } while (cur.moveToNext());

        }
    }

    /**
     * 根据路径取得缓存图片
     *
     * @param path 视频文件的路径
     * @return 缓存图片的路径
     */
    String getCache(String path, String cache) {
        //如果缓存文件夹有缓存图片就不要再把bitmap转成file了
        File file = new File(path);
        String name = "_" + file.getName();
        File cacheFile = new File(cache);
        File[] files = cacheFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(name)) {
                return files[i].getPath();
            }
        }

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
        return new FileUtils().saveBitmapFile(bitmap, cache + File.separator + "_" + file
                .getName().substring(0, file.getName().lastIndexOf(".")) + ".jpg");
    }


}