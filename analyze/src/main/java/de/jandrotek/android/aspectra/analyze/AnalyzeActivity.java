package de.jandrotek.android.aspectra.analyze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;

public class AnalyzeActivity extends AppCompatActivity {

    private AnalyzeFragment mAnalyzeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        if (savedInstanceState == null) {
//            Bundle arguments = new Bundle();

            Map<String, String> spectra = new HashMap<>();

            if(getIntent().getExtras().containsKey(AnalyzeFragment.ARG_ITEM_REFERENCE)){
                spectra.put(AnalyzeFragment.ARG_ITEM_REFERENCE, getIntent().getExtras().getString(AnalyzeFragment.ARG_ITEM_REFERENCE));
            }
            if(getIntent().getExtras().containsKey(AnalyzeFragment.ARG_ITEM_EDIT)){
                spectra.put(AnalyzeFragment.ARG_ITEM_EDIT, getIntent().getExtras().getString(AnalyzeFragment.ARG_ITEM_EDIT));
            }

            mAnalyzeFragment = AnalyzeFragment.newInstance(spectra);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_analyze_container, mAnalyzeFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analyze, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
