package basisproject.lym.org.basisproject.bottom_bar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import basisproject.lym.org.basisproject.R;
import basisproject.lym.org.bottom_bar.BottomBarLayout;
import basisproject.lym.org.bottom_bar.listener.OnBottomBarEntity;
import basisproject.lym.org.bottom_bar.listener.OnBottomBarSelectListener;

public class BottomBarActivity extends AppCompatActivity {
    private static final String[] TEXT = {"头条", "新闻", "好玩", "好看"};
    private static final int[] NORMAL_ICON = {R.mipmap.tab_home_normal, R.mipmap.tab_micro_normal, R.mipmap.tab_video_normal, R.mipmap.tab_me_normal};
    private static final int[] SELECT_ICON = {R.mipmap.tab_home_selected, R.mipmap.tab_micro_selected, R.mipmap.tab_video_selected, R.mipmap.tab_me_selected};
    private BottomBarLayout mBarLayout;
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    private Fragment mIndexFragment;

    private void showToast(CharSequence charSequence) {
        @SuppressLint("ShowToast")
        Toast toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(charSequence);
        toast.show();
    }

    public void changeFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        if (null != manager) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (null != mIndexFragment) {
                fragmentTransaction.hide(mIndexFragment);
            }
            Fragment temp = null;
            switch (index) {
                case 0:
                    temp = mFragments.get(index);
                    if (null == temp) {
                        TabFragment.sText = "tabOne";
                        temp = TabFragment.instance();
                        mFragments.put(index, temp);
                        fragmentTransaction.add(R.id.frame_content, temp);
                    }
                    break;
                case 1:
                    temp = mFragments.get(index);
                    if (null == temp) {
                        TabFragment.sText = "tabTwo";
                        temp = TabFragment.instance();
                        mFragments.put(index, temp);
                        fragmentTransaction.add(R.id.frame_content, temp);
                    }
                    break;
                case 2:
                    temp = mFragments.get(index);
                    if (null == temp) {
                        TabFragment.sText = "tabThree";
                        temp = TabFragment.instance();
                        mFragments.put(index, temp);
                        fragmentTransaction.add(R.id.frame_content, temp);
                    }
                    break;
                case 3:
                    temp = mFragments.get(index);
                    if (null == temp) {
                        TabFragment.sText = "tabFour";
                        temp = TabFragment.instance();
                        mFragments.put(index, temp);
                        fragmentTransaction.add(R.id.frame_content, temp);
                    }
                    break;
                default:
                    break;
            }
            if (temp == null) {
                return;
            }
            mIndexFragment = temp;
            fragmentTransaction.show(temp);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        mBarLayout = findViewById(R.id.bottom_bar);
        mBarLayout.setBottomBarSelectListener(new OnBottomBarSelectListener() {
            @Override
            public void onBottomBarSelect(int position) {
                changeFragment(position);
            }

            @Override
            public void onBottomBarSelectRepeat(int position) {
            }
        });
        initData();
    }

    private void initData() {
        int length = TEXT.length;
        List<OnBottomBarEntity> entities = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Entity entity = new Entity(TEXT[i], SELECT_ICON[i], NORMAL_ICON[i]);
            entities.add(entity);
        }
        mBarLayout.setTabEntity(entities);
        changeFragment(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_show_badge) {
            mBarLayout.showBadge(mBarLayout.getCurrentItemIndex(), 10);
        } else if(id== R.id.menu_hide_badge) {
            mBarLayout.hideBadge(mBarLayout.getCurrentItemIndex());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_bar, menu);
        return true;
    }
}
