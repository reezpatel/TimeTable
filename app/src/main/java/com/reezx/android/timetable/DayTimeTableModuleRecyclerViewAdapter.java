package com.reezx.android.timetable;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reezx.android.timetable.Database.Period;
import com.reezx.android.timetable.Database.Subject;

import java.util.ArrayList;

public class DayTimeTableModuleRecyclerViewAdapter extends RecyclerView.Adapter<DayTimeTableModuleRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Period> Subjects;
    private Context context;

    public DayTimeTableModuleRecyclerViewAdapter(ArrayList<Period> Subjects, Context context) {
        this.Subjects = Subjects;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_daytimetablemodule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position != 9) {
            holder.periodNumber.setText("  "+(position+1));
        }else {
            holder.periodNumber.setText(""+(position+1));
        }
        if(Subjects.get(position).subject.location != null) {
            holder.classLocation.setText(Subjects.get(position).subject.location);
        }
        holder.subjectCode.setText(Subjects.get(position).subject.name);
        holder.timeDuration.setText("Timing: "+Constants.TIMING[position]);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectDisplayDialog displayDialog = new SubjectDisplayDialog(context,Subjects.get(position).subject);
                displayDialog.show();
                displayDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView subjectCode;
        public TextView classLocation;
        public TextView periodNumber;
        public TextView timeDuration;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            subjectCode = (TextView) view.findViewById(R.id.TextViewDayTimeTableModuleSubjectCode);
            classLocation = (TextView) view.findViewById(R.id.TextViewSubjectDialogClassVenue);
            periodNumber = (TextView) view.findViewById(R.id.TextViewDayTimeTableModuleNos);
            timeDuration = (TextView) view.findViewById(R.id.TextViewDayTimeTableModuleTiming);
        }

    }
}
