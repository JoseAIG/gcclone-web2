/**
 * dashboard-calendario.js: FUNCIONALIDADES DE LOS CALENDARIOS EN EL DASHBOARD
 */

//OBTENER LOS CALENDARIOS DEL USUARIO AL CARGAR LA PAGINA
window.onload=()=>{
	    fetch('Calendario', {
	    	method: 'GET',
	    	headers: {'Content-Type': 'application/json'}
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
			mostrar_calendarios_aside(data);
			agregar_calendarios_opciones_select(data.calendarios);
	    })	    
		//CATCH PARA OBTENER DETALLE POR SI ORURRE UN ERROR
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
var p_informacion_compartido = document.getElementById("p-informacion-compartido");

//FUNCION PARA MOSTRAR ADECUADAMENTE LOS DATOS DE LOS CALENDARIOS EN EL ASIDE
var contador_editar_invitados;
var id_calendario_editar;
const mostrar_calendarios_aside = (data)=>{
	for(let i=0;i<data.calendarios.length;i++){
		//CONTENEDOR "ETIQUETA" CALENDARIO (CHECKBOX, NOMBRE Y BOTON CONFIG)
		let div = document.createElement("div");
		
		//BOTON CONFIGURACION DE CALENDARIO
		let i_tag = document.createElement("i");
		i_tag.className="material-icons right";
		i_tag.textContent="edit";
		i_tag.style='color: gray; cursor: pointer';
		div.appendChild(i_tag);
		
		//CHECKBOX + NOMBRE DEL CALENDARIO
		let p = document.createElement("p");
		//p.innerText=data.calendarios[i].nombre_calendario;
		p.id="calendario"+data.calendarios[i].id_calendario;
		p.style='color:'+data.calendarios[i].color;
				
		let label = document.createElement("label");
		let input_checkbox = document.createElement("input");
		input_checkbox.type="checkbox";
		let span = document.createElement("span");
		span.className="grey lighten-2";
		span.innerText=data.calendarios[i].nombre_calendario;
		span.style='color:'+data.calendarios[i].color;
		label.appendChild(input_checkbox);
		label.appendChild(span);
		p.appendChild(label);
		div.appendChild(p);
		
	
		//COLOCAR LOS DATOS DEL CALENDARIO EN EL MODAL PARA EDITAR CALENDARIO
		i_tag.addEventListener("click",()=>{
			//FUNCIONALIDAD BOTON ELIMINAR CALENDARIO EN FUNCION DE SU ID
			//eliminar_calendario(data.calendarios[i].id_calendario);
			//COLOCAR LOS DATOS DEL CALENDARIO EN LOS CAMPOS DEL MODAL
			id_calendario_editar = data.calendarios[i].id_calendario;
			input_nombre_editar_calendario.value=p.innerText;
			input_color_editar_calendario.value=data.calendarios[i].color;
			instancia_modal_editar_calendario.open();
			//contador_editar_invitados = data.calendarios[i].invitados.length;
			//AGREGAR LAS ENTRADAS DE LOS INVITADOS DEL CALENDARIO
			if(data.calendarios[i].invitados!=null){
				contador_editar_invitados = data.calendarios[i].invitados.length;
				
				//FUNCIONALIDAD BOTON ELIMINAR CALENDARIO EN FUNCION DE SU ID Y PRIVILEGIOS DE DUEÑO
				eliminar_calendario(data.calendarios[i].id_calendario, true);
				link_agregar_invitado_editar.style.display='inline-block';
				link_guardar_edicion_calendario.style.display='inline-block';
				input_nombre_editar_calendario.disabled = false;
				input_color_editar_calendario.disabled = false;
				p_informacion_compartido.innerText="Compartido con:";
				
				for(let j=0; j<data.calendarios[i].invitados.length; j++){
					//DIV PARA EL CAMPO DE INVITADO
					let div = document.createElement("div");
					div.className="input-field col s5 offset-s3 invitados";
					let input = document.createElement("input");
					input.type="text";
					input.className="validate selected";
					input.autocomplete="off";
					input.name="input-invitado"+j;
					input.id="editar-invitado"+j;
					input.placeholder="";
					input.value=data.calendarios[i].invitados[j];
					div.appendChild(input);
					console.log(input);
					
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
			}else{
				//FUNCIONALIDAD BOTON ELIMINAR CALENDARIO EN FUNCION DE SU ID Y PRIVILEGIOS DE NO PROPIETARIO (BORRAR DATOS DE EDICION)
				eliminar_calendario(data.calendarios[i].id_calendario, false);
			
				link_agregar_invitado_editar.style.display='none';
				link_guardar_edicion_calendario.style.display='none';
				input_nombre_editar_calendario.disabled = true;
				input_color_editar_calendario.disabled = true;
				console.log(link_guardar_edicion_calendario);
				p_informacion_compartido.innerText="Eres un invitado en este calendario!";
			}
			
			
		});
		
		//FUNCIONALIDAD CLICK DE LOS CHECKBOXES
		input_checkbox.addEventListener('click',()=>{
			//AL CLICKEAR UN CHECKBOX SE COMPRUEBA SU ESTADO. SI ES SELECCIONADO SE LLAMA A LA FUNCION "toggle_datos_calendarios" EN "dashboard-actividades.js"
			//SI EL CHECKBOX ESTA ACTIVO, SE GUARDAN LOS DATOS EN EL ARREGLO DE DATOS CALENDARIOS SELECCIONADOS EN "dashboard-actividades.js", SINO, SE REMUEVE (PRIMER PARAMETRO true)
			if(input_checkbox.checked){
				toggle_datos_calendarios(false,data.calendarios[i]);
			}else{
				toggle_datos_calendarios(true,data.calendarios[i]);			
			}
		});
		
		div_contenedor_nombres_calendarios.appendChild(div);
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
	
    fetch('Calendario', {
    	method: 'PUT',
    	body: datos_form_editar_calendario
	})
    //RESPUESTA CRUDA DEL SERVER
    .then(response => response.json())
    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
    .then(data => {
        console.log('Respuesta del servidor:', data);
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
link_guardar_edicion_calendario.onclick=guardar_edicion_calendario;

//CREAR NUEVO CALENDARIO
var link_guardar_nuevo_calendario = document.getElementById("link-guardar-nuevo-calendario");
var form_crear_calendario = document.getElementById("form-crear-calendario");
const guardar_nuevo_calendario = ()=>{
	var datos_form_crear_calendario = new FormData(form_crear_calendario);
	datos_form_crear_calendario.append("cantidad-invitados", contador_invitados);
	for(let i=0;i<contador_invitados;i++){
		if(datos_form_crear_calendario.get("input-invitado"+i)!=null){
			for(let j=0; j<contador_invitados;j++){
				if(i!=j && datos_form_crear_calendario.get("input-invitado"+i)==datos_form_crear_calendario.get("input-invitado"+j)){
					alert("No puede duplicarse un invitado");
					return;
				}
			}
		}
	}
	
	if(datos_form_crear_calendario.get("nombre-calendario")==""){
		alert("Ingrese un nombre para el calendario");
	}else{
	    fetch('Calendario', {
	    	method: 'POST',
	    	body: datos_form_crear_calendario,
			mode: "no-cors",
	    	headers: {'Content-Type': 'application/json'}
		})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data.resultado);
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
	input.id="input-invitado"+contador_invitados;
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
		if(edit){
			contador_editar_invitados--
		}else{
			contador_invitados--;
		}
	})
	
	contenedor.appendChild(div);
	contenedor.appendChild(div_remover);
	contador_invitados++;
	console.log("contador editar invitados: " + contador_editar_invitados, "contador invitados: " + contador_invitados);
}
link_agregar_invitado.onclick=()=>{crear_campo_invitado(contenedor_input_crear_calendario, false)};

//AGREGAR LA FUNCIONALIDAD DE AGREGAR INVITADO EN EL BOTON DEL MODAL EDITAR CALENDARIO
var link_agregar_invitado_editar = document.getElementById("link-agregar-invitado-editar");
var contenedor_input_editar_calendario = document.getElementById("contenedor-input-editar-calendario");
link_agregar_invitado_editar.onclick=()=>{crear_campo_invitado(contenedor_input_editar_calendario, true)};

//ELIMINACION DE UN CALENDARIO
var link_borrar_calendario = document.getElementById("link-borrar-calendario");
function eliminar_calendario (id_calendario, propietario) {
	link_borrar_calendario.onclick=()=>{
	let form_peticion = new FormData();
	form_peticion.append("id-calendario",id_calendario);
	form_peticion.append("propietario",propietario);
	    fetch('Calendario', {
	    	method: 'DELETE',
	    	body: form_peticion,
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
			alert(data.resultado);
			if(data.status==200){
				window.open("Dashboard","_self");
			}
	    })	    
		//CATCH PARA OBTENER DETALLE POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
	};
}

//HABILITAR MODAL MATERIALIZE
var instancia_modal_editar_calendario;
document.addEventListener('DOMContentLoaded', function() {
   var elems = document.querySelectorAll('.modal');
   var instances = M.Modal.init(elems);
   
   let options = {
   		onCloseEnd: ()=>{
   			contenedor_editar_invitados.innerHTML='';
   		}
	}
   var modal_editar_calendario = document.querySelector('#modal-editar-calendario');
   instancia_modal_editar_calendario = M.Modal.init(modal_editar_calendario, options); 
});