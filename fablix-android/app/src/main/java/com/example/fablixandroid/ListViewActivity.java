package com.example.fablixandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fablixandroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.util.ArrayList;


public class ListViewActivity extends Activity{

    public int totalPage;
    public int num_item_page = 20;
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        //String url = "https://10.0.2.2:8443/fablix_pj_war_exploded/search?searchBar=";
        String url = "https://ec2-13-57-191-101.us-west-1.compute.amazonaws.com:8443/fablix-pj/search?searchBar=";
        //this should be retrieved from the database and the backend server

        RequestQueue mQueue = Volley.newRequestQueue(this);
        url += String.valueOf(getIntent().getExtras().get("query"));

        pageNum = 0;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        ArrayList<Movie> list = new ArrayList<Movie>();

                        for(int i=0; i<jsonArray.length(); i++){
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("movie_id");
                                String title = jsonObject.getString("movie_title");
                                String year = jsonObject.getString("movie_year");
                                String director = jsonObject.getString("movie_director");
                                String genre = jsonObject.getString("movie_genres");
                                String[] genres = genre.split(",");
                                String starId = jsonObject.getString("star_id");
                                String[] starIdArray = starId.split(",");
                                String star = jsonObject.getString("movie_stars");
                                String[] stars = star.split(",");
                                String rating = jsonObject.getString("movie_rating");
                                list.add(new Movie(id, title, year, director, genres, stars));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        totalPage = (int) Math.ceil(list.size() / num_item_page);
                        activateButtons(list);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(jsonArrayRequest);
    }

    public void updateList(ArrayList<Movie> list){
        MovieListViewAdapter adapter = new MovieListViewAdapter(list, this);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = list.get(position);
                Intent intnt = new Intent(getApplicationContext(), SingleMovie.class);
                intnt.putExtra("singleMovie", movie.getId());
                startActivity(intnt);
            }
        });
    }

    public void activateButtons(ArrayList<Movie> list){
        Button prev = findViewById(R.id.prev);
        Button next = findViewById(R.id.next);
        TextView pg = findViewById(R.id.pg);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNum > 0){
                    pageNum--;
                    Log.d("prevButtonclicked", String.valueOf(pageNum));
                    ArrayList<Movie> pl = new ArrayList<>();
                    for(int i=0; i<num_item_page; i++){
                        if((pageNum*num_item_page+i) < list.size()){
                            pl.add(list.get(pageNum*num_item_page+i));
                        }
                    }
                    pg.setText(pageNum+1 + " of " + totalPage);
                    updateList(pl);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNum < totalPage-1){
                    pageNum++;
                    Log.d("nextButtonclicked", String.valueOf(pageNum));
                    ArrayList<Movie> pl = new ArrayList<>();
                    for(int i=0; i<num_item_page; i++){
                        if((pageNum*num_item_page+i) < list.size()){
                            pl.add(list.get(pageNum*num_item_page+i));
                        }
                    }
                    pg.setText(pageNum+1 + " of " + totalPage);
                    updateList(pl);
                }
            }
        });
        ArrayList<Movie> pl = new ArrayList<>();
        for(int i=0; i<num_item_page; i++){
            if((pageNum*num_item_page+i) < list.size()){
                pl.add(list.get(pageNum*num_item_page+i));
            }
        }
        pg.setText(pageNum+1 + " of " + totalPage);
        updateList(pl);
    }

}
