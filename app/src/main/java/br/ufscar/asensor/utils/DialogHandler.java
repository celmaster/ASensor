/* Classe criada para criar caixas de di�logos
 * 
 * Marcelo Barbosa,
 * julho, 2015.
 */

// declara��o do pacote
package br.ufscar.asensor.utils;

// importa��o de bibliotecas
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

// declara��o da classe
public class DialogHandler 
{
	// declara��o de atributos
	
	// declara��o de m�todos	
	public static void createAlertDialog(String message, String title, Context context)
	{
		// cria uma mensagem de alerta
		AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(context);
		builderAlertDialog.setMessage(message).setTitle(title);
		builderAlertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("<PositiveButton>", "Ok");				
			}
		});
		AlertDialog alertDialog = builderAlertDialog.create();
		alertDialog.show();
	}
}
