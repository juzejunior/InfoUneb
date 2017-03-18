package spacerocket.com.br.infounebapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import spacerocket.com.br.infounebapp.R;
import spacerocket.com.br.infounebapp.firebase.FirebaseConfig;
import spacerocket.com.br.infounebapp.spaceRocket.WebViewActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    String notification = "ON";
    FloatingActionButton fab;
    private GoogleMap mMap;
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;
    Intent itLocais;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.infouneb_logo1);
        itLocais = new Intent(this, Locais.class);

        //testing
        context = getApplicationContext();
        activity = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Firebase.setAndroidContext(this);
        FirebaseConfig ref = new FirebaseConfig();
        final String token = FirebaseInstanceId.getInstance().getToken();
        ref.addDatabaseChild("Token/" + token + "/Notificacoes");
        final Firebase firebase1 = new Firebase(ref.getDatabase());
        final Firebase firebase2 = new Firebase(ref.getDatabase());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.equals("ON")){
                    notification = "OFF";
                    changeNotification(firebase2, token);
                }else{
                    notification = "ON";
                    changeNotification(firebase2, token);
                }
            }
        });

        firebase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(value == null) return;
                if(value.equals("ON")){
                    notification = "ON";
                    fab.setImageResource(R.drawable.ic_notifications_active_white_24dp);
                }else{
                    notification = "OFF";
                    fab.setImageResource(R.drawable.ic_notifications_off_white_24dp);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT > 21) {
            Thread thread =  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            wait(2000);
                        }
                    }
                    catch(InterruptedException ex){
                    }
                    finally {
                        startActivity(new Intent(MainActivity.this, EndEventActivity.class));
                    }
                }
            };

            thread.start();
        } else {
            Toast.makeText(this, "A edição de 2016 acabou :(", Toast.LENGTH_LONG).show();
        }
    }

    public void changeNotification(Firebase firebase, String token){
        if(token == null) return;
        if(notification.equals("ON")){
            firebase.setValue("ON");
            fab.setImageResource(R.drawable.ic_notifications_active_white_24dp);
            FirebaseMessaging.getInstance().subscribeToTopic("infouneb");
            Toast.makeText(MainActivity.this,"Notificações Ativadas", Toast.LENGTH_SHORT).show();
        }else if(notification.equals("OFF")){
            firebase.setValue("OFF");
            fab.setImageResource(R.drawable.ic_notifications_off_white_24dp);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("infouneb");
            Toast.makeText(MainActivity.this,"Notificações Desativadas", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_programacao) avaliar();
        else if (id == R.id.action_politica) policyApp();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /*inicia a tela de programação*/
        if (id == R.id.nav_programacao) {
            startActivity(new Intent(this, Programacao.class));
        } else if(id == R.id.nav_inscricao){
           inscrever();
        } else if (id == R.id.nav_rate) {
            avaliar();
        } else if (id == R.id.nav_send) {//envia e-mail feedback
            sendEmail();
        } else if(id == R.id.nav_patrocinadores){
            startActivity(new Intent(this, Colaboradores.class));//va para a pagina de colaboradores
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void policyApp() {
        try {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Documents/PP_PT/");
            mDatabase.addValueEventListener(politicas);

        }catch (NullPointerException ex) {
            Log.e("Policy", ex.getMessage());
        }
    }

    com.google.firebase.database.ValueEventListener politicas = new com.google.firebase.database.ValueEventListener() {
        @Override
        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
            String document = dataSnapshot.getValue(String.class);
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(document)));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    //link para inscricao
    public void inscrever()
    {
        if (Build.VERSION.SDK_INT > 21) {
            startActivity(new Intent(MainActivity.this, EndEventActivity.class));
        } else {
            Toast.makeText(this, "A edição de 2016 acabou :(", Toast.LENGTH_LONG).show();
        }
    }
    /*avaliação na app store*/
    public void avaliar()
    {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public void sendEmail()
    {
        Log.i("Send email", "");
        String[] TO = {"casi.uneb@gmail.com"};
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sugestão");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Queremos saber sua opinião");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar e-mail..."));
            finish();
        }
        catch (ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Não há um app de e-mail instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //checa se o usuario deu permissao de localização
        if (checkPermission()) {
            mMap.setMyLocationEnabled(true);
        }else{
            // Show rationale and request permission.
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission
                Toast.makeText(MainActivity.this, "Abilite sua localização para visualizar sua posição no mapa.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        }

        // Adicionando o marcador na uneb e movendo a camera
        LatLng uneb = new LatLng(-12.9518357, -38.4593316);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uneb, 17));//setting the zoom and location

        //locais no mapa
        final LatLng teatro = new LatLng(-12.9527924,-38.4594206);
        // LatLng depExatas = new LatLng(-12.9518765,-38.4591859);
        LatLng colegiado = new LatLng(-12.9510871,-38.4596446);
        // LatLng depCienDaVida = new LatLng(-12.9530894,-38.4581077);
        LatLng posGraduacao = new LatLng(-12.9511934, -38.4594599);

        //locais
        MarkerOptions mTeatro = new MarkerOptions().position(teatro).title("Teatro").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
        MarkerOptions mposGraduacao = new MarkerOptions().position(posGraduacao).title("Pós Graduação").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
        // MarkerOptions mDepExatas = new MarkerOptions().position(depExatas).title("Dep. Ciências Exatas da Terra").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
        MarkerOptions mColegiado = new MarkerOptions().position(colegiado).title("Lab Informática").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
        //adicionando locais ao mapa
        mMap.addMarker(mTeatro);
        //mMap.addMarker(mDepExatas);
        mMap.addMarker(mColegiado);
        mMap.addMarker(mposGraduacao);

        //ouvindo o click no marcador
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                marker.showInfoWindow();
                if(marker.getTitle().equals("Teatro")){ // if marker source is clicked
                    itLocais.putExtra("local", "teatro1");
                }else if(marker.getTitle().equals("Pós Graduação")){ // if marker source is clicked
                    itLocais.putExtra("local", "ppg");
                } else if(marker.getTitle().equals("Lab Informática")){ // if marker source is clicked
                    itLocais.putExtra("local", "cpedr");
                }

                if(itLocais.resolveActivity(getPackageManager()) != null)
                    startActivity(itLocais);
                else Toast.makeText(context, "Não consegui abrir", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissao concedida
                    if(checkPermission()) mMap.setMyLocationEnabled(true);

                } else {
                    Toast.makeText(MainActivity.this, "Abilite sua localização para visualizar sua posição no mapa.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /*checa a permissao de acesso a localizacao do usuario*/
    boolean checkPermission() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED) return true;

        return false;
    }

    @Override
    protected void onPause() {
        if(checkPermission()) mMap.setMyLocationEnabled(false);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(checkPermission()) mMap.setMyLocationEnabled(false);
        super.onStop();
    }
}