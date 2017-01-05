package org.kll.app.mamapper.FrontActivity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.kll.app.mamapper.DialogsInterface.NetworkSettingDialog;
import org.kll.app.mamapper.Location.GPSTracker;
import org.kll.app.mamapper.Manipulation.EditBank;
import org.kll.app.mamapper.Manipulation.EditHospital;
import org.kll.app.mamapper.Manipulation.EditSchool;
import org.kll.app.mamapper.Model.OverpassQueryResult;
import org.kll.app.mamapper.Overpass.OverpassServiceProvider;
import org.kll.app.mamapper.R;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity implements MapEventsReceiver, MapView.OnFirstLayoutListener {

    MapView map;
    IMapController mapController;
    KmlDocument mKmlDocument;
    Context mContext;

    Drawable markerIncomplete, markerComplete;

    GPSTracker gps;

    double latitude;
    double longitude;

    OverpassQueryResult queryResult;
    String mQuery, mGet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mContext = this;

        // Request permissions to support Android Marshmallow and above devices
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }

        gps = new GPSTracker(MainActivity.this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        mGet = getIntent().getStringExtra("send");
        switch (mGet) {
            case "school":
                markerIncomplete = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_school_incomplete, null);
                markerComplete = ResourcesCompat.getDrawable(getResources(),R.drawable.marker_school_complete, null);
                mQuery = "[out:json][timeout:25];(node[\"amenity\"=\"school\"](28.175382163920304,83.92026901245117,28.265077617741067,84.04300689697266););way[\"amenity\"=\"school\"](28.175382163920304,83.92026901245117,28.265077617741067,84.04300689697266);out body;>;out skel qt;";
                break;
            case "bank":
                markerIncomplete = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_bank_incomplete, null);
                markerComplete = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_bank_complete, null);
                mQuery = "[out:json][timeout:25];(node[\"amenity\"=\"bank\"](28.175382163920304,83.92026901245117,28.265077617741067,84.04300689697266););way[\"amenity\"=\"bank\"](28.175382163920304,83.92026901245117,28.265077617741067,84.04300689697266);out body;>;out skel qt;";
                break;
            case "hospital":
                markerIncomplete = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_hospital_incomplete, null);
                markerComplete = ResourcesCompat.getDrawable(getResources(),R.drawable.marker_hospital_complete,null);
                mQuery = "[out:json][timeout:25];(node[\"amenity\"=\"hospital\"](28.175382163920304,83.92026901245117,28.265077617741067,84.04300689697266););way[\"amenity\"=\"hospital\"](28.175382163920304,83.92026901245117,28.265077617741067,84.04300689697266);out body;>;out skel qt;";
                break;
            default:
                markerComplete = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_poi_default, null);
                break;
        }


        //defining maps and geolocation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        //GeoPoint startPoint = new GeoPoint(longitude, latitude);
        GeoPoint startPoint = new GeoPoint(28.2156, 83.9971);

        mapController = map.getController();
        mapController.setZoom(14);
        mapController.setCenter(startPoint);


        if (!isNetworkAvailable()) {
            showNetworkSettingAlert();
        }else{
            getMarkersAndPopulateMapView();
        }
    }


    private void getMarkersAndPopulateMapView(){

        try {
            queryResult = OverpassServiceProvider.get().interpreter(mQuery).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Marker Clustering
        RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(this);

        //Customizing the clusters design
        Drawable clusterIconD = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_poi_cluster, null);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        poiMarkers.setIcon(clusterIcon);
        poiMarkers.getTextPaint().setTextSize(12 * getResources().getDisplayMetrics().density);
        poiMarkers.mAnchorV = Marker.ANCHOR_BOTTOM;
        poiMarkers.mTextAnchorU = 0.70f;
        poiMarkers.mTextAnchorV = 0.27f;

        //end of 11.1
        map.getOverlays().add(poiMarkers);
        if (queryResult.elements != null) {
            for (OverpassQueryResult.Element element : queryResult.elements) {
                Marker poiMarker = new Marker(map);
                if (element.tags.amenity != null) {
                    if (element.tags.name != null) {
                        poiMarker.setTitle(element.tags.name);
                    } else {
                        poiMarker.setTitle(" ");
                    }
                    GeoPoint elementLocation = null;
                    if (element.type.equals("way")) {
                        for (OverpassQueryResult.Element innerElement : queryResult.elements) {
                            if (innerElement.id == element.nodes.get(1)) {
                                elementLocation = new GeoPoint(innerElement.lat, innerElement.lon);
                            }
                        }

                    } else {
                        elementLocation = new GeoPoint(element.lat, element.lon);
                    }
                    poiMarker.setPosition(elementLocation);
                    if (getCompletionStatus(element).equals("incomplete")){
                        poiMarker.setIcon(markerIncomplete);
                        poiMarker.setSnippet("Some data missing, click the button to complete it");
                    }else{
                        poiMarker.setIcon(markerComplete);
                        poiMarker.setSnippet("Data is complete, click the button to review it");
                    }
                    poiMarker.setInfoWindow(new CustomInfoWindow(map));
                    poiMarker.setRelatedObject(element);
                    poiMarkers.add(poiMarker);
                }
            }
        }

        // Handling Map events
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay); //inserted at the "bottom" of all overlays

    }

    private String getCompletionStatus(OverpassQueryResult.Element element){
        String status = "incomplete";
        OverpassQueryResult.Element.Tags t = element.tags;
        if (t.amenity.equals("hospital")){
            Log.wtf("Hospital:", t.name + ":" + t.operatorType + ":" + t.facilityIcu + ":" + t.facilityNicu + ":"
            + t.facilityOT + ":" + t.facilityventilator + ":" + t.facilityXray + ":" + t.emergency + ":" + t.emergencyService + ":"
            + t.capacityBeds + ":" + t.personnelCount + ":" + t.phone + ":" + t.contactEmail + ":" + t.website + ":" + t.nepaliName);
            if (t.name != null && t.operatorType != null && t.facilityIcu != null && t.facilityOT != null
            && t.facilityventilator != null && t.facilityXray != null && t.emergency != null && t.capacityBeds != null
                    && t.facilityNicu != null && t.personnelCount != null
                    && t.phone != null && t.contactEmail != null && t.website != null && t.nepaliName != null ){
                status = "complete";
            }
        } else if (t.amenity.equals("school")){
            Log.wtf("School:", t.name + ":" + t.operator + ":" + t.personnelCount + ":" + t.studentCount + ":" + t.phone + ":" + t.website );
            if (t.name != null && t.operator != null && t.personnelCount != null && t.studentCount != null && t.phone != null && t.website != null){
                status = "complete";
            }
        } else if (t.amenity.equals("bank")) {
            if (t.name != null && t.operator != null && t.phone != null && t.website != null && t.contactEmail != null && t.openingHours != null && t.atm != null) {
                status = "complete";
            }
        }

        return status;
    }

    private void showNetworkSettingAlert() {
        Intent intent = new Intent(getApplicationContext(), NetworkSettingDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //--- Stuff for setting the mapview on a box at startup:
    BoundingBox mInitialBoundingBox = null;

    void setInitialViewOn(BoundingBox bb) {
        if (map.getScreenRect(null).height() == 0) {
            mInitialBoundingBox = bb;
            map.addOnFirstLayoutListener(this);
        } else
            map.zoomToBoundingBox(bb, false);
    }

    @Override
    public void onFirstLayout(View v, int left, int top, int right, int bottom) {
        if (mInitialBoundingBox != null)
            map.zoomToBoundingBox(mInitialBoundingBox, false);
    }


    //Customizing the bubble behaviour
    class CustomInfoWindow extends MarkerInfoWindow {

        OverpassQueryResult.Element mSelectedPoi;

        public CustomInfoWindow(MapView mapView) {
            super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);
            Button btn = (Button) (mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    switch (mGet){
                        case "school":
                            Intent school = new Intent(mContext, EditSchool.class);
                            EventBus.getDefault().postSticky(mSelectedPoi);
                            mContext.startActivity(school);
                            break;
                        case "bank":
                            Intent bank = new Intent(mContext, EditBank.class);
                            EventBus.getDefault().postSticky(mSelectedPoi);
                            mContext.startActivity(bank);
                            break;
                        case "hospital":
                            Intent hospital = new Intent(mContext, EditHospital.class);
                            EventBus.getDefault().postSticky(mSelectedPoi);
                            mContext.startActivity(hospital);
                            break;
                    }
                }
            });
        }

        @Override
        public void onOpen(Object item) {
            super.onOpen(item);
            mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
            Marker marker = (Marker) item;
            mSelectedPoi = (OverpassQueryResult.Element) marker.getRelatedObject();

/*          // put thumbnail image in bubble, fetching the thumbnail in background:
            if (mSelectedPoi.mThumbnailPath != null) {
                ImageView imageView = (ImageView) mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_image);
                mSelectedPoi.fetchThumbnailOnThread(imageView);
            }*/
        }
    }

    //Customizing the clusters design - and beyond
    class CirclesGridMarkerClusterer extends RadiusMarkerClusterer {

        public CirclesGridMarkerClusterer(Context ctx) {
            super(ctx);
        }

        @Override
        public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
            Marker m = new Marker(mapView);
            m.setPosition(cluster.getPosition());
            m.setInfoWindow(null);
            m.setAnchor(0.5f, 0.5f);
            int radius = (int) Math.sqrt(cluster.getSize() * 3);
            radius = Math.max(radius, 10);
            radius = Math.min(radius, 30);
            Bitmap finalIcon = Bitmap.createBitmap(radius * 2, radius * 2, mClusterIcon.getConfig());
            Canvas iconCanvas = new Canvas(finalIcon);
            Paint circlePaint = new Paint();
            if (cluster.getSize() < 20)
                circlePaint.setColor(Color.BLUE);
            else
                circlePaint.setColor(Color.RED);
            circlePaint.setAlpha(200);
            iconCanvas.drawCircle(radius, radius, radius, circlePaint);
            String text = "" + cluster.getSize();
            int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
            iconCanvas.drawText(text,
                    mTextAnchorU * finalIcon.getWidth(),
                    mTextAnchorV * finalIcon.getHeight() - textHeight / 2,
                    mTextPaint);
            m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));
            return m;
        }
    }

    //Handling Map events
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //Toast.makeText(this, "Tap on (" + p.getLatitude() + "," + p.getLongitude() + ")", Toast.LENGTH_SHORT).show();
        //InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    float mGroundOverlayBearing = 0.0f;

    @Override
    public boolean longPressHelper(GeoPoint p) {
        map.invalidate();
        return true;
    }

    // START PERMISSION CHECK
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "osmdroid permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nLocation to show user location.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nStorage access to store map tiles.";
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } // else: We already have permissions, so handle as normal
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (location && storage) {
                    // All Permissions Granted
                    Toast.makeText(MainActivity.this, "All permissions granted", Toast.LENGTH_SHORT).show();
                } else if (location) {
                    Toast.makeText(this, "Storage permission is required to store map tiles to reduce data usage and for offline usage.", Toast.LENGTH_LONG).show();
                } else if (storage) {
                    Toast.makeText(this, "Location permission is required to show the user's location on map.", Toast.LENGTH_LONG).show();
                } else { // !location && !storage case
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Storage permission is required to store map tiles to reduce data usage and for offline usage." +
                            "\nLocation permission is required to show the user's location on map.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // END PERMISSION CHECK


}