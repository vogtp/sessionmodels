package ch.almana.android.sessionsmodels.helper;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import ch.almana.android.sessionsmodels.access.DirectoryAccess;
import ch.almana.android.sessionsmodels.log.Logger;

public class GalleryStartHelper implements MediaScannerConnectionClient {
	public File[] allFiles;
	private File scanPath;
	private static final String FILE_TYPE = "image/*";

	private MediaScannerConnection conn;
	private final Context ctx;

	public GalleryStartHelper(Context ctx) {
		super();
		this.ctx = ctx;
	}

	//	/** Called when the activity is first created. */
	//	@Override
	//	public void onCreate(Bundle savedInstanceState) {
	//		super.onCreate(savedInstanceState);
	//		//    setContentView(R.layout.main);
	//
	//		File folder = new File("/sdcard/youfoldername/");
	//		allFiles = folder.list();
	//		//   uriAllFiles= new Uri[allFiles.length];
	//		//    for(int i=0;i<allFiles.length;i++)
	//		//    {
	//		//        Log.d("all file path"+i, allFiles[i]+allFiles.length);
	//		//    }
	//		//  Uri uri= Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/yourfoldername/"+allFiles[0]));
	//		SCAN_PATH = Environment.getExternalStorageDirectory().toString() + "/yourfoldername/" + allFiles[0];
	//		Logger.d("Gallyery Scan Path " + SCAN_PATH);
	//	}

	public void openGallery(File dir)
	{
		Logger.d("Connected success" + conn);
		if (conn != null)
		{
			conn.disconnect();
		}
		scanPath = dir;
		DirectoryAccess.getNoMediaFile(dir).delete();
		conn = new MediaScannerConnection(ctx, this);
		conn.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		Logger.d("onMediaScannerConnected success" + conn);
		//		conn.scanFile(scanPath.getAbsolutePath(), FILE_TYPE);
		allFiles = scanPath.listFiles();
		conn.scanFile(allFiles[0].getAbsolutePath(), FILE_TYPE);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		try {
			Logger.d("onScanCompleted "+ uri + "success" + conn);
			if (uri != null)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setType(FILE_TYPE);
				intent.setData(uri);
				ctx.startActivity(intent);
			}
		} finally
		{
			conn.disconnect();
			conn = null;
		}
	}
}