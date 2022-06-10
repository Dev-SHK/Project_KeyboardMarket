package model.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.notice.NoticeDTO;

public class MainNoticeDAO {
	Connection con;
	PreparedStatement ptmt;
	ResultSet rs;
	String sql;
	
	public MainNoticeDAO() {
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/qazxsw");
			con = ds.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<MainNoticeDTO> Currentlist() {
		ArrayList<MainNoticeDTO> res = new ArrayList<MainNoticeDTO>();
		sql = "select * from MainNotice";
		try {
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MainNoticeDTO dto = new MainNoticeDTO();
				dto.setMainNoticeNo(rs.getInt("MainNoticeNo"));
				dto.setNoticeNo(rs.getInt("noticeNo"));
				dto.setSubject(rs.getString("subject"));
				res.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
	}
	
	public boolean insert(NoticeDTO dto) {
		sql = "insert into MainNotice (noticeNo ,subject) values (?,?)";
		try {
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, dto.getNoticeNo());
			ptmt.setString(2, dto.getSubject());
			rs = ptmt.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}
	
	public boolean removeitem(int noticeNo) {
		sql = "delete from MainNotice where noticeNo = ?";
		try {
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, noticeNo);
			rs = ptmt.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}
	
	
	
	public void close() {
		if(rs!=null)try {rs.close();} catch (Exception e) {}
		if(ptmt!=null)try {ptmt.close();} catch (Exception e) {}
		if(con!=null)try {con.close();} catch (Exception e) {}
	}

	

	

	
	
	
}
