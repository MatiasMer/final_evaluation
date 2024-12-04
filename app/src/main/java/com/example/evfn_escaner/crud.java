package com.example.evfn_escaner;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class crud extends AppCompatActivity {
    BDD  bdd;

    EditText ingresarTarea, editarTarea;

    Button btnAgregar, btnEditar, btnEliminar;

    ListView listarTareas;

    ArrayList<String> tareas;

    ArrayAdapter<String> adapter;

    String seleccionarTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.agrega);

        bdd = new BDD(this);
        ingresarTarea = findViewById(R.id.editTextTask);
        editarTarea = findViewById(R.id.editTextEditTask);
        btnAgregar = findViewById(R.id.buttonAdd);
        btnEditar = findViewById(R.id.buttonEdit);
        btnEliminar = findViewById(R.id.buttonDelete);
        listarTareas = findViewById(R.id.listViewTasks);

        tareas = bdd.getAllTask();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tareas);
        listarTareas.setAdapter(adapter);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tarea = ingresarTarea.getText().toString();
                if (!tarea.isEmpty()) {
                    bdd.InsertTask(tarea);
                    actualizarListadoTateas();
                    ingresarTarea.setText("");
                }
            }
        });

        listarTareas.setOnItemClickListener((parent, view, position, id) -> {
            seleccionarTareas = tareas.get(position);
            editarTarea.setText(seleccionarTareas);
            editarTarea.setVisibility(View.VISIBLE);
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevaTarea = editarTarea.getText().toString();
                if (!nuevaTarea.isEmpty() && seleccionarTareas != null) {
                    bdd.updateTask(seleccionarTareas, nuevaTarea);
                    actualizarListadoTateas();
                    editarTarea.setText("");
                    editarTarea.setVisibility(View.GONE);
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seleccionarTareas != null) {
                    bdd.deleteTask(seleccionarTareas);
                    actualizarListadoTateas();
                    editarTarea.setText("");
                    editarTarea.setVisibility(View.GONE);
                }
            }
        });
    }

    public void actualizarListadoTateas() {
        tareas.clear();
        tareas.addAll(bdd.getAllTask());
        adapter.notifyDataSetChanged();
    }
}
