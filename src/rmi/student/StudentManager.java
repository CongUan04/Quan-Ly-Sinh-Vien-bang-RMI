package rmi.student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudentManager extends Remote {
    boolean addStudent(Student s) throws RemoteException;
    boolean updateStudent(Student s) throws RemoteException;
    boolean deleteStudent(String id) throws RemoteException;
    Student getStudentById(String id) throws RemoteException;
    List<Student> getAllStudents() throws RemoteException;
}
