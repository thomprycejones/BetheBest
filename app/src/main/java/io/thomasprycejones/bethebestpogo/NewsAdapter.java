package io.thomasprycejones.bethebestpogo;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import io.thomasprycejones.*;


import java.io.File;
import java.util.List;

/**
 * Created by Thomas Pryce Jones on 07-09-2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private List<News> items;
    private Activity context;
    private String team;
    private PrefManager prefManager;
    int tabColor;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public CardView newscard_cardview;
        public ImageView newscard_img;
        public TextView newscard_titulo;
        public TextView newscard_abstract;
        public TextView newscard_timestamp;
        public TextView newscard_source;


        public NewsViewHolder(final View v, final Activity context) {
            super(v);
            newscard_cardview  = (CardView) v.findViewById(R.id.newscard_cardview);
            newscard_img = (ImageView) v.findViewById(R.id.newscard_image);
            newscard_titulo = (TextView) v.findViewById(R.id.newscard_titulo);
            newscard_abstract = (TextView) v.findViewById(R.id.newscard_abstract);
            newscard_timestamp = (TextView) v.findViewById(R.id.newscard_timestamp);
            newscard_source = (TextView) v.findViewById(R.id.newscard_fuente);


        }
    }

    public NewsAdapter(List<News> items, Activity context) {
        this.items = items;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    public void setContext(Activity context){
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_card, viewGroup, false);

        return new NewsViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder viewHolder, final int i) {
        Picasso.with(context).load(items.get(i).getImg()).into(viewHolder.newscard_img);
        viewHolder.newscard_titulo.setText(items.get(i).getTitulo());
        viewHolder.newscard_abstract.setText(items.get(i).getAbstractnew());
        viewHolder.newscard_source.setText(items.get(i).getSource());
        viewHolder.newscard_timestamp.setText(items.get(i).getTimestamp());
        final String url = items.get(i).getUrl();

        final String team = prefManager.isWhatTeam();

        viewHolder.newscard_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                switch(team){
                    case "Valor":
                        builder.setToolbarColor(context.getResources().getColor(R.color.ValorStatusBar));
                        break;
                    case "Mystic":
                        builder.setToolbarColor(context.getResources().getColor(R.color.MysticStatusBar));
                        break;
                    case "Instinct":
                        builder.setToolbarColor(context.getResources().getColor(R.color.InstinctStatusBar));
                        break;
                    default:
                        builder.setToolbarColor(context.getResources().getColor(R.color.bug_dark));
                        break;
                }
                customTabsIntent.launchUrl(context, Uri.parse(url));
            }
        });
    }
}
