package com.example.flahasmart.services;

import com.example.flahasmart.entities.User;
import com.example.flahasmart.utils.MyDB;
import java.sql.*;

public class UserService {
    private final Connection cnx = MyDB.getInstance();

    public String getEmailById(int userId) throws SQLException {
        String sql = "SELECT email FROM users WHERE id_user = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("email");
        }
        return null;
    }
}