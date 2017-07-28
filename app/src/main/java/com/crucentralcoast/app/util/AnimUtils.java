package com.crucentralcoast.app.util;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * @author Tyler Wong
 */

public class AnimUtils {

    private static final long ANIMATION_DURATION = 150;

    private static final float GONE_SIZE = 0f;
    private static final float HALF_SIZE = 0.5f;
    private static final float FULL_SIZE = 1.0f;

    public static ScaleAnimation getGrowAnim() {
        ScaleAnimation growAnim = new ScaleAnimation(GONE_SIZE, FULL_SIZE, GONE_SIZE, FULL_SIZE,
                Animation.RELATIVE_TO_SELF, HALF_SIZE, Animation.RELATIVE_TO_SELF, HALF_SIZE);
        growAnim.setDuration(ANIMATION_DURATION);
        return growAnim;
    }

    public static ScaleAnimation getShrinkAnim() {
        ScaleAnimation shrinkAnim = new ScaleAnimation(FULL_SIZE, GONE_SIZE, FULL_SIZE, GONE_SIZE,
                Animation.RELATIVE_TO_SELF, HALF_SIZE, Animation.RELATIVE_TO_SELF, HALF_SIZE);
        shrinkAnim.setDuration(ANIMATION_DURATION);
        return shrinkAnim;
    }
}
