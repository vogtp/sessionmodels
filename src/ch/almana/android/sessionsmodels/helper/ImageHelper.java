package ch.almana.android.sessionsmodels.helper;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageHelper {

	public static Bitmap scaleImage(Context ctx, File image, int width) throws FileNotFoundException
	{
		if (!image.exists()) {
			throw new FileNotFoundException();
		}
		Bitmap bitmap = null;
		CacheHelper cacheHelper = new CacheHelper(ctx);
		if (cacheHelper.cacheExists(image, width)) {
			bitmap = BitmapFactory.decodeFile(cacheHelper.getFile(image, width).getAbsolutePath());
		} else {

			bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
			if (bitmap == null) {
				return bitmap; // Checking for null & return, as suggested in comments
			}
			// Get current dimensions AND the desired bounding box
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			int bounding = dpToPx(ctx, width);

			// Determine how much to scale: the dimension requiring less scaling is
			// closer to the its side. This way the image always stays inside your
			// bounding box AND either x/y axis touches it.  
			float xScale = ((float) bounding) / w;
			float yScale = ((float) bounding) / h;
			float scale = (xScale <= yScale) ? xScale : yScale;

			// Create a matrix for the scaling and add the scaling data
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
			cacheHelper.cacheBitmap(image, bitmap, width);
		}

		return bitmap;

		// Create a new bitmap and convert it to a format understood by the ImageView 
		//		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		//		width = scaledBitmap.getWidth(); // re-use
		//		height = scaledBitmap.getHeight(); // re-use
		//		BitmapDrawable result = new BitmapDrawable(scaledBitmap);
		//		Log.i("Test", "scaled width = " + Integer.toString(width));
		//		Log.i("Test", "scaled height = " + Integer.toString(height));
		//
		//		// Apply the scaled bitmap
		//		view.setImageDrawable(result);
		//
		//		// Now change ImageView's dimensions to match the scaled image
		//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		//		params.width = width;
		//		params.height = height;
		//		view.setLayoutParams(params);
		//
		//		Log.i("Test", "done");
	}

	public static int dpToPx(Context ctx, int dp)
	{
		float density = ctx.getResources().getDisplayMetrics().density;
		return Math.round(dp * density);
	}
}
