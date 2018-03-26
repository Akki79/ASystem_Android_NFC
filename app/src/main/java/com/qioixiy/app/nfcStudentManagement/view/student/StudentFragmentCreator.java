package com.qioixiy.app.nfcStudentManagement.view.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.DynInfo;
import com.qioixiy.app.nfcStudentManagement.model.LoginModel;
import com.qioixiy.app.nfcStudentManagement.model.NetDataModel;
import com.qioixiy.app.nfcStudentManagement.model.NfcTag;
import com.qioixiy.app.nfcStudentManagement.model.StudentModel;
import com.qioixiy.app.nfcStudentManagement.view.manager.AddNFCActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.AddStudentActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.ManagerNFCActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.StudentManagementActivity;
import com.qioixiy.app.nfcStudentManagement.view.manager.StudentManagementAdapter;
import com.qioixiy.utils.Geocoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.qioixiy.utils.ByteArrayChange.ByteArrayToHexString;

/**
 *
 */
public class StudentFragmentCreator extends Fragment implements StudentFragmentPagerAdapter.FragmentPageChanged {

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    StudentListInfoViewAdapter adapter = (StudentListInfoViewAdapter)msg.obj;
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };

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

        List<String> info = new ArrayList<String>();
        info.add("加载中");
        StudentListInfoViewAdapter adapter = new StudentListInfoViewAdapter(info);
        recyclerView.setAdapter(adapter);

        studentInfoFetch();
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

    @Override
    public void onChangedIndex(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                studentInfoFetch();
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    private void studentInfoFetch() {

        NetDataModel.sendHttpRequest(new NetDataModel.OnHttpRequestReturn() {
            @Override
            public void onReturn(String response) {

                Geocoder geocoder = new Geocoder();

                List<String> info = new ArrayList<String>();

                try {
                    // NfcTags
                    List<NfcTag> mNfcList = new ArrayList<>();
                    String responseNfcTag = NetDataModel.sendHttpRequestSync("nfc", "viewall");
                    JSONObject json = new JSONObject(responseNfcTag);
                    JSONArray array = json.getJSONArray("items");

                    for (int index = 0; index < array.length(); index++) {
                        JSONObject obj = array.getJSONObject(index);

                        int id = obj.getInt("id");
                        String tag = obj.getString("tag");
                        String define = obj.getString("define");

                        NfcTag nfcTag = new NfcTag();
                        nfcTag.setId(id);
                        nfcTag.setTag(tag);
                        nfcTag.setDefine(define);

                        mNfcList.add(nfcTag);
                    }

                    json = new JSONObject(response);
                    array = json.getJSONArray("items");

                    int userId = LoginModel.instance().getUserId();
                    for (int index = 0; index < array.length(); index++) {
                        JSONObject obj = array.getJSONObject(index);

                        int id = obj.getInt("id");
                        int studentId = obj.getInt("studentId");
                        String nfcTag = obj.getString("nfcTag");
                        String geo = obj.getString("geo");
                        String type = obj.getString("type");
                        String createTimestamp = obj.getString("createTimestamp");

                        if (userId != studentId) {
                            continue;
                        }

                        String str = new String();
                        str += createTimestamp;
                        for(NfcTag nfcTag1 : mNfcList) {
                            if (nfcTag1.getTag().equals(nfcTag)) {
                                str += nfcTag1.getDefine();
                                break;
                            }
                        }

                        String[] s = geo.split(",");
                        if (s.length == 2) {
                            List<String> address = geocoder.getFromLocation(s[0], s[1]);
                            if (address.size() > 0) {
                                str += "(" + address.get(0) + ")";
                            }
                        }
                        if (type.equals("check_in")) {
                            str += "签入";
                        } else {
                            str += "签出";
                        }

                        info.add(str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (info.size() == 0) {
                    info.add("无记录");
                }

                StudentListInfoViewAdapter adapter = new StudentListInfoViewAdapter(info);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = adapter;
                handler.sendMessage(msg);
            }
        }, "dyn_info", "viewall");
    }
}
