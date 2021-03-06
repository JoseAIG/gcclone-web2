package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controllers.ControladorRegistro;
/**
 * Servlet implementation class Registro
 */
@MultipartConfig()
@WebServlet("/Registro")
public class Registro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Registro() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//COMPROBAR QUE NO EXISTA UNA SESION ACTIVA PARA ENVIAR LA VISTA DE REGISTRO. REDIRIGIR AL DASHBOARD DE EXISTIR UNA SESION
		HttpSession sesion = request.getSession();
		if(sesion.getAttribute("usuario")==null) {
			response.setContentType("text/html");
			request.getRequestDispatcher("/public/views/registro.html").include(request, response); 
		}else {
			response.sendRedirect("Dashboard");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorRegistro.registrarUsuario(request));
		out.close();
	}

}
