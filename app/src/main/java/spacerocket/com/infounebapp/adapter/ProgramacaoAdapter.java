package spacerocket.com.infounebapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import spacerocket.com.infounebapp.R;
import spacerocket.com.infounebapp.model.Programation;


/**
 * Created by bill-01 on 23/09/16.
 */

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.MyViewHolder> {

    private List<Programation> programationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, status, name, tipo, horario;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.namePrograma);
            status = (TextView) view.findViewById(R.id.statusPrograma);
            name = (TextView) view.findViewById(R.id.palestrantePrograma);
            tipo = (TextView) view.findViewById(R.id.tipoPrograma);
            horario = (TextView) view.findViewById(R.id.horarioPrograma);
        }
    }

    public ProgramacaoAdapter(List<Programation> programationList){
        this.programationList = programationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.programacao_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Programation programation = programationList.get(position);
        holder.title.setText(programation.getTitulo());
        holder.status.setText(programation.getStatus());
        holder.name.setText(programation.getNome());
        holder.tipo.setText(programation.getTipo());
        holder.horario.setText(programation.getHorario());
    }

    @Override
    public int getItemCount() {
        return programationList.size();
    }
}
