package com.mobileapllication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileapllication.databinding.FragmentProfileBinding;
import com.mobileapllication.databinding.ItemWishlistBinding;
import com.mobileapllication.db.DBManager;
import com.mobileapllication.db.WishList;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //fetching wishlists from db
        DBManager dbManager = DBManager.getInstance(getContext());
        List<WishList> wishLists = dbManager.getWishListsByUser(currentUser.getUid());

        //setting up wishlist grid and its adapter
        binding.wishLists.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        binding.wishLists.setAdapter(new GenericAdapter<WishList, ItemWishlistBinding>(

                getContext(), wishLists, true) {

            @Override
            public int getLayoutResId() {
                return R.layout.item_wishlist;
            }

            @Override
            public void onBindData(WishList model, int position, ItemWishlistBinding binding) {
                //binding
                binding.wishListName.setText(model.getName());
                Glide.with(getContext())
                        .load(ContextCompat.getDrawable(getContext(), model.getImage()))
                        .centerCrop()
                        .into(binding.wishListImg);
            }

            @Override
            public void onItemClick(WishList model, int position) {
                //replacing wish list ui on item click
                WishListFragment fragment = WishListFragment.newInstance(model.getId());
                ((MainActivity) getActivity()).replaceFragment(fragment, true);
            }
        });

    }
}

