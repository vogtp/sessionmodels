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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import ch.almana.android.sessionsmodels.R;
import ch.almana.android.sessionsmodels.log.Logger;
import ch.almana.android.sessionsmodels.model.ModelModel;
import ch.almana.android.sessionsmodels.view.ModelListFragment;

public class ModelAcess extends DirectoryAccess {

	/**
	 * An array of sample (dummy) items.
	 */
	private static List<ModelModel> items = new ArrayList<ModelModel>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	private static Map<String, ModelModel> ITEM_MAP = new HashMap<String, ModelModel>();

	public static List<ModelModel> getModels(boolean reread) {

		if (reread || items.size() < 1) {
			items.clear();
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
		return items;
	}

	private static File getInfoFile(File m) {
		return new File(m, "info.json");
	}

	private static void addItem(ModelModel item) {
		items.add(item);
		ITEM_MAP.put(item.getName(), item);
	}

	public static void saveModelInfo(ModelModel model) throws Exception {
		JSONObject json = model.getJson();
		FileWriter writer = new FileWriter(getInfoFile(model.getDir()));
		writer.write(json.toString());
		writer.flush();
		writer.close();
	}

	public static void addModel(final Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(R.string.dia_add_model_title);
		builder.setMessage(R.string.dia_add_model_msg);
		final EditText userInput = new EditText(ctx);
		builder.setView(userInput);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						String nick = userInput.getText().toString();
						File modelDir = new File(getModelsDir(), nick);
						if (modelDir.exists()) {
							int i = 1;
							while (modelDir.exists()) {
								// change the directory name
								modelDir = new File(getModelsDir(), nick + i++);
							}
						}
						modelDir.mkdir();
						ModelModel model = new ModelModel(nick, modelDir);
						try {
							saveModelInfo(model);
							ctx.sendBroadcast(new Intent(ModelListFragment.LIST_CHANGED));
						} catch (Exception e) {
							Logger.e("Cannot save model info after creating new model", e);
							Toast.makeText(ctx, R.string.dia_add_model_fail, Toast.LENGTH_LONG).show();
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel, null);

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

}
