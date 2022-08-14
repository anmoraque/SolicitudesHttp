package com.anmoraque.solicitudeshttp

import android.os.AsyncTask
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.jvm.Throws


//Esta clase se usa nativamente para solicitar HTTP
//Hay que implementarle que sea Asincrona (Para que se ejecute de forma paralela)
//Te pide el parametro (String), el progreso(Void) y la respuesta(String)
//Para comunicar esta clase con la interfaz CompletadoListener colocamos un parametro
//que es una variable de CompletadoListener
class SolicitudHTTPNativa(var completadoListener: CompletadoListener): AsyncTask<String, Void, String>() {
    //Aqui procesamos la descarga de los datos
    override fun doInBackground(vararg params: String): String?
    {
        //Por si algo falla hacemos try catch
        try {
            //¿Que voy hacer con la respuesta? pues retorno la función descargarDatos con parametro 0
            return descargarDatos(params[0])
        }catch (e: IOException){return null}
    }
    //Aqui mandamos los datos una vez procesada la descarga
    override fun onPostExecute(result: String) {
        //Por si algo falla hacemos try catch
        try {
            //¿Que voy hacer con la respuesta? pues la mandamos mediante la variable
            //de la interfaz completadoListener y su funcion descargaCompleta
            completadoListener?.descargaCompleta(result)

        }catch (e:Exception){ }
    }
    //Creo la funcion para la descarga de la url
    //Pongo un @Throws por si en la recepcion de datos hay algun error
    @Throws (IOException:: class)
    private fun descargarDatos(url: String): String
    {
        //Creo una variable para manejar el flujo de datos
        var inputStream: InputStream? = null
        //Por si algo falla hacemos try finally
        try{
            //Creo la variable URL donde va la url que pido en la función
            val url = URL(url)
            //Creamos la conexion, la abrimos y la mapeo a HttpURLConnection
            val conexion = url.openConnection() as HttpURLConnection
            //Obtengo información con GET
            conexion.requestMethod = "GET"
            //Mando a llamar la URL
            conexion.connect()
            //Dame el flujo de datos de mi variable conexion
            inputStream = conexion.inputStream
            //Lo retornamos en forma de String
            return inputStream.bufferedReader().use { it.readText() }
            //Al finalizar la conexion
            }finally {
                //Si inputStream es diferente de nulo lo cierro
                if (inputStream != null){inputStream.close()}
            }
    }

}