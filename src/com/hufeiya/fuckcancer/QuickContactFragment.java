package com.hufeiya.fuckcancer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Spinner;

public class QuickContactFragment extends DialogFragment  {
    private Spinner chooseCancerSpinner;
    private EditText nameEditText;
    private Button saveInfoButton;
    private TextView welcomeTextView;
    private int oldCancerType;
    public static QuickContactFragment newInstance() {
        return new QuickContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        View root = inflater.inflate(com.hufeiya.fuckcancer.R.layout.fragment_quick_contact, container, false);
        welcomeTextView = (TextView)root.findViewById(R.id.textView2);
        nameEditText = (EditText)root.findViewById(R.id.name_edit_text);
        chooseCancerSpinner = (Spinner)root.findViewById(R.id.choose_cancer_spinner);
        saveInfoButton = (Button)root.findViewById(R.id.button_yes);
        SharedPreferences pref = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        oldCancerType = pref.getInt("type",0);
        if(pref.getString("name","") != ""){
            nameEditText.setText(pref.getString("name", ""));
            chooseCancerSpinner.setSelection(pref.getInt("type", 1) - 1);
            welcomeTextView.setText("要更改信息吗？｡◕‿◕｡");

        }
        String[] items = {"胃", "肝", "肺", "结肠", "直肠", "乳腺", "胰腺", "胆囊", "淋巴", "白血病", "黑色素瘤", "前列腺", "睾丸", "卵巢", "舌", "脑"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        chooseCancerSpinner.setAdapter(adapter);
        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                editor.putString("name", nameEditText.getText().toString());
                editor.putInt("type", chooseCancerSpinner.getSelectedItemPosition() + 1);//Cancer type from 1,1 is stomach cancer.
                editor.commit();
                if (!((MainActivity) getActivity()).alreadyChosenCancerType){
                    ((MainActivity) getActivity()).displayFragment();
                }else{
                    if(oldCancerType != 0 && oldCancerType != chooseCancerSpinner.getSelectedItemPosition() + 1){
                        //restart the main activity when change the cancer type beacause all data are different.
                        Intent i = getActivity().getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(QuickContactFragment.this).commit();

            }
        });
        return root;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();

        // change dialog width
        if (getDialog() != null) {
            int fullWidth;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                fullWidth = size.x;
            } else {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                fullWidth = display.getWidth();
            }

            final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                    .getDisplayMetrics());
            int w = fullWidth - padding;
            int h = getDialog().getWindow().getAttributes().height;
            getDialog().getWindow().setLayout(w, h);
        }
    }

}
