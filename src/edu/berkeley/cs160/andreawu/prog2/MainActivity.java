package edu.berkeley.cs160.andreawu.prog2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	// View for the class
	DrawArea mCustomView;
	// A listener that reacts to touches
	OnTouchListener touchListener;

	int color = Color.BLACK;
	float width = 3f;
	int counter = 0;

	private Button nextButton;
	
	Canvas currentCanvas;
	DrawArea da;
	LinearLayout layout;

	Menu menu;
	
	ArrayList<DrawArea> allDrawings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		layout = (LinearLayout) findViewById(R.id.ll);
		da = new DrawArea(this);
		layout.addView(da);
		
		counter = 3;
		addListenerOnButton();
		
		allDrawings = new ArrayList<DrawArea>();
	}

	public class DrawArea extends View {

		private List<Stroke> _allStrokes; // all strokes that need to be drawn
		private SparseArray<Stroke> _activeStrokes; // use to retrieve the currently drawn strokes

		public DrawArea(Context context) {
			super(context);
			_allStrokes = new ArrayList<Stroke>();
			_activeStrokes = new SparseArray<Stroke>();
		}

		public void onDraw(Canvas canvas) {
			if (_allStrokes != null) {
				for (Stroke stroke : _allStrokes) {
					if (stroke != null) {
						Path path = stroke.getPath();
						Paint painter = stroke.getPaint();
						if ((path != null) && (painter != null)) {
							canvas.drawPath(path, painter);
						}
						invalidate();
					}
				}
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			final int action = event.getActionMasked();
			final int pointerCount = event.getPointerCount();

			switch (action) {
			case MotionEvent.ACTION_DOWN: {
				pointDown((int) event.getX(), (int) event.getY(), event.getPointerId(0));
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				for (int pc = 0; pc < pointerCount; pc++) {
					pointMove((int) event.getX(pc), (int) event.getY(pc), event.getPointerId(pc));
				}
				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN: {
				for (int pc = 0; pc < pointerCount; pc++) {
					pointDown((int) event.getX(pc), (int) event.getY(pc), event.getPointerId(pc));
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				break;
			}
			case MotionEvent.ACTION_POINTER_UP: {
				break;
			}
			}
			invalidate();
			return true;
		}

		private void pointDown(int x, int y, int id) {
			// create a paint with color and width
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(color);
			paint.setStrokeWidth(width);

			// create the Stroke
			Point pt = new Point(x, y);
			Stroke stroke = new Stroke(paint);
			stroke.addPoint(pt);
			_activeStrokes.put(id, stroke);
			_allStrokes.add(stroke);
		}

		private void pointMove(int x, int y, int id) {
			// retrieve the stroke and add new point to its path
			Stroke stroke = _activeStrokes.get(id);
			if (stroke != null) {
				Point pt = new Point(x, y);
				stroke.addPoint(pt);
			}
		}
	}
	
	public void addListenerOnButton() {
		nextButton = (Button) findViewById(R.id.button1);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				allDrawings.add(da);
				System.out.println("allDrawings size: " + allDrawings.size());
				layout.removeView(da);
				System.out.println("allDrawings size after: " + allDrawings.size());
				da = new DrawArea(MainActivity.this);
				layout.addView(da);

				if (((Button) findViewById(R.id.button1)).getText().equals(
						"Reset")) {
					// reset variables
					((Button) findViewById(R.id.button1)).setText("Next");
					counter = 4;
					for (int i = 0; i < allDrawings.size(); i++) {
						layout.removeView(allDrawings.get(i));
					}
					allDrawings = new ArrayList<DrawArea>();
					menu.findItem(R.id.black).setChecked(true);
					menu.findItem(R.id.small).setChecked(true);
					color = Color.BLACK;
					width = 3f;
				}
				counter -= 1;
				if (counter == 1) {
					((Button) findViewById(R.id.button1)).setText("Finish");
				} else if (counter == 0) {
					displayFinish();
				}
			}
		});
	}
	
	public void displayFinish() {
		DisplayMetrics metrics = getBaseContext().getResources().getDisplayMetrics();
		int w = metrics.widthPixels;
		int h = metrics.heightPixels;
		w = w / 2;
		h = h / 2;
		layout.removeAllViews();
		layout = (LinearLayout) findViewById(R.id.ll);
		nextButton.setText("Reset");
		layout.addView(nextButton);
		
		LinearLayout.LayoutParams layoutParams0 = (LinearLayout.LayoutParams) allDrawings.get(0).getLayoutParams();
		layoutParams0.leftMargin = -300;
		layoutParams0.topMargin = -300;
		allDrawings.get(0).setLayoutParams(layoutParams0);
		allDrawings.get(0).invalidate();

		LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) allDrawings.get(1).getLayoutParams();
		layoutParams1.leftMargin = -550;
		layoutParams1.topMargin = -300;
		allDrawings.get(1).setLayoutParams(layoutParams1);
		allDrawings.get(1).invalidate();
		
		LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) allDrawings.get(2).getLayoutParams();
		layoutParams2.leftMargin = -975;
		layoutParams2.topMargin = 200;
		allDrawings.get(2).setLayoutParams(layoutParams2);
		allDrawings.get(2).invalidate();
		
		for (int i = 0; i < allDrawings.size(); i++) {
			allDrawings.get(i).setScaleX(0.5f);
			allDrawings.get(i).setScaleY(0.5f);
			layout.addView(allDrawings.get(i));
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		this.menu = menu;
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		menu.findItem(item.getItemId()).setChecked(true);
		switch (item.getItemId()) {
		case R.id.red:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.red) + ".", 
					Toast.LENGTH_SHORT).show();
			color = Color.RED;
			return true;
		case R.id.orange:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.orange) + ".",
					Toast.LENGTH_SHORT).show();
			color = Color.rgb(255, 127, 0);
			return true;
		case R.id.yellow:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.yellow) + ".",
					Toast.LENGTH_SHORT).show();
			color = Color.YELLOW;
			return true;
		case R.id.green:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.green) + ".",
					Toast.LENGTH_SHORT).show();
			color = Color.GREEN;
			return true;
		case R.id.blue:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.blue) + ".",
					Toast.LENGTH_SHORT).show();
			color = Color.BLUE;
			return true;
		case R.id.purple:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.purple) + ".",
					Toast.LENGTH_SHORT).show();
			color = Color.rgb(148, 0, 211);
			return true;
		case R.id.black:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.black) + ".",
					Toast.LENGTH_SHORT).show();
			color = Color.BLACK;
			return true;
		case R.id.small:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.small) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			width = 3f;
			return true;
		case R.id.medium:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.medium) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			width = 8f;
			return true;
		case R.id.large:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.large) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			width = 15f;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}