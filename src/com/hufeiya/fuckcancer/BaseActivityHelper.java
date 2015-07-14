package com.hufeiya.fuckcancer;
/**
 * Created by hufeiya on 15-6-28.
 * To control the DrawerLayout.
 */

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;

import java.util.Random;

import static com.balysv.materialmenu.MaterialMenuDrawable.IconState;

public class BaseActivityHelper {

    private int materialButtonState;
    private MaterialMenuDrawable materialIcon;
    private DrawerLayout drawerLayout;
    private boolean direction;

    public static int generateState(int previous) {
        int generated = new Random().nextInt(4);
        return generated != previous ? generated : generateState(previous);
    }

    public static IconState intToState(int state) {
        switch (state) {
            case 0:
                return IconState.BURGER;
            case 1:
                return IconState.ARROW;
            case 2:
                return IconState.X;
            case 3:
                return IconState.CHECK;
        }
        throw new IllegalArgumentException("Must be a number [0,3)");
    }

    public void init(View parent, MaterialMenuDrawable actionBarIcon) {


        materialIcon = actionBarIcon;


        drawerLayout = ((DrawerLayout) parent.findViewById(R.id.drawer_layout));
        drawerLayout.setScrimColor(parent.getResources().getColor(android.R.color.transparent));

        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialIcon.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        direction ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(android.view.View drawerView) {
                direction = true;
            }

            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                direction = false;
            }
        });
        /*
        drawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }, 1500);
        */
    }

    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
        materialIcon.animateIconState(IconState.ARROW);
    }

    public void refreshDrawerState() {
        this.direction = drawerLayout.isDrawerOpen(GravityCompat.START);
    }
}
