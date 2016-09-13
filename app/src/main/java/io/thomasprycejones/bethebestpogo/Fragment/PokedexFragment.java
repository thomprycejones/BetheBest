package io.thomasprycejones.bethebestpogo.Fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Request;

import io.thomasprycejones.bethebestpogo.Pokemon;
import io.thomasprycejones.bethebestpogo.PokemonAdapter;
import io.thomasprycejones.bethebestpogo.PrefManager;
import io.thomasprycejones.bethebestpogo.R;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.thomasprycejones.bethebestpogo.R.id.fab_reciclador;
import static io.thomasprycejones.bethebestpogo.R.id.fab_search;


public class PokedexFragment extends Fragment {

    PrefManager prefManager;
    private RecyclerView recycler;
    private PokemonAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    String JsonURL = "https://jsonblob.com/api/jsonBlob/57cf8ff7e4b0dc55a4f359b6";
    private List pokemones;
    private ArrayList<Pokemon> pokemonesBackup;
    private JSONObject pokedex;
    private RequestQueue requestQueue;

    public Boolean click = false;

    public PokedexFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getContext());
        pokemones = new ArrayList();
        pokemonesBackup = new ArrayList();

        instantiatePokemon();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new PokemonAdapter(pokemones, getActivity());
        recycler.setAdapter(adapter);
        // Setear UI
        String team = prefManager.isWhatTeam();
        setTeamsAndColors(team);

        //Fab y listener scroll recycleview
        instantiateFAB(view);

        return view;
    }


    public String loadJSONFromAsset(String source) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(source);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    //Set UI things
    private void setTeamsAndColors(String team){
        switch (team) {
            case "Valor":
                int myColor_valor = getResources().getColor(R.color.ValorBG);
                recycler.setBackgroundColor(myColor_valor);
                break;
            case "Mystic":
                int myColor_mystic = getResources().getColor(R.color.MysticBG);
                recycler.setBackgroundColor(myColor_mystic);
                break;
            case "Instinct":
                int myColor_instinct = getResources().getColor(R.color.InstinctBG);
                recycler.setBackgroundColor(myColor_instinct);
                break;
            default:
                recycler.setBackgroundColor(Color.GRAY);
                break;
        }
    }


    private void instantiatePokemon(){
        try {
            JSONArray arrayPokemon = new JSONArray(loadJSONFromAsset("pokedex.json"));
            for (int i = 0; i < arrayPokemon.length(); i++) {
                JSONObject jb =(JSONObject) arrayPokemon.get(i);
                int id = jb.getInt("id");
                String name = jb.getString("name");
                String number = jb.getString("num");
                String img = jb.getString("img");
                int att = jb.getInt("attack");
                int def = jb.getInt("defense");
                int sta = jb.getInt("stamina");

                JSONArray weaktemp = jb.getJSONArray("weaknesses");
                String [] weak = new String [weaktemp.length()];
                for (int j = 0; j < weak.length; j++) {
                    weak[j] = weaktemp.getString(j);
                }

                Double [] mult;
                try{
                    JSONArray multtemp = jb.getJSONArray("multipliers");
                    mult = new Double [multtemp.length()];
                    for (int j = 0; j < mult.length; j++) {
                        mult[j] = multtemp.getDouble(j);
                    }
                }
                catch (Exception e){
                    mult = new Double [1];
                    if(jb.isNull("multipliers")){
                        mult = null;
                    }
                    else{mult[0] = jb.getDouble("multipliers");}
                }


                String types = jb.getString("type");
                if(types.contains("/"))
                {
                    String[] artypes = types.split("/");
                    String type1_space = artypes[0];
                    String type2_space = artypes[1];
                    String type1 = type1_space.replaceAll("\\s+","");
                    String type2 = type2_space.replaceAll("\\s+","");
                    pokemones.add(new Pokemon(getContext(),id, name, number, type1, type2, img, 12,att,def,sta,weak,mult));
                    pokemonesBackup.add(new Pokemon(getContext(),id, name, number, type1, type2, img, 12,att,def,sta,weak,mult));
                }
                else
                {
                    pokemones.add(new Pokemon(getContext(),id, name, number, types, "", img, 12,att,def,sta,weak,mult));
                    pokemonesBackup.add(new Pokemon(getContext(),id, name, number, types, "", img, 12,att,def,sta,weak,mult));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void instantiateFAB(View view){
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(fab_reciclador);
        final EditText searchInput = (EditText) view.findViewById(fab_search);
        Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                android.R.interpolator.fast_out_slow_in);
        searchInput.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(5)
                .setInterpolator(interpolador)
                .start();
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click = !click;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                            android.R.interpolator.fast_out_slow_in);

                    view.animate()
                            .rotation(click ? 360f : 0)
                            .setInterpolator(interpolador)
                            .start();
                }
                if(searchInput.getVisibility() == View.GONE){
                    Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                            android.R.interpolator.fast_out_slow_in);
                    searchInput.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(500)
                            .setInterpolator(interpolador)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    searchInput.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {

                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })
                            .start();
                }
                else{
                    Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                            android.R.interpolator.fast_out_slow_in);
                    searchInput.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .setDuration(500)
                            .setInterpolator(interpolador)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    searchInput.setText("");
                                    hideKeyboard(getContext());
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    searchInput.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            })
                            .start();
                }
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String input = String.valueOf(s);
                filter((String) input);
                adapter = new PokemonAdapter(pokemones, getActivity());
                recycler.setAdapter(adapter);
            }
        });
    }


    public void filter(String text) {
        if(text.isEmpty()){
            pokemones.clear();
            pokemones.addAll(pokemonesBackup);
        } else{
            ArrayList<Pokemon> result = new ArrayList<>();
            text = text.toLowerCase();
            for(Pokemon item: pokemonesBackup){
                if(item.getName().toLowerCase().contains(text) || item.getNumber().toLowerCase().contains(text)
                        || item.getType1().toLowerCase().contains(text) || item.getType2().toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            pokemones.clear();
            pokemones.addAll(result);
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}
