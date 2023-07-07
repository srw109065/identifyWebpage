package com.customer;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/userpass";
	private String jdbcUsername = "root";
	private String jdbcPassword = "123456";
	private static final String INSERT_USERS_SQL = "INSERT INTO `userdata` (time,images) VALUES ( ?,?)";
	private static final String SELECT_ALL_USERS = "SELECT * FROM userdata";
	private static final String SELECT_USER_BY_ID = "SELECT time , images from userdata WHERE id =?";
	private static final String DELETE_USERS_SQL = "DELETE FROM userdata WHERE id = ?";
	private static final String UPDATE_USERS_SQL = "UPDATE `userdata` SET time = ?,images = ? WHERE `userdata`.`id` = ? ";
	


	public UserDAO() {
	}

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public void insertUser(User user) throws SQLException {
		System.out.println("INSERT_USERS_SQL" + INSERT_USERS_SQL);
		// try-with-resource statement will auto close the connection.
		try  (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getTime());
			preparedStatement.setBlob(2, user.getImage());
			System.out.println("aa===>preparedStatement" + preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public User selectUser(int id) {
		User user = null;
		// Step 1: Establishing a Connection
		try {
			Connection connection = getConnection();
			// Step 2:Create a statement using connection object
			// "select id,name,email,country from users where id =?"
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
			preparedStatement.setInt(1, id);
			System.out.println("bb===>preparedStatement" + preparedStatement);
			// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String time = rs.getString("time");
				Blob images = rs.getBlob("images");

				user = new User(id, time, images);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}
	
	public String convertBlobToBase64(Blob blob) throws SQLException {
	    if (blob == null) {
	        return null;
	    }
	    //通過 blob.getBytes() 方法，我們可以獲取BLOB 數據的字節數組表示。
	    //第一個參數指定讀取的起始位置（通常為1），第二個參數指定要讀取的字節數（即BLOB 數據的長度）。
	    byte[] data = blob.getBytes(1, (int) blob.length());
	    //獲取到字節數組後，我們使用 Base64.getEncoder().encodeToString() 方法將字節數組進行Base64 編碼。
	    //這將把字節數組轉換為一個Base64 編碼的字符串。最後，我們返回生成的Base64 編碼字符串。
	    String base64Image = Base64.getEncoder().encodeToString(data);
	    return base64Image;
	}


	public List<User> selectAllUsers() {
		// using try-with-resources to avoid closing resources (boiler plate code)
		List<User> users = new ArrayList<>();
		// Step 1: Establishing a Connection
		try (Connection connection = getConnection();
				// 執行查詢
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println("cc===>preparedStatement" + preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			
			// 將查詢結果轉換為 User 物件並加入清單
			while (rs.next()) {
				//以下get("")抓的都是數據庫的名稱，要注意名稱是否與數據庫欄位相通如一個不相同都不會出現資料的
				int id = rs.getInt("id");
				String time = rs.getString("time");
				Blob images = rs.getBlob("images"); // 假設圖像數據以blob編碼存儲
			    String base64Image = convertBlobToBase64(images);
			    users.add(new User(id, time, 9));

				
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			statement.setString(1, user.getTime());
			statement.setBlob(2, user.getImage());
			statement.setInt(3, user.getId());
			System.out.println(user.getId());
			System.out.println("updateUser===>preparedStatement" + statement);
			rowUpdated = statement.executeUpdate() > 0;
			System.out.println("updateUser");
		}
		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

}
