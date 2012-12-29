package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import android.os.Environment;
import ch.almana.android.sessionsmodels.log.Logger;

public class DirectoryAccess {
	
	public static FileFilter directoryFilter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	public static File getModelsDir() {
		final File topDir = getTopDir();
		File modelsDir = new File(topDir, "models");
		if (!modelsDir.isDirectory()) {
			modelsDir.mkdir();
			try {
				getNoMediaFile(modelsDir).createNewFile();
			} catch (IOException e) {
				Logger.e("Failed to create nomedia file", e);
			}
		}
		return modelsDir;
	}

	public static File getSessionsDir() {
		final File topDir = getTopDir();
		File sessionsDir = new File(topDir, "sessions");
		if (!sessionsDir.isDirectory()) {
			sessionsDir.mkdir();
			//			try {
			//				getNoMediaFile(sessionsDir).createNewFile();
			//			} catch (IOException e) {
			//				Logger.e("Failed to create nomedia file", e);
			//			}
		}
		return sessionsDir;
	}

	public static File getTopDir() {
		final File externalStorageDirectory = Environment.getExternalStorageDirectory();
		final File topDir = new File(externalStorageDirectory, "sessionsmodels");
		if (!topDir.isDirectory()) {
			topDir.mkdir();
		}
		return topDir;
	}

	public static File getNoMediaFile(File dir) {
		return new File(dir, ".nomedia");
	}

}
