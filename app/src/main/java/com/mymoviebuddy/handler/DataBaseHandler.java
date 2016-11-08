package com.example.joelnilsson6.finproject.handler;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.joelnilsson6.finproject.data.Actor;
import com.example.joelnilsson6.finproject.database.DataBaseHelper;

import java.util.ArrayList;

/**
 * Created by joel.nilsson6 on 2016-02-23.
 *
 * The DataBaseHandler is a manager for the database and is provided for filling
 * the search history list
 */
public class DataBaseHandler {

	private Spinner spinner;
	private DataBaseHelper dataBaseHelper;
	private Context context;

	public DataBaseHandler(DataBaseHelper dataBaseHelper, Spinner spinner, Context context) {
		this.dataBaseHelper = dataBaseHelper;
		this.spinner = spinner;
		this.context = context;
	}

	/**
	 * Fills the search history list via the adapter thats is set for the
	 * spinner
	 */
	public void fillHistoryList() {
		ArrayList<Actor> searchHistory = populateSearchHistory();
		ArrayList<String> searchHistoryStrings = new ArrayList<>();

		if (searchHistory != null) {
			for (int i = 0; i < searchHistory.size(); i++) {

				searchHistoryStrings
						.add(searchHistory.get(i).getFirstName() + " " + searchHistory.get(i).getLastName());

			}

			ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item, searchHistoryStrings);
			spinner.setAdapter(historyAdapter);
		}
	}

	/**
	 * Populates the search history list and returns it for use to the spinner.
	 * 
	 * @return is returning the filled arraylist of actors that was previously
	 *         searched for
	 */
	public ArrayList<Actor> populateSearchHistory() {

		Cursor result = dataBaseHelper.getAllData();
		ArrayList<Actor> tempActors = new ArrayList<Actor>();

		if (result.getCount() < 1) {
			return null;
		}

		while (result.moveToNext()) {

			String tempFirstName = result.getString(1);
			String tempLastName = result.getString(2);

			Actor tempActor = new Actor(tempFirstName, tempLastName);
			tempActors.add(tempActor);
		}

		return tempActors;
	}

	/**
	 * If the actors name already is in the database the return will be false
	 * and the names are not added
	 * 
	 * @param firstName
	 *            is the firstname of the current actor
	 * @param lastName
	 *            is the lastname of the current actor
	 * @return is returning the boolean true or false depending on the actors
	 *         existence in the database
	 */
	public boolean isNameInDataBase(String firstName, String lastName) {

		Cursor result = dataBaseHelper.getAllData();

		if (result.getCount() < 1) {
			return false;
		}

		while (result.moveToNext()) {
			String tempFirstName = result.getString(1);
			String tempLastName = result.getString(2);

			if (tempFirstName.equals(firstName) && tempLastName.equals(lastName)) {
				return true;
			}
		}
		return false;
	}
}
