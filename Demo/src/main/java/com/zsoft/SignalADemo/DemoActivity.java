package com.zsoft.SignalADemo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Constants;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zsoft.signala.Connection;
import com.zsoft.signala.SendCallback;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import java.util.HashMap;
import java.util.Map;

public class DemoActivity extends Activity {

	private static final String TAG = "KING";
	private AQuery aq;
	private Connection con = null;

	private GoogleApiClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		aq = new AQuery(this);
		String url = "http://192.168.1.75:8080/signalr/hubs";
		//String url = "http://192.168.1.75:8080/api/SingalerApi/AskData";
		aq.id(R.id.btnConnect).clicked(this, "startSignalA");
		aq.id(R.id.btnDisconnect).clicked(this, "stopSignalA");
		aq.id(R.id.btnSend).clicked(this, "sendMessage");
		aq.id(R.id.etText).text("zsoft");
		aq.id(R.id.tvReceivedMessage).text("No message yet");
		aq.id(R.id.btnDelayTest).clicked(this, "delayTest");

		con = new Connection(url, this, new LongPollingTransport()) {

			@Override
			public void OnError(Exception exception) {
				Log.d(TAG, "error:" + exception.getMessage());
				Toast.makeText(DemoActivity.this, "On error: " + exception.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void OnMessage(String message) {
				Log.d(TAG, "msg=:" + message);
				aq.id(R.id.tvReceivedMessage).text(message);
				Toast.makeText(DemoActivity.this, "Message: " + message, Toast.LENGTH_LONG).show();
			}

			@Override
			public void OnStateChanged(StateBase oldState, StateBase newState) {
				Log.d(TAG, "OnStateChanged=111111111111");
				aq.id(R.id.tvStatus).text(oldState.getState() + " -> " + newState.getState());
			}
		};
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	public void startSignalA() {
		if (con != null)
			con.Start();
	}

	public void stopSignalA() {
		if (con != null)
			con.Stop();
	}

	public void sendMessage() {
		if (con != null &&
				aq.id(R.id.etText).getText().length() > 0) {
			con.Send(aq.id(R.id.etText).getText(), new SendCallback() {
				public void OnError(Exception ex) {
					Toast.makeText(DemoActivity.this, "Error when sending: " + ex.getMessage(), Toast.LENGTH_LONG).show();
				}

				public void OnSent(CharSequence message) {
					Toast.makeText(DemoActivity.this, "Sent: " + message, Toast.LENGTH_SHORT).show();
				}

			});
		}
	}

	public void delayTest(View button) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("milliseconds", "10000");


		//update the text
		aq.id(R.id.tvStatus).clear().text("Starts....");

		String url = "http://delaytest.apphb.com/Delay";


		AjaxCallback<String> cb = new AjaxCallback<String>() {
			@Override
			public void callback(String url, String html, AjaxStatus status) {
				if (status.getCode() == 200) {
					aq.id(R.id.tvStatus).clear().text(html);


					// Second time
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("milliseconds", "110000");


					//update the text
					aq.id(R.id.tvStatus).clear().text("Second time we starts....");

					url = "http://delaytest.apphb.com/Delay";


					AjaxCallback<String> cb = new AjaxCallback<String>() {
						@Override
						public void callback(String url, String html, AjaxStatus status) {
							if (status.getCode() == 200) {
								aq.id(R.id.tvStatus).clear().text(html);

							} else {
								aq.id(R.id.tvStatus).clear().text("Fel. Duration: " + status.getDuration());

							}
						}
					};
					cb.url(url).type(String.class).expire(-1).params(params).method(Constants.METHOD_POST).timeout(115000);
					aq.ajax(cb);


				} else {
					aq.id(R.id.tvStatus).clear().text("Fel. Duration: " + status.getDuration());

				}
			}
		};

		//cb.timeout(12000);
		//aq.ajax(url, params, String.class, cb);

		cb.url(url).type(String.class).expire(-1).params(params).method(Constants.METHOD_POST).timeout(25000);
		aq.ajax(cb);

	}


	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Demo Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.zsoft.SignalADemo/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Demo Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.zsoft.SignalADemo/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}
}