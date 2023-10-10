package com.itravel04.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.AccessToken


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Configuración para el inicio de sesión con Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configuración para el inicio de sesión con Facebook
        callbackManager = CallbackManager.Factory.create()

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val btn_login = findViewById<MaterialButton>(R.id.btn_login)
        val direct_signin = findViewById<TextView>(R.id.direct_sign_in)
        val forgot_password = findViewById<TextView>(R.id.forgot_password)
        val logGoogleButton = findViewById<ImageView>(R.id.log_google)
        val logFbButton = findViewById<ImageView>(R.id.log_fb)

        btn_login.setOnClickListener {
            val usernametext = username.text.toString()
            val passwordtext = password.text.toString()

            // Verificar si el usuario ha verificado su cuenta
            auth.signInWithEmailAndPassword(usernametext, passwordtext).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = auth.currentUser

                    // Verificar si el usuario ha verificado su cuenta
                    if (user?.isEmailVerified == true) {
                        // El usuario ha verificado su cuenta
                        Toast.makeText(this, "Inicio de sesión exitoso. ¡Hola, ${user?.email}!", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, HomeActivity::class.java)
                        startActivity(intent2)
                    } else {
                        // El usuario no ha verificado su cuenta
                        Toast.makeText(this, "Debes verificar tu cuenta de correo electrónico antes de iniciar sesión.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Inicio de sesión fallido
                    val exception = task.exception
                    Toast.makeText(this, "Inicio de sesión fallido: ${exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        direct_signin.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        forgot_password.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        logGoogleButton.setOnClickListener {
            // Inicia el proceso de inicio de sesión con Google
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        logFbButton.setOnClickListener {
            // Inicia el proceso de inicio de sesión con Facebook
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }

        // Configurar el callback para el inicio de sesión con Facebook
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                // Se ejecuta si el usuario cancela el inicio de sesión en Facebook
                Toast.makeText(this@LoginActivity, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                // Se ejecuta si hay un error en el inicio de sesión en Facebook
                Toast.makeText(this@LoginActivity, "Error en el inicio de sesión: ${error?.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Inicio de sesión con Google exitoso, autenticamos con Firebase
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Inicio de sesión con éxito
                            val user = auth.currentUser
                            Toast.makeText(this, "Inicio de sesión exitoso. ¡Hola, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
                            val intent2 = Intent(this, HomeActivity::class.java)
                            startActivity(intent2)
                        } else {
                            // Fallo en el inicio de sesión con Firebase
                            Toast.makeText(this, "Inicio de sesión fallido: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: ApiException) {
                // Error al iniciar sesión con Google
                Toast.makeText(this, "Error al iniciar sesión con Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Pasar el resultado del inicio de sesión de Facebook al callbackManager
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión con éxito
                    val user = auth.currentUser
                    Toast.makeText(this, "Inicio de sesión exitoso. ¡Hola, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
                } else {
                    // Fallo en el inicio de sesión con Firebase
                    Toast.makeText(this, "Inicio de sesión fallido: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}