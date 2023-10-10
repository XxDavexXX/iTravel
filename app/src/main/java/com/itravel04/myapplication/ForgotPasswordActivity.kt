package com.itravel04.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        val username = findViewById<EditText>(R.id.username)
        val restorepassword_btn = findViewById<MaterialButton>(R.id.restorepassword_btn)
        val direct_log_in = findViewById<TextView>(R.id.direct_log_in)

        direct_log_in.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        restorepassword_btn.setOnClickListener {
            val emailtext = username.text.toString()
            auth.sendPasswordResetEmail(emailtext).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Se envió un correo electrónico al usuario con un enlace para restablecer su contraseña
                    Toast.makeText(this, "Se envió un correo electrónico al usuario con un enlace para restablecer su contraseña.", Toast.LENGTH_SHORT).show()
                } else {
                    // Se produjo un error al restablecer la contraseña del usuario
                    val exception = task.exception
                    Toast.makeText(this, "Se produjo un error al restablecer la contraseña del usuario: ${exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}