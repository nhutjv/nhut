/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity_;

import java.time.LocalTime;
import java.util.Date;
import java.sql.Time;

/**
 *
 * @author liduo
 */
public class NhanXe {

    private String maPhieu;
    private String maTheTu;
    private String khuVuc;
    private String anhTruoc;
    private String anhSau;
    private Date NgayNhan = new Date();
    private Time gioNhan;
    private String maND;
    private String bienSo;
    private String loaiXe;

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaTheTu() {
        return maTheTu;
    }

    public void setMaTheTu(String maTheTu) {
        this.maTheTu = maTheTu;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    public String getAnhTruoc() {
        return anhTruoc;
    }

    public void setAnhTruoc(String anhTruoc) {
        this.anhTruoc = anhTruoc;
    }

    public String getAnhSau() {
        return anhSau;
    }

    public void setAnhSau(String anhSau) {
        this.anhSau = anhSau;
    }

    public Date getNgayNhan() {
        return NgayNhan;
    }

    public void setNgayNhan(Date NgayNhan) {
        this.NgayNhan = NgayNhan;
    }

    public Time getGioNhan() {
        return gioNhan;
    }

    public void setGioNhan(Time gioNhan) {
        this.gioNhan = gioNhan;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }


   
   

}
