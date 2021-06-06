package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.ControladorActividad;
import helpers.Database;

/**
 * Servlet implementation class Actividad
 */
@MultipartConfig()
@WebServlet("/Actividad")
public class Actividad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Actividad() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//REDIRECCIONAR AL DASHBOARD SI SE ACCEDE AL ENDPOINT ACTIVIDAD POR MEDIO DE LA URL
		response.sendRedirect("Dashboard");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {				
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorActividad.crearActividad(request));
		out.close();
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorActividad.modificarActividad(request));
		out.close();
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorActividad.eliminarActividad(request));
		out.close();
	}

}
