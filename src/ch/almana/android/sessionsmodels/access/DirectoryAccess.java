package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.BaseModel;
import ch.almana.android.sessionsmodels.model.PortfolioModel;

public class DirectoryAccess {

	public static FileFilter directoryFilter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	private static File getSubdir(String name, boolean createNoMedia) {
		File dir = new File(getTopDir(), name);
		if (!dir.isDirectory()) {
			dir.mkdir();
			if (createNoMedia) {
				try {
					getNoMediaFile(dir).createNewFile();
				} catch (IOException e) {
					Logger.e("Failed to create nomedia file", e);
				}
			}
		}
		return dir;
	}

	public static File getTopDir() {
		final File externalStorageDirectory = Environment.getExternalStorageDirectory();
		final File topDir = new File(externalStorageDirectory, "sessionsmodels");
		if (!topDir.isDirectory()) {
			topDir.mkdir();
		}
		return topDir;
	}

	public static File getModelsDir() {
		return getSubdir("models", true);
	}

	public static File getSessionsDir() {
		return getSubdir("sessions", false);
	}

	public static File getPortfolioDir() {
		return getSubdir("portfolio", false);
	}

	public static File getNoMediaFile(File dir) {
		return new File(dir, ".nomedia");
	}

	public static BaseModel getPortfolio(Context ctx) {
		return new PortfolioModel(ctx.getString(R.string.portfolio), getPortfolioDir());
	}

}
