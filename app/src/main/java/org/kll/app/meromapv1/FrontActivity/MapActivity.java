package org.kll.app.meromapv1.FrontActivity;

/**
 * Created by Rahul Singh Maharjan on 10/24/16.
 * Project for Kathmandu Living Labs
 */


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

//for geocoding
import org.kll.app.meromapv1.R;

import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.map.MapDataDao;
import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Element;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;
import de.westnordost.osmapi.map.handler.OneElementTypeHandler;

import static junit.framework.Assert.assertTrue;



public class MapActivity extends BaseActivity {

    private MapView mapView, maplayout;
    private MapboxMap map;
    private LocationServices locationServices;

    LatLng LatLong_final;


    private int minMillisecondThresholdForLongClick = 800;
    private long startTimeForLongClick = 0;
    private float xScreenCoordinateForLongClick;
    private float yScreenCoordinateForLongClick;
    private float xtolerance  = 10;
    private float ytolerance = 10;
    private float xlow;
    private float xhigh;
    private float ylow;
    private float yhigh;





    private OsmConnection liveConnection;
    private static final double VALID_LAT = 51.7400243;
    private static final double VALID_LON = 0.2400123;


    TextView text;
    TableLayout tableLayout;


    private static final int PERMISSIONS_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*MapBox access Token start for accessing Mapbox*/
        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        //accessing the osm api






        /*
        Check if the GPS location is activated or Not
        */
        LocationManager mlocManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled) {
            showDialogGPS();
        }

        //Code Snippet for GPS location
        locationServices = LocationServices.getLocationServices(MapActivity.this);
        tableLayout = (TableLayout) findViewById(R.id.table);
        text = (TextView) findViewById(R.id.text);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                map = mapboxMap;

                enableGps();


               /* Projection projection = mapboxMap.getProjection();
                LatLng topLeft = projection.fromScreenLocation(new PointF(0,0));
                LatLng bottomRight = projection.fromScreenLocation(new PointF(
                        mapView.getWidth(), mapView.getHeight()
                ));
                String toplefts = topLeft.toString();
                Toast.makeText(MapActivity.this, toplefts, Toast.LENGTH_LONG).show();
*/


         /*       mapView.setOnTouchListener(new MapView.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int Xpos = (int) event.getX(); // get the x coordinate of touch position
                        int Ypos = (int) event.getY(); // get the y coordinate of the touch position
                        String Xposs = Float.toString(Xpos);
                        String Yposs = Float.toString(Ypos);


                        Projection projection = mapboxMap.getProjection();

                        //convert the x/y axis into latitude lng
                        LatLng topLeft = projection.fromScreenLocation(new PointF(Xpos, Ypos));
                        String toplefts = topLeft.toString();
                        LatLng bottomRight = projection.fromScreenLocation(new PointF(
                                mapView.getWidth(), mapView.getHeight()
                        ));

                        *//*OSM API java Implementation




                            OsmConnection osm = new OsmConnection("https://api.openstreetmap.org/api/0.6/",
                                    "meromap", null);

                            LatLon pos = new OsmLatLon(VALID_LAT, VALID_LON);
                            final BoundingBox boundingBox = new BoundingBox(27.333, 9.939, 23.580, 9.940);
                            CountMapDataHandler counter = new CountMapDataHandler();


                            Note myNote = new NotesDao(osm).create(pos, "My first note");
                            MapDataDao mapDao = new MapDataDao(osm);
                            mapDao.getMap(boundingBox, counter);

                            testDownloadMapIsReallyStreamed();



                         OSM API java Implementation
*//*



                            switch (event.getAction()) {
                                case MotionEvent.ACTION_UP:
                                    Log.i("TAG", "touched up");
                                    map.addMarker(new MarkerOptions().position(topLeft));
                                    Toast.makeText(MapActivity.this, toplefts, Toast.LENGTH_SHORT).show();
                                    break;
                            }


                        *//*Toast.makeText(MapActivity.this, Xposs , Toast.LENGTH_SHORT).show();
                       map.addMarker(new MarkerOptions().position(topLeft));
                String toplefts = topLeft.toString();
                Toast.makeText(MapActivity.this, toplefts, Toast.LENGTH_SHORT).show();*//*

                        return true;
                    }
                });*/



                //Enable user tracking to show the padding affect
                map.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
                map.getTrackingSettings().setDismissAllTrackingOnGesture(true); // if false we cannot move the map


                //Costomize the user location icon
                map.getMyLocationViewSettings().setPadding(0, 500, 0, 0);
                map.getMyLocationViewSettings().setForegroundTintColor(Color.parseColor("#56B881"));
                map.getMyLocationViewSettings().setAccuracyTintColor(Color.parseColor("#FBB03B"));




                //create the marker at the user location
                /*mapboxMap.addMarker(new MarkerViewOptions().
                        position(new LatLng(map.getMyLocation())).
                        title(getResources().getString(R.string.ktm)).
                        snippet("Hello Kathmandu"));*/

                //Restore the Map into full screen

                map.setOnMapClickListener(new MapboxMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        LayoutParams mapparams = mapView.getLayoutParams();
                        //  ViewGroup.LayoutParams params = uplayout.getLayoutParams();

                        //params.height = (int) getResources().getDimension(R.dimen.layout_change);
                        mapparams.height = (int) getResources().getDimension(R.dimen.custom_match_parent);
                        mapView.setLayoutParams(mapparams);
                    }
                });





                //Popup the dialog when the information is clicked


                /*map.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                    @Override
                    public boolean onInfoWindowClick( Marker marker) {

                        Intent intent = new Intent(MainActivity.this, test.class);
                        startActivity(intent);

                        LayoutParams mapparams = mapView.getLayoutParams();
                          //  ViewGroup.LayoutParams params = uplayout.getLayoutParams();
                         //params.height = (int) getResources().getDimension(R.dimen.layout_change);
                        //uplayout.setLayoutParams(params);

                            mapparams.height = (int) getResources().getDimension(R.dimen.layout_change);
                            mapView.setLayoutParams(mapparams);

                            return true;

                    }
                });*/



            }

        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //get the action from the MotionEvent: down, move, or up
        int actionType = ev.getAction();
        Projection projection;
        projection = map.getProjection();

        if (actionType == MotionEvent.ACTION_DOWN) {
            //user pressed the button down so let's initialize the main variables that we care about:
            // later on when the "Action Up" event fires, the "DownTime" should match the "startTimeForLongClick" that we set here
            // the coordinate on the screen should not change much during the long press
            startTimeForLongClick=ev.getEventTime();
            xScreenCoordinateForLongClick=ev.getX();
            yScreenCoordinateForLongClick=ev.getY();

        } else if (actionType == MotionEvent.ACTION_MOVE) {
            //For non-long press actions, the move action can happen a lot between ACTION_DOWN and ACTION_UP
            if (ev.getPointerCount()>1) {
                //easiest way to detect a multi-touch even is if the pointer count is greater than 1
                //next thing to look at is if the x and y coordinates of the person's finger change.
                startTimeForLongClick=0; //instead of a timer, just reset this class variable and in our ACTION_UP event, the DownTime value will not match and so we can reset.
            } else {
                //I know that I am getting to the same action as above, startTimeForLongClick=0, but I want the processor
                //to quickly skip over this step if it detects the pointer count > 1 above
                float xmove = ev.getX(); //where is their finger now?
                float ymove = ev.getY();
                //these next four values allow you set a tiny box around their finger in case
                //they don't perfectly keep their finger still on a long click.
                xlow = xScreenCoordinateForLongClick - xtolerance;
                xhigh= xScreenCoordinateForLongClick + xtolerance;
                ylow = yScreenCoordinateForLongClick - ytolerance;
                yhigh= yScreenCoordinateForLongClick + ytolerance;
                if ((xmove<xlow || xmove> xhigh) || (ymove<ylow || ymove> yhigh)){
                    //out of the range of an acceptable long press, reset the whole process
                    startTimeForLongClick=0;
                }
            }

        } else if (actionType == MotionEvent.ACTION_UP) {
            //determine if this was a long click:
            long eventTime = ev.getEventTime();
            long downTime = ev.getDownTime(); //this value will match the startTimeForLongClick variable as long as we didn't reset the startTimeForLongClick variable because we detected nonsense that invalidated a long press in the ACTION_MOVE block

            //make sure the start time for the original "down event" is the same as this event's "downTime"
            if (startTimeForLongClick==downTime){
                //see if the event time minus the start time is within the threshold
                if ((eventTime-startTimeForLongClick)>minMillisecondThresholdForLongClick){
                    //make sure we are at the same spot where we started the long click
                    float xup = ev.getX();
                    float yup = ev.getY();
                    //I don't want the overhead of a function call:
                    xlow = xScreenCoordinateForLongClick - xtolerance;
                    xhigh= xScreenCoordinateForLongClick + xtolerance;
                    ylow = yScreenCoordinateForLongClick - ytolerance;
                    yhigh= yScreenCoordinateForLongClick + ytolerance;
                    if ((xup>xlow && xup<xhigh) && (yup>ylow && yup<yhigh)){

                        //**** safe to process your code for an actual long press ****
                        //comment out these next rows after you confirm in logcat that the long press works
                        long totaltime=eventTime-startTimeForLongClick;
                        String strtotaltime=Long.toString(totaltime);
                        Log.d("long press detected: ", strtotaltime);



                        //convert the x/y axis into latitude lng
                        LatLng topLeft = projection.fromScreenLocation(new PointF(xup, yup));
                        LatLong_final = topLeft;
                        String toplefts = topLeft.toString();
                        /*LatLng bottomRight = projection.fromScreenLocation(new PointF(
                                mapView.getWidth(), mapView.getHeight()));*/
                        map.addMarker(new MarkerOptions().position(topLeft));
                        Toast.makeText(MapActivity.this, toplefts, Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }


        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }




    /**
     * Show a dialog to the user requesting that GPS be enabled
     */
    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        Button not_now = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button enable_now = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        not_now.setTextColor(Color.BLUE);
        enable_now.setTextColor(Color.BLUE);
    }

    private void enableGps()
    {
        //check for location permission
        if(!locationServices.areLocationPermissionsGranted())
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        }
        else
        {
            enableLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_LOCATION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                enableLocation();
            }
        }
    }

    private void enableLocation()
    {
        // if previous location
        Location lastLocation = locationServices.getLastLocation();
        if(lastLocation != null)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

        }

        locationServices.addLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(location != null)
                {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location),16));
                    locationServices.removeLocationListener(this);
                }
            }
        });

        //enable or disable the location layer on the map
        map.setMyLocationEnabled(true);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_main: return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //osm api implementation

    private class CountMapDataHandler implements MapDataHandler
    {
        public int bounds;
        public int nodes;
        public int ways;
        public int relations;

        @Override
        public void handle(BoundingBox bbox)
        {
            bounds++;
        }

        @Override
        public void handle(Node node)
        {
            nodes++;
        }

        @Override
        public void handle(Way way)
        {
            ways++;
        }

        @Override
        public void handle(Relation relation)
        {
            relations++;
        }
    }

    public void testDownloadMapIsReallyStreamed()
    {
        // should be >1MB of data
        final BoundingBox bigHamburg = new BoundingBox(53.585, 9.945, 53.59, 9.95);

        CheckFirstHandleCallHandler handler = new CheckFirstHandleCallHandler();

        long startTime = System.currentTimeMillis();
        new MapDataDao(liveConnection).getMap(bigHamburg, handler);
        long timeToFirstData = handler.firstCallTime - startTime;
        long totalTime = System.currentTimeMillis() - startTime;

        // a bit of a cheap test, but I had no better idea how to test this:

        assertTrue(totalTime *3/4 > timeToFirstData);
		/*
		 * = the first data can be processed at least after 3/4 of the total
		 * time of transmission. If the processing of the data would only be
		 * started after the whole data had been transferred, the
		 * 'timeToFirstData' would be much closer to 'totalTime' (than
		 * 'startTime')
		 */
    }
    private class CheckFirstHandleCallHandler extends OneElementTypeHandler<Element>
    {
        public long firstCallTime = -1;

        public CheckFirstHandleCallHandler()
        {
            super(Element.class);
        }

        @Override
        public void handleElement(Element element)
        {
            if(firstCallTime == -1)
                firstCallTime = System.currentTimeMillis();
        }

    }

}
