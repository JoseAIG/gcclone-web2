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
//		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
//
//		String data = br.readLine();
//		System.out.println(data);
//		JSONObject json_editar_calendario = new JSONObject(data);
//		System.out.println("El nombre del calendario es: " + json_editar_calendario.get("nombre-editar-calendario"));
//		JSONObject json_respuesta = new JSONObject();
//		json_respuesta.append("resultado", "Peticion existosa");
//		json_respuesta.append("status", 200);
		
		
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		out.println(json_respuesta);
//		out.close();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorCalendario.editarCalendarioEdicion(request));
		out.close();
		
//		System.out.println(request.getAttribute("usuario"));
//		
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		out.println("{\"resultado\": \"Put satisfactorio\", \"status\":"+200+"}");
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorCalendario.eliminarCalendario(request));
		out.close();
	}
	
}
