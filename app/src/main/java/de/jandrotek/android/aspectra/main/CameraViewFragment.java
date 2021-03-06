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
package de.jandrotek.android.aspectra.main;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.ImageProcessing;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Here comes old co=de from MainFragment, to show CameraLiveView
 */
@SuppressWarnings("deprecation")
public class CameraViewFragment extends Fragment {
    /// constants
    private static final String TAG = "CameraViewFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static int mParam2;

    public void setDeviceOrientation(int deviceOrientation) {
        if (mCamPreview != null) {
            mCamPreview.setDeviceOrientation(deviceOrientation);
        }
        CameraViewFragment.mDeviceOrientation = deviceOrientation;
    }

    private static int mDeviceOrientation;

//    private boolean mFlagConfigStarted = false;

//    private OnFragmentInteractionListener mListener;

    /// Model's members, vars
    private ImageProcessing mImageProcessing;
    private int mStartPercentHX = 0;
    private int mEndPercentHX = 100;
    private int mStartPercentVY = 44;
    //    private int mEndPercentVY = 55;
    private int mScanAreaWidth = 2;

    /// camera-preview's members
    private CameraPreview mCamPreview; //class

    private int mPreviewWidthX;
    private int mPreviewHeightY;

    // camera,  shot dimensions\
    @SuppressWarnings("deprecation")
    private Camera mCamera;
    private int mNumberOfCameras;
    private int mCurrentCamera;  // Camera ID currently chosen
    private int mCameraCurrentlyLocked;  // Camera ID that's actually acquired

    // The first rear facing camera
    private int mDefaultCameraId;

    //    private FrameLayout mOverlayView;
    private FrameLayout mFramePreview;
    public ConfigLinesView mConfigLinesView; // will be called from activity

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nParam2 Parameter 2.
     * @return A new instance of fragment CameraViewFragment.
     */
    // TODO: Rename and change types and number of parameters
        public static CameraViewFragment newInstance(int nParam2) {
        CameraViewFragment fragment = new CameraViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, nParam2);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraViewFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }

//        mImageProcessing = new ImageProcessing();
        // Find the total number of cameras available
        //noinspection deprecation,deprecation
        mNumberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the rear-facing ("default") camera
        @SuppressWarnings("deprecation") Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            //noinspection deprecation,deprecation
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCurrentCamera = mDefaultCameraId = i;
            }
        }
        setHasOptionsMenu(mNumberOfCameras > 1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Add an up arrow to the "home" button, indicating that the button will go "up"
        // one activity in the app's Activity heirarchy.
        // Calls to getActionBar() aren't guaranteed to return the ActionBar when called
        // from within the Fragment's onCreate method, because the Window's decor hasn't been
        // initialized yet.  Either call for the ActionBar reference in Activity.onCreate()
        // (after the setContentView(...) call), or in the Fragment's onActivityCreated method.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ver new
        View rootView = inflater.inflate(R.layout.fragment_camera_view, container, false);

        mConfigLinesView = new ConfigLinesView(this.getActivity());
        mConfigLinesView = (ConfigLinesView) rootView.findViewById(R.id.flConfigOverlay);

        mCamPreview = new CameraPreview(this.getActivity(), mParam2);

        if(mParam2 == AspectraGlobals.ACT_ITEM_VIEW_CONFIG){
            //mCamPreview = new CameraPreview(this.getActivity(), BaseActivity.ACT_ITEM_VIEW_CONFIG);

            mPreviewWidthX = AspectraGlobals.mPreviewWidthX;
            mPreviewHeightY = AspectraGlobals.mPreviewHeightY;
//            mConfigLinesView.setPreviewDimensions(mPreviewWidthX, mPreviewHeightY);
        }

        mFramePreview = (FrameLayout) rootView.findViewById(R.id.liveViewFrame);
        // for testing
        mCamPreview.setVisibility(View.INVISIBLE);


        // if possible update preview size from FlavorSettings


        FrameLayout mConfigContainer = (FrameLayout) rootView.findViewById(R.id.extLineConfigContainer);
        mConfigContainer.setVisibility(View.VISIBLE);

        mFramePreview.addView(mCamPreview);

        mConfigLinesView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN: {

                        if (mParam2 == AspectraGlobals.ACT_ITEM_LIVE_VIEW) { // if we are in LiveView

                            cameraProcessingShouldRun(false);
                            //create intent and call ConfigActivity
                            Intent intentConfig = new Intent(getActivity(), ViewConfigActivity.class);

                            startActivity(intentConfig);
                            // after BackButton we want back here
                            getActivity().finish();
                        } else if (mParam2 == AspectraGlobals.ACT_ITEM_VIEW_CONFIG) {
                            //create intent and call LiveViewActivity
                            Intent intentLiveView = new Intent(getActivity(), LiveViewActivity.class);

                            startActivity(intentLiveView);
                            getActivity().finish();
                            cameraProcessingShouldRun(true);

                        }
                    }
                }
                return true; //processed
            }

        });
        cameraProcessingShouldRun(true);
        mCamPreview.setDeviceOrientation(mDeviceOrientation);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Use mCurrentCamera to select the camera desired to safely restore
        // the fragment after the camera has been changed
        try {
            mCamera = Camera.open(mCurrentCamera);
            mCameraCurrentlyLocked = mCurrentCamera;
            mCamPreview.setCamera(mCamera);
            if (mImageProcessing == null) {

                mImageProcessing = ImageProcessing.getInstance();
            }
            mCamPreview.setProcessing(mImageProcessing);
            cameraProcessingShouldRun(true);
            mCamPreview.setDeviceOrientation(mDeviceOrientation);
            mConfigLinesView.initializeLines();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), R.string.camera_exception, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            // trying to stop exploding...
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamPreview.setCamera(null);
            mCamera = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void cameraProcessingShouldRun(boolean flag){
        if(mCamPreview != null) {
            mCamPreview.setProcessingShouldRun(flag);
        }
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public void updateBorderInConfigView(float startPercentX, float endPercentX, float startPercentY, float deltaLinesY) {

        if (mConfigLinesView != null) {
            mConfigLinesView.setPercent(startPercentX, endPercentX, startPercentY, deltaLinesY);
        }
    }

    public void updateOrientationInConfigView(boolean _SpectrumOrientationLandscape) {

        if (mConfigLinesView != null) {
            mConfigLinesView.setSpectrumOrientationLandscape(_SpectrumOrientationLandscape);
        }
    }

    public void setImageProcessing(ImageProcessing imageProcessing) {
        mImageProcessing = imageProcessing;
    }

//    public void setCameraOrientation(int cameraOrientation){
//        mConfigLinesView.setCameraOrientation(cameraOrientation);
//    }


    // not used ??
//    public void updateBorderPercents() {
//
//        //TODO: move to activity
//        if (mImageProcessing != null) {
//            mImageProcessing.setStartPercentX(mStartPercentHX);
//            mImageProcessing.setEndPercentX(mEndPercentHX);
//            mImageProcessing.setStartPercentY(mStartPercentVY);
////            mImageProcessing.setEndPercentY(mEndPercentVY);
//            mImageProcessing.setScanAreaWidth(mScanAreaWidth);
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "mStartPercentHX = " + mStartPercentHX + ", mEndPercentHX = " + mEndPercentHX);
//                Log.d(TAG, "mStartPercentVY = " + mStartPercentVY + ", mStartPercentVY = " + mStartPercentVY);
//            }
//        }
//        if (mConfigLinesView != null) {
//            mConfigLinesView.setPercent((float) mStartPercentHX, (float) mEndPercentHX, (float) mStartPercentVY, (float) mScanAreaWidth);
//        }
//    }


    // getter setters
    public void setStartPercentHX(int startPercentHX) {
        mStartPercentHX = startPercentHX;
    }

    public void setEndPercentHX(int endPercentHX) {
        mEndPercentHX = endPercentHX;
    }

    public void setStartPercentVY(int startPercentVY) {
        mStartPercentVY = startPercentVY;
    }

    public int getPreviewWidthX() {
        return mPreviewWidthX;
    }

    public int getPreviewHeightY() {
        return mPreviewHeightY;
    }

    public void setScanAreaWidth(int prefsScanAreaWidth) {
        mScanAreaWidth = prefsScanAreaWidth;
    }

}
