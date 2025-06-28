import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    String name;
    int rollNum;
    int[] marks;
    int attendanceBits;
    String grade;

    public Student(String name, int rollNum, int[] marks, int attendanceBits) {
        this.name = name;
        this.rollNum = rollNum;
        this.marks = marks;
        this.attendanceBits = attendanceBits;
        calculateGrade();
    }

    void calculateGrade() {
        int sum = 0;
        for (int m : marks) sum += m;
        double avg = sum / (double) marks.length;
        if (avg >= 90) grade = "A+";
        else if (avg >= 75) grade = "A";
        else if (avg >= 60) grade = "B";
        else grade = "Needs Improvement";
    }

    int getPresentDays() {
        int count = 0;
        for (int i = 0; i < 7; i++) {
            if ((attendanceBits & (1 << i)) != 0) count++;
        }
        return count;
    }

    String getAttendanceDays() {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if ((attendanceBits & (1 << i)) != 0) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(days[i]);
            }
        }
        return sb.length() > 0 ? sb.toString() : "No days present";
    }

    public String toHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><div style='padding:10px;border:1px solid #ccc;margin:10px;background:#ffffffb3;'>");
        sb.append("<h3 style='color:#2e8b57;'>" + name + " (Roll No: " + rollNum + ")</h3>");
        sb.append("<p><strong>Grade:</strong> " + grade + "<br>");
        sb.append("<strong>Attendance:</strong> " + getPresentDays() + "/7<br>");
        sb.append("<strong>Days Present:</strong> " + getAttendanceDays() + "<br>");
        sb.append("<strong>Marks:</strong> ");
        for (int m : marks) sb.append(m + " ");
        sb.append("</p></div></html>");
        return sb.toString();
    }
}

public class StudentManagerGUI extends JFrame implements ActionListener {
    JTextField nameField, rollField, attendanceField, subjectCountField, searchField;
    JTextArea marksArea;
    JButton addBtn, displayBtn, searchBtn;
    JPanel outputPanel;
    ArrayList<Student> studentList = new ArrayList<>();

    public StudentManagerGUI() {
        setTitle("Student Management System");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load and draw background image
        Image bgImage = new ImageIcon("E:\\java\\ingnite\\background.png").getImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(backgroundPanel);

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);

        // Title: Student Details
        JLabel formTitle = new JLabel("Student Details", JLabel.CENTER);
        formTitle.setFont(titleFont);

        JPanel formTitlePanel = new JPanel(new BorderLayout());
        formTitlePanel.setOpaque(false);
        formTitlePanel.add(formTitle, BorderLayout.CENTER);

        // Transparent input form
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 200));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(20);
        rollField = new JTextField(20);
        subjectCountField = new JTextField(5);
        marksArea = new JTextArea(2, 20);
        attendanceField = new JTextField(10);
        searchField = new JTextField(10);

        marksArea.setLineWrap(true);
        marksArea.setWrapStyleWord(true);

        int row = 0;
        formPanelAdd(formPanel, gbc, row++, "Name:", nameField);
        formPanelAdd(formPanel, gbc, row++, "Roll No:", rollField);
        formPanelAdd(formPanel, gbc, row++, "Subjects (3-5):", subjectCountField);
        formPanelAdd(formPanel, gbc, row++, "Marks:", new JScrollPane(marksArea));
        formPanelAdd(formPanel, gbc, row++, "Attendance (7-bit):", attendanceField);
        formPanelAdd(formPanel, gbc, row++, "Search Roll No:", searchField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        addBtn = new JButton("Add Student");
        displayBtn = new JButton("Display All");
        searchBtn = new JButton("Search");

        addBtn.setFont(font);
        displayBtn.setFont(font);
        searchBtn.setFont(font);

        buttonPanel.add(addBtn);
        buttonPanel.add(displayBtn);
        buttonPanel.add(searchBtn);

        // Output Panel with title
        JLabel outputTitle = new JLabel("Student Records", JLabel.CENTER);
        outputTitle.setFont(titleFont);
        JPanel outputTitlePanel = new JPanel(new BorderLayout());
        outputTitlePanel.setOpaque(false);
        outputTitlePanel.add(outputTitle, BorderLayout.CENTER);

        outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(outputPanel);
        scrollPane.setPreferredSize(new Dimension(900, 300));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Layout assembly
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setOpaque(false);
        topPanel.add(formTitlePanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel outputWrapper = new JPanel(new BorderLayout());
        outputWrapper.setOpaque(false);
        outputWrapper.add(outputTitlePanel, BorderLayout.NORTH);
        outputWrapper.add(scrollPane, BorderLayout.CENTER);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(outputWrapper, BorderLayout.CENTER);

        // Listeners
        addBtn.addActionListener(this);
        displayBtn.addActionListener(this);
        searchBtn.addActionListener(this);

        setVisible(true);
    }

    private void formPanelAdd(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            try {
                String name = nameField.getText().trim();
                int roll = Integer.parseInt(rollField.getText().trim());
                int subjectCount = Integer.parseInt(subjectCountField.getText().trim());

                if (subjectCount < 3 || subjectCount > 5) {
                    showError("Subject count must be between 3 and 5.");
                    return;
                }

                String[] marksStr = marksArea.getText().trim().split("\\s+");
                if (marksStr.length != subjectCount) {
                    showError("Enter exactly " + subjectCount + " marks.");
                    return;
                }

                int[] marks = new int[subjectCount];
                for (int i = 0; i < subjectCount; i++)
                    marks[i] = Integer.parseInt(marksStr[i]);

                String attStr = attendanceField.getText().trim();
                if (!attStr.matches("[01]{7}")) {
                    showError("Attendance must be a 7-bit binary string.");
                    return;
                }

                int attBits = Integer.parseInt(attStr, 2);
                Student s = new Student(name, roll, marks, attBits);
                studentList.add(s);
                showInfo("Student added successfully.");
                clearInputs();
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        } else if (e.getSource() == displayBtn) {
            outputPanel.removeAll();
            if (studentList.isEmpty()) {
                showInfo("No students added yet.");
            } else {
                for (Student s : studentList) {
                    JLabel label = new JLabel(s.toHTML());
                    outputPanel.add(label);
                }
            }
            outputPanel.revalidate();
            outputPanel.repaint();
        } else if (e.getSource() == searchBtn) {
            try {
                int roll = Integer.parseInt(searchField.getText().trim());
                outputPanel.removeAll();
                boolean found = false;
                for (Student s : studentList) {
                    if (s.rollNum == roll) {
                        JLabel label = new JLabel(s.toHTML());
                        outputPanel.add(label);
                        found = true;
                        break;
                    }
                }
                if (!found) showError("Student with roll " + roll + " not found.");
                outputPanel.revalidate();
                outputPanel.repaint();
            } catch (Exception ex) {
                showError("Invalid roll number input.");
            }
        }
    }

    private void clearInputs() {
        nameField.setText("");
        rollField.setText("");
        subjectCountField.setText("");
        marksArea.setText("");
        attendanceField.setText("");
        searchField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagerGUI::new);
    }
}
