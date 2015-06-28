/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.libplotspectra;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

// lib ver 3.
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GraphView.GraphViewData;
//import com.jjoe64.graphview.GraphViewSeries;
//import com.jjoe64.graphview.LineGraphView;
//lib ver 4.
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import de.jandrotek.android.aspectra.core.AspectraGlobals;

//import android.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlotViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlotViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlotViewFragment extends Fragment
    implements         View.OnCreateContextMenuListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLOT_DATA_SIZE = 1920;
    private int realPlotDataSize = PLOT_DATA_SIZE;

    // TODO: Rename and change types of parameters
//    private String mParam1;
    private int mParam2;

    private GraphView mGraphView;
    private int[] mPlotIntValues;
    //ver 3
//    private GraphViewData[] mData;
//    private GraphViewSeries mDataSeries;
//    private GraphViewSeries.GraphViewSeriesStyle mGraphStyle;
//    private GraphViewData[] realData;
    // ver 4
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private DataPoint[] realData;


    private OnFragmentInteractionListener mListener;
//    private boolean mFlagSavinggStarted = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlotViewFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static PlotViewFragment newInstance(String param1, String param2) {
        public static PlotViewFragment newInstance(int param2) {
        PlotViewFragment fragment = new PlotViewFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
            args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlotViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
        //ver 3
        //realData = new GraphViewData[realPlotDataSize];
//        realData = new GraphViewData[PLOT_DATA_SIZE];
//        mData = generateDemoData();
        // ver 4

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plot_view, container, false);

        mGraphView = new GraphView(getActivity());
//        mGraphView = new GraphView(getActivity(), "");
        // add data

//        mGraphStyle = new GraphViewSeries.GraphViewSeriesStyle();
//        mGraphStyle.thickness = 1;
//        mData = generateDemoData();
        mSeries1 = new LineGraphSeries<DataPoint>(generateDemoData());
        mGraphView.addSeries(mSeries1);
//        mGraphView.getGraphViewStyle().setTextSize(20);
//        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
//        mGraphView.getGraphViewStyle().setNumVerticalLabels(4);

//        GraphViewSeries.GraphViewSeriesStyle geSstyle = mDataSeries.getStyle();
//        mGraphView.setViewPort(0, realPlotDataSize);
        mGraphView.getViewport().setMinX(0);
        mGraphView.getViewport().setMaxX(realPlotDataSize);
        registerForContextMenu(mGraphView);

        mGraphView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN) {

                    if (!AspectraGlobals.mSavePlotInFile) {
                        AspectraGlobals.mSavePlotInFile = true;
                    }
                }
                return true; //processed
            }

        });

        // alone is working, but with onClickListener not
        mGraphView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        //TODO: optional - activate scaling / zooming
        // both modi will be handled with Touch-view helper class, not only in viewer
        // in liveView is disabled, first in AnalyzeActivity
        if (mParam2 == AspectraGlobals.ACT_ITEM_ANALYZE) {
            //mGraphView.setScrollable(true);
            //mGraphView.setScalable(true);
        }
        else if(mParam2 == AspectraGlobals.ACT_ITEM_LIVE_VIEW) {

        }
        FrameLayout mFrameLayout = (FrameLayout)rootView.findViewById(R.id.flPlotView);
        mFrameLayout.addView(mGraphView);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

//        mFlagSavinggStarted = false;
    }

    //TODO will not working
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.plot_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean result = performActions(item);
        if (!result) {
            result = super.onContextItemSelected(item);
        }
        return result;
    }

    private boolean performActions(MenuItem item){
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
// ver 4
    public void showPlot(int[] data, int length){
        if(mSeries1 != null) {
            realPlotDataSize = length;
            DataPoint[] graphdata = generateData(data, length);// here explode

            mGraphView.getViewport().setXAxisBoundsManual(true);
            mGraphView.getViewport().setMinX(0);
            mGraphView.getViewport().setMaxX(realPlotDataSize);
            mSeries1.resetData(graphdata);
        }
    }

    // ver 3
//    public void showPlot(int[] data, int length){
//        if(mDataSeries != null) {
//            realPlotDataSize = length;
//            GraphViewData[] graphdata = generateData(data, length);// here explode
//            mGraphView.setViewPort(0, realPlotDataSize);
//            mDataSeries.resetData(graphdata);
//        }
//    }

    //ver 4
    private DataPoint[] generateData(int[] data, int length) {
        // TODO:  variables as private in fragment, for speed up, GarbageCollection not needed
        for (int i=0; i<length; i++) {

//            realData[i] = new GraphViewData(i, data[i]);
            DataPoint v = new DataPoint(i, data[i]);
            realData[i] = v;
        }
        //TODO: check in plot act length, and add needed data only for that length

        //for(int i = length; i < realPlotDataSize ; i++){
        for(int i = length; i < PLOT_DATA_SIZE ; i++){
            DataPoint v = new DataPoint(i, 0);
            realData[i] = v;
        }
        return realData;
    }

// ver 3
    //    private GraphViewData[] generateData(int[] data, int length) {
////  variables as private in fragment, for speed up, GarbageCollection not needed
//            for (int i=0; i<length; i++) {
//
//                realData[i] = new GraphViewData(i, data[i]);
//            }
//            //TODO: check in plot act length, and add needed data only for that length
//
//            //for(int i = length; i < realPlotDataSize ; i++){
//                for(int i = length; i < PLOT_DATA_SIZE ; i++){
//                realData[i] = new GraphViewData(i, 0);
//            }
//        return realData;
//    }
//

    // ver 4
    private DataPoint[] generateDemoData(){
        DataPoint[] demoData;
        mPlotIntValues = new int[PLOT_DATA_SIZE];
        for (int i = 0; i < PLOT_DATA_SIZE/2; i++)
            mPlotIntValues[i] = i;
        for (int i = PLOT_DATA_SIZE/2; i < PLOT_DATA_SIZE; i++)
            mPlotIntValues[i] = PLOT_DATA_SIZE - i;

        demoData = new DataPoint[PLOT_DATA_SIZE];
        for (int i=0; i<PLOT_DATA_SIZE; i++) {

            demoData[i] = new DataPoint(i, mPlotIntValues[i]);
        }
        return demoData;
    }

    //ver 3
//    private GraphViewData[] generateDemoData(){
//        GraphViewData[] demoData;
//        mPlotIntValues = new int[PLOT_DATA_SIZE];
//        for (int i = 0; i < PLOT_DATA_SIZE/2; i++)
//            mPlotIntValues[i] = i;
//        for (int i = PLOT_DATA_SIZE/2; i < PLOT_DATA_SIZE; i++)
//            mPlotIntValues[i] = PLOT_DATA_SIZE - i;
//
//        demoData = new GraphViewData[PLOT_DATA_SIZE];
//        for (int i=0; i<PLOT_DATA_SIZE; i++) {
//
//            demoData[i] = new GraphViewData(i, mPlotIntValues[i]);
//        }
//        return demoData;
//    }
}


