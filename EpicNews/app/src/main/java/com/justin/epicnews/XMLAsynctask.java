package com.justin.epicnews;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lejus on 22/03/2017.
 */

public class XMLAsynctask extends AsyncTask<String,Void,Document> {
    //Cet interface permet à l'implémenteur d'avoir la possibilité d'utiliser des documents DOM
    interface DocumentConsumer{
        void setXMLDocument(Document document);
    }

    private DocumentConsumer _consumer;

    //Constructeur de l'XMLAsynctask
    public XMLAsynctask(DocumentConsumer _consumer) {
        this._consumer = _consumer;
    }

    //Le traitement Lourd bloquant qui va être fait dans un thread autre que le thread UI
    @Override
    protected Document doInBackground(String... params) {
        try{
            //Connexion au flux RSS grâce à l'url donnée lors de l'appel à l'AsyncTask en paramètre
            //Création d'un flux de données pour récupérer les XML
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream stream = connection.getInputStream();
            try {
                //On retourne le document DOM
                return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            }finally {
                stream.close();
            }

        }catch (Exception e){
            Log.e("XMLAsyncTask","Error while downloading");
            throw new RuntimeException(e);

        }

    }

    //Le traitement qui aura lieu dans le Thread UI
    //On affecte le document DOM à l'implémenteur de l'interface DocumentConsumer
    @Override
    protected void onPostExecute(Document document) {
        Log.e("XMLAsyncTask","Finished");
        _consumer.setXMLDocument(document);
    }
}
