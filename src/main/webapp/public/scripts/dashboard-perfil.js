/**
 * dashboard-perfil.js: FUNCIONALIDADES PERFIL USUARIO DENTRO DEL DASHBOARD
 */

//FUNCIONALIDAD PARA CERRAR SESION
var link_cerrar_sesion = document.getElementById("link-cerrar-sesion");
const cerrar_sesion = ()=>{
	fetch('Dashboard', {
		method: 'POST'
	})
	//RESPUESTA INICIAL DEL SERVIDOR
	.then(response => response.json())
	//OBTENCION DE LOS DATOS DE LA RESPUESTA
	.then(data => {
		alert(data.resultado);
		window.open("/","_self");
	})
	//CATCH PARA OBTENER DETALLE POR SI ORURRE UN ERROR
	.catch((error) => {
	    console.error('Error:', error);
	});
}
link_cerrar_sesion.onclick= cerrar_sesion;

//FUNCIONALIDAD PARA OBTENER DATOS PARA EDITAR PERFIL
var link_editar_perfil = document.getElementById("link-editar-perfil");
var input_editar_nombre_usuario = document.getElementById("editar-usuario");
var input_editar_correo = document.getElementById("editar-correo");
var input_editar_clave = document.getElementById("editar-clave");
var input_confirmar_editar_clave = document.getElementById("confirmar-editar-clave");
const obtener_datos_perfil = () => {
	fetch('Perfil', {
		method: 'GET',
		headers: {'Content-Type': 'application/json'}
	})
	.then(response => response.json())
	.then(data => {
		//ACTIVAR LOS INPUTS E INGRESAR LOS DATOS DEL USUARIO EN ELLOS
		input_editar_nombre_usuario.value=data.usuario;
		input_editar_nombre_usuario.disabled=false;
		input_editar_correo.value=data.correo;
		input_editar_correo.disabled=false;
		input_editar_clave.value="";
		input_editar_clave.disabled=false;
		input_confirmar_editar_clave.value="";
		input_confirmar_editar_clave.disabled=false;
	})	    
	.catch((error) => {
	    console.error('Error:', error);
	});
}
link_editar_perfil.onclick=obtener_datos_perfil;

//FUNCIONALIDAD PARA GUARDAR DATOS DE PERFIL EDITADO
var form_editar_perfil = document.getElementById("form-editar-perfil");
var link_guardar_perfil = document.getElementById("link-guardar-perfil");
const guardar_datos_perfil = () => {
	var datos_form_editar_perfil = new FormData(form_editar_perfil);
	if(!validarCorreo(datos_form_editar_perfil.get("correo"))){
		alert("Ingrese un correo valido");
	}
	else if(datos_form_editar_perfil.get("clave").length<6 && datos_form_editar_perfil.get("clave")!=""){
		alert("Ingrese una clave con 6 o mas caracteres");
	}
	else if(datos_form_editar_perfil.get("clave")!=datos_form_editar_perfil.get("confirmar-editar-clave")){
		alert("Confirme correctamente su clave");
		document.getElementById("confirmar-editar-clave").className="invalid";
	}else{
		fetch('Perfil', {
			method: 'POST',
			body: datos_form_editar_perfil,
		})
		.then(response => response.json())
		.then(data => {
			alert(data.resultado);
			if(data.status==200){
				window.open("Dashboard","_self");				
			}
		})	    
		.catch((error) => {
		    console.error('Error:', error);
		});
	}
}
link_guardar_perfil.onclick=guardar_datos_perfil;

//FUNCIONALIDAD PARA ELIMINAR UN PERFIL
var link_borrar_perfil = document.getElementById("link-borrar-perfil");
const borrar_perfil = () => {
	fetch('Perfil', {
		method: 'DELETE'
	})
	.then(response => response.json())
	.then(data => {
		alert(data.resultado);
		if(data.status==200){
			window.open("/","_self");				
		}
	})	    
	.catch((error) => {
	    console.error('Error:', error);
	});
}
link_borrar_perfil.onclick=borrar_perfil;

//FUNCION PARA VALIDAR UN CORREO ELECTRONICO (PARA VALIDAR AL MOMENTO DE EDITAR EL CORREO DE UN PERFIL)
function validarCorreo(correo) {    
	//OBTENER LOS INDICES DEL ARROBA Y DEL ULTIMO PUNTO
    let posicionArroba = correo.indexOf("@");
    let posicionUltimoArroba = correo.lastIndexOf("@");
    let posicionUltimoPunto = correo.lastIndexOf(".");
    
    //SI LOS VALORES SON EXISTENTES PROCEDER
    if(posicionArroba && posicionUltimoPunto){
    	//SI EL PRIMER ARROBA NO POSEE EL INDICE DEL ULTIMO ARROBA (MAS DE UN ARROBA) INVALIDAR
    	if(posicionArroba!=posicionUltimoArroba){
    		return false;
    	}
    	//COMPROBAR POSICIONES ERRONEAS DE ARROBA Y PUNTOS PARA INVALIDAR, DE NO CUMPLIRSE NINGUNA CONDICION, MARCAR COMO VALIDO.
		if(posicionArroba<1 || posicionUltimoPunto<1 || posicionUltimoPunto==posicionArroba+1 || posicionUltimoPunto<posicionArroba || (correo.length-1) == posicionUltimoPunto || (correo.length-1) == posicionUltimoArroba){
			return false;
		}else{
			return true;
		}   
    }
}