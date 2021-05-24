package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		System.out.println("Calendario - POST " + request.getParameter("nombre-calendario") + " " + request.getParameter("input-invitado0"));
		int cantidad_invitados = Integer.parseInt(request.getParameter("cantidad-invitados").toString());
		System.out.println("invitados: ");
		for(int i=0;i<cantidad_invitados;i++) {
			if(request.getParameter("input-invitado"+i)!=null && !request.getParameter("input-invitado"+i).equals("")) {
				System.out.println(request.getParameter("input-invitado"+i));
			}
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println("{\"resultado\": \"Creacion de calendario exitosa\", \"status\":"+200+", \"redirect\": \"/Dashboard\"}");

	}

}
