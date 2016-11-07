package com.ejemplo.album.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ejemplo.album.R;
import com.ejemplo.album.model.Album;

import java.util.List;

/**
 * Created by Diagnostifo on 03/11/2016.
 */

public class AdapterAlbum extends RecyclerView.Adapter {

    private List<Album> listaDeAlbums;
    private Context unContext;

    public AdapterAlbum(Context context) {
        this.unContext = context;
    }

    public void setListaDeAlbums(List<Album> listaDeAlbums) {
        this.listaDeAlbums = listaDeAlbums;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_celda, parent, false);
        AlbumViewHolder unAlbumViewHolder = new AlbumViewHolder(itemView);
        return unAlbumViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Album unAlbum = listaDeAlbums.get(position);
        AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
        albumViewHolder.cargarAlbum(unAlbum, unContext);
    }

    @Override
    public int getItemCount() {
        return listaDeAlbums.size();
    }


    private static class AlbumViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewTitle;
        private ImageView imageViewThumbnail;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            imageViewThumbnail = (ImageView) itemView.findViewById(R.id.imageViewThumbnail);
        }

        public void cargarAlbum(Album unAlbum, Context unContext){
            textViewTitle.setText(unAlbum.getTitle());
            Glide.with(unContext).
                    load(unAlbum.getThumbnailUrl()).
                    listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).centerCrop().
                    into(imageViewThumbnail);
        }
    }
}
