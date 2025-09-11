package rmi.student;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class ClientGUI extends JFrame {
    private StudentManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    public ClientGUI() {
        try {
            manager = (StudentManager) Naming.lookup("rmi://localhost:1099/StudentManager");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến Server: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Quản lý sinh viên - RMI Client");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // 🔹 Tiêu đề
        JLabel titleLabel = new JLabel("Danh sách sinh viên", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // 🔹 Bảng sinh viên
        tableModel = new DefaultTableModel(new Object[]{"Mã SV", "Họ và tên", "Năm sinh", "Email", "Cập nhật"}, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 4; // chỉ cho cột Cập nhật bấm được
            }
        };

        // Render nút trong cột "Cập nhật"
        table.getColumn("Cập nhật").setCellRenderer(new ButtonRenderer());
        table.getColumn("Cập nhật").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Các nút chức năng chung
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);

        // Sự kiện
        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents());

        loadStudents();
    }

    // Load dữ liệu vào bảng
    private void loadStudents() {
        try {
            tableModel.setRowCount(0); // clear
            List<Student> students = manager.getAllStudents();
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getYear(), s.getEmail(), "Cập nhật"});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách: " + e.getMessage());
        }
    }

    // Form thêm sinh viên
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Thêm sinh viên", true);
        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setLocationRelativeTo(this);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField emailField = new JTextField();

        dialog.add(new JLabel("Mã SV:"));
        dialog.add(idField);
        dialog.add(new JLabel("Họ và tên:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Năm sinh:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);

        JButton okButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");
        dialog.add(okButton);
        dialog.add(cancelButton);

        okButton.addActionListener(e -> {
            try {
                Student s = new Student(
                        idField.getText(),
                        nameField.getText(),
                        Integer.parseInt(yearField.getText()),
                        emailField.getText()
                );
                if (manager.addStudent(s)) {
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
                    loadStudents();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Form chỉnh sửa sinh viên
    public void showEditDialog(String id) {
        try {
            Student s = manager.getStudentById(id);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên!");
                return;
            }

            JDialog dialog = new JDialog(this, "Cập nhật sinh viên", true);
            dialog.setSize(350, 250);
            dialog.setLayout(new GridLayout(5, 2));
            dialog.setLocationRelativeTo(this);

            JTextField idField = new JTextField(s.getId());
            idField.setEditable(false);
            JTextField nameField = new JTextField(s.getName());
            JTextField yearField = new JTextField(String.valueOf(s.getYear()));
            JTextField emailField = new JTextField(s.getEmail());

            dialog.add(new JLabel("Mã SV:"));
            dialog.add(idField);
            dialog.add(new JLabel("Họ và tên:"));
            dialog.add(nameField);
            dialog.add(new JLabel("Năm sinh:"));
            dialog.add(yearField);
            dialog.add(new JLabel("Email:"));
            dialog.add(emailField);

            JButton saveButton = new JButton("Lưu");
            JButton cancelButton = new JButton("Hủy");
            dialog.add(saveButton);
            dialog.add(cancelButton);

            saveButton.addActionListener(e -> {
                try {
                    Student updated = new Student(
                            idField.getText(),
                            nameField.getText(),
                            Integer.parseInt(yearField.getText()),
                            emailField.getText()
                    );
                    if (manager.updateStudent(updated)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        loadStudents();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể cập nhật sinh viên!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            });

            cancelButton.addActionListener(e -> dialog.dispose());
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    // Xóa sinh viên
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sinh viên để xóa!");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sinh viên " + name + " không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (manager.deleteStudent(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên để xóa!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
}

// Renderer cho nút
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setText("Cập nhật");
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        return this;
    }
}

// Editor cho nút
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String id;
    private boolean clicked;
    private ClientGUI parent;

    public ButtonEditor(JCheckBox checkBox, ClientGUI parent) {
        super(checkBox);
        this.parent = parent;
        button = new JButton("Cập nhật");
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        id = (String) table.getValueAt(row, 0);
        clicked = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (clicked) {
            parent.showEditDialog(id);
        }
        clicked = false;
        return "Cập nhật";
    }
}
