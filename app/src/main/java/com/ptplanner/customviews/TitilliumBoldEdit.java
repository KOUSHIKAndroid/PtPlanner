package com.ptplanner.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class TitilliumBoldEdit extends EditText {

	public TitilliumBoldEdit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public TitilliumBoldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public TitilliumBoldEdit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init() {

		super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "TitilliumWeb-Bold.ttf"));

	}

}
