package com.qioixiy.app.nfcStudentManagement.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.qioixiy.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StudentManagementFragment extends Fragment {

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Create a new instance of the fragment
     */
    public static StudentManagementFragment newInstance(int index) {
        StudentManagementFragment fragment = new StudentManagementFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (getArguments().getInt("index", 0)) {
            case 0:
                View view = inflater.inflate(R.layout.fragment_nfcstudentmangement_manager, container, false);
                initManager(view);
                return view;
            case 1:
                view = inflater.inflate(R.layout.fragment_nfcstudentmangement_manager, container, false);
                initAnalysis(view);
                return view;
            case 2:
                view = inflater.inflate(R.layout.fragment_nfcstudentmangement_settings, container, false);
                initSettings(view);
                return view;
            default:
                return null;
        }
    }

    private void initSettings(View view) {
        final MainActivity mMainActivity = (MainActivity) getActivity();
        final SwitchCompat switchNfc = view.findViewById(R.id.fragment_nfcstudentmangement_switch_nfc);

        switchNfc.setChecked(mMainActivity.isNfcStatusOpened());
        switchNfc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMainActivity.updateNfcStatus(isChecked);
            }
        });

        final List<String> titleStates = new ArrayList<>();
        for (AHBottomNavigation.TitleState titleState : AHBottomNavigation.TitleState.values()) {
            titleStates.add(titleState.toString());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, titleStates);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initManager(View view) {

        fragmentContainer = view.findViewById(R.id.fragment_nfcstudentmangement_container);
        recyclerView = view.findViewById(R.id.fragment_nfcstudentmangement_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> itemsData = new ArrayList<>();
        itemsData.add("管理学员");
        itemsData.add("添加学员");
        itemsData.add("管理NFC标签");
        itemsData.add("添加NFC标签");;

        StudentManagementAdapter adapter = new StudentManagementAdapter(itemsData);
        recyclerView.setAdapter(adapter);
    }

    private void initAnalysis(View view) {

        fragmentContainer = view.findViewById(R.id.fragment_nfcstudentmangement_container);
        recyclerView = view.findViewById(R.id.fragment_nfcstudentmangement_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> itemsData = new ArrayList<>();
        itemsData.add("查看学员动态信息");
        itemsData.add("分析统计学员动态信息");

        StudentManagementAdapter adapter = new StudentManagementAdapter(itemsData);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Refresh
     */
    public void refresh() {
        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }
}
