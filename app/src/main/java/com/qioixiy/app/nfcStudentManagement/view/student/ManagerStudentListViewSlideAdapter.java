package com.qioixiy.app.nfcStudentManagement.view.student;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.Student;

import java.util.List;

public class ManagerStudentListViewSlideAdapter extends BaseAdapter {

    private List<Student> mStudentList;
    private Context context;
    private OnClickListenerEditOrDelete onClickListenerEditOrDelete;

    public ManagerStudentListViewSlideAdapter(Context context, List<Student> mStudentList) {
        this.mStudentList = mStudentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mStudentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStudentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Student student = mStudentList.get(position);

        View view;
        ViewHolder viewHolder;
        if (null == convertView) {
            view = View.inflate(context, R.layout.item_slide_delete_edit, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvContent = (TextView) view.findViewById(R.id.tvContent);
            viewHolder.img = (ImageView) view.findViewById(R.id.imgLamp);
            viewHolder.tvDelete = (TextView) view.findViewById(R.id.delete);
            viewHolder.tvEdit = (TextView) view.findViewById(R.id.tvEdit);
            view.setTag(viewHolder);//store up viewHolder
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.img.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tvName.setText(student.getName());
        viewHolder.tvContent.setText(student.getTelphone());
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerEditOrDelete != null) {
                    onClickListenerEditOrDelete.OnClickListenerDelete(position);
                }
            }
        });
        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerEditOrDelete != null) {
                    onClickListenerEditOrDelete.OnClickListenerEdit(position);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        public TextView tvName, tvEdit, tvDelete;
        public ImageView img;
        public TextView tvContent;
    }

    public interface OnClickListenerEditOrDelete {
        void OnClickListenerEdit(int position);

        void OnClickListenerDelete(int position);
    }

    public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerEditOrDelete1) {
        this.onClickListenerEditOrDelete = onClickListenerEditOrDelete1;
    }

}