/**
 * 
 */

//PROTOTYOE FUNCION "getWeek" PARA OBTENER EL NUMERO DE LA SEMANA EN UN ANIO DE UNA FECHA
//VER: https://weeknumber.com/how-to/javascript
Date.prototype.getWeek = function() {
  var date = new Date(this.getTime());
  date.setHours(0, 0, 0, 0);
  // Thursday in current week decides the year.
  date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
  // January 4 is always in week 1.
  var week1 = new Date(date.getFullYear(), 0, 4);
  // Adjust to Thursday in week 1 and count number of weeks from date to week1.
  return 1 + Math.round(((date.getTime() - week1.getTime()) / 86400000
                        - 3 + (week1.getDay() + 6) % 7) / 7);
}

let selectorFecha = document.getElementById("selector-fecha");
selectorFecha.valueAsDate= new Date();
//dibujar_plantilla(selectorFecha.value);

selectorFecha.addEventListener('change',()=>{dibujar_plantilla(selectorFecha.value)})
//selectorFecha.addEventListener('change',()=>{console.log(selectorFecha.value)})

//FUNCION LLAMADA CUANDO SE SELECCIONA UN CHECKBOX EN "dashboard-calendario.js"
var datos_calendarios = new Array();
function toggle_datos_calendarios(remover,datos){
	
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
		//obtener_actividades_semana();
		dibujar_plantilla(selectorFecha.value);
	}else{
		datos_calendarios.push(datos);
		//IMPRESION DE LOS DATOS CONTENIDOS EN EL ARREGLO
		for(let i=0; i<datos_calendarios.length; i++){
			console.log(datos_calendarios[i]);
			//obtener_actividades_semana(datos_calendario[i]);
		}		
		//obtener_actividades_semana();
		dibujar_plantilla(selectorFecha.value);
	}

	//console.log("esto esta en actividades, datos: " + datos);
}

//FUNCION PARA OBTENER EL NUMERO DE LA SEMANA DE LA FECHA DE UNA ACTIVIDAD
function obtener_numero_semana(fecha_actividad){
	console.log(fecha_actividad);
	let fecha = new Date(fecha_actividad);
	let dia = fecha.getDate();
    let mes = fecha.getMonth();
    let anio = fecha.getFullYear();

	let fecha_corregida = new Date(anio,mes,(dia+1));
	//console.log("La fecha del evento es "+fecha_corregida, "El numero de semana es: "+fecha_corregida.getWeek());
	console.log(fecha_corregida.getWeek());
	
	if(fecha_corregida.getDay()==0){
		return (fecha_corregida.getWeek()+1);
	}else{
		return fecha_corregida.getWeek();		
	}
}

var actividades_en_la_semana = new Array();
function obtener_actividades_semana(){
	//LIMPIAR ARREGLO DE LAS ACTIVIDADES DE LA SEMANA
	actividades_en_la_semana = [];
	//SE RECORREN TODOS LOS CALENDARIOS DE LOS CHECKBOXES MARCADOS
	for(let i=0; i<datos_calendarios.length; i++){
		//SE RECORREN TODAS LAS ACTIVIDADES DE CADA CALENDARIO
		for(let j=0; j<datos_calendarios[i].actividades.length; j++){
			console.log(datos_calendarios[i].actividades[j]);
			//SI EL NUMERO DE LA SEMANA DE UNA ACTIVIDAD ES IGUAL AL NUMERO DE LA SEMANA DE LA FECHA PRINCIPAL, SE GUARDA LA ACTIVIDAD EN EL ARREGLO DE ACTIVIDADES DE LA PRESENTE SEMANA
			//EN EL CASO QUE EL DIA DE LA FECHA SEA DOMINGO, SE LE SUMA 1 AL NUMERO DE SEMANA, DADO QUE LAS SEMANAS CAMBIAN DE NUMERO LOS LUNES Y QUEREMOS QUE EL DOMINGO PERTENEZCA A ESA MISMA SEMANA
			let semana_fecha_principal = fechaPrincipal.getWeek();
			if(fechaPrincipal.getDay()==0){
				semana_fecha_principal++;
			}
			if(obtener_numero_semana(datos_calendarios[i].actividades[j].fecha)==semana_fecha_principal){
				actividades_en_la_semana.push(datos_calendarios[i].actividades[j]);
			}


		}
	}
	console.log(actividades_en_la_semana);
}

//LLAMADA A LA FUNCION DIBUJAR PLANTILLA LA PRIMERA VES QUE SE CORRE LA APP
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
	console.log("semana de la fecha principal: " + fechaPrincipal.getWeek());
	
	obtener_actividades_semana();
	console.log("Dibujar plantilla - Actividades de la semana: ", actividades_en_la_semana);
	
	console.log("Fecha actual: " + fechaPrincipal);
    console.log("La dia de inicio de la semana es: " + obtenerInicioSemana(fechaPrincipal).getDate());
    console.log("La dia de fin de la semana es: " + obtenerFinalSemana(fechaPrincipal).getDate());

    let div_dia = [];
    let diasSemana = obtenerDiasSemana(fechaPrincipal);
    for(let i=0; i<7; i++){
        div_dia[i] = document.getElementById("div"+i);
        div_dia[i].innerHTML = nombre_dia_semana(i) + `<br>` + diasSemana[i] + `<hr>`;
        //AÑADIDO DE DIVS DE LAS HORAS EN LOS DIVS DE LOS DIAS
        for(let j=0; j<24; j=(j+0.5)){
			if(j%1==0){
				//SI ES UNA HORA EN PUNTO
	            div_dia[i].innerHTML += `<div style="height:5em; cursor: pointer" class="div-hora" name="${i.toString()+j.toString()}" dia="${i}" hora="${j}" numerodia="${parseInt(diasSemana[i])}"><p class="separador-horas"><span>${j+":00"}</span></p></div> <br>`
			}else{
				//SI ES UNA HORA Y MEDIA
            	div_dia[i].innerHTML += `<div style="height:5em; cursor: pointer" class="div-hora" name="${i.toString()+j.toString()}" dia="${i}" hora="${j}" numerodia="${parseInt(diasSemana[i])}"><p class="separador-horas"><span>${(j-0.5)+":30"}</span></p></div> <br>`				
			}
        }
        //main_aside.appendChild(div_dia[i]);
    }

	//OBTENER DIVS DE LAS HORAS
    let div_hora = document.getElementsByClassName("div-hora");
	for(let i=0; i<div_hora.length;i++){
        div_hora[i].style.opacity='0.5';
		div_hora[i].addEventListener('click',()=>{
			console.log("click en el div " + i, div_hora[i]);
			//console.log(div_hora[i].parentElement);
			instancia_modal_crear_actividad.open();
		});
        div_hora[i].addEventListener('mouseover',()=>{
            div_hora[i].style.opacity='1';
        });
        div_hora[i].addEventListener('mouseleave',()=>{
            div_hora[i].style.opacity='0.5';
        });
	}
	
	//RECORRER LAS ACTIVIDADES DE LA SEMANA
	for(let i=0; i<actividades_en_la_semana.length; i++){
		let dia_actividad = new Date(actividades_en_la_semana[i].fecha);
		dia_actividad.setDate(dia_actividad.getDate()+1);
		console.log(dia_actividad);
		console.log(dia_actividad.getDay());
		//RECORRER LOS BLOQUES DE HORAS Y COMPARAR CON LOS ATRIBUTOS DE LAS ACIVIDADES PARA PINTARLAS
		let flag = false;
		for(let j=0; j<div_hora.length; j++){
			//console.log("Hora del div:", div_hora[j].getAttribute("hora"), "hora inicio actividad: ", actividades_en_la_semana[i].hora_inicio, "Hora fin actividad: ", actividades_en_la_semana[i].hora_fin);
			if(div_hora[j].getAttribute("dia")==dia_actividad.getDay() && ((actividades_en_la_semana[i].hora_inicio<=div_hora[j].getAttribute("hora")) && (div_hora[j].getAttribute("hora")<=actividades_en_la_semana[i].hora_fin))){
				console.log("Hora del div:", div_hora[j].getAttribute("hora"), "hora inicio actividad: ", actividades_en_la_semana[i].hora_inicio, "Hora fin actividad: ", actividades_en_la_semana[i].hora_fin);
				if(!flag){
					div_hora[j-1].style.backgroundColor="gray";
					div_hora[j-1].innerText+=actividades_en_la_semana[i].informacion;
					flag=true;
				}
				div_hora[j].style.backgroundColor="red";
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

//ELEMENTOS INTERNOS DEL MODAL CREAR ACTIVIDAD
var input_detalle_crear_actividad = document.getElementById("input-detalle-crear-actividad");

//ACTIVAR EL MODAL PARA ACTIVIDADES (MATERIALIZE)
var instancia_modal_crear_actividad;
var instancia_rango_actividad;
document.addEventListener('DOMContentLoaded', function() {
   let options = {
   		onCloseEnd: ()=>{
			input_detalle_crear_actividad.value=null;
   		}
	}
   var modal_crear_actividad = document.querySelector('#modal-crear-actividad');
   instancia_modal_crear_actividad = M.Modal.init(modal_crear_actividad, options);   

	//INPUT TYPE RANGE
	var rango_actividad = document.querySelector('#rango-actividad');
	instancia_rango_actividad = M.Range.init(rango_actividad);

});