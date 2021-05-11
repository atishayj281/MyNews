package android.example.mytube;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
public class NewsListAdapter extends RecyclerView.Adapter<NewsViewholder>  {

    ArrayList<News> items = new ArrayList<>();
    Context context;
    newsItemClicked listner;

    NewsListAdapter(Context context, newsItemClicked listner){
        this.context = context;
        this.listner = listner;
    }

    NewsViewholder newsViewholder;

    @NonNull
    @Override
    public NewsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnews, parent, false);
        newsViewholder = new NewsViewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("url", "" + newsViewholder.getLayoutPosition());
                listner.onItemClicked(items.get(newsViewholder.getLayoutPosition()));

            }
        });
        return newsViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewholder holder, int position) {
        News s = items.get(position);
        holder.titleView.setText(s.title);
        holder.descr.setText(s.descr);
        Glide.with(holder.itemView).load(s.imageUrl).into(holder.img);


    }

    void updateNews(ArrayList<News> updatedNews){
        items.clear();
        items.addAll(updatedNews);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return items.size();
    }



}

class NewsViewholder extends RecyclerView.ViewHolder{
    TextView titleView;
    ImageView img;
    TextView descr;

    public NewsViewholder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        img = itemView.findViewById(R.id.image);
        descr = itemView.findViewById(R.id.description);
    }
}

interface newsItemClicked{
    void onItemClicked(News item);
}