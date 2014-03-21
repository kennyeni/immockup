package mx.kennyeni.immockup;

import java.util.ArrayList;





import java.util.HashSet;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.LoaderManager;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class MainActivity extends ListActivity implements NewContactDialogFragment.NoticeDialogListener {

    // This is the Adapter being used to display the list's data
	CustomParseQueryAdapter<ParseObject> adaptador;
	HashSet<String> conversaciones = new HashSet<String>();

	private ParseUser currentUser;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
        
        if(VariablesGlobales.counter){
        	Parse.initialize(this, "I7aW7NSedlwvNtAjZmQm9bcUT7FPSvl10SOauIey", "L3DPvfP1pTn7MvVzoOzQqBqwqHDp1DKGOFXj8dzt");
        	VariablesGlobales.counter = false;
        }
		login();
		cargarConversaciones();
	}

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    	Intent intent = new Intent(this, Messages.class);
    	intent.putExtra(VariablesGlobales.INTENT_MENSAJE_DESTINATARIO, adaptador.getItem(position).getString("Para"));
		startActivity(intent);
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add("Agregar contacto");
		menu.add("Salir");		
		return true;
	}
	
	private void sendToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	FindCallback<ParseObject> emisorCallback = new FindCallback<ParseObject>() {
		
		@Override
		public void done(List<ParseObject> objects, ParseException e) {
			if (e != null){
				sendToast(e.getMessage());
			}else{
				for(ParseObject destinatario : objects){
					conversaciones.add(destinatario.getString("Destinatario"));
				}
			}
			
		}
	};
	
	private void cargarConversaciones(){
		if (currentUser == null) return;
		
		// Adpatador con todas las conversaciones
		ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
					@Override
					public ParseQuery<ParseObject> create() {
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Conversacion");
						query.whereEqualTo("De", currentUser.getUsername());
						query.orderByDescending("updatedAt");
						return query;
					}
				};
		adaptador = new CustomParseQueryAdapter<ParseObject>(this, factory);
		adaptador.setTextKey("Para");
		
		adaptador.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {
			@Override
			public void onLoaded(List<ParseObject> objects, Exception e) {
				
			}

			@Override
			public void onLoading() {}
		});
		
		setListAdapter(adaptador);
		
	}
	
	private void login(){
		currentUser = ParseUser.getCurrentUser();
		
		if (currentUser == null){
			Intent intent = new Intent(this, Login.class);
		    startActivity(intent);
		    this.finish();		   
		}else{
			String username = currentUser.getUsername();
			PushService.subscribe(this, username, Messages.class);
		}
	}
	
	public void mensajes(View view){
		Intent intent = new Intent(this, Messages.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case 0:
	    	    DialogFragment newFragment = new NewContactDialogFragment();
	    	   // newFragment.show(getSupportFragmentManager(), "newContact"); falta corregir esta linea
	    	    break;
	    	case 1:
	            ParseUser.logOut();
	            Intent intent = new Intent(this,Login.class);	            
				startActivity(intent);
				this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
		

}
