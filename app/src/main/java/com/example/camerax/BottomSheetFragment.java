//package com.example.camerax;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//import java.util.Objects;
//
//public class BottomSheetFragment extends BottomSheetDialogFragment {
//    private EditText reasonEt;
//
//    @Nullable
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
//
////        reasonEt = view.findViewById(R.id.reason_et);
////        reasonEt.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////
////            }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////
////            }
////
////            @Override
////            public void afterTextChanged(Editable s) {
////                if (getActivity() instanceof MainActivity) {
////                    if(!reasonEt.getText().toString().trim().isEmpty()) {
////                        ((MainActivity) getActivity()).onBottomSheetDismissed();
////                    }
////                    else {
////                        ((MainActivity) getActivity()).EtEmpty();
////                    }
////                }
////
////            }
////        });
//
//
//
//     return view;
//    }
//
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
////        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
////        bottomSheetBehavior.setPeekHeight(170);
////        bottomSheetBehavior.setHideable(false);
////    }
//
//
//}
//
//
//
//
