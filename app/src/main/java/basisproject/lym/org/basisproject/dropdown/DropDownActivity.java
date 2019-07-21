package basisproject.lym.org.basisproject.dropdown;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import basisproject.lym.org.basisproject.BaseActivity;
import basisproject.lym.org.basisproject.R;
import basisproject.lym.org.recyclerview_divider.RecyclerViewDivider;
import view.lym.org.library.DropDownView;


/**
 * author: ym.li
 * since: 2019/7/21
 */
public class DropDownActivity extends BaseActivity implements RvTypeListAdapter.OnItemClickListener {
    private DropDownView mDropDownView;
    private View mHeadView;
    private View mExpandView;
    private TextView mTvSelectType;

    @Override
    protected void bindViews() {
        mDropDownView.setHeaderView(mHeadView);
        mDropDownView.setExpandedView(mExpandView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_drop_down;
    }

    @Override
    protected void initViews() {
        mTvSelectType = findViewById(R.id.tv_select_type);
        mDropDownView = findViewById(R.id.drop_down_view);
        mHeadView = getLayoutInflater().inflate(R.layout.drop_down_head_view, mDropDownView.getDropDownHeaderContainer(), false);
        final ImageView imageView = mHeadView.findViewById(R.id.iv_change);
        mDropDownView.setDropDownListener(new DropDownView.DropDownListener() {
            @Override
            public void onExpandDropDown() {
                rotation(imageView, 0, 180);
            }

            @Override
            public void onCollapseDropDown() {
                rotation(imageView, 180, 0);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDropDownView.isExpanded()) {
                    mDropDownView.collapseDropDown();
                } else {
                    mDropDownView.expandDropDown();
                }
            }
        });

        mExpandView = getLayoutInflater().inflate(R.layout.drop_down_expand_view, mDropDownView.getDropDownContainer(), false);

        bindExpandView();
    }

    private void rotation(View view, int startAngel, int endAngel) {
        ObjectAnimator.ofFloat(view, "rotation", startAngel, endAngel).start();
    }

    private void bindExpandView() {
        RecyclerView typeRv = mExpandView.findViewById(R.id.rv_type_list);
        RvTypeListAdapter adapter = new RvTypeListAdapter(this);
        adapter.setItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        typeRv.setLayoutManager(manager);
        typeRv.setHasFixedSize(true);
        typeRv.setAdapter(adapter);

        new RecyclerViewDivider.Builder(this)
                .color(Color.TRANSPARENT)
                .hideLastDivider()
                .size(dip2px())
                .build()
                .addTo(typeRv);
    }

    private int dip2px() {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) ((float) 18 * scale + 0.5f);
    }

    @Override
    public void onItemClick(String selectType) {
        mTvSelectType.setText(selectType);
        mDropDownView.collapseDropDown();
    }
}
