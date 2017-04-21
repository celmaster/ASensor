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
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
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
		
		// desbloqueia a interface de rede para trocar dados via HTTP
		ExpressMessage.unlockGuardPolicy();
		
		this.btHandler = BluetoothHandler.getBluetoothHandlerInstance(this, this);
		
		
		// verifica se o bluetooth esta habilitado e caso negativo, solicita sua habilitacao ao usuario
		if(!btHandler.getBluetoothStatus())
		{
			DialogHandler.createAlertDialog("Seu dispositivo esta com o bluetooth desligado.", "Aviso: ", HomeActivity.this);
			this.setBtnBluetoothText("Bluetooth desligado");
		}else
			{				
				this.expressMessage = new ExpressMessage("ASensor", Configs.SERVICE_URL);
				this.users = new HashMap<String, Object>(); 
				this.setBtnBluetoothText("Bluetooth ligado");
			}
		
		// adiciona um listener ao botao
		Button btnBluetooth = (Button) this.findViewById(R.id.btnBluetooth); 
		
		btnBluetooth.setOnClickListener(new OnClickListener(){		

			@Override
			public void onClick(View v) {
				// verifica o status do bluetooth
				if(!HomeActivity.this.btHandler.getBluetoothStatus())
				{
					HomeActivity.this.turnOnBluetooth();
				}else
					{
					HomeActivity.this.turnOffBluetooth();
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
		
		if(!adapter.enable())
		{
			DialogHandler.createAlertDialog("Nao foi possivel habilitar o bluetooth em seu dispositivo", "Erro: ", HomeActivity.this);
		}else
			{
				DialogHandler.createAlertDialog(" (V) Bluetooth ativado.", "Aviso: ", HomeActivity.this);
				this.expressMessage = new ExpressMessage("BUSS", Configs.SERVICE_URL);
				this.users = new HashMap<String, Object>();
				this.setBtnBluetoothText("Bluetooth ligado");
			}
	}
	
	public void turnOffBluetooth()
	{
		// liga o bluetooth
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		
		if(!adapter.disable())
		{
			DialogHandler.createAlertDialog("Nao foi possivel desabilitar o bluetooth em seu dispositivo", "Erro: ", HomeActivity.this);
		}else
			{
				DialogHandler.createAlertDialog(" (X) Bluetooth desativado.", "Aviso: ", HomeActivity.this);						
				this.expressMessage = null;
				this.users = null;
				this.setBtnBluetoothText("Bluetooth desligado");
				
			}
	}
	
	public void warningBluetoothEnabled()
	{
		// cria um aviso para ao usu�rio notificando-o que o Bluetooth est� desabilitado e tenta pergunta ao usuario se deseja ativa-lo
		AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(this);
		builderAlertDialog.setMessage("A comunicacao com a interface\nde rede Bluetooth esta\ndesabilitada").setTitle("Atencao:");
		builderAlertDialog.setPositiveButton(R.string.ativar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ativa o Bluetooth
				BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
				if(!adapter.enable())
				{
					DialogHandler.createAlertDialog("Nao foi possivel habilitar o bluetooth em seu dispositivo", "Erro: ", HomeActivity.this);
				}
			}
		});
		builderAlertDialog.setNegativeButton(R.string.naoAtivar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// apenas mantem o atributo logico como falso
				DialogHandler.createAlertDialog("O funcionamento do sensor sera prejudicado", "Aviso: ", HomeActivity.this);
			}
		});
		
		AlertDialog alertDialog = builderAlertDialog.create();
		alertDialog.show();
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
					
					// inicializa a interface do BUSS caso nao haja algum usuario sendo atendido
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
			this.expressMessage.post(device.getAddress(), Configs.getDataRequest().toString());
			this.users.put(device.getAddress(), "dadosDoPerfil");	
			this.hasUser = true;
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
			
			String jsonData = "{\n" +
					"    \"clean\": \"true\"" +
					"}";
			
			this.expressMessage.post(Configs.APP_NAME, jsonData);
			Log.i("<CleanAdaptation>","Limpa os dados da adaptacao");
	        
		}catch(Exception e)
			{
				Log.e("<SendProfile>", "Erro ao postar perfil");
			}
	}

	
}
