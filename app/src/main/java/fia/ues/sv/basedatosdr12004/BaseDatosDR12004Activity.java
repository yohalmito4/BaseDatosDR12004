package fia.ues.sv.basedatosdr12004;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import fia.ues.sv.basedatosdr12004.Helpers.ControlDBDR12004;

public class BaseDatosDR12004Activity extends ListActivity {
    String[] menu = {"Tabla Alumno", "Tabla Nota", "Tabla Materia", "LLenar Base de Datos"};
    String[] activities = {"AlumnoMenuActivity", "NotaMenuActivity", "MateriaMenuActivity"};
    ControlDBDR12004 BDhelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, menu));
        BDhelper=new ControlDBDR12004(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position != 3) {
            String nombreValue = activities[position];
            try {
                Class<?> clase = Class.forName("fia.ues.sv.basedatosdr12004.Menus." + nombreValue);
                Intent inte = new Intent(this, clase);
                this.startActivity(inte);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            BDhelper.abrir();
            String tost = BDhelper.llenarDBDR12004();
            BDhelper.cerrar();
            Toast.makeText(this, tost, Toast.LENGTH_SHORT).show();

        }
    }
}



