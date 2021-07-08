package com.example.webdbdemo.model_jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    // adatbázis kapcsolódási paraméterek:
    static final String DB_URL = "jdbc:mysql://localhost:3306/webdbdemo";
    static final String USER = "root";
    static final String PASS = "XDR5432ws";
    //TODO a paramétereket külön konfigurációs fáljban illik tárolni
    //TODO web alkalmazásoknál connection poolt érdemes használni

    public Optional<User> findById(int id){ // megkeres egy usert
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // kapcsolódik az adatbázishoz
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USER WHERE ID=?")){  // az sql utasítás
            stmt.setInt(1,id); // Megadja, hogy m kerüljön a ? helyére
            ResultSet rs=stmt.executeQuery(); // Futttaja az sql parancsot, visszadja az eredményét
            if(rs.next()){ // van találat
                return Optional.of(new User(rs.getInt("ID"),rs.getString("NAME"),
                        rs.getString("EMAIL")));
            }else{
                return Optional.empty();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // TODO valós alkalmazásnál kifinomultabb hibakezelés kell
        }
        return Optional.empty();
    }

    public Iterable<User> findAll(){ // lekérdezi az összes usert
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // kapcsolódik az adatbázishoz
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USER")){  // az sql utasítás
            ResultSet rs=stmt.executeQuery(); // Futattja az sql parancsot, visszadja az eredményét
            List<User> l=new ArrayList<>();
            while(rs.next()){
                l.add(new User(rs.getInt("ID"),rs.getString("NAME"),
                        rs.getString("EMAIL")));
            }
            return l;
        } catch (SQLException ex) {
            ex.printStackTrace();
            // TODO valós alkalmazásnál kifinomultabb hibakezelés kell
        }
        return null;
    }

    public void delete(User e){ // töröl egy usert
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // kapcsolódik az adatbázishoz
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM USER WHERE ID=?")){  // az sql utasítás
            stmt.setInt(1,e.getId()); // Megadja, hogy m kerüljön a ? helyére
            stmt.executeUpdate(); // végrehajtja az sql parancsot
        } catch (SQLException ex) {
            ex.printStackTrace();
            // TODO valós alkalmazásnál kifinomultabb hibakezelés kell
        }
    }

    public void save(User e){ // elment (INSERT or UPDATE) egy usert
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); // kapcsolódik az adatbázishoz
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO USER(ID,NAME,EMAIL) VALUES(?,?,?) ON DUPLICATE KEY UPDATE NAME=?, EMAIL=?")){
                // az sql utasítás
            stmt.setInt(1,e.getId()); // Megadja, hogy m kerüljön a ? helyére
            stmt.setString(2,e.getName()); // Megadja, hogy m kerüljön a ? helyére
            stmt.setString(3,e.getEmail()); // Megadja, hogy m kerüljön a ? helyére
            stmt.setString(4,e.getName()); // Megadja, hogy m kerüljön a ? helyére
            stmt.setString(5,e.getEmail()); // Megadja, hogy m kerüljön a ? helyére
            stmt.executeUpdate(); // végrehajtja az sql parancsot
        } catch (SQLException ex) {
            ex.printStackTrace();
            // TODO valós alkalmazásnál kifinomultabb hibakezelés kell
        }
    }

}
