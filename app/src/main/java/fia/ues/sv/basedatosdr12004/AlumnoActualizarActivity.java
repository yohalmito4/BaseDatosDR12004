package fia.ues.sv.basedatosdr12004;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fia.ues.sv.basedatosdr12004.Controladores.Alumno;
import fia.ues.sv.basedatosdr12004.Helpers.ControlDBDR12004;

public class AlumnoActualizarActivity extends Activity {
    ControlDBDR12004 helper;
    EditText editCarnet;
    EditText editNombre;
    EditText editApellido;
    EditText editSexo;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_actualizar);
        helper = new ControlDBDR12004(this);
        editCarnet = (EditText) findViewById(R.id.editCarnet);
        editNombre = (EditText) findViewById(R.id.editNombre);
        editApellido = (EditText) findViewById(R.id.editApellido);
        editSexo = (EditText) findViewById(R.id.editSexo);
    }

    public void actualizarAlumno(View v) {
        Alumno alumno = new Alumno();
        alumno.setCarnet(editCarnet.getText().toString());
        alumno.setNombre(editNombre.getText().toString());
        alumno.setApellido(editApellido.getText().toString());
        alumno.setSexo(editSexo.getText().toString());
        helper.abrir();
        String estado = helper.actualizar(alumno);
        helper.cerrar();
        Toast.makeText(this, estado, Toast.LENGTH_SHORT).show();
    }

    public void limpiarTexto(View v) {
        editCarnet.setText("");
        editNombre.setText("");
        editApellido.setText("");
        editSexo.setText("");
    }
}

