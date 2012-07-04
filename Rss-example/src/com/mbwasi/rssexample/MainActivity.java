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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView txtRSSItem;

	ListView listRSSItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtRSSItem = (TextView) findViewById(R.id.textViewRSSItem);

		listRSSItems = (ListView) findViewById(R.id.listViewItems);

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

				URL url = new URL(
						"https://news.google.com/news/feeds?hl=en&gl=us&q=android&um=1&ie=UTF-8&output=rss");// replace
																												// with
				// your feed
				// url. This url is a google news search feed for android
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
			if (items != null) {
				// Example: print RSS items titles to CatLog
				for (RssItem rssItem : items) {
					Log.i("RSS Reader", rssItem.getTitle());
				}

				// Example:populate TextView with first RSS Item's title
				txtRSSItem.setText(items.get(0).getTitle());

				// Example: Populate a listView with RSS items titles, using a
				// simple ArrayAdapter

				// Get item titles from the items ArrayList and put them into a
				// string array for use with the ArrayAdapter
				String[] itemTitlesArray = new String[items.size()];
				int i = 0;
				for (RssItem rssItem : items) {
					Log.i("RSS Reader", rssItem.getTitle());
					itemTitlesArray[i] = rssItem.getTitle();
					i++;
				}

				// Set titles as listview items
				listRSSItems.setAdapter(new ArrayAdapter<String>(
						getApplicationContext(), R.layout.listitem,
						R.id.textTitle, itemTitlesArray));
			} else {
				Toast.makeText(getApplicationContext(), "No RSS items found",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}
