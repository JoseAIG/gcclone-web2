package helpers;

import java.sql.*;

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
	
	//METODO DE STATEMENT
	public ResultSet dbStatement(String query) {
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(query);
			while(rs.next()) {
				System.out.print(rs.getString("usuario"));
				System.out.println(rs.getInt("edad"));
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
		return rs;
	}
	
	//METODO DE PREPARESTATEMENT (REGISTRO)
	public boolean dbPrepareStatement(String query, Object[] obj) {
		try {
			this.pstmt = this.conn.prepareStatement(query);
			this.pstmt.setString(1, (String) obj[0]);
			this.pstmt.setString(2, (String) obj[1]);
			this.pstmt.setString(3, (String) obj[2]);
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
	
	//METODO PARA REGISTRAR USUARIO CON COMPROBACION DE USUARIO EXISTENTE
	public String dbRegistroUsuario(Object[] datos) {
		try {
			//CHEQUEO DE USUARIO REGISTRADO
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery("select *from usuario");
			while(rs.next()) {
				if(rs.getString("nombre_usuario").equals(datos[0])) {
					return "Usuario ya existe";
				}
				if(rs.getString("correo").equals(datos[1])) {
					return "Correo ya registrado";
				}
			}
			//REGISTRO DE USUARIO
			this.pstmt = this.conn.prepareStatement("insert into usuario (nombre_usuario, correo, clave) values (?,?,?)");
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
	public boolean dbLogin (String query, Object[] datos) {
		try {
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(query);
			while(rs.next()) {
				String usuario = rs.getString("usuario");
				String clave = rs.getString("clave");
				if(usuario.equals(datos[0]) && clave.equals(datos[1])) {
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

