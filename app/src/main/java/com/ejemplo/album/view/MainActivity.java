package com.ejemplo.album.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ejemplo.album.R;
import com.ejemplo.album.controller.AlbumController;
import com.ejemplo.album.model.Album;

import java.util.List;

import util.ResultListener;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterAlbum adapterAlbum;
    AlbumController albumController;
    Context context;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        pullToRefresh = (SwipeRefreshLayout)findViewById(R.id.pullToRefresh);

        adapterAlbum = new AdapterAlbum(getApplicationContext());
        recyclerView.setAdapter(adapterAlbum);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        albumController = new AlbumController();

        update();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });

    }

    private void update() {
        pullToRefresh.setRefreshing(true);

        albumController.getAlbums(context, new ResultListener<List<Album>>() {
            @Override
            public void finish(List<Album> listaDeAlbums) {

                adapterAlbum.setListaDeAlbums(listaDeAlbums);

                adapterAlbum.notifyDataSetChanged();

                pullToRefresh.setRefreshing(false);
            }});
    }
    }











