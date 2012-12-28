package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.ModelModel;

public class ModelAcess extends DirectoryAccess {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<ModelModel> ITEMS = new ArrayList<ModelModel>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, ModelModel> ITEM_MAP = new HashMap<String, ModelModel>();

	static {
		File modelsDir = getModelsDir();
		File[] models = modelsDir.listFiles(DirectoryAccess.directoryFilter);
		for (int i = 0; i < models.length; i++) {
			File m = models[i];
			ModelModel model = null;
			File jsonFile = getInfoFile(m);
			if (jsonFile.exists()) {
				FileInputStream stream;
				try {
					stream = new FileInputStream(jsonFile);
					String jString = null;
					try {
						FileChannel fc = stream.getChannel();
						MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
						jString = Charset.defaultCharset().decode(bb).toString();
					} finally {
						stream.close();
					}

					JSONObject json = new JSONObject(jString);
					model = new ModelModel(json);
				} catch (Exception e) {
					Logger.e("Cannot parse json", e);
				}
			}
			if (model == null) {
				model = new ModelModel(m.getName(), m, new File(m, "model.JPG"));
			}
			addItem(model);
		}
	}

	private static File getInfoFile(File m) {
		return new File(m, "info.json");
	}

	private static void addItem(ModelModel item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.getName(), item);
	}

	public static void saveModelInfo(ModelModel model) throws Exception {
		JSONObject json = model.getJson();
		FileWriter writer = new FileWriter(getInfoFile(model.getDir()));
		writer.write(json.toString());
		writer.flush();
		writer.close();

	}

}
