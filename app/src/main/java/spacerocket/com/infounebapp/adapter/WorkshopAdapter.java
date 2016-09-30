package spacerocket.com.infounebapp.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import spacerocket.com.infounebapp.R;
import spacerocket.com.infounebapp.model.Workshop;

/**
 * Created by José Diôgo on 02/09/16.
 */
public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.MyViewHolder> {

    private Context mContext;
    private List<Workshop> workshopsList;
    private List<Image> images;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, palestrante,data, hora;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            palestrante = (TextView) view.findViewById(R.id.palestrante);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            data = (TextView) view.findViewById(R.id.dataWorkshop);
            hora = (TextView) view.findViewById(R.id.horarioWorkshop);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public WorkshopAdapter(Context mContext, List<Workshop> workshopsList) {
        this.mContext = mContext;
        this.workshopsList = workshopsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workshop_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Workshop workshop = workshopsList.get(position);
        holder.title.setText(workshop.getTitulo());
        holder.palestrante.setText(workshop.getPalestrante());
        holder.data.setText("Data: "+workshop.getData());
        holder.hora.setText("Horário: "+workshop.getHorario());
        // loading palestrante cover using Glide library
        Glide.with(mContext).load(workshop.getFoto()).into(holder.thumbnail);
       //Glide.with(mContext).load(workshop.getFoto()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return workshopsList.size();
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
