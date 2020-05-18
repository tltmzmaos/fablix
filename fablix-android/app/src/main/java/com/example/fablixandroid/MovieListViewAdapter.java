package com.example.fablixandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.fablixandroid.R;

import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;

    public MovieListViewAdapter(ArrayList<Movie> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Movie movie = movies.get(position);

        TextView title = view.findViewById(R.id.title);
        TextView year = view.findViewById(R.id.year);
        TextView director = view.findViewById(R.id.director);
        TextView genre = view.findViewById(R.id.genre);
        TextView star = view.findViewById(R.id.star);

        title.setText(movie.getName());
        year.setText(movie.getYear());
        director.setText(movie.getDirector());

        String genre_str = "Genres: ";
        if(movie.getGenre().length < 1){
            genre_str += "N/A";
        } else if(movie.getGenre().length > 3){
            for(int i=0; i<3; i++){
                genre_str += movie.getGenre()[i];
                if(i < 2){
                    genre_str += ", ";
                }
            }
        }else{
            for(int i=0; i<movie.getGenre().length; i++){
                genre_str += movie.getGenre()[i];
                if(i < movie.getGenre().length-1){
                    genre_str += ", ";
                }
            }
        }
        genre.setText(genre_str);

        String star_str = "Stars: ";
        if(movie.getStar().length < 1){
            star_str += "N/A";
        } else if(movie.getStar().length > 3){
            for(int i=0; i<3; i++){
                star_str += movie.getStar()[i];
                if(i < 2){
                    star_str += ", ";
                }
            }
        }else{
            for(int i=0; i<movie.getStar().length; i++){
                star_str += movie.getStar()[i];
                if(i < movie.getStar().length-1){
                    star_str += ", ";
                }
            }
        }
        star.setText(star_str);

        return view;
    }

}
