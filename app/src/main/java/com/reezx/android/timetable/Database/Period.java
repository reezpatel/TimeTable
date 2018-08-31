package com.reezx.android.timetable.Database;

/**
 * Created by reezpatel on 22-Jul-17.
 */

public class Period {
    public String subjectCode;
    public Subject subject;

    public Period(String subjectCode, Subject subject) {
        this.subjectCode = subjectCode;
        this.subject = subject;
    }
    
    public Period() {}

    @Override
    public String toString() {
        return subject.name;
    }
}
