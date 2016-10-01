package spacerocket.com.infounebapp.firebase;

import com.firebase.client.Firebase;

public class FirebaseConfig{
    Firebase firebase;
    private String urlDatabase;
    private String urlStorage;

    public FirebaseConfig(){
        urlDatabase = "https://infounebproject.firebaseio.com/";
        urlStorage = "gs://infounebproject.appspot.com/imagens";
    }

    public String getDatabase(){
        return urlDatabase;
    }

    public void addDatabaseChild(String child){
        urlDatabase = urlDatabase + "/" + child;
    }

    public String getStorage(){
        return urlStorage;
    }

    public void addStorageChild(String child){
        urlStorage = urlStorage + "/" + child;
    }

    public void salvarDB(String token){
        firebase = new Firebase(urlDatabase);
        firebase.setValue(token);
    }
}
