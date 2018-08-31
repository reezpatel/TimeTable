package com.reezx.android.timetable;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reezx.android.timetable.Database.Period;
import com.reezx.android.timetable.Database.PeriodManger;
import com.reezx.android.timetable.Database.Subject;
import com.reezx.android.timetable.Database.SubjectManger;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {
    private static final String DAY_ORDER_PARAM = "dayorder";
    private static String USER_ID;
    private Context context;
    private int CURRENT_DAY_ORDER = 1;
    ProgressDialog progressDialog;
    ViewPager viewPager;
    ArrayList<ArrayList<Period>> PeriodArrayLists = null;
    ArrayList<Subject> SubjectArrayList = null;

    private OnFragmentInteractionListener mListener;

    public TimeTableFragment() {}

    public static TimeTableFragment newInstance(int dayOrder) {
        TimeTableFragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putInt(DAY_ORDER_PARAM, dayOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CURRENT_DAY_ORDER = getArguments().getInt(DAY_ORDER_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.TabLayoutTimeTableFragmentMain);
        context = getActivity();
        USER_ID = SharedPrefManager.getUserData(context).getString(Constants.TEXT_ID);

        viewPager = (ViewPager) view.findViewById(R.id.ViewPagerTimeTableFragmentMain);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabButtonTimtTableFragmentMain);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabButtonClicked(viewPager.getCurrentItem()+1);
            }
        });

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
        databaseReference.keepSynced(true);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Time Table...");
        progressDialog.setIndeterminate(true);


        if(progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.child(SubjectManger.SUBJECTS).getChildren().iterator();
                SubjectArrayList = new ArrayList<Subject>();
                SubjectArrayList.add(new Subject("","FREE PERIOD","FREE PERIOD",0,""));

                while (iterator.hasNext()) {
                    Subject subject = (Subject) iterator.next().getValue(Subject.class);
                    SubjectArrayList.add(subject);
                }

                PeriodArrayLists = new ArrayList<ArrayList<Period>>();
                for(int i=0;i<5;i++) {
                    ArrayList<Period> periodArrayList = new ArrayList<Period>();
                    DataSnapshot periodDataSnapshot = dataSnapshot.child(PeriodManger.PERIODS).child(""+(i+1));
                    if(periodDataSnapshot.getValue() == null) {
                        for(int j=0;j<10;j++) {
                            periodArrayList.add(new Period("FREE PERIOD",new Subject("","FREE PERIOD","FREE PERIOD",0,"")));
                        }
                    }else {
                        for (DataSnapshot dataSnapshot1 : periodDataSnapshot.getChildren()) {
                            Period period = ((Period) dataSnapshot1.getValue(Period.class));
                            periodArrayList.add(period);
                        }
                    }
                    PeriodArrayLists.add(periodArrayList);
                }

                Log.d(Constants.TAG,PeriodArrayLists.toString());

                TimeTablePageAdapter pageAdapter = new TimeTablePageAdapter(getChildFragmentManager(),PeriodArrayLists,getContext());
                viewPager.setAdapter(pageAdapter);
                viewPager.setCurrentItem(CURRENT_DAY_ORDER-1);
                pageAdapter.notifyDataSetChanged();


                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.hide();
                }

                Log.d(Constants.TAG,"Time Table Fragment: Set The Adapter::DONE");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
             //       + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void onFabButtonClicked(final int dayOrder) {
        if(PeriodArrayLists == null || SubjectArrayList == null) {
            Toast.makeText(context,"Still Loading... Please Wait",Toast.LENGTH_LONG).show();
            return;
        }
        final PeriodDialog dialog = new PeriodDialog(getActivity(),SubjectArrayList,PeriodArrayLists.get(dayOrder-1),dayOrder);
        dialog.show();
        dialog.addOnSaveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
                ArrayList<Period> periods = dialog.savedPeriods;
                for(int i=0;i<10;i++) {
                    databaseReference.child(PeriodManger.PERIODS).child(""+dayOrder).child(""+(i+1)).setValue(periods.get(i));
                }
                CURRENT_DAY_ORDER = dayOrder;
                Toast.makeText(context,"Data Saved... ",Toast.LENGTH_LONG).show();
            }
        });
    }
}
