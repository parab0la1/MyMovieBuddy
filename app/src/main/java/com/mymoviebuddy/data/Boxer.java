package com.example.joelnilsson6.finproject.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 *
 * The Boxer class explicit function is to contain a list of actors that is used
 * for sending during runtime between acitivities.
 */
public class Boxer implements Serializable {

	private ArrayList<Actor> actors;

	public Boxer(ArrayList<Actor> actors) {

		if (actors == null) {
			actors = new ArrayList<Actor>();
		}

		this.actors = actors;

	}

	public ArrayList<Actor> getActors() {
		return actors;
	}
}
