/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Entity_.KhachHang1;
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
 * @author liduo
 */
public class KhachHangDAO extends QuanLyGiuXe_DAO<KhachHang1, String> {

    String INSERT_SQL = "insert into KhachHang (MaKH, TenKH, CCCD, SDT) values (?,?,?,?)";
    String UPDATE_SQL = "update KhachHang set TenKH = ?, CCCD = ?, SDT = ? where MaKH = ?";
    String DELETE_SQL = "delete from KhachHang where MaKH = ?";
    String SELECT_ALL_SQL = "select * from KhachHang";
    String SELECT_BY_ID_SQL = "select * from KhachHang where MaKH = ?";

    public List<KhachHang1> searchKhachHang(String keyword) {
    String sql = "SELECT * FROM KhachHang WHERE MaKH LIKE ? OR TenKH LIKE ? OR CCCD LIKE ? OR SDT LIKE ?";
    return selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%");
}

    
    
    @Override
    public void insert(KhachHang1 entity) {
        String SELECT_AUTO_ID = "SELECT dbo.AUTO_IDKhachHang1() AS NewMa";
        ResultSet rs = JdbcHelper.query(SELECT_AUTO_ID);
        System.out.println(SELECT_AUTO_ID);
        try {
            if (rs.next()) {
                String newMaTheThang = rs.getString("NewMa");
                JdbcHelper.update(INSERT_SQL,
                        newMaTheThang,
                        entity.getTenKH(),
                        entity.getCCCD(),
                        entity.getSDT());
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void update(KhachHang1 entity) {
        JdbcHelper.update(UPDATE_SQL,
                entity.getTenKH(),
                entity.getCCCD(),
                entity.getSDT(),
                entity.getMaKH());
    }

    @Override
    public void delete(String id) {

        JdbcHelper.update(DELETE_SQL, id);

    }

    @Override
    public List<KhachHang1> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public KhachHang1 selectById(String id) {
        List<KhachHang1> list = selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhachHang1> selectBySql(String sql, Object... args) {
        List<KhachHang1> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                KhachHang1 entity = new KhachHang1();
                entity.setMaKH(rs.getString("MaKH"));
                entity.setTenKH(rs.getString("TenKH"));
                entity.setCCCD(rs.getString("CCCD"));
                entity.setSDT(rs.getString("SDT"));

                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
