package com.example.autoplay_video_player;


/**
 * PlayStrategy used with masterExoPlayerHelper used for playing video inside recyclerview, which determine when to play item,
 * Value should be between 0 to 1, default is 0.75 meana when item is visible 75% then it will start play.
 */
public class PlayStrategy {
    public static final float DEFAULT = 0.75f;
}