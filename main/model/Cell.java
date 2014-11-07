package main.model;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public abstract class Cell extends Point {

	public Cell(int x, int y) {
		super(x, y);
	}

	abstract public void draw(Canvas g, int x, int y, int w, int h, Paint paint, Resources res, int heightVariation);
}
