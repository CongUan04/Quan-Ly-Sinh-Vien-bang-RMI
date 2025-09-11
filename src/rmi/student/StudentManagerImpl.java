package rmi.student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.*;

public class StudentManagerImpl extends UnicastRemoteObject implements StudentManager {
    private static final long serialVersionUID = 1L;
    private Map<String, Student> students;
    private final File storageFile = new File("students.csv");

    protected StudentManagerImpl() throws RemoteException {
        super();
        students = new HashMap<>();
        loadFromFile();
    }

    // Load từ CSV
    private synchronized void loadFromFile() {
        students.clear();
        if (!storageFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Bỏ qua dòng trống hoặc header
                if (line.trim().isEmpty() || line.startsWith("Id,")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());
                    String email = parts[3].trim();
                    students.put(id, new Student(id, name, year, email));
                }
            }
        } catch (Exception e) {
            System.err.println("Load CSV error: " + e.getMessage());
        }
    }

    // Lưu xuống CSV
    private synchronized void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storageFile))) {
            // Ghi header
            bw.write("ID,Name,Year,Email");
            bw.newLine();
            for (Student s : students.values()) {
                bw.write(s.getId() + "," + s.getName() + "," + s.getYear() + "," + s.getEmail());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Save CSV error: " + e.getMessage());
        }
    }

    @Override
    public synchronized boolean addStudent(Student s) throws RemoteException {
        if (s == null || s.getId() == null) return false;
        if (students.containsKey(s.getId())) return false;
        students.put(s.getId(), s);
        saveToFile();
        return true;
    }

    @Override
    public synchronized boolean updateStudent(Student s) throws RemoteException {
        if (s == null || s.getId() == null) return false;
        if (!students.containsKey(s.getId())) return false;
        students.put(s.getId(), s);
        saveToFile();
        return true;
    }

    @Override
    public synchronized boolean deleteStudent(String id) throws RemoteException {
        if (id == null) return false;
        if (students.remove(id) != null) {
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public synchronized Student getStudentById(String id) throws RemoteException {
        return students.get(id);
    }

    @Override
    public synchronized List<Student> getAllStudents() throws RemoteException {
        return new ArrayList<>(students.values());
    }
}
