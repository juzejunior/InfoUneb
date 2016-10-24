package spacerocket.com.br.infounebapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import spacerocket.com.br.infounebapp.R;
import spacerocket.com.br.infounebapp.adapter.ProgramacaoAdapter;
import spacerocket.com.br.infounebapp.firebase.FirebaseConfig;
import spacerocket.com.br.infounebapp.model.Programation;


public class Locais extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView backdrop;
    private TextView textPrincipal, textSecundario;
    private String local, m_local;
    private FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseConfig ref;

    //recycleview
    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected List<Programation> programationList = new ArrayList<>();

    //text to change
    private TextView statusText;

    //progressDialog
    private ProgressDialog mProgressDialog;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locais_activity);
        Firebase.setAndroidContext(this);

        Intent it = getIntent();
        local = it.getStringExtra("local");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initCollapsingToolbar();
        backdrop = (ImageView) findViewById(R.id.backdrop);
        textPrincipal = (TextView) findViewById(R.id.textPrincipal);
        textSecundario = (TextView) findViewById(R.id.textSecundario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ref = new FirebaseConfig();
        setLocal();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_viewProgramacao);
        mAdapter = new ProgramacaoAdapter(programationList);
        mRecyclerView.setHasFixedSize(true);//PAY ATENTION
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        statusText = (TextView) findViewById(R.id.semEventosPrograma);

         //start database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Programação");
        //progresss dialog
        mProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//seta o spinner circle
        mProgressDialog.setMessage("Carregando...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        getProgramacao();

        if(programationList.isEmpty())
        {
            mProgressDialog.dismiss();
            statusText.setText("Atividades em breve!\n");
            statusText.setVisibility(View.VISIBLE);
        }else{
            statusText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        //startActivity(new Intent(this, MainActivity.class));//back to main
    }

    /*Get firebase base reference*/
    public void getProgramacao()
    {
        mDatabase.addValueEventListener(programacaoListener);
    }
    /*listening the changes*/
    ValueEventListener programacaoListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mProgressDialog.dismiss();
            readProgramacao(dataSnapshot);
            if(!programationList.isEmpty()) Collections.sort(programationList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mProgressDialog.dismiss();
            Toast.makeText(Locais.this, "Verifique sua conexão com a internet!", Toast.LENGTH_SHORT).show();
        }
    };

    /*read each data on base*/
    void readProgramacao(DataSnapshot dataSnapshot)
    {
        //para verificar se deve mostrar ou não as atividades
        for(DataSnapshot dss : dataSnapshot.getChildren()){
            Programation programation = dss.getValue(Programation.class);
            Calendar c = Calendar.getInstance();//apagar
            if(programation.getLocal().equals(local))
            {
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minuto = c.get(Calendar.MINUTE);
                //toast test
                //Toast.makeText(this, "Hora: "+hora, Toast.LENGTH_SHORT).show();

                if(validElement(programation)) {
                    statusText.setVisibility(View.INVISIBLE);

                    if(programation.getHoraInicio() > hora){
                        programation.setStatus("Em breve");
                        programationList.add(programation);
                    }else if(programation.getHoraInicio() >= hora && hora <= programation.getHoraFim())
                    {
                        if(programation.getHoraInicio() == hora && programation.getMinutoInicio() > minuto) programation.setStatus("Aquecendo");
                        else if(programation.getHoraFim() == hora && minuto > programation.getMinutoFim()) programation.setStatus("Encerrado");
                        else {
                            programation.setPrioridade(0);
                            programation.setStatus("Em andamento");
                            programationList.add(programation);
                        }

                    }else{
                        programation.setPrioridade(2);
                        programation.setStatus("Encerrado");
                        programationList.add(programation);
                    }
                }
            }
        }
        /*notify the changes to adapter*/
        mAdapter.notifyDataSetChanged();
    }

    public boolean validElement(Programation programation)
    {
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);//19; //c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH)+1; //10;//c.get(Calendar.MONTH)+1;
        int ano =  c.get(Calendar.YEAR);//2016;//c.get(Calendar.YEAR);
        if(programation.getDia() == dia && programation.getMes() == mes && ano == programation.getAno())
        {
            return true;
        }
        return false;
    }

    public void setLocal(){
        String descricaoLocal;
        switch (local){
            case "teatro1": m_local = "Teatro";
                textPrincipal.setText(m_local);
                break;
            case "cpedr":  m_local = "CPEDR";
                textPrincipal.setText(m_local);
                descricaoLocal = "Laboratório de Informática";
                textSecundario.setText(descricaoLocal);
                break;
            case "ppg": m_local = "PPG";
                textPrincipal.setText(m_local);
                descricaoLocal = "Prédio de Pós Graduação";
                textSecundario.setText(descricaoLocal);
        }
         getContent();
    }

    public void getContent(){
        ref.addStorageChild("gs://infounebproject.appspot.com/imagens/locais/"+local+".png");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://infounebproject.appspot.com/imagens/locais/"+local+".png");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(Locais.this).load(uri.toString()).into(backdrop);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Locais.this, "Não consegui carregar as imagens do BD", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

            }
        });
    }
}
