package com.mobileapllication;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.core.util.Consumer;

import com.bumptech.glide.Glide;
import com.mobileapllication.databinding.ItemBookBinding;

import java.util.ArrayList;

/**
 * Binds the book response search results to book item UI
 */
public class SearchAdapter extends GenericAdapter<BookInfo, ItemBookBinding> {

    private final Consumer<BookInfo> wishListClickConsumer;

    public SearchAdapter(Context context, ArrayList<BookInfo> arrayList, Consumer<BookInfo> wishListClickConsumer) {
        super(context, arrayList, false);
        this.wishListClickConsumer = wishListClickConsumer;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_book;
    }

    @Override
    public void onBindData(BookInfo model, int position, ItemBookBinding binding) {

        //if response contains authors, showing comma-separated author names, otherwise hiding the text view for the same
        if (model.getVolumeInfo().getAuthors() != null && !model.getVolumeInfo().getAuthors().isEmpty()) {
            binding.txtBookAuthor.setVisibility(View.VISIBLE);
            binding.txtBookAuthor.setText(TextUtils.join(",", model.getVolumeInfo().getAuthors()));
        } else {
            binding.txtBookAuthor.setVisibility(View.GONE);
        }

        //setting up click listener on add to wish list button
        binding.txtBookName.setText(model.getVolumeInfo().getTitle());
        binding.txtBookSubtitle.setText(model.getVolumeInfo().getSubtitle());
        binding.imgAddWishlist.setOnClickListener(v -> {
            wishListClickConsumer.accept(model);
        });

        //if thumbnail images are available loading them in the image view with glide library
        if (model.getVolumeInfo().getImageLinks() != null) {
            if (model.getVolumeInfo().getImageLinks().getThumbnail() != null) {
                Glide.with(binding.getRoot().getContext()).load(model.getVolumeInfo().getImageLinks().getThumbnail()).into(binding.imgBook);
            }else if (model.getVolumeInfo().getImageLinks().getSmallThumbnail() != null) {
                Glide.with(binding.getRoot().getContext()).load(model.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(binding.imgBook);
            } else {
                binding.imgBook.setImageResource(R.drawable.ic_book_icon);
            }
        } else {
            binding.imgBook.setImageResource(R.drawable.ic_book_icon);
        }
    }

}