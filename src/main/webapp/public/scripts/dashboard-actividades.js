/**
 * 
 */

//PROTOTYOE numeroSemana PARA OBTENER EL NUMERO DE LA SEMANA EN UN ANIO DE UNA FECHA
Date.prototype.numeroSemana = function() {
	//SE OBTIENE EL PRIMER DIA DEL ANIO ACTUAL
    var primer_dia_anio = new Date(this.getFullYear(),0,1);
    return Math.ceil((((this - primer_dia_anio) / 86400000) + primer_dia_anio.getDay()+1)/7);
};

//OBTENER EL SELECTOR DE LA FECHA Y ASIGNARLE POR DEFECTO LA FECHA ACTUAL
let selectorFecha = document.getElementById("selector-fecha");
selectorFecha.valueAsDate= new Date();
//dibujar_plantilla(selectorFecha.value);

//EJECUTAR LA FUNCION "dibujar_plantilla" CADA VEZ QUE SE CAMBIA UN VALOR EN EL SELECTOR DE LA FECHA
selectorFecha.addEventListener('change',()=>{dibujar_plantilla(selectorFecha.value)})
//selectorFecha.addEventListener('change',()=>{console.log(selectorFecha.value)})

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
		//IMPRESION DE LOS DATOS CONTENIDOS EN EL ARREGLO
		for(let i=0; i<datos_calendarios.length; i++){
			console.log(datos_calendarios[i]);
		}
		//SE DIBUJA LA PLANTILLA SEMANAL CON LA FECHA QUE POSEE EL SELECTOR
		dibujar_plantilla(selectorFecha.value);
	}else{
		//SI REMOVER ES FALSE (AGREGAR DATOS). SE AGREGAN LOS DATOS DEL CALENDARIO EN EL ARREGLO
		datos_calendarios.push(datos);
		//IMPRESION DE LOS DATOS CONTENIDOS EN EL ARREGLO
		for(let i=0; i<datos_calendarios.length; i++){
			console.log(datos_calendarios[i]);
		}		
		//SE DIBUJA LA PLANTILLA SEMANAL CON LA FECHA QUE POSEE EL SELECTOR
		dibujar_plantilla(selectorFecha.value);
	}
}

//FUNCION PARA INCREMENTAR O DECREMENTAR EN UNA SEMANA LA FECHA DEL INPUT TYPE DATE CON BOTONES
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


//FUNCION PARA OBTENER EL NUMERO DE LA SEMANA DE LA FECHA DE UNA ACTIVIDAD
function obtener_numero_semana(fecha_actividad){
	console.log(fecha_actividad);
	let fecha = new Date(fecha_actividad);
	let dia = fecha.getDate();
    let mes = fecha.getMonth();
    let anio = fecha.getFullYear();

	let fecha_corregida = new Date(anio,mes,(dia+1));
	//console.log("La fecha del evento es "+fecha_corregida, "El numero de semana es: "+fecha_corregida.getWeek());
	console.log("numeroSemana:" + fecha_corregida.numeroSemana());
	
	//if(fecha_corregida.getDay()==0){
		//return (fecha_corregida.getWeek()+1);
	//}else{
		//return fecha_corregida.getWeek();		
	return fecha_corregida.numeroSemana();
	//}
}

var actividades_en_la_semana = new Array();
var colores_actividades = new Array();
var privilegios_actividades = new Array();
//FUNCION PARA CALCULAR LAS ACTIVIDADES DE UNA SEMANA
function obtener_actividades_semana(){
	//LIMPIAR ARREGLO DE LAS ACTIVIDADES DE LA SEMANA Y COLORES DE LAS ACTIVIDADES
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
				if(datos_calendarios[i].invitados==null){
					console.log("no brindar privilegios de edicion de actividades");
					privilegios_actividades.push(false);
				}else{
					privilegios_actividades.push(true);
				}
			}
		}
	}
	console.log(actividades_en_la_semana);
	mostrar_avisos(actividades_en_la_semana);
}

//LLAMADA A LA FUNCION DIBUJAR PLANTILLA LA PRIMERA VEZ QUE SE CORRE LA APP
dibujar_plantilla(selectorFecha.value);

var fechaPrincipal;
function dibujar_plantilla(fecha_a_dibujar){
	console.log("dibujar plantilla");
	//OBTENER LOS DATOS DE LAS FECHAS DEL INPUT DE FORMA INDIVIDUAL
    let fecha = new Date(fecha_a_dibujar);
    let dia = fecha.getDate();
    let mes = fecha.getMonth();
    let anio = fecha.getFullYear();

	//console.log(fecha.getWeek());

    //console.log("dia: " + dia, "mes: " + mes, "anio: " + anio);

	fechaPrincipal = new Date(anio,mes,(dia+1));
	console.log("numeroSemana:" + fechaPrincipal.numeroSemana());
	
	obtener_actividades_semana();
	console.log("Dibujar plantilla - Actividades de la semana: ", actividades_en_la_semana);
	
	//console.log("Fecha actual: " + fechaPrincipal);
    //console.log("La dia de inicio de la semana es: " + obtenerInicioSemana(fechaPrincipal).getDate());
    //console.log("La dia de fin de la semana es: " + obtenerFinalSemana(fechaPrincipal).getDate());

    let div_dia = [];
    let diasSemana = obtenerDiasSemana(fechaPrincipal);
    for(let i=0; i<7; i++){
        div_dia[i] = document.getElementById("div"+i);
        div_dia[i].innerHTML = nombre_dia_semana(i) + `<br>` + diasSemana[i] + `<hr>`;
        //AÑADIDO DE DIVS DE LAS HORAS EN LOS DIVS DE LOS DIAS
        for(let j=0; j<24; j=(j+0.5)){
			if(j%1==0){
				//SI ES UNA HORA EN PUNTO
	            //div_dia[i].innerHTML += `<div style="cursor: pointer" class="div-hora hoverable" dia=${i} hora=${j} numerodia="${parseInt(diasSemana[i])}"><p class="separador-horas"><span class="span-bloque-hora">${j+":00"}</span></p></div> <br>`
	            div_dia[i].innerHTML += `<div  class="div-hora hoverable" dia=${i} hora=${j} numerodia="${parseInt(diasSemana[i])}"><p class="numero-bloque-hora">${j+":00"}</p></div> <br>`
			}else{
				//SI ES UNA HORA Y MEDIA
            	//div_dia[i].innerHTML += `<div style="cursor: pointer" class="div-hora hoverable" dia=${i} hora=${j} numerodia="${parseInt(diasSemana[i])}"><p class="separador-horas"><span class="span-bloque-hora">${(j-0.5)+":30"}</span></p></div> <br>`				
            	div_dia[i].innerHTML += `<div  class="div-hora hoverable" dia=${i} hora=${j} numerodia="${parseInt(diasSemana[i])}"><p class="numero-bloque-hora">${(j-0.5)+":30"}</p></div> <br>`				
			}
        }
        //main_aside.appendChild(div_dia[i]);
    }
	
	//RECORRER LAS ACTIVIDADES DE LA SEMANA
/*	for(let i=0; i<actividades_en_la_semana.length; i++){
		console.log("recorrer actividad: " + actividades_en_la_semana[i].informacion);
		let dia_actividad = new Date(actividades_en_la_semana[i].fecha);
		dia_actividad.setDate(dia_actividad.getDate()+1);
		console.log("longitud div hora: " + div_hora.length);
		console.log("Dia de la actividad: "+ actividades_en_la_semana[i].informacion + dia_actividad);
		console.log("Numero de dia de la actividad: " + dia_actividad.getDay());
		//RECORRER LOS BLOQUES DE HORAS Y COMPARAR CON LOS ATRIBUTOS DE LAS ACIVIDADES PARA PINTARLAS
		let flag = false;
		for(let j=0; j<div_hora.length; j++){
			//console.log("Hora del div:", div_hora[j].getAttribute("hora"), "hora inicio actividad: ", actividades_en_la_semana[i].hora_inicio, "Hora fin actividad: ", actividades_en_la_semana[i].hora_fin);
			//if(div_hora[j].getAttribute("dia")==dia_actividad.getDay() && ((actividades_en_la_semana[i].hora_inicio<=div_hora[j].getAttribute("hora")) && (div_hora[j].getAttribute("hora")<=actividades_en_la_semana[i].hora_fin))){
			if(div_hora[j].getAttribute("dia")==dia_actividad.getDay()){
				if((actividades_en_la_semana[i].hora_inicio<=div_hora[j].getAttribute("hora")) && (div_hora[j].getAttribute("hora")<=actividades_en_la_semana[i].hora_fin)){
					//console.log("Hora del div:", div_hora[j].getAttribute("hora"), "hora inicio actividad: ", actividades_en_la_semana[i].hora_inicio, "Hora fin actividad: ", actividades_en_la_semana[i].hora_fin);
					if(!flag){
						div_hora[j-1].style.backgroundColor=colores_actividades[i];
						div_hora[j-1].style.opacity='1';
						div_hora[j-1].innerText+=actividades_en_la_semana[i].informacion;
						flag=true;
					}
					div_hora[j].style.backgroundColor=colores_actividades[i];				
				}
				else{
				if(div_hora[j].getAttribute("hora")==10 && div_hora[j].getAttribute("dia")==3 && dia_actividad.getDay()==3){
					console.log("aqui 1");
					console.log(actividades_en_la_semana[i]);
				}
			}
			}else{
				if(div_hora[j].getAttribute("hora")==10 && div_hora[j].getAttribute("dia")==3 && dia_actividad.getDay()==3){
					console.log("aqui 2");
				}
			}
		}
	}*/
	
	//RECORRER LOS DIVS Y ACTIVIDADES DE LA SEMANA
/*	for(let i=0; i<actividades_en_la_semana.length; i++){
	
		let dia_actividad = new Date(actividades_en_la_semana[i].fecha);
		dia_actividad.setDate(dia_actividad.getDate()+1);
		
		for(let j=0; j<div_hora.length; j++){
			
			//DATOS DE LA ACTIVIDAD
			let dia_semana_actividad = dia_actividad.getDay();
			let hora_inicio_actividad = actividades_en_la_semana[i].hora_inicio;
			let hora_fin_actividad = actividades_en_la_semana[i].hora_fin;
			//console.log("DATOS DE LA ACTIVIDAD: ", "Dia semana act: " + dia_semana_actividad, "Hora inicio act: " + hora_inicio_actividad, "Hora fin act: " + hora_fin_actividad);
			
			//DATOS DEL DIV
			let hora_del_div = div_hora[j].getAttribute("hora");
			let dia_del_div = div_hora[j].getAttribute("dia");
			//console.log("DATOS DEL DIV: ", "Dia del div: " + dia_del_div, "Hora del div: " + hora_del_div)
			
			if(dia_semana_actividad == dia_del_div){
				//console.log("Evento este dia");
				//console.log(hora_del_div);
				//console.log("Hora del div: " + hora_del_div, "Hora inicio act: " + hora_inicio_actividad, "Hora fin act: " + hora_fin_actividad);
				if(hora_inicio_actividad<=hora_del_div && hora_del_div<=hora_fin_actividad ){
					console.log("pintar");
					div_hora[j].style.backgroundColor="red";
				}
			}
			
		}
	}*/
	
	//PINTADO MANUAL DIV HORA 10 (FUNCIONO)
/*	for(let a=0; a<actividades_en_la_semana.length; a++){
	
		let dia_actividad = new Date(actividades_en_la_semana[a].fecha);
		dia_actividad.setDate(dia_actividad.getDate()+1);
	
		for(let i=0; i<div_hora.length; i++){
			if((parseInt(actividades_en_la_semana[a].hora_inicio))<=(parseInt(div_hora[i].getAttribute("hora"))) && (parseInt(div_hora[i].getAttribute("hora")))<=(parseInt(actividades_en_la_semana[a].hora_fin)) && div_hora[i].getAttribute("dia") == dia_actividad.getDay()){
				console.log("el 10");
				div_hora[i].style.backgroundColor="red";
			}
		}	
	}*/
	
	//RECORRIDO FINAL DE LAS ACTIVIDADES POR LOS DIVS DE LAS HORAS
	for(let i=0; i<actividades_en_la_semana.length; i++){
		
		let dia_actividad = new Date(actividades_en_la_semana[i].fecha);
		dia_actividad.setDate(dia_actividad.getDate()+1);
		
		//RECORRER LOS BLOQUES DE HORAS Y COMPARAR CON LOS ATRIBUTOS DE LAS ACIVIDADES PARA PINTARLAS
		let flag = false;
		for(let j=0; j<div_hora.length; j++){
			if((parseInt(actividades_en_la_semana[i].hora_inicio))<=(parseInt(div_hora[j].getAttribute("hora"))) && (parseInt(div_hora[j].getAttribute("hora")))<=(parseInt(actividades_en_la_semana[i].hora_fin-0.5)) && div_hora[j].getAttribute("dia") == dia_actividad.getDay()){
				//console.log("Hora inicio actividad: " + parseInt(actividades_en_la_semana[i].hora_inicio) , "Hora del div" + parseInt(div_hora[j].getAttribute("hora")), "Hora fin actividad: " + parseInt(actividades_en_la_semana[i].hora_fin));
				//console.log("Hora del div:", div_hora[j].getAttribute("hora"), "hora inicio actividad: ", actividades_en_la_semana[i].hora_inicio, "Hora fin actividad: ", actividades_en_la_semana[i].hora_fin);
				if(!flag){
					//ESTE FLAG ES PARA REALIZAR EL ANIADIDO ADICIONAL DE DISENIO AL PRIMER DIV DE LA ACTIVIDAD
					
					//SI LA ACTIVIDAD COMIENZA EN UNA MEDIA HORA AUMENTAR EN UNA UNIDAD EL CONTEO INICIAL PARA EL DIBUJADO DE LA ACTIVIDAD EN PLANTILLA
					if(actividades_en_la_semana[i].hora_inicio%1!=0){
						j++;
					}
					
					//INCLUIR EL DISENIO DEL DIV PRINCIPAL DE LA ACTIVIDAD
					//div_hora[j].style.backgroundColor=colores_actividades[i];
					div_hora[j].style.opacity='1';
/*					div_hora[j].style.borderTopLeftRadius='1em';
					div_hora[j].style.borderTopRightRadius='1em';*/
					div_hora[j].innerHTML+=actividades_en_la_semana[i].informacion;
					//SI LA ACTIVIDAD TIENE UNA IMAGEN, MOSTRARLA
					if(actividades_en_la_semana[i].ruta_imagen){
			        	div_hora[j+1].innerHTML+=`<span class="span-imagen-actividad"><img alt="img" src="${actividades_en_la_semana[i].ruta_imagen}" class="imagen-actividad"></span>`;					
					}
			        
			        //CONFIGURAR EL EVENTO PARA EDITAR ACTIVIDAD EN EL PRIMER DIV DE LA ACTIVIDAD SI EL USUARIO POSEE PRIVILEGIOS DE PROPIETARIO EN EL CALENDARIO, POR ENDE EN LA ACTIVIDAD
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
							
							id_actividad_editar = div_hora[j].getAttribute("id-actividad");
							//COLOCAR LOS DATOS DE LA ACTIVIDAD EN LOS CAMPOS DEL MODAL EDITAR ACTIVIDAD
							input_detalle_editar_actividad.value = actividades_en_la_semana[i].informacion;
							fecha_editar_actividad.value = actividades_en_la_semana[i].fecha;
							//ASIGNAR LOS VALORES EN LOS RANGO DE HORAS PARA EDICION (0 CREAR - 1 EDITAR)
							rango_hora_inicio[1].value= actividades_en_la_semana[i].hora_inicio;
							rango_hora_fin[1].value = actividades_en_la_semana[i].hora_fin;
							cambio_rango_horas(1);
							
							//INCLUIR BOTON PARA EDITAR ACTIVIDAD
							
							//ABRIR MODAL EDITAR ACTIVIDAD
							//instancia_modal_editar_actividad.open();
							
						});
				    }    
					flag=true;
					
					
				}
				if(actividades_en_la_semana[i].hora_fin%1==0 && div_hora[j].getAttribute("hora")!=23.5){
					div_hora[j+1].style.backgroundColor=colores_actividades[i];
/*					div_hora[j+1].addEventListener('click',()=>{
						instancia_modal_editar_actividad.open();
					});*/
				}
				
				//AGREGAR COMO ATRIBUTO EL ID DE LA ACTIVIDAD AL DIV Y PINTARLO DEL COLOR DEL CALENDARIO PERTINENTE
				div_hora[j].setAttribute("id-actividad", actividades_en_la_semana[i].id_actividad);
				div_hora[j].style.backgroundColor=colores_actividades[i];
				
			}
			
		}
	}	
}

function obtenerInicioSemana (fecha) {
    let numeroPrimerDia = fecha.getDate() - fecha.getDay();
    let fechaPrimerDia = fecha;
    fechaPrimerDia.setDate(numeroPrimerDia);
    return fechaPrimerDia;
}

function obtenerFinalSemana (fecha) {
    //EL ULTIMO DIA DE LA SEMANA SERA EL NUMERO DE DIA ACTUAL + EL NUMERO DE DIAS DE LA SEMANA EMPEZANDO EN 0 - EL NUMERO DE DIA DE LA SEMANA ACTUAL
    let numeroUltimoDia = fecha.getDate() + 6 - fecha.getDay();
    let fechaUltimoDia = fecha;
    fechaUltimoDia.setDate(numeroUltimoDia);
    return fechaUltimoDia;
}

function obtenerDiasSemana(fecha) {
    let primerDia = obtenerInicioSemana(fecha).getDate();
    let ultimoDia = obtenerFinalSemana(fecha).getDate();

    let numeroDiasSemana = [];

    if(primerDia<ultimoDia){
        for(let i=0; i<7; i++){
            numeroDiasSemana[i] = fecha.getDate() + i - fecha.getDay();
        }
    }else{
        let fechaPrimerDia = obtenerInicioSemana(fecha);
        fechaPrimerDia.setDate(31);

        let diaFinMesAnterior = 31-fechaPrimerDia.getDate()
        if(diaFinMesAnterior==0){
            diaFinMesAnterior=31;
        }
        for(let i=0;i<7;i++){
            //SI LA SUMA DE LA SIGUIENTE ITERACION NO SUPERA EL DIA FIN DEL MES ANTERIOR, EL DIA DE LA SEMANA ES LA SUMA
            if(!((primerDia+i)>diaFinMesAnterior)){
                numeroDiasSemana[i] = primerDia + i;
            }
            //PERO SI LA SUMA SUPERA EL DIA FIN DEL MES ANTERIOR, EMPIEZA EL CONTEO DEL PROXIMO MES
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

function nombre_dia_semana(numero_dia){
    let dia;
    switch(numero_dia){
        case 0:
            dia = "Domingo";
            break;
        case 1:
            dia = "Lunes";
            break;
        case 2: 
            dia = "Martes";
            break;
        case 3:
            dia = "Miercoles";
            break;
        case 4: 
            dia = "Jueves";
            break;
        case 5:
            dia = "Viernes";
            break;
        case 6:
            dia = "Sabado";
    }
    return dia;
}

//INCLUIR LA LISTA DE CALENDARIO EN LAS OPCIONES DE LAS ETIQUETAS SELECT PARA CREAR CALENDARIOS
var select_calendario_crear_actividad = document.getElementById('select-calendario-crear-actividad');
function agregar_calendarios_opciones_select(calendarios){
	//console.log("agregar opciones para select... Calendarios: ", calendarios, select_calendario_crear_actividad);
	for(let i=0; i<calendarios.length; i++){
		let option = document.createElement("option");
		option.value = calendarios[i].id_calendario;
		option.text = calendarios[i].nombre_calendario;
		select_calendario_crear_actividad.add(option);
	}
	
    //VOLVER A INSTANCIAR LAS ETIQUETAS SELECT
    var elems = document.querySelectorAll('select');
    //var instances = M.FormSelect.init(elems);
    M.FormSelect.init(elems);
}

//CREAR ACTIVIDAD
var link_guardar_nueva_actividad = document.getElementById("link-guardar-nueva-actividad");
var form_crear_actividad = document.getElementById("form-crear-actividad");
const crear_actividad = () => {
	console.log("crear actividad");
	var form_nueva_actividad = new FormData(form_crear_actividad);
	//AGREGAR LAS HORAS DE INICIO Y FIN AL FORM DATA
	//COMPROBAR CAMPOS DE LAS ACTIVIDADES
	console.log(form_nueva_actividad.get('detalle-actividad'), form_nueva_actividad.get('fecha-crear-actividad'), form_nueva_actividad.get('imagen-crear-actividad'), form_nueva_actividad.get('select-calendario-crear-actividad'), form_nueva_actividad.get('hora-inicio'), form_nueva_actividad.get('hora-fin'));
	if(!form_nueva_actividad.get('detalle-actividad') || !form_nueva_actividad.get('fecha-crear-actividad') || form_nueva_actividad.get('select-calendario-crear-actividad')==null){
		alert("llene todos los campos");
	}else{
		console.log("hacer fetch aqui");
	    fetch('Actividad', {
	    	method: 'POST',
	    	body: form_nueva_actividad,
			mode: "no-cors",
	    	headers: new Headers({'Content-Type': 'application/json'}),
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
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
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
	console.log("modificar actividad, fetch aqui");
	var datos_form_editar_actividad = new FormData(form_editar_actividad);
	datos_form_editar_actividad.append("id-actividad", id_actividad_editar);

    fetch('Actividad', {
    	method: 'PUT',
    	body: datos_form_editar_actividad
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
	//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
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

//ESTA FUNCION RECIBE POR PARAMETROS UN INDICE i, SI ES 0 ES PARA AJUSTAR LA INFORMACION PARA CREAR ACTIVIDAD Y SI ES 1 ES PARA EDITAR
function cambio_rango_horas (i) {	
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

//ACTIVAR EL MODAL PARA ACTIVIDADES (MATERIALIZE)
//var instancia_rango_actividad;
var instancia_modal_editar_actividad;
document.addEventListener('DOMContentLoaded', function() {
   let options = {
   		onCloseEnd: ()=>{
			input_detalle_crear_actividad.value=null;
			fecha_crear_actividad.value=null;
						
			document.getElementById("option-defecto").selected = 'selected';
		    //VOLVER A INSTANCIAR LAS ETIQUETAS SELECT
			var elems = document.querySelectorAll('select');
			var instances = M.FormSelect.init(elems);
			
			contenedor_imagen_previsualizar.innerHTML="";
   		}
	}
    var elems = document.querySelector('#modal-crear-actividad');
    var instances = M.Modal.init(elems, options);   

    var elems = document.querySelector('#modal-editar-actividad');
    instancia_modal_editar_actividad = M.Modal.init(elems, options); 

	//INPUT TYPE RANGE
/*	var rango_actividad = document.querySelector('#rango-actividad');
	instancia_rango_actividad = M.Range.init(rango_actividad);*/
	
    var elems  = document.querySelectorAll("input[type=range]");
    M.Range.init(elems);
	
	//BOTON FIJO AGREGADO
   	var elems = document.querySelectorAll('.fixed-action-btn');
   	var instances = M.FloatingActionButton.init(elems);
   	
   	//TOOLTIPS BOTONES AGREGADO
    var elems = document.querySelectorAll('.tooltipped');
    var instances = M.Tooltip.init(elems);
    
    //ETIQUETA SELECT
    var elems = document.querySelectorAll('select');
    var instances = M.FormSelect.init(elems);

});