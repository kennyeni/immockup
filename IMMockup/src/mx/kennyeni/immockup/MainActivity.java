package mx.kennyeni.immockup;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.LoaderManager;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class MainActivity extends ListActivity {

    // This is the Adapter being used to display the list's data
	ArrayAdapter mAdapter;

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

       ArrayList<String> list = new ArrayList<String>();

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        setListAdapter(mAdapter);
        
        
		Parse.initialize(this, "I7aW7NSedlwvNtAjZmQm9bcUT7FPSvl10SOauIey", "L3DPvfP1pTn7MvVzoOzQqBqwqHDp1DKGOFXj8dzt");
		login();
		cargarConversaciones();
	}

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
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
	
	private void cargarConversaciones(){
		mAdapter.add("Holi");
	}
	
	private void login(){
		currentUser = ParseUser.getCurrentUser();
		
		if (currentUser == null){
			Intent intent = new Intent(this, Login.class);
		    startActivity(intent);
		}else{
			String username = currentUser.getUsername();
			PushService.subscribe(this, username, Messages.class);
		}
	}
	
	public void mensajes(View view){
		Intent intent = new Intent(this, Messages.class);
		startActivity(intent);
	}

}
