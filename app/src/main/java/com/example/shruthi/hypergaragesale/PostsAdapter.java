package com.example.shruthi.hypergaragesale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.media.CamcorderProfile.get;

/**
 * Created by shruthi on 10/17/16.
 */


// Notice here to support search feature Filterable was implemented
    public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> implements Filterable {
    private ArrayList<BrowsePosts> mDataset;
    private ArrayList<BrowsePosts> mFilteredList;

    public  Uri myuri;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mPrice;
        public ImageView mImage;



        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) itemView.findViewById(R.id.titleView);
            mPrice = (TextView) itemView.findViewById(R.id.priceView);
            mImage=(ImageView) itemView.findViewById(R.id.thumbnail);
            // Implement view click Listener when make each row of RecycleView clickable


        }


    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsAdapter(ArrayList<BrowsePosts> myDataset) {
        mDataset = myDataset;
        //Added to support search feature.
        mFilteredList = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get elements from your dataset at this position
        // - replace the contents of the views with that elements
        //Previously instead of mFilteredlist,it was mDataset,this is modified to support search feature
        //during android adavnced Spring class 2017
        holder.mTitle.setText(mFilteredList.get(position).mTitle);
        holder.mPrice.setText(mFilteredList.get(position).mPrice);
        myuri=Uri.parse(mFilteredList.get(position).mImage);
        holder.mImage.setImageURI(myuri);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

            return mFilteredList.size();

    }

    //Added to support search feature
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<BrowsePosts> filteredList = new ArrayList<>();
                String charString = constraint.toString().toLowerCase();
                if (charString.isEmpty()) {

                    mFilteredList = mDataset;
                }
                else
                {

                    for( BrowsePosts post: mDataset){
                        if(post.getmTitle().toLowerCase().contains(charString))
                            filteredList.add(post);
                    }
                    mFilteredList=filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList=(ArrayList<BrowsePosts>) results.values;

                notifyDataSetChanged();
                notifyItemRangeChanged(0, mFilteredList.size());

            }
        };
    }



}