package com.justin.epicnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
 C'est l'activité qui contiendra Au moins le fragment de recyclerView des articles
 Et le fragment articleFragment si c'est une tablette.
 */
public class MainActivity extends AppCompatActivity implements URLLoader{

    //On charge l'activité principal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //On ajoute au moins le ListFragment dans l'activité
        getFragmentManager().beginTransaction()
                .add(R.id.listFragment,new MainFragment())
                .commit();

    }

    //Cette fonction permet de savoir s'il faut faire appel à une nouvelle activité (cas téléphone)
    //Ou bien s'il faut juste charger le fragment ArticleFragment (cas tablette)
    @Override
    public void load(String title, String link) {
        //Si on a un articleFragment dans le layout activity_main, c'est que l'on est sur tablette
        if (findViewById(R.id.articleFragment) != null){
            //On crée un fragment et on lui attache un lien avec la static factory method + le titre
            ArticleFragment fragment = ArticleFragment.create(link,title);
            //On remplace le fragment de l'article par un nouveau à chaque fois que l'on appuie
            //sur un lien
            getFragmentManager().beginTransaction()
                    .replace(R.id.articleFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        //Sinon, on est sur téléphone, et dans ce cas, on lance une nouvelle activité qui est
        //ArticleActivity, et lui envoie un intent avec le link et le titre
        else{
            Intent intent = new Intent(this,ArticleActivity.class);
            intent.putExtra("link",link);
            intent.putExtra("title",title);
            startActivity(intent);
        }
    }
    //Lorsque l'on appuie sur "back" sur la tablette, on affiche l'article précédent
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }

    }
}
