package com.example.abraarsyed.twitter;

/**
 * Created by abraarsyed on 18/7/15.
 */

import com.example.abraarsyed.twitter.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    private LayoutInflater inflater;
    List<Tweet> tweetss = new ArrayList<Tweet>();

    public TweetAdapter(Activity activity, List<Tweet> items){
        super(activity, R.layout.row_tweet, items);
        tweetss=items;
        inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //return inflater.inflate(R.layout.row_tweet, parent, false);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_tweet, parent,false);
        }
        //View rowView = inflater.inflate(R.layout.row_tweet, parent, false);
        TextView tweetTitle = (TextView)convertView.findViewById(R.id.tweetTitle);
        TextView tweetBody = (TextView)convertView.findViewById(R.id.tweetBody);
        Tweet tweetsss = tweetss.get(position);
        tweetTitle.setText(tweetsss.getTitle());
        tweetBody.setText(tweetsss.getBody());
        return convertView;

    }

}
