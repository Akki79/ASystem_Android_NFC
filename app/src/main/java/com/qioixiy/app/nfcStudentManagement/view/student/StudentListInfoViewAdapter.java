package com.qioixiy.app.nfcStudentManagement.view.student;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qioixiy.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StudentListInfoViewAdapter extends RecyclerView.Adapter<StudentListInfoViewAdapter.ViewHolder> {

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

	private List<String> mDataset = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mTextView;
		public ViewHolder(View v) {
			super(v);
			mTextView = (TextView) v.findViewById(R.id.layout_item_nfcstudentmangement_title);
		}
	}

	public StudentListInfoViewAdapter(List<String> dataset) {
		mDataset.clear();
		mDataset.addAll(dataset);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nfcstudentmangement, parent, false);
		ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,(int)v.getTag());
                }
            }
        });
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.mTextView.setText(mDataset.get(position));
        holder.itemView.setTag(position);
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
