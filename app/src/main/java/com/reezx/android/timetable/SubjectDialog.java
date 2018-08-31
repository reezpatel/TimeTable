package com.reezx.android.timetable;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.reezx.android.timetable.Database.Subject;


/**
 * Created by reezpatel on 01-Jul-17.
 */

public class SubjectDialog extends Dialog {
    public SubjectDialog(Context context,Subject subject) {
        super(context);
        this.subject = subject;
    }

    public SubjectDialog(Context context) {
        super(context);
    }

    private String TITLE = "";
    private String POSITIVE_BUTTON_TEXT = "";
    private Subject subject = null;
    public static final boolean OK = true;

    public boolean RESULT = false;
    public String SUBJECT_CODE = "";
    public String SUBJECT_NAME = "";
    public String FACULTY_NAME = "";
    public String CLASS_VENUE = "";
    public int CREDITS = 0;

    private EditText subjectCode,subjectName,facultyName,classVenue;
    private Spinner credits;

    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.subject_manage_dialog);


        subjectCode = (TextInputEditText)findViewById(R.id.TextViewSubjectDialogSubjectCode);
        subjectName = (TextInputEditText)findViewById(R.id.TextViewSubjectDialogSubjectName);
        facultyName = (TextInputEditText)findViewById(R.id.TextViewSubjectDialogFacultyName);
        classVenue = (TextInputEditText)findViewById(R.id.TextViewSubjectDialogClassVenue);
        credits = (Spinner)findViewById(R.id.SpinnerSubjectDialoge);
        Button button = (Button) findViewById(R.id.ButtonSubjectDialogPositive);
        getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        credits.setAdapter(ArrayAdapter.createFromResource(getContext(),R.array.credits,android.R.layout.simple_dropdown_item_1line));

        TextView titleTextView = (TextView) findViewById(R.id.TextViewSubjectDialogTitle);
        titleTextView.setText(TITLE);

        button.setText(POSITIVE_BUTTON_TEXT);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESULT = OK;

                SUBJECT_CODE = subjectCode.getText().toString();
                SUBJECT_NAME = subjectName.getText().toString();
                FACULTY_NAME = facultyName.getText().toString();
                CLASS_VENUE = classVenue.getText().toString();
                CREDITS = credits.getSelectedItemPosition();

                listener.onClick(v);
                dismiss();
            }
        });

        Button button1 = (Button) findViewById(R.id.ButtonSubjectDialogNegative);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESULT = false;
                dismiss();
            }
        });

        if(subject != null) {
            if(subjectCode != null) {
                subjectCode.setText(subject.code);
                subjectCode.setEnabled(false);
                subjectName.setText(subject.name);
                facultyName.setText(subject.faculty);
                classVenue.setText(subject.location);
                credits.setSelection(subject.credit);
            }
        }


    }

    public void setSubject(Subject subject) {

    }


    public void setPositiveButton(String text, View.OnClickListener listener) {
        POSITIVE_BUTTON_TEXT = text;
        this.listener = listener;
    }

    @Override
    public void setTitle(CharSequence title) {
        TITLE = title.toString();
    }

}
