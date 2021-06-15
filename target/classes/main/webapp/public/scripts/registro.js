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
	if(datos_form.get("usuario")=="" || datos_form.get("correo")=="" || datos_form.get("clave")=="" || datos_form.get("confirmar-clave")==""){
		alert("Llene todos los campos");
	}
	else if(!validarCorreo(datos_form.get("correo"))){
		alert("Ingrese un correo valido");
	}
	else if(datos_form.get("clave").length<6){
		alert("Ingrese una clave con 6 o mas caracteres");
	}
	else if(datos_form.get("clave")!=datos_form.get("confirmar-clave")){
		alert("Confirme correctamente su clave");
		document.getElementById("confirmar-clave").className="invalid";
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