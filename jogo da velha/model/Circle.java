package main.model;

import main.tictactoe.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circle extends Cell {

	public Circle(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas g, int x, int y, int w, int h, Paint paint,
			Resources res, int heightVariation) {
		// TODO Auto-generated method stub
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.RED);
		Bitmap im_grid = BitmapFactory.decodeResource(res, R.drawable.ball);
		g.drawBitmap(im_grid, x * w / 3, y * h / 3 + heightVariation, paint);
		// g.drawCircle(x * w / 3 + w / 6, y * h / 3 + h / 6, h / 6, paint);
	}
}
