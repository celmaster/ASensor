package br.ufscar.asensor.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Configs
{
	// declaracao de constantes para configurar a comunicacao com o servidor

	// endereco do servidor
	public static final String SERVER_URL = "http://192.168.1.100/";

	// endereco do servico
	public static final String SERVICE_URL = SERVER_URL + "SM/Library/Services/ExpressMessageService.php";

	// metodos estaticos
	public static JSONObject getDataRequest() throws JSONException
	{
		// retorna os metadados para a solicitacao de dados a um dispositivo
		// declaracao de variaveis
		JSONObject request = new JSONObject(); 	// JSON que ira agregar demais dados e metadados
		JSONObject info = new JSONObject(); 	// JSON descreve os dados do ASensor para o dispositivo destinatario
		JSONArray fields = new JSONArray();  	// informa os dados que serao solicitados ao dispositivo destinatario

		// dados solicitados
		fields.put("EmotionalState");

		// informacoes do ASensor
		info.put("app_name", "usersEmotionApp");
		info.put("mac_address", "usersEmotionApp");
		info.put("profile_fields", fields);

		// requisicao
		request.put("info", info);

		// retorno de valor
		return request;
	}

}
