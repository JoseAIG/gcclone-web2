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
const guardar_nuevo_calendario = ()=>{
	var datos_form_crear_calendario = new FormData(form_crear_calendario);
	console.log("datos form: " + datos_form_crear_calendario.get("nombre-calendario"));
	//console.log("invitados: " + datos_form_crear_calendario.get("input-invitado0"), datos_form_crear_calendario.get("input-invitado1"), contador_invitados);
	datos_form_crear_calendario.append("cantidad-invitados", contador_invitados);
	for(let i=0;i<contador_invitados;i++){
/*		if(datos_form_crear_calendario.get("input-invitado"+i)!=""){
			console.log(datos_form_crear_calendario.get("input-invitado"+i));
		}*/
		for(let j=0; j<contador_invitados;j++){
			if(i!=j && datos_form_crear_calendario.get("input-invitado"+i)==datos_form_crear_calendario.get("input-invitado"+j)){
				console.log("Son iguales: ", datos_form_crear_calendario.get("input-invitado"+i), datos_form_crear_calendario.get("input-invitado"+j));
				alert("No puede duplicarse un invitado");
				return;
			}
		}
	}
	
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

//FUNCIONALIDAD BOTON AGREGAR INVITADO
var link_agregar_invitado = document.getElementById("link-agregar-invitado");
var contenedor_input_crear_calendario = document.getElementById("contenedor-input-crear-calendario");
var contador_invitados=0;
const crear_campo_invitado = () => {
	console.log("crear campo invitado");
	
	let div = document.createElement("div");
	div.className="input-field col s6 offset-s3";
	let input = document.createElement("input");
	input.type="text";
	input.className="validate selected";
	input.autocomplete="off";
	input.name="input-invitado"+contador_invitados;
	//input.placeholder="invitado";
	div.appendChild(input);
	let label = document.createElement("label");
	label.for="input-invitado"+contador_invitados;
	label.innerText="Invitado";
	div.appendChild(label);
	
	contenedor_input_crear_calendario.appendChild(div);
	contador_invitados++;
}
link_agregar_invitado.onclick=crear_campo_invitado;
//ACTIVAR LOS MODAL DE MATERIALIZE
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
});