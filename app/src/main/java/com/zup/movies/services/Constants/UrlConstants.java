package com.zup.movies.services.Constants;

/**
 * Created by ghenrique on 29/05/16.
 */
public class UrlConstants {
    public static String OMDB_API_URL_TITLE = "http://www.omdbapi.com/?t=|titulo|&y=|year|&plot=short&r=json";
    public static String OMDB_API_URL_SEARCH= "http://www.omdbapi.com/?s=|titulo|&y=|year|&plot=short&r=json";
    public static String OMDB_API_URL_ID = "http://www.omdbapi.com/?i=|id|&plot=full&r=json";


    public static String MOVIE_TITLE = "|titulo|";
    public static String MOVIE_YEAR= "|year|";
    public static String MOVIE_ID = "|id|";
}
