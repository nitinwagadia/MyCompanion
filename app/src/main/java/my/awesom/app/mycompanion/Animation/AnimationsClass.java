package my.awesom.app.mycompanion.Animation;

/**
 * Created by nitin on 4/22/15.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;

import my.awesom.app.mycompanion.adapters.MyPastRemindersAdapter;
import my.awesom.app.mycompanion.adapters.MyScheduledRemindersAdapter;

public class AnimationsClass {
    static void animate(View v) {
        //AnimatorSet animatorSet = new AnimatorSet();
        //ObjectAnimator animatorX=ObjectAnimator.ofFloat(v,"translationX",-100,100,-50,50,0,0);
        //ObjectAnimator animatorY=ObjectAnimator.ofFloat(v,"translationY",-100,200,0,150,0,100,0,50,0,40,0,30,0,20,0,10,0).setDuration(2000).start();
        ObjectAnimator.ofFloat(v, "translationY", -100, 200, 0, 150, 0, 100, 0, 50, 0, 40, 0, 30, 0, 20, 0, 10, 0).setDuration(2000).start();
        ObjectAnimator.ofFloat(v, "translationX", -100, 200, 0, 150, 0, 100, 0, 50, 0, 40, 0, 30, 0, 20, 0, 10, 0).setDuration(2000).start();
        //ObjectAnimator.ofFloat(v,"translationY",-100,200,0,150,0,100,0,50,0,40,0,30,0,20,0,10,0).setDuration(2000).start();

        // ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0F, 0.8F, 0.4F, 0.6F, 1.0F);
        // scaleY.setDuration(1000);
        //ObjectAnimator rotate = ObjectAnimator.ofFloat(v, "rotationX", 5, 10, 15, 20, 50, 100, 50, 20, 15, 10, 5);
        //rotate.setDuration(2000);
        //animatorX.setDuration(1000);
//       animatorY.setDuration(2000);
        //animatorSet.playTogether(animatorX,animatorY);
        //animatorSet.start();

        //scaleY.start();
        //rotate.start();
        //      animatorY.start();
    }

    public static void animateInvalidInput(View view) {

        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", -50, 0, 50, -50, 0, 50, -50, 0, 50, -50, 0, 50, -50, 0, 50, -50, 0, 50, -50, 0, 50, 0);
        translateX.setDuration(1000);
        translateX.start();
    }

    public static void animateButtonClick(View v) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.8F, 0.4F, 0.8F, 1.0F);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.8F, 0.4F, 0.8F, 1.0F);
        set.setDuration(500);
        set.playTogether(scaleX, scaleY);
        set.start();


    }

    public static void animateContactBox(Button contactIntentButton) {
        ObjectAnimator error = ObjectAnimator.ofFloat(contactIntentButton, "translationX", 0, 50, -50, 0, 25, -25, 0);
        error.setDuration(500);
        error.start();

    }

    public static void animateToolbar(View v) {


        //AnimatorSet animatorSet = new AnimatorSet();
        //ObjectAnimator animatorX=ObjectAnimator.ofFloat(v,"translationX",-100,100,-50,50,0,0);
        //ObjectAnimator animatorY=ObjectAnimator.ofFloat(v,"translationY",-100,200,0,150,0,100,0,50,0,40,0,30,0,20,0,10,0).setDuration(2000).start();
        ObjectAnimator.ofFloat(v, "translationY", 800, 600, 400, 200, 100, 50, 0, 40, 0, 30, 0, 20, 0, 10, 0).setDuration(2000).start();
        //  ObjectAnimator.ofFloat(v, "translationX", -100, 200, 0, 150, 0, 100, 0, 50, 0, 40, 0, 30, 0, 20, 0, 10, 0).setDuration(2000).start();

        ObjectAnimator.ofFloat(v, "rotationX", 0, 90, 180, 270, 360).setDuration(2000).start();
        ObjectAnimator.ofFloat(v, "rotationY", 0, 90, 180, 270, 360).setDuration(2000).start();

        //ObjectAnimator.ofFloat(v,"translationY",-100,200,0,150,0,100,0,50,0,40,0,30,0,20,0,10,0).setDuration(2000).start();

        // ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0F, 0.8F, 0.4F, 0.6F, 1.0F);
        // scaleY.setDuration(1000);
        //ObjectAnimator rotate = ObjectAnimator.ofFloat(v, "rotationX", 5, 10, 15, 20, 50, 100, 50, 20, 15, 10, 5);
        //rotate.setDuration(2000);
        //animatorX.setDuration(1000);
//       animatorY.setDuration(2000);
        //animatorSet.playTogether(animatorX,animatorY);
        //animatorSet.start();

        //scaleY.start();
        //rotate.start();
        //      animatorY.start();

    }

    public static void animateList(MyScheduledRemindersAdapter.MyViewHolder v) {

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v.itemView, "translationY", -100, 100, -50, 50, 0, 0);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v.itemView, "translationX", -100, 100, -50, 50, 0, 0);
        //animatorX.setDuration(1000);
        animatorSet.setDuration(1500);
        animatorSet.playTogether(animatorX, animatorY);
        animatorX.start();
    }

    public static void animateList(MyPastRemindersAdapter.ViewHolderPastEvents v) {

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v.itemView, "translationY", -100, 100, -50, 50, 0, 0);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v.itemView, "translationX", -100, 100, -50, 50, 0, 0);
        //animatorX.setDuration(1000);
        animatorSet.setDuration(1500);
        animatorSet.playTogether(animatorX, animatorY);
        animatorX.start();
    }


}

