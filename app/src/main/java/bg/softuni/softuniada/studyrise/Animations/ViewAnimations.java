package bg.softuni.softuniada.studyrise.Animations;

import android.view.View;

public class ViewAnimations {

    public static void fadeAnimateZoom(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);
        view.setScaleX(0.f);
        view.setScaleY(0.f);
        view.animate()
                .alpha(1.f)
                .scaleX(1.f).scaleY(1.f)
                .setDuration(500)
                .start();
    }

}
