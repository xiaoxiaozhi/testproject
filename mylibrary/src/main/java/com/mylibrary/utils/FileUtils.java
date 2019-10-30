package com.mylibrary.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.mylibrary.model.Media;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;


/**
 * Created by asus on 2016/9/28 0028.
 */

public class FileUtils {

    /**
     * @param context
     * @param assetpath asset下的路径
     * @param SDpath    SDpath下保存路径
     */
    public void AssetToSD(String assetpath, String SDpath, Context context) {

        AssetManager asset = context.getAssets();
        //循环的读取asset下的文件，并且写入到SD卡
        String[] filenames = null;
        FileOutputStream out = null;
        InputStream in = null;
        try {
            filenames = asset.list(assetpath);
            if (filenames.length > 0) {//说明是目录
                //创建目录
                getDirectory(assetpath);

                for (String fileName : filenames) {
                    AssetToSD(assetpath + "/" + fileName, SDpath + "/" + fileName, context);
                }
            } else {//说明是文件，直接复制到SD卡
                File SDFlie = new File(SDpath);
                String path = assetpath.substring(0, assetpath.lastIndexOf("/"));
                getDirectory(path);

                if (!SDFlie.exists()) {
                    SDFlie.createNewFile();
                }
                //将内容写入到文件中
                in = asset.open(assetpath);
                out = new FileOutputStream(SDFlie);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteCount);
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                asset.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public long getFileSizes(File f) {
        long size = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("被检验文件大小", size + "");
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * @param context
     * @param assetpath asset单个文件名
     * @param SDpath    SDpath保存文件夹
     */
    public void AssetToSD1(String assetpath, String SDpath, Context context) {
        AssetManager asset = context.getAssets();
        //循环的读取asset下的文件，并且写入到SD卡
        FileOutputStream out = null;
        InputStream in = null;
        try {
            File SDFlie = new File(SDpath + File.separator + assetpath);
            if (!SDFlie.exists()) {
                SDFlie.createNewFile();
            }
            //将内容写入到文件中
            in = asset.open(assetpath);
            out = new FileOutputStream(SDFlie);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteCount);
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                asset.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //分级建立文件夹
    public void getDirectory(String path) {
        //对SDpath进行处理，分层级建立文件夹
        String[] s = path.split("/");
        String str = Environment.getExternalStorageDirectory().toString();
        for (int i = 0; i < s.length; i++) {
            str = str + "/" + s[i];
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
            }
        }

    }

    public void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            Log.i("", "复制单个文件操作出错");
            e.printStackTrace();

        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 把bitmap转成 file，并返回file路径
     *
     * @param bitmap
     * @param path
     * @return
     */
    public String saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getPath();

    }


    /**
     * 根据名称找 文件地址
     *
     * @param name
     * @return 没有就返回空
     */
    public String getMediaByName(Context context, String name) {

        Uri uri = null;
        if (name != null && (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        if (name != null && name.endsWith(".amr")) {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        if (name != null && (name.endsWith(".mp4")||name.endsWith(".3gp"))) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        if (name != null && (name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx") ||
                name.endsWith(".xls") || name.endsWith(".xlsx"))) {
            uri = MediaStore.Files.getContentUri("external");
        }
        String[] projection = {"_display_name", "_data"};
        Cursor cursor = context.getContentResolver().query(uri, projection
                , "_display_name =?", new String[]{name}, null);

        if (!cursor.moveToFirst()) {
            return null;
        }

        return cursor
                .getString(cursor
                        .getColumnIndexOrThrow("_data"));
    }


    /**
     * @param urlStr
     * @param path   完整路径带名字
     */
    public String downloadFIle(String urlStr, String path) {
        OutputStream output = null;
        InputStream input = null;
        try {
            URL url = new URL(urlStr);
            int len = 0;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            File file = new File(path);
            if (file.exists()) {
            } else {
                input = conn.getInputStream();
                output = new FileOutputStream(file);
                //读取大文件
                byte[] buffer = new byte[1024];
                while ((len = input.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                input.close();
                output.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            path = "";
        } catch (IOException e) {
            e.printStackTrace();
            path = "";
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;

    }

    /**
     * 文本文件转换为指定编码的字符串
     *
     * @param file     文本文件
     * @param encoding 编码类型
     * @return 转换后的字符串
     * @throws IOException
     */
    public String file2String(File file, String encoding) {
        InputStreamReader reader = null;
        FileInputStream fs = null;
        StringWriter writer = new StringWriter();
        try {
            fs = new FileInputStream(file);
            if (encoding == null || "".equals(encoding.trim())) {
                reader = new InputStreamReader(fs);
            } else {
                reader = new InputStreamReader(fs, encoding);
            }
            //将输入流写入输出流
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            fs.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            MyUtils.closeStream(reader);
            MyUtils.closeStream(fs);
        }
        //返回转换结果
        if (writer != null)
            return writer.toString();
        else return null;
    }

    /**
     * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
     *
     * @param res      原字符串
     * @param filePath 文件路径
     * @return 成功标记
     */
    public boolean string2File(String res, String filePath) {
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File distFile = new File(filePath);
            if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs();
            bufferedReader = new BufferedReader(new StringReader(res));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024];         //字符缓冲区
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            return flag;
        } finally {
            MyUtils.closeStream(bufferedReader);
            MyUtils.closeStream(bufferedWriter);
        }
        return flag;
    }
    /**
     * 根据名称找 文件地址
     *
     * @param id
     * @return
     */
    public String getVideoById(String id, Context context) {

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {"_id", "_display_name", "_data"};
        Cursor cursor = context.getContentResolver().query(uri, projection
                , "_id =?", new String[]{id}, null);

        if (!cursor.moveToFirst()) {
            return "";
        }

        return cursor
                .getString(cursor
                        .getColumnIndexOrThrow("_data"));
    }

    /**
     * 根据ID找文件地址
     *
     * @param id
     * @return
     */
    public String getImgById(String id, Context context) {

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {"_id", "_display_name", "_data"};
        Cursor cursor = context.getContentResolver().query(uri, projection
                , "_id =?", new String[]{id}, null);
        if (!cursor.moveToFirst()) {
            return "";
        }
        return cursor
                .getString(cursor
                        .getColumnIndexOrThrow("_data"));
    }

    /**
     * 根据视频路径取得缓存图片，如果没有就生成一个并存入缓存文件夹,最后返回
     *
     * @param path     视频文件的路径
     * @param cacheDir 缓存文件夹
     * @return 缓存图片的路径
     */
    public String getCache(String path, String cacheDir) {
        //如果缓存文件夹有缓存图片就不要再把bitmap转成file了
        File file = new File(path);
        String name = "_" + file.getName();
        File cacheFile = new File(cacheDir);
        File[] files = cacheFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(name)) {
                return files[i].getPath();
            }
        }

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        if (bitmap == null) {
            return null;
        } else {
            return saveBitmapFile(bitmap, cacheDir + File.separator + "_" + file
                    .getName().substring(0, file.getName().lastIndexOf(".")) + ".jpg");
        }

    }

    /**
     * 根据视频路径取得缓存图片，如果没有就生成一个并存入缓存文件夹,最后返回
     *
     * @param path     视频文件的路径
     * @param cacheDir 缓存文件夹
     * @return 缓存图片的路径
     */
    public Bitmap getCacheBitmap(String path, String cacheDir) {
        //如果缓存文件夹有缓存图片就不要再把bitmap转成file了
//        File file = new File(path);
//        String name = "_" + file.getName();
//        File cacheFile = new File(cacheDir);
//        File[] files = cacheFile.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            if (files[i].getName().equals(name)) {
//                return files[i].getPath();
//            }
//        }

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
        return bitmap;
    }

    Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
        Log.w("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
        return bm;
    }

    //显示打开方式
    public void show(Context context, String filesPath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        context.startActivity(showOpenTypeDialog(filesPath));
    }

    public Intent showOpenTypeDialog(String param) {
        Log.e("ViChildError", "showOpenTypeDialog");
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //打开文件时调用
    public void openFiles(Context context, String filesPath) {
        Uri uri = Uri.parse("file://" + filesPath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        String type = getMIMEType(filesPath);
        intent.setDataAndType(uri, type);
        if (!type.equals("*/*")) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                context.startActivity(showOpenTypeDialog(filesPath));
            }
        } else {
            context.startActivity(showOpenTypeDialog(filesPath));
        }
    }

    /**
     * --获取文件类型 --
     */
    public String getMIMEType(String filePath) {
        String type = "*/*";
        String fName = filePath;

        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }
        Map<Object, Object> map = toMap(MIME_MapTable);
        return (String) map.get(end);
    }

    public Map<Object, Object> toMap(Object[] array) {
        if (array == null) {
            return null;
        } else {
            HashMap map = new HashMap((int) ((double) array.length * 1.5D));

            for (int i = 0; i < array.length; ++i) {
                Object object = array[i];
                if (object instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) object;
                    map.put(entry.getKey(), entry.getValue());
                } else {
                    if (!(object instanceof Object[])) {
                        throw new IllegalArgumentException("Array element " + i + ", \'" + object + "\', is neither of type Map.Entry nor an Array");
                    }

                    Object[] var5 = (Object[]) ((Object[]) object);
                    if (var5.length < 2) {
                        throw new IllegalArgumentException("Array element " + i + ", \'" + object + "\', has a length less than 2");
                    }

                    map.put(var5[0], var5[1]);
                }
            }

            return map;
        }
    }

    /* * @param 将图片内容解析成字节数组
    * @param inStream
    * @return byte[]
    * @throws Exception
    */
    public byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    /**
     * @param //将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public Bitmap getPicFromBytes(byte[] bytes,
                                  BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * @param //图片缩放
     * @param bitmap 对象
     * @param w      要缩放的宽度
     * @param h      要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    /**
     * 把Bitmap转Byte
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把字节数组保存为一个文件
     */
    public File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * base64字符串转文件
     *
     * @param base64str
     * @param FilePath  文件的完全路径
     */
    public void saveBase64strToFile(String base64str, String FilePath) {
        if (base64str == null) {
            return;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(base64str);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(FilePath);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTilteName(String path) {
        try {
            return ESAPIUtil.validFilePath2(path).getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据文件名从文件夹返回文件路径，如果没有返回空
     *
     * @param fileName 文件名
     * @param dir      文件夹
     * @return
     */
    public String getPathFromDir(String fileName, File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (fileName.equals(file.getName())) {
                    return file.getPath();
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * -- MIME 列表 --
     */
    private String[][] MIME_MapTable =
            {
                    // --{后缀名， MIME类型}   --
                    {".3gp", "video/3gpp"},
                    {".3gpp", "video/3gpp"},
                    {".aac", "audio/x-mpeg"},
                    {".amr", "audio/x-mpeg"},
                    {".apk", "application/vnd.android.package-archive"},
                    {".avi", "video/x-msvideo"},
                    {".aab", "application/x-authoware-bin"},
                    {".aam", "application/x-authoware-map"},
                    {".aas", "application/x-authoware-seg"},
                    {".ai", "application/postscript"},
                    {".aif", "audio/x-aiff"},
                    {".aifc", "audio/x-aiff"},
                    {".aiff", "audio/x-aiff"},
                    {".als", "audio/x-alpha5"},
                    {".amc", "application/x-mpeg"},
                    {".ani", "application/octet-stream"},
                    {".asc", "text/plain"},
                    {".asd", "application/astound"},
                    {".asf", "video/x-ms-asf"},
                    {".asn", "application/astound"},
                    {".asp", "application/x-asap"},
                    {".asx", " video/x-ms-asf"},
                    {".au", "audio/basic"},
                    {".avb", "application/octet-stream"},
                    {".awb", "audio/amr-wb"},
                    {".bcpio", "application/x-bcpio"},
                    {".bld", "application/bld"},
                    {".bld2", "application/bld2"},
                    {".bpk", "application/octet-stream"},
                    {".bz2", "application/x-bzip2"},
                    {".bin", "application/octet-stream"},
                    {".bmp", "image/bmp"},
                    {".c", "text/plain"},
                    {".class", "application/octet-stream"},
                    {".conf", "text/plain"},
                    {".cpp", "text/plain"},
                    {".cal", "image/x-cals"},
                    {".ccn", "application/x-cnc"},
                    {".cco", "application/x-cocoa"},
                    {".cdf", "application/x-netcdf"},
                    {".cgi", "magnus-internal/cgi"},
                    {".chat", "application/x-chat"},
                    {".clp", "application/x-msclip"},
                    {".cmx", "application/x-cmx"},
                    {".co", "application/x-cult3d-object"},
                    {".cod", "image/cis-cod"},
                    {".cpio", "application/x-cpio"},
                    {".cpt", "application/mac-compactpro"},
                    {".crd", "application/x-mscardfile"},
                    {".csh", "application/x-csh"},
                    {".csm", "chemical/x-csml"},
                    {".csml", "chemical/x-csml"},
                    {".css", "text/css"},
                    {".cur", "application/octet-stream"},
                    {".doc", "application/msword"},
                    {".dcm", "x-lml/x-evm"},
                    {".dcr", "application/x-director"},
                    {".dcx", "image/x-dcx"},
                    {".dhtml", "text/html"},
                    {".dir", "application/x-director"},
                    {".dll", "application/octet-stream"},
                    {".dmg", "application/octet-stream"},
                    {".dms", "application/octet-stream"},
                    {".dot", "application/x-dot"},
                    {".dvi", "application/x-dvi"},
                    {".dwf", "drawing/x-dwf"},
                    {".dwg", "application/x-autocad"},
                    {".dxf", "application/x-autocad"},
                    {".dxr", "application/x-director"},
                    {".ebk", "application/x-expandedbook"},
                    {".emb", "chemical/x-embl-dl-nucleotide"},
                    {".embl", "chemical/x-embl-dl-nucleotide"},
                    {".eps", "application/postscript"},
                    {".epub", "application/epub+zip"},
                    {".eri", "image/x-eri"},
                    {".es", "audio/echospeech"},
                    {".esl", "audio/echospeech"},
                    {".etc", "application/x-earthtime"},
                    {".etx", "text/x-setext"},
                    {".evm", "x-lml/x-evm"},
                    {".evy", "application/x-envoy"},
                    {".exe", "application/octet-stream"},
                    {".fh4", "image/x-freehand"},
                    {".fh5", "image/x-freehand"},
                    {".fhc", "image/x-freehand"},
                    {".fif", "image/fif"},
                    {".fm", "application/x-maker"},
                    {".fpx", "image/x-fpx"},
                    {".fvi", "video/isivideo"},
                    {".flv", "video/x-msvideo"},
                    {".gau", "chemical/x-gaussian-input"},
                    {".gca", "application/x-gca-compressed"},
                    {".gdb", "x-lml/x-gdb"},
                    {".gif", "image/gif"},
                    {".gps", "application/x-gps"},
                    {".gtar", "application/x-gtar"},
                    {".gz", "application/x-gzip"},
                    {".gif", "image/gif"},
                    {".gtar", "application/x-gtar"},
                    {".gz", "application/x-gzip"},
                    {".h", "text/plain"},
                    {".hdf", "application/x-hdf"},
                    {".hdm", "text/x-hdml"},
                    {".hdml", "text/x-hdml"},
                    {".htm", "text/html"},
                    {".html", "text/html"},
                    {".hlp", "application/winhlp"},
                    {".hqx", "application/mac-binhex40"},
                    {".hts", "text/html"},
                    {".ice", "x-conference/x-cooltalk"},
                    {".ico", "application/octet-stream"},
                    {".ief", "image/ief"},
                    {".ifm", "image/gif"},
                    {".ifs", "image/ifs"},
                    {".imy", "audio/melody"},
                    {".ins", "application/x-net-install"},
                    {".ips", "application/x-ipscript"},
                    {".ipx", "application/x-ipix"},
                    {".it", "audio/x-mod"},
                    {".itz", "audio/x-mod"},
                    {".ivr", "i-world/i-vrml"},
                    {".j2k", "image/j2k"},
                    {".jad", "text/vnd.sun.j2me.app-descriptor"},
                    {".jam", "application/x-jam"},
                    {".jnlp", "application/x-java-jnlp-file"},
                    {".jpe", "image/jpeg"},
                    {".jpz", "image/jpeg"},
                    {".jwc", "application/jwc"},
                    {".jar", "application/java-archive"},
                    {".java", "text/plain"},
                    {".jpeg", "image/jpeg"},
                    {".jpg", "image/jpeg"},
                    {".js", "application/x-javascript"},
                    {".kjx", "application/x-kjx"},
                    {".lak", "x-lml/x-lak"},
                    {".latex", "application/x-latex"},
                    {".lcc", "application/fastman"},
                    {".lcl", "application/x-digitalloca"},
                    {".lcr", "application/x-digitalloca"},
                    {".lgh", "application/lgh"},
                    {".lha", "application/octet-stream"},
                    {".lml", "x-lml/x-lml"},
                    {".lmlpack", "x-lml/x-lmlpack"},
                    {".log", "text/plain"},
                    {".lsf", "video/x-ms-asf"},
                    {".lsx", "video/x-ms-asf"},
                    {".lzh", "application/x-lzh "},
                    {".m13", "application/x-msmediaview"},
                    {".m14", "application/x-msmediaview"},
                    {".m15", "audio/x-mod"},
                    {".m3u", "audio/x-mpegurl"},
                    {".m3url", "audio/x-mpegurl"},
                    {".ma1", "audio/ma1"},
                    {".ma2", "audio/ma2"},
                    {".ma3", "audio/ma3"},
                    {".ma5", "audio/ma5"},
                    {".man", "application/x-troff-man"},
                    {".map", "magnus-internal/imagemap"},
                    {".mbd", "application/mbedlet"},
                    {".mct", "application/x-mascot"},
                    {".mdb", "application/x-msaccess"},
                    {".mdz", "audio/x-mod"},
                    {".me", "application/x-troff-me"},
                    {".mel", "text/x-vmel"},
                    {".mi", "application/x-mif"},
                    {".mid", "audio/midi"},
                    {".midi", "audio/midi"},
                    {".m4a", "audio/mp4a-latm"},
                    {".m4b", "audio/mp4a-latm"},
                    {".m4p", "audio/mp4a-latm"},
                    {".m4u", "video/vnd.mpegurl"},
                    {".m4v", "video/x-m4v"},
                    {".mov", "video/quicktime"},
                    {".mp2", "audio/x-mpeg"},
                    {".mp3", "audio/x-mpeg"},
                    {".mp4", "video/mp4"},
                    {".mpc", "application/vnd.mpohun.certificate"},
                    {".mpe", "video/mpeg"},
                    {".mpeg", "video/mpeg"},
                    {".mpg", "video/mpeg"},
                    {".mpg4", "video/mp4"},
                    {".mpga", "audio/mpeg"},
                    {".msg", "application/vnd.ms-outlook"},
                    {".mif", "application/x-mif"},
                    {".mil", "image/x-cals"},
                    {".mio", "audio/x-mio"},
                    {".mmf", "application/x-skt-lbs"},
                    {".mng", "video/x-mng"},
                    {".mny", "application/x-msmoney"},
                    {".moc", "application/x-mocha"},
                    {".mocha", "application/x-mocha"},
                    {".mod", "audio/x-mod"},
                    {".mof", "application/x-yumekara"},
                    {".mol", "chemical/x-mdl-molfile"},
                    {".mop", "chemical/x-mopac-input"},
                    {".movie", "video/x-sgi-movie"},
                    {".mpn", "application/vnd.mophun.application"},
                    {".mpp", "application/vnd.ms-project"},
                    {".mps", "application/x-mapserver"},
                    {".mrl", "text/x-mrml"},
                    {".mrm", "application/x-mrm"},
                    {".ms", "application/x-troff-ms"},
                    {".mts", "application/metastream"},
                    {".mtx", "application/metastream"},
                    {".mtz", "application/metastream"},
                    {".mzv", "application/metastream"},
                    {".nar", "application/zip"},
                    {".nbmp", "image/nbmp"},
                    {".nc", "application/x-netcdf"},
                    {".ndb", "x-lml/x-ndb"},
                    {".ndwn", "application/ndwn"},
                    {".nif", "application/x-nif"},
                    {".nmz", "application/x-scream"},
                    {".nokia-op-logo", "image/vnd.nok-oplogo-color"},
                    {".npx", "application/x-netfpx"},
                    {".nsnd", "audio/nsnd"},
                    {".nva", "application/x-neva1"},
                    {".oda", "application/oda"},
                    {".oom", "application/x-atlasMate-plugin"},
                    {".ogg", "audio/ogg"},
                    {".pac", "audio/x-pac"},
                    {".pae", "audio/x-epac"},
                    {".pan", "application/x-pan"},
                    {".pbm", "image/x-portable-bitmap"},
                    {".pcx", "image/x-pcx"},
                    {".pda", "image/x-pda"},
                    {".pdb", "chemical/x-pdb"},
                    {".pdf", "application/pdf"},
                    {".pfr", "application/font-tdpfr"},
                    {".pgm", "image/x-portable-graymap"},
                    {".pict", "image/x-pict"},
                    {".pm", "application/x-perl"},
                    {".pmd", "application/x-pmd"},
                    {".png", "image/png"},
                    {".pnm", "image/x-portable-anymap"},
                    {".pnz", "image/png"},
                    {".pot", "application/vnd.ms-powerpoint"},
                    {".ppm", "image/x-portable-pixmap"},
                    {".pps", "application/vnd.ms-powerpoint"},
                    {".ppt", "application/vnd.ms-powerpoint"},
                    {".pqf", "application/x-cprplayer"},
                    {".pqi", "application/cprplayer"},
                    {".prc", "application/x-prc"},
                    {".proxy", "application/x-ns-proxy-autoconfig"},
                    {".prop", "text/plain"},
                    {".ps", "application/postscript"},
                    {".ptlk", "application/listenup"},
                    {".pub", "application/x-mspublisher"},
                    {".pvx", "video/x-pv-pvx"},
                    {".qcp", "audio/vnd.qcelp"},
                    {".qt", "video/quicktime"},
                    {".qti", "image/x-quicktime"},
                    {".qtif", "image/x-quicktime"},
                    {".r3t", "text/vnd.rn-realtext3d"},
                    {".ra", "audio/x-pn-realaudio"},
                    {".ram", "audio/x-pn-realaudio"},
                    {".ras", "image/x-cmu-raster"},
                    {".rdf", "application/rdf+xml"},
                    {".rf", "image/vnd.rn-realflash"},
                    {".rgb", "image/x-rgb"},
                    {".rlf", "application/x-richlink"},
                    {".rm", "audio/x-pn-realaudio"},
                    {".rmf", "audio/x-rmf"},
                    {".rmm", "audio/x-pn-realaudio"},
                    {".rnx", "application/vnd.rn-realplayer"},
                    {".roff", "application/x-troff"},
                    {".rp", "image/vnd.rn-realpix"},
                    {".rpm", "audio/x-pn-realaudio-plugin"},
                    {".rt", "text/vnd.rn-realtext"},
                    {".rte", "x-lml/x-gps"},
                    {".rtf", "application/rtf"},
                    {".rtg", "application/metastream"},
                    {".rtx", "text/richtext"},
                    {".rv", "video/vnd.rn-realvideo"},
                    {".rwc", "application/x-rogerwilco"},
                    {".rar", "application/x-rar-compressed"},
                    {".rc", "text/plain"},
                    {".rmvb", "audio/x-pn-realaudio"},
                    {".s3m", "audio/x-mod"},
                    {".s3z", "audio/x-mod"},
                    {".sca", "application/x-supercard"},
                    {".scd", "application/x-msschedule"},
                    {".sdf", "application/e-score"},
                    {".sea", "application/x-stuffit"},
                    {".sgm", "text/x-sgml"},
                    {".sgml", "text/x-sgml"},
                    {".shar", "application/x-shar"},
                    {".shtml", "magnus-internal/parsed-html"},
                    {".shw", "application/presentations"},
                    {".si6", "image/si6"},
                    {".si7", "image/vnd.stiwap.sis"},
                    {".si9", "image/vnd.lgtwap.sis"},
                    {".sis", "application/vnd.symbian.install"},
                    {".sit", "application/x-stuffit"},
                    {".skd", "application/x-koan"},
                    {".skm", "application/x-koan"},
                    {".skp", "application/x-koan"},
                    {".skt", "application/x-koan"},
                    {".slc", "application/x-salsa"},
                    {".smd", "audio/x-smd"},
                    {".smi", "application/smil"},
                    {".smil", "application/smil"},
                    {".smp", "application/studiom"},
                    {".smz", "audio/x-smd"},
                    {".sh", "application/x-sh"},
                    {".snd", "audio/basic"},
                    {".spc", "text/x-speech"},
                    {".spl", "application/futuresplash"},
                    {".spr", "application/x-sprite"},
                    {".sprite", "application/x-sprite"},
                    {".sdp", "application/sdp"},
                    {".spt", "application/x-spt"},
                    {".src", "application/x-wais-source"},
                    {".stk", "application/hyperstudio"},
                    {".stm", "audio/x-mod"},
                    {".sv4cpio", "application/x-sv4cpio"},
                    {".sv4crc", "application/x-sv4crc"},
                    {".svf", "image/vnd"},
                    {".svg", "image/svg-xml"},
                    {".svh", "image/svh"},
                    {".svr", "x-world/x-svr"},
                    {".swf", "application/x-shockwave-flash"},
                    {".swfl", "application/x-shockwave-flash"},
                    {".t", "application/x-troff"},
                    {".tad", "application/octet-stream"},
                    {".talk", "text/x-speech"},
                    {".tar", "application/x-tar"},
                    {".taz", "application/x-tar"},
                    {".tbp", "application/x-timbuktu"},
                    {".tbt", "application/x-timbuktu"},
                    {".tcl", "application/x-tcl"},
                    {".tex", "application/x-tex"},
                    {".texi", "application/x-texinfo"},
                    {".texinfo", "application/x-texinfo"},
                    {".tgz", "application/x-tar"},
                    {".thm", "application/vnd.eri.thm"},
                    {".tif", "image/tiff"},
                    {".tiff", "image/tiff"},
                    {".tki", "application/x-tkined"},
                    {".tkined", "application/x-tkined"},
                    {".toc", "application/toc"},
                    {".toy", "image/toy"},
                    {".tr", "application/x-troff"},
                    {".trk", "x-lml/x-gps"},
                    {".trm", "application/x-msterminal"},
                    {".tsi", "audio/tsplayer"},
                    {".tsp", "application/dsptype"},
                    {".tsv", "text/tab-separated-values"},
                    {".ttf", "application/octet-stream"},
                    {".ttz", "application/t-time"},
                    {".txt", "text/plain"},
                    {".ult", "audio/x-mod"},
                    {".ustar", "application/x-ustar"},
                    {".uu", "application/x-uuencode"},
                    {".uue", "application/x-uuencode"},
                    {".vcd", "application/x-cdlink"},
                    {".vcf", "text/x-vcard"},
                    {".vdo", "video/vdo"},
                    {".vib", "audio/vib"},
                    {".viv", "video/vivo"},
                    {".vivo", "video/vivo"},
                    {".vmd", "application/vocaltec-media-desc"},
                    {".vmf", "application/vocaltec-media-file"},
                    {".vmi", "application/x-dreamcast-vms-info"},
                    {".vms", "application/x-dreamcast-vms"},
                    {".vox", "audio/voxware"},
                    {".vqe", "audio/x-twinvq-plugin"},
                    {".vqf", "audio/x-twinvq"},
                    {".vql", "audio/x-twinvq"},
                    {".vre", "x-world/x-vream"},
                    {".vrml", "x-world/x-vrml"},
                    {".vrt", "x-world/x-vrt"},
                    {".vrw", "x-world/x-vream"},
                    {".vts", "workbook/formulaone"},
                    {".wax", "audio/x-ms-wax"},
                    {".wbmp", "image/vnd.wap.wbmp"},
                    {".web", "application/vnd.xara"},
                    {".wav", "audio/x-wav"},
                    {".wma", "audio/x-ms-wma"},
                    {".wmv", "audio/x-ms-wmv"},
                    {".wi", "image/wavelet"},
                    {".wis", "application/x-InstallShield"},
                    {".wm", "video/x-ms-wm"},
                    {".wmd", "application/x-ms-wmd"},
                    {".wmf", "application/x-msmetafile"},
                    {".wml", "text/vnd.wap.wml"},
                    {".wmlc", "application/vnd.wap.wmlc"},
                    {".wmls", "text/vnd.wap.wmlscript"},
                    {".wmlsc", "application/vnd.wap.wmlscriptc"},
                    {".wmlscript", "text/vnd.wap.wmlscript"},
                    {".wmv", "video/x-ms-wmv"},
                    {".wmx", "video/x-ms-wmx"},
                    {".wmz", "application/x-ms-wmz"},
                    {".wpng", "image/x-up-wpng"},
                    {".wps", "application/vnd.ms-works"},
                    {".wpt", "x-lml/x-gps"},
                    {".wri", "application/x-mswrite"},
                    {".wrl", "x-world/x-vrml"},
                    {".wrz", "x-world/x-vrml"},
                    {".ws", "text/vnd.wap.wmlscript"},
                    {".wsc", "application/vnd.wap.wmlscriptc"},
                    {".wv", "video/wavelet"},
                    {".wvx", "video/x-ms-wvx"},
                    {".wxl", "application/x-wxl"},
                    {".x-gzip", "application/x-gzip"},
                    {".xar", "application/vnd.xara"},
                    {".xbm", "image/x-xbitmap"},
                    {".xdm", "application/x-xdma"},
                    {".xdma", "application/x-xdma"},
                    {".xdw", "application/vnd.fujixerox.docuworks"},
                    {".xht", "application/xhtml+xml"},
                    {".xhtm", "application/xhtml+xml"},
                    {".xhtml", "application/xhtml+xml"},
                    {".xla", "application/vnd.ms-excel"},
                    {".xlc", "application/vnd.ms-excel"},
                    {".xll", "application/x-excel"},
                    {".xlm", "application/vnd.ms-excel"},
                    {".xls", "application/vnd.ms-excel"},
                    {".xlsx", "application/vnd.ms-excel"},
                    {".xlt", "application/vnd.ms-excel"},
                    {".xlw", "application/vnd.ms-excel"},
                    {".xm", "audio/x-mod"},
                    {".xml", "text/xml"},
                    {".xmz", "audio/x-mod"},
                    {".xpi", "application/x-xpinstall"},
                    {".xpm", "image/x-xpixmap"},
                    {".xsit", "text/xml"},
                    {".xsl", "text/xml"},
                    {".xul", "text/xul"},
                    {".xwd", "image/x-xwindowdump"},
                    {".xyz", "chemical/x-pdb"},
                    {".yz1", "application/x-yz1"},
                    {".z", "application/x-compress"},
                    {".zac", "application/x-zaurus-zac"},
                    {".zip", "application/zip"},
                    {"", "*/*"}
            };
}
