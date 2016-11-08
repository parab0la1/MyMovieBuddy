package com.example.joelnilsson6.finproject.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joelnilsson6.finproject.R;
import com.example.joelnilsson6.finproject.data.Movie;

import java.util.ArrayList;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 *
 * The MovieAdapter class is used for displaying the movies info, including
 * image for the current movie in the list of shown movies.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

	private ArrayList<Movie> listOfMovies;
	private Context currentContext;
	private ProgressDialog progressDialog;

	public MovieAdapter(Context context, int resource, ArrayList<Movie> films) {
		super(context, resource, films);
		this.listOfMovies = films;
		this.currentContext = context;
	}

	/**
	 * Creates the view for the adapter to create the random movie list
	 * 
	 * @param position
	 *            is the current position of the item in the item list
	 * @param convertView
	 *            is the current that will list the elements
	 * @param parent
	 *            is the viewgroup of the element where the image is to be shown
	 * @return is returning the created view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.item_list, parent, false);
		}
		Movie currentMovie = listOfMovies.get(position);

		if (position == 0) {
			progressDialog = new ProgressDialog(currentContext);
			progressDialog.setMessage("Generating movies...");
			progressDialog.show();
		}

		progressDialog.hide();

		if (currentMovie != null) {
			setMovieInfo(convertView, currentMovie);

			ImageView imgFilm = (ImageView) convertView.findViewById(R.id.imgMovie);

			String path = "";
			path = currentMovie.getPosterpath();

			imgFilm.setTag(path);
			new ImageLoadTask(path, imgFilm, currentContext, position).execute();
		}
		return convertView;
	}

	/**
	 * Sets the movie info in the right textviews depending on the current movie
	 * 
	 * @param convertView
	 *            is the view where the information is displayed
	 * @param currentMovie
	 *            is the current movie that is to be shown
	 */
	public void setMovieInfo(View convertView, Movie currentMovie) {

		TextView title = (TextView) convertView.findViewById(R.id.txtTitle);
		title.setText(currentMovie.getTitle());

		TextView date = (TextView) convertView.findViewById(R.id.txtDate);
		date.setText(currentMovie.getDate());

		TextView overview = (TextView) convertView.findViewById(R.id.txtOverview);
		overview.setText(currentMovie.getOverview());
	}

	@Override
	public Movie getItem(int position) {
		return listOfMovies.get(position);
	}

	@Override
	public int getCount() {
		return listOfMovies.size();
	}

}
