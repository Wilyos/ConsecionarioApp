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

public class FacturaActivity extends AppCompatActivity {

    EditText jetPlaca, jetMarca,jetModelo,jetValor,jetCodigo,jetFecha;
    CheckBox jcbActivo, jcbActivoF;
    ClsOpenHelper admin = new ClsOpenHelper(this, "Concesionario.db", null, 1);
    String placa, codigo, fecha;
    long resp, resp2;
    byte sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        jetPlaca=findViewById(R.id.etPlacaFac);
        jetMarca=findViewById(R.id.etMarcaFac);
        jetModelo=findViewById(R.id.etModeloFac);
        jetValor=findViewById(R.id.etValorFac);
        jcbActivo =findViewById(R.id.cbPlaca);
        jcbActivoF = findViewById(R.id.cbFac);
        jetCodigo=findViewById(R.id.etCodFactura);
        jetFecha=findViewById(R.id.etfacFecha);
        sw=0;

    }
    public void ConsultarPlaca(View view){
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
    public void Guardar(View view){
        codigo=jetCodigo.getText().toString();
        fecha=jetFecha.getText().toString();
        placa=jetPlaca.getText().toString();


        if(codigo.isEmpty()|| fecha.isEmpty() ||  placa.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetCodigo.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues registro1=new ContentValues();

            registro1.put("codigo",codigo);
            registro1.put("fecha",fecha);
            registro1.put("placa",placa);

            if(sw==0) {
                resp = db.insert("TblFactura", null, registro1);

                if (resp > 0) {
                    Anularf(view);
                    Toast.makeText(this, "Venta exitosa", Toast.LENGTH_SHORT).show();
                    Limpiar_campos();

                } else
                    Toast.makeText(this, "Error al generar la Venta", Toast.LENGTH_SHORT).show();

            }

            else{resp = db.update("TblFactura", registro1, "codigo='" + codigo + "'", null);
                sw = 0;
            }
            db.close();
        }
    }
    public void Consultar(View view){
        codigo=jetCodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "Codigo de factura requerida para la busqueda", Toast.LENGTH_SHORT).show();
            jetCodigo.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblFactura where codigo ='" + codigo +"'",null);
            ConsultarPlaca(view);
            if(fila.moveToNext()){
                sw=1;

                jetFecha.setText((fila.getString(1)));
                jetPlaca.setText((fila.getString(2)));
                if (fila.getString(3).equals("si")){
                    jcbActivoF.setChecked(true);
                }
                else{
                    jcbActivoF.setChecked(false);
                }
                ConsultarPlaca(view);
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
            resp=db.update("TblFactura",registro,"codigo='" + codigo +"'", null);
            if (resp > 0){
                ActivarPlaca(view);
                Toast.makeText(this, "Factura anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error al anular la factura", Toast.LENGTH_SHORT).show();
            db.close();
        }

        else{
            Toast.makeText(this, "Debe consultar para anular", Toast.LENGTH_SHORT).show();
        }
    }

    public void ActivarPlaca(View view){
        SQLiteDatabase db=admin.getWritableDatabase();
        ContentValues registro=new ContentValues();
        registro.put("activo","si");
        resp2 = db.update("TblVehiculo", registro, "placa='" + placa + "'", null);

    }

    public void Anularf(View view){
        if(sw==0) {
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("activo", "no");
            resp = db.update("TblVehiculo", registro, "placa='" + placa + "'", null);
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
        jetCodigo.setText("");
        jetFecha.setText("");
        jcbActivo.setChecked(false);
        jcbActivoF.setChecked((false));
        jetPlaca.requestFocus();
        sw=0;
    }

}