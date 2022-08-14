package com.anmoraque.solicitudeshttp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


/*
En esta clase
Validamos si hay internet o no despues de dar los permisos a internet
*/
class Network {
    //Creamos un objeto estatico
    companion object {
        //Aqui creamos una funcion que regrese un boleano y le llegue el contexto
        //para poder usarla en cualquier actividad
        fun hayRed(context: Context): Boolean {
            //Creamos una variable para el resultado
            var result = false
            //Creamos una variable de conectivityManager que es quien administra temas de Red
            //El metodo getSystemService() forma parte de una actividad y le llega CONNECTIVITY_SERVICE mediante contexto
            //Luego se mapea a el tipo ConnectivityManager para que funcione (Se sepa el tipo que es)
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //Creamos un condicional para según en la versión que estemos
            //probar si hay red del modo nuevo o del modo antiguo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Creamos una variable que lee de la anterior (connectivityManager)
                // para saber si esta activa la red
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                //Creamos otra variable para saber la info de la red y saber como está
                val networkInfo =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                //Resultado será true cuando este conectado a wifi, datos o eternet y si no será false
                result = when {
                    networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            //En la versión antigua
            } else {
                //Arranco connectivityManager, miro si hay info de red
                connectivityManager.run { connectivityManager.activeNetworkInfo?.run {
                    //Resultado será true cuando este conectado a wifi, datos o eternet y si no será false
                    result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }
                    }
                }
            }
            //Retorno el resultado de una u otra versión
            return result
            }
        }
    }