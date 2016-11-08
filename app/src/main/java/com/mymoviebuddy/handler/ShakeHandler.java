package com.example.joelnilsson6.finproject.handler;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.joelnilsson6.finproject.R;

/**
 * Created by joel.nilsson6 on 2016-02-25.
 *
 * Displays the dialog of which the user is informed to shake the device to
 * update the list
 */
public class ShakeHandler {

	private Context context;

	public ShakeHandler(Context context) {
		this.context = context;
	}

	/**
	 * Displays the dialog of which the user is informed to shake the device to
	 * update the list
	 */
	public void shakeForReload() {

		final Dialog shakeDialog = new Dialog(context);
		shakeDialog.setContentView(R.layout.activity_shake);
		Button btnOk = (Button) shakeDialog.findViewById(R.id.btnOk);

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shakeDialog.cancel();
			}
		});

		shakeDialog.show();
	}
}
