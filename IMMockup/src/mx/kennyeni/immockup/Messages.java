package mx.kennyeni.immockup;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class Messages extends ListActivity {
	private ParseUser currentUser;
	private String destinatario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		ParseAnalytics.trackAppOpened(getIntent());
		currentUser = ParseUser.getCurrentUser();		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {			
		    destinatario = extras.getString("STRING_I_NEED");
		    if(destinatario.equals(currentUser)){
		    	sendToast("No puedes hablar contigo mismo -.-");
		    	this.finish();
			}		    
		}else{
			this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add("Salir");
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
		((EditText)findViewById(R.id.mesnajes_mensaje)).setText("");		
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case 0:
	            ParseUser.logOut();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
