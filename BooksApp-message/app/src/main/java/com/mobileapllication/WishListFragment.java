package com.mobileapllication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobileapllication.databinding.FragmentWishlistBinding;
import com.mobileapllication.databinding.ItemBookBinding;
import com.mobileapllication.db.Book;
import com.mobileapllication.db.DBManager;
import com.mobileapllication.db.WishList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WishListFragment extends Fragment {

    private FragmentWishlistBinding binding;

    public static WishListFragment newInstance(int wishListId) {
        Bundle args = new Bundle();
        args.putInt("wishlist_id", wishListId);
        WishListFragment fragment = new WishListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWishlistBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBManager dbManager = DBManager.getInstance(getContext());

        int wishlistId = getArguments().getInt("wishlist_id");

        //setting wishlist data
        WishList wishList = dbManager.getWishListById(wishlistId);
        binding.wishListName.setText(wishList.getName());
        Glide.with(getContext())
                .load(ContextCompat.getDrawable(getContext(), wishList.getImage()))
                .centerCrop()
                .into(binding.wishListImg);


        //fetching books
        List<Book> books = dbManager.getBooksInWishList(wishlistId);
        if (books.isEmpty()) {
            Toast.makeText(getContext(), "No books found!", Toast.LENGTH_SHORT).show();
            return;
        }

        //setting adapter for books in the wishllist
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.booksInWishList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.booksInWishList.getContext(), layoutManager.getOrientation());
        binding.booksInWishList.addItemDecoration(dividerItemDecoration);

        GenericAdapter<Book, ItemBookBinding> adapter = new GenericAdapter<Book, ItemBookBinding>(getContext(), books, false) {
            @Override
            public int getLayoutResId() {
                return R.layout.item_book;
            }

            @Override
            public void onBindData(Book model, int position, ItemBookBinding binding) {
                binding.imgAddWishlist.setVisibility(View.GONE);
                if (model.getImage() != null) {
                    Glide.with(getContext()).load(model.getImage()).into(binding.imgBook);
                } else {
                    binding.imgBook.setImageResource(R.drawable.ic_book_icon);
                }
                binding.txtBookName.setText(model.getName());
                binding.txtBookAuthor.setText(model.getAuthor());
            }
        };
        binding.booksInWishList.setAdapter(adapter);


    }
}
