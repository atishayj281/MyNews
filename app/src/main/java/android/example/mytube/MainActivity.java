package android.example.mytube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements newsItemClicked{

    static int socialcount = 0;
    static int techcount = 0;
    static int healthcount = 0;
    static int educount = 0;
    ProgressBar progressBar;
    ProgressBar progressBar2;
    private String OldCategory = "health";

    private NewsListAdapter adapter = new NewsListAdapter(MainActivity.this, this);
    RecyclerView recyclerView;
    Context main = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        toolbar.setTitle("MyNews");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetch("health",1);
        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);
        TextView text1 = findViewById(R.id.health);
        text1.setBackgroundResource(R.drawable.after_click);
        text1.setTextColor(Color.parseColor("White"));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                     if(OldCategory.equals("Education")){
                         fetch(OldCategory, educount++);
                     } else if(OldCategory.equals("Health")){
                         fetch(OldCategory, healthcount++);
                     } else if(OldCategory.equals("Social")){
                         fetch(OldCategory, socialcount++);
                     } else if(OldCategory.equals("Technology")){
                         fetch(OldCategory, techcount++);
                     }
                }
            }
        });
    }



    void fetch(String query, int count){

        String url = "https://newsdata.io/api/1/news?apikey=pub_28413e324db50c203ecd3b0621c189b0ace&q="+query+"&page="+count;

        RequestQueue queue = Volley.newRequestQueue(this);
        ArrayList<News> newsArray = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newjsonArray = response.getJSONArray("results");
                            for(int i = 0; i<10000; i++){
                                JSONObject newJsonObject = newjsonArray.getJSONObject(i);
                                News news = new News(newJsonObject.getString("title"),
                                        newJsonObject.getString("description"),
                                        newJsonObject.getString("link"),
                                        newJsonObject.getString("image_url"));
                                newsArray.add(news);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if(count == 1){
                                adapter.updateNews(newsArray);
                                progressBar.setVisibility(View.GONE);
                            }else {
                                progressBar2.setVisibility(View.VISIBLE);
                                adapter.update(newsArray);
                                progressBar2.setVisibility(View.GONE);

                            }
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(main, "error", Toast.LENGTH_SHORT);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        queue.add(jsonObjectRequest);
    }


    public void change(View view) {

        int id = view.getId();
        TextView text1 = findViewById(id);
        TextView edu = findViewById(R.id.Education);
        TextView head = findViewById(R.id.social);
        TextView health = findViewById(R.id.health);
        TextView tech = findViewById(R.id.Technology);

        String click = text1.getText().toString();

        edu.setBackgroundResource(R.drawable.shape);
        edu.setTextColor(Color.parseColor("Black"));
        head.setBackgroundResource(R.drawable.shape);
        head.setTextColor(Color.parseColor("Black"));
        health.setBackgroundResource(R.drawable.shape);
        health.setTextColor(Color.parseColor("Black"));
        tech.setBackgroundResource(R.drawable.shape);
        tech.setTextColor(Color.parseColor("Black"));
        text1.setBackgroundResource(R.drawable.after_click);
        text1.setTextColor(Color.parseColor("White"));

        if(click.equals(OldCategory)){
            fetch(click, 1);
        } else{
            if(click.equals("Technology")){
                techcount = 2;
                OldCategory = "Technology";
            } else if(click.equals("Social")){
                socialcount = 2;
                OldCategory = "Social";
            } else if(click.equals("Health")){
                healthcount = 2;
                OldCategory = "Health";
            } else{
                educount++;
                OldCategory = "Education";
            }
            if(progressBar.getVisibility() == View.GONE){
                progressBar.setVisibility(View.VISIBLE);

            }
            fetch(click, 1);
        }


    }

    @Override
    public void onItemClicked(News item) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        if(item.url != null){
            customTabsIntent.launchUrl(this, Uri.parse(item.url));
        }
        else{
            Toast.makeText(main, "Link is not available", Toast.LENGTH_SHORT).show();
        }
    }
}