package com.zup.movies.services.FavoritesManger;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.zup.movies.R;
import com.zup.movies.model.Movie;
import com.zup.movies.services.Constants.GeneralConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ghenrique on 02/06/16.
 */
public class FavoritesDAOUtils {
    private Context context;

    public FavoritesDAOUtils(Context context){
        this.context = context;

    }

    public Bitmap getImageFromStorage(Movie movie){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir(GeneralConstants.DIRECTORY_PATH, Context.MODE_PRIVATE);
        return loadImageFromStorage(directory.getPath(),movie.getId());
    }

    private Bitmap loadImageFromStorage(String path,String id)
    {
        try {
            File f=new File(path,id+".jpg" );
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;

    }
}
