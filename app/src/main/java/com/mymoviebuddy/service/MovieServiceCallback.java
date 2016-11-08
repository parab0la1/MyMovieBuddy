package com.example.joelnilsson6.finproject.service;

/**
 * Created by joel.nilsson6 on 2016-02-17.
 */

import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.data.Movie;

import java.util.ArrayList;

public interface MovieServiceCallback {
	void serviceSuccess(Actor currentActor);

	void serviceFailure(Exception exception);
}
