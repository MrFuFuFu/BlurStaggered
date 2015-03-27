package com.mrfu.blurview.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.mrfu.blurview.BlurViewApplication;

/**
 * @author Mr.傅 2015-3-26 下午3:21:35
 */
public class FileStore {
	public static Context context = BlurViewApplication.getInstance();
	
	
	public synchronized static String cachePathForKeyBlur(String httpUrl) {
		if (!TextUtils.isEmpty(httpUrl)) {
			if (httpUrl.startsWith("blur_http")) {
				String md5 = MD5Util.md5(httpUrl);
				boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
				String pathName = "";
				if (sdCardExist) {
					String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mrfu/important/";
					pathName = path + md5;
					File file = new File(pathName);
					if (file.exists()) {
						return pathName;
					}

					path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mrfu/cache/";
					pathName = path + md5;
					file = new File(pathName);
					if (file.exists()) {
						return pathName;
					}
				}

				String path = context.getCacheDir().getAbsolutePath() + "/mrfu/important/";
				pathName = path + md5;
				File file = new File(pathName);
				if (file.exists()) {
					return pathName;
				}

				path = context.getCacheDir().getAbsolutePath() + "/mrfu/cache/";
				pathName = path + md5;
				file = new File(pathName);
				if (file.exists()) {
					return pathName;
				}
			}
		}
		return "";
	}
	
	public static String createNewCacheFile(String fileName, boolean addTmp) {
		synchronized (context) {
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
			String path = "";
			if (sdCardExist) {
				path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/mrfu/important/";
			} else {
				path = context.getCacheDir().getAbsolutePath()
						+ "/mrfu/important/";
			}
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
				File noScanFile = new File(path + ".nomedia");
				try {
					noScanFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					clearFile();
				}
			}
			String name = MD5Util.md5(fileName);
			String pathName = path + name;
			if(addTmp) {
				pathName += ".tmp";
			}
			File file = new File(pathName);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return pathName;
		}
	}
	
	private static void clearFile() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File cacheDir = BlurViewApplication.getInstance().getCacheDir();
				deleteFiles(cacheDir);
				boolean sdCardExist = Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED);
				if (sdCardExist) {
					try {
						deleteFiles(new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/mrfu/important/"));
					} catch (Exception e) {
					}
				}
			}
		}).start();
	}
	
	// delete files
	public static void deleteFiles(File file) {
		try {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFiles(files[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writemBitmapForBlur(String pathName, Bitmap bitmap) {
		FileOutputStream fos = null;
		boolean bOK = false;
		int compressRate = 70;
		try {
			fos = new FileOutputStream(pathName);
			bitmap.compress(Bitmap.CompressFormat.PNG, compressRate, fos);
			bOK = true;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (bOK) {
				String newPath = pathName.replace(".tmp", "");
				File newfile = new File(newPath);
				File oldfile = new File(pathName);
				oldfile.renameTo(newfile);
			}
		}
	}
	
	public synchronized static String cachePathForKey(String httpUrl) {
		if (!TextUtils.isEmpty(httpUrl)) {
			if (httpUrl.startsWith("http")) {
				String md5 = MD5Util.md5(httpUrl);
				boolean sdCardExist = Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED);
				String pathName = "";
				if (sdCardExist) {
					String path = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/mrfu/important/";
					pathName = path + md5;
					File file = new File(pathName);
					if (file.exists()) {
						return pathName;
					}
					
					path = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/mrfu/cache/";
					pathName = path + md5;
					file = new File(pathName);
					if (file.exists()) {
						return pathName;
					}
				}

				String path = context.getCacheDir().getAbsolutePath()
						+ "/mrfu/important/";
				pathName = path + md5;
				File file = new File(pathName);
				if (file.exists()) {
					return pathName;
				}
				
				path = context.getCacheDir().getAbsolutePath()
						+ "/mrfu/cache/";
				pathName = path + md5;
				file = new File(pathName);
				if (file.exists()) {
					return pathName;
				}
			}
		}
		return "";
	}
}
