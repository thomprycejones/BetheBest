package io.thomasprycejones.bethebestpogo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Pryce Jones on 18-08-2016.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private List<Pokemon> items;
    private Activity context;

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public CardView pokemoncard_cardview;
        public RelativeLayout pokemoncard_background;
        public RelativeLayout pokemoncard_normal;
        public RelativeLayout pokemoncard_expanded;
        public ImageView pokemoncard_img;
        public TextView pokemoncard_name;
        public TextView pokemoncard_number;
        public ImageView pokemoncard_type1;
        public ImageView pokemoncard_type2;
        public TextView pokemoncardextended_name;
        public TextView pokemoncardextended_number;
        public ImageView pokemoncardextended_type1;
        public ImageView pokemoncardextended_type2;
        public LinearLayout pokemoncardextended_layoutevolve;
        public Button pokemoncardextended_buttonevolve;
        public Button pokemoncardextended_layoutevolve_buttoncalculate;
        public EditText pokemoncardextended_layoutevolve_cpinput;
        public LinearLayout pokemoncardextended_layoutevolve_results;
        public LinearLayout pokemoncardextended_layoutinformation;
        public LinearLayout pokemoncardextended_informationweakcontainer;
        public LinearLayout pokemoncardextended_informationweakcontainerplus;
        public Button pokemoncardextended_buttoninformation;


        public PokemonViewHolder(final View v, final Activity context) {
            super(v);
            pokemoncard_cardview = (CardView) v.findViewById(R.id.pokemoncard_cardview);
            pokemoncard_img = (ImageView) v.findViewById(R.id.pokemoncard_img);
            pokemoncard_name = (TextView) v.findViewById(R.id.pokemoncard_name);
            pokemoncard_number = (TextView) v.findViewById(R.id.pokemoncard_number);
            pokemoncard_type1 = (ImageView) v.findViewById(R.id.pokemoncard_type1);
            pokemoncard_type2 = (ImageView) v.findViewById(R.id.pokemoncard_type2);
            pokemoncard_background =  (RelativeLayout) v.findViewById(R.id.pokemoncard_background);
            pokemoncard_expanded =  (RelativeLayout) v.findViewById(R.id.pokemoncard_layoutexpanded);
            pokemoncard_normal =  (RelativeLayout) v.findViewById(R.id.pokemoncard_layoutnormal);
            pokemoncardextended_name = (TextView) v.findViewById(R.id.pokemoncardextended_name);
            pokemoncardextended_number = (TextView) v.findViewById(R.id.pokemoncardextended_number);
            pokemoncardextended_type1 = (ImageView) v.findViewById(R.id.pokemoncardextended_type1);
            pokemoncardextended_type2 = (ImageView) v.findViewById(R.id.pokemoncardextended_type2);
            pokemoncardextended_layoutevolve = (LinearLayout) v.findViewById(R.id.pokemoncard_layoutevolve);
            pokemoncardextended_buttonevolve = (Button) v.findViewById(R.id.pokemoncardextended_buttonevolve);
            pokemoncardextended_layoutevolve_buttoncalculate = (Button) v.findViewById(R.id.pokemoncard_layoutevolve_buttoncalculate);
            pokemoncardextended_buttoninformation = (Button) v.findViewById(R.id.pokemoncardextended_buttoninformation);
            pokemoncardextended_layoutinformation = (LinearLayout) v.findViewById(R.id.pokemoncardextended_layoutinformations);
            pokemoncardextended_informationweakcontainer = (LinearLayout) v.findViewById(R.id.information_weakcontainer);
            pokemoncardextended_informationweakcontainerplus = (LinearLayout) v.findViewById(R.id.information_weakcontainerplus);
            pokemoncardextended_layoutevolve_results = (LinearLayout) v.findViewById(R.id.pokemoncard_layoutevolve_results);
            pokemoncardextended_layoutevolve_cpinput = (EditText) v.findViewById(R.id.pokemoncard_layoutevolve_combat);

            final float scale = context.getResources().getDisplayMetrics().density;
            final int closed = (int) (130 * scale + 0.5f);
            final int open = (int) (400 * scale + 0.5f);


            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    toggleCardViewnHeight(v,  open, closed, pokemoncard_normal, pokemoncard_expanded);
                }
            });


        }
    }

    public PokemonAdapter(List<Pokemon> items, Activity context) {
        this.items = items;
        this.context = context;
    }

    public void setContext(Activity context){
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pokemon_card, viewGroup, false);
        return new PokemonViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder viewHolder, final int i) {

        //Handle some problems
        //region
        if(viewHolder.pokemoncardextended_layoutevolve_results.getChildCount() > 0) {
            viewHolder.pokemoncardextended_layoutevolve_results.removeAllViews();
        }
        if(viewHolder.pokemoncardextended_layoutevolve.getVisibility() == View.VISIBLE) {
            viewHolder.pokemoncardextended_layoutevolve.setVisibility(View.GONE);
        }
        if(viewHolder.pokemoncardextended_layoutinformation.getVisibility() == View.GONE) {
            viewHolder.pokemoncardextended_layoutinformation.setVisibility(View.VISIBLE);
        }
        //endregion

        //Load pokemon image
        //region
        Picasso.with(context).load(items.get(i).getImgAssets()).into(viewHolder.pokemoncard_img);
        //endregion

        //Set background colors
        //region
        viewHolder.pokemoncard_background.setBackgroundColor(items.get(i).getPokemonBackgroundColor());
        viewHolder.pokemoncard_normal.setBackgroundColor(items.get(i).getPokemonLayoutColor());
        viewHolder.pokemoncard_expanded.setBackgroundColor(items.get(i).getPokemonLayoutColor());
        //endregion

        //Set Types
        //region
        Picasso.with(context).load(items.get(i).getType1Url()).into(viewHolder.pokemoncard_type1);
        Picasso.with(context).load(items.get(i).getType2Url()).into(viewHolder.pokemoncard_type2);
        Picasso.with(context).load(items.get(i).getType1Url()).into(viewHolder.pokemoncardextended_type1);
        Picasso.with(context).load(items.get(i).getType2Url()).into(viewHolder.pokemoncardextended_type2);
        //endregion

        //Set pokemon info
        //region
        viewHolder.pokemoncard_name.setText(items.get(i).getName());
        viewHolder.pokemoncard_number.setText("#:"+String.valueOf(items.get(i).getNumber()));
        viewHolder.pokemoncardextended_name.setText(items.get(i).getName());
        viewHolder.pokemoncardextended_number.setText("#:"+String.valueOf(items.get(i).getNumber()));
        //endregion

        //Set weakness info
        //region
        if(viewHolder.pokemoncardextended_informationweakcontainer.getChildCount() > 0) {
            viewHolder.pokemoncardextended_informationweakcontainer.removeAllViews();
        }
        if(viewHolder.pokemoncardextended_informationweakcontainerplus.getChildCount() > 0) {
            viewHolder.pokemoncardextended_informationweakcontainerplus.removeAllViews();
        }

        String[] weaks = items.get(i).getWeaknessesUrl();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 0, 0);
        if (weaks.length>3)
        {
            viewHolder.pokemoncardextended_informationweakcontainerplus.setVisibility(View.VISIBLE);
            for(int w=0;w<weaks.length;w++){
                if (w<4){
                    ImageView temp= new ImageView(context);
                    temp.setLayoutParams(params);
                    Picasso.with(context).load(weaks[w]).resize(130, 60).into(temp);
                    viewHolder.pokemoncardextended_informationweakcontainer.addView(temp);
                }
                if (w>=4){
                    ImageView temp= new ImageView(context);
                    temp.setLayoutParams(params);
                    Picasso.with(context).load(weaks[w]).resize(130, 60).into(temp);
                    viewHolder.pokemoncardextended_informationweakcontainerplus.addView(temp);
                }
            }
        }
        else{
            for(int w=0;w<weaks.length;w++){
                ImageView temp= new ImageView(context);
                temp.setLayoutParams(params);
                Picasso.with(context).load(weaks[w]).resize(130, 60).into(temp);
                viewHolder.pokemoncardextended_informationweakcontainer.addView(temp);
            }
        }
        //endregion

        //Set Multipliers
        //region
        Double[] multipliers = items.get(i).getCpMultipliers();
        if (multipliers == null){
            viewHolder.pokemoncardextended_buttonevolve.setVisibility(View.GONE);
        }
        else{
            viewHolder.pokemoncardextended_buttonevolve.setVisibility(View.VISIBLE);
        }
        //endregion

        //Set button colors
        //region
        int colorbutton = (items.get(i).getPokemonButtonColor());
        ViewCompat.setBackgroundTintList(viewHolder.pokemoncardextended_buttonevolve,
                ContextCompat.getColorStateList(context,colorbutton));
        ViewCompat.setBackgroundTintList(viewHolder.pokemoncardextended_buttoninformation,
                ContextCompat.getColorStateList(context,colorbutton));
        ViewCompat.setBackgroundTintList(viewHolder.pokemoncardextended_layoutevolve_buttoncalculate,
                ContextCompat.getColorStateList(context,colorbutton));
        //endregion

        //Set button listeners
        //region
        setOnClickListeners(viewHolder, multipliers,items.get(i).getImgEvolution());
        //endregion

    }

    static private void toggleCardViewnHeight(View cardView, int height, int minHeight, RelativeLayout normal, RelativeLayout expanded) {

        if (cardView.getHeight() == minHeight) {


            expandView(cardView,height,expanded,normal);

        } else {
            collapseView(cardView,minHeight, normal,expanded);

        }
    }

    static public void collapseView(final View cardView, int minHeight, RelativeLayout normal, RelativeLayout expanded) {

        expanded.setVisibility(View.GONE);
        ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                minHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                layoutParams.height = val;
                cardView.setLayoutParams(layoutParams);

            }
        });
        anim.start();
        normal.setVisibility(View.VISIBLE);
    }

    static public void expandView(final View cardView, int height, RelativeLayout expanded, RelativeLayout normal) {

        normal.setVisibility(View.GONE);
        ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                height);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                layoutParams.height = val;
                cardView.setLayoutParams(layoutParams);
            }
        });
        anim.start();
        expanded.setVisibility(View.VISIBLE);

    }

    public void setOnClickListeners(final PokemonViewHolder viewHolder, final Double[] multipliers, final int evolution) {


        viewHolder.pokemoncardextended_buttonevolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.pokemoncardextended_layoutinformation.setVisibility(View.GONE);
                viewHolder.pokemoncardextended_layoutevolve.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.pokemoncardextended_buttoninformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.pokemoncardextended_layoutevolve.setVisibility(View.GONE);
                viewHolder.pokemoncardextended_layoutinformation.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.pokemoncardextended_layoutevolve_buttoncalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(viewHolder.pokemoncardextended_layoutevolve_results.getChildCount() > 0) {
                        viewHolder.pokemoncardextended_layoutevolve_results.removeAllViews();
                    }
                    ImageView temp= new ImageView(context);
                    Picasso.with(context).load(evolution).into(temp);
                    viewHolder.pokemoncardextended_layoutevolve_results.addView(temp);
                    Double cp = Double.parseDouble(viewHolder.pokemoncardextended_layoutevolve_cpinput.getText().toString());
                    if (multipliers.length == 2){
                        Double dmin = (multipliers[0]*cp);
                        Double dmax = (multipliers[1]*cp);
                        int max = dmax.intValue();
                        int min = dmin.intValue();
                        TextView textTemp = new TextView(context);
                        textTemp.setText(String.valueOf(min) + " - " + String.valueOf(max));
                        textTemp.setGravity(Gravity.CENTER_HORIZONTAL & Gravity.CENTER_VERTICAL);
                        textTemp.setTextColor(Color.WHITE);
                        textTemp.setTypeface(null, Typeface.BOLD);
                        textTemp.setTextSize(20f);
                        viewHolder.pokemoncardextended_layoutevolve_results.addView(textTemp);
                    }
                    else{
                        Double d = (multipliers[0]*cp);
                        int mult = d.intValue();
                        TextView textTemp = new TextView(context);
                        textTemp.setText(String.valueOf(mult));
                        textTemp.setGravity(Gravity.CENTER_HORIZONTAL & Gravity.CENTER_VERTICAL);
                        textTemp.setTextColor(Color.WHITE);
                        textTemp.setTypeface(null, Typeface.BOLD);
                        textTemp.setTextSize(20f);
                        viewHolder.pokemoncardextended_layoutevolve_results.addView(textTemp);
                    }
                }
                catch(Exception e){
                    Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }



}