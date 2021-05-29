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
//CONTENEDOR PARA IMPRIMIR LOS NOMBRES DE LOS CALENDARIOS
var div_contenedor_nombres_calendarios = document.getElementById("contenedor-nombres-calendarios");
//ELEMENTOS QUE FORMAN PARTE DEL MODAL EDITAR CALENDARIO
var input_nombre_editar_calendario = document.getElementById("input-nombre-editar-calendario");
var input_color_editar_calendario = document.getElementById("input-color-editar-calendario");
var contenedor_editar_invitados = document.getElementById("contenedor-editar-invitados");

//FUNCION PARA MOSTRAR ADECUADAMENTE LOS DATOS DE LOS CALENDARIOS EN EL ASIDE
var contador_editar_invitados;
var id_calendario_editar;
const mostrar_calendarios_aside = (data)=>{
	for(let i=0;i<data.calendarios.length;i++){
		let p = document.createElement("p");
		p.innerText=data.calendarios[i].nombre_calendario;
		p.id="calendario"+data.calendarios[i].id_calendario;
		p.style='color:'+data.calendarios[i].color;
	
		//COLOCAR LOS DATOS DEL CALENDARIO EN EL MODAL PARA EDITAR CALENDARIO
		p.addEventListener("click",()=>{
			//document.q
			id_calendario_editar = data.calendarios[i].id_calendario;
			console.log(p.id,p.innerText);
			input_nombre_editar_calendario.value=p.innerText;
			input_color_editar_calendario.value=data.calendarios[i].color;
			instancia_modal_editar_calendario.open();
			contador_editar_invitados = data.calendarios[i].invitados.length;
			console.log("Cantidad invitados: " + data.calendarios[i].invitados.length, data.calendarios[i].invitados);
			//let contenedor_input_editar_calendario = document.getElementById("contenedor-input-editar-calendario");
			for(let j=0; j<data.calendarios[i].invitados.length; j++){
				//DIV PARA EL CAMPO DE INVITADO
				let div = document.createElement("div");
				div.className="input-field col s5 offset-s3 invitados";
				let input = document.createElement("input");
				input.type="text";
				input.className="validate selected";
				input.autocomplete="off";
				input.name="input-invitado"+j;
				//input.id="editar-invitado"+j;
				input.placeholder="";
				input.value=data.calendarios[i].invitados[j];
				div.appendChild(input);
				console.log(input);
/*				let label = document.createElement("label");
				label.for="input-invitado"+contador_invitados;
				label.innerText="Invitado";
				div.appendChild(label);*/
				
				//DIV PARA BOTON REMOVER CAMPO INVITADO
				let div_remover = document.createElement("div");
				div_remover.className="col 2 invitados";
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
					contador_editar_invitados--;
				})
				
				contenedor_editar_invitados.appendChild(div);
				contenedor_editar_invitados.appendChild(div_remover);
			}
		});
		
		div_contenedor_nombres_calendarios.appendChild(p);
	}
}
//GUARDAR EDICION CALENDARIO
var link_guardar_edicion_calendario = document.getElementById("link-guardar-edicion-calendario");
var form_editar_calendario = document.getElementById("form-editar-calendario");
const guardar_edicion_calendario = ()=>{
	//OBTENER LOS DATOS DEL FORM DATA DE EDICION DE CALENDARIO
	var datos_form_editar_calendario = new FormData(form_editar_calendario);
	datos_form_editar_calendario.append("cantidad-invitados",contador_editar_invitados);
	datos_form_editar_calendario.append("id-calendario", id_calendario_editar);
	
	var arreglo_invitados = new Array();
	console.log("el contador de editar invitados es: ", contador_editar_invitados);
	for(let i=0; i<contador_editar_invitados; i++){
		//let input_invitado = datos_form_editar_calendario;
		if(datos_form_editar_calendario.get("input-invitado"+i)!=""){
			arreglo_invitados.push(datos_form_editar_calendario.get("input-invitado"+i));
		}
	}
	console.log("El arreglo de invitados es: " + arreglo_invitados);
	
	datos_form_editar_calendario.append("arreglo","['inv1', 'inv2', 'inv3']");
	//CONVERTIR LOS DATOS DE ESE FORM DATA A JSON
	var obj = {};
	datos_form_editar_calendario.forEach(function(valor, llave){
	    obj[llave] = valor;
	});
	var json = JSON.stringify(obj);
	
	//HACER FETCH A CALENDARIO (PUT) CON ESTA DATA DE ABAJO
	console.log(json);
	
    fetch('Calendario', {
    	method: 'PUT',
    	body: json,
    	headers: new Headers({'Content-Type': 'application/json'}),
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
	
	//console.log(datos_form_editar_calendario);
	//console.log("Datos del calendario a guardar: ", input_nombre_editar_calendario.value, input_color_editar_calendario.value);
/*	console.log("guardar estos invitados");
	for(let i=0; i<contador_editar_invitados; i++){
		let input_editar_invitado_n = document.getElementById("editar-invitado"+i);
		console.log(input_editar_invitado_n.value);
	}*/
}
link_guardar_edicion_calendario.onclick=guardar_edicion_calendario;

//CREAR NUEVO CALENDARIO
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
const crear_campo_invitado = (contenedor, edit) => {
	console.log("crear campo invitado", edit);
	if(edit){
		contador_invitados=contador_editar_invitados;
		contador_editar_invitados++
	}
	
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
		contador_invitados--;
	})
	
	contenedor.appendChild(div);
	contenedor.appendChild(div_remover);
	contador_invitados++;
}
link_agregar_invitado.onclick=()=>{crear_campo_invitado(contenedor_input_crear_calendario, false)};

//AGREGAR LA FUNCIONALIDAD DE AGREGAR INVITADO EN EL BOTON DEL MODAL EDITAR CALENDARIO
var link_agregar_invitado_editar = document.getElementById("link-agregar-invitado-editar");
var contenedor_input_editar_calendario = document.getElementById("contenedor-input-editar-calendario");
link_agregar_invitado_editar.onclick=()=>{crear_campo_invitado(contenedor_input_editar_calendario, true)};

//ACTIVAR LOS MODAL DE MATERIALIZE
var instancia_modal_editar_calendario;
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
   
   let options = {
   		onCloseEnd: ()=>{
   			console.log("cerrado")
   			contenedor_editar_invitados.innerHTML='';
   		}
	}
   var modal_editar_calendario = document.querySelector('#modal-editar-calendario');
   instancia_modal_editar_calendario = M.Modal.init(modal_editar_calendario, options);   
});