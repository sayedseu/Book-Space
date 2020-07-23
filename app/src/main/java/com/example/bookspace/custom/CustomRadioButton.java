package com.example.bookspace.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookspace.R;

public class CustomRadioButton extends ConstraintLayout {

    private ImageView imageView;
    private RadioButton radioButton;
    private TextView textView;
    private Drawable imageDrawable;
    private String condition;
    private boolean checked;

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_radio_button, this, true);

        imageView = view.findViewById(R.id.imageView);
        textView = view.findViewById(R.id.textView);
        radioButton = view.findViewById(R.id.radioButton);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRadioButton, 0, 0);

        try {

            imageDrawable = typedArray.getDrawable(R.styleable.CustomRadioButton_setImageDrawable);
            condition = typedArray.getString(R.styleable.CustomRadioButton_setCondition);
            checked = typedArray.getBoolean(R.styleable.CustomRadioButton_setCheck, false);

            imageView.setImageDrawable(imageDrawable);
            textView.setText(condition);
            radioButton.setChecked(checked);

        } finally {
            typedArray.recycle();
        }

    }


    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        textView.setText(condition);
    }

    public boolean isCheck() {
        return radioButton.isChecked();
    }

    public void setChecked(boolean b) {
        radioButton.setChecked(b);
    }

    public Drawable getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(Drawable imageDrawable) {
        imageView.setImageDrawable(imageDrawable);
    }

    public void setListener(OnClickListener listener) {
        radioButton.setOnClickListener(listener);
    }
}
