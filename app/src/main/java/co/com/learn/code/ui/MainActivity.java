package co.com.learn.code.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.com.learn.code.R;
import co.com.learn.code.utils.Utilidades;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Etiqueta para la depuracion
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText txtUsuario;
    private EditText txtClave;
    private Button btnIniciarSesion;
    private TextView txtOlvidasteContraseña;
    private TextView txtCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cambiar el titulo del toolbar
        getSupportActionBar().setTitle(R.string.app_name_main);
        //iniciar atributos
        init();
    }

    //método que permite iniciar los componentes
    private void init(){
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtClave = (EditText) findViewById(R.id.txtClave);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        txtOlvidasteContraseña = (TextView) findViewById(R.id.txtOlvidasteContraseña);
        txtCrearCuenta = (TextView) findViewById(R.id.txtCrearCuenta);

        //implementar escuchadores
        btnIniciarSesion.setOnClickListener(this);
        txtOlvidasteContraseña.setOnClickListener(this);
        txtCrearCuenta.setOnClickListener(this);
    }

    //evento onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIniciarSesion:
                Intent intent = new Intent(this, InitialUsuarioActivity.class);
                startActivity(intent);
                break;
            case R.id.txtOlvidasteContraseña:
                Utilidades.showToast(this, "Recuperando Contraseña...");
                break;
            case R.id.txtCrearCuenta:
                Intent i = new Intent(this, RegistrarUsuarioActivity.class);
                if(Utilidades.materialDesign()){
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }else{
                    startActivity(i);
                }
                break;
        }
    }
}
