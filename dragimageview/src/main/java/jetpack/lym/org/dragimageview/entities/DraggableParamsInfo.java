package jetpack.lym.org.dragimageview.entities;

import java.io.Serializable;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class DraggableParamsInfo implements Serializable {
    private int viewLeft;
    private int viewTop;
    private int viewWidth;
    private int viewHeight;
    private float scaledViewWhRadio = -1F;

    public DraggableParamsInfo(int viewLeft, int viewTop, int width, int height) {
        this.viewLeft = viewLeft;
        this.viewTop = viewTop;
        this.viewWidth = width;
        this.viewHeight = height;
    }

    public boolean isValid() {
        return this.viewWidth != 0 && this.viewHeight != 0 && this.scaledViewWhRadio != -1.0F;
    }

    public int getViewLeft() {
        return this.viewLeft;
    }

    public int getViewTop() {
        return this.viewTop;
    }

    public int getViewWidth() {
        return this.viewWidth;
    }

    public int getViewHeight() {
        return this.viewHeight;
    }

    public float getScaledViewWhRadio() {
        return this.scaledViewWhRadio;
    }

    public final void setScaledViewWhRadio(float var1) {
        this.scaledViewWhRadio = var1;
    }
}
