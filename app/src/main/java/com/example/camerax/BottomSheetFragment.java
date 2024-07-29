package com.example.camerax;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private EditText reasonEt;
    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        reasonEt = view.findViewById(R.id.reason_et);


     return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(500);
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {super.onDismiss(dialog);
        if (getActivity() instanceof MainActivity) {
            if(!reasonEt.getText().toString().trim().isEmpty()) {
                ((MainActivity) getActivity()).onBottomSheetDismissed();
            }
            else {
                ((MainActivity) getActivity()).EtEmpty();
            }
        }
    }


}
