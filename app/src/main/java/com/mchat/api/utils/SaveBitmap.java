package com.mchat.api.utils;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by CloudAnt on 2016/12/19.
 */

public class SaveBitmap {
    public static String PATH = Environment.getExternalStorageDirectory()+"/LK/image";
    /**
     * @param context
     * @param mBitmap bitmap图片
     * @param bitName 图片名称
     */
    public static void saveMyBitmap(Context context, Bitmap mBitmap,
                                    String bitName) {
//		String path = "/sdcard/CloudAnt/image";
        File destDir = new File(PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try {
            File f = new File(PATH, bitName + ".png");
            if (f.exists()) {
                f.delete();
            }
//			FileOutputStream out = new FileOutputStream(f);
//			mBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int options = 100;
            mBitmap.compress(Bitmap.CompressFormat.PNG, options, os);
            while (os.toByteArray().length / 1024 > 2048) {
                os.reset();
                options -= 10;
                mBitmap.compress(Bitmap.CompressFormat.PNG, options, os);
            }
            FileOutputStream out = new FileOutputStream(f);
            out.write(os.toByteArray());
            out.flush();
            out.close();
            Log.d("TAG", "已经保存" + f);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
