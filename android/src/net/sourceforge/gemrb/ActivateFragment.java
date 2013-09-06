package net.sourceforge.gemrb;

/* This should:
 * Ask only for the gamepath and then just populate the rest of the paths:
 * -cache
 * cd1 and cd2
 * Maybe the installation type can be provided by the user. 
 * Verify gamepath can check if the previous structure exists and verify other conditions such as:
 * - Valid cache contents
 * - Valid resolution chose in mod/ supported by phone
 * WOT ELLLSSEE?? 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivateFragment extends Fragment{
	
	// the following maybe are temporary. How many types of installs are there?
	public static final String CACHE_DIR = "cache";
	public static final String CD1_DIR = "CD1";
	public static final String CD2_DIR = "CD2";
	public static final String SEPARATOR = File.separator;
	
	ActivateFragmenter mCallback;
	public static final int INTENT_PICK_DIR = 1;
	public enum gameTypes { // It has to match resources array
		bg1(0),bg2(1),iwd(2),iwd2(3),how(4),pst(5);
	
		private int id;
		gameTypes(int ido){ id = ido;}
		int getId() { return id;}
	}
	
	public interface ActivateFragmenter {
		
		public void onBack();
		public String getGameType();
		public String getGamePath();
		public File getConfFile();
		public void refresh();
		public String getGameName();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		View returnView = inflater.inflate(R.layout.sub_activate, container, false);
		
		setFields(returnView);
		setSpinnerListener(returnView);
		updateRunIcon(ActivateFragment.gameTypes.valueOf(mCallback.getGameType()),returnView);
		return returnView;
	}
	
	private void setFields(View returnView) {
		
		EditText gameName = (EditText) returnView.findViewById(R.id.game_name_textbox);
		gameName.setText(mCallback.getGameName());
		Button activateButton = (Button) returnView.findViewById(R.id.below_top_button);
		activateButton.setOnClickListener(mSetPathButton_l);
		Button bugButton = (Button) returnView.findViewById(R.id.below_3top_button);
		bugButton.setOnClickListener(mVerifyButton_l);
		Button creditsButton = (Button) returnView.findViewById(R.id.lbot_below_button);
		creditsButton.setOnClickListener(mBackButton_l);
		Button saveButton = (Button) returnView.findViewById(R.id.rbot_button);
		saveButton.setOnClickListener(mSaveButton_l);
		
		TextView gamePathText = (TextView) returnView.findViewById(R.id.text_below_top_button);
		gamePathText.setText(mCallback.getGamePath());
		
		Spinner selectedGame = (Spinner)returnView.findViewById(R.id.spinner_top_button);
		selectedGame.setSelection(gameTypes.valueOf(mCallback.getGameType()).getId());
	}
	
	private void setSpinnerListener(View returnView) {
		
		Spinner selectedGame = (Spinner)returnView.findViewById(R.id.spinner_top_button);
		selectedGame.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.d("event","Selected:"+arg0.getItemAtPosition(arg2).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.d("event", "nothing was selected in the spinner");
			}});
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ActivateFragmenter) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivateFragmenter");
        }
    }
	
	
	private OnClickListener mSetPathButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			Intent intent = new Intent("com.estrongs.action.PICK_DIRECTORY");
			try {
				startActivityForResult(intent, INTENT_PICK_DIR);
			}
			catch (ActivityNotFoundException noApp) {
				
				Toast.makeText((Context)mCallback, R.string.no_explorer, Toast.LENGTH_LONG).show();
			}
			
		}
		
	};
	
	private OnClickListener mVerifyButton_l = new Button.OnClickListener() {
		
		
		@Override
		public void onClick(View arg0) {
			
			String path = getPathSet();
			gameTypes gameSelected = getGameSelected();
			File gamepath = new File(path);
			boolean result = false;
			
			switch (gameSelected) {
			
			case iwd2:
				Log.d("event", "verifying IWD2...");
				result = GameVerifier.verifyIWD2(gamepath);
				break;
			case bg2:
				Log.d("event", "verifying BG2...");
				result = GameVerifier.verifyIWD2(gamepath);
				break;
			case iwd:
				Log.d("event", "verifying IWD...");
				result = GameVerifier.verifyIWD(gamepath);
				break;
			case pst:
				Log.d("event", "verifying PST...");
				result = GameVerifier.verifyPST(gamepath);
				break;
			case how:
				Log.d("event", "verifying HOW...");
				result = GameVerifier.verifyHOW(gamepath);
				break;
			case bg1:
				Log.d("event", "verifying BG1...");
				result = GameVerifier.verifyIWD2(gamepath);
			default:
				break;
			
			}
			
			int resultTextid = (result) ? R.string.gamepath_verify_good : R.string.gamepath_verify_bad;
			String resultText = getResources().getString(resultTextid);
			Toast.makeText(getActivity(), resultText, Toast.LENGTH_LONG).show();
		}
		
	};
	
	private OnClickListener mBackButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mCallback.onBack();
		}
		
	};

	private OnClickListener mSaveButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			View returnView = (View)arg0.getParent(); // This button has to be Fragment's view Child!
			gameTypes gameSelected = getGameSelected();
			
			try {
				doWriteOperations(returnView);
			}
			catch (IOException ioexc) {
				// Display message
				Log.d("event", "error while writing in config file");
			}
			
			updateRunIcon(gameSelected,null);
			mCallback.refresh();
		}
		
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == INTENT_PICK_DIR && data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				TextView path = (TextView)this.getView().findViewById(R.id.text_below_top_button);
				path.setText(uri.getPath());
			}  
		}
	}
	
	private void doWriteOperations(View returnView) throws IOException {
		
		//returnView = this.getView(); // Try with this <===
		Properties properties = new Properties();
		FileInputStream fin = null;
		FileOutputStream fout = null;
		EditText gameName = (EditText) returnView.findViewById(R.id.game_name_textbox);
		TextView gamePathText = (TextView) returnView.findViewById(R.id.text_below_top_button);
		Spinner selectedGame = (Spinner)returnView.findViewById(R.id.spinner_top_button);
		
		try {
			fin  = new FileInputStream(mCallback.getConfFile());
			
			properties.load(fin);
			properties.setProperty(Wrapper.GAME_PATH_KEY, (String)gamePathText.getText());
			properties.setProperty(Wrapper.GAMENAME_KEY, gameName.getText().toString());
			properties.setProperty(Wrapper.GAME_TYPE_KEY, (String)selectedGame.getSelectedItem());
			properties.setProperty(Wrapper.CACHE_PATH_KEY, ((String)gamePathText.getText()).concat(SEPARATOR).concat(CACHE_DIR).concat(SEPARATOR));
			properties.setProperty(Wrapper.CD_ONE_KEY, ((String)gamePathText.getText()).concat(SEPARATOR).concat(CD1_DIR).concat(SEPARATOR));
			properties.setProperty(Wrapper.CD_TWO_KEY, ((String)gamePathText.getText()).concat(SEPARATOR).concat(CD2_DIR).concat(SEPARATOR));
			fout  = new FileOutputStream(mCallback.getConfFile());
			
			properties.store(fout, null);
			Toast.makeText(getActivity(), getResources().getString(R.string.config_file_changed), Toast.LENGTH_LONG).show();
		}
		catch (FileNotFoundException e) {
			// This should not happen but catch block there not to duplicate close
			/*In any case, SHOW A TOAST MESSAGE*/
			Log.d("event", "The fragment could not read config file");
		}
		
		finally {
			
			if (fin != null) {
				fin.close();
				fout.close();
			}
		}
	}
	
	public gameTypes getGameSelected() {
		
		Spinner selectedGame = (Spinner)this.getView().findViewById(R.id.spinner_top_button);
		return gameTypes.valueOf((String)selectedGame.getSelectedItem());
	}
	
	public String getPathSet() {
		
		TextView pathText = (TextView)this.getView().findViewById(R.id.text_below_top_button);
		return (String) pathText.getText();
	}
	
	public void updateRunIcon(gameTypes gameType, View returnView) {
		
		ImageView botIcon = (returnView == null) ? 
				(ImageView)this.getView().findViewById(R.id.small_bot_image) :
				(ImageView)returnView.findViewById(R.id.small_bot_image);
		
		switch (gameType) {
		
		case iwd2:
			botIcon.setImageDrawable(getResources().getDrawable(R.drawable.iwd2ico));
			break;
		case bg2:
			botIcon.setImageDrawable(getResources().getDrawable(R.drawable.bg2ico));
			break;
		case bg1:
			botIcon.setImageDrawable(getResources().getDrawable(R.drawable.bgico));
			break;
		case pst:
			botIcon.setImageDrawable(getResources().getDrawable(R.drawable.pstico));
			break;
		case how:
			botIcon.setImageDrawable(getResources().getDrawable(R.drawable.howico));
			break;
		case iwd:
			botIcon.setImageDrawable(getResources().getDrawable(R.drawable.iwdico));
			break;
		default:
			break;
		
		}
	}
}
