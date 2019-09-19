package co.com.learn.code.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;

public class InitialUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView crAgendarCita;
    private CardView cr_listaCita;
    private CardView cr_editarPerfil;
    private CardView cr_CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_usuario);

        init();
    }

    /**
     * método que permite iniciar los componentes*/
    private void init(){
        crAgendarCita = (CardView) findViewById(R.id.crAgendarCita);
        cr_listaCita = (CardView) findViewById(R.id.cr_listaCita);
        cr_CerrarSesion = (CardView) findViewById(R.id.cr_CerrarSesion);
        cr_editarPerfil = (CardView) findViewById(R.id.cr_editarPerfil);

        crAgendarCita.setOnClickListener(this);
        cr_listaCita.setOnClickListener(this);
        cr_editarPerfil.setOnClickListener(this);
        cr_CerrarSesion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crAgendarCita:
                Intent agendar = new Intent(this, ListaDependenciaActivity.class);
                startActivity(agendar);
                break;
            case R.id.cr_listaCita:
                Intent listaCita = new Intent(this, ListaCitasActivity.class);
                startActivity(listaCita);
                break;
            case R.id.cr_editarPerfil:
                Intent perfil = new Intent(this, PerfilActivity.class);
                startActivity(perfil);
                break;
            case R.id.cr_CerrarSesion:
                cerrarSesion();
                break;
        }
    }

    //método que permite cerrar la sesion del usuario actual
    private void cerrarSesion(){
        Intent cerrarSesion = new Intent(this, MainActivity.class);

        Preferences.savePreferenceString(this, "", Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);
        Preferences.savePreferenceString(this, "", Constantes.PREFERENCIA_NOMBRES_CLAVE);
        Preferences.savePreferenceString(this, "", Constantes.PREFERENCIA_APELLIDOS_CLAVE);
        Preferences.savePreferenceString(this, "", Constantes.PREFERENCIA_TELEFONOS_CLAVE);
        Preferences.savePreferenceString(this, "", Constantes.PREFERENCIA_CORREO_CLAVE);
        Preferences.savePreferenceString(this, "", Constantes.PREFERENCIA_TIPO_USUARIO_CLAVE);
        Preferences.savePreferenceBoolean(this, Constantes.ESTADO_PREFERENCIA_FALSE, Constantes.PREFERENCIA_SESION_CLAVE);

        startActivity(cerrarSesion);
        finish();
    }
}
