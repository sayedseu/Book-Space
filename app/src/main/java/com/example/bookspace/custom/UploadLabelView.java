package com.example.bookspace.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookspace.R;

public class UploadLabelView extends ConstraintLayout {

    private ImageView imageView;
    private TextView textView;
    private Drawable labelIcon;
    private String label;

    public UploadLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);


        View view = LayoutInflater.from(context).inflate(R.layout.upload_label, this, true);

        imageView = view.findViewById(R.id.imageView);
        textView = view.findViewById(R.id.textView);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UploadLabelView, 0, 0);

        try {

            labelIcon = typedArray.getDrawable(R.styleable.UploadLabelView_setLabelIcon);
            label = typedArray.getString(R.styleable.UploadLabelView_setLabel);

            imageView.setImageDrawable(labelIcon);
            textView.setText(label);

        } finally {
            typedArray.recycle();
        }
    }

    public Drawable getLabelIcon() {
        return labelIcon;
    }

    public void setLabelIcon(Drawable labelIcon) {
        imageView.setImageDrawable(labelIcon);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        textView.setText(label);
    }
}
