package com.ingeniatest.gistsproject;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.ingeniatest.gistsproject.adapter.GistAdapterRecyclerView;
import com.ingeniatest.gistsproject.model.Gist;
import com.ingeniatest.gistsproject.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainPresenter.View{

    private MainPresenter mainPresenter;
    private RecyclerView gistRecycler;
    private GistAdapterRecyclerView gistAdapterRecylerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Manage Recycler View
        gistRecycler = (RecyclerView)findViewById(R.id.gistsRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gistRecycler.setLayoutManager(linearLayoutManager);

        mainPresenter = new MainPresenter(this);

        //SwipeRefresh
        swipeRefreshLayout
                = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainPresenter.refreshItems(false);
            }
        });

        showToolbar(getResources().getString(R.string.toolbar_title_mainactivity), false);
    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onDestroy() {
        mainPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void bindAdapterData(ArrayList<Gist> bindDataBaseGists) {
        if ( bindDataBaseGists != null) {
            gistAdapterRecylerView
                    = new GistAdapterRecyclerView(bindDataBaseGists,
                    R.layout.cardview_gist, this);

            gistRecycler.setAdapter(gistAdapterRecylerView);
            gistRecycler.invalidate();
        }
    }

    @Override
    public void bindUpdateAdapterData(ArrayList<Gist> bindUpdateDataBaseGists) {
        if ( bindUpdateDataBaseGists != null )
        {
            gistAdapterRecylerView.upDateData(bindUpdateDataBaseGists);
            gistAdapterRecylerView.notifyDataSetChanged();
            gistRecycler.invalidate();

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
