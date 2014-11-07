package main.model;

import main.tictactoe.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cross extends Cell {

	public Cross(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas g, int x, int y, int w, int h, Paint paint, Resources res, int heightVariation) {
		// TODO Auto-generated method stub
		/*float[] pontos = { x * w / 3, y * h / 3, x * w / 3 + w / 3,
				y * h / 3 + h / 3 };
		float[] pontos2 = { x * w / 3 + w / 3, y * h / 3, x * w / 3,
				y * h / 3 + h / 3 };*/
		Bitmap im_grid = BitmapFactory
				.decodeResource(res, R.drawable.cruz);
		g.drawBitmap(im_grid, x * w / 3, y * h / 3 + heightVariation, paint);
		paint.setColor(Color.BLUE);
		//g.drawLines(pontos, paint);
		//g.drawLines(pontos2, paint);
	}

	public boolean equals(Object obj) {
		return (obj instanceof Cross);
	}

	public String toString() {
		return "X";
	}
}
