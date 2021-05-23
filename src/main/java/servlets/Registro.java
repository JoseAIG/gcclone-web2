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
		response.setContentType("text/html");
		request.getRequestDispatcher("/public/views/registro.html").include(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Database DB = Database.getInstances();
		//doGet(request, response);
		//String usuario = request.getParameter("usuario");
		//System.out.println("Post! "+usuario+ " " + request.getParameter("correo") + " " + request.getParameter("clave"));
		//String objectToReturn = "{ key1: 'value1', key2: 'value2' }";
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		//out.println("{\"message\": \"user created\", \"status\":"+8000+"}");
		System.out.println(request.getParameter("usuario") + request.getParameter("correo") + request.getParameter("clave"));
		
		String usuario = request.getParameter("usuario");
		String correo_electronico = request.getParameter("correo");
		String clave = request.getParameter("clave");
		Object[] datos_usuario = {usuario, correo_electronico, clave}; 
		//System.out.println(DB.dbPrepareStatement("insert into usuario (nombre_usuario, correo, clave) values (?,?,?)", datos_usuario));
		//EJECUTAR EL REGISTRO DE NUEVO USUARIO Y GUARDAR EL RESULTADO EN UN STRING PARA GENERAR ESTATUS EN FUNCION DEL RESULTADO
		String resultado_registro = DB.dbRegistroUsuario(datos_usuario);
		System.out.println(resultado_registro);
		if(resultado_registro.equals("Operacion exitosa")) {
			System.out.println("redireccionamiento aqui...");
		}else {
			System.out.println("No se redireccionara.");
		}
		out.println("{\"resultado\": \""+ resultado_registro +"\", \"status\":"+200+"}");
		out.close();
	}

}
