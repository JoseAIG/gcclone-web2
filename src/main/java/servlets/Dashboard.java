package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import helpers.Database;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession sesion = request.getSession();
		System.out.println(sesion.getAttribute("usuario"));
		if(sesion.getAttribute("usuario")==null) {
			System.out.println("usuario nulo, redireccionar a pagina principal");
			response.sendRedirect("/gcclone");
		}else {
			System.out.println("usuario NO nulo, mostrar dashboard");
			//response.setContentType("text/html");
			request.getRequestDispatcher("/public/views/dashboard.html").include(request, response);
		}
//		response.setContentType("text/html");
//		request.getRequestDispatcher("/public/views/dashboard.html").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession sesion = request.getSession();
		sesion.invalidate();
		
        response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.println("{\"resultado\": \"Sesion finalizada\", \"status\":"+200+"}");
		out.close();
	}
	

}
