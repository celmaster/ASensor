/* Classe criada para criar caixas de dialogos
 * 
 * Marcelo Barbosa,
 * julho, 2017.
 */

// declaracao do pacote
package br.ufscar.asensor.utils;

// importacao de bibliotecas
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import br.mb.web.expressmessage.ExpressMessage;
import org.json.JSONObject;

import br.ufscar.asensor.R;
import br.ufscar.asensor.config.Configs;

// declaracao da classe
public class DialogHandler 
{
	// declaracao de atributos
	
	// declaracao de metodos
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

	public static void createToast(String message, Context context)
    {
        // cria um toast
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

	public static void showInputDialog(final Activity context)
    {
        // obtem os dados do servidor
        if(NetworkTool.getConnectionStatus(context))
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.setTitle("Endereço do servidor:");

            Button button = (Button) dialog.findViewById(R.id.btnInputDialog);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    EditText editText = (EditText) dialog.findViewById(R.id.serverURL);
                    Configs.SERVER_URL = editText.getText().toString();
                    Configs.SERVICE_URL = "http://" + Configs.SERVER_URL + "/SM/Library/Services/ExpressMessageService.php";

                    ExpressMessage expressmessage = new ExpressMessage("ASensor", Configs.SERVICE_URL);

                    if (expressmessage.isAlive()) {
                        // obtem as informacoes do servidor para o sensor
                        JSONObject json = expressmessage.getInfo();

                        try {
                            Configs.SERVER_NAME = json.getString("appName");
                            Configs.FIELDS = json.getJSONArray("requestedData");

                            // avisa o usuario do sucesso da operacao
                            DialogHandler.createAlertDialog("Conexão com o servidor realizada com sucesso.",
                                    "Aviso:", context);

                        } catch (Exception e) {
                            Log.e("<JSON_ERROR>", "Erro ao recuperar dados do servidor");
                            dialog.dismiss();
                        }
                    } else {
                        // avisa ao usuario da falha na operacao
                        DialogHandler.createAlertDialog("Verifique se o endereço foi informado " +
                                        "corretamente ou entre em contato com o desenvolvedor do projeto",
                                "Erro ao acessar servidor:", context);

                        // reseta os dados dos atributos estaticos da classe de configuracao
                        Configs.SERVER_URL = "";
                        Configs.SERVICE_URL = "";
                    }

                    dialog.dismiss();

                }
            });

            dialog.show();
        }else
            {
                DialogHandler.createAlertDialog("Seu dispositivo não está conectado a uma rede","Sem conexão com a rede:", context);
            }
    }


}
