/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Entity_.NhanVien1;
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
public class NhanVienDAO extends QuanLyGiuXe_DAO<NhanVien1, String> {

    String INSERT_SQL = "insert into NguoiDung (MaND,TenND,MatKhau,VaiTro,Email) values (?,?,?,?,?)";
    String UPDATE_SQL = "update NguoiDung set  TenND = ?,MatKhau = ?, VaiTro = ?,Email = ? where MaND = ?";
    String DELETE_SQL = "delete from NguoiDung where MaND = ?";
    String SELECT_ALL_SQL = "select * from NguoiDung";
    String SELECT_BY_ID_SQL = "select * from NguoiDung where MaND = ?";

    @Override
    public void insert(NhanVien1 entity) {
        String SELECT_AUTO_ID = "SELECT dbo.AUTO_IDNguoiDung11() AS NewMa";
        ResultSet rs = JdbcHelper.query(SELECT_AUTO_ID);
        try {
            if (rs.next()) {
                String newMa = rs.getString("NewMa");
                JdbcHelper.update(INSERT_SQL, newMa, entity.getHoTen(), entity.getMatKhau(), entity.isVaiTro(), entity.getEmail());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(NhanVien1 entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getHoTen(), entity.getMatKhau(), entity.isVaiTro(), entity.getEmail(), entity.getMaND());

    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<NhanVien1> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NhanVien1 selectById(String id) {
        List<NhanVien1> list = selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhanVien1> selectBySql(String sql, Object... args) {
        List<NhanVien1> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                NhanVien1 entity = new NhanVien1();
                entity.setMaND(rs.getString("MaND"));
                entity.setHoTen(rs.getString("TenND"));
                entity.setMatKhau(rs.getString("MatKhau"));
                entity.setEmail(rs.getString("Email"));
                entity.setVaiTro(rs.getBoolean("VaiTro"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<NhanVien1> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM NguoiDung WHERE MaND LIKE ? OR TenND LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%");
    }

}
