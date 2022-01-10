package com.mobileapllication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.mobileapllication.databinding.ActivityMainBinding;
import com.mobileapllication.db.DBManager;
import com.mobileapllication.db.WishList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this::onItemSelected);
        binding.bottomNavigation.setOnNavigationItemReselectedListener(this::onItemSelected);
//        binding.bottomNavigation.setSelectedItemId(R.id.nav_search);


        //fetching wishlists from db
        DBManager dbManager = DBManager.getInstance(this);
        List<WishList> wishLists = dbManager.getWishListsByUser(FirebaseAuth.getInstance().getUid());
        if (wishLists.isEmpty()) {
            //if no wishlist added yet then creating default wishlists
            List<WishList> defaultWishLists = WishList.generateDefaultWishLists(FirebaseAuth.getInstance().getUid());
            for (WishList defaultWishList : defaultWishLists) {
                dbManager.insertWishList(defaultWishList);
                wishLists.add(defaultWishList);
            }
        }

    }

    /**
     * this method listens the click on bottom navigation tabs
     * it returns true only if it has consumed the click
     */
    public boolean onItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_person:
                replaceFragment(new ProfileFragment(), false);
                return true;
            case R.id.nav_search:
                replaceFragment(new SearchFragment(), false);
                return true;
            case R.id.nav_settings:
                replaceFragment(new settingsFragment(), false);
                return true;
        }
        return false;
    }

    /**
     * this method replaces the fragments (pieces of ui, here all those screens that gets replaced when bottom navigation is clicked) inside activity container
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameSearch, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            ft.addToBackStack(fragment.getClass().getSimpleName());
        }
        ft.commit();
    }

//    logout to login screen
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


}
