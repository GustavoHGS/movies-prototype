package com.zup.movies.model;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zup.movies.services.Constants.GeneralConstants;
import com.zup.movies.services.Database.MoviesDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ghenrique on 31/05/16.
 */
@Table(database = MoviesDatabase.class)
public class Movie extends BaseModel {

    @Column
    @PrimaryKey
    private String id;
    @Column
    private String title;
    @Column
    private String year;
    @Column
    private String rated;
    @Column
    private String released;
    @Column
    private String runtime;
    @Column
    private String genre;
    @Column
    private String plot;
    @Column
    private String posterUrl;
    @Column
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void shortExtract(JSONObject movie) throws JSONException{
        this.setId(movie.getString("imdbID"));
        this.setTitle(movie.getString("Title"));
        this.setYear("Ano: "+movie.getString("Year"));
        String type = movie.getString("Type");
        type = Character.toString(type.charAt(0)).toUpperCase()+type.substring(1);
        this.setType(type);
        this.setGenre("Ação");
        this.setPlot("Description");
        this.setPosterUrl(movie.getString("Poster"));

    }

    public void fullExtract(JSONObject movie) throws JSONException{
        this.setId(movie.getString("imdbID"));
        this.setTitle(movie.getString("Title"));
        this.setGenre("Gênero: "+movie.getString("Genre"));
        this.setPlot("Resumo: "+movie.getString("Plot"));
        this.setYear("Ano: "+movie.getString("Year"));
        String type = movie.getString("Type");
        type = Character.toString(type.charAt(0)).toUpperCase()+type.substring(1);
        this.setType("Tipo: "+type);
        this.setRuntime("Duração: "+movie.getString("Runtime"));
        this.setReleased(movie.getString("Released"));
        this.setRated(movie.getString("Rated"));
        this.setPosterUrl(movie.getString("Poster"));

    }


    public void storeUserPicture(Context context) {
        try {
            URL url = new URL(this.getPosterUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            saveToInternalStorage(myBitmap, context);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage,Context context) throws Exception{
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(GeneralConstants.DIRECTORY_PATH, Context.MODE_PRIVATE);
        File mypath=new File(directory,this.getId()+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }
}