package com.bizideal.mn.webmagic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.bizideal.mn.webmagic.entity.CsdnBlog;

public class CsdnBlogDao {

	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement ps = null;

	public CsdnBlogDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/leslie?useUnicode=true&characterEncoding=UTF-8";
			conn = DriverManager.getConnection(url, "root", "root");
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int add(CsdnBlog csdnBlog) throws SQLException {
		try {
			String sql = "INSERT INTO `csdnblog` (`id`, `title`, `date`, `tags`, `category`, `view`, `comments`, `copyright`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, csdnBlog.getId());
			ps.setString(2, csdnBlog.getTitle());
			ps.setString(3, csdnBlog.getDate());
			ps.setString(4, csdnBlog.getTags());
			ps.setString(5, csdnBlog.getCategory());
			ps.setInt(6, csdnBlog.getView());
			ps.setInt(7, csdnBlog.getComments());
			ps.setInt(8, csdnBlog.getCopyright());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null && !ps.isClosed()) {
				ps = null;
				ps.close();
			}
			if (stmt != null && !stmt.isClosed()) {
				stmt = null;
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return -1;
	}

}
