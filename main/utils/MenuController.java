package pandoapps.utils;

import pandoapps.tictactoe.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MenuController extends Activity {

	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.pandoMenu:
			showAboutScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showAboutScreen() {
		Intent i = new Intent(Board_utils.pandoIntent);
		this.startActivity(i);
		return;
	}

}
