package com.ejemplo.album.controller;

import android.content.Context;

import com.ejemplo.album.dao.AlbumDAO;
import com.ejemplo.album.model.Album;

import java.util.List;

import util.HTTPConnectionManager;
import util.ResultListener;

/**
 * Created by Diagnostifo on 03/11/2016.
 */

public class AlbumController {

    public void getAlbums(Context context, final ResultListener<List<Album>> listenerAlbumDeLaView) {
        AlbumDAO albumDAO = new AlbumDAO(context);
        if (HTTPConnectionManager.isNetworkingOnline(context)) {
            albumDAO.getAlbumsFromWeb(new ResultListener<List<Album>>() {
                @Override
                public void finish(List<Album> listaDeAlbums) {
                    listenerAlbumDeLaView.finish(listaDeAlbums);
                }
            });
        } else {
            List<Album> listaDeAlbums = albumDAO.getAlbumsFromDataBase();
            listenerAlbumDeLaView.finish(listaDeAlbums);
        }
    }
}

