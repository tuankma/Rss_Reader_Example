package com.mbwasi.rssexample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Execute Get RSS Async task
		new GetRSSFeed().execute();
		
	}

	// AsyncTask is necessary because without it fetching the RSS feed will
	// happen on the main thread, this can freeze the UI and cause your App to
	// be shutdown by Android
	// furthermore it is not allowed to have network related tasks on the main
	// thread from android 4.0 onwards
	private class GetRSSFeed extends AsyncTask<Void, Void, ArrayList<RssItem>> {

		// All the work is done here, this method runs on a background thread so
		// it does not intrerfere with the UI, you can not access any UI
		// components from this method
		@Override
		protected ArrayList<RssItem> doInBackground(Void... params) {
			ArrayList<RssItem> rssItems = null;
			try {

				URL url = new URL("http://example.com/feed.rss");// replace with
																	// your feed
																	// url
				RssFeed feed = RssReader.read(url);
				rssItems = feed.getRssItems();

			} catch (MalformedURLException e) {
				Log.e("RSS Reader", "Malformed URL");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("RSS Reader", "Malformed URL");
				e.printStackTrace();
			} catch (SAXException e) {
				Log.e("RSS Reader", "Malformed URL");
				e.printStackTrace();
			}
			return rssItems;
		}

		// Gets called before the background work starts, you can use this
		// method to start a progressbar so the user knows something is
		// happening
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();
		}

		// ArrayList of fetched RSS items is passed here, from this method you
		// have access to the UI thread, so for instance you can populate a
		// TextView with the result.
		// If you started a progressBar in the onPreExecute method you can stop
		// it here.
		@Override
		protected void onPostExecute(ArrayList<RssItem> items) {
			// Example: print RSS items titles to CatLog
			for (RssItem rssItem : items) {
				Log.i("RSS Reader", rssItem.getTitle());
			}

			// Example:populate TextView with first RSS Item's title
			// textView.setText(items.get(0).getTitle());

			//Example: Populate a listView with RSS items titles
			
		}

	}

}
