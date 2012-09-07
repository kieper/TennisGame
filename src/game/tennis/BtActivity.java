package game.tennis;

import tcpip.communication.game.BtCommunication;
import tcpip.communication.game.CommunicationType;
import game.tennis.GameActivity;
import game.tennis.PreConfig;
import game.tennis.R;
import game.tennis.draw.PlayerType;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.Window;

import android.widget.Button;
import android.widget.Toast;


public class BtActivity extends Activity {

    // Debugging
    private static final String TAG = "BtActivity";

    private static final boolean D = true;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private static final int REQUEST_ENABLE_BT = 2;

    private static final int REQUEST_DISCOREVABLE = 3;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    private CommunicationType commType;

    private PlayerType playerType;

    private Button scanBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D)
            Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.device_list);

        commType = null;
        playerType = null;
        Bundle bundle = this.getIntent().getExtras();
        try {
            commType = CommunicationType.valueOf((String) bundle.get(PreConfig.COMM_TYPE.toString()));
            playerType = PlayerType.valueOf((String) bundle.get(PreConfig.PLAYER.toString()));
            if (D)
                Log.d(TAG, "playertype 1 : " + playerType.toString());
        }
        catch (NullPointerException exception) {
            Log.e(TAG, "Error while specifying communication type");
        }

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        scanBtn = (Button) findViewById(R.id.button_scan);

        scanBtn.setOnClickListener(new ScanOnClickListener(this));

        ((Button) findViewById(R.id.button_scan)).setText(R.string.bt_search4devices);


        // If BT is not on, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            if (D)
                Log.d(TAG, "turning on bt by activity ");
            // Enable bluetooth on device(use for this additionall builtin activity)
            // Result of this activity will be send to onActivityResult method
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else if (playerType == PlayerType.PLAYER2) { // serverSide(player2 menas server)
            ensureDiscoverable(); // Following that apps starts discoverable and starts serverside after
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (D)
            Log.e(TAG, "++ ON START ++");
    }


    /**
     * After firing this function device becomes discoverable, and starts automatically server side methods
     */
    private void ensureDiscoverable() {
        if (D)
            Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivityForResult(discoverableIntent, REQUEST_DISCOREVABLE);

        }
        if (D)
            Log.d(TAG, "ended ensure discoverable function");

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            if (D)
                Log.d(TAG, " In request connect device");
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                BtCommunication.device = device;
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER1.toString());
                intent.putExtra(PreConfig.COMM_TYPE.toString(), commType.toString());
                startActivity(intent);
                finish();
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns

            if (resultCode != Activity.RESULT_OK) {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BtActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            if (D) {
                Log.d(TAG, "Bluetooth enabled");
            }

            if (playerType == PlayerType.PLAYER2) {
                ensureDiscoverable();
            }

            break;
        case REQUEST_DISCOREVABLE:

            // if server did not want to be discovered - finish
            if (resultCode != Activity.RESULT_OK) {
                Intent intent = new Intent(BtActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            if (D){
                Log.d(TAG, "Discorable ended in start for result activity, starting server side");}
            startServerSideBt();
            break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }


    private void startServerSideBt() {
        if (playerType == PlayerType.PLAYER2) {
            if (D)
                Log.d(TAG, "Server side, ensure discorovebale, start game activty");
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER2.toString());
            intent.putExtra(PreConfig.COMM_TYPE.toString(), commType.toString());
            startActivity(intent);
            finish();
        }
    }


    private class ScanOnClickListener implements OnClickListener {

        private Activity activity;


        public ScanOnClickListener(Activity act) {
            activity = act;
        }


        @Override
        public void onClick(View v) {
            Intent serverIntent = new Intent(activity, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        // Call super code so we dont limit default interaction
        super.onKeyDown(keyCode, event);

        return true;
    }

}
