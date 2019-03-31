package basisproject.lym.org.basisproject.divider;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import basisproject.lym.org.basisproject.R;
import basisproject.lym.org.recyclerview_divider.RecyclerViewDivider;

/**
 * author: ym.li
 * since: 2019/3/31
 */
public class RecyclerViewDividerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_divider);
        initView();
    }

    private void initView() {
        RecyclerView recyclerViewOne = findViewById(R.id.rvDividerOne);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        new RecyclerViewDivider.Builder(this)
                .color(Color.TRANSPARENT)
                .hideLastDivider()
                .size(16)
                .build()
                .addTo(recyclerViewOne);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerViewOne.setLayoutManager(manager);
        recyclerViewOne.setAdapter(adapter);
    }
}
