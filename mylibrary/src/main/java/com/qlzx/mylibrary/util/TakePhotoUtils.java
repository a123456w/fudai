package com.qlzx.mylibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guo on 2017/7/27.
 * 拍照工具类
 */

public class TakePhotoUtils {

    /**
     * 拍照
     *
     * @param mActivity
     * @param dirName     图片文件夹名称
     * @param requestCode
     * @return
     * @throws IOException
     */
//    public static File takePhoto(Activity mActivity, String dirName, int requestCode) throws IOException {
//        //指定拍照intent
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri imageUri = null;
//        File outputImage = null;
//        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
//
//            if (Build.VERSION.SDK_INT >= 24){
//                outputImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+dirName+File.separator+"fudai"+System.currentTimeMillis()+ ".jpg");
//                outputImage.getParentFile().mkdirs();
//            }else {
//                outputImage= createImageFile(mActivity, dirName);
//            }
//
//            try {
//                if (outputImage.exists()) {
//                    outputImage.delete();
//                }
//                outputImage.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (outputImage != null) {
//
//                if (Build.VERSION.SDK_INT >= 24) {
//                    imageUri = FileProvider.getUriForFile(mActivity, "com.ruirong.chefang.fileprovider", outputImage);
//                } else {
//                    imageUri = Uri.fromFile(outputImage);
//                }
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                mActivity.startActivityForResult(takePictureIntent, requestCode);
//            }
//        }
//
//        return outputImage;
//    }


    public static File takePhoto(Activity mActivity, String dirName, int requestCode) throws IOException {
        //指定拍照intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = null;
        File outputImage = null;
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT >= 24) {
                outputImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName + File.separator + "fudai_" + System.currentTimeMillis() + ".jpg");
                outputImage.getParentFile().mkdirs();
            } else {
                outputImage = createImageFile(mActivity, dirName);
            }

            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (outputImage != null) {

                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(mActivity, "com.ruirong.chefang.fileprovider", outputImage);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag


                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                mActivity.startActivityForResult(takePictureIntent, requestCode);
            }
        }

        return outputImage;
    }


    public static File createImageFile(Activity mActivity, String dirName) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;//创建以时间命名的文件名称
        File storageDir = getOwnCacheDirectory(mActivity, dirName);//创建保存的路径
        File image = new File(storageDir.getPath(), imageFileName + ".jpg");
        if (!image.exists()) {
            try {
                //在指定的文件夹中创建文件
                image.createNewFile();
            } catch (Exception e) {
            }
        }

        return image;
    }


    /**
     * 根据目录创建文件夹
     *
     * @param context
     * @param cacheDir
     * @return
     */
    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        //判断sd卡正常挂载并且拥有权限的时候创建文件
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }


    /**
     * 检查是否有权限
     *
     * @param context
     * @return
     */
    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

}
