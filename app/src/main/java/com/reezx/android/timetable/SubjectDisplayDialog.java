package com.reezx.android.timetable;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.reezx.android.timetable.Database.Subject;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectDisplayDialog extends Dialog {
    Subject subject;

    public SubjectDisplayDialog(@NonNull Context context,Subject subject) {
        super(context);
        this.subject = subject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTEXT_MENU);
        setContentView(R.layout.dialog_subject_display);

        ((TextView)findViewById(R.id.TextViewSubjectDialogSubjectName)).setText(subject.name);
        ((TextView)findViewById(R.id.TextViewSubjectDialogSubjectCode)).setText(subject.code);
        ((TextView)findViewById(R.id.TextViewSubjectDialogFacultyName)).setText(subject.faculty);
        ((TextView)findViewById(R.id.TextViewSubjectDialogClassVenue)).setText(subject.location);
        ((TextView)findViewById(R.id.TextViewSubjectDialogCredit)).setText(subject.credit+"");
        ((Button)findViewById(R.id.ButtonSubjectDialogNegative)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
