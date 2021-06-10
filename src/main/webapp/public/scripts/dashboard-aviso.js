/**
 * 
 */

console.log("dashboard-aviso")
let div_hora = document.getElementsByClassName("div-hora");

function mostrar_avisos(actividades_semana){
	console.log("mostrar avisos: ", actividades_semana);
	
	let fecha_actual = new Date();
	console.log("dia de hoy: "+fecha_actual.getDate());
	//DETERMINAR EL BLOQUE DE HORAS ACTUALES
	let bloque_actual = fecha_actual.getHours();
	if(fecha_actual.getMinutes()>=30){
		bloque_actual+=".5";
	}
	console.log("bloque actual: " + bloque_actual);
	
	//SE RECORREN TODAS LAS ACTIVIDADES DE LA SEMANA
	for(let i=0; i<actividades_semana.length;i++){
		//SE CREA UN NUEVO OBJETO DATE CON LA FECHA DE LA ACTIVIDAD
		let fecha_evento = new Date(actividades_semana[i].fecha);
		//SI EL NUMERO DE DIA DEL EVENTO DE LA SEMANA ES EL MISMO NUMERO DE DIA DEL DIA ACTUAL
		if(fecha_evento.getDate()+1 == fecha_actual.getDate()){			
			//SI LA HORA DE LA FECHA ACTUAL ES MAYOR A LA HORA FINAL DE UNA ACTIVIDAD, LA MISMA SE DESCRIBE COMO CONCLUIDA 
			if(parseFloat(actividades_semana[i].hora_fin)==parseFloat(bloque_actual)){
				M.toast({html: '<span>Esta por acabar la actividad: '+actividades_semana[i].informacion+'</span>', classes: 'pulse'})
			}
			else if(parseFloat(actividades_semana[i].hora_inicio)<=parseFloat(bloque_actual) && parseFloat(bloque_actual)<=parseFloat(actividades_semana[i].hora_fin)){
				M.toast({html: '<span>Esta en curso la actividad: '+actividades_semana[i].informacion+'</span>'})
			}
			else if(parseFloat(actividades_semana[i].hora_fin)<parseFloat(bloque_actual)){
				M.toast({html: '<span>Ha acabado la actividad: '+actividades_semana[i].informacion+'</span>'})
			}
		}
	}
}