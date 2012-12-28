package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.almana.android.sessionsmodels.model.SessionModel;

public class SessionAcess extends DirectoryAccess {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<SessionModel> ITEMS = new ArrayList<SessionModel>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, SessionModel> ITEM_MAP = new HashMap<String, SessionModel>();

	static {
		File modelsDir = getSessionsDir();
		File[] models = modelsDir.listFiles(DirectoryAccess.directoryFilter);
		for (int i = 0; i < models.length; i++) {
			File m = models[i];
			addItem(new SessionModel(m.getName(), m, m.listFiles()[0]));
		}
	}

	private static void addItem(SessionModel item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.getName(), item);
	}

}
