package com.reezx.android.timetable.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by reezpatel on 22-Jul-17.
 */

public class PeriodManger {
    private DatabaseReference storageReference;
    private String authCode;
    public static final String PERIODS = "periods";

    public PeriodManger(String authCode) {
        storageReference = FirebaseDatabase.getInstance().getReference(PERIODS);
        storageReference.keepSynced(true);
        this.authCode = authCode;
    }

    public boolean addPeriod(Period period) {
        storageReference.child(authCode).child(period.subjectCode).setValue(period);
        return true;
    }
}
