package com.reezx.android.timetable.Database;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reezx.android.timetable.MainActivity;

import java.util.ArrayList;

/**
 * Created by reezpatel on 01-Jul-17.
 */

public class SubjectManger {
    private DatabaseReference storageReference;
    private String authCode;
    public static final String SUBJECTS = "subjects";

    public SubjectManger(String authCode) {
        storageReference = FirebaseDatabase.getInstance().getReference(authCode);
        storageReference.keepSynced(true);
        this.authCode = authCode;
    }

    public boolean addSubject(Subject subject) {
        storageReference.child(SUBJECTS).child(subject.getCode()).setValue(subject);
        return true;
    }

}
