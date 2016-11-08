package com.example.joelnilsson6.finproject.handler;

import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.data.Movie;

import java.util.ArrayList;

/**
 * Created by joel.nilsson6 on 2016-02-25.
 */
public class MovieSorter {

	private Actor actor;

	/**
	 * Sorts the list and the returns the sorted list of movies
	 * 
	 * @param actor
	 *            the actor of which the list is to be sorted at
	 * @return returns the sorted list
	 */
	public ArrayList<Movie> filterMoviesByRating(Actor actor) {

		this.actor = actor;

		if (actor == null) {
			return null;
		}
		ArrayList<Movie> tempMovies = actor.getActorsMovies();
		double rating;

		for (int i = 0; i < tempMovies.size(); i++) {

			if (tempMovies.get(i).getRating() != null) {
				rating = Double.parseDouble(tempMovies.get(i).getRating());
				if (rating < 5) {
					tempMovies.remove(i);
				}
			}
		}

		return tempMovies;
	}
}
