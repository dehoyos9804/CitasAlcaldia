package co.com.learn.code.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegresarToolbar;
    private TextView txtNombrePerfil;
    private LinearLayout txtEditarPerfil;
    private TextView cuenta_usuario;
    private TextView cuenta_telefono;
    private TextView cuenta_tipousuario;
    private TextView cuenta_direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        init();//iniciar componentes
    }

    private void init(){
        btnRegresarToolbar = (Button) findViewById(R.id.btnRegresarToolbar);
        txtNombrePerfil = (TextView) findViewById(R.id.txtNombrePerfil);
        txtEditarPerfil = (LinearLayout) findViewById(R.id.txtEditarPerfil);
        cuenta_usuario = (TextView) findViewById(R.id.cuenta_usuario);
        cuenta_telefono = (TextView) findViewById(R.id.cuenta_telefono);
        cuenta_tipousuario = (TextView) findViewById(R.id.cuenta_tipousuario);
        cuenta_direccion = (TextView) findViewById(R.id.cuenta_direccion);

        //cambio los textos por cada layout
        txtNombrePerfil.setText((Preferences.getPreferenceString(this, Constantes.PREFERENCIA_NOMBRES_CLAVE) + " " + Preferences.getPreferenceString(this, Constantes.PREFERENCIA_APELLIDOS_CLAVE)));
        cuenta_usuario.setText(Preferences.getPreferenceString(this, Constantes.PREFERENCIA_CORREO_CLAVE));
        cuenta_telefono.setText(("+57 " + Preferences.getPreferenceString(this, Constantes.PREFERENCIA_TELEFONOS_CLAVE)));
        cuenta_tipousuario.setText(Preferences.getPreferenceString(this, Constantes.PREFERENCIA_TIPO_USUARIO_CLAVE));
        cuenta_direccion.setText(Preferences.getPreferenceString(this, Constantes.PREFERENCIA_IDENTIFICACION_CLAVE));

        btnRegresarToolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegresarToolbar://boton regresar del toolbar
                finish();
                break;
        }
    }
}
