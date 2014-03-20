package mx.kennyeni.immockup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class Messages extends Activity {

	private ParseUser currentUser;
	private String destinatario = "kennyeni";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		ParseAnalytics.trackAppOpened(getIntent());
		currentUser = ParseUser.getCurrentUser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void sendToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	
	public void sendMessage(View v){
		String str = ((EditText)findViewById(R.id.mesnajes_mensaje)).getText() + "";
		
		ParseObject msg = new ParseObject("Mensajes");
		msg.put("Emisor", currentUser.getUsername());
		msg.put("Destinatario", destinatario);
		msg.put("Mensaje", str);
		msg.saveInBackground(messageCallback);
	}
	
	SaveCallback messageCallback = new SaveCallback() {
		
		@Override
		public void done(ParseException e) {
			if (e == null){
				sendToast("Mensaje enviado");
			}else{
				sendToast(e.getMessage());
			}
			
		}
	};

}
