package helpers;

import java.sql.*;
import java.util.ArrayList;

public class Database {

	private static Database DB = new Database();
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String driverDB = "org.postgresql.Driver";
	//LOCAL
	private String dbName = "gcclone";
	private String urlDB = "jdbc:postgresql://localhost:5432/" + this.dbName;
	private String userDB = "postgres";
	private String passDB = "masterkey";
	//REMOTO EN HEROKU
//	private String dbName = "dedr8acp5f19aa";
//	private String urlDB = "jdbc:postgresql://ec2-52-70-67-123.compute-1.amazonaws.com:5432/" + this.dbName;
//	private String userDB = "jiyqdixxxzgjww";
//	private String passDB = "bd78b205becfd9122927ad9a733eb5d7101b26fb79887bfa86942bde475ec61d";
	
	//CONSTRUCTOR
	private Database(){
		try {
			Class.forName(driverDB);
			this.conn = DriverManager.getConnection(urlDB, userDB, passDB);
			System.out.println("Conexion establecida");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	//METODO PARA OBTENER INSTANCIA DE DB
	public static Database getInstances() {
		return DB;
	}
	
//	//METODO DE STATEMENT
//	public ResultSet dbStatement(String query) {
//		try {
//			this.stmt = this.conn.createStatement();
//			this.rs = this.stmt.executeQuery(query);
//			while(rs.next()) {
//				System.out.print(rs.getString("usuario"));
//				System.out.println(rs.getInt("edad"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				this.stmt.close();
//				this.rs.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return rs;
//	}
	
//	//METODO DE PREPARESTATEMENT (REGISTRO)
//	public boolean dbPrepareStatement(String query, Object[] obj) {
//		try {
//			this.pstmt = this.conn.prepareStatement(query);
//			this.pstmt.setString(1, (String) obj[0]);
//			this.pstmt.setString(2, (String) obj[1]);
//			this.pstmt.setString(3, (String) obj[2]);
//			this.pstmt.executeUpdate();
//		} catch (SQLException e) {
////			e.printStackTrace();
//			return false;
//		} finally {
//			try {
//				this.pstmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return true;
//	}
	
	//METODO PARA REGISTRAR USUARIO CON COMPROBACION DE USUARIO EXISTENTE
	public String dbRegistroUsuario(Object[] datos) {
		try {
			//CHEQUEO DE USUARIO REGISTRADO
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from usuarios");
			while(rs.next()) {
				if(rs.getString("nombre_usuario").equals(datos[0])) {
					return "Usuario ya existe";
				}
				if(rs.getString("correo").equals(datos[1])) {
					return "Correo ya registrado";
				}
			}
			//REGISTRO DE USUARIO
			this.pstmt = this.conn.prepareStatement("insert into usuarios (nombre_usuario, correo, clave) values (?,?,?)");
			this.pstmt.setString(1, (String) datos[0]);
			this.pstmt.setString(2, (String) datos[1]);
			this.pstmt.setString(3, (String) datos[2]);
			this.pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			return "Problema de conexion";
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
				//this.pstmt.close();
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		
		return "Operacion exitosa";
	}
	
	//METODO PARA OBTENER COINCIDENCIAS DE USUARIO Y CLAVE PARA LOGIN
	public boolean dbLogin (Object[] datos) {
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from usuarios");
			while(rs.next()) {
				String usuario = rs.getString("nombre_usuario");
				String correo = rs.getString("correo");
				String clave = rs.getString("clave");
				if((usuario.equals(datos[0]) || correo.equals(datos[0])) && clave.equals(datos[1])) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//METODO PARA OBTENER LOS DATOS DE UN USUARIO
	public String[] dbObtenerDatosUsuario(String usuario) {
		String [] datos = new String[3];
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from usuarios where nombre_usuario ='"+usuario+"' OR correo='"+usuario+"';");
			while(rs.next()) {
				datos[0] = rs.getString("nombre_usuario");
				datos[1] = rs.getString("correo");
				datos[2] = rs.getString("clave");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return datos;
	}
	
	//METODOS PARA ACTUALIZAR LOS DATOS DE UN PERFIL
	//ACTUALIZAR LOS DATOS DE UN USUARIO CUANDO CAMBIA LA CLAVE
	public boolean dbActualizarDatosUsuario(String usuario, String nombre, String correo, String hash_clave) {
		try {
			this.pstmt = this.conn.prepareStatement("UPDATE usuarios SET nombre_usuario = ?, correo = ?, clave = ? WHERE nombre_usuario='"+usuario+"' OR correo='"+usuario+"';");
			this.pstmt.setString(1, (String) nombre);
			this.pstmt.setString(2, (String) correo);
			this.pstmt.setString(3, (String) hash_clave);
			this.pstmt.executeUpdate();
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		} finally {
			try {
				this.pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	//ACTUALIZAR LOS DATOS DE UN USUARIO CUANDO ---NO--- CAMBIA LA CLAVE
	public boolean dbActualizarDatosUsuario(String usuario, String nombre, String correo) {
		try {
			this.pstmt = this.conn.prepareStatement("UPDATE usuarios SET nombre_usuario = ?, correo = ? WHERE nombre_usuario='"+usuario+"' OR correo='"+usuario+"';");
			this.pstmt.setString(1, (String) nombre);
			this.pstmt.setString(2, (String) correo);
			this.pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				this.pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	//METODO PARA ELIMINAR UN USUARIO DEL SISTEMA
	public Boolean dbEliminarPerfil (String usuario) {
		try {
			this.stmt = this.conn.createStatement();
			this.stmt.executeUpdate("DELETE FROM usuarios WHERE nombre_usuario='"+ usuario +"' OR correo='"+usuario+"';");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				this.stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	//METODO PARA CONOCER SI UN USUARIO EXISTE Y RETORNAR SU NOMBRE Y CORREO
	public String[] dbExisteUsuario (String usuario) {
		String [] datos = new String[2];
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from usuarios where nombre_usuario ='"+usuario+"' OR correo='"+usuario+"';");
			while(rs.next()) {
				datos[0] = rs.getString("nombre_usuario");
				datos[1] = rs.getString("correo");
				if(!(usuario.equals(datos[0]) || usuario.equals(datos[1]))) {
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return datos;
	}
	
	//METODO PARA CREAR UN NUEVO CALENDARIO, RETORNA EL ID DEL CALENDARIO CREADO
	public int dbCrearCalendario (Object[] datos_calendario) {
		//SE INICIALIZA EN 0, REPRESENTANDO ERROR
		int id_calendario_creado = 0;
		try {
			//OBTENER ID DEL ULTIMO CALENDARIO
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("SELECT MAX(id_calendario) FROM calendarios");
			while(rs.next()) {
				id_calendario_creado = (rs.getInt("max")+1);
			}
			//INGRESAR LOS DATOS DEL CALENDARIO CON SU ID RESPECTIVO
			this.pstmt = this.conn.prepareStatement("INSERT INTO calendarios (id_calendario, nombre, color) VALUES (?,?,?)");
			this.pstmt.setInt(1,  id_calendario_creado);
			this.pstmt.setString(2, (String) datos_calendario[0]);
			this.pstmt.setString(3, (String) datos_calendario[1]);
			this.pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			//EN ESTE PUNTO RETORNARIA 0 (ERROR)
			return id_calendario_creado;
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
				//this.pstmt.close();
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		
		return id_calendario_creado;
	}
	
	//METODO PARA CREAR DATOS DE EDICION PARA UN CALENDARIO ESPECIFICO
	public Boolean dbCrearDatosEdicionCalendario(Object[] datos_ediciones) {
		try {
			this.pstmt = this.conn.prepareStatement("INSERT INTO ediciones (nombre_usuario, correo, id_calendario) VALUES (?,?,?)");
			this.pstmt.setString(1, (String) datos_ediciones[0]);
			this.pstmt.setString(2, (String) datos_ediciones[1]);
			this.pstmt.setInt(3,  (int) datos_ediciones[2]);
			this.pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			//EN ESTE PUNTO RETORNARIA 0 (ERROR)
			return false;
		}finally {
			try {
				//this.stmt.close();
				this.rs.close();
				this.pstmt.close();
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		return true;
	}
	
	//METODO PARA OBTENER DATOS DE EDICION DE CALENDARIOS
	public ArrayList<Integer> dbObtenerIDsCalendarios(String usuario) {
		ArrayList<Integer> id_calendario = new ArrayList<>();
		//String [] datos = new String[3];
		//String [] id_calendarios = new String [2];
 		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from ediciones where nombre_usuario ='"+usuario+"' OR correo='"+usuario+"';");
			//int i=0;
			while(rs.next()) {
//				datos[0] = rs.getString("nombre_usuario");
//				datos[1] = rs.getString("correo");
//				datos[2] = rs.getString("id_calendario");	
				
				//id_calendarios[i] = rs.getString("id_calendario");
				id_calendario.add(rs.getInt("id_calendario"));
				//i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return id_calendario;
	}
	
	//METODO PARA OBTENER DATOS DE UN CALENDARIO
	public String[] dbObtenerDatosCalendario(int id_calendario) {
		//String [] datos = new String[3];
		String[] datos_calendario = new String[2];
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from calendarios where id_calendario ='"+id_calendario+"';");
			while(rs.next()) {
//				datos[0] = rs.getString("id_calendario");
//				datos[1] = rs.getString("nombre");
//				datos[2] = rs.getString("color");	
				datos_calendario[0]=rs.getString("nombre");
				datos_calendario[1]=rs.getString("color");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return datos_calendario;
	}
	
	//METODO PARA OBTENER LOS INVITADOS DE UN CALENDARIO
	public ArrayList<String> dbObtenerInvitadosCalendario(String usuario, int id_calendario) {
		ArrayList<String> invitados = new ArrayList<>();
 		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from ediciones where (nombre_usuario!='"+usuario+"' AND correo!='"+usuario+"') AND id_calendario="+id_calendario+";");
			while(rs.next()) {
				invitados.add(rs.getString("nombre_usuario"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return invitados;
	}
	
	//METODO PARA ACTUALIZAR UN CALENDARIO + DATOS DE EDICION DE CALENDARIO
	public Boolean dbActualizarCalendario(int id_calendario, String nuevo_nombre, String nuevo_color) {
		try {
			this.pstmt = this.conn.prepareStatement("UPDATE calendarios SET nombre = ?, color = ? WHERE id_calendario="+id_calendario+";");
			this.pstmt.setString(1, (String) nuevo_nombre);
			this.pstmt.setString(2, (String) nuevo_color);
			this.pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				this.pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	//METODO PARA ACTUALIZAR LOS DATOS DE EDICION DE UN CALENDARIO
	public Boolean dbActualizarDatosEdicion(String usuario, int id_calendario, ArrayList<String> nombres_invitados, ArrayList<String> correos_invitados) {
		try {
			this.stmt = this.conn.createStatement();
			this.stmt.executeUpdate("DELETE FROM ediciones WHERE id_calendario="+id_calendario+" AND (nombre_usuario!='"+usuario+"' AND correo!='"+usuario+"');");
			
			for(int i=0; i<nombres_invitados.size(); i++) {
				this.pstmt = this.conn.prepareStatement("INSERT INTO ediciones (nombre_usuario, correo, id_calendario) VALUES (?,?,?);");
				this.pstmt.setString(1, (String) nombres_invitados.get(i));
				this.pstmt.setString(2, (String) correos_invitados.get(i));
				this.pstmt.setInt(3, id_calendario);
				this.pstmt.executeUpdate();	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				this.pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	//METODO PARA ELIMINAR UN CALENDARIO DE LA BASE DE DATOS
	public Boolean dbEliminarCalendario (int id_calendario) {
		try {
			this.stmt = this.conn.createStatement();
			this.stmt.executeUpdate("DELETE FROM calendarios WHERE id_calendario="+id_calendario+";");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				this.stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	//METODO PARA OBTENER LOS DATOS DE LAS ACTIVIDADES DE UN CALENDARIO
	public ArrayList<String[]> dbObtenerActividadesCalendario( int id_calendario) {
		ArrayList<String[]> actividades = new ArrayList<>();
 		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from actividades where id_calendario="+ id_calendario +"");
			while(rs.next()) {
				String[] datos_actividad = new String[8];
				datos_actividad[0] = rs.getString("id_actividad");
				datos_actividad[1] = rs.getString("id_calendario");
				datos_actividad[2] = rs.getString("informacion");
				datos_actividad[3] = rs.getString("fecha");
				datos_actividad[4] = rs.getString("hora_inicio");
				datos_actividad[5] = rs.getString("hora_fin");
				datos_actividad[6] = rs.getString("duracion");
				datos_actividad[7] = rs.getString("ruta_imagen");
				actividades.add(datos_actividad);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				this.stmt.close();
				this.rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return actividades;
	}
	
	
	//METODO PARA CERRAR LA SESION DE LA BASE DE DATOS
	public void dbClose() {
		try {
			this.conn.close();
			System.out.println("Conexion cerrada");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

