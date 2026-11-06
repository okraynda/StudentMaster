package com.studentmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.*;

public class StudentForm extends JFrame {

    // WARNA
    private final Color NAVY = new Color(25, 35, 65);
    private final Color BLUE = new Color(55, 105, 190);
    private final Color BACKGROUND = new Color(242, 245, 250);
    private final Color WHITE = Color.WHITE;

    // KOMPONEN FORM
    private JTextField txtNim, txtNama, txtEmail, txtTelepon;
    private JComboBox<String> cmbGender, cmbJurusan;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    private final StudentDAO dao = new StudentDAO();

    private int selectedId = 0;

    // CONSTRUCTOR
    public StudentForm() {

        setTitle("Student Management System");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createSidebar();
        createMainContent();

        loadStudents();

        setVisible(true);
    }

    // ==========================================
    // SIDEBAR
    // ==========================================

    private void createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // LOGO UBL
        JLabel lblLogo = new JLabel();

        try {

            java.net.URL logoURL =
                    getClass().getResource("/image/logo_ubl.png");

            if (logoURL != null) {

                ImageIcon icon = new ImageIcon(logoURL);

                Image img = icon.getImage().getScaledInstance(
                        75, 75, Image.SCALE_SMOOTH
                );

                lblLogo.setIcon(new ImageIcon(img));

            } else {

                lblLogo.setText("UBL");
                lblLogo.setForeground(WHITE);
            }

        } catch (Exception e) {

            lblLogo.setText("UBL");
            lblLogo.setForeground(WHITE);
        }

        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 10, 10)
        );

        JLabel title = new JLabel("STUDENT");

        title.setForeground(WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("MANAGEMENT SYSTEM");

        subtitle.setForeground(new Color(180, 195, 220));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(title);
        sidebar.add(subtitle);

        sidebar.add(Box.createVerticalStrut(60));

        sidebar.add(createMenu("Dashboard"));
        sidebar.add(createMenu("Data Mahasiswa"));
        sidebar.add(createMenu("Laporan"));
        sidebar.add(createMenu("Pengaturan"));

        add(sidebar, BorderLayout.WEST);
    }

    // MENU SIDEBAR
    private JButton createMenu(String text) {

        JButton button = new JButton("   " + text);

        button.setMaximumSize(new Dimension(220, 50));
        button.setPreferredSize(new Dimension(220, 50));

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setForeground(WHITE);
        button.setBackground(NAVY);

        button.setFont(new Font("Arial", Font.PLAIN, 14));

        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }

    // ==========================================
    // MAIN CONTENT
    // ==========================================

    private void createMainContent() {

        JPanel main = new JPanel(new BorderLayout(15, 15));

        main.setBackground(BACKGROUND);

        main.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // HEADER
        JPanel header = new JPanel(new BorderLayout());

        header.setOpaque(false);

        JLabel title = new JLabel("Student Dashboard");

        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(NAVY);

        JLabel welcome = new JLabel("Student Management System");

        welcome.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel headerText = new JPanel(new GridLayout(2, 1));

        headerText.setOpaque(false);

        headerText.add(title);
        headerText.add(welcome);

        // TOMBOL CETAK LAPORAN
        JButton btnCetak = new JButton("Cetak Laporan");

        btnCetak.setBackground(BLUE);
        btnCetak.setForeground(WHITE);

        btnCetak.setFocusPainted(false);

        btnCetak.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        btnCetak.setPreferredSize(
                new Dimension(150, 40)
        );

        btnCetak.addActionListener(e -> cetakLaporan());

        header.add(headerText, BorderLayout.CENTER);
        header.add(btnCetak, BorderLayout.EAST);

        // STATISTIC CARDS
        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));

        cards.setOpaque(false);

        cards.add(createCard("TOTAL MAHASISWA", "0"));
        cards.add(createCard("PROGRAM STUDI", "4"));
        cards.add(createCard("STATUS SISTEM", "ONLINE"));

        JPanel top = new JPanel(new BorderLayout(0, 20));

        top.setOpaque(false);

        top.add(header, BorderLayout.NORTH);
        top.add(cards, BorderLayout.CENTER);

        // CONTENT
        JPanel content = new JPanel(new BorderLayout(15, 0));

        content.setOpaque(false);

        // FORM SCROLL
        JScrollPane formScroll = new JScrollPane(createForm());

        formScroll.setPreferredSize(new Dimension(330, 450));

        formScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        formScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        formScroll.setBorder(null);

        content.add(formScroll, BorderLayout.WEST);

        content.add(createTable(), BorderLayout.CENTER);

        main.add(top, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    // ==========================================
    // STATISTIC CARD
    // ==========================================

    private JPanel createCard(String title, String value) {

        JPanel card = new JPanel(new BorderLayout(5, 10));

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 240)
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        JLabel lblTitle = new JLabel(title);

        lblTitle.setForeground(Color.GRAY);

        lblTitle.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JLabel lblValue = new JLabel(value);

        lblValue.setForeground(BLUE);

        lblValue.setFont(
                new Font("Arial", Font.BOLD, 25)
        );

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        if (title.equals("TOTAL MAHASISWA")) {

            lblTotal = lblValue;
        }

        return card;
    }

    // ==========================================
    // FORM MAHASISWA
    // ==========================================

    private JPanel createForm() {

        JPanel form = new JPanel();

        form.setPreferredSize(new Dimension(310, 650));

        form.setBackground(WHITE);

        form.setLayout(new GridLayout(0, 1, 5, 5));

        form.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 240)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 18, 15, 18
                        )
                )
        );

        JLabel title = new JLabel("FORM MAHASISWA");

        title.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        title.setForeground(NAVY);

        txtNim = new JTextField();
        txtNama = new JTextField();
        txtEmail = new JTextField();
        txtTelepon = new JTextField();

        cmbGender = new JComboBox<>(
                new String[]{
                        "Laki-laki",
                        "Perempuan"
                }
        );

        cmbJurusan = new JComboBox<>(
                new String[]{
                        "Teknik Informatika",
                        "Sistem Informasi",
                        "Manajemen",
                        "Akuntansi"
                }
        );

        form.add(title);

        addField(form, "NIM", txtNim);
        addField(form, "Nama Lengkap", txtNama);
        addField(form, "Jenis Kelamin", cmbGender);
        addField(form, "Program Studi", cmbJurusan);
        addField(form, "Email", txtEmail);
        addField(form, "Nomor Telepon", txtTelepon);

        // TOMBOL CRUD
        JPanel buttonPanel = new JPanel(
                new GridLayout(2, 2, 8, 8)
        );

        buttonPanel.setBackground(WHITE);

        buttonPanel.setPreferredSize(
                new Dimension(270, 75)
        );

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnReset = new JButton("Reset");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUbah);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);

        form.add(buttonPanel);

        // ACTION BUTTON
        btnTambah.addActionListener(e -> addStudent());
        btnUbah.addActionListener(e -> updateStudent());
        btnHapus.addActionListener(e -> deleteStudent());
        btnReset.addActionListener(e -> clearForm());

        return form;
    }

    // LABEL DAN INPUT
    private void addField(
            JPanel panel,
            String label,
            JComponent field
    ) {

        JLabel lbl = new JLabel(label);

        lbl.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        panel.add(lbl);

        field.setPreferredSize(
                new Dimension(250, 30)
        );

        panel.add(field);
    }

    // ==========================================
    // TABEL MAHASISWA
    // ==========================================

    private JPanel createTable() {

        JPanel panel = new JPanel(new BorderLayout(0, 10));

        panel.setBackground(WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 240)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel title = new JLabel("DATA MAHASISWA");

        title.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        title.setForeground(NAVY);

        String[] columns = {
                "ID",
                "NIM",
                "NAMA",
                "GENDER",
                "JURUSAN",
                "EMAIL",
                "TELEPON"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {

                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(32);

        table.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        table.getTableHeader().setBackground(NAVY);

        table.getTableHeader().setForeground(WHITE);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        // PILIH DATA DARI TABEL
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = table.getSelectedRow();

                if (row >= 0) {

                    selectedId = Integer.parseInt(
                            model.getValueAt(row, 0).toString()
                    );

                    txtNim.setText(
                            model.getValueAt(row, 1).toString()
                    );

                    txtNama.setText(
                            model.getValueAt(row, 2).toString()
                    );

                    cmbGender.setSelectedItem(
                            model.getValueAt(row, 3)
                    );

                    cmbJurusan.setSelectedItem(
                            model.getValueAt(row, 4)
                    );

                    txtEmail.setText(
                            model.getValueAt(row, 5).toString()
                    );

                    txtTelepon.setText(
                            model.getValueAt(row, 6).toString()
                    );
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        panel.add(title, BorderLayout.NORTH);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // LOAD DATA
    // ==========================================

    private void loadStudents() {

        model.setRowCount(0);

        List<Student> students = dao.getAllStudents();

        for (Student student : students) {

            model.addRow(new Object[]{
                    student.getId(),
                    student.getNim(),
                    student.getNama(),
                    student.getJenisKelamin(),
                    student.getJurusan(),
                    student.getEmail(),
                    student.getNoTelepon()
            });
        }

        lblTotal.setText(
                String.valueOf(students.size())
        );
    }

    // ==========================================
    // TAMBAH DATA
    // ==========================================

    private void addStudent() {

        if (!validateInput()) {
            return;
        }

        Student student = getStudentFromForm();

        if (dao.addStudent(student)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Data mahasiswa berhasil ditambahkan!"
            );

            loadStudents();

            clearForm();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menambahkan data mahasiswa. Periksa NIM dan koneksi database."
            );
        }
    }

    // ==========================================
    // UBAH DATA
    // ==========================================

    private void updateStudent() {

        if (selectedId == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data mahasiswa dari tabel terlebih dahulu!"
            );

            return;
        }

        if (!validateInput()) {
            return;
        }

        Student student = getStudentFromForm();

        student.setId(selectedId);

        if (dao.updateStudent(student)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Data mahasiswa berhasil diperbarui!"
            );

            loadStudents();

            clearForm();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memperbarui data mahasiswa."
            );
        }
    }

    // ==========================================
    // HAPUS DATA
    // ==========================================

    private void deleteStudent() {

        if (selectedId == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data mahasiswa yang ingin dihapus!"
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (dao.deleteStudent(selectedId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data mahasiswa berhasil dihapus!"
                );

                loadStudents();

                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Gagal menghapus data mahasiswa."
                );
            }
        }
    }

    // ==========================================
    // AMBIL DATA FORM
    // ==========================================

    private Student getStudentFromForm() {

        return new Student(
                selectedId,
                txtNim.getText().trim(),
                txtNama.getText().trim(),
                cmbGender.getSelectedItem().toString(),
                cmbJurusan.getSelectedItem().toString(),
                txtEmail.getText().trim(),
                txtTelepon.getText().trim(),
                ""
        );
    }

    // ==========================================
    // VALIDASI
    // ==========================================

    private boolean validateInput() {

        if (txtNim.getText().trim().isEmpty()
                || txtNama.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()
                || txtTelepon.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Semua kolom harus diisi!"
            );

            return false;
        }

        return true;
    }

    // ==========================================
    // RESET FORM
    // ==========================================

    private void clearForm() {

        selectedId = 0;

        txtNim.setText("");
        txtNama.setText("");
        txtEmail.setText("");
        txtTelepon.setText("");

        cmbGender.setSelectedIndex(0);
        cmbJurusan.setSelectedIndex(0);

        table.clearSelection();
    }

    // ==========================================
    // CETAK LAPORAN JASPER
    // ==========================================

    private void cetakLaporan() {

        String namaFile = "Laporan_Mahasiswa.pdf";

        File folderDokumen = new File(
                System.getProperty("user.home"),
                "Documents"
        );

        if (!folderDokumen.exists()) {
            folderDokumen.mkdirs();
        }

        File filePDF = new File(
                folderDokumen,
                namaFile
        );

        try (
                InputStream reportStream =
                        getClass().getResourceAsStream(
                                "/reports/laporan_mahasiswa2.jrxml"
                        )
        ) {

            if (reportStream == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "File laporan_mahasiswa2.jrxml tidak ditemukan!\n" +
                                "Periksa folder src/main/resources/reports."
                );

                return;
            }

            // COMPILE REPORT
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(
                            reportStream
                    );

            // KONEKSI DATABASE
            try (Connection connection =
                         DatabaseConnection.getConnection()) {

                if (connection == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Koneksi database gagal!"
                    );

                    return;
                }

                // PARAMETER REPORT
                HashMap<String, Object> parameters =
                        new HashMap<>();

                // ISI REPORT
                JasperPrint jasperPrint =
                        JasperFillManager.fillReport(
                                jasperReport,
                                parameters,
                                connection
                        );

                // EXPORT PDF
                JasperExportManager.exportReportToPdfFile(
                        jasperPrint,
                        filePDF.getAbsolutePath()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Laporan berhasil dibuat!\n\nLokasi file:\n"
                                + filePDF.getAbsolutePath()
                );

                // BUKA PDF
                if (Desktop.isDesktopSupported()) {

                    Desktop.getDesktop().open(filePDF);

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "PDF berhasil dibuat, tetapi tidak dapat dibuka otomatis."
                    );
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencetak laporan:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}