package com.example.android.visualizerpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

/**
 * Created by Swapnil on 18-12-2016.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener{
    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
          addPreferencesFromResource(R.xml.pref_visualizer);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for(int i=0;i<count;i++){
            Preference p = preferenceScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }

        }

        Preference pref = findPreference(getString(R.string.pref_size_key));
        pref.setOnPreferenceChangeListener(this);
    }
    private void setPreferenceSummary(Preference pref , String value){
       if(pref instanceof ListPreference){
           ListPreference listPreference = (ListPreference) pref;
           int prefIndex = listPreference.findIndexOfValue(value);
           if(prefIndex>=0){
               listPreference.setSummary(listPreference.getEntries()[prefIndex]);
               }
       }
        else if ( pref instanceof EditTextPreference){
           pref.setSummary(value);
       }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference pref = findPreference(s);
        if(null!=pref){
            if( !(pref instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(pref.getKey(),"");
                setPreferenceSummary(pref, value);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Called when a Preference has been changed by the user. This is
     * called before the state of the Preference is about to be updated and
     * before the state is persisted.
     *
     * @param preference The changed Preference.
     * @param newValue   The new value of the Preference.
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

         Toast error = Toast.makeText(getContext(), "Please select a number between 0.1 and 3",Toast.LENGTH_SHORT);
        String sizeKey = getString(R.string.pref_size_key);
        if(preference.getKey().equals(sizeKey)){
            String stringSize = ((String) (newValue)).trim();
            if(stringSize.equals("")) {
                stringSize = "1";
            }
            try {
                float size = Float.parseFloat(stringSize);
                if(size>3 || size <=0){
                    error.show();
                    return false;
                }
            }catch(NumberFormatException e){
                error.show();
                return false;
            }
        }
        return true;
    }
}
