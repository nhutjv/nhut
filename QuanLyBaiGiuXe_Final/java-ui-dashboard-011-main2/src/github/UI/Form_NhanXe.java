/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package github.UI;

import Dao.NhanXeDAO;
import Entity_.DangKyVeThang;
import Entity_.KhachHang1;
import Entity_.NhanVien1;
import Entity_.NhanXe;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import utils.Auth;
import utils.MsgBox;
import utils.XDate;
import utils.XImage;

/**
 *
 * @author NHUT
 */
public class Form_NhanXe extends javax.swing.JPanel {

    JFileChooser filechooser = new JFileChooser();
    NhanXeDAO dao = new NhanXeDAO();
    int row = -1;

    public Form_NhanXe() {
        initComponents();
        init();
        
        
        Timer timer;
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.Date date = new java.util.Date();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String time = timeFormat.format(date);
                txtGioNhan.setText(time);
            }
        };
        timer = new Timer(1000, actionListener);
        timer.setInitialDelay(0);
        timer.start();
    }

    void init() {
        this.fillTable();
        this.fillComboboxKhuVuc();
        txtMaNNX.setEditable(false);
        this.row = -1;
        this.updateStatus();
    }

    void setForm(Entity_.NhanXe nx) {
        txtMaPhieuXe.setText(nx.getMaPhieu());
        txtMaTheTu.setText(nx.getMaTheTu());
        txtNgayNhan.setText(XDate.toString(nx.getNgayNhan(), "dd-MM-yyyy"));
        // Kiểm tra xem gioNhan có giá trị không trước khi đặt vào txtGioNhan
        if (nx.getGioNhan() != null) {
            // Chuyển đổi gioNhan từ java.sql.Time sang LocalTime
            LocalTime gioNhanLocalTime = nx.getGioNhan().toLocalTime();

            // Định dạng gioNhan thành chuỗi "HH:mm:ss"
            String formattedGioNhan = DateTimeFormatter.ofPattern("HH:mm:ss").format(gioNhanLocalTime);

            txtGioNhan.setText(formattedGioNhan);
        } else {
            txtGioNhan.setText(""); // Đặt giá trị mặc định nếu gioNhan là null
        }
        txtMaKV.setText(nx.getKhuVuc());
        if (nx.getAnhTruoc() != null) {
            ImageIcon imageIcon = XImage.read(nx.getAnhTruoc());
            Image image = imageIcon.getImage();
            lblAnhTruoc.setToolTipText(nx.getAnhTruoc());
            lblAnhTruoc.setIcon(XImage.read(nx.getAnhTruoc()));
            //set kích thước ảnh
            int width = 180; // Đặt chiều rộng mong muốn
            int height = 160; // Đặt chiều cao mong muốn
            lblAnhTruoc.setHorizontalAlignment(lblAnhTruoc.CENTER); // Đặt canh giữa theo chiều ngang
            lblAnhTruoc.setVerticalAlignment(lblAnhTruoc.CENTER); // Đặt canh giữa theo chiều dọc

            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageIcon.setImage(resizedImage);

            lblAnhTruoc.setIcon(imageIcon);
        }
        if (nx.getAnhSau() != null) {
            ImageIcon imageIcon = XImage.read(nx.getAnhSau());
            Image image = imageIcon.getImage();
            lblAnhSau.setToolTipText(nx.getAnhTruoc());
            lblAnhSau.setIcon(XImage.read(nx.getAnhSau()));
            //set kích thước ảnh
            int width = 180; // Đặt chiều rộng mong muốn
            int height = 160; // Đặt chiều cao mong muốn
            lblAnhSau.setHorizontalAlignment(lblAnhSau.CENTER); // Đặt canh giữa theo chiều ngang
            lblAnhSau.setVerticalAlignment(lblAnhSau.CENTER); // Đặt canh giữa theo chiều dọc

            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageIcon.setImage(resizedImage);

            lblAnhSau.setIcon(imageIcon);
        }
        txtMaNNX.setText(nx.getMaND());
        txtBienSo.setText(nx.getBienSo());
        txtLoaiXe.setText(nx.getLoaiXe());

    }

    NhanXe getForm() {//tạo phiếu gửi xe từ from
        NhanXe nx = new NhanXe();
        nx.setMaPhieu(txtMaPhieuXe.getText());
        nx.setMaTheTu(txtMaTheTu.getText());
        nx.setKhuVuc(txtMaKV.getText());
        nx.setAnhTruoc(lblAnhTruoc.getToolTipText());
        nx.setAnhSau(lblAnhSau.getToolTipText());
        if (txtMaNNX.getText().equals("")) {
            nx.setMaND(Auth.user.getMaND());
        } else {
            nx.setMaND(txtMaNNX.getText());
        }
        nx.setBienSo(txtBienSo.getText());

        return nx;
    }

    void fillComboboxKhuVuc() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cboKhuVuc.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<String> list = dao.selectKhuVucNames();
            for (String kv : list) {
                comboBoxModel.addElement(kv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        cboKhuVuc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xác định giá trị được chọn
                String selectedValue = cboKhuVuc.getSelectedItem().toString();

                if ("A".equalsIgnoreCase(selectedValue)) {
                    txtMaKV.setText("KV01");
                } else if ("B".equalsIgnoreCase(selectedValue)) {
                    txtMaKV.setText("KV02");
                } else if ("C".equalsIgnoreCase(selectedValue)) {
                    txtMaKV.setText("KV03");
                } else if ("D".equalsIgnoreCase(selectedValue)) {
                    txtMaKV.setText("KV04");
                } else {
                    txtMaKV.setText("KV05");
                }
            }
        });

    }

    void edit() {
        try {
            String maPN = (String) tblNhanXe.getValueAt(this.row, 0);
            Entity_.NhanXe nx = dao.selectById(maPN);
            this.setForm(nx);
            this.updateStatus();
        } catch (Exception e) {
            System.out.println("khong the fill len form");
            e.printStackTrace();
        }
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblNhanXe.getModel();
        model.setRowCount(0);
        try {
            List<Entity_.NhanXe> list = dao.selectAll();
            for (Entity_.NhanXe nx : list) {
                Object[] row = {
                    nx.getMaPhieu(),
                    nx.getMaTheTu(),
                    nx.getNgayNhan(),
                    nx.getGioNhan(),
                    nx.getKhuVuc(),
                    nx.getMaND(),
                    nx.getBienSo(),
                    nx.getLoaiXe()
                };

                model.addRow(row);
            }
        } catch (Exception e) {

            MsgBox.alert(this, "lỗi truy vấn dữ liệu!");
        }
    }

    void chonAnhTruoc() {
        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = filechooser.getSelectedFile();
                XImage.save(file);
                ImageIcon icon = XImage.read(file.getName());
                Image img = ImageIO.read(file);
                lblAnhTruoc.setIcon(new ImageIcon(img.getScaledInstance(170, 110, Image.SCALE_DEFAULT)));
                lblAnhTruoc.setToolTipText(file.getName());
            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi khi chọn ảnh!");
            }
        }
    }

    void chonAnhSau() {
        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = filechooser.getSelectedFile();
                XImage.save(file);
                ImageIcon icon = XImage.read(file.getName());
                Image img = ImageIO.read(file);
                lblAnhSau.setIcon(new ImageIcon(img.getScaledInstance(170, 110, Image.SCALE_DEFAULT)));
                lblAnhSau.setToolTipText(file.getName());
            } catch (Exception e) {
                MsgBox.alert(this, "Lỗi khi chọn ảnh!");
            }
        }
    }

    void insert() {

        NhanXe nx = getForm();
        try {
            dao.insert(nx);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thêm mới thất bại!");
        }

    }

    void update() {
        NhanXe nx = getForm();
        try {
            dao.update(nx);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "sửa thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "sửa thất bại!");

        }
    }

    void delete() {
        String mapx = txtMaPhieuXe.getText();
        try {
            dao.delete(mapx);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "xóa thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "xóa thất bại rồi!");
        }

    }

    void clearForm() {
        NhanXe nx = new NhanXe();
        this.txtMaPhieuXe.setText("");
        this.txtMaTheTu.setText("");
        this.txtNgayNhan.setText("");
        this.txtMaKV.setText("");
        this.txtMaNNX.setText("");
        this.txtBienSo.setText("");
        this.txtTimKiem.setText("");
        this.lblAnhTruoc.setIcon(null);
        this.lblAnhSau.setIcon(null);
        this.txtLoaiXe.setText("");
        this.txtMaKH.setText("");
        this.txtTenKH.setText("");
        this.txtGioNhan.setText("");
        this.row = -1;
        this.updateStatus();
        fillTable();
    }

    private int getIndexByBienSo(String bienSo) {
        for (int i = 0; i < tblNhanXe.getRowCount(); i++) {
            if (bienSo.equals(tblNhanXe.getValueAt(i, 0).toString())) {
                return i;
            }
        }
        return -1;
    }

    void first() {
        this.row = 0;
        this.edit();
    }

    void prev() {
        if (this.row > 0) {
            this.row--;
            this.edit();
        }
    }

    void next() {
        if (this.row < tblNhanXe.getRowCount() - 1) {
            this.row++;
            this.edit();
        }
    }

    void last() {
        this.row = tblNhanXe.getRowCount() - 1;
        this.edit();
    }

    void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        txtMaTheTu.setEditable(edit);
        txtBienSo.setEditable(edit);

        txtMaNNX.setEditable(edit);
        txtMaPhieuXe.setEditable(edit);
        txtNgayNhan.setEditable(edit);
        btnNhan.setEnabled(edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
        txtLoaiXe.setEditable(edit);
        lblAnhTruoc.setText("");
//
//        btnFirst.setEnabled(edit && !first);
//        btnPrev.setEnabled(edit && !first);
//        btnNext.setEnabled(edit && !last);
//        btnLast.setEnabled(edit && !last);

    }

//    void setForm(entity.NhanXe nx) {
//        txtMaPhieuXe.setText(nx.getMaPhieu());
//        txtMaTheTu.setText(nx.getMaTheTu());
//        txtNgayNhan.setText(XDate.toString(nx.getNgayNhan(), "dd-MM-yyyy"));
//        txtKhuVuc.setText(nx.getKhuVuc());
//        if (nx.getAnhTruoc() != null) {
//            ImageIcon imageIcon = XImage.read(nx.getAnhTruoc());
//            Image image = imageIcon.getImage();
//            lblAnhTruoc.setToolTipText(nx.getAnhTruoc());
//            lblAnhTruoc.setIcon(XImage.read(nx.getAnhTruoc()));
//            //set kích thước ảnh
//            int width = 170; // Đặt chiều rộng mong muốn
//            int height = 150; // Đặt chiều cao mong muốn
//            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//            imageIcon.setImage(resizedImage);
//
//            lblAnhTruoc.setIcon(imageIcon);
//        }
//
//        if (nx.getAnhSau() != null) {
//            ImageIcon imageIcon = XImage.read(nx.getAnhSau());
//            Image image = imageIcon.getImage();
//            lblAnhSau.setToolTipText(nx.getAnhTruoc());
//            lblAnhSau.setIcon(XImage.read(nx.getAnhSau()));
//            //set kích thước ảnh
//            int width = 170; // Đặt chiều rộng mong muốn
//            int height = 150; // Đặt chiều cao mong muốn
//            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//            imageIcon.setImage(resizedImage);
//
//            lblAnhSau.setIcon(imageIcon);
//        }
//        txtMaNNX.setText(nx.getMaND());
//        txtBienSo.setText(nx.getBienSo());
//        txtLoaiXe.setText(nx.getLoaiXe());
//
//    }
//
//    NhanXe getForm() {//tạo phiếu gửi xe từ from
//        NhanXe nx = new NhanXe();
//        nx.setMaPhieu(txtMaPhieuXe.getText());
//        nx.setMaTheTu(txtMaTheTu.getText());
//        nx.setNgayNhan(XDate.toDate(txtNgayNhan.getText(), "dd-MM-yyyy"));
//        nx.setKhuVuc(txtKhuVuc.getText());
//        nx.setAnhTruoc(lblAnhTruoc.getToolTipText());
//        nx.setAnhSau(lblAnhSau.getToolTipText());
//        nx.setMaND(txtMaNNX.getText());
//        nx.setBienSo(txtBienSo.getText());
//        nx.setLoaiXe(txtLoaiXe.getText());
//
//        return nx;
//    }
//    void updateStatus() {
////        boolean edit = (this.row >= 0);
////        boolean first = (this.row == 0);
////        txtMaKH.setEditable(!edit);
////        btnThem.setEnabled(!edit);
////        btnSua.setEnabled(edit);
////        btnXoa.setEnabled(edit);
////
////        btnFirst.setEnabled(edit && !first);
////        btnPrev.setEnabled(edit && !first);
////        btnNext.setEnabled(edit && !last);
////        btnLast.setEnabled(edit && !last);
//
//    }
//    void edit() {
//        try {
//            String maPN = (String) tblNhanXe.getValueAt(this.row, 0);
//            entity.NhanXe nx = dao.selectById(maPN);
//            this.setForm(nx);
//            this.updateStatus();
//        } catch (Exception e) {
//            System.out.println("khoong the fill len from");
//            e.printStackTrace();
//        }
//    }
//
//    void fillTable() {
//        DefaultTableModel model = (DefaultTableModel) tblNhanXe.getModel();
//        model.setRowCount(0);
//        try {
//            List<entity.NhanXe> list = dao.selectAll();
//            for (entity.NhanXe nx : list) {
//                Object[] row = {
//                    nx.getMaPhieu(),
//                    nx.getMaTheTu(),
//                    nx.getNgayNhan(),
//                    nx.getKhuVuc(),
//                    nx.getMaND(),
//                    nx.getBienSo(),
//                    nx.getLoaiXe()
//                };
//
//                model.addRow(row);
//            }
//        } catch (Exception e) {
//
//            MsgBox.alert(this, "lỗi truy vấn dữ liệu!");
//        }
//    }
//
//    void chonAnhTruoc() {
//        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//            try {
//                File file = filechooser.getSelectedFile();
//                XImage.save(file);
//                ImageIcon icon = XImage.read(file.getName());
//                Image img = ImageIO.read(file);
//                lblAnhTruoc.setIcon(new ImageIcon(img.getScaledInstance(170, 110, Image.SCALE_DEFAULT)));
//                lblAnhTruoc.setToolTipText(file.getName());
//    
//            } catch (Exception e) {
//                MsgBox.alert(this, "Lỗi khi chọn ảnh!");
//            }
//        }
//    }
//
//    void chonAnhSau() {
//        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//            try {
//                File file = filechooser.getSelectedFile();
//                XImage.save(file);
//                ImageIcon icon = XImage.read(file.getName());
//                Image img = ImageIO.read(file);
//                lblAnhSau.setIcon(new ImageIcon(img.getScaledInstance(170, 110, Image.SCALE_DEFAULT)));
//                lblAnhSau.setToolTipText(file.getName());
//            } catch (Exception e) {
//                MsgBox.alert(this, "Lỗi khi chọn ảnh!");
//            }
//        }
//    }
//
//    void insert() {
//
//        NhanXe nx = getForm();
//        String maphieuxe = new String(txtMaPhieuXe.getText());
//
//        try {
//            dao.insert(nx);
//            this.fillTable();
//            this.clearForm();
//            MsgBox.alert(this, "Thêm mới thành công!");
//        } catch (Exception e) {
//            e.printStackTrace();
////                MsgBox.alert(this, "Thêm mới thất bại!");
//        }
//
//    }
//
//    void update() {
//        NhanXe nx = getForm();
//        try {
//            dao.update(nx);
//            this.fillTable();
//            this.clearForm();
//            MsgBox.alert(this, "sửa thành công!");
//        } catch (Exception e) {
//            MsgBox.alert(this, "sửa thất bại!");
//
//        }
//    }
//
//    void delete() {
//        String mapx = txtMaPhieuXe.getText();
//        try {
//            dao.delete(mapx);
//            this.fillTable();
//            this.clearForm();
//            MsgBox.alert(this, "xóa thành công!");
//        } catch (Exception e) {
//            MsgBox.alert(this, "xóa thất bại rồi!");
//        }
//
//    }
//
//    void clearForm() {
//        NhanXe nx = new NhanXe();
//        // this.setForm(nx);
//        boolean edit = (this.row >= 0);
//        boolean first = (this.row == 0);
//        this.txtMaPhieuXe.setText("");
//        this.txtMaTheTu.setText("");
//        this.txtNgayNhan.setText("");
//        this.txtKhuVuc.setText("");
//        this.txtMaNNX.setText("");
//        this.txtBienSo.setText("");
//        this.txtLoaiXe.setText("");
//        txtMaTheTu.setEditable(!edit);
//        txtBienSo.setEditable(!edit);
//        txtKhuVuc.setEditable(!edit);
//        txtMaNNX.setEditable(!edit);
//        txtMaPhieuXe.setEditable(!edit);
//        txtNgayNhan.setEditable(!edit);
//        txtLoaiXe.setEditable(!edit);
//        btnNhan.setEnabled(!edit);
//        btnSua.setEnabled(!edit);
//        btnXoa.setEnabled(!edit);
//        this.row = -1;
////        this.updateStatus();
//        fillTable();
//    }
//
//    private int getIndexByBienSo(String bienSo) {
//        for (int i = 0; i < tblNhanXe.getRowCount(); i++) {
//            if (bienSo.equals(tblNhanXe.getValueAt(i, 0).toString())) {
//                return i;
//            }
//        }
//        return -1; // Trả về -1 nếu không tìm thấy
//    }
//
//    void first() {
//        this.row = 0;
//        this.edit();
//    }
//
//    void prev() {
//        if (this.row > 0) {
//            this.row--;
//            this.edit();
//        }
//    }
//
//    void next() {
//        if (this.row < tblNhanXe.getRowCount() - 1) {
//            this.row++;
//            this.edit();
//        }
//    }
//
//    void last() {
//        this.row = tblNhanXe.getRowCount() - 1;
//        this.edit();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        txtNgayNhan = new javax.swing.JTextField();
        txtMaPhieuXe = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        btnNhan = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblNhanXe = new javax.swing.JTable();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        txtMaTheTu = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtMaNNX = new javax.swing.JTextField();
        lblAnhSau = new javax.swing.JLabel();
        lblAnhTruoc = new javax.swing.JLabel();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtBienSo = new javax.swing.JTextField();
        cboKhuVuc = new javax.swing.JComboBox<>();
        txtMaKV = new javax.swing.JTextField();
        txtMaKH = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtLoaiXe = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtGioNhan = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel17.setPreferredSize(new java.awt.Dimension(1000, 236));

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel45.setText("Mã phiếu xe:");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel46.setText("Ngày nhận:");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel47.setText("Khu vực:");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel48.setText("Mã người nhận xe:");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel50.setText("Mã thẻ từ:");

        btnNhan.setBackground(new java.awt.Color(0, 51, 255));
        btnNhan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhan.setForeground(new java.awt.Color(255, 255, 255));
        btnNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/nhanXe_icon.png"))); // NOI18N
        btnNhan.setText("NHẬN");
        btnNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanActionPerformed(evt);
            }
        });

        tblNhanXe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã phiếu xe", "Mã thẻ từ", "Ngày nhận", "Giờ nhận", "Khu vực", "Mã người nhận ", "Biển số xe", "Loại xe"
            }
        ));
        tblNhanXe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanXeMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblNhanXe);

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel51.setText("Danh sách nhận xe");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel52.setText("Ảnh trước");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel53.setText("Ảnh sau");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Biển Số:");

        btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search_icon.png"))); // NOI18N
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        txtMaTheTu.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtMaTheTuCaretUpdate(evt);
            }
        });
        txtMaTheTu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaTheTuFocusLost(evt);
            }
        });
        txtMaTheTu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaTheTuActionPerformed(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel55.setText("Loại xe:");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel56.setText("Mã khách hàng:");

        lblAnhSau.setBackground(new java.awt.Color(255, 255, 255));
        lblAnhSau.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblAnhSau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhSauMouseClicked(evt);
            }
        });

        lblAnhTruoc.setBackground(new java.awt.Color(255, 255, 255));
        lblAnhTruoc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblAnhTruoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhTruocMouseClicked(evt);
            }
        });

        btnFirst.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnFirst.setText("|<<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPrev.setText("<<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLast.setText(">>|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(0, 51, 255));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/update_icon_menu.png"))); // NOI18N
        btnLamMoi.setText("LÀM MỚI");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(0, 51, 255));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/update_icon.png"))); // NOI18N
        btnSua.setText("SỬA");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(0, 51, 255));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete_icon.png"))); // NOI18N
        btnXoa.setText("XÓA");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        cboKhuVuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel57.setText("Biển số:");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel58.setText("Tên khách hàng:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Giờ nhận:");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane9)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel56)
                                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel46)
                                                    .addComponent(jLabel45))))
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtMaPhieuXe, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtNgayNhan)
                                                        .addComponent(txtMaNNX, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(jLabel58))
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addGap(46, 46, 46)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel1)
                                                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(2, 2, 2))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtGioNhan, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTenKH, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBienSo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaTheTu)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                                        .addComponent(cboKhuVuc, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMaKV, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 59, Short.MAX_VALUE))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(341, 341, 341)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel57)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(lblAnhTruoc, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblAnhSau, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(btnNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel52)
                        .addGap(103, 103, 103)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMaPhieuXe, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaTheTu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel50)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMaNNX, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboKhuVuc, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaKV, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel47))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNgayNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGioNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel48))
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel58)
                                        .addComponent(jLabel56))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(lblAnhSau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAnhTruoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel57)
                                .addComponent(txtBienSo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel55)
                                .addComponent(jLabel52)
                                .addComponent(jLabel53))
                            .addComponent(txtLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel51)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(378, 378, 378)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 1294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanActionPerformed
        insert();
    }//GEN-LAST:event_btnNhanActionPerformed

    private void tblNhanXeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanXeMouseClicked
        if (evt.getClickCount() == 1) {
            this.row = tblNhanXe.getSelectedRow();
            this.edit();
        }
    }//GEN-LAST:event_tblNhanXeMouseClicked

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblNhanXe.getModel();
        model.setRowCount(0);
        String bienSoTimKiem = txtTimKiem.getText();
        List<NhanXe> list = dao.selectByKeyWord(bienSoTimKiem);
        for (NhanXe nx : list) {
            Object[] row = {nx.getMaPhieu(), nx.getMaTheTu(), nx.getNgayNhan(), nx.getGioNhan(), nx.getKhuVuc(), nx.getMaND(), nx.getBienSo(), nx.getLoaiXe()};
            model.addRow(row);
        }

        // Cập nhật trạng thái và làm mới
        updateStatus();
    }//GEN-LAST:event_btnTimActionPerformed

    private void txtMaTheTuCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtMaTheTuCaretUpdate
        String maTheTu = txtMaTheTu.getText();
        if (!maTheTu.isEmpty()) {
            String maKhachHang = dao.findMaKHByMaTTUInCurrentDAO(maTheTu);
            String tenKhachHang = dao.findTenKHByMaTTUInCurrentDAO(maTheTu);
            String bienSoXe = dao.findBienSoByMaTTUInCurrentDAO(maTheTu);
            String loaiXe = dao.findLoaiXeByMaTTUInCurrentDAO(maTheTu);

            txtMaKH.setText(maKhachHang != null ? maKhachHang : "");
            txtTenKH.setText(tenKhachHang != null ? tenKhachHang : "");
            txtBienSo.setText(bienSoXe != null ? bienSoXe : "");
            txtLoaiXe.setText(loaiXe != null ? loaiXe : "");
        } else {
            txtMaKH.setText("");
            txtTenKH.setText("");
            txtBienSo.setText("");
            txtLoaiXe.setText("");
        }

//        String maTheTu = txtMaTheTu.getText();
//        if (!maTheTu.isEmpty()) {
//            String maKhachHang = dao.findMaKHByMaTTUInCurrentDAO(maTheTu);
//            String tenKhachHang = dao.findTenKHByMaTTUInCurrentDAO(maTheTu);
//            String bienSoXe = dao.findBienSoByMaTTUInCurrentDAO(maTheTu);
//            String loaiXe = dao.findLoaiXeByMaTTUInCurrentDAO(maTheTu);
//
//            txtMaKH.setText(maKhachHang != null ? maKhachHang : "");
//            txtTenKH.setText(tenKhachHang != null ? tenKhachHang : "");
//            txtBienSo.setText(bienSoXe != null ? bienSoXe : "");
//            txtLoaiXe.setText(loaiXe != null ? loaiXe : "");
//        } else {
//            txtMaKH.setText("");
//            txtTenKH.setText("");
//            txtBienSo.setText("");
//            txtLoaiXe.setText("");
//        }
    }//GEN-LAST:event_txtMaTheTuCaretUpdate

    private void txtMaTheTuFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaTheTuFocusLost

    }//GEN-LAST:event_txtMaTheTuFocusLost

    private void txtMaTheTuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaTheTuActionPerformed

    }//GEN-LAST:event_txtMaTheTuActionPerformed

    private void lblAnhSauMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhSauMouseClicked
        if (evt.getClickCount() == 1) {
            this.chonAnhSau();
        }
    }//GEN-LAST:event_lblAnhSauMouseClicked

    private void lblAnhTruocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhTruocMouseClicked
        if (evt.getClickCount() == 1) {
            this.chonAnhTruoc();
        }
    }//GEN-LAST:event_lblAnhTruocMouseClicked

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
//        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
//        prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
//        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
//        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNhan;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboKhuVuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblAnhSau;
    private javax.swing.JLabel lblAnhTruoc;
    private javax.swing.JTable tblNhanXe;
    private javax.swing.JTextField txtBienSo;
    private javax.swing.JTextField txtGioNhan;
    private javax.swing.JTextField txtLoaiXe;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKV;
    private javax.swing.JTextField txtMaNNX;
    private javax.swing.JTextField txtMaPhieuXe;
    private javax.swing.JTextField txtMaTheTu;
    private javax.swing.JTextField txtNgayNhan;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

}
