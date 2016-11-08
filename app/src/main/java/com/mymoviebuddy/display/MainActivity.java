package com.example.joelnilsson6.finproject.display;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.joelnilsson6.finproject.R;
import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.data.Boxer;
import com.example.joelnilsson6.finproject.data.Movie;
import com.example.joelnilsson6.finproject.database.DataBaseHelper;
import com.example.joelnilsson6.finproject.handler.DataBaseHandler;
import com.example.joelnilsson6.finproject.handler.MovieSorter;
import com.example.joelnilsson6.finproject.service.MovieService;
import com.example.joelnilsson6.finproject.service.MovieServiceCallback;

import java.util.ArrayList;

/*
* The Mainactivity of this app is the starting screen for the whole app.
* The User types in their favourite actor and it is added to the list so then
* it can be added to the database.
* */

public class MainActivity extends AppCompatActivity implements MovieServiceCallback {

	private Button btnAct, btnList;
	private EditText txtFirst, txtSecond;
	private String firstName, lastName;
	private MovieService movieservice;
	private ArrayList<Actor> actors;
	private ProgressDialog progressDialog;
	private static final int REQUEST_CODE_ONE = 1000;
	static final int SEND_ACTORS_REQUEST = 1;
	private DataBaseHelper dataBaseHelper;
	private Spinner spinner;
	private static boolean isFirstStartup = true;
	public static boolean isListFirstStartup = true;
	private boolean isSuccess;
	private DataBaseHandler dataBaseHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initializeItems();
		dataBaseHandler.fillHistoryList();
		movieservice = new MovieService(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		dataBaseHandler.fillHistoryList();
	}

	/**
	 * Initializes all items for the application, buttons, spinners and so
	 * forth.
	 */
	public void initializeItems() {

		btnAct = (Button) findViewById(R.id.btnAct);
		btnList = (Button) findViewById(R.id.btnList);
		spinner = (Spinner) findViewById(R.id.actor_spinner);
		txtFirst = (EditText) findViewById(R.id.txtFirstName);
		txtSecond = (EditText) findViewById(R.id.txtLastName);
		dataBaseHelper = new DataBaseHelper(getApplicationContext());
		dataBaseHandler = new DataBaseHandler(dataBaseHelper, spinner, getApplicationContext());

		spinner.setPrompt("Search History");

		if (actors == null) {
			actors = new ArrayList<>();
			btnList.setEnabled(false);

		}

		addListenerToActButton();
		addListenerToListButton();
		addListenerToSpinner();
	}

	/**
	 * Takes the selected actors first name and last name from the database and
	 * puts it correctly into the right textfields for search querys
	 */
	public void addListenerToSpinner() {

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String actorName = parent.getItemAtPosition(position).toString();

				String[] splited = actorName.split("\\s+");

				String name1 = splited[0];
				String name2 = splited[1];

				txtFirst = (EditText) findViewById(R.id.txtFirstName);
				txtSecond = (EditText) findViewById(R.id.txtLastName);

				if (isFirstStartup) {
					txtFirst.setText("");
					txtSecond.setText("");

				} else {
					txtFirst.setText(name1);
					txtSecond.setText(name2);
				}
				isFirstStartup = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * Adds the actor to the list of actors and fills his list of films with all
	 * their attributes
	 */
	public void addListenerToActButton() {

		btnAct.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				firstName = txtFirst.getText().toString();
				lastName = txtSecond.getText().toString();

				firstName = firstName.trim();
				lastName = lastName.trim();

				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("Adding actor...");
				progressDialog.show();
				btnAct.setEnabled(false);
				btnList.setEnabled(false);

				movieservice.getMovies(firstName, lastName);
			}
		});

	}

	/**
	 * Sends the user to the ListMovies activity that is generating the random
	 * movie list.
	 */
	public void addListenerToListButton() {

		btnList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ListMovies.class);

				Boxer bundle = new Boxer(actors);
				intent.putExtra("bundle", bundle);

				startActivityForResult(intent, SEND_ACTORS_REQUEST);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_clear) {

			dataBaseHelper = new DataBaseHelper(getApplicationContext());
			dataBaseHelper.clearDb();

			finish();
			startActivity(getIntent());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Gets the current actor from the service and sorts his list of films
	 * depending on the rating of the movie and saves it to the database
	 *
	 * @param currentActor
	 *            is the returned actor from the MovieService class
	 *
	 */
	@Override
	public void serviceSuccess(Actor currentActor) {
		Actor newActor = currentActor;

		MovieSorter movieSorter = new MovieSorter();
		ArrayList<Movie> tempActorsMovies = movieSorter.filterMoviesByRating(newActor);

		newActor.setActorsMovies(tempActorsMovies);

		if (newActor.getActorsMovies().size() > 38) {
			newActor.getActorsMovies().subList(38, newActor.getActorsMovies().size()).clear();
		}

		actors.add(newActor);
		putActorInDb();

		Toast.makeText(MainActivity.this, newActor.getFirstName() + " " + newActor.getLastName() + " was added!",
				Toast.LENGTH_SHORT).show();
		btnAct.setEnabled(true);
		btnList.setEnabled(true);
		progressDialog.hide();
	}

	/**
	 * If service failure the user gets a message that no actor was found and
	 * please try again
	 *
	 * @param exception
	 *            is the current exception that is thrown
	 */
	@Override
	public void serviceFailure(Exception exception) {

		if (actors == null || actors.size() == 0) {
			Toast.makeText(getApplicationContext(), "Actor not found, please try again!", Toast.LENGTH_SHORT).show();
			progressDialog.hide();
			btnAct.setEnabled(true);
		} else {
			btnAct.setEnabled(true);
			btnList.setEnabled(true);
			progressDialog.hide();
		}
	}

	/**
	 * Puts the first and lastname in the database for search history
	 */
	public void putActorInDb() {
		boolean isActorAlreadyInBase = dataBaseHandler.isNameInDataBase(firstName, lastName);

		if (isActorAlreadyInBase == false) {

			isSuccess = dataBaseHelper.insertData(firstName, lastName);

			if (isSuccess == true) {
				/*
				 * Toast.makeText(getApplicationContext(), "Success",
				 * Toast.LENGTH_SHORT).show();
				 */
			} else {
				Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * Gets the result from the List activity which is the list of actors in the
	 * application
	 *
	 * @param requestCode
	 *            is the request that is used to match the activityresult
	 * @param resultCode
	 *            is the code for describing if the result was ok or not
	 * @param data
	 *            is the returned intent from the other acitivity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SEND_ACTORS_REQUEST) {
			if (resultCode == RESULT_OK) {
				Intent resultIntent = data;

				Boxer bundleTwo = (Boxer) resultIntent.getSerializableExtra("bundleTwo");

				if (bundleTwo.getActors() != null) {
					actors = bundleTwo.getActors();
				} else {
				}
			}
		}

	}

}
