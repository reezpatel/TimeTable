package com.reezx.android.timetable.Database;

/**
 * Created by reezpatel on 18-Jul-17.
 */

public class Subject {
    public String faculty;
    public String name;
    public String code;
    public int credit;
    public String location;

    Subject() {

    }

    public Subject(String faculty, String name, String code, int credit, String location) {
        this.faculty = faculty;
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
