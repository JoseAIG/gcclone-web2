/**
 * 
 */

//FUNCIONALIDAD PARA CERRAR SESION
var link_cerrar_sesion = document.getElementById("link-cerrar-sesion");
const cerrar_sesion = ()=>{
	    fetch('/gcclone/Dashboard', {
	    	method: 'DELETE',
	    	//body: datos_form,
			//mode: "no-cors",
	    	//headers: new Headers({'Content-Type': 'application/json'}),
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
			alert(data.resultado);
			window.open("/gcclone","_self");
	    })	    
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
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
const obtener_datos_perfil = () => {
    fetch('/gcclone/Perfil', {
    	method: 'GET',
    	//body: datos_form,
		//mode: "no-cors",
    	//headers: new Headers({'Content-Type': 'application/json'}),
		})
    //RESPUESTA CRUDA DEL SERVER
    .then(response => response.json())
    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
    .then(data => {
        console.log('Respuesta del servidor - Datos del perfil para editar:', data);
		input_editar_nombre_usuario.value=data.usuario;
		input_editar_nombre_usuario.disabled=false;
		input_editar_correo.value=data.correo;
		input_editar_correo.disabled=false;
		input_editar_clave.value=data.clave;
		input_editar_clave.disabled=false;
    })	    
	//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
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
	console.log(datos_form_editar_perfil);
	console.log("Usuario: "+datos_form_editar_perfil.get("usuario"), "Correo: " + datos_form_editar_perfil.get("correo"), "Clave: " + datos_form_editar_perfil.get("clave"));

	
    fetch('/gcclone/Perfil', {
    	method: 'POST',
    	body: datos_form_editar_perfil,
		mode: "no-cors",
    	headers:{'Content-Type': 'application/json'},
		})
    //RESPUESTA CRUDA DEL SERVER
    .then(response => response.json())
    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
    .then(data => {
        console.log('Respuesta del servidor:', data);
		alert(data.resultado + " Su nuevo usuario es: " + data.nuevo_usuario);
    })	    
	//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
    .catch((error) => {
        console.error('Error:', error);
    });
}
link_guardar_perfil.onclick=guardar_datos_perfil;

//FUNCIONALIDAD PARA ELIMINAR UN PERFIL
var link_borrar_perfil = document.getElementById("link-borrar-perfil");
const borrar_perfil = () => {
	    fetch('/gcclone/Perfil', {
	    	method: 'DELETE',
		})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
			alert(data.resultado);
			if(data.status==200){
				window.open("/gcclone","_self");				
			}
	    })	    
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
}
link_borrar_perfil.onclick=borrar_perfil;

//ACTIVAR LOS MODAL DE MATERIALIZE
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
});