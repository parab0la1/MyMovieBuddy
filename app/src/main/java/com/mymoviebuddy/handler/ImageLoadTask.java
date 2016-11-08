package com.example.joelnilsson6.finproject.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 *
 * Is used for the listview to properly show the images to each movie in the
 * list shown to the user.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
	private String url;
	private ImageView imageView;
	private ProgressDialog waitingDialog;
	private Context context;
	private ImageView currentImageView;
	private String path;

	public ImageLoadTask(String url, ImageView imageView, Context context, int position) {
		this.url = url;
		this.imageView = imageView;

		this.context = context;
		this.currentImageView = imageView;
		this.path = currentImageView.getTag().toString();

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	/**
	 * Is setting up a connection for the bitmap to download
	 * 
	 * @param params
	 * @return the current bitmap for setting in the imageview
	 */
	@Override
	protected Bitmap doInBackground(Void... params) {

		try {
			URL urlConnection = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;

		} catch (Exception e) {
			/* e.printStackTrace(); */
			Log.e("error", "file not found");
		}

		return null;
	}

	/**
	 * Sets the bitmap to the imageview. If the path is not the same as the
	 * intended imageview's path, the method stops or else it sets the correct
	 * bitmap
	 * 
	 * @param result
	 *            is the current bitmap to be set
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

		if (!imageView.getTag().toString().equals(path)) {
			return;
		}

		imageView.setImageBitmap(result);
	}
}
