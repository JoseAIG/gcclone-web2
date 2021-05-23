/**
 * 
 */

var link_cerrar_sesion = document.getElementById("link-cerrar-sesion");

const cerrar_sesion = ()=>{
	    fetch('http://localhost:8080/gcclone/Dashboard', {
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

var link_editar_perfil = document.getElementById("link-editar-perfil");

var input_editar_nombre_usuario = document.getElementById("editar-usuario");
var input_editar_correo = document.getElementById("editar-correo");
var input_editar_clave = document.getElementById("editar-clave");


const obtener_datos_perfil = () => {
	    fetch('http://localhost:8080/gcclone/Dashboard', {
	    	method: 'OPTIONS',
	    	//body: datos_form,
			//mode: "no-cors",
	    	//headers: new Headers({'Content-Type': 'application/json'}),
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
/*			alert(data.resultado);
			window.open("/gcclone","_self");*/
			input_editar_nombre_usuario.value=data.usuario;
			input_editar_correo.value=data.correo;
			input_editar_clave.value=data.clave;
	    })	    
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
}

link_editar_perfil.onclick=obtener_datos_perfil;

//ACTIVAR LOS MODAL DE MATERIALIZE
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
});