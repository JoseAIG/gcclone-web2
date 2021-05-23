/**
 * 
 */

/*var link_crear_calendario = document.getElementById("link-crear-calendario");
link_crear_calendario.addEventListener('click',()=>{
	console.log("crear calendario");
});*/

var link_guardar_nuevo_calendario = document.getElementById("link-guardar-nuevo-calendario");
//var input_nombre_calendario = document.getElementById("input-nombre-calendario");
var form_crear_calendario = document.getElementById("form-crear-calendario");
guardar_nuevo_calendario = ()=>{
	var datos_form_crear_calendario = new FormData(form_crear_calendario);
	console.log("datos form: " + datos_form_crear_calendario.get("nombre-calendario"));
	//console.log("guardar nuevo calendario");
	//console.log(input_nombre_calendario.value);
	if(datos_form_crear_calendario.get("nombre-calendario")==""){
		alert("Ingrese un nombre para el calendario");
	}else{
		console.log("ok..");
	    fetch('/gcclone/Calendario', {
	    	method: 'POST',
	    	body: datos_form_crear_calendario,
			mode: "no-cors",
	    	headers: new Headers({'Content-Type': 'application/json'}),
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data.resultado);
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
	//window.open("Dashboard","_self");
}
link_guardar_nuevo_calendario.onclick=guardar_nuevo_calendario;

//ACTIVAR LOS MODAL DE MATERIALIZE
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
});