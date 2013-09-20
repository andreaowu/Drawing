package edu.berkeley.cs160.ericxiao.prog2;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
	CustomView mCustomView;
	// A listener that reacts to touches
	OnTouchListener touchListener;

	int color = Color.BLACK;
	float width = 1f;

	LinearLayout layout;

	private Paint paint = new Paint();
	private Path path = new Path();

	Menu menu;
	// ArrayList<ArrayList<Path>> savedDrawings;

	// Saved current path
	ArrayList<Path> allPaths;
	// List of colors for paths in allPaths
	ArrayList<Integer> colorsUsed;
	// List of stroke widths for paths in allPaths
	ArrayList<Float> widthsUsed;

	private Button nextButton;

	int counter;

	public class CustomView extends View {

		public CustomView(Context context) {
			super(context);
			paint.setAntiAlias(true);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// savedDrawings = new ArrayList<ArrayList<Path>>();

		allPaths = new ArrayList<Path>();
		colorsUsed = new ArrayList<Integer>();
		widthsUsed = new ArrayList<Float>();
		counter = 3;
		addListenerOnButton();

		layout = (LinearLayout) findViewById(R.id.ll);
		mCustomView = new CustomView(this);

		layout.addView(mCustomView);

		touchListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				float eventX = event.getX();
				float eventY = event.getY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					break;
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					path.lineTo(eventX, eventY);
					break;
				} // ends switch statement
				mCustomView.invalidate();
				return true;
			}
		};
		mCustomView.setOnTouchListener(touchListener);
	}

	public void addListenerOnButton() {
		nextButton = (Button) findViewById(R.id.button1);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// savedDrawings.add(allPaths);
				allPaths.add(path);
				colorsUsed.add(color);
				widthsUsed.add(width);
				if (((Button) findViewById(R.id.button1)).getText().equals(
						"Reset")) {
					// reset variables
					// savedDrawings = new ArrayList<ArrayList<Path>>();
					allPaths.add(path);
					allPaths = new ArrayList<Path>();
					((Button) findViewById(R.id.button1)).setText("Next");
					counter = 4;
				}
				mCustomView.invalidate();
				path = new Path();
				counter -= 1;
				if (counter == 1) {
					((Button) findViewById(R.id.button1)).setText("Finish");
				} else if (counter == 0) {
					((Button) findViewById(R.id.button1)).setText("Reset");
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
		System.out.println("hi");
		Matrix scaleMatrix1 = new Matrix();
		scaleMatrix1.setScale(0.50f, 0.50f, 0, 0);
		allPaths.get(0).transform(scaleMatrix1);
		path.addPath(allPaths.get(0));
		mCustomView.invalidate();
		
		Matrix scaleMatrix2 = new Matrix();
		scaleMatrix2.setScale(0.50f, 0.50f, 1.75f * w, 0);
		allPaths.get(1).transform(scaleMatrix2);
		path.addPath(allPaths.get(1));
		mCustomView.invalidate();
		
		Matrix scaleMatrix3 = new Matrix();
		scaleMatrix3.setScale(0.50f, 0.50f, 0, 1.75f * h);
		allPaths.get(2).transform(scaleMatrix3);
		path.addPath(allPaths.get(2));
		mCustomView.invalidate();
		
		
		
//		for (int i = 0; i < allPaths.size(); i++) {
//			allPaths.get(i).transform(scaleMatrix1);
//			paint.setColor(colorsUsed.get(i));
//			paint.setStrokeWidth(widthsUsed.get(i));
//			path.addPath(allPaths.get(i));
//			mCustomView.invalidate();
//		}

//		ArrayList<Path> first = savedDrawings.get(0);
//		ArrayList<Path> second = savedDrawings.get(1);
//		ArrayList<Path> third = savedDrawings.get(2);
//
//		int max = Math.max(Math.max(first.size(), second.size()), third.size());
//		System.out.println("Max: " + max);
//		System.out.println("first size: " + first.size());
//		System.out.println("second size: " + second.size());
//		System.out.println("third size: " + third.size());
//		for (int i = 0; i < max; i++) {
//			if (max - 1 < first.size()) {
//				first.get(i).transform(scaleMatrix1);
//				path.addPath(first.get(i));
//				mCustomView.invalidate();
//			}
//			if (max - 1 < second.size()) {
//				second.get(i).transform(scaleMatrix2);
//				path.addPath(second.get(i));
//				mCustomView.invalidate();
//			}
//			if (max - 1 < third.size()) {
//				third.get(i).transform(scaleMatrix3);
//				path.addPath(third.get(i));
//				mCustomView.invalidate();
//			}
//		}
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
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.red)
							+ " .", Toast.LENGTH_SHORT).show();
//			addPath(Color.RED);
			return true;
		case R.id.orange:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.orange) + " .",
					Toast.LENGTH_SHORT).show();
			paint.setColor(Color.rgb(255, 127, 0));
			color = Color.rgb(255, 127, 0);
			return true;
		case R.id.yellow:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.yellow) + " .",
					Toast.LENGTH_SHORT).show();
//			addPath(Color.YELLOW);
			return true;
		case R.id.green:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.green) + " .",
					Toast.LENGTH_SHORT).show();
//			addPath(Color.GREEN);
			return true;
		case R.id.blue:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.blue) + " .",
					Toast.LENGTH_SHORT).show();
//			addPath(Color.BLUE);
			return true;
		case R.id.purple:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.purple) + " .",
					Toast.LENGTH_SHORT).show();
//			addPath(Color.rgb(148, 0, 211));
			return true;
		case R.id.black:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.black) + " .",
					Toast.LENGTH_SHORT).show();
//			addPath(Color.BLACK);
			return true;
		case R.id.small:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.small) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			addPath(-1, 1f);
			
			return true;
		case R.id.medium:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.small) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			addPath(-1, 5f);
			paint.setStrokeWidth(5f);
			width = 5f;
			return true;
		case R.id.large:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.small) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			addPath(-1, 10f);
			paint.setStrokeWidth(10f);
			width = 10f;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void addPath(int color, float width) {
		if (color > 0) {
			
		}
		paint.setColor(Color.rgb(255, 127, 0));
		color = Color.rgb(255, 127, 0);
		
		
		
		Path old = path;
//		currentPaths.add(old);
		path = new Path();
		path.addPath(old);
	}
	
}
