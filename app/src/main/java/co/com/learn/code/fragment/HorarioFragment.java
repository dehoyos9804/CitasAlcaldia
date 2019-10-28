package co.com.learn.code.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.List;

import co.com.learn.code.Adapter.AdaptadorUsuarios;
import co.com.learn.code.R;

public class HorarioFragment extends Fragment implements View.OnClickListener {

    //etiqueta para la depuracion
    private static final String TAG = UsuariosFragment.class.getSimpleName();
    private Gson gson = new Gson();
    //adaptador del RecicleView
    private AdaptadorUsuarios adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //instancia del progress dialog
    private static ProgressDialog loading = null;

    private View view;

    private TextView data_empty;
    private FloatingActionButton fab_usuario;

    private LinearLayout lyt_hours_1;
    private LinearLayout lyt_hours_2;
    private LinearLayout lyt_hours_3;
    private LinearLayout lyt_hours_4;
    private LinearLayout lyt_hours_5;
    private LinearLayout lyt_hours_6;
    private LinearLayout lyt_hours_7;

    private SwitchCompat switch_open_1;
    private SwitchCompat switch_open_2;
    private SwitchCompat switch_open_3;
    private SwitchCompat switch_open_4;
    private SwitchCompat switch_open_5;
    private SwitchCompat switch_open_6;
    private SwitchCompat switch_open_7;

    private AppCompatSpinner spin_bh_from_1, spin_bh_to_1;
    private AppCompatSpinner spin_bh_from_2, spin_bh_to_2;
    private AppCompatSpinner spin_bh_from_3, spin_bh_to_3;
    private AppCompatSpinner spin_bh_from_4, spin_bh_to_4;
    private AppCompatSpinner spin_bh_from_5, spin_bh_to_5;
    private AppCompatSpinner spin_bh_from_6, spin_bh_to_6;
    private AppCompatSpinner spin_bh_from_7, spin_bh_to_7;

    private AppCompatButton btn_apply;

    //costructor del fragmento
    public HorarioFragment() {

    }

    /**
     * Crea una instancia prefabricada de {@link UsuariosFragment}
     *
     * @return Instancia dle fragmento
     */
    public static HorarioFragment newInstance() {
        HorarioFragment fragment = new HorarioFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_section_horario, container, false);

        init();//iniciar componentes

        return view;
    }

    private void init(){
        lyt_hours_1 = (LinearLayout) view.findViewById(R.id.lyt_hours_1);
        lyt_hours_2 = (LinearLayout) view.findViewById(R.id.lyt_hours_2);
        lyt_hours_3 = (LinearLayout) view.findViewById(R.id.lyt_hours_3);
        lyt_hours_4 = (LinearLayout) view.findViewById(R.id.lyt_hours_4);
        lyt_hours_5 = (LinearLayout) view.findViewById(R.id.lyt_hours_5);
        lyt_hours_6 = (LinearLayout) view.findViewById(R.id.lyt_hours_6);
        lyt_hours_7 = (LinearLayout) view.findViewById(R.id.lyt_hours_7);

        switch_open_1 = (SwitchCompat) view.findViewById(R.id.switch_open_1);
        switch_open_2 = (SwitchCompat) view.findViewById(R.id.switch_open_2);
        switch_open_3 = (SwitchCompat) view.findViewById(R.id.switch_open_3);
        switch_open_4 = (SwitchCompat) view.findViewById(R.id.switch_open_4);
        switch_open_5 = (SwitchCompat) view.findViewById(R.id.switch_open_5);
        switch_open_6 = (SwitchCompat) view.findViewById(R.id.switch_open_6);
        switch_open_7 = (SwitchCompat) view.findViewById(R.id.switch_open_7);

        spin_bh_from_1 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_1);
        spin_bh_from_2 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_2);
        spin_bh_from_3 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_3);
        spin_bh_from_4 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_4);
        spin_bh_from_5 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_5);
        spin_bh_from_6 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_6);
        spin_bh_from_7 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_from_7);

        spin_bh_to_1 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_1);
        spin_bh_to_2 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_2);
        spin_bh_to_3 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_3);
        spin_bh_to_4 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_4);
        spin_bh_to_5 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_5);
        spin_bh_to_6 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_6);
        spin_bh_to_7 = (AppCompatSpinner) view.findViewById(R.id.spin_bh_to_7);

        btn_apply = (AppCompatButton) view.findViewById(R.id.btn_apply);

        lyt_hours_1.setVisibility(View.GONE);
        lyt_hours_2.setVisibility(View.GONE);
        lyt_hours_3.setVisibility(View.GONE);
        lyt_hours_4.setVisibility(View.GONE);
        lyt_hours_5.setVisibility(View.GONE);
        lyt_hours_6.setVisibility(View.GONE);
        lyt_hours_7.setVisibility(View.GONE);


        //agrego los eventos a los switch
        addEventSwitch();
        btn_apply.setOnClickListener(this);

    }

    private void setVisibleLayout(final SwitchCompat swith_compat){
        swith_compat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (swith_compat.getId()){
                    case R.id.switch_open_1:
                        if(switch_open_1.isChecked()){
                            lyt_hours_1.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_1.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.switch_open_2:
                        if(switch_open_2.isChecked()){
                            lyt_hours_2.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_2.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.switch_open_3:
                        if(switch_open_3.isChecked()){
                            lyt_hours_3.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_3.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.switch_open_4:
                        if(switch_open_4.isChecked()){
                            lyt_hours_4.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_4.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.switch_open_5:
                        if(switch_open_5.isChecked()){
                            lyt_hours_5.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_5.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.switch_open_6:
                        if(switch_open_6.isChecked()){
                            lyt_hours_6.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_6.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.switch_open_7:
                        if(switch_open_7.isChecked()){
                            lyt_hours_7.setVisibility(View.VISIBLE);
                        }else{
                            lyt_hours_7.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
    }

    private void addEventSwitch(){
        for (int i = 1; i<= 7; i++){
            if(i == 1){
                setVisibleLayout(switch_open_1);
            }
            if(i == 2){
                setVisibleLayout(switch_open_2);
            }
            if(i == 3){
                setVisibleLayout(switch_open_3);
            }
            if(i == 4){
                setVisibleLayout(switch_open_4);
            }
            if(i == 5){
                setVisibleLayout(switch_open_5);
            }
            if(i == 6){
                setVisibleLayout(switch_open_6);
            }
            if(i == 7){
                setVisibleLayout(switch_open_7);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_apply:
                validarDatosGUI();
                break;
        }
    }



    private void validarSwith(AppCompatSpinner s1, AppCompatSpinner s2){
        if(s1.getSelectedItemPosition() == 0){
            showSnackBar("Debe seleccionar una hora");
            s1.performClick();
        }else{
            if(s2.getSelectedItemPosition() == 0){
                showSnackBar("Debe seleccionar una hora");
                s2.performClick();
            }
        }
    }

    private void validarDatosGUI(){
        boolean estado = true;
            if(!switch_open_1.isChecked() && !switch_open_1.isChecked()
                    && !switch_open_3.isChecked()
                    && !switch_open_4.isChecked()
                    && !switch_open_5.isChecked()
                    && !switch_open_6.isChecked()
                    && !switch_open_7.isChecked()){
                showSnackBar("Debe Seleccionar al menos un dia");
            }

            if(switch_open_1.isChecked()){
                validarSwith(spin_bh_from_1, spin_bh_to_1);
            }

            if(switch_open_2.isChecked()){
                validarSwith(spin_bh_from_2, spin_bh_to_2);
            }

            if(switch_open_3.isChecked()){
                validarSwith(spin_bh_from_3, spin_bh_to_3);
            }

            if(switch_open_4.isChecked()){
                validarSwith(spin_bh_from_4, spin_bh_to_4);
            }

            if(switch_open_5.isChecked()){
                validarSwith(spin_bh_from_5, spin_bh_to_5);
            }

            if(switch_open_6.isChecked()){
                validarSwith(spin_bh_from_6, spin_bh_to_6);
            }

            if(switch_open_7.isChecked()){
                validarSwith(spin_bh_from_7, spin_bh_to_7);
            }
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */

    private void showSnackBar(String msg) {
        Snackbar
                .make(view.findViewById(R.id.coordinator_horario), msg, Snackbar.LENGTH_LONG)
                .show();
    }

}
