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

import controllers.ControladorPerfil;
import helpers.Database;

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
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.println(ControladorPerfil.obtenerDatosPerfil(request));
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorPerfil.actualizarDatosPerfil(request));
		out.close();
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(ControladorPerfil.eliminarDatosPerfil(request));
		out.close();
	}
	
//	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("Perfil - PUT");
//		System.out.println(request.getParameter("usuario"));
//		System.out.println(request.getParameter("clave"));
//		System.out.println(request.getParameter("correo"));
//		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
//
//		String data = br.readLine();
//		System.out.println(data);
//	};

}
