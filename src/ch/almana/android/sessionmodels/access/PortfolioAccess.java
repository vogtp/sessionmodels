package ch.almana.android.sessionmodels.access;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.widget.Toast;
import ch.almana.android.sessionmodels.R;
import ch.almana.android.sessionmodels.helper.ImageHelper;
import ch.almana.android.sessionmodels.log.Logger;
import ch.almana.android.sessionmodels.model.PortfolioModel;

public class PortfolioAccess extends DirectoryAccess {

	public static PortfolioModel getPortfolio(Context ctx) {
		PortfolioModel portfolio = null;
		File portfolioDir = getPortfolioDir();
		try {
			portfolio = new PortfolioModel(readJsonInfo(portfolioDir));
		} catch (Exception e) {
			Logger.e("Cannot parse json", e);
		}
		if (portfolio == null) {
			portfolio = new PortfolioModel(ctx.getString(R.string.portfolio), portfolioDir);
		}
		return portfolio;
	}

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

	public static boolean isInPortfolio(File image) {
		if (image == null) {
			return false;
		}
		File pFile = new File(getPortfolioDir(), image.getName());
		if (pFile == null || !pFile.isFile()) {
			return false;
		}
		return pFile.exists();
	}

	public static void removeFromPortfolio(File image) {
		image.delete();
	}
}
