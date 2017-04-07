package com.justin.epicnews;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by lejus on 27/03/2017.
 * Le Fragement chargé d'afficher l'article
 */

public class ArticleFragment extends Fragment {
    //Static Factory method d'ArticleFragment
    //Au moment de la création du fragment, on attache
    //un bundle au fragment qui est le link de l'article
    public static ArticleFragment create(String link, String title) {
        Bundle args = new Bundle();
        args.putString("link", link);
        args.putString("title", title);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Ajoute une ActionBar au Fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle((String)getArguments().get("title"));
    }

    //On lui met l'action partager et voir dans l'Action bar pr le biais d'un menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.buttonShare : {
                //On crée un intent générique qui a pour but d'envoyer
                Intent intent = new Intent(Intent.ACTION_SEND);
                //Le type de données est du texte
                intent.setType("text/plain");
                //On insère le lien que l'on a reçu auparavant et on le met dans l'intent EXTRA_TEXT
                intent.putExtra(Intent.EXTRA_TEXT,(String)getArguments().get("link"));

                //On démarre l'activité de partage
                startActivity(Intent.createChooser(intent,"Share the link!"));

                return true;
            }
            //Dans le cas du bouton navigateur, L'action et de Voir, et on donne le lien en question.
            //Puis en démarre l'activité
            case R.id.buttonNavigator:{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((String)getArguments().get("link")));
                startActivity(intent);
                return true;
            }
            default:return super.onOptionsItemSelected(item);
        }
    }
    //L'affichage de l'article
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article,container,false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //La progressBar
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //On affiche la WebView grâce au lien envoyé par l'Intent
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        //Le chargement de la page
        webView.loadUrl((String)getArguments().get("link"));

        //La bar
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress <100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(newProgress);
                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
