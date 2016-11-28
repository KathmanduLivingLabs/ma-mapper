package org.kll.app.meromapv1.FrontActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import org.kll.app.meromapv1.Location.GPSTracker;
import org.kll.app.meromapv1.R;
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
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerDragListener;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;


import java.util.ArrayList;

public class MainActivitys extends BaseActivity implements MapEventsReceiver, MapView.OnFirstLayoutListener {

    MapView map;
    KmlDocument mKmlDocument;


    String infoName;
    String infoDiscription;

    String select;


    GPSTracker gps;

    double latitude;
    double longitude;




    @Override protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //create a instance of SQLite database

        gps = new GPSTracker(MainActivitys.this);

        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }




        String get = getIntent().getStringExtra("send");
        if(get == "school")
        {
            select = "school";
        }
        else if(get == "bank")
        {
            select = "bank";
        }


        //defining maps and geolocation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(longitude, latitude);
        //GeoPoint startPoint = new GeoPoint(27.7360100,
        //        85.3355140);
        IMapController mapController = map.getController();
        mapController.setZoom(20);
        mapController.setCenter(startPoint);


        //Using Nominatim
        NominatimPOIProvider poiProvider = new NominatimPOIProvider("OsmNavigator/1.0");
        ArrayList<POI> pois = poiProvider.getPOICloseTo(startPoint, get, 150, 0.1);
        //or : ArrayList<POI> pois = poiProvider.getPOIAlong(road.getRouteLow(), "fuel", 50, 2.0);


        //FolderOverlay poiMarkers = new FolderOverlay(this);
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
        Drawable poiIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_poi_default, null);
        if (pois != null) {
            for (POI poi : pois) {
                Marker poiMarker = new Marker(map);
                poiMarker.setTitle(poi.mType);
                poiMarker.setSnippet(poi.mDescription);
                poiMarker.setPosition(poi.mLocation);
                poiMarker.setPosition(poi.mLocation);



                poiMarker.setIcon(poiIcon);
                if (poi.mThumbnail != null) {
                    poiMarker.setImage(new BitmapDrawable(getResources(), poi.mThumbnail));

                }


                System.out.println(poi.mDescription);

                poiMarker.setInfoWindow(new CustomInfoWindow(map));
                poiMarker.setRelatedObject(poi);
                poiMarkers.add(poiMarker);
            }
        }

       /* //12. Loading KML content
        mKmlDocument = new KmlDocument();
        //Get OpenStreetMap content as KML with Overpass API:
        OverpassAPIProvider overpassProvider = new OverpassAPIProvider();
        BoundingBox oBB = new BoundingBox(startPoint.getLatitude() + 0.25, startPoint.getLongitude() + 0.25,
                startPoint.getLatitude() - 0.25, startPoint.getLongitude() - 0.25);
        String oUrl = overpassProvider.urlForTagSearchKml("highway=speed_camera", oBB, 500, 30);
        boolean ok = overpassProvider.addInKmlFolder(mKmlDocument.mKmlRoot, oUrl);

        if (ok) {
            //13.1 Simple styling
            Drawable defaultMarker = ResourcesCompat.getDrawable(getResources(), R.drawable.marker_kml_point, null);
            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
            Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3.0f, 0x20AA1010);
            //13.2 Advanced styling with Styler
            KmlFeature.Styler styler = new MyKmlStyler(defaultStyle);

            FolderOverlay kmlOverlay = (FolderOverlay) mKmlDocument.mKmlRoot.buildOverlay(map, defaultStyle, styler, mKmlDocument);
            map.getOverlays().add(kmlOverlay);
            BoundingBox bb = mKmlDocument.mKmlRoot.getBoundingBox();
            if (bb != null) {
                //map.zoomToBoundingBox(bb, false); //=> not working in onCreate - this is a well-known osmdroid issue.
                //Workaround:
                setInitialViewOn(bb);
            }
        } else
            Toast.makeText(this, "Error when loading KML", Toast.LENGTH_SHORT).show();

        //14. Grab overlays in KML structure, save KML document locally
        if (mKmlDocument.mKmlRoot != null) {
            KmlFolder root = mKmlDocument.mKmlRoot;
            mKmlDocument.saveAsKML(mKmlDocument.getDefaultPathForAndroid("my_route.kml"));
            //15. Loading and saving of GeoJSON content
            mKmlDocument.saveAsGeoJSON(mKmlDocument.getDefaultPathForAndroid("my_route.json"));
        }
*/

        // Handling Map events
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay); //inserted at the "bottom" of all overlays
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

    //Using the Marker and Polyline overlays - advanced options
    class OnMarkerDragListenerDrawer implements OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;
        Polyline mPolyline;

        OnMarkerDragListenerDrawer() {
            mTrace = new ArrayList<GeoPoint>(100);
            mPolyline = new Polyline();
            mPolyline.setColor(0xAA0000FF);
            mPolyline.setWidth(2.0f);
            mPolyline.setGeodesic(true);
            map.getOverlays().add(mPolyline);
        }

        @Override public void onMarkerDrag(Marker marker) {
            //mTrace.add(marker.getPosition());
        }

        @Override public void onMarkerDragEnd(Marker marker) {
            mTrace.add(marker.getPosition());
            mPolyline.setPoints(mTrace);
            map.invalidate();
        }

        @Override public void onMarkerDragStart(Marker marker) {
            //mTrace.add(marker.getPosition());
        }
    }

    //Customizing the bubble behaviour
    class CustomInfoWindow extends MarkerInfoWindow {
        POI mSelectedPoi;

        public CustomInfoWindow(MapView mapView) {
            super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);
            Button btn = (Button) (mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mSelectedPoi.mUrl != null) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSelectedPoi.mUrl));
                        view.getContext().startActivity(myIntent);
                    } else {
                        Intent DetailInfo = new Intent(MainActivitys.this, org.kll.app.meromapv1.Manipulation.DetailInfo.class);
                        infoName = mSelectedPoi.mType;
                        infoDiscription = mSelectedPoi.mDescription;
                        DetailInfo.putExtra("DetailName", infoName);
                        DetailInfo.putExtra("DetailDisc", infoDiscription);
                        startActivity(DetailInfo);
                        Toast.makeText(view.getContext(), "button clicked: "+ mSelectedPoi.mType, Toast.LENGTH_LONG).show();



                    }
                }
            });
        }

        @Override
        public void onOpen(Object item) {
            super.onOpen(item);
            mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
            Marker marker = (Marker) item;
            mSelectedPoi = (POI) marker.getRelatedObject();

            // put thumbnail image in bubble, fetching the thumbnail in background:
            if (mSelectedPoi.mThumbnailPath != null) {
                ImageView imageView = (ImageView) mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_image);
                mSelectedPoi.fetchThumbnailOnThread(imageView);
            }
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

    //Loading KML content - Advanced styling with Styler
    class MyKmlStyler implements KmlFeature.Styler {
        Style mDefaultStyle;

        MyKmlStyler(Style defaultStyle) {
            mDefaultStyle = defaultStyle;
        }

        @Override
        public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
            //Custom styling:
            polyline.setColor(Color.GREEN);
            polyline.setWidth(Math.max(kmlLineString.mCoordinates.size() / 200.0f, 3.0f));
        }

        @Override
        public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
            //Keeping default styling:
            kmlPolygon.applyDefaultStyling(polygon, mDefaultStyle, kmlPlacemark, mKmlDocument, map);
        }

        @Override
        public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {
            //Keeping default styling:
            kmlTrack.applyDefaultStyling(polyline, mDefaultStyle, kmlPlacemark, mKmlDocument, map);
        }

        @Override
        public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {
            //Styling based on ExtendedData properties:
            if (kmlPlacemark.getExtendedData("maxspeed") != null)
                kmlPlacemark.mStyle = "maxspeed";
            kmlPoint.applyDefaultStyling(marker, mDefaultStyle, kmlPlacemark, mKmlDocument, map);
        }

        @Override
        public void onFeature(Overlay overlay, KmlFeature kmlFeature) {
            //If nothing to do, do nothing.
        }
    }

    //Handling Map events
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Toast.makeText(this, "Tap on (" + p.getLatitude() + "," + p.getLongitude() + ")", Toast.LENGTH_SHORT).show();
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    float mGroundOverlayBearing = 0.0f;

    @Override public boolean longPressHelper(GeoPoint p) {

        // Using Polygon, defined as a circle:
        Polygon circle = new Polygon();
        circle.setPoints(Polygon.pointsAsCircle(p, 2000.0));
        circle.setFillColor(0x12121212);
        circle.setStrokeColor(Color.RED);
        circle.setStrokeWidth(2);
        map.getOverlays().add(circle);
        circle.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
        circle.setTitle("Centered on " + p.getLatitude() + "," + p.getLongitude());

        //18. Using GroundOverlay
        GroundOverlay myGroundOverlay = new GroundOverlay();
        myGroundOverlay.setPosition(p);
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher, null);
        myGroundOverlay.setImage(d.mutate());
        myGroundOverlay.setDimensions(2000.0f);
        //myGroundOverlay.setTransparency(0.25f);
        myGroundOverlay.setBearing(mGroundOverlayBearing);
        mGroundOverlayBearing += 20.0f;
        map.getOverlays().add(myGroundOverlay);

        map.invalidate();
        return true;

    }



}