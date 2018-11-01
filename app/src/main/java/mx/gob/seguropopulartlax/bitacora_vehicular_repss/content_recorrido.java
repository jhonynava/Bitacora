package mx.gob.seguropopulartlax.bitacora_vehicular_repss;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;

public class content_recorrido extends Fragment {

    private Button btn_finalizar_recorrido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_recorrido, container, false);

        btn_finalizar_recorrido = v.findViewById(R.id.btn_finalizar_recorrido);

        btn_finalizar_recorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Finalizar_recorrido.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }
}
