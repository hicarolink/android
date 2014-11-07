package com.main.tobuylist;

import java.util.ArrayList;
import java.util.HashMap;

import com.main.controller.CtrItem;
import com.main.controller.ItemsAdapter;
import com.main.model.Item;
import com.main.utils.MenuController;
import com.main.listadecompras.R;
import com.google.ads.*;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends MenuController {

	private ListView listView;

	private ArrayList<HashMap<String, String>> mylist;

	private TextView txtViewShowTotal;
	private ItemsAdapter adapterItems;

	private boolean alphaCrescent = false;
	private boolean highestTotalPrice = false;
	private boolean highestPrice = false;
	private boolean highestQuantity = false;

	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintable);

		txtViewShowTotal = (TextView) findViewById(R.id.textViewShowTotal);

		if (savedInstanceState != null) {

			// Restore value of members from saved state
			mylist = (ArrayList<HashMap<String, String>>) savedInstanceState
					.getSerializable(getString(R.string.STATE_MYLIST));
			adapterItems = new ItemsAdapter(this, mylist, txtViewShowTotal);
			adapterItems
					.setListClone((ArrayList<HashMap<String, String>>) savedInstanceState
							.getSerializable(getString(R.string.STATE_MYLIST_CLONE)));
			adapterItems.notifyDataSetChanged();
		} else {
			mylist = new ArrayList<HashMap<String, String>>();
			adapterItems = new ItemsAdapter(this, mylist, txtViewShowTotal);
		}

		listView = (ListView) findViewById(R.id.newItemsList);
		listView.setAdapter(adapterItems);


	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	// ON RESUME
	public void onResume() {
		super.onResume();
		mylist.clear();
		adapterItems.getListClone().clear();
		ArrayList<Item> list = CtrItem.getAllItem(getApplicationContext());
		for (Item item : list) {
			HashMap<String, String> map = new HashMap<String, String>();
			float price, total;
			int quantity;
			price = Float.parseFloat(item.getPrice());
			quantity = Integer.parseInt(item.getQuantity());
			total = price * quantity;
			map.put(getString(R.string.name_item), item.getName());
			map.put(getString(R.string.price_item), item.getPrice());
			map.put(getString(R.string.quantity), item.getQuantity());
			map.put(getString(R.string.total_item), "" + total);

			mylist.add(item.getId(), map);
			adapterItems.getListClone().add(item.getId(), map);
		}

		adapterItems.notifyDataSetChanged();
	}

	// SALVANDO ESTADO DAS VARIAVEIS

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's current game state

		savedInstanceState.putSerializable(
				getString(R.string.STATE_MYLIST_CLONE),
				adapterItems.getListClone());

		savedInstanceState.putSerializable(getString(R.string.STATE_MYLIST),
				mylist);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	// ADICIONANDO NOVO ITEM

	public void addNewItem(View v) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(getString(R.string.name_item), getString(R.string.default_name));
		map.put(getString(R.string.price_item),
				getString(R.string.default_price));
		map.put(getString(R.string.quantity),
				getString(R.string.default_quantity));
		map.put(getString(R.string.total_item),
				getString(R.string.default_price));
		mylist.add(0, map);
		adapterItems.getListClone().add(0, map);
		adapterItems.setNewItem(true);
		adapterItems.notifyDataSetChanged();
	}

	// METODOS DE ORDENACAO
	// ORDENA POR NOME

	public void orderByName(View v) {
		orderListAlphabetical();
		alphaCrescent = !alphaCrescent;
	}

	public void orderListAlphabetical() {
		quick_sort(mylist, 0, mylist.size() - 1);
		quick_sort(adapterItems.getListClone(), 0, adapterItems.getListClone()
				.size() - 1);
		adapterItems.notifyDataSetChanged();
	}

	public void quick_sort(ArrayList<HashMap<String, String>> items, int ini,
			int fim) {
		int meio;

		if (ini < fim) {
			meio = partition(items, ini, fim);
			quick_sort(items, ini, meio);
			quick_sort(items, meio + 1, fim);
		}
	}

	public int partition(ArrayList<HashMap<String, String>> items, int ini,
			int fim) {
		String pivoName, pivoPrice, pivoQuantity, pivoTotal;
		int topo = 0, i;

		pivoName = items.get(ini).get(getString(R.string.name_item));
		pivoPrice = items.get(ini).get(getString(R.string.price_item));
		pivoQuantity = items.get(ini).get(getString(R.string.quantity));
		pivoTotal = items.get(ini).get(getString(R.string.total_item));

		topo = ini;

		for (i = ini + 1; i <= fim; i++) {
			if (compareStringAscendent(
					items.get(i).get(getString(R.string.name_item)), pivoName)) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(getString(R.string.name_item), pivoName);
			map.put(getString(R.string.price_item), pivoPrice);
			map.put(getString(R.string.quantity), pivoQuantity);
			map.put(getString(R.string.total_item), pivoTotal);
			items.set(topo, map);
		}

		return topo;
	}

	public boolean compareStringAscendent(String str1, String str2) {
		int result = str1.compareToIgnoreCase(str2);
		if (alphaCrescent == true) {
			return result < 0;
		} else {
			return result > 0;
		}
	}

	// ORDENA POR QUANTIDADE
	public void orderByQuantity(View v) {
		System.out.println("QUANTIDADE");
		orderListHighestQuantity();
		highestQuantity = !highestQuantity;
	}

	public void orderListHighestQuantity() {
		quick_sortQuantity(mylist, 0, mylist.size() - 1);
		quick_sortQuantity(adapterItems.getListClone(), 0, adapterItems
				.getListClone().size() - 1);
		adapterItems.notifyDataSetChanged();
	}

	public void quick_sortQuantity(ArrayList<HashMap<String, String>> items,
			int ini, int fim) {
		int meio;

		if (ini < fim) {
			meio = partitionQuantity(items, ini, fim);
			quick_sortQuantity(items, ini, meio);
			quick_sortQuantity(items, meio + 1, fim);
		}
	}

	public int partitionQuantity(ArrayList<HashMap<String, String>> items,
			int ini, int fim) {
		String pivoName, pivoPrice, pivoQuantity, pivoTotal;
		int topo = 0, i;
		int pivoQuantityValue = 0;

		pivoName = items.get(ini).get(getString(R.string.name_item));
		pivoPrice = items.get(ini).get(getString(R.string.price_item));
		pivoQuantity = items.get(ini).get(getString(R.string.quantity));
		pivoTotal = items.get(ini).get(getString(R.string.total_item));

		pivoQuantityValue = Integer.parseInt(pivoQuantity);

		topo = ini;

		for (i = ini + 1; i <= fim; i++) {
			int quantityValue = Integer.parseInt(items.get(i).get(
					getString(R.string.quantity)));
			if (highestQuantity && quantityValue > pivoQuantityValue) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			} else if (!highestQuantity && quantityValue < pivoQuantityValue) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(getString(R.string.name_item), pivoName);
			map.put(getString(R.string.price_item), pivoPrice);
			map.put(getString(R.string.quantity), pivoQuantity);
			map.put(getString(R.string.total_item), pivoTotal);
			items.set(topo, map);
		}

		return topo;
	}

	// ORDENA POR PRECO
	public void orderByPrice(View v) {
		orderListHighestPrice();
		highestPrice = !highestPrice;
	}

	public void orderListHighestPrice() {
		quick_sortPrice(mylist, 0, mylist.size() - 1);
		quick_sortPrice(adapterItems.getListClone(), 0, adapterItems
				.getListClone().size() - 1);
		adapterItems.notifyDataSetChanged();
	}

	public void quick_sortPrice(ArrayList<HashMap<String, String>> items,
			int ini, int fim) {
		int meio;

		if (ini < fim) {
			meio = partitionPrice(items, ini, fim);
			quick_sortPrice(items, ini, meio);
			quick_sortPrice(items, meio + 1, fim);
		}
	}

	public int partitionPrice(ArrayList<HashMap<String, String>> items,
			int ini, int fim) {
		String pivoName, pivoPrice, pivoQuantity, pivoTotal;
		int topo = 0, i;
		float pivoPriceValue = 0;

		pivoName = items.get(ini).get(getString(R.string.name_item));
		pivoPrice = items.get(ini).get(getString(R.string.price_item));
		pivoQuantity = items.get(ini).get(getString(R.string.quantity));
		pivoTotal = items.get(ini).get(getString(R.string.total_item));

		pivoPriceValue = Float.parseFloat(pivoPrice);

		topo = ini;

		for (i = ini + 1; i <= fim; i++) {
			float priceValue = Float.parseFloat(items.get(i).get(
					getString(R.string.price_item)));
			if (highestPrice && priceValue > pivoPriceValue) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			} else if (!highestPrice && priceValue < pivoPriceValue) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(getString(R.string.name_item), pivoName);
			map.put(getString(R.string.price_item), pivoPrice);
			map.put(getString(R.string.quantity), pivoQuantity);
			map.put(getString(R.string.total_item), pivoTotal);
			items.set(topo, map);
		}

		return topo;
	}

	// ORDENA POR PRECO TOTAL
	public void orderByTotalPrice(View v) {
		orderListHighestTotalPrice();
		highestTotalPrice = !highestTotalPrice;
	}

	public void orderListHighestTotalPrice() {
		quick_sortTotalPrice(mylist, 0, mylist.size() - 1);
		quick_sortTotalPrice(adapterItems.getListClone(), 0, adapterItems
				.getListClone().size() - 1);
		adapterItems.notifyDataSetChanged();
	}

	public void quick_sortTotalPrice(ArrayList<HashMap<String, String>> items,
			int ini, int fim) {
		int meio;

		if (ini < fim) {
			meio = partitionTotalPrice(items, ini, fim);
			quick_sortTotalPrice(items, ini, meio);
			quick_sortTotalPrice(items, meio + 1, fim);
		}
	}

	public int partitionTotalPrice(ArrayList<HashMap<String, String>> items,
			int ini, int fim) {
		String pivoName, pivoPrice, pivoQuantity, pivoTotal;
		int topo = 0, i;
		float pivoPriceValue = 0;

		pivoName = items.get(ini).get(getString(R.string.name_item));
		pivoPrice = items.get(ini).get(getString(R.string.price_item));
		pivoQuantity = items.get(ini).get(getString(R.string.quantity));
		pivoTotal = items.get(ini).get(getString(R.string.total_item));

		pivoPriceValue = Float.parseFloat(pivoTotal);

		topo = ini;

		for (i = ini + 1; i <= fim; i++) {
			float priceValue = Float.parseFloat(items.get(i).get(
					getString(R.string.total_item)));
			if (highestTotalPrice && priceValue > pivoPriceValue) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			} else if (!highestTotalPrice && priceValue < pivoPriceValue) {
				HashMap<String, String> map = new HashMap<String, String>();
				HashMap<String, String> map2 = new HashMap<String, String>();

				map.put(getString(R.string.name_item),
						items.get(i).get(getString(R.string.name_item)));
				map.put(getString(R.string.price_item),
						items.get(i).get(getString(R.string.price_item)));
				map.put(getString(R.string.quantity),
						items.get(i).get(getString(R.string.quantity)));
				map.put(getString(R.string.total_item),
						items.get(i).get(getString(R.string.total_item)));

				map2.put(getString(R.string.name_item), items.get(topo + 1)
						.get(getString(R.string.name_item)));
				map2.put(getString(R.string.price_item), items.get(topo + 1)
						.get(getString(R.string.price_item)));
				map2.put(getString(R.string.quantity),
						items.get(topo + 1).get(getString(R.string.quantity)));
				map2.put(getString(R.string.total_item), items.get(topo + 1)
						.get(getString(R.string.total_item)));

				items.set(topo, map);
				items.set(i, map2);
				topo++;
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(getString(R.string.name_item), pivoName);
			map.put(getString(R.string.price_item), pivoPrice);
			map.put(getString(R.string.quantity), pivoQuantity);
			map.put(getString(R.string.total_item), pivoTotal);
			items.set(topo, map);
		}

		return topo;
	}

	// REMOVENDO TODOS OS ITENS

	public void removeAllItems(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.remove_all_items_confirm))
				.setPositiveButton(getString(R.string.confirm_remove_alert),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								adapterItems.getListClone().clear();
								mylist.clear();
								adapterItems.clearDatabase();
								adapterItems.notifyDataSetChanged();
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});
		// Create the AlertDialog object and return it
		builder.create().show();
	}

	// REMOVE UM ITEM

	public void removeItem(final View v) {
		final int position = listView.getPositionForView(v);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getString(R.string.remove_item_alert)
						+ " "
						+ adapterItems.getListClone().get(position)
								.get(getString(R.string.item_name)))
				.setPositiveButton(getString(R.string.confirm_remove_alert),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mylist.remove(position);
								adapterItems.getListClone().remove(position);
								adapterItems.clearDatabase();
								adapterItems.notifyDataSetChanged();
								if (adapterItems.isEmpty()) {
									CtrItem.cleanTable(getApplicationContext());
								}
								if (mylist.size() == 0) {
									txtViewShowTotal
											.setText(getString(R.string.default_total));
								}
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});
		// Create the AlertDialog object and return it
		builder.create().show();
	}
}
