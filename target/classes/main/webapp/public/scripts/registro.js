/*
 *  registro.js: FUNCIONALIDADES DE LA VISTA DE REGISTRO
 */
var form_registro = document.getElementById("form-registro");
var boton_registro = document.getElementById("boton-registro");

const enviar_datos = () => {
	var datos_form = new FormData(form_registro);
	
	//COMPROBACION QUE LOS CAMPOS QUE INGRESO EL USUARIO ESTEN COMPLETOS
	if(datos_form.get("usuario")=="" || datos_form.get("correo")=="" || datos_form.get("clave")=="" ){
		alert("Llene todos los campos");
	}
	else if(!CorreoValido(datos_form.get("correo"))){
		alert("Ingrese un correo valido");
	}else{
	    fetch('Registro', {
	    	method: 'POST',
	    	body: datos_form,
/*			mode: "no-cors",
	    	headers:{'Content-Type': 'application/json'}*/
		})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data.resultado);
			alert(data.resultado);
			if(data.status==200){
				window.open("/","_self");
			}
	    })	    
		//CATCH PARA OBTENER DETALLE POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
	}

}
boton_registro.onclick=enviar_datos;

function CorreoValido(correo) {
	if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(correo)){
    	return true;
	}
    return false;
}