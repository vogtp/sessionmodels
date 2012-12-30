package ch.almana.android.sessionsmodels.access;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.widget.Toast;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.helper.ImageHelper;
import ch.almana.android.sessionsmodels.log.Logger;

public class PortfolioAccess extends DirectoryAccess {

	public static void addToPortfolio(final Activity act, final File image) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File portfolioFile = new File(DirectoryAccess.getPortfolioDir(), image.getName());
				FileOutputStream out = null;
				try {
					Bitmap bitmap = ImageHelper.scaleImage(act, image, ImageHelper.getDisplayWidth(act));
					out = new FileOutputStream(portfolioFile);
					bitmap.compress(CompressFormat.PNG, 90, out);
					out.flush();
				} catch (IOException e) {
					Logger.e("Cannot save to portfolio", e);
					Toast.makeText(act, act.getString(R.string.error_saving_to_portfolio), Toast.LENGTH_LONG).show();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}
		}).start();

	}
}
