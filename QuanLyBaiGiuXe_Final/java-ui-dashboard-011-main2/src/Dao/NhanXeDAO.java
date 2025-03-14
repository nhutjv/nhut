/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Entity_.DangKyVeThang;
import Entity_.KhachHang1;
import Entity_.NhanVien1;
import Entity_.NhanXe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.JdbcHelper;
import java.time.LocalTime;
import java.sql.Time;

/**
 *
 * @author liduo
 */
public class NhanXeDAO extends QuanLyGiuXe_DAO<NhanXe, String> {

    String INSERT_SQL = "INSERT INTO PhieuGuiXe ( MaPhieuXe, MaTheTu, KhuVuc, AnhTruoc, AnhSau, MaND, BienSo) values (?,?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE PhieuGuiXe SET MaTheTu = ?, "
            + " KhuVuc = ?, AnhTruoc = ?, AnhSau = ?, MaND = ?, BienSo = ?"
            + " WHERE MaPhieuXe = ?";
    String DELETE_SQL = "DELETE FROM PhieuGuiXe WHERE MaPhieuXe = ?";
    String SELECT_ALL_SQL = "SELECT MaPhieuXe, TheTu.MaTheTu, NgayNhan, GioNhan, KhuVuc, "
            + "AnhTruoc, AnhSau, MaND, BienSo, LoaiXe.TenLoaiXe "
            + "FROM PhieuGuiXe "
            + "INNER JOIN TheTu ON PhieuGuiXe.MaTheTu = TheTu.MaTheTu "
            + "INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe ";

    String SELECT_BY_ID_SQL = "SELECT MaPhieuXe, TheTu.MaTheTu, NgayNhan, GioNhan, KhuVuc, "
            + "AnhTruoc, AnhSau, MaND, BienSo, LoaiXe.TenLoaiXe "
            + "FROM PhieuGuiXe "
            + "INNER JOIN TheTu ON PhieuGuiXe.MaTheTu = TheTu.MaTheTu "
            + "INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe "
            + "WHERE PhieuGuiXe.MaPhieuXe = ? ";

    public List<String> selectKhuVucNames() {
        String SELECT_KHUVUC = "SELECT TenKV FROM KhuVuc";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JdbcHelper.query(SELECT_KHUVUC);
            while (rs.next()) {
                String tenKhuVuc = rs.getString("TenKV");
                list.add(tenKhuVuc);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public void insert(NhanXe entity) {
        JdbcHelper.update(INSERT_SQL,
                entity.getMaPhieu(),
                entity.getMaTheTu(),
                entity.getKhuVuc(),
                entity.getAnhTruoc(),
                entity.getAnhSau(),
                entity.getMaND(),
                entity.getBienSo());
    }

    @Override
    public void update(NhanXe entity) {
        JdbcHelper.update(UPDATE_SQL,
                entity.getMaTheTu(),
                entity.getKhuVuc(),
                entity.getAnhTruoc(),
                entity.getAnhSau(),
                entity.getMaND(),
                entity.getBienSo(),
                entity.getMaPhieu());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<NhanXe> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NhanXe selectById(String id) {
        List<NhanXe> list = selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhanXe> selectBySql(String sql, Object... args) {
        List<NhanXe> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                NhanXe entity = new NhanXe();
                entity.setMaPhieu(rs.getString("MaPhieuXe"));
                entity.setMaTheTu(rs.getString("MaTheTu"));
                entity.setNgayNhan(rs.getDate("NgayNhan"));
                entity.setGioNhan(rs.getTime("GioNhan"));
                entity.setKhuVuc(rs.getString("KhuVuc"));
                entity.setAnhTruoc(rs.getString("AnhTruoc"));
                entity.setAnhSau(rs.getString("AnhSau"));
                entity.setMaND(rs.getString("MaND"));
                entity.setBienSo(rs.getString("BienSo"));
                entity.setLoaiXe(rs.getString("TenLoaiXe"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        return list;
    }

    public List<NhanXe> selectByKeyWord(String keyword) {
        String sql = "SELECT MaPhieuXe, TheTu.MaTheTu, NgayNhan, GioNhan, KhuVuc, "
                + "AnhTruoc, AnhSau, MaND, BienSo, LoaiXe.TenLoaiXe "
                + "FROM PhieuGuiXe "
                + "INNER JOIN TheTu ON PhieuGuiXe.MaTheTu = TheTu.MaTheTu "
                + "INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe "
                // + "INNER JOIN KhuVuc ON PhieuGuiXe.KhuVuc = KhuVuc.MaKV "
                + "WHERE BienSo LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");

    }

    public String findMaKHByMaTTUInCurrentDAO(String maTheTu) {
        String SELECT_MA_KHACHHANG_SQL = "SELECT TheThang.MaKH"
                + " FROM TheTu "
                + " INNER JOIN TheThang ON TheTu.MaTheTu = TheThang.MaTheTu "
                + " WHERE TheThang.MaTheTu = ?";

        try {
            ResultSet rs = JdbcHelper.query(SELECT_MA_KHACHHANG_SQL, maTheTu);
            if (rs.next()) {
                return rs.getString("MaKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy
    }

    public String findTenKHByMaTTUInCurrentDAO(String maTheTu) {
        String SELECT_TEN_KHACHHANG_SQL = "SELECT KhachHang.TenKH"
                + " FROM TheTu "
                + " INNER JOIN TheThang ON TheTu.MaTheTu = TheThang.MaTheTu "
                + " INNER JOIN KhachHang ON TheThang.MaKH = KhachHang.MaKH "
                + " WHERE TheThang.MaTheTu = ?";

        try {
            ResultSet rs = JdbcHelper.query(SELECT_TEN_KHACHHANG_SQL, maTheTu);
            if (rs.next()) {
                return rs.getString("TenKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy
    }

    public String findBienSoByMaTTUInCurrentDAO(String maTheTu) {
        String SELECT_BIENSO_SQL = "SELECT TheThang.BienSoXe"
                + " FROM TheTu "
                + " INNER JOIN TheThang ON TheTu.MaTheTu = TheThang.MaTheTu "
                + " WHERE TheThang.MaTheTu = ?";

        try {
            ResultSet rs = JdbcHelper.query(SELECT_BIENSO_SQL, maTheTu);
            if (rs.next()) {
                return rs.getString("BienSoXe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy
    }

    public String findLoaiXeByMaTTUInCurrentDAO(String maTheTu) {
        String SELECT_LOAIXE_SQL = "SELECT LoaiXe.TenLoaiXe"
                + " FROM TheTu "
                + " INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe "
                + " WHERE TheTu.MaTheTu = ?";

        try {
            ResultSet rs = JdbcHelper.query(SELECT_LOAIXE_SQL, maTheTu);
            if (rs.next()) {
                return rs.getString("TenLoaiXe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy
    }

}
