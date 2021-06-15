/**
 * dashboard-aviso.js: CONTIENE LAS FUNCIONALIDADES PARA MOSTRAR LAS NOTIFICACIONES (AVISOS) DEL ESTADO DE LAS ACTIVIDADES DE UN DIA
 */

let div_hora = document.getElementsByClassName("div-hora");
let contenedor_notificaciones = document.getElementById("contenedor-notificaciones");

function mostrar_avisos(actividades_semana){
	
	//LIMPIAR EL CONTENEDOR DE LAS NOTIFICACIONES
	contenedor_notificaciones.innerHTML='';

	//DETERMINAR EL BLOQUE DE HORAS ACTUALES	
	let fecha_actual = new Date();
	let bloque_actual = fecha_actual.getHours();
	if(fecha_actual.getMinutes()>=30){
		bloque_actual+=".5";
	}
	
	console.log(actividades_semana);
		
	//SE RECORREN TODAS LAS ACTIVIDADES DE LA SEMANA
	for(let i=0; i<actividades_semana.length;i++){
		//SE CREA UN NUEVO OBJETO DATE CON LA FECHA DE LA ACTIVIDAD
		let fecha_evento = new Date(actividades_semana[i].fecha);
		//SI EL NUMERO DE DIA DEL EVENTO DE LA SEMANA ES EL MISMO NUMERO DE DIA DEL DIA ACTUAL Y LA ACTIVIDAD SE ENCUENTRA EN EL ANIO ACTUAL SE NOTIFICARA
		if(fecha_evento.getDate()+1 == fecha_actual.getDate() && fecha_evento.getFullYear() == fecha_actual.getFullYear()){
			//EL BLOQUE ACTUAL ES IGUAL AL FINAL DE UNA ACTIVIDAD, SE NOTIFICA QUE ESTA POR ACABAR (IMPORTANTE = TRUE)
			if(parseFloat(actividades_semana[i].hora_fin)==parseFloat(bloque_actual)){
				crear_notificacion("Esta por acabar la actividad: "+actividades_semana[i].informacion, true);
			}
			//SI EL BLOQUE DE HORA ACTUAL SE ENCUENTRA DENTRO DEL RANGO DE HORAS DE LA ACTIVIDAD, SE NOTIFICA QUE LA ESTA ESTA EN CURSO
			else if(parseFloat(actividades_semana[i].hora_inicio)<=parseFloat(bloque_actual) && parseFloat(bloque_actual)<=parseFloat(actividades_semana[i].hora_fin)){
				crear_notificacion("Esta en curso la actividad: " + actividades_semana[i].informacion, false);
			}
			//SI EL BLOQUE DE HORA ACTUAL ES MAYOR A LA HORA FIN DE LA ACTIVIDAD, SE NOTIFICA QUE LA ACTIVIDAD HA ACABADO
			else if(parseFloat(actividades_semana[i].hora_fin)<parseFloat(bloque_actual)){
				crear_notificacion("Ha acabado la actividad: " + actividades_semana[i].informacion, false);
			}
		}
	}
}

//FUNCION PARA CREAR NOTIFICACIONES EN EL DIV CONTENEDOR DE NOTIFICACIONES
function crear_notificacion(texto, importante){
	//DIV PRINCIPAL DE LA NOTIFICACION
	let div = document.createElement("div");
	div.className = "div-alerta valign-wrapper";
	
	//SI EL PARAMETRO "importante" ES TRUE, AGREGAR LA CLASE PULSE AL DIV DE LA NOTIFICACION
	if(importante){
		div.classList.add("pulse");
	}
	
	//TEXTO DE LA NOTIFICACION
	let p = document.createElement("p");
	p.className = "texto-notificacion";
	p.textContent=texto;
	div.appendChild(p);
	
	//BOTON QUITAR NOTIFICACION
	let a = document.createElement("a");
	a.className="btn-floating btn-small waves-effect waves-light red boton-quitar-notificacion";
	
	let i = document.createElement("i");
	i.className="material-icons";
	i.textContent="clear";
	
	a.appendChild(i);
	div.appendChild(a);
	contenedor_notificaciones.appendChild(div);
	
	//EVENTO QUITAR NOTIFICACION
	a.addEventListener('click',()=>{
		div.classList.add("remover");
		div.addEventListener("transitionend",()=>{
			div.remove();			
		})
	});
}