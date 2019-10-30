package com.mylibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by asus on 2016/11/9 0009.
 */

public class ImagUtil {
    Context context;

    public ImagUtil(Context c) {
        context = c;
    }

    public void imgExcute(ImageView imageView, ImgCallBack icb, String... params) {
        LoadBitAsynk loadBitAsynk = new LoadBitAsynk(imageView, icb);
        loadBitAsynk.execute(params);
    }

    public interface ImgCallBack {
        public void resultImgCall(ImageView imageView, Bitmap bitmap);
    }

    //显示原生图片尺寸大小
    public Bitmap getPathBitmap(Uri imageFilePath, int dw, int dh) throws FileNotFoundException {
        //获取屏幕的宽和高
        /**
         * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片
         * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true
         * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。
         * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值
         */
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        //由于使用了MediaStore存储，这里根据URI获取输入流的形式
        Bitmap pic = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageFilePath),
                null, op);

        int wRatio = (int) Math.ceil(op.outWidth / (float) dw); //计算宽度比例
        int hRatio = (int) Math.ceil(op.outHeight / (float) dh); //计算高度比例

        /**
         * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。
         * 如果高和宽不是全都超出了屏幕，那么无需缩放。
         * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》
         * 这需要判断wRatio和hRatio的大小
         * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。
         * 缩放使用的还是inSampleSize变量
         */
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
        pic = BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(imageFilePath), null, op);

        return pic;
    }

    public class LoadBitAsynk extends AsyncTask<String, Integer, Bitmap> {

        ImageView imageView;
        ImgCallBack icb;

        LoadBitAsynk(ImageView imageView, ImgCallBack icb) {
            this.imageView = imageView;
            this.icb = icb;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        bitmap = getPathBitmap(Uri.fromFile(new File(params[i])), 200, 200);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                icb.resultImgCall(imageView, result);
            }
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
