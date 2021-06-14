/*
 *  registro.js: FUNCIONALIDADES DE LA VISTA DE REGISTRO
 */
 
//OBTENER ELEMENTOS DEL REGISTRO
var form_registro = document.getElementById("form-registro");
var boton_registro = document.getElementById("boton-registro");

//FUNCION PARA ENVIAR LA PETICION DEL REGISTRO
const enviar_datos = () => {
	var datos_form = new FormData(form_registro);
	
	//COMPROBACION QUE LOS CAMPOS QUE INGRESO EL USUARIO ESTEN COMPLETOS
	if(datos_form.get("usuario")=="" || datos_form.get("correo")=="" || datos_form.get("clave")=="" ){
		alert("Llene todos los campos");
	}
	else if(!validarCorreo(datos_form.get("correo"))){
		alert("Ingrese un correo valido");
	}else{
	    fetch('Registro', {
	    	method: 'POST',
	    	body: datos_form,
		})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
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

//FUNCION PARA VALIDAR UN CORREO ELECTRONICO
function validarCorreo(correo) {
	if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(correo)){
    	return true;
	}
    return false;
}