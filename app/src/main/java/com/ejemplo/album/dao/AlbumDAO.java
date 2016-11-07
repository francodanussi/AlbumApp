package com.ejemplo.album.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.ejemplo.album.model.Album;
import com.ejemplo.album.model.AlbumContainer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import util.DAOException;
import util.HTTPConnectionManager;
import util.ResultListener;

/**
 * Created by Diagnostifo on 03/11/2016.
 */

public class AlbumDAO extends SQLiteOpenHelper {

    private static final String DATABASENAME = "AlbumDB";
    private static final int DATABASEVERSION = 1;
    private static final String TABLEALBUMS = "TableAlbums";
    private static final String ALBUMID = "AlbumId";
    private static final String ID = "Id";
    private static final String TITLE = "Title";
    private static final String URL = "URL";
    private static final String THUMBNAILURL = "ThumbnailURL";

    public AlbumDAO(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE_TABLE " + TABLEALBUMS + "("
                + ALBUMID + "INTEGER, "
                + ID + " INTEGER PRIMARY KEY, "
                + TITLE + " TEXT, "
                + URL + " TEXT, "
                + THUMBNAILURL + " TEXT, "
                + ")";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void getAlbumsFromWeb(ResultListener<List<Album>> listener) {
        RetrieveFeedTaskAlbum retrieveFeedTask = new RetrieveFeedTaskAlbum(listener);
        retrieveFeedTask.execute();
    }

    private Boolean checkIfExist(Integer unID) {

        SQLiteDatabase database = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLEALBUMS
                + " WHERE " + ID + "==" + unID;

        Cursor result = database.rawQuery(selectQuery, null);
        Integer count = result.getCount();

        Log.v("AlbumDAO", "El Album " + unID + " ya está en la base");

        database.close();

        return (count > 0);
    }

    public void addAlbums(List<Album> listaDeAlbums) {
        for (Album unAlbum : listaDeAlbums) {
            if(!checkIfExist(unAlbum.getId())) {
                this.addAlbum(unAlbum);
            }
        }
    }

    public void addAlbum(Album unAlbum) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues row = new ContentValues();


        row.put(ALBUMID, unAlbum.getAlbumId());
        row.put(TITLE, unAlbum.getTitle());
        row.put(ID, unAlbum.getId());
        row.put(URL, unAlbum.getUrl());
        row.put(THUMBNAILURL, unAlbum.getThumbnailUrl());


        database.insert(TABLEALBUMS, null, row);

        database.close();
    }

    public List<Album> getAlbumsFromDataBase () {

        SQLiteDatabase database = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLEALBUMS;
        Cursor cursor = database.rawQuery(selectQuery, null);

        List<Album> listaDeAlbums = new ArrayList<>();
        while (cursor.moveToNext()) {

            Integer unAlbumId = cursor.getInt(cursor.getColumnIndex(ALBUMID));
            Integer unId = (cursor.getInt(cursor.getColumnIndex(TITLE)));
            String unTitle = (cursor.getString(cursor.getColumnIndex(TITLE)));
            String unUrl = (cursor.getString(cursor.getColumnIndex(URL)));
            String unThumbnailUrl = (cursor.getString(cursor.getColumnIndex(THUMBNAILURL)));

            Album unAlbum = new Album(unAlbumId, unId, unTitle, unUrl, unThumbnailUrl);

            listaDeAlbums.add(unAlbum);

        }
        cursor.close();
        database.close();
        return listaDeAlbums;
    }


    class RetrieveFeedTaskAlbum extends AsyncTask<String, Void, List<Album>> {
        private ResultListener<List<Album>> listener;

        public void setListener(ResultListener<List<Album>> listener) {
            this.listener = listener;
        }

        public RetrieveFeedTaskAlbum(ResultListener<List<Album>> listener) {
            this.listener = listener;
        }

        @Override
        protected List<Album> doInBackground(String... params) {


            HTTPConnectionManager connectionManager = new HTTPConnectionManager();
            String input = null;

            try {
                input = connectionManager.getRequestString("https://api.myjson.com/bins/25hip");
            } catch (DAOException e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            AlbumContainer albumContainer = gson.fromJson(input, AlbumContainer.class);

            return albumContainer.getListaDeAlbums();

        }

        @Override
        protected void onPostExecute(List<Album> listaDeAlbums) {
            addAlbums(listaDeAlbums);
            this.listener.finish(listaDeAlbums);
        }
    }
    }

