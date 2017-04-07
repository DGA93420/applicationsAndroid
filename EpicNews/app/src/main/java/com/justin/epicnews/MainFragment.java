package com.justin.epicnews;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by lejus on 27/03/2017.
 * Ce Fragment contiendra le code qui génère les 3 XMLAsyncTask ainsi que la recyclerview
 */

public class MainFragment extends Fragment {
    //Attributs globaux
    XMLAsynctask _task1 = null;
    XMLAsynctask _task2 = null;
    XMLAsynctask _task3 = null;
    RecyclerView recyclerView;
    EpicNewsAdapter adapter;
    //C'est la View du Fragment: ici, c'est un layout précrée qui contient une recyclerView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container,false);
    }

    //Ici, ce sont les instructions du fragment après la vue chargée
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new EpicNewsAdapter((URLLoader) getActivity());
        //On extrait la progressBar et on la fait diparaître lorsqu'il y a un changement dans la
        //RecyclerView
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBarRV);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                progressBar.setVisibility(View.GONE);
            }
        });
        //On récupère la recyclerView et on lui met un layout manager, ainsi qu'un adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewPrincipal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        Log.e("MainFragment","getActivity : " + getActivity());
        Log.e("MainFragment","UrlLoader : " + (URLLoader) getActivity());

        //On lance les 3 Asynctasks
        reload();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //La création du bouton refresh !!! À changer n'apparaît pas
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
    }

    //L'action du bouton refresh
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionRefresh){
            //petit message
            Toast.makeText(getActivity(),"Refreshing the list",Toast.LENGTH_SHORT).show();
            reload();
        }

        return true;
    }

    //Pour l'annulation des téléchargements si on quitte
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(_task1!=null && _task1.getStatus()!= AsyncTask.Status.FINISHED){
            _task1.cancel(true);
        }
        if(_task2!=null && _task2.getStatus()!= AsyncTask.Status.FINISHED){
            _task2.cancel(true);
        }
        if(_task3!=null && _task3.getStatus()!= AsyncTask.Status.FINISHED){
            _task3.cancel(true);
        }
    }

    //L'implémentation de l'executions des trois Asynctasks
    public void reload(){
        //On lance les 3 Task de téléchargement des documents RSS:
        //Les trois téléchargements sont faits en même temps grace à execute on Thread executor
        //Le premier pour les nouvelles technologies
        //Le deuxière pour l'actualité sécurité
        //La troisième pour l'actualité JeuxVidéo
        if(adapter!=null){
            _task1 = new XMLAsynctask(adapter);
            _task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://www.01net.com/rss/actualites/produits/");
            _task2 = new XMLAsynctask(adapter);
            _task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://www.01net.com/rss/actualites/securite/");
            _task3 = new XMLAsynctask(adapter);
            _task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://www.01net.com/rss/jeux-video/");

        }


    }
}
