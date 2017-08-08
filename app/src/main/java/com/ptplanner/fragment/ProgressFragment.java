package com.ptplanner.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ptplanner.LandScreenActivity;
import com.ptplanner.PreviewUploadImageActivity;
import com.ptplanner.ProgressGraphView;
import com.ptplanner.R;
import com.ptplanner.customviews.HelveticaSemiBold;
import com.ptplanner.customviews.HelveticaSemiBoldLight;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.GraphClientAllDataType;
import com.ptplanner.datatype.GraphClientDetailsDataType;
import com.ptplanner.datatype.GraphClientGoalImages;
import com.ptplanner.datatype.GraphClientImagesDataType;
import com.ptplanner.datatype.ZoomCurrentImageDataType;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.ImageFilePath;
import com.ptplanner.helper.Trns;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgressFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    protected GoogleApiClient mGoogleApiClient;
    File finalFile = null;
    private GoogleApiClient client;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_GALLERY = 2;
    View fView;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, llGrphdetailsList;
    RelativeLayout llMessagebutton;
    LinearLayout allGraph;
    ImageView imgThumb, currentPicture, goalPicture;
    HelveticaSemiBold first_name;
    HelveticaSemiBoldLight last_name, current_picture_date, age_years, height_of_client, weight_of_client;
    ProgressBar progBar;
    ScrollView scrollView;
    RelativeLayout uploadCurrentImg, uploadGoalImg;
    String exception = "", urlResponse = "", exceptionImg = "", urlResponseImg = "",
            exceptionGraph = "", urlResponseGraph = "", exceptionGraphDetails = "", getUrlResponseGraphDetails = "";
    GraphClientDetailsDataType graphClientDetailsDataType;
    GraphClientImagesDataType graphClientImagesDataType;
    GraphClientGoalImages graphClientGoalImages;
    LinkedList<GraphClientImagesDataType> graphClientImagesDataTypeLinkedList;
    GraphClientAllDataType graphClientAllDataType;
    LinkedList<GraphClientAllDataType> graphClientAllDataTypeLinkedList;
    ConnectionDetector connectionDetector;
    LayoutInflater inflator;
    Dialog dialogChooser;
    LinearLayout llGallery, llCamera, llCancel;
    // --- For Camera and Gallery ---
    String Current_PATH = "", imgTYPE = "";
    // --- End ---
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    LinearLayout calendarButton, appointmentButton;
    RelativeLayout messageButton;
    TextView txtMSGCount, txtCal, txtApnt, txtPrg, txtMsg;
    ImageView imgCal, imgApnt, imgPrg, imgMsg;

    String saveString;
    SharedPreferences userId;
    ProgressBar progressBar2, progressBar3;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                return;
            } else {
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //////////////////////////////////////////////////////////////////////////

        calendarButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        appointmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        messageButton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);
        ///////////////////////////////////////////////////////////////////////////

        fView = inflater.inflate(R.layout.frag_progress, container, false);

        ////////////////////////////////////////////////
        String languageToLoad = AppConfig.LANGUAGE;
        Locale mLocale = new Locale(languageToLoad);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        this.fView = inflater.inflate(R.layout.frag_progress, container, false);
        /////////////////////////////////////////////////////

        buildGoogleApiClient();
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();

        connectionDetector = new ConnectionDetector(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();

        inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        allGraph = (LinearLayout) fView.findViewById(R.id.all_graph);
        allGraph.setEnabled(false);
        allGraph.setClickable(false);

        progBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        scrollView = (ScrollView) fView.findViewById(R.id.scrl_body);
        progBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);

        imgThumb = (ImageView) fView.findViewById(R.id.image_thumb);
        first_name = (HelveticaSemiBold) fView.findViewById(R.id.first_name);
        last_name = (HelveticaSemiBoldLight) fView.findViewById(R.id.last_name);
        age_years = (HelveticaSemiBoldLight) fView.findViewById(R.id.age_years);
        height_of_client = (HelveticaSemiBoldLight) fView.findViewById(R.id.height_of_client);
        weight_of_client = (HelveticaSemiBoldLight) fView.findViewById(R.id.weight_of_client);

        currentPicture = (ImageView) fView.findViewById(R.id.current_picture);
        goalPicture = (ImageView) fView.findViewById(R.id.goal_picture);
        current_picture_date = (HelveticaSemiBoldLight) fView.findViewById(R.id.current_picture_date);

        llGrphdetailsList = (LinearLayout) fView.findViewById(R.id.Grph_list_details);

        uploadCurrentImg = (RelativeLayout) fView.findViewById(R.id.upload_current_img);
        uploadGoalImg = (RelativeLayout) fView.findViewById(R.id.upload_goal_img);
        progressBar2 = (ProgressBar) fView.findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) fView.findViewById(R.id.progressBar2);

        dialogChooser = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogChooser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogChooser.getWindow().setGravity(Gravity.BOTTOM);
        dialogChooser.setContentView(R.layout.dialog_camera_selection);
        dialogChooser.setCanceledOnTouchOutside(true);

        llGallery = (LinearLayout) dialogChooser.findViewById(R.id.ll_gallery);
        llCamera = (LinearLayout) dialogChooser.findViewById(R.id.ll_camera);
        llCancel = (LinearLayout) dialogChooser.findViewById(R.id.ll_cancel);

        userId = this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        saveString = userId.getString("UserId", "");

        uploadCurrentImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialogChooser.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                imgTYPE = "currentImg";
            }
        });
        uploadGoalImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialogChooser.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                imgTYPE = "goalImg";
            }
        });

        llGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTION_TAKE_GALLERY);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTION_TAKE_GALLERY);
                }

                dialogChooser.dismiss();
            }
        });

        llCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogChooser.dismiss();

                finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(finalFile));
                startActivityForResult(takePictureIntent, 2000);
            }
        });

        llCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.dismiss();
                imgTYPE = "";
            }
        });

        allGraph.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                fragmentTransaction = fragmentManager.beginTransaction();
                AllGraphFragment allGraphFragment = new AllGraphFragment();
                fragmentTransaction.replace(R.id.fragment_container, allGraphFragment);
                int count = fragmentManager.getBackStackEntryCount();
                fragmentTransaction.addToBackStack(String.valueOf(count));
                fragmentTransaction.commit();
            }
        });

        currentPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                LandScreenActivity land = (LandScreenActivity) getActivity();
                ArrayList<ZoomCurrentImageDataType> list = new ArrayList<ZoomCurrentImageDataType>();
                for (int i = 0; i < graphClientImagesDataTypeLinkedList.size(); i++) {

                    ZoomCurrentImageDataType zoomCurrentImageDataType = new ZoomCurrentImageDataType(
                            graphClientImagesDataTypeLinkedList.get(i).getImage_link(),
                            graphClientImagesDataTypeLinkedList.get(i).getUploaded_date()
                    );

                    list.add(zoomCurrentImageDataType);

                }

                Collections.reverse(list);
                land.Show_FullScreen_ViewPager(land, list);
            }
        });

        goalPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LandScreenActivity land = (LandScreenActivity) getActivity();
                ArrayList<ZoomCurrentImageDataType> list = new ArrayList<ZoomCurrentImageDataType>();
                ZoomCurrentImageDataType zoomCurrentImageDataType = new ZoomCurrentImageDataType(
                        graphClientGoalImages.getImage(),
                        graphClientGoalImages.getUploaded_date()
                );
                list.add(zoomCurrentImageDataType);
                land.Show_FullScreen_ViewPager(land, list);
            }
        });

        if (connectionDetector.isConnectingToInternet()) {
            getClientDetails();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        llCalenderButton = (LinearLayout) getActivity().findViewById(R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(R.id.messagebutton);

        txtCal = (TextView) getActivity().findViewById(R.id.txt_cal);
        txtApnt = (TextView) getActivity().findViewById(R.id.txt_apnt);
        txtPrg = (TextView) getActivity().findViewById(R.id.txt_prg);
        txtMsg = (TextView) getActivity().findViewById(R.id.txt_msg);

        imgCal = (ImageView) getActivity().findViewById(R.id.img_cal);
        imgApnt = (ImageView) getActivity().findViewById(R.id.img_apnt);
        imgPrg = (ImageView) getActivity().findViewById(R.id.img_prg);
        imgMsg = (ImageView) getActivity().findViewById(R.id.img_msg);
        txtCal.setTextColor(Color.parseColor("#A4A4A5"));
        txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
        txtPrg.setTextColor(Color.parseColor("#22A7F0"));
        txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
        imgCal.setBackgroundResource(R.drawable.cal);
        imgApnt.setBackgroundResource(R.drawable.apnt);
        imgPrg.setBackgroundResource(R.drawable.prgclick2);
        imgMsg.setBackgroundResource(R.drawable.msg);
        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(false);
        llMessagebutton.setClickable(true);

        mGoogleApiClient.connect();

        return fView;
    }

    //---------Permission

    @Override
    public void onConnected(Bundle bundle) {



    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(finalFile));
                    startActivityForResult(takePictureIntent, 2000);

                } else {
//                    Toast.makeText(getActivity(), "Unable to get images.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW,
//                "ProgressFragment Page",
//                Uri.parse("http://host/path"),
//                Uri.parse("android-app://com.ptplanner/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW,
//                "ProgressFragment Page",
//                Uri.parse("http://host/path"),
//                Uri.parse("android-app://com.ptplanner/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Current_PATH = "";
        if (ACTION_TAKE_GALLERY == requestCode && data != null) {
            try {
                if (resultCode == getActivity().RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    try {
                        Current_PATH = ImageFilePath.getPath(getActivity(), selectedImage);
//                        Log.i("Image File Path : ", "" + Current_PATH);

                        if (!Current_PATH.equals("")) {
                            Intent intent = new Intent(getActivity(), PreviewUploadImageActivity.class);
                            intent.putExtra("IMG", Current_PATH);
                            intent.putExtra("imgType", imgTYPE);
                            startActivity(intent);
                        } else {
//                            Toast.makeText(getActivity(), "Current_PATH : " + Current_PATH, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
//                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        Current_PATH = "";
                    }
                }
            } catch (Exception e) {
//                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                Current_PATH = "";
            }
        } else if (finalFile != null) {

            Current_PATH = finalFile.toString();
//            Log.d("CAMERA IMAGE", Current_PATH);

            Intent intent = new Intent(getActivity(), PreviewUploadImageActivity.class);
            intent.putExtra("IMG", Current_PATH);
            intent.putExtra("imgType", imgTYPE);
            startActivity(intent);
        }
    }

    // *******************

    public void getClientDetails() {

        AsyncTask<Void, Void, Void> clientDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                ///////////////////////////////////////////////
                messageButton.setClickable(false);
                messageButton.setEnabled(false);
                calendarButton.setClickable(false);
                calendarButton.setEnabled(false);
                appointmentButton.setClickable(false);
                appointmentButton.setEnabled(false);

                ///////////////////////////////////////////////////
                progBar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_client_details?client_id=" + saveString)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    Log.i("jOBJ",""+jOBJ);

                    graphClientDetailsDataType = new GraphClientDetailsDataType(
                            jOBJ.getString("id"),
                            jOBJ.getString("user_type"),
                            jOBJ.getString("name"),
                            jOBJ.getString("image"),
                            jOBJ.getString("email"),
                            jOBJ.getString("address"),
                            jOBJ.getString("company"),
                            jOBJ.getString("work_address"),
                            jOBJ.getString("billing_address"),
                            jOBJ.getString("phone"),
                            jOBJ.getString("about"),
                            jOBJ.getString("date_of_birth"),
                            jOBJ.getString("height"),
                            jOBJ.getString("weight"),
                            jOBJ.getString("fat")
                    );

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", AppConfig.HOST + "app_control/get_client_details?client_id=" + saveString);
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                ///////////////////////////////////////////////
                messageButton.setClickable(true);
                messageButton.setEnabled(true);
                calendarButton.setClickable(true);
                calendarButton.setEnabled(true);
                appointmentButton.setClickable(true);
                appointmentButton.setEnabled(true);
                allGraph.setEnabled(true);
                allGraph.setClickable(true);

                ///////////////////////////////////////////////////
                if (exception.equals("") & isAdded()) {
                    Picasso.with(getActivity())
                            .load(graphClientDetailsDataType.getImage()).placeholder(R.drawable.no_image_available_placeholdder)
                            .transform(new Trns())
                            .fit()
                            .centerCrop()
                            .into(imgThumb);
//                    Log.i("Progress Name : ", graphClientDetailsDataType.getName());
                    String[] name = graphClientDetailsDataType.getName().split(" ");
                    try {
                        first_name.setText(name[0]);
                    } catch (Exception eFname) {
                        Log.i("eFname : ", eFname.toString());
                    }
                    try {
                        last_name.setText(name[1]);
                    } catch (Exception eLname) {
                        Log.i("eLname : ", eLname.toString());
                    }

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    String Birth_day = graphClientDetailsDataType.getDate_of_birth();
                    String BirthYear = Birth_day.substring(0, 4);
                    if (BirthYear.equals("0000")) {
                        age_years.setText("0");
                    } else {
                        int dob = Integer.parseInt(BirthYear);
                        int diff = year - dob;
                        age_years.setText(String.valueOf(diff));
                    }

                    height_of_client.setText(graphClientDetailsDataType.getWeight());
                    weight_of_client.setText(graphClientDetailsDataType.getHeight());

                    getClientImg();
                } else {
                    Log.d("Exception : ", exception);
                }
            }

        };
        clientDetails.execute();

    }

    public void getClientImg() {

        AsyncTask<Void, Void, Void> clientImg = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionImg = "";
                    urlResponseImg = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/get_client_images?client_id=" +
                                    saveString)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponseImg = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponseImg);
                    Log.i("jOBJ",""+jOBJ);

                    graphClientImagesDataTypeLinkedList = new LinkedList<GraphClientImagesDataType>();
                    try {
                        JSONArray jsonArrayCurrentImg = jOBJ.getJSONArray("current_images");
                        JSONArray jsonArrayGoalImg = jOBJ.getJSONArray("goal_image");
                        for (int i = 0; i < jsonArrayCurrentImg.length(); i++) {
                            JSONObject jsonObject = jsonArrayCurrentImg.getJSONObject(i);
                            graphClientImagesDataType = new GraphClientImagesDataType(
                                    jsonObject.getString("uploaded_date"),
                                    jsonObject.getString("image_thumbnail"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("id"));
                            graphClientImagesDataTypeLinkedList.add(graphClientImagesDataType);
                        }

                        JSONObject jsonObj = jsonArrayGoalImg.getJSONObject(0);
                        graphClientGoalImages = new GraphClientGoalImages(
                                jsonObj.getString("id"),
                                jsonObj.getString("uploaded_date"),
                                jsonObj.getString("image_thumbnail"),
                                jsonObj.getString("image"));
                    } catch (Exception e) {
                        try{
                            JSONArray jsonArrayCurrentImgC = jOBJ.getJSONArray("current_image");
                            JSONArray jsonArrayGoalImgC = jOBJ.getJSONArray("goal_image");
                            for (int i = 0; i < jsonArrayCurrentImgC.length(); i++) {
                                JSONObject jsonObject = jsonArrayCurrentImgC.getJSONObject(i);
                                graphClientImagesDataType = new GraphClientImagesDataType(
                                        jsonObject.getString("uploaded_date"),
                                        "",
                                        jsonObject.getString("image"),
                                        "");
                                graphClientImagesDataTypeLinkedList.add(graphClientImagesDataType);
                            }

                            JSONObject jsonObj = jsonArrayGoalImgC.getJSONObject(0);
                            graphClientGoalImages = new GraphClientGoalImages(
                                    "",
                                    jsonObj.getString("uploaded_date"),
                                    "",
                                    jsonObj.getString("image")
                            );
                        } catch (Exception eew) {
                            JSONArray jsonArrayCurrentImgC = jOBJ.getJSONArray("current_image");
                            JSONArray jsonArrayGoalImgC = jOBJ.getJSONArray("goal_image");
                            for (int i = 0; i < jsonArrayCurrentImgC.length(); i++) {
                                JSONObject jsonObject = jsonArrayCurrentImgC.getJSONObject(i);
                                graphClientImagesDataType = new GraphClientImagesDataType(
                                        "",
                                        "",
                                        jsonObject.getString("image"),
                                        "");
                                graphClientImagesDataTypeLinkedList.add(graphClientImagesDataType);
                            }

                            JSONObject jsonObj = jsonArrayGoalImgC.getJSONObject(0);
                            graphClientGoalImages = new GraphClientGoalImages(
                                    "",
                                    "",
                                    "",
                                    jsonObj.getString("image")
                            );
                        }

                    }
                } catch (Exception e) {
                    exceptionImg = e.toString();
                }
                Log.d("URLSURAJ", AppConfig.HOST + "app_control/get_client_images?client_id=" + saveString);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exceptionImg.equals("")) {
                    Picasso.with(getActivity())
                            .load(graphClientImagesDataTypeLinkedList.get(0).getImage_link().toString())
                            .fit()
                            .centerCrop()
                            .into(currentPicture, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar2.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    progressBar3.setVisibility(View.GONE);
                                    Picasso.with(getActivity())
                                            .load(R.drawable.no_progress_images)
                                            .fit()
                                            .centerCrop()
                                            .into(goalPicture);
                                }
                            });
                    Picasso.with(getActivity())
                            .load(graphClientGoalImages.getImage())
                            .fit()
                            .centerCrop()
                            .into(goalPicture, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar3.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    progressBar3.setVisibility(View.GONE);
                                    Picasso.with(getActivity())
                                            .load(R.drawable.no_progress_images)
                                            .fit()
                                            .centerCrop()
                                            .into(goalPicture);
                                }
                            });
                    try {
                        String current_show_month = graphClientImagesDataTypeLinkedList.get(0).getUploaded_date().substring(5, 7);
                        String current_show_date = graphClientImagesDataTypeLinkedList.get(0).getUploaded_date().substring(8, 10);
                        switch (current_show_month) {
                            case "01":
                                current_show_month = "jan";
                                break;
                            case "02":
                                current_show_month = "feb";
                                break;
                            case "03":
                                current_show_month = "mar";
                                break;
                            case "04":
                                current_show_month = "apr";
                                break;
                            case "05":
                                current_show_month = "may";
                                break;
                            case "06":
                                current_show_month = "jun";
                                break;
                            case "07":
                                current_show_month = "july";
                                break;
                            case "08":
                                current_show_month = "aug";
                                break;
                            case "09":
                                current_show_month = "sept";
                                break;
                            case "10":
                                current_show_month = "oct";
                                break;
                            case "11":
                                current_show_month = "nov";
                                break;
                            case "12":
                                current_show_month = "dec";
                                break;
                        }
                        //current_picture_date.setText("Nuvarande," + " " + current_show_date + " " + current_show_month);
                        current_picture_date.setText("Nuvarande");
                    } catch (Exception ex) {
                        Log.i("BackEnd :", ex.toString());
                        current_picture_date.setText("Nuvarande");
                    }

                    getClientGraph();
                } else {
                    Log.d("Exception : ", exceptionImg);
                    //  Toast.makeText(getActivity(), "Server not responding for image...." + exceptionImg, Toast.LENGTH_LONG).show();
                }
            }

        };
        clientImg.execute();

    }

    public void getClientGraph() {

        AsyncTask<Void, Void, Void> clientGraph = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionGraph = "";
                    urlResponseGraph = "";


                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/all_graphs?client_id=" +
                                    saveString)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponseGraph = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponseGraph);
                    Log.i("jOBJ",""+jOBJ);


                    JSONArray jsonArray = jOBJ.getJSONArray("all_graphs");
                    graphClientAllDataTypeLinkedList = new LinkedList<GraphClientAllDataType>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        JSONArray points = jsonObj.getJSONArray("points");
                        graphClientAllDataType = new GraphClientAllDataType(
                                jsonObj.getString("id"),
                                jsonObj.getString("goal"),
                                points.getJSONObject(0).getString("x_axis_point"),
                                jsonObj.getString("measure_unit"),
                                jsonObj.getString("graph_for"),
                                jsonObj.getString("graph_type"));
                        graphClientAllDataTypeLinkedList.add(graphClientAllDataType);

                    }

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("!! GRAPH URL", AppConfig.HOST + "app_control/all_graphs?client_id=" + saveString);
                } catch (Exception e) {
                    exceptionGraph = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if (exceptionGraph.equals("") & isAdded()) {
                    for (int i = 0; i < graphClientAllDataTypeLinkedList.size(); i++) { //ttt
                        final GraphClientAllDataType temp_one = graphClientAllDataTypeLinkedList.get(i);
                        View view = inflator.inflate(R.layout.prgrs_grph_det_list, null);
                        TitilliumRegular text_type = (TitilliumRegular) view.findViewById(R.id.type_progrss_1);
                        TitilliumSemiBold goal_progress = (TitilliumSemiBold) view.findViewById(R.id.goal_progress_1);
                        TitilliumRegular goal_progress_measure = (TitilliumRegular) view.findViewById(R.id.goal_progress_measure_1);
                        TitilliumSemiBold goal_progress_date = (TitilliumSemiBold) view.findViewById(R.id.goal_progress_date1);
                        text_type.setText(temp_one.getGraph_for());
                        int pos = 0;
                        String w8 = temp_one.getY_axis_point();
                        for (int j = 0; j < w8.length(); j++) {
                            if (w8.charAt(j) == '.') {
                                pos = j;
                            }
                        }
                        String wt1 = w8.substring(0, pos);
                        try {
                            goal_progress.setText("" + temp_one.getY_axis_point().split("\\.")[0]);
                        } catch (Exception ee) {
                            goal_progress.setText("" + temp_one.getY_axis_point());
                        }
                        goal_progress_measure.setText(" " + temp_one.getMeasure_unit().toLowerCase());

                        try {
                            DateFormat originalFormatToday = new SimpleDateFormat("yyyy/dd/MM");
                            DateFormat targetFormatToday = new SimpleDateFormat("yyyy-MM-dd");
                            Date todayDate = null;
                            try {
                                todayDate = originalFormatToday.parse(
                                        temp_one.getX_axis_point()
                                );
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            goal_progress_date.setText(targetFormatToday.format(todayDate));
                        } catch (Exception e) {
//                            Log.i("Tilbaka : ", " Dead : " + e.toString());
                        }


                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Toast.makeText(getActivity(), "GRAPH ID" + v.getTag().toString(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), ProgressGraphView.class);
                                intent.putExtra("GRAPH_ID", v.getTag().toString());
                                startActivity(intent);
                            }
                        });

                        view.setTag(graphClientAllDataTypeLinkedList.get(i).getGraph_id());
                        llGrphdetailsList.addView(view);
                    }
                } else {
//                    Log.d("Exception : ", exceptionGraph);
                    //Toast.makeText(getActivity(), "Server not responding for graph...." + exceptionGraph, Toast.LENGTH_LONG).show();
                }
            }

        };
        clientGraph.execute();

    }

}