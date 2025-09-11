package rmi.student;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int year;
    private String email;

    public Student() {}

    public Student(String id, String name, int year, String email) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.email = email;
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return id + " - " + name + " - " + year + " - " + email;
    }
}
