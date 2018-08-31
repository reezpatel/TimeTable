package com.reezx.android.timetable;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.reezx.android.timetable.Database.Subject;

import java.util.ArrayList;

public class SubjectsRecyclerViewAdapter extends RecyclerView.Adapter<SubjectsRecyclerViewAdapter.ViewHolder> implements View.OnLongClickListener{
    ArrayList<Subject> subjects;
    Context context;
    OnSubjectContextMenuSelectedListner listner;
    int position;
    public SubjectsRecyclerViewAdapter(ArrayList<Subject> subjects,Context context,OnSubjectContextMenuSelectedListner listner) {
        this.subjects = subjects;
        this.context = context;
        this.listner = listner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_subjects, parent, false);

        view.setOnLongClickListener(this);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mView.setTag(position);
        Subject subject = subjects.get(position);
        holder.subjectCode.setText(subject.code);
        holder.subjectName.setText(subject.name);
        holder.credits.setText(String.valueOf(subject.credit));
        holder.faculty.setText(subject.faculty);
        holder.venue.setText(subject.location);
    }

    @Override
    public int getItemCount() {
        return subjects == null?0:subjects.size();
    }

    @Override
    public boolean onLongClick(View v) {
        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.inflate(R.menu.list_edit_menu);
        final Subject subject = subjects.get((int)v.getTag());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        listner.onDeleteSubject(subject);
                        break;
                    case R.id.action_edit:
                        listner.onEditSubject(subject);
                        break;
                    default:
                        Toast.makeText(context,"Error in Subject Adapter",Toast.LENGTH_LONG).show();
                        return false;
                }
                return true;
            }
        });
        popupMenu.show();
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         final View mView;
        TextView subjectCode;
        TextView subjectName;
        TextView credits;
        TextView faculty;
        TextView venue;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            subjectCode = (TextView) view.findViewById(R.id.TextViewDayTimeTableModuleSubjectCode);
            subjectName = (TextView) view.findViewById(R.id.TextViewSubjectDialogSubjectName);
            credits = (TextView) view.findViewById(R.id.TextViewSubjectListCredit);
            faculty = (TextView) view.findViewById(R.id.TextViewSubjectDialogFacultyName);
            venue = (TextView) view.findViewById(R.id.TextViewSubjectDialogClassVenue);
        }
    }

    public interface OnSubjectContextMenuSelectedListner {
        public void onEditSubject(Subject subject);
        public void onDeleteSubject(Subject subject);
    }
}
