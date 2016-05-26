package fia.ues.sv.basedatosdr12004.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fia.ues.sv.basedatosdr12004.Controladores.Alumno;
import fia.ues.sv.basedatosdr12004.Controladores.Materia;
import fia.ues.sv.basedatosdr12004.Controladores.Nota;

public class ControlDBDR12004 {
    private static final String[] camposAlumno = new String[]{"carnet", "nombre", "apellido", "sexo", "matganadas"};
    private static final String[] camposNota = new String[]{"carnet", "codmateria", "ciclo", "notafinal"};
    private static final String[] camposMateria = new String[]{"codmateria", "nommateria", "unidadesval"};

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ControlDBDR12004(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String BASE_DATOS = "alumno.s3db";
        private static final int VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, BASE_DATOS, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE alumno(carnet VARCHAR(7) NOT NULL PRIMARY KEY, nombre VARCHAR(30), apellido VARCHAR(30), sexo VARCHAR(1), matganadas INTEGER); ");
                db.execSQL("CREATE TABLE materia(codmateria VARCHAR(6) NOT NULL PRIMARY KEY, nommateria VARCHAR(30), unidadesval VARCHAR(1)); ");
                db.execSQL("CREATE TABLE nota(carnet VARCHAR(7) NOT NULL ,codmateria VARCHAR(6)NOT NULL, ciclo VARCHAR(5), notafinal REAL, PRIMARY KEY(carnet, codmateria, ciclo)); ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return;
    }

    public void cerrar() {
        DBHelper.close();
    }

    public String insertar(Alumno alumno) {
        String regInsertados = "Registro Insertado No= ";
        long contador = 0;

        ContentValues alum = new ContentValues();

        alum.put("carnet", alumno.getCarnet());
        alum.put("nombre", alumno.getNombre());
        alum.put("apellido", alumno.getApellido());
        alum.put("sexo", alumno.getSexo());
        alum.put("matganadas", alumno.getMatganadas());

        contador = db.insert("alumno", null, alum);

        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado.Verificar inserción ";
        } else {
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Nota nota) {
        String regInsertados = "Registro Insertado No= ";
        long contador = 0;


        if (verificarIntegridad(nota,1)) {
            ContentValues notas = new ContentValues();

            notas.put("carnet", nota.getCarnet());
            notas.put("codmateria", nota.getCodmateria());
            notas.put("ciclo", nota.getCiclo());
            notas.put("nota final", nota.getNotafinal());

            contador = db.insert("nota", null, notas);
        }

        if (contador == -1 || contador == 0) {
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar inserción ";
        } else {
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Materia materia) { return null; }

    public String actualizar(Alumno alumno) {
        if (verificarIntegridad(alumno, 5)) {
            String[] id = {alumno.getCarnet()};
            ContentValues cv = new ContentValues();
            cv.put("nombre", alumno.getNombre());
            cv.put("apellido", alumno.getApellido());
            cv.put("sexo", alumno.getSexo());
            db.update("alumno", cv, "carnet = ?", id);
            return "Registro Actualizado Correctamente";
        } else {
            return "Registro con carnet " + alumno.getCarnet() + " no existe";
        }

    }

    public String actualizar(Materia materia) {
        return null;
    }

    public String actualizar(Nota nota) {
        return null;
    }

    public String eliminar(Alumno alumno) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        if (verificarIntegridad(alumno, 3)) {
            contador += db.delete("nota", "carnet='" + alumno.getCarnet() + "'", null);
        }
        contador += db.delete("alumno", "carnet='" + alumno.getCarnet() + "'", null);
        regAfectados += contador;
        return regAfectados;

    }

    public String eliminar(Nota nota) {
        return null;
    }

    public Alumno consultarAlumno(String carnet) {
        String[] id = {carnet};
        Cursor cursor = db.query("alumno", camposAlumno, "carnet = ?", id, null, null, null);
        if (cursor.moveToFirst()) {
            Alumno alumno = new Alumno();
            alumno.setCarnet(cursor.getString(0));
            alumno.setNombre(cursor.getString(1));
            alumno.setApellido(cursor.getString(2));
            alumno.setSexo(cursor.getString(3));
            alumno.setMatganadas(cursor.getInt(4));
            return alumno;
        } else {
            return null;
        }

    }

    public Materia consultarMateria(String codmateria) {
        return null;
    }

    public Nota consultarNota(String carnet, String codmateria, String ciclo) {
        return null;
    }

    private boolean verificarIntegridad(Object dato, int relacion) throws SQLException {
        switch (relacion) {
            case 1: {
                //verificar que al insertar nota exista carnet del alumno y el codigo de materia
                Nota nota = (Nota) dato;
                String[] id1 = {nota.getCarnet()};
                String[] id2 = {nota.getCodmateria()};
                //abrir();
                Cursor cursor1 = db.query("alumno", null, "carnet = ?", id1, null, null, null);
                Cursor cursor2 = db.query("materia", null, "codmateria = ?", id2, null, null, null);
                if (cursor1.moveToFirst() && cursor2.moveToFirst()) {
                    //Se encontraron datos
                    return true;
                }
                return false;
            }
            case 2: {
                //verificar que al modificar nota exista carnet del alumno, el codigo de materia y el ciclo
                Nota nota1 = (Nota) dato;
                String[] ids = {nota1.getCarnet(), nota1.getCodmateria(), nota1.getCiclo()};
                abrir();
                Cursor c = db.query("nota", null, "carnet = ? AND codmateria = ? AND ciclo = ? ", ids, null, null, null);
                if (c.moveToFirst()) {
                    //Se encontraron datos
                    return true;
                }
                return false;
            }
            case 3: {
                Alumno alumno = (Alumno) dato;
                Cursor c = db.query(true, "nota", new String[]{"carnet"}, "carnet='" + alumno.getCarnet() + "'", null, null, null, null, null);
                if (c.moveToFirst())
                    return true;
                else
                    return false;
            }
            case 4: {
                Materia materia = (Materia) dato;
                Cursor cmat = db.query(true, "nota", new String[]{"codmateria"}, "codmateria='" + materia.getCodmateria() + "'", null, null, null, null, null);
                if (cmat.moveToFirst()) {
                    return true;
                } else {
                    return false;
                }
            }

            case 5: {
                //verificar que exista alumno
                Alumno alumno2 = (Alumno) dato;
                String[] id = {alumno2.getCarnet()};
                abrir();
                Cursor c2 = db.query("alumno", null, "carnet = ?", id, null, null, null);

                if (c2.moveToFirst()) {
                    //Se encontro Alumno
                    return true;
                }
                return false;
            }
            case 6: {
                //verificar que exista Materia
                Materia materia2 = (Materia) dato;
                String[] idm = {materia2.getCodmateria()};
                abrir();
                Cursor cm = db.query("materia", null, "codmateria = ?", idm, null, null, null);
                if (cm.moveToFirst()) {
                    //Se encontro Materia
                    return true;
                }
                return false;
            }

            default:
                return false;
        }
    }

    public String llenarDBDR12004() {
        final String[] VAcarnet = {"OO12035", "OF12044", "GG11098", "CC12021"};
        final String[] VAnombre = {"Carlos", "Pedro", "Sara", "Gabriela"};
        final String[] VAapellido = {"Orantes", "Ortiz", "Gonzales", "Coto"};
        final String[] VAsexo = {"M", "M", "F", "F"};
        final String[] VMcodmateria = {"MAT115", "PRN115", "IEC115", "TSI115"};
        final String[] VMnommateria = {"Matematica I", "Programacion I", "Ingenieria Economica", " Teoria de Sistemas"};
        final String[] VMunidadesval = {"4", "4", "4", "4"};
        final String[] VNcarnet = {"OO12035", "OF12044", "GG11098", "CC12021", "OO12035", "GG11098", "OF12044"};
        final String[] VNcodmateria = {"MAT115", "PRN115", "IEC115", "TSI115", "IC115", "MAT115", "PRN115"};
        final String[] VNciclo = {"12016", "12016", "22016", "22016", "22016", "12016", "22016"};
        final float[] VNnotafinal = {7, 5, 8, 7, 6, 10, 7};

        abrir();
        db.execSQL("DELETE FROM alumno");
        db.execSQL("DELETE FROM materia");
        db.execSQL("DELETE FROM nota");

        Alumno alumno = new Alumno();
        for (int i = 0; i < 4; i++) {
            alumno.setCarnet(VAcarnet[i]);
            alumno.setNombre(VAnombre[i]);
            alumno.setApellido(VAapellido[i]);
            alumno.setSexo(VAsexo[i]);
            alumno.setMatganadas(0);
            insertar(alumno);
        }

        Materia materia = new Materia();
        for (int i = 0; i < 4; i++) {
            materia.setCodmateria(VMcodmateria[i]);
            materia.setNommateria(VMnommateria[i]);
            materia.setUnidadesval(VMunidadesval[i]);
            insertar(materia);
        }

        Nota nota = new Nota();
        for (int i = 0; i < 7; i++) {
            nota.setCarnet(VNcarnet[i]);
            nota.setCodmateria(VNcodmateria[i]);
            nota.setCiclo(VNciclo[i]);
            nota.setNotafinal(VNnotafinal[i]);
            insertar(nota);
        }

        cerrar();
        return "Guardo Correctamente";
    }
}

