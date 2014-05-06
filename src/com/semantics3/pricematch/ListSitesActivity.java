package com.semantics3.pricematch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.zxing.integration.android.IntentIntegrator;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListSitesActivity extends Activity {
	public static final String PRODUCT_DATA = "productData";
	private Product product;
	
	private ImageView productImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listsites);
		TextView productName = (TextView)  findViewById(R.id.productName);
		ListView siteDetailsList = (ListView)  findViewById(R.id.productSiteDetails);
		this.productImage = (ImageView) findViewById(R.id.productImage);

		Intent intent = getIntent();
		this.product = intent.getParcelableExtra(PRODUCT_DATA);
		productName.setText(product.getName());
		new ProductImageTask().execute(product.getImageUrl());
		final List<Map<String,String>> sellers = product.getSiteDetails();
		siteDetailsList.setAdapter(new SellerListAdapter(this,sellers));
		siteDetailsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
				String url = sellers.get(position).get("url");
				 Uri uri = Uri.parse(url);
				 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				 startActivity(intent);
			}


		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_sites, menu);
		return true;
	}

	private class ProductImageTask extends AsyncTask<String, Integer, Drawable> {

		@Override
		protected Drawable doInBackground(String... params) {
	        Drawable d = null;
			try {
				d = Drawable.createFromStream(
						(InputStream) new URL(params[0]).getContent(), params[0]);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			return d;
		}
		
		@Override
		protected void onPostExecute(Drawable d) {
			if (d != null) {
				productImage.setImageDrawable(d);
			}
		}
		
	}

}
