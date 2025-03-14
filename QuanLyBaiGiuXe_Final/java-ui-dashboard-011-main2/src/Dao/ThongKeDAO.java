package Dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */





import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.JdbcHelper;

/**
 *
 * @author minhkhanh
 */
public class ThongKeDAO {
    private List<Object[]>getListOfArray(String sql, String[] cols, Object...args){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next())
            {
                Object[] vals = new Object[cols.length];
                for(int i = 0; i<cols.length; i++)
                {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
       public List<Object[]>getTT(Date datemin,Date datemax){
        String sql = "{CALL ThongKePhieuGui (?,?)}";
        String[] cols = {"SoXeGui","TongTien"};
        return getListOfArray(sql, cols, datemin,datemax);
       }
   
        public List<Object[]>getTTMonth(Date datemin,Date datemax){
        String sql = "{CALL ThongKeTheThang (?,?)}";
        String[] cols = {"SoXeGui","TongTien"};
        return getListOfArray(sql, cols, datemin,datemax);
       }
}
