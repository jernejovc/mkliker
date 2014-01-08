/* 
 * This file is part of mKliker.
 * 
 * mKliker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * mKliker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with mKliker.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jernejovc.mkliker.net;

import com.jernejovc.mkliker.StartFragment;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Asynchronous task that connects to the server and notifies the parent fragment
 * when connecting to the server is finished.
 * @author matej
 *
 */
public class ServerConnectAsyncTask extends AsyncTask<Server, Integer, Boolean> {
	private StartFragment m_parent;
	private Button m_button;
	private ProgressBar m_progressbar;

	/**
	 * 
	 * @param parent {@link StartFragment} which will later be notified when connecting 
	 * is done
	 * @param button Connect button in the form 
	 * @param bar Loading bar in the form
	 */
	public ServerConnectAsyncTask(StartFragment parent, Button button, ProgressBar bar) {
		super();
		m_parent = parent;
		m_button = button;
		m_progressbar = bar;

		m_button.setVisibility(View.GONE);
		m_progressbar.setVisibility(View.VISIBLE);
	}
	protected Boolean doInBackground(Server... server) {
		return server[0].connect();
	}

	protected void onProgressUpdate(Integer... progress) { }

	protected void onPostExecute(Boolean result) {
		m_parent.connectionEstablished();
	}
}
