package controller.admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Service;

@WebServlet("/admin/*")
public class adminController extends HttpServlet {

	//ArrayList<String> nonClass;

	public adminController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String serviceStr = request.getRequestURI().substring((request.getContextPath()+ "/admin/").length());
		System.out.println(serviceStr);
		try {
			Service service = (Service) Class.forName("service.cartpage."+serviceStr).newInstance();
			service.execute(request, response);
			
			if(serviceStr.equals("AdMain")) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/administratos/admainpage.jsp");
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
