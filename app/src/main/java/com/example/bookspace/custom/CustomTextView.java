package com.example.bookspace.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookspace.R;

public class CustomTextView extends ConstraintLayout {

    private String labelName, labelValue;
    private TextView labelTV, valueTV;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_text_view, this, true);

        labelTV = view.findViewById(R.id.key);
        valueTV = view.findViewById(R.id.value);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);

        try {

            labelName = typedArray.getString(R.styleable.CustomTextView_setLabelName);
            labelValue = typedArray.getString(R.styleable.CustomTextView_setLabelValue);

            labelTV.setText(labelName);
            valueTV.setText(labelValue);

        } finally {
            typedArray.recycle();
        }
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        labelTV.setText(labelName);
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        valueTV.setText(labelValue);
    }
}
