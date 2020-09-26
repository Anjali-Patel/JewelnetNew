package com.gss.jbc;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;


public class VerticalViewPager extends ViewPager {

   GestureDetector jnGestureDetector;

   public boolean showingLastPage = false;
   public int actionDown = 0, actionUp = 0;

   Context context;
   VideoView videoView;
   boolean swipedUp = false;
   boolean swipedDown = false;

   public VerticalViewPager(Context context) {



       super(context);
       init();
       this.context = context;

   }

   public VerticalViewPager(Context context, AttributeSet attrs) {
       super(context, attrs);
       init();
       this.context = context;
   }

   private void init() {
       // The majority of the magic happens here
       setPageTransformer(true, new VerticalPageTransformer());
       // The easiest way to get rid of the overscroll drawing that happens on the left and right
       setOverScrollMode(OVER_SCROLL_NEVER);

       jnGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {



       });



   }

   private class VerticalPageTransformer implements ViewPager.PageTransformer {
       private static final float MIN_SCALE = 0.75f;
       @Override
       public void transformPage(View view, float position) {


           if (position < -1) { // [-Infinity,-1)
               // This page is way off-screen to the left.
               view.setAlpha(0);

           }  else if (position <= 0) { // [-1,0]
               // Use the default slide transition when moving to the left page
               view.setAlpha(1);
               // Counteract the default slide transition
               view.setTranslationX(view.getWidth() * -position);

               //set Y position to swipe in from top
               float yPosition = position * view.getHeight();
               view.setTranslationY(yPosition);
               view.setScaleX(1);
               view.setScaleY(1);

           } else if (position <= 1) { // [0,1]
               view.setAlpha(1);

               // Counteract the default slide transition
               view.setTranslationX(view.getWidth() * -position);


               // Scale the page down (between MIN_SCALE and 1)
               float scaleFactor = MIN_SCALE
                       + (1 - MIN_SCALE) * (1 - Math.abs(position));
               view.setScaleX(scaleFactor);
               view.setScaleY(scaleFactor);

           }
           else { // (1,+Infinity]
               // This page is way off-screen to the right.
               view.setAlpha(0);
           }
       }
   }

   /**
    * Swaps the X and Y coordinates of your touch event.
    */
   private MotionEvent swapXY(MotionEvent ev) {
       float width = getWidth();
       float height = getHeight();

       float newX = (ev.getY() / height) * width;
       float newY = (ev.getX() / width) * height;

       ev.setLocation(newX, newY);

       return ev;
   }

   @Override
   public boolean onInterceptTouchEvent(MotionEvent ev){
       swipedUp = false;
       boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
       swapXY(ev); // return touch coordinates to original reference frame for any child views
       return intercepted;
   }

   @Override
   public boolean onTouchEvent(MotionEvent ev) {

       switch(ev.getAction()){
           case MotionEvent.ACTION_UP:
               actionUp = 1;
               Log.d("VVP" , "action up");
               break;

           case MotionEvent.ACTION_DOWN:
               actionDown = 1;
               Log.d("VVP" , "action down");
               break;

           default:
               Log.d("VVP" , "default");
               break;
       }
       return super.onTouchEvent(swapXY(ev));
   }

   public boolean isSwipedUp(){
       return swipedUp;
   }

   public void setSwipedUp (boolean swipedUp){
       this.swipedUp = swipedUp;
   }

   public boolean isSwipedDown(){
       return swipedDown;
   }

   public void setSwipedDown (boolean swipedDown){
       this.swipedDown = swipedDown;
   }

   public int getActionDown() {
       return actionDown;
   }

   public int getActionUp() {
       return actionUp;
   }

   public void resetActions (){
       this.actionDown = 0;
       this.actionUp = 0;
   }

}
