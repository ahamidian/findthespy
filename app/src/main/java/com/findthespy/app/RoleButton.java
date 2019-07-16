package com.findthespy.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class RoleButton extends RelativeLayout {

    private ImageView slidingButton;
    private float initialY = 0;
    private boolean active;
    private int initialButtonHeight;
    private TextView centerText;

    private Drawable disabledDrawable;
    private Drawable enabledDrawable;

    public RoleButton(Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public RoleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, -1, -1);
    }

    public RoleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(21)
    public RoleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        RelativeLayout background = new RelativeLayout(context);

        LayoutParams layoutParamsView = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsView.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        background.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded));
        addView(background, layoutParamsView);

        final TextView centerText = new TextView(context);
        this.centerText = centerText;

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        centerText.setText("SWIPE"); //add any text you need
        centerText.setTextColor(Color.WHITE);
        centerText.setPadding(35, 35, 35, 35);
        background.addView(centerText, layoutParams);


        final ImageView swipeButton = new ImageView(context);
        this.slidingButton = swipeButton;

        disabledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background);
        enabledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_foreground);

        slidingButton.setImageDrawable(disabledDrawable);
        slidingButton.setPadding(40, 40, 40, 40);

        LayoutParams layoutParamsButton = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsButton.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        swipeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button));
        swipeButton.setImageDrawable(disabledDrawable);
        addView(swipeButton, layoutParamsButton);

        setOnTouchListener(getButtonTouchListener());
    }

    private OnTouchListener getButtonTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (initialY == 0) {
                            initialY = slidingButton.getY();
                        }
                        if (event.getY() > initialY + slidingButton.getHeight() / 2 &&
                                event.getY() + slidingButton.getHeight() / 2 < getHeight()) {
                            slidingButton.setY(event.getY() - slidingButton.getHeight() / 2);
                            centerText.setAlpha(1 - 1.3f * (slidingButton.getY() + slidingButton.getHeight()) / getHeight());
                        }

                        if (event.getY() + slidingButton.getHeight() / 2 > getHeight() &&
                                slidingButton.getY() + slidingButton.getHeight() / 2 < getHeight()) {
                            slidingButton.setY(getHeight() - slidingButton.getHeight());
                        }

                        if (event.getY() < slidingButton.getHeight() / 2 &&
                                slidingButton.getY() > 0) {
                            slidingButton.setY(0);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (active) {
                            collapseButton();
                        } else {
                            initialButtonHeight = slidingButton.getHeight();

                            if (slidingButton.getY() + slidingButton.getHeight() > getHeight() * 0.85) {
                                expandButton();
                            } else {
                                moveButtonBack();
                            }
                        }

                        return true;
                }

                return false;
            }
        };
    }

    private void expandButton() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getY(), 0);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setY(y);
            }
        });


        final ValueAnimator heightAnimator = ValueAnimator.ofInt(
                slidingButton.getHeight(),
                getHeight());

        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = slidingButton.getLayoutParams();
                params.height = (Integer) heightAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                active = true;
                slidingButton.setImageDrawable(enabledDrawable);
            }
        });

        animatorSet.playTogether(positionAnimator, heightAnimator);
        animatorSet.start();
    }

    private void collapseButton() {
        final ValueAnimator heightAnimator = ValueAnimator.ofInt(
                slidingButton.getHeight(),
                initialButtonHeight);

        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params =  slidingButton.getLayoutParams();
                params.height = (Integer) heightAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });

        heightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                active = false;
                slidingButton.setImageDrawable(disabledDrawable);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(objectAnimator, heightAnimator);
        animatorSet.start();
    }


    private void moveButtonBack() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getY(), 0);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setY(y);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        positionAnimator.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, positionAnimator);
        animatorSet.start();
    }

}
