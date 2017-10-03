package com.dmitriialeksandrov.githubapp.screen.commits;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dmitriialeksandrov.githubapp.R;
import com.dmitriialeksandrov.githubapp.content.Commit;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.widget.BaseAdapter;

import java.util.List;

public class CommitsAdapter extends BaseAdapter<CommitHolder, CommitResponse> {

    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false;
    boolean isMoreDataAvailable = true;

    public CommitsAdapter(@NonNull List<CommitResponse> items) {
        super(items);
    }

    @Override
    public CommitHolder onCreateViewHolder(ViewGroup parent, int itemViewType) {
        return new CommitHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_commit, parent, false));
    }

    @Override
    public void onBindViewHolder(CommitHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CommitResponse commitResponse = getItem(position);
        holder.bind(commitResponse);
        if (position == getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
    }

    interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public void refreshRecycler() {
        super.refreshRecycler();
        isLoading = false;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }
}
