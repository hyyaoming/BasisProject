package basisproject.lym.org.basisproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * author: ym.li
 * since: 2019/7/21
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initViews();
        bindViews();
    }

    protected abstract void bindViews();

    protected abstract int getLayoutRes();

    protected abstract void initViews();
}
