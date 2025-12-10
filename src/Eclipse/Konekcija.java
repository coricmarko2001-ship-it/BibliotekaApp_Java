package Biblioteka;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import net.proteanit.sql.DbUtils;
import java.sql.*;

public class Konekcija {

	public static Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/biblioteka?useUnicode=yes&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "");

			System.out.println("Connected");

			return con;

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;
	}

}
