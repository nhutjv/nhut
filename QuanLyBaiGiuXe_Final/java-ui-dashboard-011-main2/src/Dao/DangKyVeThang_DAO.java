/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Entity_.DangKyVeThang;
import utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NHUT
 */
public class DangKyVeThang_DAO extends QuanLyGiuXe_DAO<DangKyVeThang, String> {

    String INSERT_SQL = "INSERT INTO TheThang (MaTheThang, MaKH, DonGia, NgayDangKy, NgayKetThuc,BienSoXe, MaTheTu) VALUES ( ?,?, ?, ?, ?, ?,?)";
    String DELETE_SQL = "DELETE FROM TheThang WHERE MaTheThang = ?";

    String UPDATE_SQL = "UPDATE TheThang "
            + "SET MaKH = ?, DonGia = ?, NgayDangKy = ?, NgayKetThuc = ?,BienSoXe = ?, MaTheTu = ? "
            + "WHERE MaTheThang = ?";

    String SELECT_ALL_SQL = "SELECT TheThang.*, KhachHang.TenKH "
            + "FROM TheThang "
            + "INNER JOIN KhachHang ON TheThang.MaKH = KhachHang.MaKH";
    String SELECT_BY_ID_SQL = "SELECT TheThang.*, KhachHang.TenKH "
            + "FROM TheThang "
            + "INNER JOIN KhachHang ON TheThang.MaKH = KhachHang.MaKH "
            + "WHERE TheThang.MaTheThang = ?";

    public List<DangKyVeThang> selectByKeyWord(String keyword) {
        String sql = "SELECT TheThang.*, KhachHang.TenKH "
                + "FROM TheThang "
                + "INNER JOIN KhachHang ON TheThang.MaKH = KhachHang.MaKH "
                + "WHERE TheThang.BienSoXe LIKE ? OR TheThang.MaKH LIKE ? OR TheThang.MaTheThang LIKE ? OR TheThang.NgayDangKy LIKE ? OR TheThang.DonGia LIKE ? OR KhachHang.TenKH LIKE ?";

        return this.selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%");
    }

    //cbo
    public List<String> selectLoaiXeNames() {
        String SELECT_LOAIXE = "SELECT TenLoaiXe FROM LoaiXe";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JdbcHelper.query(SELECT_LOAIXE);
            while (rs.next()) {
                String tenLoaiXe = rs.getString("TenLoaiXe");
                list.add(tenLoaiXe);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public void insert(DangKyVeThang entity) {

        String SELECT_AUTO_ID = "SELECT dbo.AUTO_IDTheThang10() AS NewMaTheThang";
        try {
            ResultSet rs = JdbcHelper.query(SELECT_AUTO_ID);
            System.out.println(SELECT_AUTO_ID);
            if (rs.next()) {
                String newMaTheThang = rs.getString("NewMaTheThang");
                JdbcHelper.update(INSERT_SQL, newMaTheThang, entity.getMaKhachHang(), entity.getDonGia(), entity.getNgayDangKy(), entity.getNgayKetThuc(), entity.getBienSoXe(), entity.getMaTheTu());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(DangKyVeThang entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getMaKhachHang(), entity.getDonGia(),
                entity.getNgayDangKy(), entity.getNgayKetThuc(), entity.getMaTheTu(), entity.getBienSoXe(), entity.getMaTheThang());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<DangKyVeThang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public DangKyVeThang selectById(String maTheThang) {
        List<DangKyVeThang> list = selectBySql(SELECT_BY_ID_SQL, maTheThang);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DangKyVeThang> selectBySql(String sql, Object... args) {
        List<DangKyVeThang> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                DangKyVeThang entity = new DangKyVeThang();
                entity.setMaTheThang(rs.getString("MaTheThang"));
                entity.setMaKhachHang(rs.getString("MaKH"));
                entity.setDonGia(rs.getDouble("DonGia"));
                entity.setNgayDangKy(rs.getDate("NgayDangKy"));
                entity.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                // Thêm thông tin từ bảng KhachHang
                entity.setBienSoXe(rs.getString("BienSoXe"));
                entity.setTenKhachHang(rs.getString("TenKH"));
                entity.setMaTheTu(rs.getString("MaTheTu"));
                list.add(entity);
            }
        } catch (Exception e) {

        }
        return list;

    }

    public String findTenKhachHangByMaKhachHangInCurrentDAO(String maKhachHang) {
        String SELECT_TEN_KHACH_HANG_SQL = "SELECT TenKH FROM KhachHang WHERE MaKH = ?";

        try {
            ResultSet rs = JdbcHelper.query(SELECT_TEN_KHACH_HANG_SQL, maKhachHang);
            if (rs.next()) {
                return rs.getString("TenKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy
    }

}
