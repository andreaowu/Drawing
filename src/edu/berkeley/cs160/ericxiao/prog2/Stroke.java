package edu.berkeley.cs160.ericxiao.prog2;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

public class Stroke {
    private Path _path;
    private Paint _paint;

    public Stroke (Paint paint) {
        _paint = paint;
    }

    public Path getPath() {
        return _path;
    }

    public Paint getPaint() {
        return _paint;
    }

	public void addPoint(Point pt) {
        if (_path == null) {
            _path = new Path();
            _path.moveTo(pt.x, pt.y);
        } else {
            _path.lineTo(pt.x, pt.y);
        }
    }
}
