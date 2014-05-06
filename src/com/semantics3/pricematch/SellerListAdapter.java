package com.semantics3.pricematch;

import java.util.List;
import java.util.Map;

import com.semantics3.pricematch.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SellerListAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, String>> data;
	private static LayoutInflater inflater = null;

	public SellerListAdapter(Context context, List<Map<String, String>> data) {
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int i) {
		return data.get(i);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (vi == null) {
			vi = inflater.inflate(R.layout.selleritem, null);
		}
		TextView sellerName = (TextView) vi.findViewById(R.id.sellername);
		TextView sellerPrice = (TextView) vi.findViewById(R.id.sellerprice);
		sellerName.setText(data.get(position).get("seller"));
		sellerPrice.setText(data.get(position).get("price"));
		
		return vi;
	}

}
