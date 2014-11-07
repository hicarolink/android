package main.tictactoe;

import main.utils.Board_utils;
import main.utils.MenuController;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OpeningGame extends MenuController {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opening_game);

		TextView txtTwoPlayers = (TextView) findViewById(R.id.two_players_opening);
		TextView txtEasyLevel = (TextView) findViewById(R.id.easyLevel);
		TextView txtNormalLevel = (TextView) findViewById(R.id.normalLevel);
		TextView txtHardLevel = (TextView) findViewById(R.id.hardLevel);
		TextView txtVersusPC = (TextView) findViewById(R.id.versus_pc);

		Typeface font = Typeface.createFromAsset(getAssets(),
				"DSAccent.ttf");

		txtTwoPlayers.setTypeface(font);
		txtVersusPC.setTypeface(font);
		txtEasyLevel.setTypeface(font);
		txtNormalLevel.setTypeface(font);
		txtHardLevel.setTypeface(font);
	}

	public void OpenGame(View v) {
		switch (v.getId()) {
		case R.id.two_players_opening:
			selectStartTurn(0);
			break;
		case R.id.easyLevel:
			selectStartTurn(1);
			break;
		case R.id.normalLevel:
			selectStartTurn(2);
			break;
		case R.id.hardLevel:
			selectStartTurn(3);
			break;
		default:
			break;
		}
	}

	public void selectStartTurn(final int level) {
		// Two players
		if (level == 0) {
			// Player 1 comeca
			StartGame(level, 0);
		} else {
			new AlertDialog.Builder(this)
					.setTitle(R.string.selectTurn)
					.setItems(R.array.selectTurn,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int turn) {
									// TODO Auto-generated method stub
									StartGame(level, turn);
								}
							}).show();
		}
	}

	public void StartGame(int level, int turn) {
		Intent intent = new Intent(OpeningGame.this, TicTacToe.class);
		intent.putExtra(Board_utils.GAME_LEVEL, level);
		intent.putExtra(Board_utils.GAME_TURN, turn);
		startActivity(intent);
	}

	public void ExitGame(View v) {
		finish();
	}
}
