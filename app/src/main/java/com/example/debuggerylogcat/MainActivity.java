package com.example.debuggerylogcat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText etA, etB;
    private Button btnDividir;
    private TextView tvResultado;

    private static final String TAG = "CESBA_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Inicializar Vistas
        inicializarVistas();

        // 2. Configurar Insets (Edge-to-Edge)
        configurarWindowInsets();

        // 3. Configurar Eventos
        configurarEventos();

        Log.i(TAG, "App iniciada correctamente (onCreate)");
    }

    private void inicializarVistas() {
        etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB);
        btnDividir = findViewById(R.id.btnDividir);
        tvResultado = findViewById(R.id.tvResultado);
    }

    private void configurarWindowInsets() {
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void configurarEventos() {
        btnDividir.setOnClickListener(v -> realizarDivision());
    }

    private void realizarDivision() {
        Log.d(TAG, "== Iniciando Proceso de División ==");

        String aTxt = etA.getText().toString().trim();
        String bTxt = etB.getText().toString().trim();

        // 1. Validación de campos vacíos
        if (aTxt.isEmpty() || bTxt.isEmpty()) {
            Log.w(TAG, "Validación fallida: Campos vacíos");
            Toast.makeText(this, getString(R.string.msg_completa_campos), Toast.LENGTH_SHORT).show();
            tvResultado.setText(getString(R.string.resultado_inicial));
            return;
        }

        try {
            // Convertimos a double para permitir decimales (calculadora real)
            double numA = Double.parseDouble(aTxt);
            double numB = Double.parseDouble(bTxt);

            Log.i(TAG, "Valores capturados: A=" + numA + ", B=" + numB);

            // 2. Validación de división por cero
            if (numB == 0) {
                // Forzamos un log de error con stack trace para práctica de Logcat
                ArithmeticException e = new ArithmeticException("División por cero detectada manualmente");
                Log.e(TAG, "ERROR CRÍTICO: Intento de división por cero", e);
                
                tvResultado.setText(getString(R.string.error_div_zero));
                Toast.makeText(this, getString(R.string.msg_no_cero), Toast.LENGTH_SHORT).show();
            } else {
                double resultado = numA / numB;
                
                // Formateamos el resultado para mostrarlo
                String resultadoTxt = getString(R.string.resultado_formato, String.valueOf(resultado));
                tvResultado.setText(resultadoTxt);
                
                Log.d(TAG, "Operación exitosa: " + numA + " / " + numB + " = " + resultado);
            }

        } catch (NumberFormatException e) {
            // Este catch captura si el usuario ingresa algo que no es un número
            Log.e(TAG, "Error de formato: No se pudo convertir a número. Detalle: " + e.getMessage());
            tvResultado.setText(getString(R.string.error_formato));
            Toast.makeText(this, getString(R.string.msg_solo_numeros), Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            // Catch genérico para cualquier otro error inesperado
            Log.e(TAG, "Error inesperado en la aplicación", e);
            Toast.makeText(this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "== Fin del Proceso de División ==");
    }
}
