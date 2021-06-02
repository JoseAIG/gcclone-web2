/**
 * 
 */

var form_login = document.getElementById("form-login");
var boton_login = document.getElementById("boton-login");

const iniciar_sesion = () => {
	var datos_form = new FormData(form_login);
	console.log(datos_form,datos_form.get("usuario"),datos_form.get("clave"));
	
	//COMPROBACION QUE LOS CAMPOS QUE INGRESO EL USUARIO ESTEN COMPLETOS
	if(datos_form.get("usuario")=="" || datos_form.get("clave")=="" ){
		alert("Llene todos los campos");
	}else{
	    fetch('Login', {
	    	method: 'POST',
	    	body: datos_form,
			mode: "no-cors",
	    	headers: new Headers({'Content-Type': 'application/json'}),
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
	        alert(data.resultado);
			if(data.status==200){
				//console.log("redireccionar aqui");
				//alert("redireccionar!");
				window.open("Dashboard","_self");
			}
	    })
	    //CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
	}
}

boton_login.onclick = iniciar_sesion;