package br.ufscar.asensor.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Configs
{
	// declaracao de constantes para configurar a comunicacao com o servidor

	// endereco do servidor
	public static String SERVER_URL = "";

	// endereco do servico
	public static String SERVICE_URL = "";

	// nome da aplicacao do servidor
	public static String SERVER_NAME = "";

	// campos a serem requisitados
	public static JSONArray FIELDS = null;



	// metodos estaticos
	public static JSONObject getDataRequest() throws JSONException
	{
		// retorna os metadados para a solicitacao de dados a um dispositivo
		// declaracao de variaveis
		JSONObject request = new JSONObject(); 	// JSON que ira agregar demais dados e metadados
		JSONObject info = new JSONObject(); 	// JSON descreve os dados do ASensor para o dispositivo destinatario
		JSONArray fields = new JSONArray();  	// informa os dados que serao solicitados ao dispositivo destinatario

		// dados solicitados
		fields.put(Configs.FIELDS);

		// informacoes do ASensor
		info.put("app_name", Configs.SERVER_NAME);
		info.put("mac_address",Configs.SERVER_NAME);
		info.put("profile_fields", fields);

		// requisicao
		request.put("info", info);

		// retorno de valor
		return request;
	}

}
