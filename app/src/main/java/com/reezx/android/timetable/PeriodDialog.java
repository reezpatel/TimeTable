package com.reezx.android.timetable;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.reezx.android.timetable.Database.Period;
import com.reezx.android.timetable.Database.Subject;

import java.util.ArrayList;

/**
 * Created by reezpatel on 23-Jul-17.
 */

public class PeriodDialog extends Dialog {
    ArrayList<Subject> subjects;
    ArrayList<Period> periods;
    Spinner spinners[] = new Spinner[10];
    int dayOrder = 1;
    View.OnClickListener listener;
    Context context;

    public ArrayList<Period> savedPeriods = null;

    public PeriodDialog(@NonNull Context context, ArrayList<Subject> subjects, ArrayList<Period> periods,int dayOrder) {
        super(context);
        this.context = context;
        this.subjects = subjects;
        this.periods = periods;
        this.dayOrder = dayOrder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_period_mange);
        ((TextView) findViewById(R.id.TextViewPeriodDialogTitle)).setText("Day Order: " + dayOrder);
        ((Button) findViewById(R.id.buttonPeriodDialogSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPeriods = new ArrayList<Period>(10);
                for(int i=1;i<=10;i++) {
                    Subject subject = subjects.get(spinners[i-1].getSelectedItemPosition());
                    savedPeriods.add(new Period(subject.code,subject));
                }
                listener.onClick(v);
                dismiss();
            }
        });

        ((Button) findViewById(R.id.buttonPeriodDialogCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        for(int i=1;i<=10;i++) {
            spinners[i-1] = (Spinner) findViewById(context.getResources().getIdentifier("SpinnerPeriodDialog" + i,"id",context.getPackageName()));
            SpinnerAdapter adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,subjects.toArray());
            spinners[i-1].setAdapter(adapter);

            int index = getIndexOf(periods.get(i-1).subject.code);
            Log.d(Constants.TAG,"Index: " + index);
            spinners[i-1].setSelection(index);
        }

    }

    int getIndexOf(String code) {
        for(int i=0;i<subjects.size();i++) {
            if(code.equals(subjects.get(i).code)) {
                return i;
            }
        }
        return 0;
    }

    void addOnSaveClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
