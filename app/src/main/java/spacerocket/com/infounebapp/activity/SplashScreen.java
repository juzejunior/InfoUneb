package spacerocket.com.infounebapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.util.Log;
import android.content.Intent;

import spacerocket.com.infounebapp.R;


public class SplashScreen extends AppCompatActivity{
    Animation animation1;
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        image = (ImageView) findViewById(R.id.imageView2);
        image.startAnimation(animation1);
        Thread TempodeInicio = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Log.d("Exception", "Exception" + e);
                } finally {
                    Intent main = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(main);
                }
                finish();
            }
        };
        TempodeInicio.start();
    }
}
