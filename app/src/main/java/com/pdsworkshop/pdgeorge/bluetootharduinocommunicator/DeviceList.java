package com.pdsworkshop.pdgeorge.bluetootharduinocommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Pdgeorge on 4/03/2017.
 * Use it to do things.
 */

public class DeviceList extends AppCompatActivity {

    Button pairButton;
    ListView pairList;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);

        pairButton = (Button)findViewById(R.id.scanButton);
        pairList = (ListView)findViewById(R.id.btDevicesList);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //show a message that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish... just... close everything.
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Asks the user to turn their Bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        pairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });
    }

    private void listPairedDevices()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        pairList.setAdapter(adapter);
        pairList.setOnItemClickListener(myListClickListener); //Calls myListClickListener when an item in pairList is 'clicked'
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            //Makes an intent to start the 'Main' Activity
            Intent i = new Intent(DeviceList.this, MainActivity.class);

            //Changes to the 'Main' Activity and puts the address in EXTRA_ADDRESS
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };
}
