package net.devras.tdm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

	private static String host, port, name, user, pass;
	private static String url;
	private static boolean _isConnected = false;
	private static Connection con;

	public MySQL(String host, String port, String name, String user, String pass){
		MySQL.host = host;
		MySQL.port = port;
		MySQL.name = name;
		MySQL.user = user;
		MySQL.pass = pass;

		MySQL.Initialize();
	}
	public static void Config(String host, String port, String name, String user, String pass){
		MySQL.host = host;
		MySQL.port = port;
		MySQL.name = name;
		MySQL.user = user;
		MySQL.pass = pass;

		MySQL.Initialize();
	}
	public static void Initialize(){
		MySQL.url = String.format(
				"jdbc:mysql://%s:%s/%s",
				host,
				port,
				name);
	}

	public static void Connect(){

		try {
			Class.forName("com.mysql.jdbc.Driver");
			MySQL.con = DriverManager.getConnection(url, user, pass);
			MySQL._isConnected = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){

		if (MySQL._isConnected == false){
			return null;
		}
		return MySQL.con;
	}

	public static void update(String query){
		try {
			Connection c = MySQL.getConnection();
			Statement stm = c.createStatement();
			stm.executeUpdate(query);
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet query(String query){
		try {
			Connection c = MySQL.getConnection();
			Statement stm = c.createStatement();
			return stm.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean tableExists(String name){
		try {
			DatabaseMetaData meta = MySQL.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, name, new String[] { "TABLE" });

			while (res.next()){
				return true;
			}

			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void createTable(String name, String column) {

		if (MySQL.tableExists(name)){
			return;
		}

		String sql = String.format("create table `%s`(%s)", name, column);
		
		Connection c = MySQL.getConnection();
		try {
			Statement stm = c.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void disconnect() {
		try {
			MySQL.con.close();
			MySQL._isConnected = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public static boolean isConnected() {
		return MySQL._isConnected;
	}
	public static void insertData(String columns, String values, String table) {
		MySQL.update("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")");
	}
	public int countRows(String table) {
		int i = 0;
		if (table == null) {
			return i;
		}
		ResultSet rs = MySQL.query("SELECT * FROM " + table);
		try {
			while (rs.next()) {
				i++;
			}
		}
		catch (Exception localException) {}
		return i;
	}
}
