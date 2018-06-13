package com.dongyang.dev.foodary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;

    // Provide a reference to the views for each data item

    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextView2;


        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mTextView = (TextView)view.findViewById(R.id.textview);
            mTextView2 = (TextView)view.findViewById(R.id.textview2);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }




    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
//        ...
        final ViewHolder vh = new ViewHolder(v);
        vh.itemView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), vh.mTextView.getText(), Toast.LENGTH_SHORT).show( );
            }
        });

        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).text);
        holder.mTextView2.setText(mDataset.get(position).text2);
        holder.mImageView.setImageResource(mDataset.get(position).img);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class MyData{
    public String text;
    public String text2;
    public int img;

    public MyData(String text, String text2, int img){
        this.text = text;
        this.text2= text2;
        this.img = img;

    }
}
