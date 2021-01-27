package com.example.webscrapting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Document petrolDoc,dieselDoc;
    TextView petrolPrice,dieselPrice;
    Spinner cityList;

    String cityData,cityName,cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petrolPrice=findViewById(R.id.petrolPrice);
        dieselPrice=findViewById(R.id.dieselPrice);
        cityList=findViewById(R.id.spinner);



        String[] spinnerCity={"Bhavnagar","Rajkot","Surat"};
        String[] city={"58,Bhavnagar","52,Rajkot","51,Surat"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerCity);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cityList.setAdapter(adapter);

        cityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 cityData=city[position];
                cityCode=cityData.substring(0,cityData.indexOf(','));
                cityName=cityData.substring(cityData.indexOf(',')+1);

                Content content=new Content();
                content.execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    public class Content extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {

            System.out.println(petrolDoc.text());
            System.out.println(dieselDoc.text());
            //as per good return web site

            /*String sreg = null;
            String[] arrOfStr = doc.title().split(",", 3);
            for (String a : arrOfStr)
            {
                 sreg= StringUtils.substring(a,0,a.length()-14);
            }
            price.setText(sreg);*/


            //as per myPetrolPrice

            String[] sPetrol = petrolDoc.text().split("▲",2);
            String[] sDiesel= dieselDoc.text().split("▲",2);


            for(String a:sPetrol)
            {
                petrolPrice.setText(a.substring(0,16));
            }

            for(String a:sDiesel)
            {
                dieselPrice.setText(a.substring(0,16));
            }


            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                petrolDoc = Jsoup.connect("https://www.mypetrolprice.com/"+cityCode+"/Petrol-price-in-"+cityName).get();
                dieselDoc = Jsoup.connect("https://www.mypetrolprice.com/"+cityCode+"/Diesel-price-in-"+cityName).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}