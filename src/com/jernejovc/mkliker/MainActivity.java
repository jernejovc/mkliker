package com.jernejovc.mkliker;

import com.jernejovc.mkliker.util.KlikerPreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Main Activity which is started when the app is ran.
 * @author matej
 *
 */
public class MainActivity extends FragmentActivity {
	private StartFragment m_startFragment;
	private SelectRoomFragment m_selectRoomFragment;
	private RoomFragment m_roomFragment;
	private boolean m_smsMode;
	
	/**
	 * When the Activity is created, {@link StartFragment} is loaded
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		m_startFragment = new StartFragment();
		m_startFragment.setMainActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		// TODO Auto-generated method stub
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.replace(R.id.fragment_container, m_startFragment);
		//transaction.addToBackStack(null);

		// Commit the transaction
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		boolean smsEnabled = new KlikerPreferences(this).isSMSEnabled();
		menu.findItem(R.id.action_menu_enable_sms).setChecked(smsEnabled);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_menu_enable_sms:
	        	item.setChecked(!item.isChecked());
	        	new KlikerPreferences(this).setSMSEnabled(item.isChecked());
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Handle back presses. When there is only StartFragment loaded, exit
	 * immediately. In both other cases (SelectRoomFragment, RoomFragment)
	 * user is asked if he'd really like to disconnect from server / leave
	 * room. 
	 */
	@Override
	public void onBackPressed() {
		// RoomFragment
		if(getSupportFragmentManager().getBackStackEntryCount() == 2) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
		    adb.setTitle("Do you want to exit room?");
		    adb.setIcon(android.R.drawable.ic_dialog_alert);
		    adb.setMessage("Are you sure you want to leave the room?");
		    System.out.println("Count: " + getSupportFragmentManager().getBackStackEntryCount());
		    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            Toast.makeText(MainActivity.this, "leave", Toast.LENGTH_SHORT).show();
		            m_selectRoomFragment.getServer().setReceiver(m_selectRoomFragment);
		            MainActivity.super.onBackPressed();
		      } });
	
	
		    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	Toast.makeText(MainActivity.this, "stay", Toast.LENGTH_SHORT).show();
		      } });
		    adb.show();
		} else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
		    adb.setTitle("Disconnect from server?");
		    adb.setIcon(android.R.drawable.ic_dialog_alert);
		    adb.setMessage("Are you sure you want to disconnect from server?");
		    System.out.println("Count: " + getSupportFragmentManager().getBackStackEntryCount());
		    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            Toast.makeText(MainActivity.this, "disconnect", Toast.LENGTH_SHORT).show();
		            m_selectRoomFragment.getServer().getConnection().disconnect();
		            MainActivity.super.onBackPressed();
		      } });
	
	
		    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	Toast.makeText(MainActivity.this, "stay", Toast.LENGTH_SHORT).show();
		      } });
		    adb.show();
		} else {
			MainActivity.super.onBackPressed();
		}
	}
	
	/**
	 * Sets the {@link SelectRoomFragment} that is used throughout the whole app life
	 * @param fragment
	 */
	public void setSelectRoomFragment(SelectRoomFragment fragment) {
		m_selectRoomFragment = fragment;
	}
	
	/**
	 * Sets the {@link RoomFragment} that is used throughout the whole app life
	 * @param fragment
	 */
	public void setRoomFragment(RoomFragment fragment) {
		m_roomFragment = fragment;
	}
	
	/**
	 * Opens select room fragment
	 */
	public void openSelectRoomFragment() {
		openFragment(m_selectRoomFragment);
	}
	
	/**
	 * Opens room fragment
	 */
	public void openRoomFragment() {
		openFragment(m_roomFragment);
	}
	
	/**
	 * Opens arbitrary fragment and adds it to the back stack.
	 * @param fragment
	 */
	private void openFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, fragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
	}
	
	/**
	 * Sets app into SMS mode, where SMS communication is preffered over
	 * data (WebSocket) connection.
	 * @param smsMode
	 */
	public void setSMSMode(boolean smsMode) {
		m_smsMode = smsMode;
	}
	
	/**
	 * @return true if app is in SMS mode, false otherwise
	 */
	public boolean inSMSMode() {
		return m_smsMode;
	}
	
	/**
	 * @return true if Data Network is available, false otherwise
	 */
	public boolean isDataNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo network = cm.getActiveNetworkInfo();
	    
	    return network != null && network.isConnected();
	}
	
	/**
	 * Shows info about SMS usage in the app.
	 */
	public void showSMSInfoDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("Enable SMS");
		adb.setIcon(android.R.drawable.ic_dialog_info);
		adb.setMessage("You are not connected to a network, however you can " +
		"enable participation via SMS in the menu (if the server supports SMS participation).");
		adb.setPositiveButton("OK", null);
		adb.show();
	}
}
