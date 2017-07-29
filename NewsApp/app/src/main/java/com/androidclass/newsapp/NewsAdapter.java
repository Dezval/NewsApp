package com.androidclass.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidclass.newsapp.model.Contract;
import com.squareup.picasso.Picasso;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private Context context;
    public static final String TAG = "newsAdapter";

    public NewsAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener=listener;

    }


    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex, String url);//passes the item clicked index and the url of news item that was clicked on
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context= viewGroup.getContext();
        int layoutIdForListItem=R.layout.news_list_item;
        LayoutInflater inflater= LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately= false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NewsAdapterViewHolder holder = new NewsAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;
        TextView timeStamp;
        ImageView imageViewURL;
        String imageURL;
        String url;

        NewsAdapterViewHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.name);
            description = (TextView)view.findViewById(R.id.description);
            timeStamp = (TextView)view.findViewById(R.id.time);
            imageViewURL = (ImageView)view.findViewById(R.id.image);
            view.setOnClickListener(this);
            }

        public void bind(int pos) {

            cursor.moveToPosition(pos);
            imageURL = cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE));//sets imageURL to the string value of urlToImage from the database
            url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL));//sets url to the url string from the database

            title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_TITLE)));//sets the title text to the title string from the database
            timeStamp.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT)));//sets the timestamp text to the description in the database
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION)));//sets description from the database info

            if(url != null) {//Picasso loads the image in the ImageView from the imageURL
                Picasso.with(context)
                        .load(imageURL)
                        .into(imageViewURL);
            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos,url);

        }
    }

}
