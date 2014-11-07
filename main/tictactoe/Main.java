package main.tictactoe;

import main.utils.MenuController;
import android.os.Bundle;

public class Main extends MenuController {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
}
