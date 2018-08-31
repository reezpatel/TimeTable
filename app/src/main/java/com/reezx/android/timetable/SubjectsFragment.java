package com.reezx.android.timetable;

import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reezx.android.timetable.Database.Subject;
import com.reezx.android.timetable.Database.SubjectManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubjectsFragment extends Fragment {
    private SubjectManger subjectManger;
    private ProgressDialog progressDialog;
    private OnListFragmentInteractionListener mListener;

    public SubjectsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_subjects, container, false);
        Bundle bundle = SharedPrefManager.getUserData(getActivity());
        subjectManger = new SubjectManger(bundle.getString(Constants.TEXT_ID));


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Subjects...");
        progressDialog.setIndeterminate(true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SubjectDialog subjectDialog = new SubjectDialog(getActivity());
                subjectDialog.setTitle("Add Subject");
                subjectDialog.setPositiveButton("ADD", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = subjectDialog.SUBJECT_CODE;
                        String subject = subjectDialog.SUBJECT_NAME;
                        String faculty = subjectDialog.FACULTY_NAME;
                        int credits = subjectDialog.CREDITS;
                        String venue = subjectDialog.CLASS_VENUE;


                        //add the data to db
                        if(code.equals("")) {
                            Toast.makeText(getContext(),"Please Enter A Valid Subject Code",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //add the data to db
                        subjectManger.addSubject(new Subject(faculty,subject,code,credits,venue));
                        Toast.makeText(getContext(),"Subject Added",Toast.LENGTH_SHORT).show();
                        renderFragment(view);
                    }
                });
                subjectDialog.show();
            }
        });

        renderFragment(view);
        return view;
    }

    void renderFragment(View view) {
        if(progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        // Set the adapter
        Context context = view.getContext();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewSubjectFragmentMain);

        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
        final String USER_ID = SharedPrefManager.getUserData(context).getString(Constants.TEXT_ID);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.child(SubjectManger.SUBJECTS).getChildren().iterator();
                ArrayList<Subject> subjects = new ArrayList<Subject>();

                while (iterator.hasNext()) {
                    Subject subject = (Subject) iterator.next().getValue(Subject.class);
                    subjects.add(subject);
                }

                SubjectsRecyclerViewAdapter adapter = new SubjectsRecyclerViewAdapter(subjects, getContext(), new SubjectsRecyclerViewAdapter.OnSubjectContextMenuSelectedListner() {
                    @Override
                    public void onEditSubject(Subject subject) {
                        final SubjectDialog subjectDialog = new SubjectDialog(getActivity(),subject);
                        subjectDialog.setTitle("Edit " + subject.code);
                        subjectDialog.setPositiveButton("SAVE CHANGES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String code = subjectDialog.SUBJECT_CODE;
                                String subject = subjectDialog.SUBJECT_NAME;
                                String faculty = subjectDialog.FACULTY_NAME;
                                int credits = subjectDialog.CREDITS;
                                String venue = subjectDialog.CLASS_VENUE;


                                //add the data to db
                                if(code.equals("")) {
                                    Toast.makeText(getContext(),"Please Enter A Valid Subject Code",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                subjectManger.addSubject(new Subject(faculty,subject,code,credits,venue));
                                Toast.makeText(getContext(),"Changes Saved",Toast.LENGTH_SHORT).show();
                            }
                        });
                        subjectDialog.show();
                    }

                    @Override
                    public void onDeleteSubject(Subject subject) {
                        databaseReference.child(SubjectManger.SUBJECTS).child(subject.code).removeValue();

                    }
                });
                recyclerView.setAdapter(adapter);
                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.RecyclerViewSubjectFragmentMain) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.list_edit_menu, menu);
        }else {
            Log.d(Constants.TAG,"Context Menu ID: " + v.getId());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Integer i);
    }
}
