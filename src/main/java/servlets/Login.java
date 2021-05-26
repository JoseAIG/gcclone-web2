package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import helpers.Database;

/**
 * Servlet implementation class Login
 */
@MultipartConfig()
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		request.getRequestDispatcher("/public/views/login.html").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		//out.println("{\"message\": \"Proceso login\", \"status\":"+200+"}");
		Database DB = Database.getInstances();
		
		String usuario = request.getParameter("usuario");
		String clave = request.getParameter("clave");
		Object[] datos = {usuario, clave};
		Boolean resultadoLogin = DB.dbLogin(datos);
		//System.out.println(request.getParameter("usuario") + request.getParameter("clave"));
		System.out.println(resultadoLogin);
		if(resultadoLogin) {
			HttpSession sesion = request.getSession();
			sesion.setAttribute("usuario", usuario);
			System.out.println("sesion otorgada a: " + sesion.getAttribute("usuario"));		
			out.println("{\"message\": \"Login exitoso\", \"status\":"+200+", \"redirect\": \"/Dashboard\"}");

		}
	}

}
