package com.jernejovc.mkliker;

import java.util.ArrayList;
import java.util.HashMap;

import com.jernejovc.mkliker.net.KlikerWebSocketHandler;
import com.jernejovc.mkliker.net.Server;
import com.jernejovc.mkliker.net.ServerConnectAsyncTask;
import com.jernejovc.mkliker.net.WaitForConnection;
import com.jernejovc.mkliker.util.KlikerPreferences;
import com.jernejovc.mkliker.util.ServerList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StartFragment extends Fragment implements WaitForConnection {
	private MainActivity m_activity;
	private ServerList m_serverlist;
	private Server m_connectedserver;
	private KlikerPreferences m_prefs;
	private View m_view;
	
	private KlikerWebSocketHandler m_wshandler = new KlikerWebSocketHandler();
	private ProgressBar m_progressbar;
	private Button m_button;
	private TextView m_textview;
	
	private class ConnectClickListener implements View.OnClickListener {
		private StartFragment parent = StartFragment.this;
		
		@Override
		public void onClick(View v) {
			m_connectedserver = m_serverlist.get(getSelectedServerIndex());
			new ServerConnectAsyncTask(parent, m_button, m_progressbar, m_textview)
			.execute(m_connectedserver);
		}
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      m_view = inflater.inflate(R.layout.start_fragment, container, false);
      
      m_progressbar = (ProgressBar) m_view.findViewById(R.id.connectingProgressBar);
      m_button = (Button) m_view.findViewById(R.id.connectButton);
      m_textview = (TextView) m_view.findViewById(R.id.connectErrorTextView);
      m_prefs = new KlikerPreferences(getActivity());
		// Initialize mKliker database
//		kliker_db = new KlikerDatabase(this);
//		try {
//			kliker_db.createDatabase();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		kliker_db.openDatabase();
		
		// Load servers into spinner
		loadServerListSpinner();
		
		ConnectClickListener connect_listener = new ConnectClickListener();
		connect_listener.parent = this;
		m_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				m_connectedserver = m_serverlist.get(getSelectedServerIndex());
				m_connectedserver.setHandler(m_wshandler);
				m_wshandler.setConnectionWaiter(StartFragment.this);
				ProgressBar bar = (ProgressBar) getView().findViewById(R.id.connectingProgressBar);
				TextView tv = (TextView) getView().findViewById(R.id.connectErrorTextView);
				new ServerConnectAsyncTask(StartFragment.this, m_button, bar, tv).execute(m_connectedserver);
			}
		});
		
		ImageButton addButton = (ImageButton) m_view.findViewById(R.id.addServerButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(v.getContext());
				dialog.setContentView(R.layout.dialog_add_server);
				dialog.setTitle("Add server");
				Button okButton = (Button) dialog.findViewById(R.id.addServerDialogOKButton);
				Button cancelButton = (Button) dialog.findViewById(R.id.addServerDialogCancelButton);
				class AddServerListener implements View.OnClickListener {
					ServerList m_serverlist;
					KlikerPreferences m_prefs;
					Activity m_parent;
					
					public AddServerListener(ServerList sl, KlikerPreferences prefs, Activity parent) {
						m_serverlist = sl;
						m_prefs = prefs;
						m_parent = parent;
					}
					@Override
					public void onClick(View v) {
						// TODO add server to DB
						Toast.makeText(v.getContext(), "adding server to settings...", Toast.LENGTH_SHORT).show();
						final EditText nameEditText = (EditText) dialog.findViewById(R.id.addServerDialogNameEditText);
						final EditText urlEditText = (EditText) dialog.findViewById(R.id.addServerDialogURLEditText);
						final EditText smsEditText = (EditText) dialog.findViewById(R.id.addServerDialogSMSEditText);
						final CheckBox defaultCheckBox = (CheckBox) dialog.findViewById(R.id.addServerDialogDefaultCheckBox);
						String name = nameEditText.getText().toString();
						String url = urlEditText.getText().toString();
						String sms = smsEditText.getText().toString();
						boolean def = defaultCheckBox.isChecked();
						
						Server s  = new Server(name, url, sms, def);
						boolean exists = false;
						for(Server ss : m_serverlist.serverList()) {
							if(ss.getName() == s.getName()) {
								exists = true;
							}
						}
						if(exists) {
							AlertDialog.Builder builder = new AlertDialog.Builder(m_parent);
							builder.setMessage("Server already exists!")
							       .setCancelable(false)
							       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							                nameEditText.requestFocus();
							           }
							       });
							AlertDialog alert = builder.create();
							alert.show();
						} else {
							m_serverlist.add(s);
							m_prefs.saveServerList(m_serverlist);
							dialog.dismiss();
						}
					}
					
				}
				okButton.setOnClickListener(
						new AddServerListener(m_serverlist, m_prefs, getActivity().getParent()));
				
				cancelButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				
				// Update server list if server was added.
				dialog.setOnDismissListener(new Dialog.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						loadServerListSpinner();
					}
				});
				
				dialog.show();
			}
		});
		
		ImageButton editButton = (ImageButton) m_view.findViewById(R.id.editServerButton);
		editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(v.getContext());
				dialog.setContentView(R.layout.dialog_edit_server);
				dialog.setTitle("Edit server");
				Button okButton = (Button) dialog.findViewById(R.id.editServerDialogOKButton);
				Button cancelButton = (Button) dialog.findViewById(R.id.editServerDialogCancelButton);
				Button deleteButton = (Button) dialog.findViewById(R.id.editServerDialogDeleteButton);
				final EditText nameEditText = (EditText) dialog.findViewById(R.id.editServerDialogNameEditText);
				final EditText urlEditText = (EditText) dialog.findViewById(R.id.editServerDialogURLEditText);
				final EditText smsEditText = (EditText) dialog.findViewById(R.id.editServerDialogSMSEditText);
				final CheckBox defaultCheckBox = (CheckBox) dialog.findViewById(R.id.editServerDialogDefaultCheckBox);
				
				final int idx = getSelectedServerIndex();
				final Server s = m_serverlist.serverList().get(idx);
				
				nameEditText.setText(s.getName());
				nameEditText.setEnabled(false);
				urlEditText.setText(s.getUrl().toString());
				smsEditText.setText(s.getSMSNumber());
				defaultCheckBox.setChecked(s.getIsDefault());
				
				okButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(), "editing server...", Toast.LENGTH_SHORT).show();
						s.setUrl(urlEditText.getText().toString());
						if(defaultCheckBox.isChecked()) {
							s.setIsDefault(true);
							m_serverlist.setDefault(s);
						}
						m_serverlist.set(s, idx);
						m_prefs.saveServerList(m_serverlist);
						dialog.dismiss();
					}
				});
				
				cancelButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				
				deleteButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						m_serverlist.remove(s);
						m_prefs.saveServerList(m_serverlist);
					}
				});
				
				// Update server list if server was added, edited or removed.
				dialog.setOnDismissListener(new Dialog.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						loadServerListSpinner();
					}
				});
				
				dialog.show();
			}
		});
      return m_view;
    }
    
    private void loadServerListSpinner() {
		m_serverlist = m_prefs.getServerList();
		
		Spinner serverListSpinner = (Spinner) m_view.findViewById(R.id.serverListSpinner);
	    ArrayList<HashMap<String,String>> adapter_server_list = new ArrayList<HashMap<String,String>>();
		for(Server s : m_serverlist.serverList()) {
			HashMap<String,String> server = new HashMap<String, String>();
			server.put("name", s.getName());
			server.put("url", s.getUrl().toString());
			adapter_server_list.add(server);
		}
		String[] from = { "name", "url" };
	    int[] to = { android.R.id.text1, android.R.id.text2 };
		SpinnerAdapter adapter = new SimpleAdapter(getActivity(), adapter_server_list, android.R.layout.simple_list_item_2, from, to);
		
		serverListSpinner.setAdapter(adapter);
	}
    
    private int getSelectedServerIndex() {
		Spinner server_spinner = (Spinner) m_view.findViewById(R.id.serverListSpinner);
		int idx = server_spinner.getSelectedItemPosition();
		return idx;
	}
    
    
	
	public void connectionSuccessful() {
		
	}


	private void openSelectRoomFragment() {
		SelectRoomFragment fragment = new SelectRoomFragment();
		// set fragment variables
		fragment.setServer(m_connectedserver);
		fragment.setKlikerPreferences(m_prefs);
		fragment.setMainActivity(m_activity);
		m_activity.setSelectRoomFragment(fragment);
		m_activity.openSelectRoomFragment();
		
		}

	@Override
	public void connectionEstablished() {
		System.out.println(m_connectedserver.getConnection().isConnected());
		m_progressbar.setVisibility(View.GONE);
		m_button.setVisibility(View.VISIBLE);
		if(!m_connectedserver.getConnection().isConnected()) {
			m_textview.setVisibility(View.VISIBLE);
			m_textview.setText("Error connecting to server.");
		} else {
			Toast.makeText(getActivity().getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
			openSelectRoomFragment();	
		}	
	}
	
	public void setMainActivity(MainActivity activity) {
		m_activity = activity;
	}
}
