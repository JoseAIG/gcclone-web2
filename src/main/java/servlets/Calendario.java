package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.ControladorCalendario;

/**
 * Servlet implementation class Calendario
 */
@MultipartConfig()
@WebServlet("/Calendario")
public class Calendario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Calendario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//SE COMPRUEBA EL CONTENT-TYPE DE LA SOLICITUD. SI ES NULL, QUIERE DECIR QUE SE ESTA ACCEDIENDO DESDE LA URL AL ENDPOINT Y SE REDIRECCIONA AL DASHBOARD
		if(request.getContentType()==null) {
			response.sendRedirect("Dashboard");
		}else {
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.println(ControladorCalendario.obtenerCalendariosUsuario(request));
			out.close();	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorCalendario.crearNuevoCalendrio(request));
		out.close();

	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorCalendario.editarCalendarioEdicion(request));
		out.close();
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorCalendario.eliminarCalendario(request));
		out.close();
	}
	
}
