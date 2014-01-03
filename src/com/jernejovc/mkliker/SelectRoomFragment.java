package com.jernejovc.mkliker;

import com.jernejovc.mkliker.message.Message;
import com.jernejovc.mkliker.message.MessageFactory;
import com.jernejovc.mkliker.net.ReceiveMessage;
import com.jernejovc.mkliker.net.Server;
import com.jernejovc.mkliker.net.User;
import com.jernejovc.mkliker.question.QuestionType;
import com.jernejovc.mkliker.question.QuestionTypeUtil;
import com.jernejovc.mkliker.util.KlikerPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.app.Fragment;

public class SelectRoomFragment extends Fragment implements ReceiveMessage {
	private MainActivity m_activity;
	private Server m_server;
	private User m_user;
	private KlikerPreferences m_preferences;
	
	private EditText m_roomEditText;
	private EditText m_nicknameEditText;
	private Button m_joinRoomButton;
	private ProgressBar m_progressBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.select_room_layout, container, false);
		
		m_joinRoomButton = (Button) view.findViewById(R.id.selectRoomJoinButton);
		m_roomEditText = (EditText) view.findViewById(R.id.selectRoomRoomEditText);
		m_nicknameEditText = (EditText) view.findViewById(R.id.selectRoomNicknameEditText);
		m_progressBar = (ProgressBar) view.findViewById(R.id.selectRoomProgressBar);
		
		m_roomEditText.setText(m_preferences.getLastRoomName());
		m_nicknameEditText.setText(m_preferences.getLastNickname());
		
		m_joinRoomButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				connectToServer();
			}
		});
		return view;
	}
	
	/**
	 * Connects to server
	 */
	private void connectToServer() {
		// Sanity checks
		String room = m_roomEditText.getText().toString();
		if(room == "") {
			Toast.makeText(getActivity(),
					"Enter room name!", Toast.LENGTH_LONG).show();
			m_roomEditText.requestFocus();
			return;
		}
		
		String nickname = m_nicknameEditText.getText().toString();
		if(nickname == "") {
			Toast.makeText(getActivity(),
					"Enter your nickname!", Toast.LENGTH_LONG).show();
			m_nicknameEditText.requestFocus();
			return;
		}
		
		if(!m_activity.isDataNetworkAvailable()) {
			/* If we don't have data network, check if SMS is enabled and use that,
			 * if it is not enabled remind user that it can be enabled.
			 */
			if(!m_preferences.isSMSEnabled()) {
				m_activity.showSMSInfoDialog();
			} else {
				m_activity.setSMSMode(true);
				m_user = new User(-1, nickname, room);
				openRoomFragment(QuestionType.SHORTANSWER, true);
			}
		} else {
			/* Else just try to join the room
			 */
			m_activity.setSMSMode(false);
			m_joinRoomButton.setVisibility(View.GONE);
			m_progressBar.setVisibility(View.VISIBLE);

			Message toSend = MessageFactory.makeLoginStudentMessage(room, "");
			m_server.send(toSend);
		}
	}
	
	/**
	 * Opens the room fragment with certain QuestionType and running 
	 * question indicator.
	 * @param type
	 * @param running
	 */
	private void openRoomFragment(QuestionType type, boolean running) {
		RoomFragment fragment = new RoomFragment();
		// set fragment variables
		fragment.setMainActivity(m_activity);
		fragment.setServer(m_server);
		fragment.setUser(m_user);
		m_preferences.setLastRoomName(m_user.getRoom());
		m_preferences.setLastNickName(m_user.getNickname());
		
		fragment.setQuestionType(type);
		fragment.setRunning(running);
		
		m_activity.setRoomFragment(fragment);
		m_activity.openRoomFragment();
		
	}
	
	@Override
	public void receiveMessage(Message msg) {
		switch(msg.messageType()) {
			case ENROLLED:
				// Successful connection, save variables and load room fragment 
				String[] payloads = msg.message().split(Message.SEPARATOR);
				int uid = Integer.valueOf(payloads[0]);
				QuestionType question_type = QuestionTypeUtil.stringToQuestionType(payloads[3]);
				boolean question_running = payloads[4].contentEquals("true");
				String nickname = m_nicknameEditText.getText().toString();
				m_user = new User(uid, nickname, m_roomEditText.getText().toString());
				openRoomFragment(question_type, question_running);
				break;
			case NOSUCHROOM:
				// No such room exists
				Toast.makeText(getActivity(), "This room doesn't exist!", Toast.LENGTH_LONG).show();
				m_roomEditText.requestFocus();
				m_progressBar.setVisibility(View.GONE);
				m_joinRoomButton.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Set the server to which we are connected
	 * @param server
	 */
	public void setServer(Server server) {
		m_server = server;
		m_server.setReceiver(this);
	}
	
	/**
	 * Sets the preferences class
	 * @param prefs
	 */
	public void setKlikerPreferences(KlikerPreferences prefs) {
		m_preferences = prefs;
	}
	
	/**
	 * @return the server we are connected to
	 */
	public Server getServer() {
		return m_server;
	}
	
	/**
	 * Sets parent main activity
	 * @param activity
	 */
	public void setMainActivity(MainActivity activity) {
		m_activity = activity;
	}
}
