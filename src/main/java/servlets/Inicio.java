package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Inicio
 */
@WebServlet("")
public class Inicio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Inicio() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession sesion = request.getSession();
		System.out.println(sesion.getAttribute("usuario"));
		if(sesion.getAttribute("usuario")==null) {
			System.out.println("es null, mostrar vista principal de inicio");
			response.setContentType("text/html");
			request.getRequestDispatcher("/public/views/inicio.html").include(request, response); 
		}else {
			System.out.println("NO es null, redireccionar a dashboard");
			response.sendRedirect("Dashboard");
		}
//		response.setContentType("text/html");
//		request.getRequestDispatcher("/public/views/inicio.html").include(request, response); 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
