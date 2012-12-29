package ch.almana.android.sessionsmodels.helper;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import ch.almana.android.sessionsmodels.access.DirectoryAccess;
import ch.almana.android.sessionsmodels.log.Logger;

public class GalleryHelper {

	public static void rescanDirectories(Context ctx) {
		rescanDirectories(ctx, DirectoryAccess.getTopDir());
	}

	public static void rescanDirectories(Context ctx, File directory) {
		Logger.i("Rescanning: " + directory.getAbsolutePath());

		ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(directory)));
	}

}
