package com.semantics3.pricematch;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.semantics3.api.Products;
import com.semantics3.pricematch.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button btnScan;

	// private TextView txtResponse;
	private Products products;

	private IntentIntegrator integrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		products = new Products(
				"SEM35B72B282E4F014BCCC42D0406BCB879F",
				"MWJhYzFmY2NmODAwMTY1Nzg0YzA1YTBlZDM1ZWUwNWU"
			);
		
		btnScan = (Button) findViewById(R.id.btnScan);

		integrator = new IntentIntegrator(this);

	}

	public void scanBarcode(View view) {
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			btnScan.setText("Requesting UPC from Semantics3 API...");
			new Sem3ProductTask().execute(scanResult.getContents());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void displayProductData(JSONObject data) {
		Intent intent = new Intent(this,ListSitesActivity.class);
		intent.putExtra(ListSitesActivity.PRODUCT_DATA, new Product(data));
		startActivity(intent);
	}

	private class Sem3ProductTask extends
			AsyncTask<String, Integer, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... params) {
			products.field("upc", params[0]);
			//products.field("upc","883974958450");
			JSONObject result = null;
			try {
				result = products.getProducts().getJSONArray("results")
						.getJSONObject(0);
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {

			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			btnScan.setText("Scan");
			if (result != null) {
				Log.d("DEBUG", result.toString());
				displayProductData(result);
			}
		}

	}
}
