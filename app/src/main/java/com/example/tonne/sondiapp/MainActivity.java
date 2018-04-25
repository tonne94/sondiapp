package com.example.tonne.sondiapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    private ListView listView;
    private Button buttonSend;
    private String respo;
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list_view);

        adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listItems);

        listView.setAdapter(adapter);

        buttonSend = findViewById(R.id.button_send);

        buttonSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final Request request = new Request.Builder()
                        .url("http://54.200.18.28/api/profile/ivic/newsfeed").get()
                        .build();
                new MyAsyncTask().execute(request);

                            }
        });
    }

    private void processJSON(String jsonStr) throws JSONException {
        System.out.println("ab");
        JSONArray jArray = new JSONArray(jsonStr);
        for(int i=0; i<jArray.length(); i++){
            JSONObject jObject = jArray.getJSONObject(i);
            String a = jObject.getString("Naziv");
            System.out.println(a);
        }
    }

    class MyAsyncTask extends AsyncTask<Request, Void, Response> {

        @Override
        protected Response doInBackground(Request... requests) {
            Response response = null;

            try {
                response = client.newCall(requests[0]).execute();
                respo = response.body().string();

                if(respo.length()>0){
                    processJSON(respo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

        }
    }
}
