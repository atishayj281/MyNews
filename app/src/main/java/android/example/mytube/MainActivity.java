package android.example.mytube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements newsItemClicked{

    static int socialcount = 0;
    static int techcount = 0;
    static int healthcount = 0;
    static int educount = 0;

    private NewsListAdapter adapter = new NewsListAdapter(MainActivity.this, this);
    RecyclerView recyclerView;
    Context main = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetch("health", healthcount);
        TextView text1 = findViewById(R.id.health);
        text1.setBackgroundResource(R.drawable.after_click);
        text1.setTextColor(Color.parseColor("White"));
        recyclerView.setAdapter(adapter);

    }

    void fetch(String query, int count){

        String url = "https://newsdata.io/api/1/news?apikey=pub_104fe4c3e8aa6cbaf9e33d0b5968c23e894&country=in&page=" + count + "&q="+ query;

        RequestQueue queue = Volley.newRequestQueue(this);
        ArrayList<News> newsArray = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newjsonArray = response.getJSONArray("results");
                            Toast.makeText(main, "" + newjsonArray.length(), Toast.LENGTH_SHORT).show();
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
                            adapter.updateNews(newsArray);
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(main, "error", Toast.LENGTH_SHORT);
                    }
                });
        queue.add(jsonObjectRequest);
    }


    public void change(View view) {

        int id = view.getId();
        TextView text1 = findViewById(id);
        TextView edu = findViewById(R.id.Educatation);
        TextView head = findViewById(R.id.social);
        TextView health = findViewById(R.id.health);
        TextView tech = findViewById(R.id.Technology);

//        ArrayList<TextView> textViews = new ArrayList<>();
//        textViews.add(edu);
//        textViews.add(head);
//        textViews.add(health);
//        textViews.add(tech);
        String a = text1.getText().toString();

//        for(int i= 0; i<4; i++){
//            if(textViews.get(i).getText().toString().equals(a)){
//                textViews.get(i).setBackgroundResource(R.drawable.after_click);
//            }
//            textViews.get(i).setBackgroundResource(R.drawable.shape);
//
//        }

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

        Toast.makeText(main, a, Toast.LENGTH_SHORT).show();
        if(a.equals("Technology")){
            techcount++;
            fetch(a, techcount);
        } else if(a.equals("Social")){
            socialcount++;
            fetch(a, socialcount);
        } else if(a.equals("Health")){
            healthcount++;
            fetch(a, healthcount);
        } else{
            educount++;
            fetch(a, educount);
        }
    }

    @Override
    public void onItemClicked(News item) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}