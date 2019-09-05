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

public class InitialUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView crAgendarCita;

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

        crAgendarCita.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crAgendarCita:
                Intent agendar = new Intent(this, ListaDependenciaActivity.class);
                startActivity(agendar);
                break;
        }
    }
}
