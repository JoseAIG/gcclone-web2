/**
 * login.js: FUNCIONALIDADES DE LA VISTA DE LOGIN
 */

//OBTENER LOS ELEMENTOS DEL LOGIN
var form_login = document.getElementById("form-login");
var boton_login = document.getElementById("boton-login");

//FUNCION PARA ENVIAR LA PETICION E INICIAR SESION
const iniciar_sesion = () => {
	var datos_form = new FormData(form_login);	
	//COMPROBACION QUE LOS CAMPOS QUE INGRESO EL USUARIO ESTEN COMPLETOS
	if(datos_form.get("usuario")=="" || datos_form.get("clave")=="" ){
		alert("Llene todos los campos");
	}else{
	    fetch('Login', {
	    	method: 'POST',
	    	body: datos_form,
		})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        alert(data.resultado);
			if(data.status==200){
				window.open("Dashboard","_self");
			}
	    })
	    //CATCH PARA OBTENER DETALLE POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
	}
}
boton_login.onclick = iniciar_sesion;