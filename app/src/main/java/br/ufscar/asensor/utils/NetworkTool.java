/* Classe criada para modelar uma ferramenta de acesso a informacoes de rede
 *
 * Marcelo Barbosa,
 * novembro, 2017
 */


// declaracao do pacote
package br.ufscar.asensor.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;


// declaracao da classe
public class NetworkTool
{
    // metodos estaticos
    public static boolean getConnectionStatus(Activity context)
    {
        // retorna verdadeiro se o dispositivo esta conectado em uma rede
        // declaracao de variaveis
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean status = false;

        if((connectivityManager.getActiveNetworkInfo() != null) &&
                (connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()))
        {
            // altera o valor da variavel logica
            status = true;
        }


        // retorno de valor
        return status;
    }

    public static void enableExternalConnection()
    {
        // habilita a conexao via http
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
