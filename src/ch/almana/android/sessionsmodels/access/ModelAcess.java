package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		File[] models = modelsDir.listFiles();
		for (int i = 0; i < models.length; i++) {
			File m = models[i];
			addItem(new ModelModel(m.getName(), new File(m, "model.JPG")));
		}
	}

	private static void addItem(ModelModel item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.name, item);
	}

}
