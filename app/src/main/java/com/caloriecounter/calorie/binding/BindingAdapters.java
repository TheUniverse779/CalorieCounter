package com.caloriecounter.calorie.binding;

import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

public class BindingAdapters {

    @BindingAdapter("android:text")
    public static void bindIntegerInText(EditText tv, int value)
    {
        tv.setText(String.valueOf(value));

        // Set the cursor to the end of the text
        tv.setSelection(tv.getText().length());
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getIntegerFromBinding(TextView view)
    {
        String string = view.getText().toString();

        return string.isEmpty() ? 0 : Integer.parseInt(string);
    }


}
