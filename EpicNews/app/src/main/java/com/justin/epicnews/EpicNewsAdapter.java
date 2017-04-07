package com.justin.epicnews;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by lejus on 22/03/2017.
 * L'adapter de mon programme
 */

public class EpicNewsAdapter extends RecyclerView.Adapter<EpicNewsAdapter.ArticleViewHolder> implements XMLAsynctask.DocumentConsumer {

    //L'adapter possède un fichier DOM initialisé à null
    private Document _document = null;
    private List<News> news = new ArrayList<>();
    private final URLLoader _urlLoader;

    //Constructeur de l'adapter contenant l'urlloader
    public EpicNewsAdapter(URLLoader urlLoader) {
        this._urlLoader = urlLoader;
    }


    //Création d'une ViewHolder pour les cellules de la RecyclerView
    @Override
    public EpicNewsAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell_type1,parent,false);
        return new ArticleViewHolder(view);
    }

    //Permet de relier les informations au viewholder suivant sa position
    @Override
    public void onBindViewHolder(EpicNewsAdapter.ArticleViewHolder holder, int position) {
        News n = news.get(position);
        holder.setElement(n);
    }
    //Permet de compter le nombre d'items dans ce qu'il faut afficher dans la recyclerView
    @Override
    public int getItemCount() {
        if(news!=null)
        return news.size();
        else return 0;
    }
    //Dans cette fonction implémentée, nous allons extraire les informations du XML et ajouter
    //les informations clés à la liste des news, trie par date
    @Override
    public void setXMLDocument(Document document) {
        _document=document;
        //Création d'une liste de noeud qui correspondent à des noeuds XML
        //On obtient donc une liste avec tous les items
        NodeList newsRaw = _document.getElementsByTagName("item");
        //On récupère les infos de cette liste, on les met dans un objet news et on l'ajoute à la liste,
        //Puis on trie cette liste par date
        for (int i = 0; i< newsRaw.getLength();i++){
            Element _currentElement = (Element) newsRaw.item(i);
            String title = _currentElement.getElementsByTagName("title").item(0).getTextContent();
            String description = _currentElement.getElementsByTagName("description").item(0).getTextContent();
            String link = _currentElement.getElementsByTagName("link").item(0).getTextContent();
            String publicationDate = _currentElement.getElementsByTagName("pubDate").item(0).getTextContent();
            String category = _document.getElementsByTagName("link").item(0).getTextContent();
            try {
                News temp = new News(title,description,link,category,publicationDate);
                if (!news.contains(temp)){
                    news.add(temp);
                }
            }catch (ParseException e){
                e.printStackTrace();
            }

        }
        sortNewsByDate();


    }

    //Fonction de tri avec un comparateur
    private void sortNewsByDate(){
        //on trie les news à l'aide d'un comparateur
        Collections.sort(news, new Comparator<News>() {
            @Override
            public int compare(News o1, News o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        notifyDataSetChanged();
    }


    //Les caractéristiques du ViewHolder
    class ArticleViewHolder extends RecyclerView.ViewHolder{
        //La viewHolder a 3 views: un titre, une description et une imagette
        private final TextView title;
        private final TextView description;
        private final ImageView imageView;
        //Le currentLink permet d'envoyer à l'activité contenant une webview de charger l'url
        private String _currentLink;
        private String _currentTitle;

        //Constructeur
        public ArticleViewHolder(final View itemView) {
            super(itemView);
            //On récupère les view pour les manipuler
            title = (TextView) itemView.findViewById(R.id.textViewTitle);
            description = (TextView) itemView.findViewById(R.id.textViewDescription);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //Listener pour envoyer le lien à la webView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //On met un intent qui va envoyer l'article à ArticleActivity pour l'afficher
                    //Intent intent = new Intent(itemView.getContext(),ArticleActivity.class);
                    //intent.putExtra("link",_currentLink);
                    //intent.putExtra("title",_currentTitle);
                    // itemView.getContext().startActivity(intent);
                    String link = _currentLink;
                    String title = _currentTitle;
                    //On utilise URLLoader à la place
                    if(_urlLoader!=null)
                        _urlLoader.load(title,link);
                    else{
                        Log.e("ATTENTION URLLOader","Valeur: " + _urlLoader);
                        Log.i("Link : ", link);
                        Log.i("Titre : ", title);

                    }
                }
            });
        }

        public void setElement(News n){
            _currentLink = n.getLink();
            _currentTitle = n.getTitle();
            title.setText(n.getTitle());
            description.setText(n.getDescription());
            //Ce code permet de sélectionner l'imagette suivant la catégorie de l'article
            switch (n.getCategory()){
                case "http://www.01net.com/actualites/securite/":
                    imageView.setImageResource(R.drawable.ic_securite);
                    break;
                case "http://www.01net.com/actualites/produits/":
                    imageView.setImageResource(R.drawable.nv_techno);
                    break;
                case "http://www.01net.com/jeux-video/":
                    imageView.setImageResource(R.drawable.lab_xboxone);
                    default:break;

            }
        }
    }
}
