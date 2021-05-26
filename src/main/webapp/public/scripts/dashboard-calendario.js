/**
 * 
 */

/*var link_crear_calendario = document.getElementById("link-crear-calendario");
link_crear_calendario.addEventListener('click',()=>{
	console.log("crear calendario");
});*/

window.onload=()=>{
	    fetch('/gcclone/Calendario', {
	    	method: 'GET',
/*	    	body: datos_form_crear_calendario,
			mode: "no-cors",
	    	headers: new Headers({'Content-Type': 'application/json'}),*/
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
			console.log(data.resultado);
			mostrar_calendarios_aside(data);
/*			console.log(data.calendarios);
			console.log("Longitud array calendarios: " + data.calendarios.length);
			console.log(data.calendarios[0]);
			console.log(data.calendarios[0].nombre_calendario);*/

/*			if(data.status==200){
				window.open("/gcclone","_self");
			}*/
	    })	    
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
}

var div_contenedor_nombres_calendarios = document.getElementById("contenedor-nombres-calendarios");
const mostrar_calendarios_aside = (data)=>{
	for(let i=0;i<data.calendarios.length;i++){
		let p = document.createElement("p");
		p.innerText=data.calendarios[i].nombre_calendario;
		p.id="calendario"+data.calendarios[i].id_calendario;
		p.style='color:'+data.calendarios[i].color;
		
		p.addEventListener("click",()=>{console.log(p.id,p.innerText)});
		
		div_contenedor_nombres_calendarios.appendChild(p);
	}
}

var link_guardar_nuevo_calendario = document.getElementById("link-guardar-nuevo-calendario");
//var input_nombre_calendario = document.getElementById("input-nombre-calendario");
var form_crear_calendario = document.getElementById("form-crear-calendario");
const guardar_nuevo_calendario = ()=>{
	var datos_form_crear_calendario = new FormData(form_crear_calendario);
	//console.log("datos form: " + datos_form_crear_calendario.get("nombre-calendario"));
	//console.log("invitados: " + datos_form_crear_calendario.get("input-invitado0"), datos_form_crear_calendario.get("input-invitado1"), contador_invitados);
	datos_form_crear_calendario.append("cantidad-invitados", contador_invitados);
	for(let i=0;i<contador_invitados;i++){
		if(datos_form_crear_calendario.get("input-invitado"+i)!=null){
			console.log(datos_form_crear_calendario.get("input-invitado"+i));
			for(let j=0; j<contador_invitados;j++){
				if(i!=j && datos_form_crear_calendario.get("input-invitado"+i)==datos_form_crear_calendario.get("input-invitado"+j)){
					console.log("Son iguales: ", datos_form_crear_calendario.get("input-invitado"+i), datos_form_crear_calendario.get("input-invitado"+j));
					alert("No puede duplicarse un invitado");
					return;
				}
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
	
	//DIV PARA EL CAMPO DE INVITADO
	let div = document.createElement("div");
	div.className="input-field col s5 offset-s3";
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
	
	//DIV PARA BOTON REMOVER CAMPO INVITADO
	let div_remover = document.createElement("div");
	div_remover.className="col 2";
	div_remover.style="margin-top: 1.5em";
	let a_remover = document.createElement("a");
	a_remover.className="btn-floating waves-effect waves-light red";
	let i_remover = document.createElement("i");
	i_remover.className="material-icons";
	i_remover.innerText="clear";
	a_remover.appendChild(i_remover);
	div_remover.appendChild(a_remover);
	
	a_remover.addEventListener("click",()=>{
		div.remove();
		div_remover.remove();
		//contador_invitados--;
	})
	
	contenedor_input_crear_calendario.appendChild(div);
	contenedor_input_crear_calendario.appendChild(div_remover);
	contador_invitados++;
}
link_agregar_invitado.onclick=crear_campo_invitado;

//prueba put
var prueba_put = document.getElementById("prueba-put");
const fetchput = () => {
	let data = {username: 'example'};
	
	    fetch('/gcclone/Calendario', {
	    	method: 'PUT',
	    	body: JSON.stringify(data),
			//mode: "no-cors",
	    	headers: new Headers({'Content-Type': 'application/json'}),
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data.resultado);
			alert(data.resultado);
/*			if(data.status==200){
				window.open("/gcclone","_self");
			}*/
	    })	    
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
}
prueba_put.onclick=fetchput;

//ACTIVAR LOS MODAL DE MATERIALIZE
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
});