package com.example.joelnilsson6.finproject.display;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.joelnilsson6.finproject.R;
import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.data.Boxer;
import com.example.joelnilsson6.finproject.data.Movie;
import com.example.joelnilsson6.finproject.handler.MovieAdapter;
import com.example.joelnilsson6.finproject.handler.MovieRandomizer;
import com.example.joelnilsson6.finproject.handler.ShakeHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * The ListMovies class is used for the acitivity that is listing the movies in
 * the listview. It creates a list with five random movies to display and
 * everytime it generates a random actor from the actors list and also generates
 * a random movie from that actors list of movies.
 *
 * The accelerometer of the telephone is used for generating a new list of five
 * movies for the user to see. If the is shaken the movie list is updated and
 * display anew.
 */

public class ListMovies extends AppCompatActivity implements Serializable, SensorEventListener {

	private ArrayList<Actor> actors;
	private ArrayList<Movie> moviesToDisplay;
	private Intent intent;
	private MovieRandomizer movieRandomizer;
	private Button btnBack;
	private ListView lvListActors;
	private ImageView imgShake;
	private SensorManager sensorManager;
	private Sensor sensor;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 400;
	static final int SEND_ACTORS_REQUEST = 1;
	private Random rnd;
	private int RANDOM_ACTOR_INDEX, LAST_INDEX;
	private ShakeHandler shakeHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_actors);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initializeListMovies();
		movieRandomizer = new MovieRandomizer(actors);

		rnd = new Random();

		if (MainActivity.isListFirstStartup == true) {
			shakeHandler.shakeForReload();
		}
		MainActivity.isListFirstStartup = false;

		generateFilms();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_generate) {

			generateFilms();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void initializeListMovies() {

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

		shakeHandler = new ShakeHandler(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		addListenerToBtnBack();

		intent = getIntent();

		Boxer bundle = (Boxer) intent.getSerializableExtra("bundle");
		actors = bundle.getActors();

	}

	/**
	 * Sends the user back to the starting screen, also sends the arraylist of
	 * actors back to MainActivity
	 */
	public void addListenerToBtnBack() {

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
				Boxer bundleTwo = new Boxer(actors);

				backIntent.putExtra("bundleTwo", bundleTwo);
				setResult(Activity.RESULT_OK, backIntent);

				finish();
			}
		});
	}

	/**
	 * Gets the values from all 3 parametres of the accelerometer. If long
	 * enough time has passed since last update it takes a new update from the
	 * accelerometer. If the phone is shaked more than the threshold, the list
	 * refreshes
	 *
	 * @param event
	 *            is the sensor event for the accelerometer
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor mySensor = event.sensor;

		if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];

			long curTime = System.currentTimeMillis();

			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

				if (speed > SHAKE_THRESHOLD) {
					generateFilms();
				}

				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * Generates a list of five random movies with a randomly chosen actor from
	 * the list of actors. It also sets the adapter for the listview to list the
	 * movie list.
	 */
	public void generateFilms() {

		if (moviesToDisplay == null) {
			moviesToDisplay = new ArrayList<Movie>();
		}

		moviesToDisplay.clear();

		for (int i = 0; i < 5; i++) {

			int RANDOM_ACTOR_INDEX = rnd.nextInt(actors.size());

			Movie movie = movieRandomizer.randomizeFilm(moviesToDisplay, RANDOM_ACTOR_INDEX);

			moviesToDisplay.add(movie);

		}
		MovieAdapter movieAdapter = new MovieAdapter(this, R.layout.item_list, moviesToDisplay);
		lvListActors = (ListView) findViewById(R.id.lwActors);
		lvListActors.setAdapter(movieAdapter);

	}
}
