package com.example.currencyapp;

import android.os.AsyncTask;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataLoader extends AsyncTask<String, Void, ArrayList<String>> {
    private DataLoaderListener listener;

    public DataLoader(DataLoaderListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        ArrayList<String> rates = new ArrayList<>();
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            NodeList nodeList = document.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                String currency = nodeList.item(i).getAttributes().getNamedItem("currency").getNodeValue();
                String rate = nodeList.item(i).getAttributes().getNamedItem("rate").getNodeValue();
                rates.add(currency + " - " + rate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }

    @Override
    protected void onPostExecute(ArrayList<String> rates) {
        listener.onDataLoaded(rates);
    }

    public interface DataLoaderListener {
        void onDataLoaded(ArrayList<String> rates);
    }
}