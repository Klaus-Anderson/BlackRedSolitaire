package gms.angus.angussoli.view

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import gms.angus.angussoli.R

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView.setBackgroundColor(TypedValue().let{
            activity?.theme?.resolveAttribute(R.attr.backgroundColor, it, true)
            it.data
        })
        super.onViewCreated(view, savedInstanceState)
    }
}