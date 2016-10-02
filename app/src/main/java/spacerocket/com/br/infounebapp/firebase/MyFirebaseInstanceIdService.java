package spacerocket.com.br.infounebapp.firebase;

import android.content.Context;

import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileOutputStream;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        FirebaseMessaging.getInstance().subscribeToTopic("infouneb");
        escreverArquivo();
        salvarDatabase();
    }

    public void escreverArquivo(){
        String filename = "notificacao";
        String notification = "ON";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(notification.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvarDatabase(){
        Firebase.setAndroidContext(this);
        FirebaseConfig ref = new FirebaseConfig();
        final String token = FirebaseInstanceId.getInstance().getToken();
        ref.addDatabaseChild("Token/" + token + "/Notificacoes");
        Firebase firebase = new Firebase(ref.getDatabase());
        firebase.setValue("ON");
    }
}