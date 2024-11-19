package com.example.currencyapp;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Parser {
    public ArrayList<String> parseXML(String urlString) {
        ArrayList<String> rates = new ArrayList<>();
        try {
            URL url = new URL(urlString);
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
}