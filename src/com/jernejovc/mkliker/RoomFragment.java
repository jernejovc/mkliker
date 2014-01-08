package com.jernejovc.mkliker;

import java.util.Locale;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
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

/**
 * This class represents the Room, into which a user is connected, either
 * by classic WebSocket connection or SMS participation.
 * @author matej
 *
 */
public class RoomFragment extends Fragment implements ReceiveMessage {
	
	/**
	 * Button listener which sends whatever answer it was given in the
	 * constructor, when clicked.
	 * @author matej
	 *
	 */
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

	// Answer laayouts shown one-by-one depending on the question
	private LinearLayout m_yesnoLayout;
	private LinearLayout m_abcdeSingleLayout;
	private LinearLayout m_abcdeMultiLayout;
	private LinearLayout m_textanswerLayout;

	// Yes / No / Don't know
	private Button m_answerYesButton;
	private Button m_answerNoButton;
	private Button m_answerDontKnowButton;

	// A / B / C / D / E single answer buttons
	private Button m_answerAButton;
	private Button m_answerBButton;
	private Button m_answerCButton;
	private Button m_answerDButton;
	private Button m_answerEButton;
	
	// A / B / C / D / E multi answer checkboxes
	private CheckBox m_answerACheckBox;
	private CheckBox m_answerBCheckBox;
	private CheckBox m_answerCCheckBox;
	private CheckBox m_answerDCheckBox;
	private CheckBox m_answerECheckBox;

	// Arbitrary answer edit text and button which sends answer
	private EditText m_answerEditText;
	private Button m_sendAnswerButton;

	boolean sent_message = false;
	private QuestionType m_questionType;
	private boolean m_questionRunning;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.room_layout, container, false);
		
		// Initialize values and set listeners
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

		// Send Message to lecturer button listener
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

		m_view = view;
		
		if(m_activity.inSMSMode()) {
			m_textanswerLayout.setVisibility(View.GONE);
			m_sendAnswerButton.setVisibility(View.GONE);
			m_activity.showSMSParticipationDialog();
		}
		
		// Enable or disable question
		questionChanged();
		
		return m_view;
	}
	
	@Override
	public void receiveMessage(Message msg) {
		String[] payloads = msg.message().split(Message.SEPARATOR);
		QuestionType type;
		switch(msg.messageType()) {
		case ACKNOWGLEDGE:
			// If we've sent message, ACK confirms its delivery, so we clear edittext
			if(sent_message) {
				m_messageEditText.setText("");
				m_messageEditText.clearFocus();
				sent_message = false;
			}
			break;
		case ROOMSTATUS:
			if(payloads.length >= 5) {
				//					m_roomStatusTextView.setText(payloads[4]);
			}
			break;
		case STARTQUESTION:
			// Set question type and mark question as running
			type = QuestionTypeUtil.stringToQuestionType(payloads[0]);
			setQuestionType(type);
			setRunning(true);
			questionChanged();
			break;
		case STOPQUESTION:
			// Disable question
			type = QuestionTypeUtil.stringToQuestionType(payloads[0]);
			setQuestionType(type);
			setRunning(false);
			questionChanged();
			break;
		case BLOCK:
			// Disable question
			setRunning(false);
			questionChanged();
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
	 * Sets the user data
	 * @param user User data
	 */
	public void setUser(User user) {
		m_user = user;
	}
	
	/**
	 * What kind of QuestionType is currently running
	 * @param type
	 */
	public void setQuestionType(QuestionType type) {
		m_questionType = type;

	}
	
	/**
	 * Set whether or not the question is running 
	 * @param running
	 */
	public void setRunning(boolean running) {
		m_questionRunning = running;

		// If setup is finalized
		if(m_view != null)
			questionChanged();
	}

	/**
	 * Update GUI based on question type and question running status
	 */
	private void questionChanged() {
		if(m_questionRunning) {
			// show question layouts based on question type, also show some informative text
			switch(m_questionType) {
			case YESNO:
				m_roomStatusTextView.setText(R.string.roomfragment_yes_no_dontknow);
				m_yesnoLayout.setVisibility(View.VISIBLE);
				break;
			case ABCDESINGLE:
				m_roomStatusTextView.setText(R.string.roomfragment_abcde);
				m_abcdeSingleLayout.setVisibility(View.VISIBLE);
				break;
			case ABCDEMULTI:
				m_roomStatusTextView.setText(R.string.roomfragment_abcde_multi);
				m_abcdeMultiLayout.setVisibility(View.VISIBLE);
				m_sendAnswerButton.setVisibility(View.VISIBLE);
				break;
			case SHORTANSWER:
				m_roomStatusTextView.setText(R.string.roomfragment_shortanswer);
				m_textanswerLayout.setVisibility(View.VISIBLE);
				m_sendAnswerButton.setVisibility(View.VISIBLE);
				break;
			}
		} else {
			// Hide all layouts
			m_roomStatusTextView.setText(R.string.roomfragment_no_question);

			m_yesnoLayout.setVisibility(View.GONE);

			m_abcdeSingleLayout.setVisibility(View.GONE);

			m_abcdeMultiLayout.setVisibility(View.GONE);
			m_sendAnswerButton.setVisibility(View.GONE);

			m_textanswerLayout.setVisibility(View.GONE);
			m_sendAnswerButton.setVisibility(View.GONE);
		}
	}

	/**
	 * Sends the answer to the server.
	 * @param answer Answer to be sent, only valid if question type is YES/NO or 
	 * ABCDE single, in other cases the answer is constructed.
	 */
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
	
	/**
	 * Sends an SMS message to the lecturer
	 */
	private void sendSMSMessage() {
		String text = String.format(Locale.getDefault(), 
				"%s#MSG#%s", 
				m_user.getRoom(),
				m_messageEditText.getText().toString());
		sendSMS(text);
		m_messageEditText.setText("");
	}
	
	/**
	 * Sends an SMS answer to the lecturer
	 */
	private void sendSMSAnswer() {
		String text = String.format(Locale.getDefault(), 
				"%s#%s", 
				m_user.getRoom(),
				m_answerEditText.getText().toString());
		sendSMS(text);
	}
	
	/**
	 * Sends an arbitrary SMS text to the server 
	 * @param text
	 */
	private void sendSMS(String text) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(m_server.getSMSNumber(), null, text, null, null);
		m_answerEditText.setText("");
	}
	
	/**
	 * Sets parent main activity
	 * @param activity
	 */
	public void setMainActivity(MainActivity activity) {
		m_activity = activity;
	}
}