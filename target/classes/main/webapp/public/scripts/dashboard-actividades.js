/**
 *  "dashboard-actividades.js" MANEJA TODAS LAS FUNCIONALIDADES REFERENTES AL MANEJO DE LAS ACTIVIDADES Y LA PLANTILLA SEMANAL DEL CALENDARIO
 */

//PROTOTYOE numeroSemana PARA OBTENER EL NUMERO DE LA SEMANA EN UN ANIO DE UNA FECHA
Date.prototype.numeroSemana = function() {
	//SE OBTIENE EL PRIMER DIA DEL ANIO ACTUAL
	let primer_dia_anio = new Date(this.getFullYear(),0,1);
	let dia_semana_primero_enero = primer_dia_anio.getDay()+1;
	//OBTENER DIFERENCIA DE TIEMPO ENTRE LAS FECHAS EN MILISEGUNDOS Y DIVIDIRLO ENTRE LOS MILISEGUNDOS EN UN DIA PARA OBTENER LOS DIAS DE DIFERENCIA ENTRE LAS FECHAS
	let dias_diferencia_primero_enero = (this - primer_dia_anio) / 86400000;
	//SUMAR LOS DIAS DE DIFERENCIA DESDE EL PRIMERO DE ENERO HASTA LA FECHA CON EL NUMERO DE DIA DE SEMANA DE PRIMERO DE ENERO PARA OBTENER LA DIFERENCIA DE DIAS DESDE EL PRINCIPIO DE LA PRIMERA SEMANA
	let diferencia_dias_inicio_primera_semana = (dias_diferencia_primero_enero + dia_semana_primero_enero);
	//PASAR LA DIFERENCIA DE DIAS A SEMANAS Y RETORNAR RESPUESTA
	let diferencia_semanas = Math.ceil(diferencia_dias_inicio_primera_semana/7);
	return diferencia_semanas;   
};

//OBTENER EL SELECTOR DE LA FECHA Y ASIGNARLE POR DEFECTO LA FECHA ACTUAL
let selectorFecha = document.getElementById("selector-fecha");
selectorFecha.valueAsDate= new Date();
//dibujar_plantilla(selectorFecha.value);

//EJECUTAR LA FUNCION "dibujar_plantilla" CADA VEZ QUE SE CAMBIA UN VALOR EN EL SELECTOR DE LA FECHA
selectorFecha.addEventListener('change',()=>{dibujar_plantilla(selectorFecha.value)})

//FUNCION LLAMADA CUANDO SE SELECCIONA UN CHECKBOX EN "dashboard-calendario.js"
var datos_calendarios = new Array();
function toggle_datos_calendarios(remover,datos){
	//SI SE PASA TRUE EN EL PARAMETRO REMOVER, RECORRER EL ARREGLO DE LOS DATOS DEL CALENDARIO Y REMOVER EXACTAMENTE LOS DATOS DE UN CALENDARIO ESPECIFICO UNICAMENTE
	if(remover){
		for(let i=0; i<datos_calendarios.length; i++){
			if(datos_calendarios[i]==datos){
				datos_calendarios.splice(i,1);
			}
		}
		//SE DIBUJA LA PLANTILLA SEMANAL CON LA FECHA QUE POSEE EL SELECTOR
		dibujar_plantilla(selectorFecha.value);
	}else{
		//SI REMOVER ES FALSE (AGREGAR DATOS). SE AGREGAN LOS DATOS DEL CALENDARIO EN EL ARREGLO
		datos_calendarios.push(datos);		
		//SE DIBUJA LA PLANTILLA SEMANAL CON LA FECHA QUE POSEE EL SELECTOR
		dibujar_plantilla(selectorFecha.value);
	}
}

//FUNCION PARA INCREMENTAR O DECREMENTAR EN UNA SEMANA LA FECHA DEL INPUT TYPE DATE CON BOTONES Y DIBUJAR LA PLANTILLA SEMANAL
var boton_anterior_semana = document.getElementById("anterior-semana");
var boton_siguiente_semana = document.getElementById("siguiente-semana");
function cambiar_semana(incrementar){
	//SE CREA UNA NUEVA FECHA EN BASE A LA DEL SELECTOR, SI SE INCREMENTA AUMENTA LA FECHA EN 7 DIAS, SINO, LA DECREMENTA EN 7
	let nueva_fecha = new Date(selectorFecha.value);
	if(incrementar){
		nueva_fecha.setDate(nueva_fecha.getDate()+7);	
	}else{
		nueva_fecha.setDate(nueva_fecha.getDate()-7);	
	}
	//ASIGNAR NUEVO VALOR COMO FECHA AL SELECTOR DE LA FECHA PARA DIBUJAR LA PLANTILLA SEMANAL CON LA FECHA DEL SELECTOR
	selectorFecha.valueAsDate = nueva_fecha;
	dibujar_plantilla(selectorFecha.value);
}
boton_siguiente_semana.addEventListener('click',()=>{cambiar_semana(true)});
boton_anterior_semana.addEventListener('click',()=>{cambiar_semana(false)});


//FUNCION PARA CORREGIR UNA FECHA Y OBTENER EL NUMERO DE LA SEMANA DE LA FECHA DE UNA ACTIVIDAD
function obtener_numero_semana(fecha_actividad){
	let fecha = new Date(fecha_actividad);
	let dia = fecha.getDate();
	let mes = fecha.getMonth();
	let anio = fecha.getFullYear();
	let fecha_corregida = new Date(anio,mes,(dia+1));
		
	return fecha_corregida.numeroSemana();
}

//VARIABLE DE FECHA PRINCIPAL CON LA QUE SE MANEJARAN LAS ACTIVIDADES DE UNA SEMANA, LA FECHA PRINCIPAL SE CALCULA EN LA FUNCION "dibujar_plantilla"
var fechaPrincipal;
//ARREGLOS PARA MANEJAR LAS ACTIVIDADES EN LA SEMANA
var actividades_en_la_semana = new Array();
var colores_actividades = new Array();
var privilegios_actividades = new Array();
//FUNCION PARA CALCULAR LAS ACTIVIDADES DE UNA SEMANA
function obtener_actividades_semana(){
	//LIMPIAR ARREGLOS DE LAS ACTIVIDADES DE LA SEMANA
	actividades_en_la_semana = [];
	colores_actividades = [];
	privilegios_actividades = [];
	//SE RECORREN TODOS LOS CALENDARIOS DE LOS CHECKBOXES MARCADOS
	for(let i=0; i<datos_calendarios.length; i++){
		//SE RECORREN TODAS LAS ACTIVIDADES DE CADA CALENDARIO
		for(let j=0; j<datos_calendarios[i].actividades.length; j++){
			//SI EL NUMERO DE LA SEMANA DE UNA ACTIVIDAD ES IGUAL AL NUMERO DE LA SEMANA DE LA FECHA PRINCIPAL, SE GUARDA LA ACTIVIDAD EN EL ARREGLO DE ACTIVIDADES DE LA PRESENTE SEMANA
			let semana_fecha_principal = fechaPrincipal.numeroSemana();
			if(obtener_numero_semana(datos_calendarios[i].actividades[j].fecha)==semana_fecha_principal){
				actividades_en_la_semana.push(datos_calendarios[i].actividades[j]);
				colores_actividades.push(datos_calendarios[i].color);
				//SI EL CALENDARIO TIENE INVITADOS NULL, SIGNIFICA QUE NO TIENE PRIVILEGIOS DE EDICION EN ESTE, POR ENDE NO TIENE EN LAS ACTIVIDADES
				if(datos_calendarios[i].invitados==null){
					privilegios_actividades.push(false);
				}else{
					privilegios_actividades.push(true);
				}
			}
		}
	}
	//FUNCION PARA MOSTRAR LAS NOTIFICACIONES DE LAS ACTIVIDADES EN EL DIA EN "dashboard-aviso.js"
	mostrar_avisos(actividades_en_la_semana);
}

//ARREGLO QUE CONTIENE LOS NOMBRES DE LOS MESES DE UN ANIO
const nombre_meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

//ARREGLO QUE CONTIENE LOS NOMBRES DE LOS DIAS DE LA SEMANA
const nombre_dias = ["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"];

//LLAMADA A LA FUNCION DIBUJAR PLANTILLA LA PRIMERA VEZ QUE SE CORRE LA APP
dibujar_plantilla(selectorFecha.value);

//FUNCION PARA DIBUJAR LA PLANTILLA SEMANAL EN EL SECTION DE LA APLICACION DADA UNA FECHA
function dibujar_plantilla(fecha_a_dibujar){
	//OBTENER LOS DATOS DE LAS FECHAS DEL INPUT DE FORMA INDIVIDUAL
	let fecha = new Date(fecha_a_dibujar);
	let dia = fecha.getDate();
	let mes = fecha.getMonth();
	var anio = fecha.getFullYear();

	//CORREGIR FECHA SUMANDOLE 1 DIA PARA OBTENER LA FECHA PRINCIPAL
	fechaPrincipal = new Date(anio,mes,(dia+1));
	
	//OBTENER LAS ACTIVIDADES DE LA SEMANA EN BASE A LA FECHA PRINCIPAL CALCULADA
	obtener_actividades_semana();

	//OBTENER EL H4 QUE CONTIENE EL NOMBRE DEL MES DE LA FECHA Y COLOCAR EL NOMBRE DEL MES CORRESPONDIENTE
	let informacion_mes_fecha = document.getElementById("informacion-mes-fecha");
	informacion_mes_fecha.innerText=nombre_meses[mes];

	let div_dia = [];
	//OBTENER LOS NUMEROS DE LOS DIAS DE LA SEMANA DADA LA FECHA PRINCIPAL
	let diasSemana = obtenerDiasSemana(fechaPrincipal);
	//RECORRER LOS DIVS DIAS PARA COLOCAR LOS DIVS DE LAS HORAS DENTRO DE CADA UNO
	for(let i=0; i<7; i++){
		//INGRESAR LOS VALORES BASICOS EN LOS DIVS DE LOS DIAS
	    div_dia[i] = document.getElementById("div"+i);
	    div_dia[i].innerHTML = nombre_dias[i] + `<br>` + diasSemana[i] + `<hr>`;
	    
	    //SI EL DIA DE LA FECHA ACTUAL ES IGUAL AL DIA DE LA SEMANA Y EL MES ACTUAL ES IGUAL AL MES DE LA FECHA PRINCIPAL Y EL SELECTOR SE ENCUENTRA EN EL ANIO ACTUAL, COLOREAR EL DIV DIA DE AZUL REPRESENTANDO EL DIA ACTUAL
	    if(new Date().getDate()==diasSemana[i] && new Date().getMonth()==fechaPrincipal.getMonth() && new Date().getFullYear()==anio){
	    	div_dia[i].style.backgroundColor="#448aff";
	    }else{
	    	div_dia[i].style.backgroundColor="#fafafa";
	    }
	    
	    //AÑADIDO DE DIVS DE LAS HORAS EN LOS DIVS DE LOS DIAS
	    for(let j=0; j<24; j=(j+0.5)){
			if(j%1==0){
				//SI ES UNA HORA EN PUNTO
	            div_dia[i].innerHTML += `<div  class="div-hora hoverable" dia=${i} hora=${j} numerodia="${parseInt(diasSemana[i])}"><p class="numero-bloque-hora">${j+":00"}</p></div> <br>`
			}else{
				//SI ES UNA HORA Y MEDIA
	        	div_dia[i].innerHTML += `<div  class="div-hora hoverable" dia=${i} hora=${j} numerodia="${parseInt(diasSemana[i])}"><p class="numero-bloque-hora">${(j-0.5)+":30"}</p></div> <br>`				
			}
	    }
	}
		
	//RECORRIDO FINAL DE LAS ACTIVIDADES POR LOS DIVS DE LAS HORAS
	for(let i=0; i<actividades_en_la_semana.length; i++){
		//GENERAR UN OBJETO DATE CON LA FECHA DE CADA ACTIVIDAD 
		let dia_actividad = new Date(actividades_en_la_semana[i].fecha);
		dia_actividad.setDate(dia_actividad.getDate()+1);
		
		//RECORRER LOS BLOQUES DE HORAS Y COMPARAR CON LOS ATRIBUTOS DE LAS ACIVIDADES PARA PINTARLAS
		let flag = false; 	//ESTE FLAG ES PARA REALIZAR EL ANIADIDO ADICIONAL DE DISENIO AL PRIMER DIV DE LA ACTIVIDAD
		for(let j=0; j<div_hora.length; j++){
			//SI LOS BLOQUES DE HORAS DE LAS ACTIVIDADES SE ENCUENTRAN DENTRO DE LOS DIVS HORAS RECORRIDOS SIEMPRE Y CUANDO SE ENCUENTREN EN EL MISMO DIA Y ANIO, SE PLASMA EN LA PLANTILLA
			if((parseInt(actividades_en_la_semana[i].hora_inicio))<=(parseInt(div_hora[j].getAttribute("hora"))) && (parseInt(div_hora[j].getAttribute("hora")))<=(parseInt(actividades_en_la_semana[i].hora_fin-0.5)) && div_hora[j].getAttribute("dia") == dia_actividad.getDay() && dia_actividad.getFullYear() == anio){
				if(!flag){					
					//SI LA ACTIVIDAD COMIENZA EN UNA MEDIA HORA AUMENTAR EN UNA UNIDAD EL CONTEO INICIAL PARA EL DIBUJADO DE LA ACTIVIDAD EN PLANTILLA
					if(actividades_en_la_semana[i].hora_inicio%1!=0){
						j++;
					}
					
					//INCLUIR EL DISENIO DEL DIV PRINCIPAL DE LA ACTIVIDAD
					div_hora[j].style.opacity='1';
					div_hora[j].innerHTML+=actividades_en_la_semana[i].informacion;
					//SI LA ACTIVIDAD TIENE UNA IMAGEN, MOSTRARLA EN EL SEGUNDO DIV DE LA ACTIVIDAD
					if(actividades_en_la_semana[i].ruta_imagen){
			        	div_hora[j+1].innerHTML+=`<span class="span-imagen-actividad"><img alt="img" src="${actividades_en_la_semana[i].ruta_imagen}" class="imagen-actividad"></span>`;					
					}
			        
			        //AGREGAR BOTON EDITAR Y CONFIGURAR EL EVENTO PARA EDITAR ACTIVIDAD EN EL PRIMER DIV DE LA ACTIVIDAD SI EL USUARIO POSEE PRIVILEGIOS DE PROPIETARIO EN EL CALENDARIO, POR ENDE EN LA ACTIVIDAD
			        if(privilegios_actividades[i]){
			        	let link_editar_actividad = document.createElement("a");
			        	link_editar_actividad.className="waves-effect waves-light modal-trigger link-editar-actividad";
			        	link_editar_actividad.setAttribute("data-target", "modal-editar-actividad");
			        	link_editar_actividad.innerHTML= `<i class="material-icons">edit</i>`;
    					div_hora[j].appendChild(link_editar_actividad);
						link_editar_actividad.addEventListener('click',()=>{
							
							//SI LA ACTIVIDAD POSEE UNA IMAGEN MOSTRARLA EN LA PREVISUALIZACION
							if(actividades_en_la_semana[i].ruta_imagen){
								previsualizar_imagen_editar_actividad.style.display="inline-block";
								contenedor_imagen_previsualizar.innerHTML+=`<img alt="img" src="${actividades_en_la_semana[i].ruta_imagen}" class="imagen-previsualizada">`;							
							}else{
								previsualizar_imagen_editar_actividad.style.display="none";
							}
							
							//ASIGNAR EL ID DE LA ACTIVIDAD QUE A MODIFICAR
							id_actividad_editar = div_hora[j].getAttribute("id-actividad");
							//COLOCAR LOS DATOS DE LA ACTIVIDAD EN LOS CAMPOS DEL MODAL EDITAR ACTIVIDAD
							input_detalle_editar_actividad.value = actividades_en_la_semana[i].informacion;
							fecha_editar_actividad.value = actividades_en_la_semana[i].fecha;
							
							//ASIGNAR LOS VALORES EN LOS RANGO DE HORAS PARA EDICION (0 CREAR - 1 EDITAR)
							rango_hora_inicio[1].value= actividades_en_la_semana[i].hora_inicio;
							rango_hora_fin[1].value = actividades_en_la_semana[i].hora_fin;
							cambio_rango_horas(1); //FUNCION PARA REALIZAR EL DIBUJADO EN FUNCION DE LOS VALORES DE LOS RANGOS DE LAS HORAS
						});
				    }    
					flag=true;
				}
				//PINTAR LOS DIVS SIGUIENTES DE LAS HORAS SEGUN EL COLOR DEL CALENDARIO SIEMPRE QUE LA HORA FIN SEA :00 Y NO SEA 23:30 (PARA QUE NO SE CORRAN LOS BLOQUES DIBUJADOS AL DIA SIGUIENTE)
				if(actividades_en_la_semana[i].hora_fin%1==0 && div_hora[j].getAttribute("hora")!=23.5){
					div_hora[j+1].style.backgroundColor=colores_actividades[i];
				}
				
				//AGREGAR COMO ATRIBUTO EL ID DE LA ACTIVIDAD AL DIV ACTUAL Y PINTARLO DEL COLOR DEL CALENDARIO
				div_hora[j].setAttribute("id-actividad", actividades_en_la_semana[i].id_actividad);
				div_hora[j].style.backgroundColor=colores_actividades[i];
				
			}
			
		}
	}
}

//FUNCION QUE DEVUELVE EL PRIMER DIA DE LA SEMANA DADA UNA FECHA
function obtenerInicioSemana (fecha) {
	//EL PRIMER DIA SERA EL NUMERO DEL DIA DEL MES MENOS EL NUMERO DE DIA DE LA SEMANA DE ESA FECHA
    let numeroPrimerDia = fecha.getDate() - fecha.getDay();
    let fechaPrimerDia = fecha;
    fechaPrimerDia.setDate(numeroPrimerDia);
    return fechaPrimerDia;
}

//FUNCION QUE DEVUELVE EL ULTIMO DIA DE LA SEMANA DADA UNA FECHA
function obtenerFinalSemana (fecha) {
    //EL ULTIMO DIA DE LA SEMANA SERA EL NUMERO DE DIA DEL MES + EL NUMERO DE DIAS DE LA SEMANA EMPEZANDO EN 0 - EL NUMERO DE DIA DE LA SEMANA DE ESA FECHA
    let numeroUltimoDia = fecha.getDate() + 6 - fecha.getDay();
    let fechaUltimoDia = fecha;
    fechaUltimoDia.setDate(numeroUltimoDia);
    return fechaUltimoDia;
}

//FUNCION PARA OBTENER LOS DIAS DE UNA SEMANA DADA UNA FECHA (LA FECHA DEBE SER CORREGIDA, DATE+1)
function obtenerDiasSemana(fecha) {
	let primerDia = obtenerInicioSemana(fecha).getDate();
	let ultimoDia = obtenerFinalSemana(fecha).getDate();
	
	let numeroDiasSemana = [];
	
	//SI EL PRIMER DIA ES MENOR AL ULTIMO DIA DE LA SEMANA, RECORRER LOS DIAS Y GUARDAR EN EL ARREGLO LOS NUMEROS DE LOS DIAS
	if(primerDia<ultimoDia){
	    for(let i=0; i<7; i++){
			//EL NUMERO DEL DIA i ES EL DIA DE LA FECHA MENOS EL NUMERO DEL DIA DE LA SEMANA DE LA FECHA SUMANDOLE LA ITERACION PARA INCREMENTAR LOS DIAS CONSECUTIVAMENTE
			numeroDiasSemana[i] = fecha.getDate() + i - fecha.getDay();
	    }
	}else{
		//PERO SI DENTRO DE LA SEMANA CULMINA UN MES (FECHA INICIAL MAYOR QUE LA FINAL), SE OBTIENE LA FECHA DE INICIO DE LA SEMANA Y SE SETEA EL DIA A 31 PARA OBTENER CON QUE NUMERO DE DIA CULMINA EL MES
	    let fechaPrimerDia = obtenerInicioSemana(fecha);
	    fechaPrimerDia.setDate(31); //SETEANDOLO A 31, SI EL MES TERMINA EN 28, 29 O 30, OBTENDREMOS LA DIFERENCIA (31-N) REPRESENTADA EN EL DIA DEL SIGUIENTE MES
	
		//SI LA RESTA DE 31 CON LA FECHA DEL PRIMER DIA SETEADA EN 31 PARA ES 0, SIGINIFICA QUE EN EFECTO EL MES TERMINA EN EL DIA 31
	    let diaFinMesAnterior = 31-fechaPrimerDia.getDate()
	    if(diaFinMesAnterior==0){
	        diaFinMesAnterior=31;
	    }
	    
	    //SE RECORREN LOS DIAS DE LA SEMANA
	    for(let i=0;i<7;i++){
	        //SI LA SUMA ENTRE EL PRIMER DIA Y EL ITERADOR NO SUPERA EL DIA FIN DEL MES ANTERIOR, EL DIA DE LA SEMANA ES LA SUMA
	        if(!((primerDia+i)>diaFinMesAnterior)){
	            numeroDiasSemana[i] = primerDia + i;
	        }
	        //PERO SI LA SUMA SUPERA EL DIA FIN DEL MES ANTERIOR, EMPIEZA EL CONTEO DEL PROXIMO MES CON EL INDICE DEL ITERADOR COMENZANDO CON EL VALOR ANTERIOR ITERADOR
	        else{
	            for(let j=i;j<7;j++){
	                numeroDiasSemana[j] = j-i+1;
	            }
	            break;
	        }
	    }
	}
	return numeroDiasSemana;
}

//INCLUIR LA LISTA DE CALENDARIOS EN LAS OPCIONES DE LAS ETIQUETAS SELECT PARA CREAR CALENDARIOS (LLAMADA EN "dashboard-calendario.js" AL INICIAR LA PAGINA)
var select_calendario_crear_actividad = document.getElementById('select-calendario-crear-actividad');
function agregar_calendarios_opciones_select(calendarios){
	//RECORRER TODOS LOS CALENDARIOS DEL USUARIO
	for(let i=0; i<calendarios.length; i++){
		//SI INVITADOS != NULL, ES DECIR, ES PROPIETARIO DEL CALENDARIO, AGREGAR OPTION PARA EL SELECT CREAR ACTIVIDAD
		if(calendarios[i].invitados){
			let option = document.createElement("option");
			option.value = calendarios[i].id_calendario;
			option.text = calendarios[i].nombre_calendario;
			select_calendario_crear_actividad.add(option);
		}
	}
	
	//VOLVER A INICIAR LAS ETIQUETAS SELECT
	var elems = document.querySelectorAll('select');
	M.FormSelect.init(elems);
}

//CREAR ACTIVIDAD
var link_guardar_nueva_actividad = document.getElementById("link-guardar-nueva-actividad");
var form_crear_actividad = document.getElementById("form-crear-actividad");
const crear_actividad = () => {
	var form_nueva_actividad = new FormData(form_crear_actividad);
	//COMPROBAR CAMPOS DE LAS ACTIVIDADES
	if(!form_nueva_actividad.get('detalle-actividad') || !form_nueva_actividad.get('fecha-crear-actividad') || form_nueva_actividad.get('select-calendario-crear-actividad')==null){
		alert("Llene todos los campos");
	}else{
	    fetch('Actividad', {
	    	method: 'POST',
	    	body: form_nueva_actividad,
		})
	    .then(response => response.json())
	    .then(data => {
			alert(data.resultado);
			if(data.status==200){
				window.open("Dashboard","_self");
			}
	    })	    
	    .catch((error) => {
	        console.error('Error:', error);
	    });
	}
}
link_guardar_nueva_actividad.onclick=crear_actividad;

//GUARDAR MODIFICACION ACTIVIDAD
var id_actividad_editar;
var link_guardar_editar_actividad = document.getElementById("link-guardar-editar-actividad");
var form_editar_actividad = document.getElementById("form-editar-actividad");
const modificar_actividad = () => {
	var datos_form_editar_actividad = new FormData(form_editar_actividad);
	datos_form_editar_actividad.append("id-actividad", id_actividad_editar);
	fetch('Actividad', {
		method: 'PUT',
		body: datos_form_editar_actividad
	})
	.then(response => response.json())
	.then(data => {
		alert(data.resultado);
		if(data.status==200){
			window.open("Dashboard","_self");
		}
	})	    
	.catch((error) => {
	    console.error('Error:', error);
	});
}
link_guardar_editar_actividad.onclick=modificar_actividad;

//ELIMINAR UNA ACTIVIDAD
var link_eliminar_actividad = document.getElementById("link-eliminar-actividad");
const eliminar_actividad = () =>{		
	let form_eliminar_actividad = new FormData();
	form_eliminar_actividad.append("id-actividad",id_actividad_editar);
	fetch('Actividad', {
		method: 'DELETE',
		body: form_eliminar_actividad
	})
	.then(response => response.json())
	.then(data => {
		alert(data.resultado);
		if(data.status==200){
			window.open("Dashboard","_self");
		}
	})	    
	.catch((error) => {
	    console.error('Error:', error);
	});
}
link_eliminar_actividad.onclick = eliminar_actividad;

//RANGO DE HORAS VANILLA PARA CREAR Y EDITAR ACTIVIDADES
var rango_hora_inicio = document.getElementsByClassName("rango-hora-inicio");
var rango_hora_fin = document.getElementsByClassName("rango-hora-fin");

var label_hora_inicio = document.getElementsByClassName("label-hora-inicio");
var label_hora_fin = document.getElementsByClassName("label-hora-fin");

//AJUSTAR RANGO DE HORAS. ESTA FUNCION RECIBE POR PARAMETROS UN INDICE i, SI ES 0 ES PARA AJUSTAR LA INFORMACION PARA CREAR ACTIVIDAD Y SI ES 1 ES PARA EDITAR
function cambio_rango_horas (i) {	
	//SI EL RANGO DE HORA FIN ES INFERIOR O IGUAL AL RANGO DE HORA INICIO, COLOCARLE A ESTE PRIMERO EL VALOR DE LA HORA INICIO Y HACER UN STEP UP (INCREMENTAR EN 0.5) PARA MANTENER LOS BLOQUES DE HORAS CONTINUOS
	if(parseFloat(rango_hora_fin[i].value)<=parseFloat(rango_hora_inicio[i].value)){
		rango_hora_fin[i].value=rango_hora_inicio[i].value;
		rango_hora_fin[i].stepUp(1);
	}
	
	//COLOCAR LA INFORMACION DE LA HORA DEL RANGO DE LA ACTIVIDAD EN SU LABEL
	if(rango_hora_inicio[i].value%1==0){
		label_hora_inicio[i].innerText="Hora inicio: " + rango_hora_inicio[i].value + ":00";	
	}else{
		label_hora_inicio[i].innerText="Hora inicio: " + parseInt(rango_hora_inicio[i].value) + ":30";
	}
	if(rango_hora_fin[i].value%1==0){
		label_hora_fin[i].innerText="Hora fin: " + rango_hora_fin[i].value + ":00";	
	}else{
		label_hora_fin[i].innerText="Hora fin: " + parseInt(rango_hora_fin[i].value) + ":30";
	}
}
rango_hora_inicio[0].addEventListener("input",()=>{cambio_rango_horas(0)});
rango_hora_fin[0].addEventListener("input",()=>{cambio_rango_horas(0)})
rango_hora_inicio[1].addEventListener("input",()=>{cambio_rango_horas(1)});
rango_hora_fin[1].addEventListener("input",()=>{cambio_rango_horas(1)})

//ELEMENTOS INTERNOS DEL MODAL CREAR ACTIVIDAD
var input_detalle_crear_actividad = document.getElementById("input-detalle-crear-actividad");
var fecha_crear_actividad = document.getElementById("fecha-crear-actividad");

//ELEMENTOS INTERNOS DEL MODAL EDITAR ACTIVIDAD
var input_detalle_editar_actividad = document.getElementById("input-detalle-editar-actividad");
var fecha_editar_actividad = document.getElementById("fecha-editar-actividad");
var previsualizar_imagen_editar_actividad = document.getElementById("previsualizar-imagen-editar-actividad");
var contenedor_imagen_previsualizar = document.getElementById("contenedor-imagen-previsualizar");

//INICIALIZAR LOS ELEMENTOS VISUALES DE MATERIALIZE CUANDO CARGUE EL CONTENIDO DEL DOM
document.addEventListener('DOMContentLoaded', function() {
	let options = {
		onCloseEnd: ()=>{
			input_detalle_crear_actividad.value=null;
			fecha_crear_actividad.value=null;
			document.getElementById("option-defecto").selected = 'selected';
			document.getElementById("eliminar-imagen-actividad").checked = false;
			contenedor_imagen_previsualizar.innerHTML="";
			//VOLVER A INICIAR LAS ETIQUETAS SELECT
			var elems = document.querySelectorAll('select');
			M.FormSelect.init(elems);
		}
	}
	var elems = document.querySelector('#modal-crear-actividad');
	M.Modal.init(elems, options);   
	
	var elems = document.querySelector('#modal-editar-actividad');
	M.Modal.init(elems, options); 
	
	var elems  = document.querySelectorAll("input[type=range]");
	M.Range.init(elems);
	
	//BOTON FIJO AGREGADO PARA CREAR ACTIVIDADES Y CALENDARIOS
	var elems = document.querySelectorAll('.fixed-action-btn');
	M.FloatingActionButton.init(elems);
	
	//TOOLTIPS BOTONES AGREGADO
	var elems = document.querySelectorAll('.tooltipped');
	var instances = M.Tooltip.init(elems);
	
	//ETIQUETA SELECT
	var elems = document.querySelectorAll('select');
	M.FormSelect.init(elems);

});