package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;

public class content_recorrido extends Fragment {

    private CardView cardView_finalizar, cardView_cargarGas;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";
    private Boolean recarga_gasolina = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_recorrido, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recarga_gasolina = sharedPreferences.getBoolean("recarga_gasolina", false);

        cardView_finalizar = v.findViewById(R.id.cardview_finalizar);
        cardView_cargarGas = v.findViewById(R.id.cardview_cargarGas);

        if (recarga_gasolina)
            cardView_cargarGas.setVisibility(View.GONE);

        cardView_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Finalizar_recorrido.class);
                startActivity(intent);
            }
        });

        cardView_cargarGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Recarga_combustible.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
