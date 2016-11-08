package com.example.joelnilsson6.finproject.handler;

import android.util.Log;
import com.example.joelnilsson6.finproject.data.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by joel.nilsson6 on 2016-02-11. Randomizes a movie from the current
 * actors selected movie list
 */
public class MovieRandomizer {

	private ArrayList<Actor> actors;
	private Actor currentActor;
	private Movie currentMovie;
	private int RANDOM_MOVIE_INDEX, RANDOM_ACTOR_INDEX;
	private Random rnd;
	private ArrayList<Movie> moviesToDisplay;

	public MovieRandomizer(ArrayList<Actor> actors) {
		this.actors = actors;
	}

	public MovieRandomizer() {

	}

	/**
	 * Randomizes a movie and checks if the random number is the same as the
	 * previous number. Then a new number is generated and a new movie is
	 * randomized
	 * 
	 * @param moviesToDisplay
	 *            is the list the movies that finally are about to be displayed
	 * @param randomNumber
	 *            is the random number that is used for selected the current
	 *            movie
	 * @return returns the randomized movie
	 */
	public Movie randomizeFilm(ArrayList<Movie> moviesToDisplay, int randomNumber) {

		this.moviesToDisplay = moviesToDisplay;
		this.RANDOM_ACTOR_INDEX = randomNumber;

		if (actors.size() == 1) {
			RANDOM_ACTOR_INDEX = 0;
		}

		currentActor = actors.get(RANDOM_ACTOR_INDEX);
		rnd = new Random();
		RANDOM_MOVIE_INDEX = rnd.nextInt(currentActor.getActorsMovies().size() - 1);
		currentMovie = currentActor.getActorsMovies().get(RANDOM_MOVIE_INDEX);

		while (currentMovie.getPosterpath() == null) {
			RANDOM_MOVIE_INDEX = rnd.nextInt(currentActor.getActorsMovies().size() - 1);
			currentMovie = currentActor.getActorsMovies().get(RANDOM_MOVIE_INDEX);

		}

		if (checkIfMovieIsInList() == true) {
			RANDOM_MOVIE_INDEX = rnd.nextInt(currentActor.getActorsMovies().size() - 1);
			currentMovie = currentActor.getActorsMovies().get(RANDOM_MOVIE_INDEX);
		}
		return currentMovie;
	}

	/**
	 * Checks if the movie already is in the list to display
	 * 
	 * @return is returning a boolean for which if the movies already is in the
	 *         list or not.
	 */
	public boolean checkIfMovieIsInList() {

		for (int i = 0; i < moviesToDisplay.size(); i++) {
			if (currentMovie.getTitle().equals(moviesToDisplay.get(i).getTitle())) {
				return true;
			}
		}

		return false;
	}
}
