package com.jernejovc.mkliker.net;

import com.jernejovc.mkliker.StartFragment;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ServerConnectAsyncTask extends AsyncTask<Server, Integer, Boolean> {
	private StartFragment m_parent;
	private Button m_button;
	private ProgressBar m_progressbar;
	private TextView m_textview;
	public ServerConnectAsyncTask(StartFragment parent, Button button, ProgressBar bar, TextView tv) {
		super();
		m_parent = parent;
		m_button = button;
		m_progressbar = bar;
		m_textview = tv;
		
		m_button.setVisibility(View.GONE);
		m_progressbar.setVisibility(View.VISIBLE);
	}
	protected Boolean doInBackground(Server... server) {
		/*int sleep_time = 50;
		int max_sleep = 5000;
		int sleep = 0;
		if(server[0].connect()) {
			while(!server[0].getConnection().isConnected()) {
				try {
					Thread.sleep(sleep_time);
					sleep += sleep_time;
					if(sleep >= max_sleep) {
						return false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;*/
		return server[0].connect();
	}

	protected void onProgressUpdate(Integer... progress) { }

	protected void onPostExecute(Boolean result) {
		m_parent.connectionEstablished();
	}
}
