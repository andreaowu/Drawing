package edu.berkeley.cs160.ericxiao.prog2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	   CustomView mCustomView;
	   OnTouchListener touchListener;
	   
	   int color = Color.BLACK;

	   LinearLayout layout;
	   
	   private Paint paint = new Paint();
	   private Path path = new Path();
		
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
	    

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
	   return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case R.id.red:
	    	  Toast.makeText(this, "You have chosen " + getResources().getString(R.string.red) + " .",
	    			  		Toast.LENGTH_SHORT).show();
	    	  paint.setColor(Color.RED); 
	    	  color = Color.RED;
	    	  return true;
	      case R.id.black:
	    	  Toast.makeText(this, "You have chosen " + getResources().getString(R.string.black) + " .",
	    			  		Toast.LENGTH_SHORT).show();
	    	  paint.setColor(Color.BLACK);
	    	  color = Color.BLACK;         
	    	  return true;
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}
	
}
