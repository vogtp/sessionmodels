package ch.almana.android.sessionsmodels.access;

import java.io.File;

import android.os.Environment;

public class DirectoryAccess {

	public static File getModelsDir() {
		final File topDir = getTopDir();
		File modelsDir = new File(topDir, "models");
		return modelsDir;
	}

	public static File getSessionsDir() {
		final File topDir = getTopDir();
		File modelsDir = new File(topDir, "sessions");
		return modelsDir;
	}

	public static File getTopDir() {
		final File externalStorageDirectory = Environment.getExternalStorageDirectory();
		final File topDir = new File(externalStorageDirectory, "sessionsmodels");
		return topDir;
	}

}
