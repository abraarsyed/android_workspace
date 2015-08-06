package com.example.abraarsyed.twitter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TweetListActivity extends ListActivity {

    List<Tweet> tweets = new ArrayList<Tweet>();
    List<Tweet> tweetsRead;
    //private ListView tweetListView;
    private ArrayAdapter tweetItemArrayAdapter;

    private static final String TWEETS_CACHE_FILE = "tweet_cache.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //tweetsRead = read tweets from cache file
        //tweetsWrite = create dummy tweets
        //write tweetsWrite to cache file
        //use tweetsRead to show output on the screen

        try {
            // open files
            // perform operations
            FileInputStream fis = openFileInput(TWEETS_CACHE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tweetsRead = (List<Tweet>) ois.readObject();
            Log.d("codelearn", "Successfully read tweets from the file.");
            ois.close();
            fis.close();
        } catch (Exception e){
            //log exceptions (at least)
            Log.e("codelearn","Error reading tweets",e);
        }

        for ( int i = 0; i < 20; i++ ) {
            Tweet tweet = new Tweet();
            tweet.setTitle("A nice header for Tweet # " +i);
            tweet.setBody("Some random body text for the tweet # " +i);
            tweets.add(tweet);
        }

        try {
            //code to write to a file
            FileOutputStream fos = openFileOutput(TWEETS_CACHE_FILE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tweets);
            Log.d("codelearn", "Successfully wrote tweets to the file.");
            //close operators
            oos.close();
            fos.close();
        } catch (Exception e) {
            Log.e("codelearn", "Error writing tweets", e);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);
        tweetItemArrayAdapter = new TweetAdapter(this, tweetsRead);
        //tweetListView = (ListView) findViewById(R.id.tweetList);
        //tweetListView.setAdapter(tweetItemArrayAdapter);
        setListAdapter(tweetItemArrayAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, TweetDetailActivity.class);
        Tweet tweetPassed = (Tweet) getListAdapter().getItem(position);
        intent.putExtra("clicked_tweet",tweetPassed);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
