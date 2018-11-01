package mx.gob.seguropopulartlax.bitacora_vehicular_repss.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.MenuPrincipal;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.R;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.Recorridos;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades.Vehiculo;

import static android.content.Context.MODE_PRIVATE;

public class VehiculoAdaptador extends RecyclerView.Adapter<VehiculoAdaptador.VehiculoHolder> {

    Context context;
    List<Vehiculo> listaVehiculos;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    public VehiculoAdaptador(List<Vehiculo> listaVehiculos, Context context){
        this.listaVehiculos = listaVehiculos;
        this.context = context;
    }

    @NonNull
    @Override
    public VehiculoAdaptador.VehiculoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_automoviles,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = this.context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        return new VehiculoHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull VehiculoAdaptador.VehiculoHolder holder, final int position) {
        Boolean recorrido = sharedPreferences.getBoolean("recorrido", false) ;

        holder.img_cardview.setImageResource(R.drawable.chevrolet_captiva_sport_2013_blanco);
        holder.texto_noEconomico.setText(listaVehiculos.get(position).getNo_economico());
        holder.texto_placas.setText(listaVehiculos.get(position).getPlacas());

        if (listaVehiculos.get(position).isDisponibilidad()){
            holder.texto_disponibilidad.setText("LIBRE");
            holder.texto_disponibilidad.setTextColor(context.getResources().getColor(R.color.verde));
            if (!recorrido){
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,Recorridos.class);
                        intent.putExtra("no_economico",listaVehiculos.get(position).getNo_economico());
                        context.startActivity(intent);
                    }
                });
            }

        } else {
            holder.texto_disponibilidad.setText("OCUPADO" );
            holder.texto_disponibilidad.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            if (!recorrido){
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "El vehiculo esta ocupado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaVehiculos.size();
    }

    public class VehiculoHolder  extends RecyclerView.ViewHolder{

        TextView texto_noEconomico, texto_disponibilidad, texto_placas;
        ImageView img_cardview;
        CardView cardView;

        public VehiculoHolder(View itemView) {
            super(itemView);
            texto_noEconomico = itemView.findViewById(R.id.texto_noEconomico);
            texto_disponibilidad = itemView.findViewById(R.id.texto_disponibilidad);
            texto_placas = itemView.findViewById(R.id.texto_placas);
            img_cardview = itemView.findViewById(R.id.img_cardview);
            cardView = itemView.findViewById(R.id.cardview_automoviles);
        }
    }
}
