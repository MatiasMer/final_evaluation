package com.example.evfn_escaner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

// Clase BDD que extiende SQLiteOpenHelper para manejar la base de datos
public class BDD extends SQLiteOpenHelper {
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "Productos.db";
    // Nombre de la tabla
    private static final String TABLE_NAME = "Produc";
    // Nombres de las columnas
    private static final String COL_1 = "ID";
    private static final String COL_2 = "Name";
    private static final String COL_3 = "precio";
    private static final String COL_4 = "image";

    // Constructor que inicializa la base de datos
    public BDD(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Método llamado al crear la base de datos por primera vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla con todas las columnas necesarias
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID autoincremental
                COL_2 + " TEXT, " +                                // Nombre del producto
                COL_3 + " REAL, " +                                // Precio del producto
                COL_4 + " BLOB" +                                  // Imagen del producto en formato BLOB
                ")";
        db.execSQL(createTable); // Ejecutar la sentencia SQL para crear la tabla
    }

    // Método llamado cuando se actualiza la versión de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla existente si existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); // Crear una nueva tabla
    }

    // Método para insertar un nuevo producto en la base de datos
    public boolean insertProducto(String name, double precio, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtener acceso de escritura a la base de datos
        ContentValues contentValues = new ContentValues(); // Crear un objeto ContentValues para almacenar los valores a insertar

        // Agregar los valores al ContentValues
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, precio);
        contentValues.put(COL_4, image);

        // Insertar los valores en la tabla y obtener el resultado
        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1; // Retornar true si la inserción fue exitosa (no -1)
    }

    // Método para actualizar el precio de un producto existente
    public boolean updatePrecio(String name, double nuevoPrecio) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtener acceso de escritura a la base de datos
        ContentValues contentValues = new ContentValues(); // Crear un objeto ContentValues

        contentValues.put(COL_3, nuevoPrecio); // Agregar el nuevo precio al ContentValues

        // Actualizar el precio del producto donde el nombre coincide y obtener el resultado
        int result = db.update(TABLE_NAME, contentValues, COL_2 + " = ?", new String[]{name});

        return result > 0; // Retornar true si se actualizó al menos un registro
    }

    // Método para actualizar la imagen de un producto existente
    public boolean updateImage(String name, byte[] newImage) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtener acceso de escritura a la base de datos
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_4, newImage); // Agregar la nueva imagen al ContentValues

        // Actualizar la imagen del producto donde el nombre coincide y obtener el resultado
        int result = db.update(TABLE_NAME, contentValues, COL_2 + " = ?", new String[]{name});

        return result > 0; // Retornar true si se actualizó al menos un registro
    }

    // Método para obtener todos los productos almacenados en la base de datos
    public ArrayList<Producto> getAllProducts() {
        ArrayList<Producto> productos = new ArrayList<>(); // Crear una lista para almacenar los productos obtenidos
        SQLiteDatabase db = this.getReadableDatabase(); // Obtener acceso de lectura a la base de datos

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // Ejecutar una consulta SQL para seleccionar todos los productos

        if (res.moveToFirst()) { // Verificar si hay resultados disponibles
            do {
                Producto producto = new Producto(
                        res.getInt(0),      // ID del producto (columna 0)
                        res.getString(1),   // Nombre del producto (columna 1)
                        res.getDouble(2),   // Precio del producto (columna 2)
                        res.getBlob(3)      // Imagen del producto (columna 3)
                );
                productos.add(producto); // Agregar el producto a la lista
            } while (res.moveToNext()); // Continuar mientras haya más resultados disponibles
        }

        res.close(); // Cerrar el cursor para liberar recursos
        return productos; // Retornar la lista completa de productos
    }

    // Método para eliminar un producto basado en su nombre
    public boolean deleteProducto(String name) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtener acceso de escritura a la base de datos

        return db.delete(TABLE_NAME, COL_2 + " = ?", new String[]{name}) > 0;
        // Retornar true si se eliminó al menos un registro basado en el nombre proporcionado
    }
}

// Clase auxiliar para manejar los productos almacenados en la base de datos.
class Producto {
    private int id;       // ID del producto
    private String name;  // Nombre del producto
    private double precio;  // Precio del producto
    private byte[] image;   // Imagen del producto en formato BLOB

    public Producto(int id, String name, double precio, byte[] image) {
        this.id = id;
        this.name = name;
        this.precio = precio;
        this.image = image;
    }

    // Métodos getter y setter para acceder y modificar los atributos del producto.

    public int getId() { return id; }

    public String getName() { return name; }

    public double getPrecio() { return precio; }

    public byte[] getImage() { return image; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setPrecio(double precio) { this.precio = precio; }

    public void setImage(byte[] image) { this.image = image; }
}