package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VehiculoActivity extends AppCompatActivity {

    EditText jetPlaca,jetMarca,jetModelo,jetValor;
    CheckBox jcbActivo;
    ClsOpenHelper admin = new ClsOpenHelper(this,"Concesionario.db",null,1);
    String placa, marca, modelo, valor;
    long resp;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        //quitar menu por defecto y asocair objetos java y xml

        jetPlaca=findViewById(R.id.etPlaca);
        jetMarca=findViewById(R.id.etMarca);
        jetModelo=findViewById(R.id.etModelo);
        jetValor=findViewById(R.id.etValor);
        jcbActivo =findViewById(R.id.cbActivo);
        sw=0;
    }

    public void Guardar(View view){
        placa=jetPlaca.getText().toString();
        marca=jetMarca.getText().toString();
        modelo=jetModelo.getText().toString();
        valor=jetValor.getText().toString();

        if(placa.isEmpty()|| marca.isEmpty() || modelo.isEmpty()||valor.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetPlaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("placa",placa);
            registro.put("marca",marca);
            registro.put("modelo",modelo);
            registro.put("valor",Integer.parseInt(valor));
            if(sw==0) {
                resp = db.insert("TblVehiculo", null, registro);
                if (resp > 0) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Limpiar_campos();
                } else {
                    Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                resp = db.update("TblVehiculo", registro, "placa='" + placa + "'", null);
                sw = 0;

                if(jcbActivo.isChecked()){
                resp=db.update("TblVehiculo",registro, "placa '" + placa + "'", null);
                }
                else{
                Toast.makeText(this, "El vehiculo existe pero no se puede modificar", Toast.LENGTH_SHORT).show();
                sw=0;
                }
            }
            db.close();
        }
    }
    public void Consultar(View view){
        placa=jetPlaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "Placa requerida para la busqueda", Toast.LENGTH_SHORT).show();
            jetPlaca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblVehiculo where placa='" + placa +"'",null);
            if(fila.moveToNext()){
                sw=1;
                jetMarca.setText((fila.getString(1)));
                jetModelo.setText((fila.getString(2)));
                jetValor.setText((fila.getString(3)));
                if (fila.getString(4).equals("si")){
                    jcbActivo.setChecked(true);
                }
                else{
                    jcbActivo.setChecked(false);
                }
            }
            else{
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void Anular(View view){
        if(sw==1){
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblVehiculo",registro,"placa='" + placa +"'", null);
           if (resp > 0){
               Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
               Limpiar_campos();
           }
           else
               Toast.makeText(this, "Error al anularel registro", Toast.LENGTH_SHORT).show();
            db.close();
        }

        else{
            Toast.makeText(this, "Debe consultar para anular", Toast.LENGTH_SHORT).show();
        }
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }

    public void regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
    private void Limpiar_campos(){
        jetPlaca.setText("");
        jetMarca.setText("");
        jetModelo.setText("");
        jetValor.setText("");
        jcbActivo.setChecked(false);
        jetPlaca.requestFocus();
        sw=0;
    }
}