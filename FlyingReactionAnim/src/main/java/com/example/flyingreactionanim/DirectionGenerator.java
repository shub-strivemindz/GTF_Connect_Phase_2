package com.example.flyingreactionanim;

import android.app.Activity;

import java.util.Random;

public class DirectionGenerator {
    /**
     * Gets the random pixel points in the given directions of the screen
     * @param activity - activity from where you are referring the random value.
     * @param directions - on among LEFT,RIGHT,TOP,BOTTOM,RANDOM
     * @return a pixel point {x,y} in the given directions.
     */
    public int[] getPointsInDirection(Activity activity, Directions directions) {

        switch (directions) {

            case LEFT:
                return getRandomLeft(activity);
            case RIGHT:
                return getRandomRight(activity);
            case BOTTOM:
                return getRandomBottom(activity);
            case TOP:
                return getRandomTop(activity);

            default:
                Directions[] allDirections = new Directions[]{Directions.LEFT,Directions.TOP,Directions.BOTTOM,Directions.RIGHT};
                int index = new Random().nextInt(allDirections.length);
                return getPointsInDirection(activity, allDirections[index]);

        }

    }

    /**
     * Gets the random pixel points in the left directions of the screen. The value will be of {0,y} where y will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomLeft(Activity activity) {

        int x = 0;

        int height = activity.getResources().getDisplayMetrics().heightPixels;

        Random random = new Random();
        int y = random.nextInt(height);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the top directions of the screen. The value will be of {x,0} where x will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomTop(Activity activity) {

        int y = 0;

        int width = activity.getResources().getDisplayMetrics().widthPixels;

        Random random = new Random();
        int x = random.nextInt(width);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the right directions of the screen. The value will be of {screen_width,y} where y will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomRight(Activity activity) {


        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        int x = width ;

        Random random = new Random();
        int y = random.nextInt(height);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the bottom directions of the screen. The value will be of {x,screen_height} where x will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomBottom(Activity activity) {


        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;


        int y = height ;
        Random random = new Random();
        int x = random.nextInt(width);

        return new int[]{x, y};
    }

    /**
     * Gets a random directions.
     * @return one among LEFT,RIGHT,BOTTOM,TOP
     */
    public Directions getRandomDirection() {
        Directions[] allDirections = new Directions[]{Directions.LEFT,Directions.TOP,Directions.BOTTOM,Directions.RIGHT};
        int index = new Random().nextInt(allDirections.length);
        return (allDirections[index]);
    }

    /**
     * Gets a random directions skipping the given directions.
     * @param toSkip a directions which should not be returned by this method.
     * @return one among LEFT,RIGHT,BOTTOM if TOP is provided as directions to skip,
     * one among TOP,RIGHT,BOTTOM if LEFT is provided as directions to skip
     * and so on.
     */
    public Directions getRandomDirection(Directions toSkip) {
        Directions[] allExceptionalDirections;
        switch (toSkip) {

            case LEFT:
                allExceptionalDirections = new Directions[]{Directions.TOP,Directions.BOTTOM,Directions.RIGHT};
                break;
            case RIGHT:
                allExceptionalDirections = new Directions[]{Directions.TOP,Directions.BOTTOM,Directions.LEFT};
                break;
            case BOTTOM:
                allExceptionalDirections = new Directions[]{Directions.TOP,Directions.LEFT,Directions.RIGHT};
                break;
            case TOP:
                allExceptionalDirections = new Directions[]{Directions.LEFT,Directions.BOTTOM,Directions.RIGHT};
                break;

            default:
                allExceptionalDirections = new Directions[]{Directions.LEFT,Directions.TOP,Directions.BOTTOM,Directions.RIGHT};


        }

        int index = new Random().nextInt(allExceptionalDirections.length);
        return (allExceptionalDirections[index]);
    }
}