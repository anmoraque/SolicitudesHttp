package com.anmoraque.solicitudeshttp


//Creamos una interfaz para comunicar la clase SolicitudHTTPNativa
//con el MainActivity y mandarle la información (Hace de puente)
//Se activa cuando se completa el proceso
interface CompletadoListener {
    //Defino una funcion puente que lleva un String (los datos de la URL)
    fun descargaCompleta(resultado: String)
}