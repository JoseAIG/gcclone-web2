/**
 * 
 */

var link_cerrar_sesion = document.getElementById("link-cerrar-sesion");

const cerrar_sesion = ()=>{
	    fetch('http://localhost:8080/gcclone/Dashboard', {
	    	method: 'DELETE',
	    	//body: datos_form,
			//mode: "no-cors",
	    	//headers: new Headers({'Content-Type': 'application/json'}),
			})
	    //RESPUESTA CRUDA DEL SERVER
	    .then(response => response.json())
	    //RESPUESTA CON LOS RESULTADOS DEL SERVIDOR
	    .then(data => {
	        console.log('Respuesta del servidor:', data);
			alert(data.resultado);
			window.open("/gcclone","_self");
	    })	    
		//CATCH PARA OBTENER DETALLER POR SI ORURRE UN ERROR
	    .catch((error) => {
	        console.error('Error:', error);
	    });
}

link_cerrar_sesion.onclick= cerrar_sesion;