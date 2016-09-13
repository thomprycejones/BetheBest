package io.thomasprycejones.bethebestpogo.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import io.thomasprycejones.bethebestpogo.TipsAdapter;
import io.thomasprycejones.bethebestpogo.PrefManager;
import io.thomasprycejones.bethebestpogo.R;
import io.thomasprycejones.bethebestpogo.Tips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TipsFragment extends Fragment {

    PrefManager prefManager;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    String JsonURL = "https://jsonblob.com/api/jsonBlob/57d22f32e4b0dc55a4f40d41";
    private List tips;
    private RequestQueue requestQueue;
    public Activity applicationActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public TipsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getContext());
        tips = new ArrayList();

        loadJson();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new TipsAdapter(tips, getActivity());
        recycler.setAdapter(adapter);
        // Setear UI
        String team = prefManager.isWhatTeam();
        setTeamsAndColors(team);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshReciclador);
        createSwipeRefreshLayout();


        return view;
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

    //Reload fragment list
    private void reLoad(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void setActivity(Activity activity){
        this.applicationActivity = activity;
    }

    private void loadJson(){
        requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest arrayreq = new JsonArrayRequest(Request.Method.GET,JsonURL,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray arrayNews) {
                        try {
                            for (int i = 0; i < arrayNews.length(); i++) {
                                JSONObject jb =(JSONObject) arrayNews.get(i);
                                String title = jb.getString("title");
                                String abstractnews = jb.getString("abstract");
                                String img = jb.getString("img");
                                String timestamp = jb.getString("timestamp");
                                String url = jb.getString("url");
                                tips.add(new Tips(getContext(), title, abstractnews, img, timestamp, url));
                            }
                            reLoad();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(arrayreq);
    }

    private void createSwipeRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }
    void refreshItems() {
        tips.clear(); // no es muy eficiente, habría que guardar lo que ya hay y agregar lo nuevo
        loadJson();
        onItemsLoadComplete();
    }//Fix refresh no optimizado
    void onItemsLoadComplete() {
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
