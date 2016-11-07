package com.ejemplo.album.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diagnostifo on 03/11/2016.
 */

public class AlbumContainer extends ArrayList<Album> {
    private List listaDeAlbums;


    public List getListaDeAlbums() {
        return listaDeAlbums;
    }

    public void setListaDeAlbums(List listaDeAlbums) {
        this.listaDeAlbums = listaDeAlbums;
    }
}
