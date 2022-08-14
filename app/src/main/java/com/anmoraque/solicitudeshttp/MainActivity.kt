package com.anmoraque.solicitudeshttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import java.io.IOException
import java.lang.Exception


/*
En esta actividad hemos hablado de:
Pedir permisos a internet y validarlo
Solicitudes Nativas, HTTP Volley, okHTTP
*/

//Tengo que crear permiso para conectarme a internet en el manifest
//A la clase tengo que implementarle la interfaz CompletadoListener
//para que herede de ella, esto obliga a implementar la funcion
//descargaCompleta(resultado: String) de la interfaz
class MainActivity : AppCompatActivity(), CompletadoListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Validamos si hay internet o no despues de dar permisos a internet
        //Referenciamos el botón para probar la validación de la red
        var bValidarRed = findViewById<Button>(R.id.bValidarRed)
        //Añado el listener al button
        bValidarRed.setOnClickListener {
            //Codigo para validar red
            //Network tiene red (Se referencia con this, mi actividad actual)
            //En la class Network era context aqui this
            //Si hay red pongo un toast y si no otro
            if (Network.hayRed(this)) {
                Toast.makeText(this, "Si hay red", Toast.LENGTH_LONG).show()

            }else {
                Toast.makeText(this, "No hay red", Toast.LENGTH_LONG).show()
            }
        }

        //Hacemos la solicitud HTTP Volley
        //Referenciamos el botón para probar la solicitud con Volley
        var bSolicitudHTTPVolley = findViewById<Button>(R.id.bSolicitudHTTPVolley)
        //Añado el listener al button
        bSolicitudHTTPVolley.setOnClickListener {
            //Primero validamos si hay red
            if (Network.hayRed(this)) {
                //Llamamos a la función para descargar los datos
                solicitudHTTPVolley("https://www.google.com")
            }else {
                Toast.makeText(this, "No hay red", Toast.LENGTH_LONG).show()
            }
        }

        //Hacemos la solicitud HTTP OkHTTP
        //Referenciamos el botón para probar la solicitud con HTTPoK
        var bSolicitudOkHTTP = findViewById<Button>(R.id.bSolicitudOkHTTP)
        //Añado el listener al button
        bSolicitudOkHTTP.setOnClickListener {
            //Primero validamos si hay red
            if (Network.hayRed(this)) {
                //Llamamos a la función para descargar los datos
                solicitudHTTPOK("https://www.google.com")
            }else {
                Toast.makeText(this, "No hay red", Toast.LENGTH_LONG).show()
            }
        }

        //Hacemos la solicitud HTTP Nativa Asincrona
        //Referenciamos el botón para probar la solicitud Nativa
        var bSolicitudNativa = findViewById<Button>(R.id.bSolicitudNativa)
        //Añado el listener al button
        bSolicitudNativa.setOnClickListener {
            //Primero validamos si hay red
            if (Network.hayRed(this)) {
                //Llamamos a la función para descargar los datos y la ejecutamos con la url
                //Ponemos this porque heredamos de CompletadoListener y ejecutamos aqui
                SolicitudHTTPNativa(this).execute("https://www.google.com")

            }else {
                Toast.makeText(this, "No hay red", Toast.LENGTH_LONG).show()
            }
        }
    }
    //Aqui implementamos la funcion de la interfaz CompletadoListener
    override fun descargaCompleta(resultado: String) {
        //Aqui llega el resultado en String de la URL
        // de la clase SolicitudHTTPNativa
        //¿Que voy hacer con la respuesta?, en este caso un Log
        Log.d("ETIQUETA_LOG","solicitudHTTPNativa $resultado")
    }


    //Metodo para solicitar HTTP con volley
    //Voy a pedir una URL
    private fun solicitudHTTPVolley (url:String){
        //Esta libreria funciona a base de colas, es decir
        // administra multiples solicitudes HTTP a la vez
        //Creamos una variable de volley para una nueva solicitud
        val queue = Volley.newRequestQueue(this)
        //Esta variable nos permite construir la solicitud y el
        //resultado lo da en forma de String
        //Nos pide varios parametros (Tipo de solicitud en este caso GET para recibir,
        //una url, un listener de respuesta en este caso tipo String (donde escribo el codigo
        //a realizar con la respuesta) y al final un listener de error
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            //Aqui viene la respuesta
                response ->
            //Por si algo falla hacemos try catch
            try {
                //¿Que voy hacer con la respuesta?, en este caso un Log
                Log.d("ETIQUETA_LOG","solicitudHTTPVolley $response")
            }catch (e:Exception) {  }
        }, Response.ErrorListener {  })
        //Lo añadimos a la variable que hace la solicitud
        queue.add(solicitud)
    }


    //Metodo para solicitar HTTP con OkHTTP
    //Voy a pedir una URL
    private fun solicitudHTTPOK (url: String){
            //Creo el cliente HTTP
            val cliente = OkHttpClient()
            //Construyo la solicitud
            val solicitud = okhttp3.Request.Builder().url(url).build()
            //Pongo la solicitud en cola y manejo la devolución que me llega
            cliente.newCall(solicitud).enqueue(object: Callback{
                //Manejamos si ha ido bien o mal la solicitud en cola
                override fun onFailure(call: Call, e: IOException) {
                    //Implementamos si ha ido mal
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: okhttp3.Response) {
                    //Implementamos si ha ido bien
                    //La respuesta la metemos en el cuerpo del resultado y la hacemos String
                    val resultado = response.body.string()
                    this@MainActivity.runOnUiThread {
                        //Por si algo falla hacemos try catch
                        try {
                            //¿Que voy hacer con la respuesta?, en este caso un Log
                            Log.d("ETIQUETA_LOG", "solicitudHTTPOK $resultado")

                        } catch (e: Exception) {

                        }
                    }
                }
            })
    }
}