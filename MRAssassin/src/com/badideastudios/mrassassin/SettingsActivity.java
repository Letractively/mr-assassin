package com.badideastudios.mrassassin;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity
{
	AssassinApp app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		app = (AssassinApp) getApplication();
		
		/**Access player information for display*/
		Preference playerNamePref = (Preference) findPreference("UserPref");
		playerNamePref.setSummary( app.getOurName() );
		
		/**Access version information for display*/
		Preference versionPref = (Preference) findPreference("VersionPref");
		versionPref.setSummary( app.getVersion() );
		
		/**Handle button click on logout button */
		Preference logoutPref = (Preference) findPreference("LogoutPref");
        logoutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() 
        {
                public boolean onPreferenceClick(Preference preference) 
                {
                        Toast.makeText(getBaseContext(), "Logging out.", Toast.LENGTH_LONG).show();
                        //Remove all player information and return to login activity.
                        return true;
                }

        });
		
		/**Handle button click on "About Us" button */
		Preference aboutPref = (Preference) findPreference("AboutUsPref");
        aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() 
        {
                public boolean onPreferenceClick(Preference preference) 
                {
                		Intent creditsActivity = new Intent(getBaseContext(), CreditsActivity.class);
                		startActivity(creditsActivity);
                        return true;
                }

        });
	}
}
