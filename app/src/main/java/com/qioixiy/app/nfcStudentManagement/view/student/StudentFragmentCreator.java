package com.qioixiy.app.nfcStudentManagement.view.student;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.NetDataModel;
import com.qioixiy.app.nfcStudentManagement.model.StudentModel;
import com.qioixiy.app.nfcStudentManagement.view.manager.AddNFCActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.AddStudentActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.ManagerNFCActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.StudentManagementActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.StudentManagementAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StudentFragmentCreator extends Fragment {

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Create a new instance of the fragment
     */
    public static StudentFragmentCreator create(int index) {
        StudentFragmentCreator fragment = new StudentFragmentCreator();
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
                initStudentUploader(view);
                return view;
            case 1:
                view = inflater.inflate(R.layout.fragment_nfcstudentmangement_manager, container, false);
                initStudentSelfInfo(view);
                return view;
            case 2:
                view = inflater.inflate(R.layout.fragment_nfcstudentmangement_settings, container, false);
                initSettings(view);
                return view;
            default:
                return null;
        }
    }

    private void initStudentUploader(View view)
    {
        fragmentContainer = view.findViewById(R.id.fragment_nfcstudentmangement_container);
        recyclerView = view.findViewById(R.id.fragment_nfcstudentmangement_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> itemsData = new ArrayList<>();
        itemsData.add("签入");
        itemsData.add("签出");

        StudentManagementAdapter adapter = new StudentManagementAdapter(itemsData);

        adapter.setOnItemClickListener(new StudentManagementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), StudentCheckInActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), StudentCheckOutActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initSettings(View view) {
        final StudentMainActivity mMainActivity = (StudentMainActivity) getActivity();
        final SwitchCompat switchNfc = view.findViewById(R.id.fragment_nfcstudentmangement_switch_nfc);

        switchNfc.setChecked(mMainActivity.isNfcStatusOpened());
        switchNfc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }
        });
    }

    private void initStudentSelfInfo(View view) {

        fragmentContainer = view.findViewById(R.id.fragment_nfcstudentmangement_container);
        recyclerView = view.findViewById(R.id.fragment_nfcstudentmangement_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        StudentModel studentModel = new StudentModel();
        List<String> info = studentModel.getSelfInfo();

        StudentListInfoViewAdapter adapter = new StudentListInfoViewAdapter(info);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
