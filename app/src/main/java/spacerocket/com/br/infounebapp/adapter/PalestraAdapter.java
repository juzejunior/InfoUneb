package spacerocket.com.br.infounebapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import spacerocket.com.br.infounebapp.R;
import spacerocket.com.br.infounebapp.model.Palestra;


/**
 * Created by blackwolf on 18/09/16.
 */
public class PalestraAdapter extends RecyclerView.Adapter<PalestraAdapter.MyViewHolder> {

    private Context mContext;
    private List<Palestra> palestrasList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, palestrante, data, hora;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titlePalestra);
            palestrante = (TextView) view.findViewById(R.id.palestrantePalestra);
            data = (TextView) view.findViewById(R.id.dataPalestra);
            hora = (TextView) view.findViewById(R.id.horarioPalestra);
            thumbnail = (ImageView) view.findViewById(R.id.fotoPalestra);
        }
    }

    public PalestraAdapter(Context mContext, List<Palestra> palestraLists) {
        this.mContext = mContext;
        this.palestrasList = palestraLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.palestra_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Palestra palestra = palestrasList.get(position);
        holder.title.setText(palestra.getTitulo());
        holder.palestrante.setText(palestra.getPalestrante());
        holder.data.setText("Data: "+palestra.getData());
        holder.hora.setText("Horário: "+palestra.getHorario());
        // loading palestrante cover using Glide library
        Glide.with(mContext).load(palestra.getFoto()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return palestrasList.size();
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
