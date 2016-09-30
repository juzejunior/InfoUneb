package spacerocket.com.infounebapp.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import spacerocket.com.infounebapp.R;
import spacerocket.com.infounebapp.model.Colaborador;
import spacerocket.com.infounebapp.spaceRocket.WebViewActivity;


/**
 * Created by blackwolf on 18/09/16.
 */
public class ColaboradorAdapter extends RecyclerView.Adapter<ColaboradorAdapter.MyViewHolder> {

    private Context mContext;
    private List<Colaborador> colaboradoresList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nome, tipo;
        public ImageView foto;
        public View view;
        public ClipData.Item currentItem;
        public TextView visitar;

        public MyViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.colaborador_nome);
            tipo = (TextView) view.findViewById(R.id.colaborador_descricao);
            foto = (ImageView) view.findViewById(R.id.colaborador_photo);
            visitar = (TextView) view.findViewById(R.id.btnVisitar);
        }
    }

    public ColaboradorAdapter(Context mContext, List<Colaborador> colaboradorLists) {
        this.mContext = mContext;
        this.colaboradoresList = colaboradorLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.colaboradores_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Colaborador colaborador = colaboradoresList.get(position);
        holder.nome.setText(colaborador.getNome());
        holder.tipo.setText(colaborador.getTipo());
        //holder.tipo.setText(colaborador.getLink());
        // loading colaborador cover using Glide library
        Glide.with(mContext).load(colaborador.getFoto()).into(holder.foto);
        //guando o usuario quiser visitar o site do colaborador
        holder.visitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebSite(colaborador.getLink());
            }
        });
    }
    //va para o website do colaborador
    public void openWebSite(String url)
    {
        Intent it = new Intent(mContext, WebViewActivity.class);
        it.putExtra("web_page", url);
        mContext.startActivity(it);
    }

    @Override
    public int getItemCount() {
        return colaboradoresList.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots

     private void showPopupMenu(View view) {
     // inflate menu
     PopupMenu popup = new PopupMenu(mContext, view);
     MenuInflater inflater = popup.getMenuInflater();
     inflater.inflate(R.menu.menu_album, popup.getMenu());
     popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
     popup.show();
     }*/

}
