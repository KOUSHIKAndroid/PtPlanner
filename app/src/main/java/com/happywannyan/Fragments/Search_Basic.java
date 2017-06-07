package com.happywannyan.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.CalenderActivity;
import com.happywannyan.Adapter.Adapter_petlist;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.PetService;
import com.happywannyan.R;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class Search_Basic extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static  final int CALL_CALENDER=12;
    SFNFTextView TXT_Loction,TXT_DateRange;
    private String StartDate="";
    private String EndDate="";
    LinearLayout LL_Calender;
    LinearLayout LL_PetServiceList;
    LinearLayout LL_defaultLabel;
    RecyclerView Rec_petlist;
    ImageView IMG_erase_location;
    ArrayList<PetService> ArrayPetService;
    Adapter_petlist adapter_petlist;
    Place place;
    JSONObject JSONFULLDATA;
    private OnFragmentInteractionListener mListener;

    public Search_Basic() {
    }

    // TODO: Rename and change types and number of parameters
    public static Search_Basic newInstance(String param1, String param2) {
        Search_Basic fragment = new Search_Basic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayPetService=new ArrayList<>();

        TXT_Loction=(SFNFTextView)view.findViewById(R.id.TXT_Loction);
        TXT_DateRange=(SFNFTextView)view.findViewById(R.id.TXT_DateRange);
        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).Menu_Drawer();
            }
        });
        LL_Calender=(LinearLayout)view.findViewById(R.id.LL_Calender);
        LL_PetServiceList=(LinearLayout)view.findViewById(R.id.LL_PetServiceList);
        LL_defaultLabel=(LinearLayout)view.findViewById(R.id.LL_defaultLabel);
        LL_PetServiceList.setVisibility(View.GONE);
        Rec_petlist=(RecyclerView)view.findViewById(R.id.Rec_petlist);
        Rec_petlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "parent_service", new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object=new JSONObject(Result);
                    JSONFULLDATA=object;
                    JSONArray PetService=object.getJSONArray("serviceCatList");
                    for(int i=0;i<PetService.length();i++){
                        JSONObject OBJE=PetService.getJSONObject(i);
                        PetService petService=new PetService();
                        petService.setId(OBJE.getString("id"));
                        petService.setName(OBJE.getString("name"));
                        petService.setDefault_image(OBJE.getString("default_image"));
                        petService.setSelected_image(OBJE.getString("selected_image"));
                        petService.setTooltip_name(OBJE.getString("tooltip_name"));
                        petService.setJsondata(OBJE);
                        petService.setTick_value(false);

                        ArrayPetService.add(petService);
                    }

//                    ArrayPetService.get(0).setTick_value(true);

                    adapter_petlist=new Adapter_petlist(Search_Basic.this,getActivity(),ArrayPetService);
                    Rec_petlist.setAdapter(adapter_petlist);
                }catch (JSONException e)
                {

                }
            }

            @Override
            public void OnError(String Error) {

            }
        });





        view.findViewById(R.id.RL_Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .build(getActivity());
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    Loger.MSG("@@ SERVICE"," "+e.getMessage());
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    Loger.MSG("@@ SERVICE"," 2 "+e.getMessage());
//                }
            }
        });

        LL_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivityForResult(new Intent(getActivity(), CalenderActivity.class),CALL_CALENDER);
            }
        });

        view.findViewById(R.id.IMG_erase_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXT_Loction.setText("");
                LL_PetServiceList.setVisibility(View.GONE);
                LL_defaultLabel.setVisibility(View.VISIBLE);
                for(PetService petService:ArrayPetService)
                    petService.setTick_value(false);
                ArrayPetService.get(0).setTick_value(true);
                adapter_petlist.notifyDataSetChanged();
            }
        });



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_basic, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {

                switch (requestCode)
                {

                    case CALL_CALENDER:
                        StartDate=data.getStringExtra("startdate");
                        EndDate=data.getStringExtra("enddate");

                        Loger.MSG("@@ START",StartDate);
                        Loger.MSG("@@ END",EndDate);
                        if(EndDate.length()>0)
                        TXT_DateRange.setText(StartDate +"  to   "+EndDate);
                        else
                            TXT_DateRange.setText(StartDate);

                        break;
                    case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                         place = PlacePicker.getPlace(getActivity(), data);
                        Loger.MSG("@@ PLACE",""+place.getLatLng());
                        Loger.MSG("@@ ViewPosrt","- "+place.getViewport().toString());

                        String Location = ""+ place.getName();
                        TXT_Loction.setText(Location);
                        LL_PetServiceList.setVisibility(View.VISIBLE);
                        LL_defaultLabel.setVisibility(View.GONE);
                        break;

                }

            }

    }

    public void GotoAdvancedSearched(JSONObject jsondata) {
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        JSONObject latalng=new JSONObject();
        try {
            latalng.put("lat",place.getLatLng().latitude);
            latalng.put("lng",place.getLatLng().longitude);

            JSONObject ViewPort=new JSONObject();
            ViewPort.put("southwest_LAT",place.getViewport().southwest.latitude);
            ViewPort.put("southwest_LNG",place.getViewport().southwest.longitude);

            ViewPort.put("northeast_LAT",place.getViewport().northeast.latitude);
            ViewPort.put("northeast_LNG",place.getViewport().northeast.longitude);

            jsondata.put("LocationName",place.getName());
            jsondata.put("latlng",latalng);
            jsondata.put("viewposrt",ViewPort);
            jsondata.put("Address",place.getAddress());
            jsondata.put("StartDate",StartDate);
            jsondata.put("EndDate",EndDate);
            jsondata.put("allPetDetails",JSONFULLDATA.getJSONArray("allPetDetails"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        fragmentTransaction.add(R.id.Base_fargment_layout,Advanced_search.newInstance(jsondata.toString(),null));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    public interface OnFragmentInteractionListener {

    }
}
