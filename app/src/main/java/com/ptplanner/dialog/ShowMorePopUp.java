package com.ptplanner.dialog;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ptplanner.R;
import com.ptplanner.customviews.TitilliumRegular;
import com.ptplanner.customviews.TitilliumSemiBold;

/**
 * Created by su on 25/6/15.
 */
public class ShowMorePopUp extends PopupWindow {

    Context context;
    View popupView;
    LinearLayout llHide;
    TitilliumSemiBold txtTitle;
    TitilliumRegular txtDescription, txtInstruction;

    String title, description, instruction;

    public ShowMorePopUp(final Context context, String title, String description, String instruction) {
        super(context);

        this.context = context;
        this.title = title;
        this.description = description;
        this.instruction = instruction;

        setContentView(LayoutInflater.from(context).inflate(R.layout.frag_more, null));
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupView = getContentView();
        setFocusable(true);

        llHide = (LinearLayout) popupView.findViewById(R.id.ll_hide);
        txtTitle = (TitilliumSemiBold) popupView.findViewById(R.id.txt_title);
        txtDescription = (TitilliumRegular) popupView.findViewById(R.id.txt_description);
        txtInstruction = (TitilliumRegular) popupView.findViewById(R.id.instruction);

        txtTitle.setText(title);
        txtDescription.setText(Html.fromHtml(description));
        txtInstruction.setText(Html.fromHtml(instruction));

        llHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
