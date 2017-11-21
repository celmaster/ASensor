package br.ufscar.asensor.utils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import br.ufscar.asensor.bluetooth.BluetoothClientListener;
import android.util.Log;

public class BluetoothHandler {

	private static BluetoothHandler btHandler;
	private BluetoothAdapter adapter;
	private BluetoothBroadcastReceiver receiver;
	private Activity activity;
	private BluetoothClientListener listener;
	private Set<BluetoothDevice> devicesAroundMe;

	private BluetoothHandler(Activity activity, BluetoothClientListener listener) {
		this.adapter = BluetoothAdapter.getDefaultAdapter();
		this.activity = activity;
		this.listener = listener;
		this.devicesAroundMe = new HashSet<BluetoothDevice>();
		this.receiver = new BluetoothBroadcastReceiver();		
	}

	public static BluetoothHandler getBluetoothHandlerInstance(
			Activity activity, BluetoothClientListener listener) {
		if (btHandler == null) {
			btHandler = new BluetoothHandler(activity, listener);
		}
		return btHandler;
	}

	public void activateBluetooth() {
		if (!adapter.isEnabled()) {
			Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(turnOn, 0);
		}
	}

	public boolean disableBluetooth() 
	{
		return adapter.disable();
	}

	public void turnBluetoothVisible() {
		Intent bluetoothVisible = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		activity.startActivityForResult(bluetoothVisible, 0);
	}

	public Set<BluetoothDevice> retrievePairedDevices() {
		if (!adapter.isEnabled()) {
			activateBluetooth();
		}
		return adapter.getBondedDevices();
	}

	public void discoverDevices() {
		devicesAroundMe.clear();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activity.registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		activity.registerReceiver(receiver, filter);
		adapter.startDiscovery();
		Log.i("<Bluetooth>","Discovery service start");
	}
	
	private class BluetoothBroadcastReceiver extends BroadcastReceiver {
		private Exception exception;

		public void onReceive(Context context, Intent intent) {  
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				devicesAroundMe.add(device);
			}
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				activity.unregisterReceiver(receiver);
				listener.retrieveBluetoothDevicesSet(devicesAroundMe, exception);
			}
		}
	}
	
	public ParcelUuid [] servicesFromDevice(BluetoothDevice device) {
	    try {
	        Class cl =
	            Class.forName("android.bluetooth.BluetoothDevice");
	        Class[] par = {};
	        Method method = cl.getMethod("getUuids", par);
	        Object[] args = {};
	        ParcelUuid[] retval = (ParcelUuid[]) method.invoke(device, args);
	        return retval;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	} }
	
	public String getLocalBluetoothAddress()
	{
		return adapter.getAddress();
	}
	
	public String getLocalBluetoothName()
	{
		return adapter.getName();
	}
	
	public void setLocalBluetoothName(String name)
	{
		adapter.setName(name);
	}
	
	public boolean getBluetoothStatus()
	{
		return this.adapter.isEnabled();
	}
	
	public boolean enableBluetooth()
	{
		return adapter.enable();
	}
	
	
}
