package com.dmitriialeksandrov.githubapp.widget;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    private final List<T> items = new ArrayList<>();

    @Nullable
    private OnItemClickListener<T> onItemClickListener;

    private final View.OnClickListener internalListener = (view) -> {
        if (onItemClickListener != null) {
            int position = (int) view.getTag();
            T item = items.get(position);
            onItemClickListener.onItemClick(item);
        }
    };

    @Nullable
    private EmptyRecyclerView recyclerView;

    public BaseAdapter(@NonNull List<T> items) {
        this.items.addAll(items);
    }

    public void attachToRecyclerView(@NonNull EmptyRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setAdapter(this);
        refreshRecycler();
    }

    public final void add(@NonNull T value) {
        items.add(value);
        refreshRecycler();
    }

    public final void add(@NonNull List<T> values) {
        items.addAll(values);
        refreshRecycler();
    }

    public final void changeDataSet(@NonNull List<T> values) {
        items.clear();
        items.addAll(values);
        refreshRecycler();
    }

    public final void clear() {
        items.clear();
        refreshRecycler();
    }

    public void refreshRecycler() {
        notifyDataSetChanged();
        if (recyclerView != null) {
            recyclerView.checkIfEmpty();
        }
    }

    @CallSuper
    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(internalListener);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener<T> {

        void onItemClick(@NonNull T item);

    }

}
