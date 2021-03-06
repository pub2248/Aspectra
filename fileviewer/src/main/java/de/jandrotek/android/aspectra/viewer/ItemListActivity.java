/**
 * This file is part of Aspectra.
 *
 * Aspectra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aspectra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aspectra.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jan Debiec
 */
package de.jandrotek.android.aspectra.viewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.libprefs.AspectraGlobalPrefsActivity;
import de.jandrotek.android.aspectra.libprefs.AspectraLiveViewPrefs;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

public class ItemListActivity extends ActionBarActivity
        implements ItemListFragment.Callbacks {

    private static final String TAG = "ListItemsAct";
    private AspectraLiveViewPrefs mAspectraSettings;
    private String mFileFolder;
    private String mFileExt;

    private SpectrumFiles mSpectrumFiles = null;
    private int mFileListSize = 0;
    private int mChartLength;
    public int mPlotsCount = 1;// default = 1, can be changed in ListFragment
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate() called");
        }

        mAspectraSettings = new AspectraLiveViewPrefs();
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        mAspectraSettings.connectPrefs(context, prefs);

        updateFromPreferences();

        if(mSpectrumFiles == null) {
            mSpectrumFiles = new SpectrumFiles();
            mFileListSize = mSpectrumFiles.scanFolderForFiles(mFileFolder,mFileExt );
        }

        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFromPreferences();
        mFileListSize = mSpectrumFiles.scanFolderForFiles(mFileFolder,mFileExt );
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AspectraGlobalPrefsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_help){

        }
        else if (id == R.id.action_about){

        }
        return false;
    }

    @Override
    public void onItemSelected(ArrayList<String> filesNames) {
//TODO: activate the twoPane display
//        if (mTwoPane) {
//            // t will be fixed later, first we go with single pane
//
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putStringArrayList(PlotViewFragment_notToUse.ARG_ITEM_IDS, filesNames);
//            PlotViewFragment_notToUse fragment = new PlotViewFragment_notToUse();
//            fragment.setArguments(arguments);
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit();
//
//        } else {
            Bundle arguments = new Bundle();
            arguments.putStringArrayList(AspectraGlobals.ARG_ITEM_IDS, filesNames);
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtras(arguments);
            startActivity(detailIntent);
//        }
    }

    protected void updateFromPreferences() {
        mAspectraSettings.loadSettings();
        mFileFolder = mAspectraSettings.getPrefsSaveFolderName();
        mFileExt = mAspectraSettings.getPrefsExtensionName();
    }
}
