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

    private ListView list;
    private Button buttonSend;
    private String respo;
    OkHttpClient client = new OkHttpClient();

    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

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
        list=findViewById(R.id.list_view);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_list, arrayList);

        list.setAdapter(adapter);

        buttonSend = findViewById(R.id.button_send);

        buttonSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final Request request = new Request.Builder()
                        .url("http://54.200.18.28/api/profile/ivic/newsfeed").get()
                        .build();
                new MyAsyncTask().execute(request);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(respo.length()>0){
                    try {
                        processJSON(respo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                            }
        });
    }

    private void processJSON(String jsonStr) throws JSONException {
        System.out.println("ab");
        JSONArray jArray = new JSONArray(jsonStr);
        for(int i=0; i<jArray.length(); i++){
            JSONObject jObject = jArray.getJSONObject(i);
            String a = jObject.getString("Naziv");
            arrayList.add(a);
            adapter.notifyDataSetChanged();
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

            } catch (IOException e) {
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
