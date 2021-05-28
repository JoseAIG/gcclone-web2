package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controllers.ControladorCalendario;
import helpers.Database;

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
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession sesion = request.getSession();
//		Database DB = Database.getInstances();
//		
//		String [] datos_ediciones = DB.dbObtenerDatosEdicionCalendario(sesion.getAttribute("usuario").toString());
//		System.out.println("Datos de ediciones: " + datos_ediciones[0]+datos_ediciones[1]+datos_ediciones[2]);
//		
//		String [] datos_calendarios = DB.dbObtenerDatosCalendario(Integer.parseInt(datos_ediciones[2]));
//		System.out.println("Datos de ediciones: " + datos_calendarios[0]+datos_calendarios[1]+datos_calendarios[2]);

		String calendarios = ControladorCalendario.obtenerCalendariosUsuario(sesion.getAttribute("usuario").toString());
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		//out.println("{\"resultado\": \"Datos calendarios satisfactorio\", \"status\":"+200+", \"nombre\":\""+datos_calendarios[1]+"\", \"color\":\""+datos_calendarios[2]+"\"}");
		out.println(calendarios);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		System.out.println("Calendario - POST " + request.getParameter("nombre-calendario"));
//		int cantidad_invitados = Integer.parseInt(request.getParameter("cantidad-invitados").toString());
//		System.out.println("invitados: ");
//		for(int i=0;i<cantidad_invitados;i++) {
//			if(request.getParameter("input-invitado"+i)!=null && !request.getParameter("input-invitado"+i).equals("")) {
//				System.out.println(request.getParameter("input-invitado"+i));
//			}
//		}
		
	
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorCalendario.crearNuevoCalendrio(request));
		out.close();

	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String data = br.readLine();
		System.out.println(data);
		
//		System.out.println(request.getAttribute("usuario"));
//		
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		out.println("{\"resultado\": \"Put satisfactorio\", \"status\":"+200+"}");
	}

}
