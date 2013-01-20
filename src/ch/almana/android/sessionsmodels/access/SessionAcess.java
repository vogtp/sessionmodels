package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.SessionModel;

public class SessionAcess extends DirectoryAccess {

	public static final List<SessionModel> DUMMIES = new ArrayList<SessionModel>();

	private static List<SessionModel> items = new ArrayList<SessionModel>();

	private static Map<String, SessionModel> ITEM_MAP = new HashMap<String, SessionModel>();

	public static List<SessionModel> getSessions() {
		return getSessions(false);
	}

	public static List<SessionModel> getSessions(boolean reread) {
		if (items.size() < 1 || reread) {
			items.clear();
			File sessionsDir = getSessionsDir();
			File[] sessions = sessionsDir.listFiles(DirectoryAccess.directoryFilter);
			if (sessions == null) {
				return DUMMIES;
			}
			for (int i = 0; i < sessions.length; i++) {
				File m = sessions[i];
				SessionModel session = null;
				try {
					session = new SessionModel(readJsonInfo(m));
				} catch (Exception e) {
					Logger.i("Cannot parse json", e);
				}
				if (session == null) {
					session = new SessionModel(m.getName(), m, m.listFiles()[0]);
				}
				addItem(session );
			}
		}
		return items;
	}

	private static void addItem(SessionModel item) {
		items.add(item);
		ITEM_MAP.put(item.getName(), item);
	}

}
