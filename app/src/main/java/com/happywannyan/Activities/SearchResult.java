package com.happywannyan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.happywannyan.Adapter.SearchPets_Adapter;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.SearchData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener {

    public static final String SEARCHKEY="@Data";
    private static final String TAG = "@@ SRCH_RESULT";
    GoogleMap Map;
    RecyclerView recycler_view;
    ArrayList<SearchData> ListARRY;
    JSONObject SearchKeys;
    LatLngBounds PresetLatBounds;
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        appLoader=new AppLoader(this);

        MapsInitializer.initialize(SearchResult.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(SearchResult.this);
        try {
            SearchKeys=new JSONObject(getIntent().getStringExtra(SEARCHKEY));
            ((SFNFTextView)findViewById(R.id.PAGE_Titile)).setText(SearchKeys.getString("LocationName"));
            Loger.MSG("@@ SEARCH KEY",SearchKeys.toString());

        }catch (JSONException e){}
        catch (Exception e){}


        recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        ListARRY=new ArrayList<>();

        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        appLoader.Show();

        ArrayList<APIPOSTDATA> PostData=new ArrayList<>();
        APIPOSTDATA apipostdata=new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues("30");
        PostData.add(apipostdata);

        apipostdata=new APIPOSTDATA();
        apipostdata.setPARAMS("langid");
        apipostdata.setValues("en");
        PostData.add(apipostdata);

        apipostdata=new APIPOSTDATA();
        apipostdata.setPARAMS("per_page");
        apipostdata.setValues("10");
        PostData.add(apipostdata);


        try {
            apipostdata=new APIPOSTDATA();
            apipostdata.setPARAMS("search_location");
            apipostdata.setValues(SearchKeys.getString("LocationName"));
            PostData.add(apipostdata);

            for(int i=0;i<SearchKeys.getJSONArray("keyinfo").length();i++)
            {
                JSONObject object=SearchKeys.getJSONArray("keyinfo").getJSONObject(i);
                apipostdata=new APIPOSTDATA();
                apipostdata.setPARAMS(object.getString("name"));
                apipostdata.setValues(object.getString("value"));
                PostData.add(apipostdata);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        new JSONPerser().API_FOR_POST(AppContsnat.BASEURL + "search_setter", PostData, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                try {

                    JSONObject object=new JSONObject(Result);
                    JSONArray ARRA=object.getJSONArray("results");

                    for(int i=0;i<ARRA.length();i++)
                    {
                        JSONObject jjj=ARRA.getJSONObject(i);
                        SearchData searchData=new SearchData();
                        searchData.setSearcItem(jjj);

                        ListARRY.add(searchData);
                    }
                    recycler_view.setAdapter(new SearchPets_Adapter(SearchResult.this,ListARRY));

                }catch (Exception e)
                {

                }

            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                try {
                JSONObject jsonObject=    new JSONObject(Response);
                    if(jsonObject.getInt("next_data")==0 && jsonObject.getInt("start_form")==0){
                        new MYAlert(SearchResult.this).AlertOnly(getResources().getString(R.string.app_name), Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }

                }catch (Exception e)
                {

                }



            }

            @Override
            public void OnError(String Error) {

                appLoader.Dismiss();
            }
        });




        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                findViewById(R.id.INC_LIST).setVisibility(View.GONE);
                findViewById(R.id.map).setVisibility(View.VISIBLE);
                findViewById(R.id.list).setVisibility(View.VISIBLE);
                try {
                    LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                    for (int i = 0; i < ListARRY.size(); i++) {


                        try {
                            LatLng latLng=new LatLng(Double.parseDouble(ListARRY.get(i).getSearcItem().getString("lat")),Double.parseDouble(ListARRY.get(i).getSearcItem().getString("long")));
                            MarkerOptions markerOptions = new MarkerOptions().
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon)).
                                    position(latLng).zIndex(0.0f);
//                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

                            Marker lomarakar = Map.addMarker(markerOptions);
                            builder2.include(markerOptions.getPosition());
                            lomarakar.setTag(i);
                        }catch (Exception ee){
                            Loger.Error(TAG,"3 "+ee.getMessage());
                        }






                    }

                    LatLngBounds bounds = builder2.build();
                    int padding = 100; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding,100,100);
                    Map.animateCamera(cu);
                }catch (Exception e)
                {
                    Loger.Error(TAG," "+e.getMessage());
                }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                findViewById(R.id.map).setVisibility(View.GONE);
                findViewById(R.id.INC_LIST).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onCameraIdle() {
        Loger.MSG("@@ Zoom"," "+Map.getCameraPosition().zoom);

    }

    @Override
    public void onCameraMove() {
        Loger.MSG("@@ ViewPost"," "+Map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);

        if(Map.getCameraPosition().zoom>5 && Map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude<
                PresetLatBounds.northeast.latitude && Map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude> PresetLatBounds.southwest.latitude
                )
        {
            Map.getUiSettings().setScrollGesturesEnabled(true);
        }else {
//            Map.getUiSettings().setScrollGesturesEnabled(false);
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(PresetLatBounds.getCenter(),Map.getCameraPosition().zoom) );
        }


    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map=googleMap;
        Map.setOnCameraIdleListener(this);
        Map.setOnCameraMoveListener(this);
        Map.setMinZoomPreference((float) 4.99);
        Map.getUiSettings().setRotateGesturesEnabled(false);
        Map.getUiSettings().setScrollGesturesEnabled(false);



        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(25.341635885457396,129.0904974192381));
        builder.include(new LatLng(46.958814679811375,146.3319904357195));
         PresetLatBounds=builder.build();
        Map.setLatLngBoundsForCameraTarget(PresetLatBounds);
        Map.animateCamera(CameraUpdateFactory.newLatLngZoom(PresetLatBounds.getCenter(),5) );
//        int padding = 100; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(camrearset, padding);
//        Map.animateCamera(cu);

    }
}
