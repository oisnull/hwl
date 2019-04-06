package com.hwl.beta.ui.imgcompress;

import android.content.Context;
import android.graphics.Bitmap;

import com.hwl.beta.HWLApp;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/6.
 */

public class CompressChatImage {

    public final static int FIX_WIDTH = 960;
    public final static int FIX_HEIGHT = 1280;
    public final static int FIX_QUALITY = 75;

    public static File chatImage(String path) {
        return chatImage(HWLApp.getContext(), path);
    }

    public static File chatImage(Context context, String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            return new Compressor(context)
                    .setMaxWidth(FIX_WIDTH)
                    .setMaxHeight(FIX_HEIGHT)
                    .setQuality(FIX_QUALITY)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    //.setDestinationDirectoryPath(ResxType.getResxStoreDir(ResxType.CHATIMAGE))
                    .compressToFile(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
