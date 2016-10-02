package spacerocket.com.br.infounebapp.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import spacerocket.com.br.infounebapp.R;
import spacerocket.com.br.infounebapp.firebase.FirebaseConfig;


/**
 * A simple {@link Fragment} subclass.
 */
public class Maratona extends Fragment {
    private View v;
    private Toolbar tool;

    public Maratona() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Firebase.setAndroidContext(this.getActivity());
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_maratona, container, false);
        tool = (Toolbar) v.findViewById(R.id.toolbar);
        initCollapsingToolbar();
        FirebaseConfig ref = new FirebaseConfig();
        ref.addDatabaseChild("Maratona/Informacoes");
        final Firebase firebase = new Firebase(ref.getDatabase());
        final TextView c_m = (TextView) v.findViewById(R.id.informacoes);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if(value == null) return;
                c_m.setText(value);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return v;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) v.findViewById(R.id.appbar);
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
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Maratona de Projetos");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

}
