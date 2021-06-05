package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println("ACTIVIDAD - POST");
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
//		System.out.println(request.getParameter("select-calendario-crear-actividad"));
//		System.out.println(request.getParameter("detalle-actividad"));
//		System.out.println(request.getParameter("fecha-crear-actividad"));
//		System.out.println(request.getParameter("hora-inicio"));
//		System.out.println(request.getParameter("hora-fin"));
//		System.out.println(request.getParameter("imagen-crear-actividad"));
		Object[] datos_nueva_actividad = {
			request.getParameter("select-calendario-crear-actividad"), 
			request.getParameter("detalle-actividad"),
			request.getParameter("fecha-crear-actividad"),
			request.getParameter("hora-inicio"),
			request.getParameter("hora-fin"),
			"wip.png"
		};
		Database DB = Database.getInstances();
		if(DB.dbCrearActividad(datos_nueva_actividad)) {
			out.println("{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+"}");
		}else {
			out.println("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+"}");
		}


		
	}

}
