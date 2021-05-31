/**
 * 
 */

let selectorFecha = document.getElementById("selector-fecha");
selectorFecha.addEventListener('change',()=>{dibujar_plantilla(selectorFecha.value)})
//selectorFecha.addEventListener('change',()=>{console.log(selectorFecha.value)})

function dibujar_plantilla(fecha_a_dibujar){
	//OBTENER LOS DATOS DE LAS FECHAS DEL INPUT DE FORMA INDIVIDUAL
    let fecha = new Date(fecha_a_dibujar);
    let dia = fecha.getDate();
    let mes = fecha.getMonth();
    let anio = fecha.getFullYear();
    //console.log("dia: " + dia, "mes: " + mes, "anio: " + anio);

	var fechaPrincipal = new Date(anio,mes,(dia+1));
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
	            div_dia[i].innerHTML += `<div style="height:5em" class="div-hora" name="${i.toString()+j.toString()}" dia="${i}" hora="${j}" numerodia="${parseInt(diasSemana[i])}"><p class="separador-horas"><span>${j+":00"}</span></p></div> <br>`
			}else{
				//SI ES UNA HORA Y MEDIA
            	div_dia[i].innerHTML += `<div style="height:5em" class="div-hora" name="${i.toString()+j.toString()}" dia="${i}" hora="${j}" numerodia="${parseInt(diasSemana[i])}"><p class="separador-horas"><span>${(j-0.5)+":30"}</span></p></div> <br>`				
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
			instancia_modal_crear_actividad.open();
		});
        div_hora[i].addEventListener('mouseover',()=>{
            div_hora[i].style.opacity='1';
        });
        div_hora[i].addEventListener('mouseleave',()=>{
            div_hora[i].style.opacity='0.5';
        });
	}
}

const obtenerInicioSemana = (fecha) => {
    let numeroPrimerDia = fecha.getDate() - fecha.getDay();
    let fechaPrimerDia = fecha;
    fechaPrimerDia.setDate(numeroPrimerDia);
    return fechaPrimerDia;
}

const obtenerFinalSemana = (fecha) => {
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