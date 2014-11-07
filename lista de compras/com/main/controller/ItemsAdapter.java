package com.main.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import com.main.model.Item;
import com.main.listadecompras.R;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemsAdapter extends ArrayAdapter {
	ArrayList<HashMap<String, String>> list;
	// Lista clone que permite recuperar as mudancas feitas no Adapter
	ArrayList<HashMap<String, String>> listClone = new ArrayList<HashMap<String, String>>();
	Context context;
	// Referencia do preco total da MainActivity
	TextView showTotal;
	boolean newItem;

	public ItemsAdapter(Context context,
			ArrayList<HashMap<String, String>> list, TextView showTotal) {
		super(context, android.R.layout.simple_list_item_multiple_choice, list);
		this.list = list;
		this.context = context;
		this.showTotal = showTotal;
		this.newItem = false;

	}

	// @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.rowlinearlayout, null);
		}

		final EditText nameItem = (EditText) v
				.findViewById(R.id.edTextNameItem);
		final EditText priceItem = (EditText) v
				.findViewById(R.id.edTextPriceItem);
		final EditText quantityItem = (EditText) v
				.findViewById(R.id.edTextQuantityItem);
		final TextView totalItem = (TextView) v
				.findViewById(R.id.edTextTotalItem);

		nameItem.setText(list.get(position).get(
				context.getString(R.string.name_item)));
		priceItem.setText(list.get(position).get(
				context.getString(R.string.price_item)));
		quantityItem.setText(list.get(position).get(
				context.getString(R.string.quantity)));
		totalItem.setText(list.get(position).get(
				context.getString(R.string.total_item)));

		nameItem.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						HashMap<String, String> map = new HashMap<String, String>();

						map.put(context.getString(R.string.name_item), nameItem
								.getText().toString());
						map.put(context.getString(R.string.price_item),
								list.get(position).get(
										context.getString(R.string.price_item)));
						map.put(context.getString(R.string.quantity),
								list.get(position).get(
										context.getString(R.string.quantity)));
						map.put(context.getString(R.string.total_item),
								list.get(position).get(
										context.getString(R.string.total_item)));

						list.set(position, map);
						listClone.set(position, map);

						return true;

					default:
						break;
					}
				}
				return false;
			}
		});

		nameItem.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (list.size() > 0 && position < list.size()) {
					if (hasFocus == false) {
						HashMap<String, String> map = new HashMap<String, String>();

						if (newItem && position == 0) {
							map.put(context.getString(R.string.name_item), "");
						} else {
							map.put(context.getString(R.string.name_item),
									nameItem.getText().toString());
						}

						map.put(context.getString(R.string.price_item),
								list.get(position).get(
										context.getString(R.string.price_item)));
						map.put(context.getString(R.string.quantity),
								list.get(position).get(
										context.getString(R.string.quantity)));
						map.put(context.getString(R.string.total_item),
								list.get(position).get(
										context.getString(R.string.total_item)));

						list.set(position, map);
						listClone.set(position, map);

						setNewItem(false);
					} else {
						nameItem.setSelection(nameItem.length());
					}
				}
			}
		});

		priceItem.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						if (nameItem.getText().toString().equals("")) {
							Toast toast = Toast.makeText(context,
									context.getString(R.string.define_name),
									Toast.LENGTH_SHORT);
							toast.show();
							priceItem.setText(context
									.getString(R.string.default_price));
							priceItem.clearFocus();
						} else {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(context.getString(R.string.name_item),
									list.get(position)
											.get(context
													.getString(R.string.name_item)));
							if (priceItem.getText().toString().equals("")) {
								priceItem.setText(context
										.getString(R.string.default_price));
							}
							BigDecimal bd = new BigDecimal(priceItem.getText()
									.toString());
							bd = bd.setScale(2, RoundingMode.HALF_UP);
							if (bd.floatValue() < 0) {
								bd = bd.multiply(new BigDecimal("-1"));
							}
							priceItem.setText("" + bd);
							map.put(context.getString(R.string.price_item), ""
									+ bd);
							map.put(context.getString(R.string.quantity),
									list.get(position)
											.get(context
													.getString(R.string.quantity)));

							if (!quantityItem.getText().toString().equals("")) {
								String totalItemLine = calcTotalPriceItem(
										priceItem.getText().toString(),
										quantityItem.getText().toString());

								totalItem.setText(totalItemLine);

								map.put(context.getString(R.string.total_item),
										totalItemLine);
							} else {
								map.put(context.getString(R.string.total_item),
										list.get(position)
												.get(context
														.getString(R.string.total_item)));
							}

							list.set(position, map);
							listClone.set(position, map);

							calcTotal();
						}
						return true;

					default:
						break;
					}
				}
				return false;
			}
		});

		priceItem.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (list.size() > 0 && position < list.size()) {
					if (hasFocus == false) {
						if (nameItem.getText().toString().equals("")) {
							Toast toast = Toast.makeText(context,
									context.getString(R.string.define_name),
									Toast.LENGTH_SHORT);
							toast.show();
							priceItem.setText(context
									.getString(R.string.default_price));
							priceItem.clearFocus();
						} else {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(context.getString(R.string.name_item),
									list.get(position)
											.get(context
													.getString(R.string.name_item)));
							if (priceItem.getText().toString().equals("")) {
								priceItem.setText(context
										.getString(R.string.default_price));
							}
							BigDecimal bd = new BigDecimal(priceItem.getText()
									.toString());
							bd = bd.setScale(2, RoundingMode.HALF_UP);
							if (bd.floatValue() < 0) {
								bd = bd.multiply(new BigDecimal("-1"));
							}
							priceItem.setText("" + bd);
							map.put(context.getString(R.string.price_item), ""
									+ bd);
							map.put(context.getString(R.string.quantity),
									list.get(position)
											.get(context
													.getString(R.string.quantity)));

							if (!quantityItem.getText().toString().equals("")) {
								String totalItemLine = calcTotalPriceItem(
										priceItem.getText().toString(),
										quantityItem.getText().toString());

								totalItem.setText(totalItemLine);

								map.put(context.getString(R.string.total_item),
										totalItemLine);
							} else {
								map.put(context.getString(R.string.total_item),
										list.get(position)
												.get(context
														.getString(R.string.total_item)));
							}

							list.set(position, map);
							listClone.set(position, map);

							calcTotal();
						}
					} else {
						priceItem.setSelection(priceItem.length());
					}
				}
			}
		});

		quantityItem.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						if (nameItem.getText().toString().equals("")) {
							Toast toast = Toast.makeText(context,
									context.getString(R.string.define_name),
									Toast.LENGTH_SHORT);
							toast.show();
							quantityItem.setText(context
									.getString(R.string.default_quantity));
							quantityItem.clearFocus();
						} else {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(context.getString(R.string.name_item),
									list.get(position)
											.get(context
													.getString(R.string.name_item)));
							map.put(context.getString(R.string.price_item),
									list.get(position)
											.get(context
													.getString(R.string.price_item)));

							if (quantityItem.getText().toString().equals("")) {
								quantityItem.setText(context
										.getString(R.string.default_quantity));
							}
							int quant = Integer.parseInt(quantityItem.getText()
									.toString());
							if (quant < 0)
								quant *= -1;
							quantityItem.setText("" + quant);

							map.put(context.getString(R.string.quantity), ""
									+ quant);

							if (!priceItem.getText().toString().equals("")) {
								String totalItemLine = calcTotalPriceItem(
										priceItem.getText().toString(),
										quantityItem.getText().toString());

								totalItem.setText(totalItemLine);
								if (!totalItem.equals(context
										.getString(R.string.default_total))) {
									quantityItem.clearFocus();
								}

								map.put(context.getString(R.string.total_item),
										totalItemLine);
							} else {
								map.put(context.getString(R.string.total_item),
										list.get(position)
												.get(context
														.getString(R.string.total_item)));
							}

							list.set(position, map);
							listClone.set(position, map);

							calcTotal();
						}
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});

		quantityItem.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (list.size() > 0 && position < list.size()) {
					if (hasFocus == false) {
						if (nameItem.getText().toString().equals("")) {
							Toast toast = Toast.makeText(context,
									context.getString(R.string.define_name),
									Toast.LENGTH_SHORT);
							toast.show();
							quantityItem.setText(context
									.getString(R.string.default_quantity));
							quantityItem.clearFocus();
						} else {
							if (!list.get(position)
									.get(context.getString(R.string.name_item))
									.equals("")) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put(context.getString(R.string.name_item),
										list.get(position)
												.get(context
														.getString(R.string.name_item)));
								map.put(context.getString(R.string.price_item),
										list.get(position)
												.get(context
														.getString(R.string.price_item)));

								if (quantityItem.getText().toString()
										.equals("")) {
									quantityItem.setText(context
											.getString(R.string.default_quantity));
								}
								int quant = Integer.parseInt(quantityItem
										.getText().toString());
								if (quant < 0)
									quant *= -1;

								map.put(context.getString(R.string.quantity),
										"" + quant);

								if (!priceItem.getText().toString().equals("")) {
									String totalItemLine = calcTotalPriceItem(
											priceItem.getText().toString(),
											quantityItem.getText().toString());

									totalItem.setText(totalItemLine);

									map.put(context
											.getString(R.string.total_item),
											totalItemLine);
								} else {
									map.put(context
											.getString(R.string.total_item),
											list.get(position)
													.get(context
															.getString(R.string.total_item)));
								}

								list.set(position, map);
								listClone.set(position, map);

								calcTotal();
							}

						}
					} else {
						quantityItem.setSelection(quantityItem.length());
					}
				}
			}
		});

		if (position + 1 == list.size()) {
			calcTotal();
			addToLocalDatabase();
		}
		if (list.size() == 0) {
			addToLocalDatabase();
		}
		return v;
	}

	public void calcTotal() {
		showTotal.setText(calcTotalPrice());
	}

	public String calcTotalPrice() {
		float total = 0;

		String price, quantity;
		for (int i = 0; i < list.size(); i++) {
			price = list.get(i).get(context.getString(R.string.price_item));
			quantity = list.get(i).get(context.getString(R.string.quantity));
			total += Float.parseFloat(calcTotalPriceItem(price, quantity));
		}

		BigDecimal bd = new BigDecimal("" + total);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return "" + bd;
	}

	public String calcTotalPriceItem(String price, String quantity) {
		float total = 0;
		if (price.equals("")) {
			price = "0";
		}
		if (quantity.equals("")) {
			quantity = "0";
		}
		int quant = Integer.parseInt(quantity);
		float pr = Float.parseFloat(price);
		total = quant * pr;
		BigDecimal bd = new BigDecimal("" + total);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return "" + bd;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public String[] getValues() {
		return getValues();
	}

	public ArrayList<HashMap<String, String>> getListClone() {
		return listClone;
	}

	public void setListClone(ArrayList<HashMap<String, String>> listClone) {
		this.listClone = listClone;
	}

	public void clearDatabase() {
		if (list.size() == 0) {
			CtrItem.cleanTable(context);
		}
	}

	public void addToLocalDatabase() {
		System.out.println("ADICIONANDO");
		CtrItem.cleanTable(context);
		for (int i = 0; i < getListClone().size(); i++) {
			Item item = new Item();
			item.setId(i);
			item.setName(getListClone().get(i).get(
					context.getString(R.string.name_item)));
			item.setPrice(getListClone().get(i).get(
					context.getString(R.string.price_item)));
			item.setQuantity(getListClone().get(i).get(
					context.getString(R.string.quantity)));

			CtrItem.add(item, context);
		}
	}

	public boolean isNewItem() {
		return newItem;
	}

	public void setNewItem(boolean newItem) {
		this.newItem = newItem;
	}

}
