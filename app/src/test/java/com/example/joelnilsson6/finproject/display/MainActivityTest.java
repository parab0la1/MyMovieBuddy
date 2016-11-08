package com.example.joelnilsson6.finproject.display;

import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.data.Movie;
import com.example.joelnilsson6.finproject.handler.MovieSorter;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by joel.nilsson6 on 2016-02-22.
 *
 * This test shows that if a movie has a rating below 5.0 it
 * is removed from the current actors arraylist and the arraylist is
 * now filtered by rating above 5.0
 */
public class MainActivityTest {

    @Test
    public void testFilterMoviesByRating() throws Exception {

        MovieSorter movieSorter = new MovieSorter();

        Movie movieOne = new Movie("Seven", "2015-01-01", "288");
        Movie movieTwo = new Movie("Eight", "2015-02-01", "298");
        Movie movieThree = new Movie("Nine", "2015-03-01", "278");

        movieOne.setRating("0.0");
        movieTwo.setRating("7.8");
        movieThree.setRating("6.9");

        ArrayList<Movie> tempMovies = new ArrayList<Movie>();

        tempMovies.add(movieOne);
        tempMovies.add(movieTwo);
        tempMovies.add(movieThree);

        Actor tempActor = new Actor("Brad", "Pitt");

        tempActor.setActorsMovies(tempMovies);

        MainActivity mainActivity = new MainActivity();

        tempMovies = movieSorter.filterMoviesByRating(tempActor);

        // checks that the movie with rating below 5.0 is removed from the list

        assertEquals(tempMovies.get(0), movieTwo);

        assertEquals(tempMovies.get(1), movieThree);

        // expected size of the list is 2 instead of 3

        assertEquals(tempMovies.size(), 2);

        tempMovies = movieSorter.filterMoviesByRating(null);

        assertEquals(tempMovies, null);





    }
}