package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.ControladorPerfil;

/**
 * Servlet implementation class Perfil
 */
@MultipartConfig()
@WebServlet("/Perfil")
public class Perfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Perfil() {
        super();
        // TODO Auto-generated constructor stub
    }

    //METODO doGet ENCARGADO DE RESPONDER LOS DATOS DEL USUARIO PARA QUE EL MISMO PUEDA EDITARLOS
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//SE COMPRUEBA EL CONTENT-TYPE DE LA SOLICITUD. SI ES NULL, QUIERE DECIR QUE SE ESTA ACCEDIENDO DESDE LA URL AL ENDPOINT Y SE REDIRECCIONA AL DASHBOARD
		if(request.getContentType()==null) {
			response.sendRedirect("Dashboard");
		}else {
			response.setContentType("application/json");  
			PrintWriter out = response.getWriter();
			out.println(ControladorPerfil.obtenerDatosPerfil(request));
			out.close();	
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorPerfil.actualizarDatosPerfil(request));
		out.close();
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorPerfil.eliminarDatosPerfil(request));
		out.close();
	}

}
