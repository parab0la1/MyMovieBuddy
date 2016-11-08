package com.example.joelnilsson6.finproject.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 *
 * The Actor class is used to make an object of an actor with attributes that is needed
 * for display in the app. This also includes a list with the actors current movies.
 */
public class Actor implements Serializable {

    private String firstName, lastName, urlForMovies, actorId;
    private ArrayList<Movie> actorsMovies;

    public Actor(String firstName, String secondName) {
        this.firstName = firstName;
        this.lastName = secondName;
        this.urlForMovies = "https://api.themoviedb.org/3/search/person?api_key=91ef50ef82f85a66f0bfd5792529f4bc&search_type=ngram&query=" + firstName + "+" + lastName;

        if(this.actorsMovies == null) {
            this.actorsMovies = new ArrayList<>();
        }
    }

    public String getActorId() {
        return actorId;
    }

    public ArrayList<Movie> getActorsMovies() {
        return actorsMovies;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUrlForMovies() {
        return urlForMovies;
    }

    public void setActorsMovies(ArrayList<Movie> actorsMovies) {
        this.actorsMovies = actorsMovies;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public void setUrlForMovies(String urlForMovies) {
        this.urlForMovies = urlForMovies;
    }
}
