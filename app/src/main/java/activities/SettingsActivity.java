package activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.popularmoviesstage1.R;

import java.util.List;

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class SettingsActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsAppPreferences extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        private Preference section;
        private Preference orderBy;
        private Context context;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_main_settings);
            section = findPreference(getString(R.string.settings_api_key_key));
            bindPreferenceSummaryValue(section);
            orderBy = findPreference(getString(R.string.settings_search_key));
            bindPreferenceSummaryValue(orderBy);
        }

        private void bindPreferenceSummaryValue(Preference section) {
            section.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(section.getContext());
            String prefString = preferences.getString(section.getKey(),"");
            onPreferenceChange(section, prefString);

        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            bindPreferenceSummaryValue(section);
            bindPreferenceSummaryValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String value = o.toString();
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference)preference;
                int index = listPreference.findIndexOfValue(value);
                if (index >= 0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary((labels[index]));
                }
            }else {
                preference.setSummary(value);
            }
            return true;
        }
    }
}
