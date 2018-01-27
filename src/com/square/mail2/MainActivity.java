package com.square.mail2;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends ListActivity {
TextView t;
EditText tHost;
EditText tUsername;
EditText tPassword;
RadioGroup rProtocolGroup;
RadioButton rProtocol;
RadioButton rProtocolImap;
EditText tWebmail;
static List<String> mailMess=null;
static final String pop3Host ="";
static final String storeType ="";
static final String user= "";
static final String password = "";
String wrongEmailIds;
String message;
static final String webmail = "";
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	tHost=(EditText)findViewById(R.id.editText1);
	tUsername=(EditText)findViewById(R.id.editText2);
	tPassword=(EditText)findViewById(R.id.editText3);
	tHost=(EditText)findViewById(R.id.editText1);
	rProtocolGroup = (RadioGroup) findViewById(R.id.radioGroup1);
	rProtocol = (RadioButton)findViewById(R.id.radioButton1);
	rProtocolImap = (RadioButton)findViewById(R.id.radioButton2);
	tWebmail=(EditText)findViewById(R.id.editText4);
   
	 ParamDataSource paramDataSource = new ParamDataSource(getBaseContext());
     paramDataSource.open();
     tHost.setText(paramDataSource.getParam(1).toString());
     tUsername.setText(paramDataSource.getParam(2).toString());
     tPassword.setText(paramDataSource.getParam(3).toString());
     if(Boolean.valueOf(paramDataSource.getParam(4).toString())){rProtocol.setChecked(true);rProtocolImap.setChecked(false);}
     if(Boolean.valueOf(paramDataSource.getParam(5).toString())){rProtocol.setChecked(false);rProtocolImap.setChecked(true);}
     tWebmail.setText(paramDataSource.getParam(6).toString());
         
     Button fetch = (Button) findViewById(R.id.button1);
     fetch.setOnClickListener(new View.OnClickListener() {
     @Override
          public void onClick(View view) {
            ParamDataSource paramDataSource = new ParamDataSource(getBaseContext());
            paramDataSource.open();
            String[]tmpStArray= new String[6];
            tmpStArray[0]=tHost.getText().toString();
            tmpStArray[1]=tUsername.getText().toString(); 
            tmpStArray[2]=tPassword.getText().toString();
            if(Boolean.valueOf(rProtocol.isChecked())){tmpStArray[3]="true";tmpStArray[4]="false";}
            else if(Boolean.valueOf(rProtocolImap.isChecked())){tmpStArray[3]="false";tmpStArray[4]="true";}
            tmpStArray[5]=tWebmail.getText().toString();
            paramDataSource.updateParam(tmpStArray);
            tHost.setText(paramDataSource.getParam(1).toString());
            tUsername.setText(paramDataSource.getParam(2).toString());
            tPassword.setText(paramDataSource.getParam(3).toString());
            if(Boolean.valueOf(paramDataSource.getParam(4).toString())){rProtocol.setChecked(true);rProtocolImap.setChecked(false);}
            if(Boolean.valueOf(paramDataSource.getParam(5).toString())){rProtocol.setChecked(false);rProtocolImap.setChecked(true);}
           
            t =(TextView) findViewById(R.id.textView1);
            System.out.println("vv before asyntask");
            Log.e("vv", "before asynctask");
            t.setText(getResources().getString(R.string.seeking));
            if(tHost.getText().toString().equals(getResources().getString(R.string.protocol))||tUsername.getText().toString().equals(getResources().getString(R.string.mail)))t.setText(R.string.host_or_username);
            else{
            new AsyncTask<Void, Void, Void>() {
				
            	protected Void doInBackground(Void ... v) {
            		String host = tHost.getText().toString();;//change accordingly
            		String username= tUsername.getText().toString();
            		String password= tPassword.getText().toString();//change accordingly
            		int selectedId = rProtocolGroup.getCheckedRadioButtonId();
				 	Log.e("vv",host+" "+username+" "+password+" "+selectedId+" "+R.id.radioButton2);
				 	// 	find the radiobutton by returned id
				 	if(selectedId==R.id.radioButton1)
				 		mailMess=ReceiveMail.receiveEmail(host, "pop3", username, password);
				 	else if(selectedId==R.id.radioButton2)
				 		mailMess=ReceiveMail.receiveEmail(host, "imap", username, password);
				 	else
			    	   wrongEmailIds=getResources().getString(R.string.sorry);
				  
				 	System.out.println("vv");
				 	Log.e("vv","in asynctask");
				 	return null;
            	}
			
            	@Override
            	protected void onPostExecute(Void result) {
            		t.setText(getResources().getString(R.string.done));
            		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
			    	        android.R.layout.simple_list_item_1, mailMess);
			    	try{setListAdapter(adapter);}catch(Exception e){t.setText(getResources().getString(R.string.sorry));
			    	}
			    	
			    	Log.e("vv","post execute");
		         
            	}
            }.execute();
        }
	    

     
      }
  });
     
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	  public boolean onOptionsItemSelected(MenuItem item)
	    {
	         
	        switch (item.getItemId())
	        {
	        case R.id.menu_webmail:
	            // Single menu item is selected do something
	            // Ex: launching new activity/screen or show alert message
	        	String url= tWebmail.getText().toString();
	        	if (!url.startsWith("http://") && !url.startsWith("https://"))
	        		   url = "http://" + url;
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        	startActivity(browserIntent);
	            return true;
	        }
			return false;
	    }
	
}
