package net.sourceforge.gemrb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.sourceforge.gemrb.ActivateFragment.gameTypes;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ConfigFragment extends Fragment{

	ConfigFragmenter mCallback;
	
	
	public interface ConfigFragmenter {
		
		public File getConfFile();
		public void onBack();
		public String getGameName();
		public int getBPP();
		public int getHeight();
		public int getWidth();
		public int getTooltip();
		public boolean getFullscreen();
		public boolean getGUI();
		public boolean getFOW();
		public String getGameType();
		public void subUpdateRun(ImageView viewToUpdate, gameTypes gameType,String icoFilePrefix);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		View returnView = inflater.inflate(R.layout.sub_config, container, false);
		setFields(returnView);
		updateRunIcon(ActivateFragment.gameTypes.valueOf(mCallback.getGameType()),returnView);
		
		return returnView;
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ConfigFragmenter) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConfigFragmenter");
        }
    }
    
    private void setFields(View returnView) {
    
		/*EditText gameName = (EditText) returnView.findViewById(R.id.game_name_textbox);
		gameName.setText(mCallback.getGameName());*/
		EditText bpp = (EditText) returnView.findViewById(R.id.bpp_textbox);
		bpp.setText(Integer.toString(mCallback.getBPP()));
		EditText tooltip = (EditText) returnView.findViewById(R.id.tooltip_textbox);
		tooltip.setText(Integer.toString(mCallback.getTooltip()));
		EditText height = (EditText) returnView.findViewById(R.id.height_textbox);
		height.setText(Integer.toString(mCallback.getHeight()));
		EditText width = (EditText) returnView.findViewById(R.id.width_textbox);
		width.setText(Integer.toString(mCallback.getWidth()));

		CheckBox checkboxFullscreen = (CheckBox) returnView.findViewById(R.id.checkbox_fullscreen);
		checkboxFullscreen.setChecked(mCallback.getFullscreen());
		CheckBox checkboxGUI = (CheckBox) returnView.findViewById(R.id.checkbox_gui);
		checkboxGUI.setChecked(mCallback.getGUI());
		CheckBox checkboxFOW = (CheckBox) returnView.findViewById(R.id.checkbox_fow);
		checkboxFOW.setChecked(mCallback.getFOW());
		
		Button saveButton = (Button) returnView.findViewById(R.id.save_button);
		saveButton.setOnClickListener(mSaveButton_l);
		Button backButton = (Button) returnView.findViewById(R.id.back_button_conf);
		backButton.setOnClickListener(mBackButton_l);
    }
    
	private OnClickListener mBackButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mCallback.onBack();
		}
		
	};
	
	private OnClickListener mSaveButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			try {
				doWriteOperations();
				Toast.makeText(getActivity(), getResources().getString(R.string.config_file_changed), Toast.LENGTH_LONG).show();
			}
			catch (IOException ioexc) {
				// Display message
				Log.d("event", "error while writing in config file");
			}
		}
		
	};
	
	private void doWriteOperations() throws IOException {
		
		View returnView = this.getView(); // Try with this <===
		Properties properties = new Properties();
		FileInputStream fin = null;
		FileOutputStream fout = null;
		
		//EditText gameName = (EditText) returnView.findViewById(R.id.game_name_textbox);
		EditText bpp = (EditText) returnView.findViewById(R.id.bpp_textbox);
		EditText tooltip = (EditText) returnView.findViewById(R.id.tooltip_textbox);
		EditText height = (EditText) returnView.findViewById(R.id.height_textbox);
		EditText width = (EditText) returnView.findViewById(R.id.width_textbox);
		CheckBox checkboxFullscreen = (CheckBox) returnView.findViewById(R.id.checkbox_fullscreen);
		CheckBox checkboxGUI = (CheckBox) returnView.findViewById(R.id.checkbox_gui);
		CheckBox checkboxFOW = (CheckBox) returnView.findViewById(R.id.checkbox_fow);
		
		try {
			fin  = new FileInputStream(mCallback.getConfFile());
			
			properties.load(fin);
			//properties.setProperty(Wrapper.GAMENAME_KEY, gameName.getText().toString());
			properties.setProperty(Wrapper.BPP_KEY, bpp.getText().toString());
			properties.setProperty(Wrapper.TOOLTIP_KEY, tooltip.getText().toString());
			properties.setProperty(Wrapper.HEIGHT_KEY, height.getText().toString());
			properties.setProperty(Wrapper.WIDTH_KEY, width.getText().toString());
			properties.setProperty(Wrapper.FULLSCREEN_KEY, b2iString(checkboxFullscreen.isChecked()));
			properties.setProperty(Wrapper.GUI_KEY, b2iString(checkboxGUI.isChecked()));
			properties.setProperty(Wrapper.FOW_KEY, b2iString(checkboxFOW.isChecked()));
			
			fout  = new FileOutputStream(mCallback.getConfFile());
			
			properties.store(fout, null);
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
	
	public String b2iString(boolean bol) {
		
		return bol? "1" : "0";

	}
	
	public void updateRunIcon(gameTypes gameType, View returnView) {
		
		ImageView botIcon = (returnView == null) ? 
				(ImageView)this.getView().findViewById(R.id.small_bot_image) :
				(ImageView)returnView.findViewById(R.id.small_bot_image);
	
		mCallback.subUpdateRun(botIcon,null,null);
	}
}
