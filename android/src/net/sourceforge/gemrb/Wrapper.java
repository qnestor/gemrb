package net.sourceforge.gemrb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipException;

import net.sourceforge.gemrb.ActivateFragment.ActivateFragmenter;
import net.sourceforge.gemrb.ConfigFragment.ConfigFragmenter;
import net.sourceforge.gemrb.HomeFragment.HomeFragmenter;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class Wrapper extends Activity implements 
	HomeFragmenter, ActivateFragmenter, ConfigFragmenter {

	public static final String CONFIG_FILENAME = "GemRB.cfg";
	public static final String GAME_TYPE_KEY = "GameType";
	public static final String GAME_PATH_KEY = "GamePath";
	public static final String CACHE_PATH_KEY = "CachePath";
	public static final String CD_ONE_KEY = "CD1";
	public static final String CD_TWO_KEY = "CD2";
	public static final String GAMENAME_KEY = "GameName";
	public static final String BPP_KEY = "Bpp";
	public static final String TOOLTIP_KEY = "TooltipDelay";
	public static final String HEIGHT_KEY = "Height";
	public static final String WIDTH_KEY = "Width";
	public static final String GUI_KEY = "GUIEnhancements";
	public static final String FULLSCREEN_KEY = "Fullscreen";
	public static final String FOW_KEY = "FogOfWar";
	
	// VERIFY ACCESS LEVEL FOR THE FOLLOWING DATAT
	private File mConfigFile;
	public Properties mProperties = new Properties(); // inside method instead?
	private String mGameType;
	private String mGamePath;
	private String mGameName;
	private String mBpp;
	private String mTooltip;
	private String mHeight;
	private String mWidth;
	private boolean mGui;
	private boolean mFullscreeen;
	private boolean mFow;
	
	// FRAGMENTS
	private HomeFragment mHomeFragment;
	private ActivateFragment mActivateFragment;
	private ConfigFragment mConfigFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.container);
		
        if (savedInstanceState != null) {
            return;
        }
        
        readConfigFile();
        startFragments();
	}

	public void startFragments() {
		
		mHomeFragment = new HomeFragment();
		mActivateFragment = new ActivateFragment();
		mConfigFragment = new ConfigFragment();
		
        getFragmentManager().beginTransaction()
        .add(R.id.fragment_container, mHomeFragment).commit();
	}

	@Override
	public void onActivateMenu() {
		
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mActivateFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	}

	@Override
	public void onBack() {
		
		// FRAGMENT OVERLAP? FindByID, xml fragment
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //transaction.replace(R.id.fragment_container, mHomeFragment);

        // Commit the transaction
        //transaction.commit();
		getFragmentManager().popBackStack();
	}
	
	private boolean readConfigFile() {
		
		FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File dir, String filename) {
				
				return filename.equals(CONFIG_FILENAME);
			}
		};
		
		File gemrbHomeFolder = getExternalFilesDir(null);
		File[] listFiles = gemrbHomeFolder.listFiles(filter);
		
		try {
			// Attempt twice if cfg file is not there
			if (!readConfigOperations(gemrbHomeFolder,listFiles,filter)) {
				return readConfigOperations(gemrbHomeFolder,listFiles,filter);
			}
			return true;
		}
		catch (IOException ioExc) {
			return false;
		}
	}
	
	private boolean readConfigOperations(
			File gemrbHomeFolder,File[] listFiles, FilenameFilter filter) throws IOException {
	
		if(listFiles != null && listFiles.length > 0) {
			
			// get file
			mConfigFile = new File(gemrbHomeFolder.getAbsolutePath().concat(File.separator).concat(CONFIG_FILENAME));
			Log.d("event","Config File:"+mConfigFile.toString());
			
			FileInputStream fin = null;
			try {
				
				fin  = new FileInputStream(mConfigFile);
				mProperties.load(fin);
				mGameType = mProperties.getProperty(GAME_TYPE_KEY, getResources().getString(R.string.wrong_configfile));
				Log.d("event", "game type is: "+mProperties.getProperty(GAME_TYPE_KEY));
				mGamePath = mProperties.getProperty(GAME_PATH_KEY,getResources().getString(R.string.wrong_configfile));
				mGameName = mProperties.getProperty(GAMENAME_KEY,getResources().getString(R.string.wrong_configfile));
				mBpp = mProperties.getProperty(BPP_KEY,getResources().getString(R.string.wrong_configfile));
				mTooltip = mProperties.getProperty(TOOLTIP_KEY,getResources().getString(R.string.wrong_configfile));
				mHeight = mProperties.getProperty(HEIGHT_KEY,getResources().getString(R.string.wrong_configfile));;
				mWidth = mProperties.getProperty(WIDTH_KEY,getResources().getString(R.string.wrong_configfile));
				mGui = iString2b(mProperties.getProperty(GUI_KEY,getResources().getString(R.string.wrong_configfile)));
				mFow = iString2b(mProperties.getProperty(FOW_KEY,getResources().getString(R.string.wrong_configfile)));
				mFullscreeen = iString2b(mProperties.getProperty(FULLSCREEN_KEY,getResources().getString(R.string.wrong_configfile)));
				
			}
			catch (FileNotFoundException e) {
				// This should not happen but catch block there not to duplicate close
				return false;
			}
			finally {
				
				if (fin != null) {
					fin.close();
				}
			}
			
			return true;
		}
		else {
			// Config file not in expected location: corrupt folder or first execution
			try {
			File finalConfFile = new File(gemrbHomeFolder.getAbsolutePath().concat(File.separator).concat(CONFIG_FILENAME));
			GemRB.extractFolder(this.getApplication().getPackageCodePath(), gemrbHomeFolder.getAbsolutePath());
			GemRB.createCfgFromAsset(gemrbHomeFolder, finalConfFile);
			}
			catch (ZipException zipError) {
				// do stuff
			}
			catch (IOException ioException) {
				// do stuff
			}
			return false;
		}
	}

	@Override
	public String getGameType() {
		
		return mGameType;
	}

	@Override
	public String getGamePath() {
		
		return mGamePath;
	}

	@Override
	public File getConfFile() {
		
		return mConfigFile;
	}

	@Override
	public String getGameName() {
		return mGameName;
	}

	@Override
	public int getBPP() {
		int a = Integer.parseInt(mBpp);
		return a;
	}

	@Override
	public int getHeight() {
		return Integer.parseInt(mHeight);
	}

	@Override
	public int getWidth() {
		return Integer.parseInt(mWidth);
	}

	@Override
	public int getTooltip() {
		return Integer.parseInt(mTooltip);
	}

	@Override
	public boolean getFullscreen() {
		return mFullscreeen;
	}

	@Override
	public boolean getGUI() {
		return mGui;
	}

	@Override
	public boolean getFOW() {
		return mFow;
	}
	
	public boolean iString2b (String val) {
		
		if (val.equals("1")) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void onConfigMenu() {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mConfigFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	}

	@Override
	public void refresh() {
		
		readConfigFile();
	}
}
