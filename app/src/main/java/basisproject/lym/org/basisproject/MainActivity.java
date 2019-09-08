package basisproject.lym.org.basisproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import basisproject.lym.org.basisproject.bottom_bar.BottomBarActivity;
import basisproject.lym.org.basisproject.dialog.DialogActivity;
import basisproject.lym.org.basisproject.divider.RecyclerViewDividerActivity;
import basisproject.lym.org.basisproject.dragimageview.DragImageViewActivity;
import basisproject.lym.org.basisproject.dropdown.DropDownActivity;
import jetpack.lym.org.dragimageview.ImagesViewerActivity;

/**
 * @author ym.li
 * @since 2019年2月24日18:16:10
 */
public class MainActivity extends AppCompatActivity {
    private static final Class[] CLASS = {BottomBarActivity.class, DialogActivity.class, RecyclerViewDividerActivity.class, DropDownActivity.class,
            DragImageViewActivity.class};
    private static final String[] CLASS_NAME = {"BottomBarActivity", "DialogActivity", "RecyclerViewDividerActivity", "DropDownActivity", "DragImageViewActivity"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rvSample);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SampleAdapter());
    }

    class SampleAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sample_item, viewGroup, false);
            return new SampleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
            ((TextView) viewHolder.itemView).setText(CLASS_NAME[i]);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, CLASS[i]));
                }
            });
        }

        @Override
        public int getItemCount() {
            return CLASS_NAME.length;
        }
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {

        SampleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
