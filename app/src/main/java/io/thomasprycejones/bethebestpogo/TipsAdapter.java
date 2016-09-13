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
public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder>{
    private List<Tips> items;
    private Activity context;
    private String team;
    private PrefManager prefManager;
    int tabColor;

    public static class TipsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public CardView tipscard_cardview;
        public ImageView tipscard_img;
        public TextView tipscard_titulo;
        public TextView tipscard_abstract;
        public TextView tipscard_timestamp;
        public TextView tipscard_source;


        public TipsViewHolder(final View v, final Activity context) {
            super(v);
            tipscard_cardview  = (CardView) v.findViewById(R.id.tipscard_cardview);
            tipscard_img = (ImageView) v.findViewById(R.id.tipscard_image);
            tipscard_titulo = (TextView) v.findViewById(R.id.tipscard_titulo);
            tipscard_abstract = (TextView) v.findViewById(R.id.tipscard_abstract);
            tipscard_timestamp = (TextView) v.findViewById(R.id.tipscard_timestamp);
            tipscard_source = (TextView) v.findViewById(R.id.tipscard_fuente);


        }
    }

    public TipsAdapter(List<Tips> items, Activity context) {
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
    public TipsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tips_card, viewGroup, false);

        return new TipsViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(TipsViewHolder viewHolder, final int i) {
        Picasso.with(context).load(items.get(i).getImg()).into(viewHolder.tipscard_img);
        viewHolder.tipscard_titulo.setText(items.get(i).getTitulo());
        viewHolder.tipscard_abstract.setText(items.get(i).getabstractTip());
        viewHolder.tipscard_timestamp.setText(items.get(i).getTimestamp());
        final String url = items.get(i).getUrl();

        final String team = prefManager.isWhatTeam();

        viewHolder.tipscard_cardview.setOnClickListener(new View.OnClickListener() {
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

