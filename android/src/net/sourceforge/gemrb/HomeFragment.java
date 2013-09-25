package net.sourceforge.gemrb;

import net.sourceforge.gemrb.ActivateFragment.gameTypes;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class HomeFragment extends Fragment {

	HomeFragmenter mCallback;
	
	public interface HomeFragmenter {
		
		public void onActivateMenu();
		public void onConfigMenu();
		public String getGameType();
		public void subUpdateRun(ImageView viewToUpdate, gameTypes gameType,String icoFilePrefix);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		View returnView = inflater.inflate(R.layout.activity_wrapper, container, false);
		
		Button instructionsButton = (Button) returnView.findViewById(R.id.top_button);
		instructionsButton.setOnClickListener(mInstructionsButton_l);
		Button activateButton = (Button) returnView.findViewById(R.id.below_top_button);
		activateButton.setOnClickListener(mActivateButton_l);
		Button configButton = (Button) returnView.findViewById(R.id.below_2top_button);
		configButton.setOnClickListener(mConfigButton_l);
		Button bugButton = (Button) returnView.findViewById(R.id.below_3top_button);
		bugButton.setOnClickListener(mBugButton_l);
		Button creditsButton = (Button) returnView.findViewById(R.id.lbot_below_button);
		creditsButton.setOnClickListener(mCreditsButton_l);
		Button playButton = (Button) returnView.findViewById(R.id.rbot_button);
		playButton.setOnClickListener(mPlayButton_l);
		
		return returnView;
	}

	@Override
	public void onViewCreated(View returnView, Bundle savedInstanceState) {
		
		ImageView botIcon = (returnView == null) ? 
				(ImageView)this.getView().findViewById(R.id.small_bot_image) :
				(ImageView)returnView.findViewById(R.id.small_bot_image);
		//updateRunIcon(ActivateFragment.gameTypes.valueOf(mCallback.getGameType()),returnView);
		mCallback.subUpdateRun(botIcon, null, null);
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (HomeFragmenter) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement HomeFragmenter");
        }
    }
	
	private OnClickListener mActivateButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mCallback.onActivateMenu();
		}
		
	};
	
	private OnClickListener mInstructionsButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Log.d("event","need instructions you?");
		}
		
	};
	
	private OnClickListener mConfigButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mCallback.onConfigMenu();
		}
		
	};
	
	private OnClickListener mBugButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Log.d("event","Oh no! there be no bugs!");
		}
		
	};
	
	private OnClickListener mCreditsButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Log.d("event","Made by Elsa Pito");
		}
		
	};
	
	private OnClickListener mPlayButton_l = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			Intent gemrbStart = new Intent(getActivity(), GemRB.class);
			Log.d("event", "Attempting to start GemRB...");
			getActivity().startActivity(gemrbStart);
		}
		
	};
	
}
