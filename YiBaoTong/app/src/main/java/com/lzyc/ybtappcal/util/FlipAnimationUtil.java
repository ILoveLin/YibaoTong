package com.lzyc.ybtappcal.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by lovelin on 2016/11/18.
 */
public class FlipAnimationUtil extends Animation {
    private Camera camera;
    private float fromDegree;
    private float toDegree;
    private float centerX;
    private float centerY;
    private float depthZ;
    private boolean reverse;

    public FlipAnimationUtil(float fromDegree, float toDegree, float centerX, float centerY,
                             float depthZ, boolean reverse) {
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
        this.centerX = centerX;
        this.centerY = centerY;
        this.reverse = reverse;
        this.depthZ = depthZ;

        /**
         * 设置动画的差值器，决定了时间片的时间点
         */
        this.setInterpolator(new LinearInterpolator());
    }

    /**
     * 动画初始化函数
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    /**
     * 动画调用的关键函数，在每一个动画时间片到达的时候调用，会调用多次，动画的差值器对它的 执行频率直接影响
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        float degree = fromDegree + (toDegree - fromDegree) * interpolatedTime;

        final Matrix matrix = t.getMatrix();
        camera.save();
        if (reverse) {
            camera.translate(0, 0, depthZ * interpolatedTime);
        } else {
            camera.translate(0, 0, depthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degree);
        camera.getMatrix(matrix);
//        camera.rotateX(degree);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
