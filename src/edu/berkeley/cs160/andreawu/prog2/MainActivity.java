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
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
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

    // Color for the drawing
	int color = Color.BLACK;
	// True if erase mode, false if draw mode
	boolean erase = false;
    // Line width for the drawing
	float width = 3f;
    // Keeps track of when to do "Next", "Finish", or "Reset" mode
	int counter = 0;

    // Button for Next/Finish/Reset
	private Button nextButton;
    // Current view that is being drawn on
	DrawArea da;
    // Current layout
	LinearLayout layout;
    // Menu bar at the top
	Menu menu;
	// List of all drawings
	ArrayList<DrawArea> allDrawings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        // Setting up the layout
		layout = (LinearLayout) findViewById(R.id.ll);
		da = new DrawArea(this);
		layout.addView(da);
		
        // Setting up other variables
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

        @Override
		public void onDraw(Canvas canvas) {
            // Draws all non-null strokes that are in _allStrokes
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
			if (erase) {
				paint.setColor(Color.WHITE);
			} else {
				paint.setColor(color);
			}
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
                // Add current da to list and reset da
				allDrawings.add(da);
				layout.removeView(da);
				da = new DrawArea(MainActivity.this);
				layout.addView(da);

				if (((Button) findViewById(R.id.button1)).getText().equals(
						"Reset")) {
					// reset variables
					((Button) findViewById(R.id.button1)).setText("Next");
					counter = 4;
                    // clear views
					for (int i = 0; i < allDrawings.size(); i++) {
						layout.removeView(allDrawings.get(i));
					}
                    // reset drawing tools
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
        // resets layout to draw everything on it
		layout.removeAllViews();
		layout = (LinearLayout) findViewById(R.id.ll);
		nextButton.setText("Reset");
		layout.addView(nextButton);
		
        // set up layout parameters for first drawing
		LinearLayout.LayoutParams layoutParams0 = (LinearLayout.LayoutParams) allDrawings.get(0).getLayoutParams();
		layoutParams0.leftMargin = -300;
		layoutParams0.topMargin = -300;
		allDrawings.get(0).setLayoutParams(layoutParams0);
		allDrawings.get(0).invalidate();

        // set up layout parameters for second drawing
		LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) allDrawings.get(1).getLayoutParams();
		layoutParams1.leftMargin = -550;
		layoutParams1.topMargin = -300;
		allDrawings.get(1).setLayoutParams(layoutParams1);
		allDrawings.get(1).invalidate();
		
        // set up layout parameters for third drawing
		LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) allDrawings.get(2).getLayoutParams();
		layoutParams2.leftMargin = -975;
		layoutParams2.topMargin = 200;
		allDrawings.get(2).setLayoutParams(layoutParams2);
		allDrawings.get(2).invalidate();
		
        // scale all and add all layouts
		for (int i = 0; i < allDrawings.size(); i++) {
			allDrawings.get(i).setScaleX(0.5f);
			allDrawings.get(i).setScaleY(0.5f);
			layout.addView(allDrawings.get(i));
		}
	}
	
    // puts all menu items onto the menu
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		this.menu = menu;
		return true;
	}
	
    // changes the color and stroke width with accordance to what is chosen in the drop down menus
	public boolean onOptionsItemSelected(MenuItem item) {
		menu.findItem(item.getItemId()).setChecked(true);
		switch (item.getItemId()) {
		case R.id.draw:
			Toast.makeText(this, "You have chosen to " + getResources().getString(R.string.draw) + ".",
					Toast.LENGTH_SHORT).show();
			erase = false;
			return true;
		case R.id.erase:
			Toast.makeText(this, "You have chosen to " + getResources().getString(R.string.erase) + ".",
					Toast.LENGTH_SHORT).show();
			erase = true;
			return true;
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
