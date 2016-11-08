package com.example.joelnilsson6.finproject.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 *
 * Fills the current movie with the correct information from the TMDB api and
 * returns the movie.
 */
public class MovieService extends Service {

	private MovieServiceCallback movieServiceCallback;
	private Exception error;
	private String firstName, lastName, urlForMovie, urlForMovieTwo, newUrlTwo;
	private Actor currentActorForMovies, tempActorTwo;
	private String urlParam;
	private ArrayList<Movie> tempMovieList;
	private Movie newMovie;
	private int maxCount, counter;

	public MovieService(MovieServiceCallback movieServiceCallback) {
		this.movieServiceCallback = movieServiceCallback;
	}

	/**
	 * Takes in the first and lastname of the actor and makes a query to the
	 * TMDB api and gets the information about the actors movies
	 * 
	 * @param firstNameIn
	 *            is the actors first name
	 * @param lastNameIn
	 *            is the actors last name
	 */
	public void getMovies(String firstNameIn, String lastNameIn) {

		this.urlParam = "https://api.themoviedb.org/3/search/person?api_key=91ef50ef82f85a66f0bfd5792529f4bc&search_type=ngram&query="
				+ firstNameIn + "+" + lastNameIn;
		this.firstName = firstNameIn;
		this.lastName = lastNameIn;

		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					URL url = new URL(urlParam);
					URLConnection urlConnection = url.openConnection();

					InputStream inputStream = urlConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

					StringBuilder result = new StringBuilder();
					String line;

					while ((line = bufferedReader.readLine()) != null) {
						result.append(line);
					}
					return result.toString();

				} catch (MalformedURLException e) {
					error = e;
					Toast.makeText(getApplicationContext(), "No actor found!", Toast.LENGTH_SHORT).show();
					return null;
				} catch (IOException e) {
					error = e;
					return null;
				}
			}

			@Override
			protected void onPostExecute(String s) {
				if (s == null && error != null) {
					movieServiceCallback.serviceFailure(error);
					return;
				}

				try {
					JSONObject data = new JSONObject(s);
					int count = data.optInt("total_results");

					if (count < 1) {
						movieServiceCallback.serviceFailure(new MovieServiceException("No movies found"));
						return;
					}

					JSONArray dataArray = data.getJSONArray("results");
					JSONObject finalObj = dataArray.getJSONObject(0);
					String id = finalObj.optString("id");

					Actor newActor = new Actor(firstName, lastName);
					newActor.setActorId(id);

					String newUrl = "https://api.themoviedb.org/3/person/" + newActor.getActorId()
							+ "/movie_credits?api_key=91ef50ef82f85a66f0bfd5792529f4bc";

					getActorsMovies(newActor, newUrl);

				} catch (JSONException e) {
					movieServiceCallback.serviceFailure(e);
				}
			}
		}.execute();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class MovieServiceException extends Exception {
		public MovieServiceException(String message) {
			super(message);
		}
	}

	/**
	 * Gets all the movies for the current actor and fills the actors movie list
	 * 
	 * @param currentActor
	 *            is the actor that is chosen
	 * @param newUrlIn
	 *            is the new url for the api query
	 */
	public void getActorsMovies(Actor currentActor, String newUrlIn) {

		currentActorForMovies = currentActor;
		urlForMovie = newUrlIn;

		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					URL url = new URL(urlForMovie);
					URLConnection urlConnection = url.openConnection();

					InputStream inputStream = urlConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

					StringBuilder result = new StringBuilder();
					String line;

					while ((line = bufferedReader.readLine()) != null) {
						result.append(line);
					}

					return result.toString();

				} catch (MalformedURLException e) {
					error = e;
					return null;
				} catch (IOException e) {
					error = e;
					return null;
				}
			}

			@Override
			protected void onPostExecute(String s) {
				if (s == null && error != null) {
					movieServiceCallback.serviceFailure(error);
					return;
				}

				try {
					// Get the result of all movies found
					JSONObject data = new JSONObject(s);
					JSONArray dataArray = data.getJSONArray("cast");
					int arrayCount = dataArray.length();

					// If no match was found, respond with an error
					if (arrayCount < 1) {
						movieServiceCallback.serviceFailure(new MovieServiceException("No movies to add!"));
						return;
					}

					tempMovieList = new ArrayList<Movie>();

					for (int i = 0; i < arrayCount; i++) {

						JSONObject obj = data.getJSONArray("cast").getJSONObject(i);

						String title = obj.getString("original_title");
						String date = obj.getString("release_date");
						String id = obj.getString("id");

						newMovie = new Movie(title, date, id);

						tempMovieList.add(newMovie);

					}

					currentActorForMovies.setActorsMovies(tempMovieList);

					getExtraData(currentActorForMovies);

				} catch (JSONException e) {
					movieServiceCallback.serviceFailure(e);
				}
			}
		}.execute();
	}

	/**
	 * Gets the extra data for each movie, sets the posterpath to the movie and
	 * sets the overview
	 * 
	 * @param tempActor
	 *            is the current actor that is chosen
	 */
	public void getExtraData(Actor tempActor) {

		tempActorTwo = tempActor;

		maxCount = tempActorTwo.getActorsMovies().size();

		if (maxCount > 39) {
			maxCount = 39;
		}

		counter = 0;

		for (int i = 0; i < maxCount; i++) {

			final Movie tempMovie = tempActorTwo.getActorsMovies().get(i);

			final String urlForExtraData = "http://api.themoviedb.org/3/movie/" + tempMovie.getId()
					+ "?api_key=91ef50ef82f85a66f0bfd5792529f4bc";

			new AsyncTask<String, String, String>() {

				@Override
				protected String doInBackground(String... params) {

					try {
						URL url = new URL(urlForExtraData);
						URLConnection urlConnection = url.openConnection();

						InputStream inputStream = urlConnection.getInputStream();
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

						StringBuilder result = new StringBuilder();
						String line;

						while ((line = bufferedReader.readLine()) != null) {
							result.append(line);
						}

						return result.toString();

					} catch (MalformedURLException e) {
						error = e;
						return null;
					} catch (IOException e) {
						error = e;
						return null;
					}
				}

				@Override
				protected void onPostExecute(String s) {
					if (s == null && error != null) {
						movieServiceCallback.serviceFailure(error);
						return;
					}

					try {
						JSONObject data = new JSONObject(s);

						String posterPath = data.optString("poster_path");
						String overView = data.optString("overview");
						String rating = data.optString("vote_average");

						tempMovie.setPosterpath(posterPath);
						tempMovie.setOverview(overView);
						tempMovie.setRating(rating);
						counter++;

						// Is the last index
						if (counter == maxCount - 1) {
							movieServiceCallback.serviceSuccess(tempActorTwo);
						}

					} catch (JSONException e) {
						movieServiceCallback.serviceFailure(e);
					}
				}
			}.execute();

		}
	}
}