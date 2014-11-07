package main.tictactoe;

import java.util.ArrayList;
import java.util.Random;

import main.utils.Board_utils;
import main.utils.MenuController;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class TicTacToe extends MenuController {

	private boolean player_turn;
	private int[][] board = new int[3][3];
	private boolean two_players;
	private int playerSymbol;
	private int machineSymbol;
	private boolean easyMode, normalMode, HardMode, drawGame;
	private int gameLevel;
	private int initialTurn;
	private int x_score;
	private int o_score;
	private TextView textXScore;
	private TextView textOScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tictactoe);

		x_score = 0;
		textXScore = (TextView) findViewById(R.id.textViewXScore);
		o_score = 0;
		textOScore = (TextView) findViewById(R.id.textViewOScore);
		gameLevel = getIntent().getIntExtra(Board_utils.GAME_LEVEL, 0);
		initialTurn = getIntent().getIntExtra(Board_utils.GAME_TURN, 0);

		setGame();
	}

	public void addItem(View v) {
		if (isGameOver())
			setGame();
		else {
			if (two_players || isPlayer_turn()) {
				switch (v.getId()) {
				case R.id.item00:
					markPlayerSymbol(0, 0);
					break;
				case R.id.item01:
					markPlayerSymbol(0, 1);
					break;
				case R.id.item02:
					markPlayerSymbol(0, 2);
					break;
				case R.id.item10:
					markPlayerSymbol(1, 0);
					break;
				case R.id.item11:
					markPlayerSymbol(1, 1);
					break;
				case R.id.item12:
					markPlayerSymbol(1, 2);
					break;
				case R.id.item20:
					markPlayerSymbol(2, 0);
					break;
				case R.id.item21:
					markPlayerSymbol(2, 1);
					break;
				case R.id.item22:
					markPlayerSymbol(2, 2);
					break;
				}
			}
			checkPlayer();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void checkPlayer() {
		if (!isPlayer_turn() && !two_players && !isGameOver()) {
			winWhenPossible();
		}
	}

	public boolean markPlayerSymbol(int posI, int posJ) {
		if (board[posI][posJ] == Board_utils.MARK_EMPTY) {
			board[posI][posJ] = isPlayer_turn() ? getPlayerSymbol()
					: getMachineSymbol();

			ImageView iv = getImageView(posI, posJ);
			Animation myFadeInAnimation = AnimationUtils.loadAnimation(this,
					R.anim.fadein);
			int x = getResources().getIdentifier("@drawable/cruz", null,
					getPackageName());
			int o = getResources().getIdentifier("@drawable/ball", null,
					getPackageName());
			Drawable res;
			if (getSymbol() == 1)
				res = getResources().getDrawable(x);
			else
				res = getResources().getDrawable(o);
			iv.setImageDrawable(res);
			iv.setVisibility(View.VISIBLE);
			iv.startAnimation(myFadeInAnimation);

			switchTurn();

			if (isGameOver()) {
				if (drawGame == false) {
					if (getSymbol() == 0)
						x_score++;
					else if (getSymbol() == 1)
						o_score++;
					animaWinner();
				}
			}

			return true;
		}
		return false;
	}

	public ImageView getImageView(int i, int j) {
		if (i == 0 && j == 0)
			return (ImageView) findViewById(R.id.item00);
		if (i == 0 && j == 1)
			return (ImageView) findViewById(R.id.item01);
		if (i == 0 && j == 2)
			return (ImageView) findViewById(R.id.item02);
		if (i == 1 && j == 0)
			return (ImageView) findViewById(R.id.item10);
		if (i == 1 && j == 1)
			return (ImageView) findViewById(R.id.item11);
		if (i == 1 && j == 2)
			return (ImageView) findViewById(R.id.item12);
		if (i == 2 && j == 0)
			return (ImageView) findViewById(R.id.item20);
		if (i == 2 && j == 1)
			return (ImageView) findViewById(R.id.item21);
		return (ImageView) findViewById(R.id.item22);
	}

	public void easyLevel() {
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> cols = new ArrayList<Integer>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == Board_utils.MARK_EMPTY) {
					rows.add(i);
					cols.add(j);
				}
			}
		}
		Random generator = new Random();
		int pos = generator.nextInt(rows.size());
		markPlayerSymbol(rows.get(pos), cols.get(pos));
	}

	public void normalLevel() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == Board_utils.MARK_EMPTY) {
					// Verifica horizontais
					if (j == 0) {
						if (board[i][1] == board[i][2]) {
							if (board[i][1] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (j == 1) {
						if (board[i][0] == board[i][2]) {
							if (board[i][0] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (j == 2) {
						if (board[i][j - 1] == board[i][j - 2]) {
							if (board[i][j - 1] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}

					// Verifica verticais
					if (i == 0) {
						if (board[i + 1][j] == board[i + 2][j]) {
							if (board[i + 1][j] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 1) {
						if (board[i - 1][j] == board[i + 1][j]) {
							if (board[i - 1][j] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 2) {
						if (board[i - 1][j] == board[i - 2][j]) {
							if (board[i - 1][j] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}

					// Diagonais
					if (i == j) {
						if (i == 0) {
							if (board[1][1] == board[2][2]) {
								if (board[1][1] != Board_utils.MARK_EMPTY) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						} else if (i == 2) {
							if (board[1][1] == board[0][0]) {
								if (board[1][1] != Board_utils.MARK_EMPTY) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						} else {
							if (board[0][0] == board[2][2]) {
								if (board[0][0] != Board_utils.MARK_EMPTY) {
									markPlayerSymbol(i, j);
									return;
								}
							}
							if (board[2][0] == board[0][2]) {
								if (board[2][0] != Board_utils.MARK_EMPTY) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						}
					} else if (i == 2 && j == 0) {
						if (board[1][1] == board[0][2]) {
							if (board[1][1] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 0 && j == 2) {
						if (board[2][0] == board[1][1]) {
							if (board[2][0] != Board_utils.MARK_EMPTY) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}
				}
			}
		}
		// Chama o modo facil
		easyLevel();
	}

	public void hardLevel() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == Board_utils.MARK_EMPTY) {
					// Verifica horizontais
					if (j == 0) {
						if (board[i][1] == board[i][2]) {
							if (board[i][1] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (j == 1) {
						if (board[i][0] == board[i][2]) {
							if (board[i][0] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (j == 2) {
						if (board[i][j - 1] == board[i][j - 2]) {
							if (board[i][j - 1] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}

					// Verifica verticais
					if (i == 0) {
						if (board[i + 1][j] == board[i + 2][j]) {
							if (board[i + 1][j] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 1) {
						if (board[i - 1][j] == board[i + 1][j]) {
							if (board[i - 1][j] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 2) {
						if (board[i - 1][j] == board[i - 2][j]) {
							if (board[i - 1][j] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}

					// Diagonais
					if (i == j) {
						if (i == 0) {
							if (board[1][1] == board[2][2]) {
								if (board[1][1] == getPlayerSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						} else if (i == 2) {
							if (board[1][1] == board[0][0]) {
								if (board[1][1] == getPlayerSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						} else {
							if (board[0][0] == board[2][2]) {
								if (board[0][0] == getPlayerSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
							if (board[2][0] == board[0][2]) {
								if (board[2][0] == getPlayerSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						}
					} else if (i == 2 && j == 0) {
						if (board[1][1] == board[0][2]) {
							if (board[1][1] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 0 && j == 2) {
						if (board[2][0] == board[1][1]) {
							if (board[2][0] == getPlayerSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}
				}
			}
		}

		normalLevel();
	}

	public void winWhenPossible() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == Board_utils.MARK_EMPTY) {
					// Verifica horizontais
					if (j == 0) {
						if (board[i][1] == board[i][2]) {
							if (board[i][1] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (j == 1) {
						if (board[i][0] == board[i][2]) {
							if (board[i][0] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (j == 2) {
						if (board[i][j - 1] == board[i][j - 2]) {
							if (board[i][j - 1] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}

					// Verifica verticais
					if (i == 0) {
						if (board[i + 1][j] == board[i + 2][j]) {
							if (board[i + 1][j] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 1) {
						if (board[i - 1][j] == board[i + 1][j]) {
							if (board[i - 1][j] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 2) {
						if (board[i - 1][j] == board[i - 2][j]) {
							if (board[i - 1][j] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}

					// Verifica diagonais
					if (i == j) {
						if (i == 0) {
							if (board[1][1] == board[2][2]) {
								if (board[1][1] == getMachineSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						} else if (i == 2) {
							if (board[1][1] == board[0][0]) {
								if (board[1][1] == getMachineSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						} else {
							if (board[0][0] == board[2][2]) {
								if (board[0][0] == getMachineSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
							if (board[2][0] == board[0][2]) {
								if (board[2][0] == getMachineSymbol()) {
									markPlayerSymbol(i, j);
									return;
								}
							}
						}
					} else if (i == 2 && j == 0) {
						if (board[1][1] == board[0][2]) {
							if (board[1][1] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					} else if (i == 0 && j == 2) {
						if (board[2][0] == board[1][1]) {
							if (board[2][0] == getMachineSymbol()) {
								markPlayerSymbol(i, j);
								return;
							}
						}
					}
				}
			}
		}

		if (easyMode) {
			easyLevel();
		} else if (normalMode) {
			normalLevel();
		} else if (HardMode) {
			hardLevel();
		}
	}

	public void restartGame(View v) {
		setGame();
	}

	public void backMenu(View v) {
		startBoard();
		finish();
	}

	public int getSymbol() {
		int symbol = playerSymbol;

		if (!isPlayer_turn()) {
			symbol = machineSymbol;
		}
		return symbol;
	}

	public void switchTurn() {
		setPlayer_turn(!isPlayer_turn());
	}

	public boolean isPlayer_turn() {
		return player_turn;
	}

	public void setPlayer_turn(boolean player_turn) {
		this.player_turn = player_turn;
	}

	public void startBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = Board_utils.MARK_EMPTY;
			}
		}
	}

	public boolean isGameOver() {
		if (board[0][0] != Board_utils.MARK_EMPTY
				&& (verifyBoard(0, 0, 0, 1) && verifyBoard(0, 0, 0, 2)
						|| verifyBoard(0, 0, 1, 0) && verifyBoard(0, 0, 2, 0) || verifyBoard(
						0, 0, 1, 1) && verifyBoard(0, 0, 2, 2))) {
			return true;
		} else if (board[0][1] != Board_utils.MARK_EMPTY
				&& verifyBoard(0, 1, 1, 1) && verifyBoard(0, 1, 2, 1)) {
			return true;
		} else if (board[0][2] != Board_utils.MARK_EMPTY
				&& (verifyBoard(0, 2, 1, 1) && verifyBoard(0, 2, 2, 0) || verifyBoard(
						0, 2, 1, 2) && verifyBoard(0, 2, 2, 2))) {
			return true;
		} else if (board[1][0] != Board_utils.MARK_EMPTY
				&& verifyBoard(1, 0, 1, 1) && verifyBoard(1, 0, 1, 2)) {
			return true;
		} else if (board[2][0] != Board_utils.MARK_EMPTY
				&& verifyBoard(2, 0, 2, 1) && verifyBoard(2, 0, 2, 2)) {
			return true;
		} else {
			if (verifyDraw()) {
				drawGame = true;
				return true;
			}
		}
		return false;
	}

	public void selectLevel() {
		switch (gameLevel) {
		case 0:
			two_players = true;
			easyMode = normalMode = HardMode = false;
			break;
		case 1:
			easyMode = true;
			two_players = normalMode = HardMode = false;
			break;
		case 2:
			normalMode = true;
			two_players = easyMode = HardMode = false;
			break;
		case 3:
			HardMode = true;
			two_players = easyMode = normalMode = false;
			break;
		}
	}

	public boolean initialStartTurn() {
		return (initialTurn == 0);
	}

	public String checkWinner(int symbol) {
		String winner = "";

		if (symbol == Board_utils.MARK_EMPTY) {
			winner = getString(R.string.game_draw);
		} else if (symbol == playerSymbol) {
			winner = getString(R.string.player1_win);
		} else if (gameLevel == 0 && symbol == machineSymbol) {
			winner = getString(R.string.player2_win);
		} else {
			winner = getString(R.string.machine_win);
		}

		return winner;
	}

	public boolean verifyDraw() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == Board_utils.MARK_EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	public void determineSymbol() {
		if (gameLevel > 0) {
			if (getInitialTurn() == 0) {
				setPlayerSymbol(1);
				setMachineSymbol(0);
			} else if (getInitialTurn() == 1) {
				setPlayerSymbol(0);
				setMachineSymbol(1);
			}
		} else if (gameLevel == 0) {
			setPlayerSymbol(1);
			setMachineSymbol(0);
		}
	}

	public void setGame() {
		drawGame = false;
		textXScore.setText(textXScore.getText().toString().split(":")[0] + ": "
				+ x_score);
		textOScore.setText(textOScore.getText().toString().split(":")[0] + ": "
				+ o_score);
		cleanBoard();
		setPlayer_turn(initialStartTurn());
		determineSymbol();
		startBoard();
		selectLevel();
		checkPlayer();
	}

	public void animaWinner() {
		Animation myFadeInLoopAnimation = AnimationUtils.loadAnimation(this,
				R.anim.fadeinloop);
		if (board[0][0] != Board_utils.MARK_EMPTY && board[0][0] == board[1][0]
				&& board[0][0] == board[2][0]) {
			((ImageView) findViewById(R.id.item00))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item10))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item20))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[0][1] != Board_utils.MARK_EMPTY && board[0][1] == board[1][1]
				&& board[0][1] == board[2][1]) {
			((ImageView) findViewById(R.id.item01))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item11))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item21))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[0][2] != Board_utils.MARK_EMPTY && board[0][2] == board[1][2]
				&& board[0][2] == board[2][2]) {
			((ImageView) findViewById(R.id.item02))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item12))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item22))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[0][0] != Board_utils.MARK_EMPTY && board[0][0] == board[0][1]
				&& board[0][0] == board[0][2]) {
			((ImageView) findViewById(R.id.item00))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item01))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item02))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[1][0] != Board_utils.MARK_EMPTY && board[1][0] == board[1][1]
				&& board[1][0] == board[1][2]) {
			((ImageView) findViewById(R.id.item10))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item11))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item12))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[2][0] != Board_utils.MARK_EMPTY && board[2][0] == board[2][1]
				&& board[2][0] == board[2][2]) {
			((ImageView) findViewById(R.id.item20))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item21))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item22))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[0][0] != Board_utils.MARK_EMPTY && board[0][0] == board[1][1]
				&& board[0][0] == board[2][2]) {
			((ImageView) findViewById(R.id.item00))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item11))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item22))
					.startAnimation(myFadeInLoopAnimation);
		}
		if (board[2][0] != Board_utils.MARK_EMPTY && board[2][0] == board[1][1]
				&& board[2][0] == board[0][2]) {
			((ImageView) findViewById(R.id.item20))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item11))
					.startAnimation(myFadeInLoopAnimation);
			((ImageView) findViewById(R.id.item02))
					.startAnimation(myFadeInLoopAnimation);
		}
	}

	// Apaga todas marcações do quadro
	private void cleanBoard() {
		Drawable res = getResources().getDrawable(
				getResources().getIdentifier("@drawable/empty", null,
						getPackageName()));
		((ImageView) findViewById(R.id.item00)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item01)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item02)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item10)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item11)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item12)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item20)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item21)).setImageDrawable(res);
		((ImageView) findViewById(R.id.item22)).setImageDrawable(res);
	}

	public int getInitialTurn() {
		return initialTurn;
	}

	public void setInitialTurn(int initialTurn) {
		this.initialTurn = initialTurn;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public void setTurnSymbol(int i, int j, int value) {
		board[i][j] = value;
		switchTurn();
	}

	public boolean verifyBoard(int i1, int j1, int i2, int j2) {
		return board[i1][j1] == board[i2][j2];
	}

	public int getPlayerSymbol() {
		return playerSymbol;
	}

	public void setPlayerSymbol(int playerSymbol) {
		this.playerSymbol = playerSymbol;
	}

	public int getMachineSymbol() {
		return machineSymbol;
	}

	public void setMachineSymbol(int machineSymbol) {
		this.machineSymbol = machineSymbol;
	}

}
