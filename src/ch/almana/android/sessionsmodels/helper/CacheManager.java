package ch.almana.android.sessionsmodels.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import ch.almana.android.sessionsmodels.log.Logger;


public class CacheManager {

	private final Context context;


	public CacheManager(Context context) {
		super();
		this.context = context;
	}


	public void cacheBitmap(File f, Bitmap bitmap, int resolution) {
		Logger.w("Caching: " + f);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getCacheFile(f, resolution));
			bitmap.compress(CompressFormat.PNG, 90, out);
			out.flush();
		} catch (IOException e) {
			Logger.w("Can not cache file", e);
			getCacheFile(f, resolution).delete();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	public static void deleteCache(Context context) {
		Logger.w("Deleting cache");
		File cacheDir = context.getCacheDir();
		for (File file : cacheDir.listFiles()) {
			file.delete();
		}
	}

	public File getCacheFile(File f, int resolution) {
		File cacheDir = context.getCacheDir();
		return new File(cacheDir, resolution + "_" + Integer.toString(f.getAbsoluteFile().hashCode()));
	}

	public boolean cacheExists(File f, int resolution) {
		File cacheFile = getCacheFile(f, resolution);
		if (cacheFile.exists()) {
			if (cacheFile.lastModified() < f.lastModified()) {
				cacheFile.delete();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public File getFile(File f, int resolution) {
		//		if (!cacheExists(urlStr, resolution)) {
		//			Logger.v("Load url " + urlStr);
		//			cacheStream(urlStr);
		//		}
		return getCacheFile(f, resolution);
	}

}
