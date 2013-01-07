package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONObject;

import android.os.Environment;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.BaseModel;

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

	protected static File getInfoFile(File m) {
		return new File(m, "info.json");
	}

	protected static JSONObject readJsonInfo(File m) throws Exception {
		File jsonFile = getInfoFile(m);
		if (!jsonFile.exists()) {
			throw new FileNotFoundException(jsonFile.getAbsolutePath());
		}
		FileInputStream stream;
		stream = new FileInputStream(jsonFile);
		String jString = null;
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			jString = Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}

		return new JSONObject(jString);
	}
	public static void save(BaseModel model) throws Exception {
		JSONObject json = model.getJson();
		FileWriter writer = new FileWriter(getInfoFile(model.getDir()));
		writer.write(json.toString(1));
		writer.flush();
		writer.close();
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

}
