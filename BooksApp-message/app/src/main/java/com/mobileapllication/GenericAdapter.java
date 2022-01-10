package com.mobileapllication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic adapter is a common recyclerview adapter which can be extended in order to fit the list items in rows/columns.
 * It abstracts the binding of item UI to data model object.
 */
public abstract class GenericAdapter<T, D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> mArrayList;
    private final boolean listenItemClick;

    public abstract int getLayoutResId();

    public abstract void onBindData(T model, int position, D dataBinding);

    public  void onItemClick(T model, int position){};

    public GenericAdapter(Context context, List<T> arrayList, boolean listenItemClick) {
        this.mContext = context;
        this.mArrayList = arrayList;
        this.listenItemClick = listenItemClick;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResId(), parent, false);
        return new ItemViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        onBindData(mArrayList.get(position), position, ((ItemViewHolder) holder).mDataBinding);
        View view = ((ViewDataBinding) ((ItemViewHolder) holder).mDataBinding).getRoot();
        if(listenItemClick) {
            view.setOnClickListener(view1 -> onItemClick(mArrayList.get(position), position));
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void addItems(ArrayList<T> arrayList) {
        mArrayList = arrayList;
        this.notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mArrayList.get(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        protected D mDataBinding;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mDataBinding = (D) binding;
        }
    }
}