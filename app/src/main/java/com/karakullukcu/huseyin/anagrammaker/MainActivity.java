package com.karakullukcu.huseyin.anagrammaker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.karakullukcu.huseyin.anagrammaker.anagram.AnagramAlgorithm;

import java.util.List;

/**
* Single screen anagram finding app. Using AsyncTaskLoader for threading and
* handling saved instance state.
*/

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    private RecyclerViewAdapter adapter;
    private ProgressBar spinner;
    private ImageButton searchButton;
    private static final int LOADER_ID = 0;
    private static final String SEARCH_WORD_BUNDLE_KEY = "searchWord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.anagramsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecyclerViewAdapter(this,null);
        recyclerView.setAdapter(adapter);

        adapter.addOnListItemClickListener(new RecyclerViewAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int itemIndex, String text) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("anagram",text);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(MainActivity.this,getString(R.string.clipboard_event),Toast.LENGTH_SHORT).show();
            }
        });


        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        final Bundle args = new Bundle();
        getSupportLoaderManager().initLoader(LOADER_ID,args,this);

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        final AppCompatEditText searchEditText = (AppCompatEditText) findViewById(R.id.searchEditText);
        searchEditText.clearFocus();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWord = searchEditText.getText().toString().trim().replaceAll("[^a-zA-Z]","");
                args.putString(SEARCH_WORD_BUNDLE_KEY,searchWord);
                getSupportLoaderManager().restartLoader(LOADER_ID,args,MainActivity.this).forceLoad();
                searchButton.setVisibility(View.INVISIBLE);
                searchButton.setEnabled(false);
                spinner.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<String>>(this) {
            @Override
            public List<String> loadInBackground() {
                String searchWord = args.getString(SEARCH_WORD_BUNDLE_KEY);
                if (searchWord != null) {
                    AnagramAlgorithm anagramAlgorithm = new AnagramAlgorithm(MainActivity.this);
                    return anagramAlgorithm.getAnagrams(searchWord);
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        adapter.swapData(data);
        spinner.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        searchButton.setEnabled(true);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        adapter.swapData(null);
    }
}
