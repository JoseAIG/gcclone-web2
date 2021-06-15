package helpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Database {

	private static Database DB = new Database();
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private String driverDB = "org.postgresql.Driver";
	
	private PropertiesReader PR;
	
	private String dbName;
	private String urlDB;
	private String userDB;
	private String passDB;
	
	//CONSTRUCTOR
	private Database(){
		try {
			//CLASE PROPERTIESREADER
			PR = PropertiesReader.getInstance();

			//REMOTO
			dbName = PR.obtenerPropiedad("dbName");
			urlDB = PR.obtenerPropiedad("urlDB") + this.dbName;
			userDB = PR.obtenerPropiedad("userDB");
			passDB = PR.obtenerPropiedad("passDB");
						
			//LOCAL
//			dbName = PR.obtenerPropiedad("localDBName");
//			urlDB = PR.obtenerPropiedad("localUrlDB") + this.dbName;
//			userDB = PR.obtenerPropiedad("localUserDB");
//			passDB = PR.obtenerPropiedad("localPassDB");
			
			//ESTABLECER CONEXION
			Class.forName(driverDB);
			this.conn = DriverManager.getConnection(urlDB, userDB, passDB);
			System.out.println("Conexion establecida");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//METODO PARA OBTENER INSTANCIA DE DB
	public static Database getInstances() {
		return DB;
	}
	
	//METODO STATEMENT PARA CONSULTAS SIMPLES QUE NO REQUIERAN RETORNO DE DATOS ESPECIFICOS
	public boolean dbStatement (String query) {
		try {
			this.stmt = this.conn.createStatement();
			this.stmt.executeUpdate(query);
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
	
	//METODO PREPARED STATEMENT CAPAZ DE OBTENER DISTINTOS TIPOS DE DATOS CON UN FOR-EACH Y DEPENDIENDO DE ESTOS REALIZAR PREPARED STATEMENTS DINAMICAMENTE
	//ESTE METODO NO RETORNA VALORES ESPECIFICOS MAS ALLA DE UN BOOLEAN PARA CONOCER EL ESTATUS DE LA CONSULTA
	public boolean dbPreparedStatement(String query, Object[] datos) {
		try {
			this.pstmt = this.conn.prepareStatement(query);
			int i = 0;
			for(Object dato : datos) {
				System.out.println(dato);
				if(dato instanceof Integer) {
					this.pstmt.setInt(++i, (int) dato);
				}
				else if(dato instanceof String) {
					this.pstmt.setString(++i, (String) dato);
				}
				else if(dato instanceof Double) {
					this.pstmt.setDouble(++i, (Double) dato);
				}
				else if(dato instanceof Boolean) {
					this.pstmt.setBoolean(++i, (Boolean) dato);
				}
				else if(dato==null) {
					this.pstmt.setNull(++i, Types.VARCHAR);
				}
			}
			
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
	
	//METODO PARA REGISTRAR USUARIO CON COMPROBACION DE USUARIO EXISTENTE
	public String dbRegistroUsuario(Object[] datos) {
		try {
			//CHEQUEO DE USUARIO REGISTRADO
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(PR.obtenerPropiedad("obtenerUsuarios"));
			while(rs.next()) {
				if(rs.getString("nombre_usuario").equals(datos[0])) {
					return "Usuario ya existe";
				}
				if(rs.getString("correo").equals(datos[1])) {
					return "Correo ya registrado";
				}
			}
			//REGISTRO DE USUARIO
			this.pstmt = this.conn.prepareStatement(PR.obtenerPropiedad("registroUsuario"));
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return "Operacion exitosa";
	}
	
	//METODO PARA OBTENER LOS DATOS DE UN USUARIO
	public String[] dbObtenerDatosUsuario(String usuario) {
		String [] datos = new String[3];
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(PR.obtenerPropiedad("obtenerDatosUsuario")+usuario+"' OR correo='"+usuario+"';");
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
		
	//METODO PARA CREAR UN NUEVO CALENDARIO, RETORNA EL ID DEL CALENDARIO CREADO
	public int dbCrearCalendario (Object[] datos_calendario) {
		//SE INICIALIZA EN 0, REPRESENTANDO ERROR
		int id_calendario_creado = 0;
		try {
			//OBTENER ID DEL ULTIMO CALENDARIO
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(PR.obtenerPropiedad("obtenerIdCalendario"));
			while(rs.next()) {
				id_calendario_creado = (rs.getInt("max")+1);
			}
			//INGRESAR LOS DATOS DEL CALENDARIO CON SU ID RESPECTIVO
			this.pstmt = this.conn.prepareStatement(PR.obtenerPropiedad("crearCalendario"));
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id_calendario_creado;
	}
		
	//METODO PARA OBTENER DATOS DE EDICION DE CALENDARIOS
	public ArrayList<Integer> dbObtenerIDsCalendarios(String usuario) {
		ArrayList<Integer> id_calendario = new ArrayList<>();
 		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from ediciones where nombre_usuario ='"+usuario+"' OR correo='"+usuario+"';");
			while(rs.next()) {
				id_calendario.add(rs.getInt("id_calendario"));
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
		String[] datos_calendario = new String[2];
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(PR.obtenerPropiedad("obtenerDatosCalendario")+id_calendario);
			while(rs.next()) {	
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
	
	//METODO PARA OBTENER LOS INVITADOS DE UN CALENDARIO COMPROBANDO QUE EL USUARIO SEA PROPIETARIO DEL MISMO
	public ArrayList<String> dbObtenerInvitadosCalendario(String usuario, int id_calendario) {
		ArrayList<String> invitados = new ArrayList<>();
 		try {
 			//COMPROBAR SI EL USUARIO ES PROPIETARIO DEL CALENDARIO
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from ediciones where (nombre_usuario='"+usuario+"' OR correo='"+usuario+"') AND id_calendario="+id_calendario+" AND propietario");
 			if(!rs.next()) {
 				//SI NO ES PROPIETARIO RETORNAR NULL
 				return null;
 			}
			this.stmt.close();
			this.rs.close();
 			
 			//OBTENER LOS INVITADOS DEL CALENDARIO YA QUE EL USUARIO ES PROPIETARIO DEL CALENDARIO
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
		
	//METODO PARA ACTUALIZAR LOS DATOS DE EDICION DE UN CALENDARIO
	public Boolean dbActualizarDatosEdicion(String usuario, int id_calendario, ArrayList<String> nombres_invitados, ArrayList<String> correos_invitados) {
		try {
			this.stmt = this.conn.createStatement();
			this.stmt.executeUpdate("DELETE FROM ediciones WHERE id_calendario="+id_calendario+" AND (nombre_usuario!='"+usuario+"' AND correo!='"+usuario+"');");
			
			for(int i=0; i<nombres_invitados.size(); i++) {
				this.pstmt = this.conn.prepareStatement(PR.obtenerPropiedad("crearDatosEdicion"));
				this.pstmt.setString(1, (String) nombres_invitados.get(i));
				this.pstmt.setString(2, (String) correos_invitados.get(i));
				this.pstmt.setInt(3, id_calendario);
				this.pstmt.setBoolean(4, false);
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
		
	//METODO PARA OBTENER LOS DATOS DE LAS ACTIVIDADES DE UN CALENDARIO
	public ArrayList<String[]> dbObtenerActividadesCalendario (int id_calendario) {
		ArrayList<String[]> actividades = new ArrayList<>();
 		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(PR.obtenerPropiedad("obtenerActividadesCalendario")+ id_calendario);
			while(rs.next()) {
				String[] datos_actividad = new String[8];
				datos_actividad[0] = rs.getString("id_actividad");
				datos_actividad[1] = rs.getString("id_calendario");
				datos_actividad[2] = rs.getString("informacion");
				datos_actividad[3] = rs.getString("fecha");
				datos_actividad[4] = rs.getString("hora_inicio");
				datos_actividad[5] = rs.getString("hora_fin");
				datos_actividad[6] = rs.getString("ruta_imagen");
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