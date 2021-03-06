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
package de.jandrotek.android.aspectra.plottest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;
import de.jandrotek.android.aspectra.libprefs.AspectraGlobalPrefsActivity;

public class MainActivity extends AppCompatActivity
    implements ButtonHolderFragment.OnButtonClickListener
{

    public PlotTestModelController mModelController = null;
    private PlotViewPresenter mPlotViewPresenter = null;

    private static PlotViewFragment mPlotViewFragment = null;
    private static ButtonHolderFragment mButtonHolderFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ArrayList<String> dummyItems = null;


        mButtonHolderFragment = ButtonHolderFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentButtonHolder, mButtonHolderFragment)
                .commit();

        mPlotViewFragment = PlotViewFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fvPlotView, mPlotViewFragment)
                .commit();
        mPlotViewPresenter = new PlotViewPresenter(1, mPlotViewFragment);
        mModelController = new PlotTestModelController(this);
        mModelController.setPlotViewPresenter(mPlotViewPresenter);
   }

//    public boolean isPlotFragmentInitialized(){
//        return mPlotViewFragment.isFullInitialized();
//    }

//    public void initPlotPresenter( int[][] data){
//        mPlotViewPresenter.init(1, data);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, AspectraGlobalPrefsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClickListener(int _buttonId){
        switch (_buttonId) {
            case ButtonHolderFragment.eButtonMoveLeft:{
                mModelController.onButtonMoveLeft();
                break;
            }
            case ButtonHolderFragment.eButtonMoveRight:{
                mModelController.onButtonMoveRight();
                break;
            }
            case ButtonHolderFragment.eButtonStretch:{
                mModelController.onButtonStretch();
                break;
            }
            case ButtonHolderFragment.eButtonSqueeze:{
                mModelController.onButtonSqeeze();
                break;
            }


            case ButtonHolderFragment.eButtonSingle:{
                mModelController.onButtonSingle();
                break;
            }
            case ButtonHolderFragment.eButtonAdd:{
                mModelController.onButtonAdd();
                break;
            }
            case ButtonHolderFragment.eButtonClear:{
                mModelController.onButtonClear();
                break;
            }
            case ButtonHolderFragment.eButtonAuto:{
                mModelController.onButtonAuto();
                break;
            }

        } // switch
    }

//    public void updatePlot(int[] data){
//        mPlotViewPresenter.updateSinglePlot(0, data);
//    }

    public void onFragmentInteraction(Uri uri){

        // do whatever you wish with the uri
    }

}
