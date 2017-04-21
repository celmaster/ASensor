package br.ufscar.asensor.bluetooth;

import java.util.Set;

import android.bluetooth.BluetoothDevice;

public interface BluetoothClientListener {
   void retrieveBluetoothDevicesSet(Set<BluetoothDevice> devicesAroundMe, Exception exception);
}
