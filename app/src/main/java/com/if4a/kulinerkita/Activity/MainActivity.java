package com.if4a.kulinerkita.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.if4a.kulinerkita.API.APIRequestData;
import com.if4a.kulinerkita.API.RetroServer;
import com.if4a.kulinerkita.Adapter.AdapterKuliner;
import com.if4a.kulinerkita.Model.ModelKuliner;
import com.if4a.kulinerkita.Model.ModelResponse;
import com.if4a.kulinerkita.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_kuliner;
    private FloatingActionButton fab_tambah;
    private RecyclerView.Adapter adKuliner;
    private RecyclerView.LayoutManager lmKuliner;
    private List<ModelKuliner> ListKuliner = new ArrayList<>();
    private ProgressBar pbKuliner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_kuliner = findViewById(R.id.rv_kuliner);
        fab_tambah = findViewById(R.id.fab_tambah);
        pbKuliner = findViewById(R.id.pb_kuliner);
        lmKuliner = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_kuliner.setLayoutManager(lmKuliner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveKuliner();
    }

    public void retrieveKuliner(){
        pbKuliner.setVisibility(View.VISIBLE);

        APIRequestData ARD = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ModelResponse> proses = ARD.ardRetrieve();

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                String kode = response.body().getKode();
                String pesan = response.body().getPesan();
                ListKuliner = response.body().getData();

                adKuliner = new AdapterKuliner(MainActivity.this,ListKuliner);
                rv_kuliner.setAdapter(adKuliner);
                adKuliner.notifyDataSetChanged();

                pbKuliner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT).show();
                pbKuliner.setVisibility(View.GONE);
            }
        });
    }
}