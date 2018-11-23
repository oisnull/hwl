package com.hwl.beta.emotion.utils;

import java.io.File;

import android.text.TextUtils;

public final class FileUtils {
	private static final String FOLDER_NAME = "hwl_app";//这里可以换成你的app名称
	private static final long MIN_STORAGE=52428800;//50*1024*1024最低50m
	
	//缓存路径
	public static String getCachePath(){
		String path = getSavePath(MIN_STORAGE);
		if(TextUtils.isEmpty(path)){
			return null;
		}
		path= path + FOLDER_NAME + "/cache";
		makeDir(path);
		return path;
	}
	
	/**
	 * 获取保存文件路径  
	 * @param saveSize  预留空间
	 * @return 文件路径
	 */
	private static String getSavePath(long saveSize) {
	    String savePath = null;
	    if (StorageUtils.getExternaltStorageAvailableSpace() > saveSize) {//扩展存储设备>预留空间
	        savePath = StorageUtils.getExternalStorageDirectory();
	        File saveFile = new File(savePath);
	        if (!saveFile.exists()) {
	            saveFile.mkdirs();
	        } else if (!saveFile.isDirectory()) {
	            saveFile.delete();
	            saveFile.mkdirs();
	        }
	    } else if (StorageUtils.getSdcard2StorageAvailableSpace() > saveSize) {//sdcard2外部存储空间>预留空间
	        savePath = StorageUtils.getSdcard2StorageDirectory();
	        File saveFile = new File(savePath);
	        if (!saveFile.exists()) {
	            saveFile.mkdirs();
	        } else if (!saveFile.isDirectory()) {
	            saveFile.delete();
	            saveFile.mkdirs();
	        }
	    } else if (StorageUtils.getEmmcStorageAvailableSpace() > saveSize) {//可用的 EMMC 内部存储空间>预留空间
	        savePath = StorageUtils.getEmmcStorageDirectory();
	        File saveFile = new File(savePath);
	        if (!saveFile.exists()) {
	            saveFile.mkdirs();
	        } else if (!saveFile.isDirectory()) {
	            saveFile.delete();
	            saveFile.mkdirs();
	        }
	    } else if (StorageUtils.getOtherExternaltStorageAvailableSpace()>saveSize) {//其他外部存储可用空间>预留空间
	        savePath = StorageUtils.getOtherExternalStorageDirectory();
	        File saveFile = new File(savePath);
	        if (!saveFile.exists()) {
	            saveFile.mkdirs();
	        } else if (!saveFile.isDirectory()) {
	            saveFile.delete();
	            saveFile.mkdirs();
	        }
	    }else if (StorageUtils.getInternalStorageAvailableSpace() > saveSize) {//内部存储目录>预留空间
	        savePath = StorageUtils.getInternalStorageDirectory() + File.separator;
	    }
	    return savePath;
	}
	
	/**
	 * 创建文件夹
	 * @param path 文件路径
	 */
	private static void makeDir(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		file = null;
	}
}