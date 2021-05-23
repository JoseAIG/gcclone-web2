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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //METODO doGet ENCARGADO DE RESPONDER LOS DATOS DEL USUARIO PARA QUE EL MISMO PUEDA EDITARLOS
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Pefil - GET");
		HttpSession sesion = request.getSession();
		
		Database DB = Database.getInstances();
		String [] datos_usuario = DB.dbObtenerDatosUsuario(sesion.getAttribute("usuario").toString());
        
		response.setContentType("application/json");  
		PrintWriter out = response.getWriter();
		out.println("{\"usuario\": \""+datos_usuario[0]+"\", \"correo\":\""+datos_usuario[1]+"\", \"clave\":\""+datos_usuario[2]+"\"}");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Perfil - POST");
		System.out.println(request.getParameter("usuario"));
		System.out.println(request.getParameter("clave"));
		System.out.println(request.getParameter("correo"));
		
		HttpSession sesion = request.getSession();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		String usuario = request.getParameter("usuario");
		String correo = request.getParameter("correo");
		String clave = request.getParameter("clave");
		Object[] datos_usuario = {usuario, correo, clave}; 
		
		Database DB = Database.getInstances();
		Boolean resultado = DB.dbActualizarDatosUsuario(sesion.getAttribute("usuario").toString(), datos_usuario);
		System.out.println(resultado);
		if(resultado) {
			sesion.setAttribute("usuario", usuario);
			out.println("{\"resultado\": \"Datos actualizados\", \"status\":"+200+", \"nuevo_usuario\":\""+usuario+"\"}");
			out.close();
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		Database DB = Database.getInstances();
		Boolean resultado = DB.dbEliminarPerfil(sesion.getAttribute("usuario").toString());
		System.out.println(resultado);
		if(resultado) {
			sesion.setAttribute("usuario", null);
			out.println("{\"resultado\": \"Perfil eliminado\", \"status\":"+200+"}");
			out.close();
		}else {
			System.out.println("El usuario no pudo eliminarse");
		}
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
