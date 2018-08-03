package com.fahimehghasemi.freesamplecode.recyclerview.endless;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fahimehghasemi.freesamplecode.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EndlessRecyclerActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    NewsAdapter mNewsAdapter = new NewsAdapter();
    List<News> mNews = new ArrayList<>();
    static final int ITEM_PER_PAGE = 5;
    JSONArray mJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endless_recycler);
        try {
            mJSONArray = new JSONArray(Util.loadJSONFromAsset(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new ScrollListener(linearLayoutManager));
        mRecyclerView.setAdapter(mNewsAdapter);
        simulateGetItemFromNet(1);
    }

    private void simulateGetItemFromNet(final int currentPage) {

        Thread getDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showNews(getItems(currentPage, ITEM_PER_PAGE));

            }
        });
        getDataThread.start();

    }

    private void showNews(List<News> items) {
        if (items == null || items.size() == 0)
            return;
        final int itemInsertedIndex = mNews.size();
        mNews.addAll(items);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNewsAdapter.notifyItemInserted(itemInsertedIndex);
            }
        });
    }

    public List<News> getItems(int currentPage, int itemPerPage) {
        int startIndex = (currentPage - 1) * itemPerPage;
        List<News> newsList = new ArrayList<>();
        for (int i = startIndex; i < (itemPerPage * currentPage) && i < mJSONArray.length(); ++i) {
            try {
                JSONObject jsonObject = mJSONArray.getJSONObject(i);
                News news = new News(jsonObject.getString("title"), jsonObject.getString("body"));
                newsList.add(news);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newsList;
    }

    class ScrollListener extends EndlessRecyclerOnScrollListener {

        public ScrollListener(LinearLayoutManager linearLayoutManager) {
            super(linearLayoutManager);
        }

        @Override
        public void onLoadMore(int current_page) {
            simulateGetItemFromNet(current_page);
        }
    }

    class NewsAdapter extends RecyclerView.Adapter {
        final int ITEM_TYPE = 1;
        final int PROGRESS_TYPE = 2;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == PROGRESS_TYPE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_progress, parent, false);
                return new ProgressViewHolder(view);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_news, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position != mNews.size()) {
                News news = mNews.get(position);
                ((NewsViewHolder) holder).title.setText(news.getTitle());
                ((NewsViewHolder) holder).body.setText(news.getBody());
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == mNews.size() ? PROGRESS_TYPE : ITEM_TYPE;
        }

        @Override
        public int getItemCount() {
            //we add 1 to size because of progress
            return mNews.size() + 1;
        }

        class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView body;

            public NewsViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                body = itemView.findViewById(R.id.body);
            }
        }

        class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar mProgressBar;

            public ProgressViewHolder(View itemView) {
                super(itemView);
                mProgressBar = itemView.findViewById(R.id.progress);
            }
        }
    }
}
