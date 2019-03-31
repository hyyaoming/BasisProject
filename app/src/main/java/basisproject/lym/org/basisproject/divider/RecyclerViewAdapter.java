package basisproject.lym.org.basisproject.divider;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import basisproject.lym.org.basisproject.R;

/**
 * author: ym.li
 * since: 2019/3/31
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ONE = 0x001f;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_holder_one, viewGroup, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RecyclerViewAdapter.ViewHolderOne) {
            ((TextView) viewHolder.itemView).setText(String.valueOf(i));
        }
    }

    @Override
    public int getItemCount() {
        return 24;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (null == manager) {
            return;
        }
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i % 3 + 1;
            }
        });
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {

        ViewHolderOne(@NonNull View itemView) {
            super(itemView);
        }
    }
}
