package com.karakullukcu.huseyin.anagrammaker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Simple RecyclerView adapter with OnClickListener.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mAnagrams;
    private static ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int itemIndex, String text);
    }

    public RecyclerViewAdapter(Context context, List<String> anagrams) {
        mContext = context;
        mAnagrams = anagrams;
    }

    public void swapData(List<String> newData) {
        mAnagrams = newData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mAnagrams.get(position));
    }

    public void addOnListItemClickListener(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mAnagrams == null)
            return 0;
        else
            return mAnagrams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.anagramText);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition(),textView.getText().toString());
        }
    }
}
