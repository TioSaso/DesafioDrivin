package com.example.desafiodrivin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<String> breeds;

    private View.OnClickListener listener;

    public Adapter(Context context, ArrayList<String> breeds){
        this.inflater = LayoutInflater.from(context);
        this.breeds = breeds;
    }
    /** Creacion de la vista en el RecyclerView utilizando el custom_layout_list**/
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout_list, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    /** Llamado de datos en la posicion especifica **/
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        obtainImg(breeds.get(position), holder.itemView.findViewById(R.id.breedsImage));

    }

    @Override
    public int getItemCount() {
        return breeds.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageBreeds;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBreeds = itemView.findViewById(R.id.breedsImage);
        }
    }
    /** Metodo encargado de recuperar la imagen y utilizar la imagen para la vista **/
    private void obtainImg(String breedname, View holder) {
        RequestQueue queue = Volley.newRequestQueue(inflater.getContext());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "https://dog.ceo/api/breed/"+breedname+"/images/random", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String uriImg = response.getString("message");
                    Picasso.get().load(uriImg).into((ImageView) holder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
