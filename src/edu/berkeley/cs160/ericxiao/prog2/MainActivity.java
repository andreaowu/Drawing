package edu.berkeley.cs160.ericxiao.prog2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	// View for the class
	CustomView mCustomView;
	// A listener that reacts to touches
	OnTouchListener touchListener;

	int color = Color.BLACK;
	float width = 1f;

	Canvas currentCanvas;
	
	LinearLayout layout;

	private Paint paint = new Paint();
	private ColoredPath path = new ColoredPath();

	Menu menu;
	ArrayList<ArrayList<Path>> savedDrawings;

	// Saved current path
	ArrayList<ColoredPath> currentPaths;
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
			currentCanvas = canvas;
		}

	}

	public class ColoredPath extends Path {
		
		private int pathColor;
		private float pathWidth;
		
		public ColoredPath() {
		}
		
		public int getPathColor() {
			return pathColor;
		}
		
		public void setPathColor(int pathColor) {
			this.pathColor = pathColor;
		}
		
		public float getPathWidth() {
			return pathWidth;
		}
		
		public void setPathWidth(float pathWidth) {
			this.pathWidth = pathWidth;
		}
		
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		savedDrawings = new ArrayList<ArrayList<Path>>();

		currentPaths = new ArrayList<ColoredPath>();
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
//				savedDrawings.add(currentPaths);
		        
		        Bitmap bitmap = Bitmap.createBitmap(mCustomView.getWidth(), mCustomView.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				mCustomView.draw(canvas);
		        
		        try {           
		        	FileOutputStream fos = new FileOutputStream("image" + counter + ".jpg");
		            // Use the compress method on the BitMap object to write image to the OutputStream
		            bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
		            fos.close();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				
				path.setPathColor(color);
				path.setPathWidth(width);
				currentPaths.add(path);
				System.out.println("size: " + currentPaths.size());
				colorsUsed.add(color);
				widthsUsed.add(width);
				if (((Button) findViewById(R.id.button1)).getText().equals(
						"Reset")) {
					// reset variables
					((Button) findViewById(R.id.button1)).setText("Next");
					counter = 4;
				}
				mCustomView.invalidate();
				path = new ColoredPath();
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
		
		try {
			File f = new File("image" + counter + ".jpg");
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			Canvas c = new Canvas(b);
			c.drawBitmap(b, 0, 0, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
//		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
//		Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE
//														// bitmap
//		Canvas canvas = new Canvas(bmp);
//		System.out.println("hi");
//		Matrix scaleMatrix0 = new Matrix();
//		scaleMatrix0.setScale(0.50f, 0.50f, 0, 0);
//
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setColor(currentPaths.get(0).getPathColor());
//		paint.setStrokeWidth(currentPaths.get(0).getPathWidth());
//		currentPaths.get(0).transform(scaleMatrix0);
//		path = currentPaths.get(0);
//		mCustomView.invalidate();
//
//		Matrix scaleMatrix1 = new Matrix();
//		scaleMatrix1.setScale(0.50f, 0.50f, 1.75f * w, 0);
//		paint.setAntiAlias(true);
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setColor(currentPaths.get(1).getPathColor());
//		paint.setStrokeWidth(currentPaths.get(1).getPathWidth());
//		currentPaths.get(1).transform(scaleMatrix1);
//		path.addPath(currentPaths.get(1));
//		mCustomView.invalidate();
//
//		Matrix scaleMatrix2 = new Matrix();
//		scaleMatrix2.setScale(0.50f, 0.50f, 0, 1.75f * h);
		
//		allPaths.get(2).transform(scaleMatrix2);
//		paint.setColor(colorsUsed.get(2));
//		paint.setStrokeWidth(widthsUsed.get(2));
//		path.addPath(allPaths.get(2));
//		mCustomView.invalidate();

//		 ArrayList<Path> first = savedDrawings.get(0);
//		 ArrayList<Path> second = savedDrawings.get(1);
//		 ArrayList<Path> third = savedDrawings.get(2);
//		
//		 int max = Math.max(Math.max(first.size(), second.size()), third.size());
//		 System.out.println("Max: " + max);
//		 System.out.println("first size: " + first.size());
//		 System.out.println("second size: " + second.size());
//		 System.out.println("third size: " + third.size());
//		 for (int i = 0; i < max; i++) {
//			if (max - 1 < first.size()) {
//				first.get(i).transform(scaleMatrix0);
//				path.addPath(first.get(i));
//				mCustomView.invalidate();
//			}
//			if (max - 1 < second.size()) {
//				second.get(i).transform(scaleMatrix1);
//				path.addPath(second.get(i));
//				mCustomView.invalidate();
//			}
//			if (max - 1 < third.size()) {
//				third.get(i).transform(scaleMatrix2);
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
			paint.setColor(Color.RED);
			color = Color.RED;
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
			paint.setColor(Color.YELLOW);
			color = Color.YELLOW;
			return true;
		case R.id.green:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.green) + " .",
					Toast.LENGTH_SHORT).show();
			paint.setColor(Color.GREEN);
			color = Color.GREEN;
			return true;
		case R.id.blue:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.blue) + " .",
					Toast.LENGTH_SHORT).show();
			paint.setColor(Color.BLUE);
			color = Color.BLUE;
			return true;
		case R.id.purple:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.purple) + " .",
					Toast.LENGTH_SHORT).show();
			paint.setColor(Color.rgb(148, 0, 211));
			color = Color.rgb(148, 0, 211);
			return true;
		case R.id.black:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.black) + " .",
					Toast.LENGTH_SHORT).show();
			paint.setColor(Color.BLACK);
			color = Color.BLACK;
			return true;
		case R.id.small:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.small) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			paint.setStrokeWidth(1f);
			width = 1f;
			return true;
		case R.id.medium:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.medium) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			paint.setStrokeWidth(5f);
			width = 1f;
			return true;
		case R.id.large:
			Toast.makeText(this, "You have chosen " + getResources().getString(R.string.large) + " stroke width.",
					Toast.LENGTH_SHORT).show();
			paint.setStrokeWidth(10f);
			width = 1f;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void addPath(int color, float width) {
		if (color > 0) {
			paint.setColor(color);
			this.color = color;
		} else {
			paint.setStrokeWidth(width);
			this.width = width;
		}
		
		
		
		
//		Path old = path;
////		currentPaths.add(old);
//		path = new Path();
//		path.addPath(old);
	}
	
}
