/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Moviews.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.Moviews.Model.Movies;
import org.Moviews.Model.UserMovies;
import org.Moviews.View.ViewManipulateMovie;
import org.Moviews.View.ViewMovieList;
import org.Moviews.View.ViewMoviePage;

/**
 *
 * @author TSR
 */
public class ControllerMovieList extends defaultController{
    private ViewMovieList view;
    private Movies model;
    
    public ControllerMovieList() {
        
    }
    public ControllerMovieList(ViewMovieList view, Movies model) throws SQLException {
        this.view = view;
        this.model = model;
        loadMovies();
        
        this.view.setAddEvent(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    toAddMovie();
                } catch (SQLException ex) {
                    Logger.getLogger(ControllerMovieList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.view.setDeleteEvent(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteMovie();
                try {
                    loadMovies();
                } catch (SQLException ex) {
                    Logger.getLogger(ControllerMovieList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.view.setEditEvent(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(getMovieSelectedInfo() != null){
                        try {
                            toEditMovie(getMovieSelectedInfo());
                        } catch (SQLException ex) {
                            Logger.getLogger(ControllerMovieList.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        System.out.println("Tidak ada baris yang dipilih");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ControllerMovieList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.view.setRefreshEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadMovies();
                } catch (SQLException ex) {
                    Logger.getLogger(ControllerMovieList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.view.setOpenMovEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    toMoviePage(getMovieSelectedInfo());
                    closeView();
                } catch (SQLException ex) {
                    Logger.getLogger(ControllerMovieList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.view.setHomeEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toHomeCall();
                closeView();
            }
        });
        
        this.view.setSearchEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCall();
                closeView();
            }
        });
        
        if(this.user.getTipe() == 'R'){
            this.view.addVisibility(false);
            this.view.editVisibility(false);
            this.view.deleteVisibility(false);
        }
    }
    public void toHomeCall(){
        toHome();
    }
    public void searchCall(){
        search(this.view.getSearchBox());
    }
    
    public void closeView(){
        this.view.dispose();
    }

    
    public void loadMovies() throws SQLException{
        DefaultTableModel dtm = new DefaultTableModel(new String[]{"ID","Judul","Rating","Tanggal Rilis","Durasi(menit)","Sutradara","Sinopsis"},0);
        ArrayList<Movies> arm = this.model.getAllMovies();
        for (Movies m : arm){
            dtm.addRow(new Object[]{m.getId_mov(),m.getTitle(),m.getRatingfilm(),m.getRelease(),m.getDuration(),m.getDirector(),m.getSinopsis()});
        }
        this.view.setMovies(dtm);
    }
    
    public void ShowView(){
        this.view.setLocationRelativeTo(null);
        this.view.show();
    }
    
    public void toAddMovie() throws SQLException{
        ControllerManipulateMovie am = new ControllerManipulateMovie(new ViewManipulateMovie(), new Movies(), 'c');
        am.showView();
        this.view.dispose();
    }
    
    public void toEditMovie(Movies m) throws SQLException{
        ViewManipulateMovie Eview = new ViewManipulateMovie();
        
        Eview.setTitle(m.getTitle());
        Eview.setDuration(m.getDuration());
        Eview.setTanggal(String.valueOf(m.getRelease().getDay()));
        Eview.setBulan(String.valueOf(m.getRelease().getMonth()));
        Eview.setTahun(String.valueOf(m.getRelease().getYear()));
        Eview.setDirector(m.getDirector());
        Eview.setStudio(m.getStudio());
        Eview.setSinopsis(m.getSinopsis());
        Eview.setRating(String.valueOf(m.getRatingfilm()));
        
        ControllerManipulateMovie em = new ControllerManipulateMovie(Eview, new Movies(), 'u');
        em.setIdSelected(m.getId_mov());
        em.showView();
        this.view.dispose();
    }
    
    public Movies getMovieSelectedInfo() throws SQLException{
        String id = this.view.getSelectedMovies();
        Movies m = this.model.findData(id);
        //System.out.println("Movie yang terpilih adalah : "+id);
        return m;
    }
    
    public void DeleteMovie(){
        this.model.Delete("movies","id_mov",this.view.getSelectedMovies());
    }
    
    public void toMoviePage(Movies m){
        this.movie = m;
        ViewMoviePage vmp = new ViewMoviePage();
        vmp.setJudul(m.getTitle());
        System.out.println("Selected film : "+ m.getTitle());
        vmp.setSinopsis(m.getSinopsis());
        DateFormat df = new SimpleDateFormat("dd - MM - yyyy");
        String date = df.format(m.getRelease());
        vmp.setRelease(date);
        vmp.setDuration(String.valueOf(m.getDuration()));
        vmp.setDirector(m.getDirector());
        vmp.setStudio(m.getStudio());
        vmp.setTitle(m.getTitle());
        vmp.setRate(String.valueOf(m.getRatingfilm()));
        
        ControllerMoviePage mp = new ControllerMoviePage(vmp, new UserMovies());
        mp.ShowView();

        //this.view.dispose();
        
    }
}
