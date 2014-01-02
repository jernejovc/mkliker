package com.jernejovc.mkliker;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jernejovc.mkliker.message.Message;
import com.jernejovc.mkliker.message.MessageFactory;
import com.jernejovc.mkliker.net.ReceiveMessage;
import com.jernejovc.mkliker.net.Server;
import com.jernejovc.mkliker.net.User;
import com.jernejovc.mkliker.question.QuestionType;
import com.jernejovc.mkliker.question.QuestionTypeUtil;

public class RoomFragment extends Fragment implements ReceiveMessage {
	
	private class AnswerButtonListener implements View.OnClickListener {
		public AnswerButtonListener(String answer) {
			super();
			this.answer = answer;
		}
		private String answer = "";
		@Override
		public void onClick(View v) {
			if(m_activity.inSMSMode()) {
				sendSMSAnswer();
			} else {
				sendAnswer(answer);
			}
		}
	}

	private MainActivity m_activity; 
	private View m_view;
	private User m_user;
	private Server m_server;
	private Button m_sendButton;
	private EditText m_messageEditText;
	private TextView m_nicknameTextView;
	private TextView m_roomTextView;
	private TextView m_roomStatusTextView;

	private LinearLayout m_yesnoLayout;
	private LinearLayout m_abcdeSingleLayout;
	private LinearLayout m_abcdeMultiLayout;
	private LinearLayout m_textanswerLayout;

	private Button m_answerYesButton;
	private Button m_answerNoButton;
	private Button m_answerDontKnowButton;

	private Button m_answerAButton;
	private Button m_answerBButton;
	private Button m_answerCButton;
	private Button m_answerDButton;
	private Button m_answerEButton;

	private CheckBox m_answerACheckBox;
	private CheckBox m_answerBCheckBox;
	private CheckBox m_answerCCheckBox;
	private CheckBox m_answerDCheckBox;
	private CheckBox m_answerECheckBox;

	private EditText m_answerEditText;

	private Button m_sendAnswerButton;

	boolean sent_message = false;
	private QuestionType m_questionType;
	private boolean m_questionRunning;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.room_layout, container, false);

		m_sendButton = (Button) view.findViewById(R.id.roomSendMessageButton);
		m_messageEditText = (EditText) view.findViewById(R.id.roomMessageEditText);
		m_roomTextView = (TextView) view.findViewById(R.id.roomRoomTextView);
		m_nicknameTextView = (TextView) view.findViewById(R.id.roomNickTextView);
		m_roomStatusTextView = (TextView) view.findViewById(R.id.roomQuestionTextView);

		m_yesnoLayout = (LinearLayout) view.findViewById(R.id.roomYesNoLayout);
		m_abcdeSingleLayout = (LinearLayout) view.findViewById(R.id.roomABCDELayout);
		m_abcdeMultiLayout = (LinearLayout) view.findViewById(R.id.roomABCDECheckLayout);
		m_textanswerLayout = (LinearLayout) view.findViewById(R.id.roomTextAnswerLayout);


		m_answerYesButton = (Button) view.findViewById(R.id.roomYesButton);
		m_answerNoButton = (Button) view.findViewById(R.id.roomNoButton);
		m_answerDontKnowButton = (Button) view.findViewById(R.id.roomDontKnowButton);
		m_answerYesButton.setOnClickListener(new AnswerButtonListener("A"));
		m_answerNoButton.setOnClickListener(new AnswerButtonListener("B"));
		m_answerDontKnowButton.setOnClickListener(new AnswerButtonListener("C"));

		m_answerAButton = (Button) view.findViewById(R.id.roomAButton);
		m_answerBButton = (Button) view.findViewById(R.id.roomBButton);
		m_answerCButton = (Button) view.findViewById(R.id.roomCButton);
		m_answerDButton = (Button) view.findViewById(R.id.roomDButton);
		m_answerEButton = (Button) view.findViewById(R.id.roomEButton);
		m_answerAButton.setOnClickListener(new AnswerButtonListener("A"));
		m_answerBButton.setOnClickListener(new AnswerButtonListener("B"));
		m_answerCButton.setOnClickListener(new AnswerButtonListener("C"));
		m_answerDButton.setOnClickListener(new AnswerButtonListener("D"));
		m_answerEButton.setOnClickListener(new AnswerButtonListener("E"));

		m_answerACheckBox = (CheckBox) view.findViewById(R.id.roomACheckBox);
		m_answerBCheckBox = (CheckBox) view.findViewById(R.id.roomBCheckBox);
		m_answerCCheckBox = (CheckBox) view.findViewById(R.id.roomCCheckBox);
		m_answerDCheckBox = (CheckBox) view.findViewById(R.id.roomDCheckBox);
		m_answerECheckBox = (CheckBox) view.findViewById(R.id.roomECheckBox);

		m_answerEditText = (EditText) view.findViewById(R.id.roomAnswerEditText);

		m_sendAnswerButton = (Button) view.findViewById(R.id.roomSendAnswerButton);
		m_sendAnswerButton.setOnClickListener(new AnswerButtonListener(""));

		m_sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = m_messageEditText.getText().toString();
				Message msg = MessageFactory.makeMessageMessage(
						m_user.getRoom(), 
						m_user.getuserID(),
						m_user.getNickname(), 
						message);
				if(m_activity.inSMSMode()) {
					sendSMSMessage();
				} else {
					m_server.send(msg);
					sent_message = true;
				}
			}
		});	

		m_roomTextView.setText(m_user.getRoom());
		m_nicknameTextView.setText(m_user.getNickname());

		// Enable or disable question
		questionChanged();

		m_view = view;
		m_view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				System.out.println(keyCode);
				return false;
			}
		});
		
		if(m_activity.inSMSMode()) {
			m_textanswerLayout.setVisibility(View.GONE);
			m_sendAnswerButton.setVisibility(View.GONE);
			AlertDialog.Builder adb = new AlertDialog.Builder(m_activity);
			adb.setTitle("Participation via SMS");
			adb.setIcon(android.R.drawable.ic_dialog_alert);
			adb.setMessage("You are participating by SMS. That means that this screen will not " + 
			"update and you will have to write your answers (e.g. 'A', 'ADE' and 'answer') and send them " + 
					"manually when your lecturer starts the question.");
			adb.show();
		}
		return m_view;
	}
	
	@Override
	public void receiveMessage(Message msg) {
		String[] payloads = msg.message().split(Message.SEPARATOR);
		QuestionType type;
		switch(msg.messageType()) {
		case ACKNOWGLEDGE:
			if(sent_message) {
				m_messageEditText.setText("");
				m_messageEditText.clearFocus();
			}
			break;
		case ROOMSTATUS:
			if(payloads.length >= 5) {
				//					m_roomStatusTextView.setText(payloads[4]);
			}
			break;
		case STARTQUESTION:
			type = QuestionTypeUtil.stringToQuestionType(payloads[0]);
			setQuestionType(type);
			setRunning(true);
			questionChanged();
			break;
		case STOPQUESTION:
			type = QuestionTypeUtil.stringToQuestionType(payloads[0]);
			setQuestionType(type);
			setRunning(false);
			questionChanged();
			break;
		case BLOCK:
			setRunning(false);
			questionChanged();
		default:
			break;
		}

	}
	public void setServer(Server server) {
		m_server = server;
		m_server.setReceiver(this);
	}

	public void setUser(User user) {
		m_user = user;
	}
	public void setQuestionType(QuestionType type) {
		m_questionType = type;

	}
	public void setRunning(boolean running) {
		m_questionRunning = running;

		// If setup is finalized
		if(m_view != null)
			questionChanged();
	}

	private void questionChanged() {
		if(m_questionRunning) {
			// show question based on question type
			switch(m_questionType) {
			case YESNO:
				m_roomStatusTextView.setText("Tap Yes, No or Don't know.");
				m_yesnoLayout.setVisibility(View.VISIBLE);
				break;
			case ABCDESINGLE:
				m_roomStatusTextView.setText("Tap one of A, B, C, D or E.");
				m_abcdeSingleLayout.setVisibility(View.VISIBLE);
				break;
			case ABCDEMULTI:
				m_roomStatusTextView.setText("Select one or more from A, B, C, D or E and tap send answer.");
				m_abcdeMultiLayout.setVisibility(View.VISIBLE);
				m_sendAnswerButton.setVisibility(View.VISIBLE);
				break;
			case SHORTANSWER:
				m_roomStatusTextView.setText("Type your answer and tap send answer.");
				m_textanswerLayout.setVisibility(View.VISIBLE);
				m_sendAnswerButton.setVisibility(View.VISIBLE);
				break;
			}
		} else {
			m_roomStatusTextView.setText("No question running.");
			switch(m_questionType) {
			case YESNO:
				m_yesnoLayout.setVisibility(View.GONE);
				break;
			case ABCDESINGLE:
				m_abcdeSingleLayout.setVisibility(View.GONE);
				break;
			case ABCDEMULTI:
				m_abcdeMultiLayout.setVisibility(View.GONE);
				m_sendAnswerButton.setVisibility(View.GONE);
				break;
			case SHORTANSWER:
				m_textanswerLayout.setVisibility(View.GONE);
				m_sendAnswerButton.setVisibility(View.GONE);
				break;
			}
		}
	}

	private void sendAnswer(String answer) {
		String ans = answer;
		switch(m_questionType) {
		case ABCDEMULTI:
			StringBuilder builder = new StringBuilder();
			if(m_answerACheckBox.isChecked())
				builder.append("A");
			if(m_answerBCheckBox.isChecked())
				builder.append("B");
			if(m_answerCCheckBox.isChecked())
				builder.append("C");
			if(m_answerDCheckBox.isChecked())
				builder.append("D");
			if(m_answerECheckBox.isChecked())
				builder.append("E");
			ans = builder.toString();
			break;			
		case SHORTANSWER:
			ans = m_answerEditText.getText().toString();
			break;
		default:
			break;	
		}

		Message msg = MessageFactory.makeStudentAnswerMessage(
				m_user.getRoom(), 
				m_user.getuserID(),
				ans);
		m_server.send(msg);
	}
	
	private void sendSMSMessage() {
		String text = String.format(Locale.getDefault(), 
				"%s#MSG#%s", 
				m_user.getRoom(),
				m_messageEditText.getText().toString());
		sendSMS(text);
		m_messageEditText.setText("");
	}
	private void sendSMSAnswer() {
		String text = String.format(Locale.getDefault(), 
				"%s#%s", 
				m_user.getRoom(),
				m_answerEditText.getText().toString());
		sendSMS(text);
	}
	
	private void sendSMS(String text) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(m_server.getSMSNumber(), null, text, null, null);
	}
	
	public void setMainActivity(MainActivity activity) {
		m_activity = activity;
	}
}