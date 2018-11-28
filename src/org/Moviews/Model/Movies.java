/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Moviews.Model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.Moviews.Database.Database;

/**
 *
 * @author TSR
 */
public class Movies {
    private String id_mov;
    private String title;
    private String sinopsis;
    private Date release;
    private int duration;
    private String director;
    private String studio;
    private double ratingfilm;
    private int JMov;

    public Movies(String id_mov, String title, String sinopsis, Date release, int duration, String director, String studio, double ratingfilm) {
        this.id_mov = id_mov;
        this.title = title;
        this.sinopsis = sinopsis;
        this.release = release;
        this.duration = duration;
        this.director = director;
        this.studio = studio;
        this.ratingfilm = ratingfilm;
    }

    public Movies(String id_mov, String title) {
        this.id_mov = id_mov;
        this.title = title;
    }

    public Movies() {
        
    }

    
    public String getId_mov() {
        return id_mov;
    }

    public void setId_mov(String id_mov) {
        this.id_mov = id_mov;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public double getRatingfilm() {
        return ratingfilm;
    }

    public void setRatingfilm(double ratingfilm) {
        this.ratingfilm = ratingfilm;
    }
    
    public int getCurrentId() throws SQLException{
        int x = 0;
        String query = "SELECT COUNT(id_mov) FROM `movies`";
        Database db = new Database();
        db.Connect();
        System.out.println(query);
        db.setRs(query);
        if(!db.isRsEmpty(db.getRs())){
            while(db.getRs().next()){
                this.JMov = db.getRs().getInt("COUNT(id_mov)");
                System.out.println("Banyak user : "+this.JMov);
                x = this.JMov;
            }
        }
        db.Disconnect();
        return x;
    }
    
    public ArrayList<Movies> getAllMovies() throws SQLException{
        ArrayList<Movies> arm = new ArrayList<>();
        Database db = new Database();
        db.Connect();
        String query = "SELECT * FROM `movies`";
        System.out.println(query);
        db.setRs(query);
        if(!db.isRsEmpty(db.getRs())){
            ResultSet rs = db.getRs();
            while(db.getRs().next()){
                Movies m = new Movies(
                        "MOV"+getCurrentId(),
                        rs.getString("title"),
                        rs.getString("sinopsis"),
                        rs.getDate("release"),
                        rs.getInt("duration"),
                        rs.getString("director"),
                        rs.getString("studio"),
                        rs.getDouble("ratingfilm")
                );
                arm.add(m);
            }
        }else{
            System.out.println("DATABASE MOVIES KOSONG!");
        }
        return arm;        
    }
    
    public void addMovies(Movies m){
        Database db = new Database();
        db.Connect();
        String query = "INSERT INTO `movies` VALUES('";
        query += m.getId_mov()+"','";
        query += m.getTitle()+"','";
        query += m.getSinopsis()+"','";
        query += m.getRelease()+"','";
        query += m.getDuration()+"','";
        query += m.getDirector()+"','";
        query += m.getStudio()+"','";
        query += m.getRatingfilm()+"'";
        System.out.println(query);
        if(db.Manipulate(query)){
            System.out.println("Data berhasil di tambahkan ke database!");
        }else {
            System.out.println("Data gagal di tambahkan ke database.");
        }
        db.Disconnect();
    }
}
