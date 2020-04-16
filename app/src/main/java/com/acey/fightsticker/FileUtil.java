package com.acey.fightsticker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {

    public static Bitmap getImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String saveImage(Bitmap bitmap, Context context) {
        File dir = null;
        String dirPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/image/";
        String filePath = dirPath + System.currentTimeMillis() + ".jpg";
        dir = new File(dirPath);
        try {
            if (!dir.exists())
                dir.mkdirs();
            new File(filePath).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    filePath, fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 通知图库更新
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            MediaScannerConnection.scanFile(context, new String[]{filePath}, null,
//                    (path, uri) -> {
//                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        mediaScanIntent.setData(uri);
//                    });
//        }
        return filePath;
    }
}
