package android.example.mytube;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewholder>  {

    ArrayList<News> items = new ArrayList<>();
    Context context;
    newsItemClicked listner;

    NewsListAdapter(Context context, newsItemClicked listner){
        this.context = context;
        this.listner = listner;
    }

    NewsViewholder newsViewholder;

    void updateNews(ArrayList<News> updatedNews){
        items.clear();
        items.addAll(updatedNews);
        notifyDataSetChanged();
    }

    void update(ArrayList<News> updatedNews){
        items.addAll(updatedNews);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public NewsViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnews, parent, false);
        return new NewsViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsListAdapter.NewsViewholder holder, int position) {
        News s = items.get(position);
        holder.titleView.setText(s.title);
        holder.descr.setText(s.descr);
        Glide.with(holder.itemView).load(s.imageUrl).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    public class NewsViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleView;
        ImageView img;
        TextView descr;

        public NewsViewholder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.image);
            descr = itemView.findViewById(R.id.description);
            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = this.getAdapterPosition();
            listner.onItemClicked(items.get(pos));
        }
    }


}



interface newsItemClicked{
    void onItemClicked(News item);
}