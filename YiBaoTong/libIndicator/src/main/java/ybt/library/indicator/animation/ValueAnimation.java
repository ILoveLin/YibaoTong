package ybt.library.indicator.animation;


public class ValueAnimation {

    private ColorAnimation colorAnimation;
    private ScaleAnimation scaleAnimation;
    private WormAnimation wormAnimation;
    private SlideAnimation slideAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {

        void onColorAnimationUpdated(int color, int colorReverse);

        void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse);

        void onWormAnimationUpdated(int leftX, int rightX);

        void onSlideAnimationUpdated(int xCoordinate);
    }

    public ValueAnimation(UpdateListener listener) {
        updateListener = listener;
    }

    
    public ColorAnimation color() {
        if (colorAnimation == null) {
            colorAnimation = new ColorAnimation(updateListener);
        }

        return colorAnimation;
    }

    
    public ScaleAnimation scale() {
        if (scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(updateListener);
        }

        return scaleAnimation;
    }

    
    public WormAnimation worm() {
        if (wormAnimation == null) {
            wormAnimation = new WormAnimation(updateListener);
        }

        return wormAnimation;
    }

    
    public SlideAnimation slide() {
        if (slideAnimation == null) {
            slideAnimation = new SlideAnimation(updateListener);
        }

        return slideAnimation;
    }
}
