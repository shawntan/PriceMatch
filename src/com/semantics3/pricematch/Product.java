package com.semantics3.pricematch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	private JSONObject data;
	private List<Map<String,String>> siteDetails;
  	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		public Product[] newArray(int size) {
			return new Product[size];
		}
	};
	
	public String getName() {
		return data.getString("name");
	}
	
	public String getImageUrl(){
		return data.getJSONArray("images").getString(0);
	}
	
	public List<Map<String, String>> getSiteDetails() {
		if (this.siteDetails == null) {
			List<Map<String,String>> result = new ArrayList<Map<String,String>>();
			JSONArray sd = data.getJSONArray("sitedetails");
			
			for (int i=0;i<sd.length();i++) {
				String offerUrl = sd.getJSONObject(i).getString("url");
				JSONArray sellers = sd.getJSONObject(i).getJSONArray("latestoffers");
				for (int j=0;j<sellers.length();j++) {
					JSONObject seller = sellers.getJSONObject(j);
					Map<String,String> sellerEntry = new HashMap<String, String>();	
					sellerEntry.put("url", offerUrl);
					sellerEntry.put("seller", seller.getString("seller"));
					sellerEntry.put("price",  seller.getString("price"));
					result.add(sellerEntry);
				}
			}
			this.siteDetails = result;
		}
		return this.siteDetails;
	}

	public Product(JSONObject data) {
		this.data = data;
	}
	
	
	private Product(Parcel in) {
		this.data = new JSONObject(new JSONTokener(in.readString()));
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.data.toString());
	}

	public int describeContents() {
		return 0;
	}

}
