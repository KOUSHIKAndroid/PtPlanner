package com.ptplanner.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ptplanner.LandScreenActivity;
import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumBold;
import com.ptplanner.customviews.TitilliumLight;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.datatype.TimeSlotsDataType;
import com.ptplanner.fragment.AppointmantFragment;
import com.ptplanner.fragment.BookAppointmentFragment;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
;import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookAppointAdapter extends ArrayAdapter<TimeSlotsDataType> {

    Context context;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    ArrayList<TimeSlotsDataType> trainerBookingDetailsDataTypeArrayList;
    ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList;
    ConnectionDetector connectionDetector;
    String exception = "", urlResponse = "";
    ProgressDialog progressDialog;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Dialog bookingConfirmDialog;
    TitilliumLight txtTrainerName, txtTrainerTime, txtTrainerDate;
    LinearLayout llCancel, llBook;
    BookAppointmentFragment bookAppointmentFragment;
    String trainerName = "";
    String traineremail="";

    public BookAppointAdapter(Context context, int resource, ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList, String trainerName, String traineremail, BookAppointmentFragment bookAppointmentFragment) {
        super(context, resource, timeSlotsDataTypeArrayList);
        this.context = context;
        this.timeSlotsDataTypeArrayList = timeSlotsDataTypeArrayList;
        this.trainerName = trainerName;
        this.traineremail=traineremail;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        connectionDetector = new ConnectionDetector(context);
        this.bookAppointmentFragment=bookAppointmentFragment;
        fragmentManager = ((FragmentActivity) this.context)
                .getSupportFragmentManager();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.book_app_list, parent, false);

            holder.mainContainer = (RelativeLayout) convertView.findViewById(R.id.con);
            holder.llBookingStatus = (LinearLayout) convertView.findViewById(R.id.ll_booking_status);
            holder.txtTimeing = (TitilliumRegular) convertView.findViewById(R.id.txt_timeing);
            holder.txtApt = (TitilliumBold) convertView.findViewById(R.id.txt_apt);
            holder.txtStatus = (TitilliumSemiBold) convertView.findViewById(R.id.txt_status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTimeing.setText(timeSlotsDataTypeArrayList.get(position).getSlot_start()
                + " - " + timeSlotsDataTypeArrayList.get(position).getSlot_end());
//        holder.txtApt.setText(timeSlotsDataTypeArrayList.get(position).getCounter());
        holder.txtApt.setText("");

        if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("NB")) {
            holder.mainContainer.setVisibility(View.VISIBLE);
            holder.llBookingStatus.setVisibility(View.VISIBLE);
            holder.llBookingStatus.setBackgroundResource(R.drawable.book_button);
            holder.txtStatus.setText(context.getResources().getString(R.string.Book));
            holder.txtStatus.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("Ex")) {
            holder.mainContainer.setVisibility(View.GONE);
            holder.llBookingStatus.setVisibility(View.GONE);
        }
        else if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("B")) {
            holder.mainContainer.setVisibility(View.VISIBLE);
            holder.llBookingStatus.setVisibility(View.VISIBLE);
            holder.llBookingStatus.setBackgroundResource(R.drawable.booked_button);
            holder.txtStatus.setText(context.getResources().getString(R.string.Booked));
            holder.txtStatus.setTextColor(Color.parseColor("#22A7F0"));
        }

        holder.llBookingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("NB")) {
                        bookingConfirmDialog = new Dialog(context);
                        bookingConfirmDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
                        bookingConfirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        bookingConfirmDialog.setContentView(R.layout.dialog_booking_confirm);
                        bookingConfirmDialog.setCancelable(true);
                        txtTrainerName = (TitilliumLight) bookingConfirmDialog.findViewById(R.id.txt_trainer_name);
                        txtTrainerTime = (TitilliumLight) bookingConfirmDialog.findViewById(R.id.txt_trainer_time);
                        txtTrainerDate = (TitilliumLight) bookingConfirmDialog.findViewById(R.id.txt_trainer_date);
                        llCancel = (LinearLayout) bookingConfirmDialog.findViewById(R.id.ll_cancel);
                        llBook = (LinearLayout) bookingConfirmDialog.findViewById(R.id.ll_done);

                        bookingConfirmDialog.show();

                        if(trainerName.equals("")){
                            txtTrainerName.setText(traineremail);
                        }
                        else{
                            txtTrainerName.setText(trainerName);
                        }
                        txtTrainerTime.setText(timeSlotsDataTypeArrayList.get(position).getSlot_start()
                                + " - " + timeSlotsDataTypeArrayList.get(position).getSlot_end());

                        SimpleDateFormat givenFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                        Date convertedDate = new Date();
                        try {
                            convertedDate = givenFormat.parse(timeSlotsDataTypeArrayList.get(position).getBooking_date());
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            Log.i("Dialog Exc B: ", e.toString());
                        }
                        txtTrainerDate.setText(sdfDate.format(convertedDate));

                        llBook.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bookingConfirmDialog.dismiss();


                                addBooking(timeSlotsDataTypeArrayList.get(position).getTrainer_id(),
                                        timeSlotsDataTypeArrayList.get(position).getBooking_date(),
                                        timeSlotsDataTypeArrayList.get(position).getSlot_start(),
                                        timeSlotsDataTypeArrayList.get(position).getSlot_end(),
                                        position);

                                /////////////////////update by suraj shaw/////////////////
                                if (AppConfig.appointmentArrayList.size() <= 0) {
                                    Log.i("appointmentArrayList0",""+AppConfig.appointmentArrayList.size());
                                    bookAppointmentFragment.showCalPopup.getAllEvent();
                                } else {
                                    Log.i("appointmentArrayList1",""+AppConfig.appointmentArrayList.size());
                                    AppConfig.appointmentArrayList.clear();
                                    AppConfig.programArrayList.clear();
                                    AppConfig.mealArrayList.clear();
                                    AppConfig.availableDateArrayList.clear();
                                    bookAppointmentFragment.showCalPopup.getAllEvent();
                                }
                                //////////////////////////////////////////////////////////
                            }
                        });
                        llCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bookingConfirmDialog.dismiss();
                            }
                        });


                    } else if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("B")) {
                        Bundle bundle = new Bundle();
                        //Toast.makeText(context, "B iD : " + timeSlotsDataTypeArrayList.get(position).getBooking_id(), Toast.LENGTH_LONG).show();
                        bundle.putString("BOOKID", timeSlotsDataTypeArrayList.get(position).getBooking_id());
                        bundle.putString("DateChange", timeSlotsDataTypeArrayList.get(position).getBooking_date());

                        Log.d("@@KOUSHIK","-> "+timeSlotsDataTypeArrayList.get(position).getBooking_date());
                        bundle.putString("PAGE", "BOOKINGADAPTER");
                        fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmantFragment app_fragment = new AppointmantFragment();
                        app_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_container, app_fragment);
//                        int count = fragmentManager.getBackStackEntryCount();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        return convertView;
    }


    public class ViewHolder {
        TitilliumRegular txtTimeing;
        TitilliumBold txtApt;
        TitilliumSemiBold txtStatus;
        LinearLayout llBookingStatus;
        RelativeLayout mainContainer;
    }

    public void addBooking(final String trainerId, final String date, final String slotStart, final String slotEnd, final int position) {
        Log.i("@@add_booking_url",""+AppConfig.HOST + "app_control/add_booking?trainer_id=" + trainerId
                + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                + "&booking_date=" + date
                + "&slot_start=" + slotStart
                + "&slot_end=" + slotEnd);

        AsyncTask<Void, Void, Void> booking = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

                SharedPreferences loginPreferences = ((LandScreenActivity)context).getSharedPreferences("Login", Context.MODE_PRIVATE);
                AppConfig.loginDatatype = new LoginDataType(
                        loginPreferences.getString("UserId", ""),
                        loginPreferences.getString("Username", ""),
                        loginPreferences.getString("Password", ""));
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Uppdatera...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(AppConfig.HOST + "app_control/add_booking?trainer_id=" + trainerId
                                    + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                                    + "&booking_date=" + date
                                    + "&slot_start=" + slotStart
                                    + "&slot_end=" + slotEnd)
                            .build();

                    Response response = client.newCall(request).execute();
                    urlResponse = response.body().string();
                    JSONObject jOBJ = new JSONObject(urlResponse);
//                    Log.i("@@KOUSHIK_jOBJ",""+jOBJ);
                    Log.i("@@KOUSHIK_String",""+urlResponse);
                    Log.i("add_booking_url",""+AppConfig.HOST + "app_control/add_booking?trainer_id=" + trainerId
                            + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&booking_date=" + date
                            + "&slot_start=" + slotStart
                            + "&slot_end=" + slotEnd);

                } catch (JSONException e) {
                    exception = e.toString();
                }catch (Exception e)
                {
                    exception = e.toString();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                 Log.i("GET EXCEPTION : ", exception);
                if (exception.equals("")) {
                    timeSlotsDataTypeArrayList.get(position).setStatusDependent("B");
                    notifyDataSetChanged();
                } else {
                    //Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

        };
        booking.execute();

    }

}