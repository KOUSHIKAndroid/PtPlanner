package com.ptplanner.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;
import com.ptplanner.datatype.TrainingPerticularExerciseSetsDatatype;
import com.ptplanner.fragment.TrainingFragment;
import com.ptplanner.helper.AppConfig;
import com.ptplanner.helper.ConnectionDetector;
import com.ptplanner.helper.OnOneOffClickListener;

import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrainingAdapter extends ArrayAdapter<TrainingPerticularExerciseSetsDatatype> {

    Context context;
    ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList;
    LayoutInflater inflator;
    ViewHolder holder;
    ConnectionDetector cd;
    String weightnew = "", urlResponseFinish = "", statusFinish = "";
    String trainingID, userProgramID, updatedSetReps = "", updatedSetKg = "";


    private  int PreVPosition=-1;
    TrainingFragment trainingFragment;

    public TrainingAdapter(TrainingFragment trainingFragment,Context context, int resource,
                           ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList,
                           String trainingID, String userProgramID) {
        super(context, resource, trainingPerticularExerciseSetsDatatypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.trainingFragment=trainingFragment;
        this.trainingPerticularExerciseSetsDatatypeArrayList = trainingPerticularExerciseSetsDatatypeArrayList;
        this.trainingID = trainingID;
        this.userProgramID = userProgramID;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        cd = new ConnectionDetector(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.training_item, parent, false);
            holder = new ViewHolder();

            holder.txtSet = (TitilliumSemiBold) convertView.findViewById(R.id.txt_set);
            holder.txtReps = (TitilliumSemiBold) convertView.findViewById(R.id.txt_reps);
            holder.etWeight = (EditText) convertView.findViewById(R.id.txt_weight);

            holder.llCheck = (LinearLayout) convertView.findViewById(R.id.ll_check);
            holder.llChange = (LinearLayout) convertView.findViewById(R.id.ll_change);

            holder.llSet = (LinearLayout) convertView.findViewById(R.id.set_ll);
            holder.llReps = (LinearLayout) convertView.findViewById(R.id.reps_ll);
            holder.llKG = (LinearLayout) convertView.findViewById(R.id.kg_ll);
            holder.txtRP = (TitilliumRegular) convertView.findViewById(R.id.rp);
            holder.txtKG = (TitilliumRegular) convertView.findViewById(R.id.kg);

            holder.main_container = (RelativeLayout) convertView.findViewById(R.id.main_container);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


//        Log.d("@@@REPS  :  ", updatedSetReps);

        if (trainingPerticularExerciseSetsDatatypeArrayList.get(position).getREPS().equalsIgnoreCase("0")&&
                trainingPerticularExerciseSetsDatatypeArrayList.get(position).getKg().equalsIgnoreCase("0")) {
            holder.main_container.setVisibility(View.GONE);
        } else {
            holder.main_container.setVisibility(View.VISIBLE);

            holder.txtSet.setText("Set " + (position + 1));
            holder.txtReps.setText(trainingPerticularExerciseSetsDatatypeArrayList.get(position).getREPS());
            holder.etWeight.setText(trainingPerticularExerciseSetsDatatypeArrayList.get(position).getKg());
            holder.etWeight.setId(position);

        }

        if (trainingPerticularExerciseSetsDatatypeArrayList.get(position).getIsEditable()) {
            holder.llCheck.setVisibility(View.GONE);
            holder.llChange.setVisibility(View.VISIBLE);

            if(trainingPerticularExerciseSetsDatatypeArrayList.get(position).getKg().equalsIgnoreCase("0"))
                holder.etWeight.setText(" ");


            holder.etWeight.setClickable(true);
            holder.etWeight.setEnabled(true);
            holder.etWeight.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(holder.etWeight, InputMethodManager.SHOW_IMPLICIT);
            holder.etWeight.setSelection(holder.etWeight.getText().length());
            holder.txtSet.setTextColor(Color.parseColor("#AEDFFB"));
            holder.txtReps.setTextColor(Color.parseColor("#AEDFFB"));
            holder.txtRP.setTextColor(Color.parseColor("#AEDFFB"));
            holder.txtKG.setTextColor(Color.parseColor("#FFFFFF"));
            holder.etWeight.setTextColor(Color.parseColor("#FFFFFF"));
            holder.llSet.setBackgroundColor(Color.parseColor("#26B9F6"));
            holder.llReps.setBackgroundColor(Color.parseColor("#26B9F6"));
            holder.llKG.setBackgroundColor(Color.parseColor("#00A5F4"));

        } else {

            holder.llCheck.setVisibility(View.VISIBLE);
            holder.llChange.setVisibility(View.GONE);
            holder.txtSet.setTextColor(Color.parseColor("#333333"));
            holder.txtReps.setTextColor(Color.parseColor("#22A7F0"));
            holder.txtRP.setTextColor(Color.parseColor("#333333"));
            holder.txtKG.setTextColor(Color.parseColor("#333333"));
            holder.etWeight.setTextColor(Color.parseColor("#22A7F0"));
            holder.llSet.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.llReps.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.llKG.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                weightnew=charSequence.toString();
                Log.d("@@@@",weightnew);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.llCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llCheck.setVisibility(View.GONE);
                holder.llChange.setVisibility(View.VISIBLE);
                holder.etWeight.setClickable(true);
                holder.etWeight.setEnabled(true);
                holder.etWeight.requestFocus();
                if(PreVPosition!=-1)
                    trainingPerticularExerciseSetsDatatypeArrayList.get(PreVPosition).setIsEditable(false);

                trainingPerticularExerciseSetsDatatypeArrayList.get(position).setIsEditable(true);
                PreVPosition=position;
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(holder.etWeight, InputMethodManager.SHOW_IMPLICIT);
                holder.etWeight.setSelection(holder.etWeight.getText().length());
                holder.txtSet.setTextColor(Color.parseColor("#AEDFFB"));
                holder.txtReps.setTextColor(Color.parseColor("#AEDFFB"));
                holder.txtRP.setTextColor(Color.parseColor("#AEDFFB"));
                holder.txtKG.setTextColor(Color.parseColor("#FFFFFF"));
                holder.etWeight.setTextColor(Color.parseColor("#FFFFFF"));
                holder.llSet.setBackgroundColor(Color.parseColor("#26B9F6"));
                holder.llReps.setBackgroundColor(Color.parseColor("#26B9F6"));
                holder.llKG.setBackgroundColor(Color.parseColor("#00A5F4"));
//                notifyDataSetInvalidated();
                notifyDataSetChanged();

            }
        });


        holder.llChange.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (cd.isConnectingToInternet()) {

                    Log.i("check internet","true");

                    if (weightnew.trim().equals("")) {
                        Toast.makeText(context, "ange vikt.", Toast.LENGTH_SHORT).show();
                    } else {

//                        Toast.makeText(context,"Button- "+weightnew,Toast.LENGTH_SHORT).show();
                        trainingPerticularExerciseSetsDatatypeArrayList.get(position).setKg(weightnew);
                        notifyDataSetChanged();
                        trainingFragment.editExcercise(userProgramID, trainingID, position);

                    }

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });



//       did

        holder.etWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (cd.isConnectingToInternet()) {

                        Log.i("check internet","true");


                            if (weightnew.trim().equals("")) {
                                Toast.makeText(context, "ange vikt.", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(context,"KEY2- "+weightnew,Toast.LENGTH_SHORT).show();
                                trainingPerticularExerciseSetsDatatypeArrayList.get(position)
                                        .setKg(weightnew);
                                notifyDataSetChanged();
                                        trainingFragment.editExcercise(userProgramID, trainingID, position);
                            }

                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        return convertView;
    }

    protected class ViewHolder {
        TitilliumSemiBold txtSet, txtReps;
        TitilliumRegular txtRP, txtKG;
        EditText etWeight;
        LinearLayout llCheck, llChange, llSet, llReps, llKG;
        RelativeLayout main_container;

    }



}
