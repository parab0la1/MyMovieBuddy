package com.example.joelnilsson6.finproject.data;

import java.io.Serializable;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 *
 * The Movie class is used for storing information in a created object of the
 * class. Within this object information about the movie which was created.
 */
public class Movie implements Serializable {

	private String title, date, id, posterpath, overview, rating;

	public Movie(String title, String date, String id) {

		this.title = title;
		this.date = date;
		this.id = id;

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getPosterpath() {
		return posterpath;
	}

	public void setPosterpath(String posterpath) {

		posterpath = "http://image.tmdb.org/t/p/w300" + posterpath;

		this.posterpath = posterpath;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
}
