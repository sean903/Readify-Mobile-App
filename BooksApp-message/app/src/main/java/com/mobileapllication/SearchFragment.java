package com.mobileapllication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.mobileapllication.databinding.FragmentSearchBinding;
import com.mobileapllication.db.Book;
import com.mobileapllication.db.DBManager;
import com.mobileapllication.db.WishList;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// Represents the screen for search UI

public class SearchFragment extends Fragment {

    private final BookService bookService = ServiceGenerator.createService(BookService.class);
    private FragmentSearchBinding binding;
    private ArrayList<BookInfo> books;
    private SearchAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //when view gets created, listening all user searches on search bar
        binding.searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchConfirmed(CharSequence text) {
                super.onSearchConfirmed(text);

                //when search is confirmed by user, calling the method which fetches data from books API
                if (!TextUtils.isEmpty(text)) {
                    searchBooks(text.toString());
                }
            }
        });

        //taking a blank list of books and setting up adapter with vertical list configuration on recyclerview
        books = new ArrayList<>();
        binding.searchBar.openSearch();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.listBooks.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.listBooks.getContext(), layoutManager.getOrientation());
        binding.listBooks.addItemDecoration(dividerItemDecoration);
        adapter = new SearchAdapter(getContext(), books, this::showWishListsDialog);
        binding.listBooks.setAdapter(adapter);

    }



//     this method calls google books API and updates the search results in the view

    private void searchBooks(String query) {
        SimpleArcDialog mDialog = new SimpleArcDialog(getContext());
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();

        //calling web service with given query and max 30 search results(google books api returns only 30 max results for any query)
        Call<BooksResponse> bookInfoCall = bookService.searchBooks(query, getString(R.string.books_api_key), 30);
        //enqueuing the  api call asynchronously
        bookInfoCall.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(@NotNull Call<BooksResponse> call, @NotNull Response<BooksResponse> response) {
                if (isDetached()) return;
                mDialog.dismiss();
                if (response.isSuccessful()) {

                    //checking if results are found within response body for the given query or not
                    BooksResponse booksResponse = response.body();
                    if (booksResponse == null || booksResponse.getItems() == null || booksResponse.getItems().isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_books_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //if results are found, clearing the original list and refilling it
                    List<BookInfo> booksResponseItems = booksResponse.getItems();
                    books.clear();
                    books.addAll(booksResponseItems);
                    adapter.notifyDataSetChanged();
                    binding.searchBar.closeSearch();
                } else {
                    Toast.makeText(getContext(), R.string.err_search, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<BooksResponse> call, @NotNull Throwable t) {

                //in case of failure, dismissing loader, printing the err to console, closing search bar and showing an error message on UI
                if (isDetached()) return;
                mDialog.dismiss();
                t.printStackTrace();
                binding.searchBar.closeSearch();
                Toast.makeText(getContext(), R.string.err_search, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showWishListsDialog(BookInfo bookInfo) {
        DBManager dbManager = DBManager.getInstance(getContext());
        List<WishList> wishLists = dbManager.getWishListsByUser(FirebaseAuth.getInstance().getUid());
        // setting up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.choose_whishlist);

        //gathering wishlist items to display and their current state
        String[] items = new String[wishLists.size()];
        int checkedItemPos = -1;
        for (int i = 0; i < wishLists.size(); i++) {
            WishList wishList = wishLists.get(i);
            items[i] = wishList.getName();

            List<Book> booksInWishList = dbManager.getBooksInWishList(wishList.getId());
            for (Book book : booksInWishList) {
                if (book.getId().equals(bookInfo.getId())) {
                    checkedItemPos = i;
                    break;
                }
            }
        }

        //showing multi choice dialog
        int finalCheckedItemPos = checkedItemPos;
        builder.setSingleChoiceItems(items, checkedItemPos, (dialog, position) -> {
            if (position == finalCheckedItemPos) {
                return;
                //nothing to do if that's a same wishlist
            }
            if (finalCheckedItemPos != -1) {
                //remove if entry exists from another wish list
                dbManager.removeBookFromWishList(bookInfo.getId(), wishLists.get(finalCheckedItemPos).getId());
            }
            //insert book in wishlist
            Book book = new Book();
            book.setId(bookInfo.getId());
            book.setName(bookInfo.getVolumeInfo().getTitle());
            book.setImage(bookInfo.getVolumeInfo().getImageLinks() != null ? bookInfo.getVolumeInfo().getImageLinks().getThumbnail() : null);
            List<String> authors = bookInfo.getVolumeInfo().getAuthors();
            book.setAuthor(authors != null ? TextUtils.join(",", authors) : "");
            dbManager.insertBookInWishList(book, wishLists.get(position).getId());
        });
        // adding OK and Cancel buttons
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {

        });
        builder.setNegativeButton(android.R.string.cancel, null);
        // creating and showing the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
