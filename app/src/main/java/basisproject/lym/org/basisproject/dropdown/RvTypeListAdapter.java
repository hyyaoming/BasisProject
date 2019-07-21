package basisproject.lym.org.basisproject.dropdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import basisproject.lym.org.basisproject.R;

/**
 * author: ym.li
 * since: 2019/7/21
 */
public class RvTypeListAdapter extends RecyclerView.Adapter<RvTypeListAdapter.TypeViewHolder> {
    private String[] mTypeArray;
    private LayoutInflater mInflater;
    private int mSelectPosition;
    private OnItemClickListener mItemClickListener;

    RvTypeListAdapter(Context context) {
        mTypeArray = context.getResources().getStringArray(R.array.type_array);
        mInflater = LayoutInflater.from(context);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = mInflater.inflate(R.layout.item_type_tv, viewGroup, false);
        return new TypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder typeViewHolder, @SuppressLint("RecyclerView") final int i) {
        typeViewHolder.mTextView.setText(mTypeArray[i]);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(mSelectPosition == i ? "#ff6632" : "#F5F5F5"));
        drawable.setCornerRadius(dip2px(typeViewHolder.mTextView.getContext()));
        typeViewHolder.mTextView.setTextColor(Color.parseColor(mSelectPosition == i ? "#ffffff" : "#333333"));
        typeViewHolder.mTextView.setBackgroundDrawable(drawable);
        typeViewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPosition = i;
                notifyDataSetChanged();
                if (null != mItemClickListener) {
                    mItemClickListener.onItemClick(mTypeArray[mSelectPosition]);
                }
            }
        });
    }

    private int dip2px(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) 3 * scale + 0.5f);
    }

    @Override
    public int getItemCount() {
        return mTypeArray.length;
    }

    public interface OnItemClickListener {
        void onItemClick(String selectType);
    }

    class TypeViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_type);
        }
    }
}
