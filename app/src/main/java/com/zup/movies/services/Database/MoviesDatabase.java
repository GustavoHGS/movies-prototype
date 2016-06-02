package com.zup.movies.services.Database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.zup.movies.services.Constants.GeneralConstants;

/**
 * Created by ghenrique on 31/05/16.
 */
@Database(name = MoviesDatabase.NAME, version = MoviesDatabase.VERSION)
public class MoviesDatabase {

    public static final String NAME = GeneralConstants.DATABASE_NAME;

    public static final int VERSION = 1;
}