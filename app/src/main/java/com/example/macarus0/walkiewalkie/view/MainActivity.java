package com.example.macarus0.walkiewalkie.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.macarus0.walkiewalkie.R;
import com.example.macarus0.walkiewalkie.viewmodel.WalkieViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        DogListAdapter.DogClickHandler,
        OwnerListAdapter.OwnerClickHandler,
        WalkListAdapter.WalkClickHandler{

    String TAG = "MainActivity";

    @BindView(R.id.items_list)
    RecyclerView mItemsRecyclerView;
    @BindView(R.id.add_item_button)
    AppCompatButton mAddItemButton;
    @BindView(R.id.main_start_walk_fab)
    FloatingActionButton mStartWalkFab;
    WalkieViewModel mViewModel;
    private MenuItem mSelectedBottomItemId;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> selectBottomItem(item.getItemId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(WalkieViewModel.class);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        selectBottomItem(R.id.navigation_dogs);

        mStartWalkFab.setOnClickListener(view -> startWalk());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mItemsRecyclerView.setLayoutManager(linearLayoutManager);


    }

    private void showDogsList() {
        mAddItemButton.setText(getText(R.string.add_dog));
        mAddItemButton.setVisibility(View.VISIBLE);
        DogListAdapter dogListAdapter = new DogListAdapter();
        dogListAdapter.setDogClickHandler(this::dogClick);
        mItemsRecyclerView.setAdapter(dogListAdapter);
        mViewModel.getAllDogs().observe(this, dogListAdapter::setDogs);
        mAddItemButton.setOnClickListener(v -> showDog(DogContactActivity.ADD_DOG));
    }

    private void showOwnersList() {
        mAddItemButton.setText(getText(R.string.add_owner));
        mAddItemButton.setVisibility(View.VISIBLE);
        OwnerListAdapter ownerListAdapter = new OwnerListAdapter();
        ownerListAdapter.setOwnerClickHandler(this::ownerClick);
        mItemsRecyclerView.setAdapter(ownerListAdapter);
        mViewModel.getAllOwners().observe(this, ownerListAdapter::setOwners);
        mAddItemButton.setOnClickListener(v -> showOwner(OwnerContactActivity.ADD_OWNER));
    }

    private void showWalksList() {
        mAddItemButton.setVisibility(View.GONE);
        WalkListAdapter walkListAdapter = new WalkListAdapter();
        walkListAdapter.setWalkClickHandler(this::showWalk);
        mItemsRecyclerView.setAdapter(walkListAdapter);
        mViewModel.getAllWalks().observe(this, walkListAdapter::setWalks);
    }

    private void showDog(long id) {
        Intent intent = new Intent(this, DogContactActivity.class);
        intent.putExtra(DogContactActivity.DOG_ID, id);
        startActivity(intent);
    }

    private void showOwner(long id) {
        Intent intent = new Intent(this, OwnerContactActivity.class);
        intent.putExtra(OwnerContactActivity.OWNER_ID, id);
        startActivity(intent);
    }

    private void showWalk(long id) {
        Intent intent = new Intent(this, WalkSummaryActivity.class);
        intent.putExtra(WalkSummaryActivity.WALK_ID, id);
        startActivity(intent);
    }

    private boolean selectBottomItem(int itemId) {
        switch (itemId) {
            case R.id.navigation_dogs:
                showDogsList();
                return true;
            case R.id.navigation_owners:
                showOwnersList();
                return true;
            case R.id.navigation_walks:
                showWalksList();
                return true;
        }
        return false;
    }

    @Override
    public void dogClick(long id) {
        showDog(id);
    }

    @Override
    public void ownerClick(long id) {
        showOwner(id);
    }

    @Override
    public void walkClick(long id ){
        Log.i(TAG, "walkClick: Walk CLicked!");
        showWalk(id);}

    private void startWalk() {
        Intent intent = new Intent(this, StartWalkActivity.class);
        startActivity(intent);
    }
}
