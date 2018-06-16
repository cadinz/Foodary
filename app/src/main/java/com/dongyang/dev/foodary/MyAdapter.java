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

        ImageView item_imageView;
        TextView item_tvid;
        TextView item_tvtitle;


        public ViewHolder(View view) {
            super(view);
            item_imageView = view.findViewById(R.id.item_imageview);
            item_tvid = view.findViewById(R.id.item_id);
            item_tvtitle = view.findViewById(R.id.item_title);

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
                Toast.makeText(view.getContext(), vh.item_tvid.getText(), Toast.LENGTH_SHORT).show( );
            }
        });

        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.item_tvid.setText(mDataset.get(position).item_id);
        holder.item_tvtitle.setText(mDataset.get(position).item_title);
        holder.item_imageView.setImageResource(mDataset.get(position).item_image);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class MyData{
    public String item_id;
    public String item_title;
    public int item_image;

    public MyData(String item_id, String item_title, int item_image){
        this.item_id = item_id;
        this.item_title= item_title;
        this.item_image = item_image;

    }
}
