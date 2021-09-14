package com.example.desafiodrivin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<String> breedsList;
    private static String JSON_URL = "https://dog.ceo/api/breeds/list/all";
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**  **/
        recyclerView = findViewById(R.id.breeds);
        breedsList = new ArrayList<>();
        obtainBreeds();

    }
    /** Metodo encargada de rescatar los datos de la API de DOG.CEO con la lista de razás existentes
     *  Realiza el envio de datos rescatados al Adaptador para su utilizacion en el RecyclerView
     * **/
    private void obtainBreeds() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("message");
                    JSONArray jsonArray = jsonObject.names();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        breedsList.add((String) jsonArray.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                adapter= new Adapter(getApplicationContext(), breedsList);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "La raza seleccionada es "+breedsList.get(recyclerView.getChildAdapterPosition(view)), Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "onErrorResponse:" + error);
            }
        });
        queue.add(jsonArrayRequest);
    }
}