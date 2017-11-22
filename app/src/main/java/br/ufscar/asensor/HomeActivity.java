/* Activity principal do aplicativo ASensor
 * 
 * Marcelo Barbosa,
 * outubro, 2016.
 */

// declaracao do pacote
package br.ufscar.asensor;

// importacao de pacotes
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import br.mb.web.expressmessage.ExpressMessage;
import br.ufscar.asensor.bluetooth.BluetoothClientListener;
import br.ufscar.asensor.config.Configs;
import br.ufscar.asensor.utils.BluetoothHandler;
import br.ufscar.asensor.utils.DialogHandler;
import br.ufscar.asensor.utils.NetworkTool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// declaracao da classe
public class HomeActivity extends Activity implements BluetoothClientListener
{
	// declaracao de atributos
	private BluetoothHandler btHandler;	
	private ExpressMessage expressMessage;
	private Map<String, Object> users;
	private boolean hasUser = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);		
		
		// habilita a interface de rede para trocar dados via HTTP
		NetworkTool.enableExternalConnection();

		// inicializa as configuracoes
		this.init();

		// inicializa atributos
		this.btHandler = BluetoothHandler.getBluetoothHandlerInstance(this, this);
		this.expressMessage = null;
	}

	public void init()
	{
		// inicializa as configuracoes da aplicacao
		// declaracao de variaveis
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// desliga o bluetooth caso as configuracoes do servidor nao tenham sido especificadas
		if(Configs.SERVICE_URL.equals(""))
		{
			if (bluetoothAdapter.isEnabled()) {
				this.turnOffBluetooth();
			}
		}else
			{
				// liga o bluetooth
				if (!bluetoothAdapter.isEnabled())
				{
					this.turnOnBluetooth();
				}
			}

		// adiciona listeners aos botoes

		// botao do bluetooth
		final Button btnBluetooth = (Button) this.findViewById(R.id.btnBluetooth);
		btnBluetooth.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// verifica o status do bluetooth
				if(!HomeActivity.this.btHandler.getBluetoothStatus())
				{
					// ativa o bluetooth somente se as configuracoes do servidor foram definidas
					if(!Configs.SERVICE_URL.equals(""))
					{
						HomeActivity.this.turnOnBluetooth();
					}else
						{
							DialogHandler.createAlertDialog("O endereço do serviço não foi configurado.","Aviso:", HomeActivity.this);
						}
				}else
				{
					HomeActivity.this.turnOffBluetooth();
				}

			}
		});

		// botao de configuracoes
		Button btnConfig = (Button) this.findViewById(R.id.btnConfig);
		btnConfig.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				HomeActivity.this.setConfigurations();
			}
		});

		// botao de informacoes
		Button btnInfo = (Button) this.findViewById(R.id.btnInfo);
		btnInfo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				// exibe informacoes do servidor somente se suas configuracoes forem definidas
				if(!Configs.SERVICE_URL.equals("")) {
					DialogHandler.createAlertDialog("Nome da aplicacao: " + Configs.SERVER_NAME
									+ "\n\nCampos requisitados: " + Configs.FIELDS.toString()
							, "\n\nDados do servidor", HomeActivity.this);
					try
					{
						Log.i("<DadosRequisitados>", Configs.getDataRequest().toString());
					}catch(Exception e)
						{
							Log.e("error",e.getMessage());
						}
				}else
					{
						DialogHandler.createAlertDialog("O endereço do serviço não foi configurado.","Aviso:", HomeActivity.this);
					}
			}
		});
	}


	public void setBtnBluetoothText(String text)
	{
		// altera o texto do botao do bluetooth
		// declaracao de variaveis
		Button button = (Button) this.findViewById(R.id.btnBluetooth);
		button.setText(text);
	}
	
	public void sleep(int milliseconds)
	{
		// causa um atraso na execucao preenptando a linha de execucao atual
		try 
		{
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) 
				{			
					e.printStackTrace();
				}
	}
	
	public void turnOnBluetooth()
	{
		// liga o bluetooth
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter == null)
		{
			DialogHandler.createAlertDialog("Nao foi possivel habilitar o bluetooth em seu dispositivo",
										       "Erro: ", HomeActivity.this);
		}else
			{
				if(!adapter.isEnabled())
				{
					// habilita o bluetooth
					btHandler.activateBluetooth();
					DialogHandler.createToast("(V) Habilitando bluetooth", HomeActivity.this);

					// configura a instancia do ExpressMessage para troca de dados via http
					this.expressMessage = new ExpressMessage(Configs.SERVER_NAME, Configs.SERVICE_URL);

					// inicializa a tabela de usuarios
					this.users = new HashMap<String, Object>();

					// troca a mensagem do botao ligar/desligar bluetooth
					this.setBtnBluetoothText("Desligar bluetooth");
				}
			}
	}
	
	public void turnOffBluetooth()
	{
		// liga o bluetooth
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		
		if(adapter == null)
		{
			DialogHandler.createAlertDialog("Nao foi possivel desabilitar o bluetooth em seu dispositivo", "Erro: ", HomeActivity.this);
		}else
			{
				if(adapter.isEnabled())
				{
					// desabilita o bluetooth
					adapter.disable();
					DialogHandler.createToast("(X) Desabilitando o Blutooth", HomeActivity.this);

					// destroi a instancia para o ExpressMessxage
					this.expressMessage = null;

					// destroi a tabela de usuarios
					this.users = null;

					// troca a mensagem do botao ligar/desligar bluetooth
					this.setBtnBluetoothText("Ligar Bluetooth");
				}
			}
	}

	public void setConfigurations()
	{
		// configura os dados de acesso ao express message
		DialogHandler.showInputDialog(this);
	}

	@Override
	public void retrieveBluetoothDevicesSet(Set<BluetoothDevice> devicesAroundMe, Exception exception)
	{	 		
		// executa o servico do bluetooth de descoberta de dispositivos
		try
		{
			
			// verifica se a estrutura de dados possui elementos recuperados durante o servico de descoberta de dispositivos
			if(!devicesAroundMe.isEmpty())
			{				
				// chama o primeiro usuario caso ainda nao tenha qualquer um deles sendo atendido ate o momento
				if(this.users.size() == 0)				
				{				
					BluetoothDevice device = (BluetoothDevice) devicesAroundMe.toArray()[0];				
					this.processDevice(device);
					
					Log.i("<Bluetooth>", "a tabela de hash esta vazia e o primeiro elemento e buscado");
					
				}else
					{
						// inicializa a variavel logica
						boolean status = false;
					
						// verifica se o usuario esta presente no ambiente e caso nao estiver chama o proximo
						for (BluetoothDevice device : devicesAroundMe) 
						{	
							if(this.users.containsKey(device.getAddress()))
							{
								status = true;
								Log.i("<Bluetooth>", "O dispositivo: " + device.getAddress() + " ainda esta presente");
							}
						}
						
						// atende ao proximo usuario caso o atual nao se encontre presente					
						if(!status)
						{
							this.users = null;
							this.users = new HashMap<String, Object>(); 
							BluetoothDevice device = (BluetoothDevice) devicesAroundMe.toArray()[0];				
							this.processDevice(device);
							Log.i("<Bluetooth>", "Chamando o dispositivo de endereco: " + device.getAddress());
						}
					}
			}else
				{
					// limpa a tabela de hash de usuarios
					this.users = null;
					this.users = new HashMap<String, Object>();
					
					// inicializa a interface da aplicacao caso nao haja algum usuario sendo atendido
					if(this.hasUser)
					{
						this.sendProfileDefault();
						
						// altera o valor da variavel logica
						this.hasUser = false;
					}
					
					Log.i("<Bluetooth>", "Nao ha dispositivos e a tabela de hash foi inicializada");
					
				}
			
			Log.i("<Bluetooth>", "servico de descoberta ativo");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("<Bluetooth>", "Erro ao executar servico de descoberta do Bluetooth:\n" + ex.getMessage());
		}
		
		btHandler.discoverDevices();
	}

	@Override
	public void onResume() {
		super.onResume();
		btHandler = BluetoothHandler.getBluetoothHandlerInstance(this, this);
		btHandler.discoverDevices();
	}

	private void processDevice(BluetoothDevice device)
	{
		// processa o dispositivo de usuario corrente
		// faz a requisicao de perfil
		try
		{
			if(NetworkTool.getConnectionStatus(HomeActivity.this))
			{
				this.expressMessage.post(device.getAddress(), Configs.getDataRequest().toString());
				this.users.put(device.getAddress(), "dadosDoPerfil");
				this.hasUser = true;
			}
		}catch(Exception e)
			 {
				e.printStackTrace();
				Log.e("<ProcessDevice>", "Erro ao processar dispositivo de usuario");
			 }
		
	}
	private void sendProfileDefault()
	{
		// limpa a adaptacao local
		// declaracao de variaveis
		try
		{
			if(NetworkTool.getConnectionStatus(HomeActivity.this))
			{
				String jsonData = "{\n" +
						"    \"clean\": \"true\"" +
						"}";

				this.expressMessage.post(Configs.SERVER_NAME, jsonData);
				Log.i("<CleanAdaptation>", "Limpa os dados da adaptacao");
			}
	        
		}catch(Exception e)
			{
				Log.e("<SendProfile>", "Erro ao postar perfil");
			}
	}

	
}
