package com.reezx.android.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reezx.android.timetable.Database.Period;
import com.reezx.android.timetable.Database.PeriodManger;
import com.reezx.android.timetable.Database.SubjectManger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DayTimeTableModuleFragment extends Fragment {
    private int CURRENT_DAY_ORDER = 1;
    private Context context;
    private static String DAY_ORDER_ARG = "dayOrder";
    private ArrayList<Period> periodArrayList = null;
    private static String PERIODS_ARGS = "period";
    private OnListFragmentInteractionListener mListener;
    String USER_ID;

    public DayTimeTableModuleFragment() {
    }

    public static DayTimeTableModuleFragment newInstance(int dayOrder, ArrayList<Period> periodArrayList) {
        DayTimeTableModuleFragment fragment = new DayTimeTableModuleFragment();
        Bundle args = new Bundle();
        args.putInt(DAY_ORDER_ARG,dayOrder);
        args.putSerializable(PERIODS_ARGS,periodArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            CURRENT_DAY_ORDER = getArguments().getInt(DAY_ORDER_ARG);
            periodArrayList = (ArrayList<Period>) getArguments().getSerializable(PERIODS_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daytimetablemodule_list, container, false);
        context = getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewDayTimeTableModuleMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DayTimeTableModuleRecyclerViewAdapter adapter = new DayTimeTableModuleRecyclerViewAdapter(periodArrayList,context);
        recyclerView.setAdapter(adapter);

        Log.d(Constants.TAG,"Day Time Table Module Fragment: Set The Adapter::DONE");
        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
              //      + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int item);
    }
}
