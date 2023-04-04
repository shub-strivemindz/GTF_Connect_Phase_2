package com.example.flyingreactionanim;


import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;


public class ZeroGravityAnimation {

        private static final int RANDOM_DURATION = -1;



        private Directions mOriginationDirection = Directions.RANDOM;
        private Directions mDestinationDirection = Directions.RANDOM;
        private int mDuration = RANDOM_DURATION;
        private int mCount = 1;
        private int mImageResId;

        private String emojiResID;

        private Bitmap bitmap;
        private float mScalingFactor = 1f;
        private Animation.AnimationListener mAnimationListener;


        /**
         * Sets the orignal direction. The animation will originate from the given direction.

         */
        public ZeroGravityAnimation setOriginationDirection(Directions direction) {
            this.mOriginationDirection = direction;
            return this;
        }

        /**
         * Sets the animation destination direction. The translate animation will proceed towards the given direction.
         * @param direction
         * @return
         */
        public ZeroGravityAnimation setDestinationDirection(Directions direction) {
            this.mDestinationDirection = direction;
            return this;
        }

        /**
         * Will take a random time duriation for the animation
         * @return
         */
        public ZeroGravityAnimation setRandomDuration() {
            return setDuration(RANDOM_DURATION);
        }

        /**
         * Sets the time duration in millseconds for animation to proceed.
         * @param duration
         * @return
         */
        public ZeroGravityAnimation setDuration(int duration) {
            this.mDuration = duration;
            return this;
        }

        /**
         * Sets the image reference id for drawing the image
         * @param resId
         * @return
         */
        public ZeroGravityAnimation setImage(Bitmap resId) {
            this.bitmap = resId;
            return this;
        }




/*
    */
/**
     * Sets the String emoji reference id for drawing the image
     * @param emoji
     * @return
     *//*

        public ZeroGravityAnimation setImage(Bitmap emoji) {
            this.bitmap = emoji;
            return this;
        }
*/




    /**
         * Sets the image scaling value.
         * @param scale
         * @return
         */
        public ZeroGravityAnimation setScalingFactor(float scale) {
            this.mScalingFactor = scale;
            return this;
        }

        public ZeroGravityAnimation setAnimationListener(Animation.AnimationListener listener) {
            this.mAnimationListener = listener;
            return this;
        }

        public ZeroGravityAnimation setCount(int count) {
            this.mCount = count;
            return this;
        }


        /**
         * Starts the Zero gravity animation by creating an OTT and attach it to th given ViewGroup
         * @param activity
         * @param ottParent
         */
        public void play(Activity activity, ViewGroup ottParent) {

            DirectionGenerator generator = new DirectionGenerator();

            if(mCount > 0) {

                for (int i = 0; i < mCount; i++) {


                    final int iDupe = i;

                    Directions origin = mOriginationDirection == Directions.RANDOM ? generator.getRandomDirection() : mOriginationDirection;
                    Directions destination = mDestinationDirection == Directions.RANDOM ? generator.getRandomDirection(origin) : mDestinationDirection;

                    int startingPoints[] = generator.getPointsInDirection(activity, origin);
                    int endPoints[] = generator.getPointsInDirection(activity,destination);


                    //Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), mImageResId);

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * mScalingFactor), (int) (bitmap.getHeight() * mScalingFactor), false);

                    switch (origin) {
                        case LEFT:
                            startingPoints[0] -= scaledBitmap.getWidth();
                            break;

                        case RIGHT:
                            startingPoints[0] += scaledBitmap.getWidth();
                            break;

                        case TOP:
                            startingPoints[1] -= scaledBitmap.getHeight();
                            break;
                        case BOTTOM:
                            startingPoints[1] += scaledBitmap.getHeight();
                            break;
                    }

                    switch (destination) {
                        case LEFT:
                            endPoints[0] -= scaledBitmap.getWidth();
                            break;

                        case RIGHT:
                            endPoints[0] += scaledBitmap.getWidth();
                            break;

                        case TOP:
                            endPoints[1] -= scaledBitmap.getHeight();
                            break;
                        case BOTTOM:
                            endPoints[1] += scaledBitmap.getHeight();
                            break;
                    }


                    final OverTheTopLayer layer = new OverTheTopLayer();

                    FrameLayout ottLayout = layer.with(activity)
                            .scale(mScalingFactor)
                            .attachTo(ottParent)
                            .setBitmap(scaledBitmap, startingPoints)
                            .create();


                    switch (origin) {
                        case LEFT:

                    }

                    int deltaX = endPoints[0]  - startingPoints[0];
                    int deltaY = endPoints[1] - startingPoints[1];

                    int duration = mDuration;
                    if (duration == RANDOM_DURATION) {
                        duration = RandomUtil.generateRandomBetween(3500, 8500);
                    }

                    TranslateAnimation animation = new TranslateAnimation(0, deltaX, 0, deltaY);
                    animation.setDuration(duration);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                            if (iDupe == 0) {
                                if (mAnimationListener != null) {
                                    mAnimationListener.onAnimationStart(animation);
                                }
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            layer.destroy();

                            if (iDupe == (mCount - 1)) {
                                if (mAnimationListener != null) {
                                    mAnimationListener.onAnimationEnd(animation);
                                }
                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layer.applyAnimation(animation);
                }
            }
            else  {

                Log.e(ZeroGravityAnimation.class.getSimpleName(),"Count was not provided, animation was not started");
            }
        }

        /**
         * Takes the content view as view parent for laying the animation objects and starts the animation.
         * @param activity - activity on which the zero gravity animation should take place.
         */
        public void play(Activity activity) {

            play(activity,null);

        }
    }