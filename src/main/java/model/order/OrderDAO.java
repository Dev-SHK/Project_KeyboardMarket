package model.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.cart.CartDTO;
import model.item.itemDTO;
import model.member.memberDTO;


public class OrderDAO {
	Connection con;
	PreparedStatement ptmt; // 보안적용
	ResultSet rs;
	String sql;
	
	public OrderDAO() {
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/qazxsw");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	ArrayList<OrderDTO> res = new ArrayList<OrderDTO>();
//	sql = "select * from order";
//	try {
//		ptmt = con.prepareStatement(sql);
//		rs = ptmt.executeQuery();
//		while(rs.next()) {
//			itemDTO dto = new itemDTO();
//			dto.setItemNo(rs.getInt("itemNo"));
//			dto.setItem_name(rs.getString("item_name"));
//			dto.setManufacture(rs.getString("manufacture"));
//			dto.setCategory(rs.getString("category"));
//			dto.setSwitchs(rs.getString("switchs"));
//			dto.setSpec(rs.getString("spec"));
//			dto.setPrice(rs.getInt("price"));
//			dto.setStock(rs.getInt("stock"));
//			dto.setReg_date(rs.getDate("reg_date"));
//			dto.setItem_img1(rs.getString("item_img1"));
//			dto.setItem_img2(rs.getString("item_img2"));
//			dto.setItem_sold(rs.getInt("item_sold"));
//			res.add(dto);
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	} finally {
//		close();
//	}
//	return res;
	
	public void addOrder(String ordered_num, String merchant, memberDTO member,  String addr1, String addr2, CartDTO cartDTO){
		sql = "insert into orders (ordered_num, ordered_date, category, switchs, "
				+ "select_count,item_name,manufacture,spec,price,reg_date, "
				+ "item_img1,item_img2,memberNo,name,addr1,addr2,tel,status) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, ordered_num);
			ptmt.setString(2, merchant);
			ptmt.setString(3, cartDTO.getCategory());
			ptmt.setString(4, cartDTO.getSwitchs());
			ptmt.setInt(5, cartDTO.getSelected_count());
			ptmt.setString(6, cartDTO.getItem_name());
			ptmt.setString(7, cartDTO.getManufacture());
			ptmt.setString(8, cartDTO.getSpec());
			ptmt.setInt(9, cartDTO.getPrice());
			ptmt.setString(10, cartDTO.getReg_date());
			ptmt.setString(11, cartDTO.getItem_img1());
			ptmt.setString(12, cartDTO.getItem_img2());
			ptmt.setInt(13, member.getMemberNo());
			ptmt.setString(14, member.getName());
			ptmt.setString(15, addr1);
			ptmt.setString(16, addr2);
			ptmt.setString(17, member.getTel());
			ptmt.setString(18, "주문완료");
			ptmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public ArrayList<OrderDTO> allList() {
		ArrayList<OrderDTO>  res = new ArrayList<OrderDTO> ();
		sql = "SELECT * FROM orders WHERE ( ordered_date > LAST_DAY(NOW() - interval 1 month) AND ordered_date <= LAST_DAY(NOW()))";
		try {
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				OrderDTO dto = new OrderDTO();
				dto.setOrdered_num(rs.getString("ordered_num"));
				dto.setName(rs.getString("name"));
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = formatter.parse(rs.getString("ordered_date"));
				dto.setOrdered_date(date);
				dto.setPrice(rs.getInt("price"));
				dto.setSelect_count(rs.getInt("select_count"));
				dto.setStatus(rs.getString("status"));
				res.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			close();
		}
		return res;
	}
	
	public ArrayList<OrderDTO> list() {
		ArrayList<OrderDTO>  res = new ArrayList<OrderDTO> ();
		sql = "select * from orders where ordered_date > date_add(now(),interval -7 day)";
		try {
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				OrderDTO dto = new OrderDTO();
				dto.setOrdered_num(rs.getString("ordered_num"));
				dto.setName(rs.getString("name"));
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = formatter.parse(rs.getString("ordered_date"));
				dto.setOrdered_date(date);
				dto.setPrice(rs.getInt("price"));
				dto.setSelect_count(rs.getInt("select_count"));
				dto.setStatus(rs.getString("status"));
				res.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			close();
		}
		return res;
	}
	
	public ArrayList<OrderDTO> orderinglist() {
		ArrayList<OrderDTO>  res = new ArrayList<OrderDTO> ();
		sql = "select * from orders where status = '주문완료'";
		try {
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				OrderDTO dto = new OrderDTO();
				dto.setOrdered_num(rs.getString("ordered_num"));
				dto.setName(rs.getString("name"));
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = formatter.parse(rs.getString("ordered_date"));
				dto.setOrdered_date(date);
				dto.setPrice(rs.getInt("price"));
				dto.setSelect_count(rs.getInt("select_count"));
				dto.setStatus(rs.getString("status"));
				res.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			close();
		}
		return res;
	}
	
	public void requestRefund(String ordered_num, String aStatus) {
		
		sql = "update orders set refund = ?, refund_date = sysdate() where ordered_num = ? ";
		
		String refundStatus;
		
		try {
			ptmt = con.prepareStatement(sql);
			
			if(aStatus.equals("주문완료")) {
				refundStatus = "취소신청";
			}else if(aStatus.equals("배송중")){
				refundStatus = "환불신청";
			}else if(aStatus.equals("배송완료")){
				refundStatus = "반품신청";
			}else {
				refundStatus = "";
			}
			
			ptmt.setString(1, refundStatus);
			ptmt.setString(2, ordered_num);
			
			ptmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
	}
	
	public ArrayList<OrderDTO> refundList(){
		ArrayList<OrderDTO> res = new ArrayList<OrderDTO>();
		
		sql = "select * from orders where refund is not null ";
		
		try {
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			
			while(rs.next()) {
				OrderDTO dto = new OrderDTO();
				
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setOrdered_num(rs.getString("ordered_num"));
				dto.setOrdered_date(rs.getTimestamp("ordered_date"));
				dto.setManufacture(rs.getString("manufacture"));
				dto.setCategory(rs.getString("category"));
				if(rs.getString("switchs")!=null) {
					dto.setSwitchs(rs.getString("switchs"));
				}
				dto.setSpec(rs.getString("spec"));
				dto.setPrice(rs.getInt("price"));
				dto.setSelect_count(rs.getInt("select_count"));
				dto.setItem_name(rs.getString("item_name"));
				dto.setReg_date(rs.getTimestamp("reg_date"));
				dto.setItem_img1(rs.getString("item_img1"));
				dto.setItem_img2(rs.getString("item_img2"));
				dto.setMemberNo(rs.getInt("memberNo"));
				dto.setName(rs.getString("name"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setStatus(rs.getString("status"));
				if(rs.getString("refund") != null) {
					dto.setRefund(rs.getString("refund"));
				}
				if(rs.getTimestamp("refund_date") != null) {
					dto.setRefund_date(rs.getTimestamp("refund_date"));
				}
				
				res.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return res;
	}
	
	public void refundCancle(int order_no) {
		sql = "update orders set refund = ?, refund_date = sysdate() where orderNo = ? ";
		
		try {
			ptmt = con.prepareStatement(sql);
			
			ptmt.setString(1, "취소불가");
			ptmt.setInt(2, order_no);
			
			ptmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (ptmt != null) {
			try {
				ptmt.close();
			} catch (SQLException e) {
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}
}
