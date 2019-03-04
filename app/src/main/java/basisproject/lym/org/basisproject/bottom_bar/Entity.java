package basisproject.lym.org.basisproject.bottom_bar;

import android.graphics.Bitmap;

import basisproject.lym.org.bottombar.listener.OnBottomBarEntity;

/**
 * author: ym.li
 * since: 2019/3/2
 */
public class Entity implements OnBottomBarEntity {
    private CharSequence mText;
    private int mSelectIcon;
    private int mUnSelectIcon;

    public Entity(CharSequence text, int selectIcon, int unSelectIcon) {
        mText = text;
        mSelectIcon = selectIcon;
        mUnSelectIcon = unSelectIcon;
    }

    @Override
    public CharSequence getText() {
        return mText;
    }

    @Override
    public Bitmap getSelectBitmap() {
        return null;
    }

    @Override
    public Bitmap getUnSelectBitmap() {
        return null;
    }

    @Override
    public int getSelectIcon() {
        return mSelectIcon;
    }

    @Override
    public int getUnSelectIcon() {
        return mUnSelectIcon;
    }
}
