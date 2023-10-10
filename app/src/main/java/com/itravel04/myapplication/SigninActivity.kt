package com.itravel04.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = FirebaseAuth.getInstance()

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signin_btn = findViewById<MaterialButton>(R.id.signin_btn)
        val direct_log_in = findViewById<TextView>(R.id.direct_log_in)

        signin_btn.setOnClickListener {
            val usernametext = username.text.toString()
            val passwordtext = password.text.toString()

            // Crear un nuevo usuario con correo electrónico y contraseña
            auth.createUserWithEmailAndPassword(usernametext, passwordtext)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registro exitoso
                        val user = auth.currentUser

                        // Enviar correo electrónico de verificación
                        user?.sendEmailVerification()

                        // Mostrar mensaje de éxito
                        Toast.makeText(
                            this,
                            "Registro exitoso. Se ha enviado un correo electrónico de verificación a ${user?.email}.",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Dirigir al usuario a la pantalla de inicio de sesión
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Registro fallido
                        val exception = task.exception
                        Toast.makeText(
                            this,
                            "Registro fallido: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        direct_log_in.setOnClickListener {
            // Dirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
