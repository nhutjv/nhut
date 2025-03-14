/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Entity_.NhanXe2;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.JdbcHelper;

/**
 *
 * @author NHUT
 */
public class NhanXe_DAO2 extends QuanLyGiuXe_DAO<NhanXe2, String> {

    //9
    String INSERT_SQL = "INSERT INTO PhieuGuiXe ( MaPhieuXe, MaTheTu, NgayNhan,GioNhan,KhuVuc, AnhTruoc, AnhSau, MaND, BienSo) values (?,?,?,?,?,?,?,?,?)";

//     String UPDATE_SQL = "UPDATE PhieuGuiXe SET MaTheTu = ?, "
//            + " KhuVuc = ?, AnhTruoc = ?, AnhSau = ?, MaND = ?, BienSo = ?"
//            + " WHERE MaPhieuXe = ?";
//    String DELETE_SQL = "DELETE FROM PhieuGuiXe WHERE MaPhieuXe = ?";
//    String SELECT_ALL_SQL = "SELECT MaPhieuXe, TheTu.MaTheTu, NgayNhan, GioNhan, KhuVuc, "
//            + "AnhTruoc, AnhSau, MaND, BienSo, LoaiXe.TenLoaiXe "
//            + "FROM PhieuGuiXe "
//            + "INNER JOIN TheTu ON PhieuGuiXe.MaTheTu = TheTu.MaTheTu "
//            + "INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe ";

String SELECT_ALL_SQL = "SELECT MaPhieuXe, TheTu.MaTheTu, NgayNhan, GioNhan, KhuVuc, "
    + "AnhTruoc, AnhSau, MaND, BienSo, LoaiXe.TenLoaiXe "
    + "FROM PhieuGuiXe "
    + "INNER JOIN TheTu ON PhieuGuiXe.MaTheTu = TheTu.MaTheTu "
    + "INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe";


    
//    String SELECT_BY_ID_SQL = "SELECT MaPhieuXe, TheTu.MaTheTu, NgayNhan, GioNhan, KhuVuc, "
//            + "AnhTruoc, AnhSau, MaND, BienSo, LoaiXe.TenLoaiXe "
//            + "FROM PhieuGuiXe "
//            + "INNER JOIN TheTu ON PhieuGuiXe.MaTheTu = TheTu.MaTheTu "
//            + "INNER JOIN LoaiXe ON TheTu.MaLoaiXe = LoaiXe.MaLoaiXe "
//            + "WHERE PhieuGuiXe.MaPhieuXe = ? ";
    @Override
    public void insert(NhanXe2 entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(NhanXe2 entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<NhanXe2> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NhanXe2 selectById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<NhanXe2> selectBySql(String sql, Object... args) {
        List<NhanXe2> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                NhanXe2 entity = new NhanXe2();
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

}
