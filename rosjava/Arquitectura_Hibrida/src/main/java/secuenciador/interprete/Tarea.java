package secuenciador.interprete;

import java.util.LinkedList;
import planificador.gestorPlanes.Meta;
import planificador.gestorPlanes.Accion;

/**
 * Clase que representa el conjunto de acciones a llevar a cabo 
 * para cumplir una submeta dentro del plan establecido. Es el 
 * elemento munimo de secuenciaciun, que permite seleccionar las 
 * habilidades a ejecutar para alcanzar dicha submeta.
 * 
 * @author Josu Luis Duaz Cebriun
 * @version 1.0
 * 
 */
public class Tarea {
	
	/**
	 * Identificador unico de la tarea, correspondiente con el nombre dado para esa tarea.
	 */
	private String nombre;
	
	/**
	 * Meta que se busca alcanzar llevando a cabo la tarea. Tambiun identifica unuvocamente.
	 * a la tarea.
	 */
	public Meta submeta;
	
	/**
	 * Lista de acciones del plan que se engloban bajo la misma tarea.
	 */
	private LinkedList<Accion> acciones;
	
	
	/**
	 * Inicializa por defecto con los atributos a null.
	 */
	public Tarea(){}
	
	/**
	 * Inicializa la tarea, asignando un identificador de nombre y una meta a usta.
	 * 
	 * @param nom El nombre de la tarea
	 * @param subm La meta que se alcanza al llevar a cabo la tarea
	 * 
	 * TODO Constructor auxiliar usado de momento hasta ver quu ocurre con las acciones
	 */
	public Tarea(String nom, Meta subm){
		this.nombre = nom;
		this.submeta = subm;
		this.acciones = new LinkedList<Accion>();
            //    acciones.add(new Accion("MantenerTrayectoria"));
	//	acciones.add(new Accion("EncontrarAlgo"));
	}
	
	/**
	 * Inicializa la tarea, asignando un identificador de nombre y una meta a usta, asu
	 * como una lista de acciones que se ejecutan al llevar a cabo la tarea.
	 * 
	 * @param nom El nombre de la tarea
	 * @param subm La meta que se alcanza al llevar a cabo la tarea
	 * @param acc La lista de acciones que engloba la tarea
	 * 
	 */
	public Tarea(String nom, Meta subm, Accion[] acc){
		this.nombre = nom;
		this.submeta = subm;
		this.acciones = new LinkedList<Accion>();
		for(int i=0; i<acc.length; i++){
			this.acciones.add(acc[i]);
		}
                //acciones.add(new Accion("MantenerTrayectoria"));
		//acciones.add(new Accion("EncontrarAlgo"));
	}
	
	
	/**
	 * Devuelve el identificador unico de la tarea.
	 * 
	 * @return El nombre de la tarea
	 * 
	 */
	public String getNombre(){
		return this.nombre;
	}
	
	/**
	 * Devuelve la meta que se alcanza al llevar a cabo la tarea.
	 * 
	 * @return La meta asociada a la tarea
	 * 
	 */
	public Meta getMeta(){
		return this.submeta;
	}
	
	/**
	 * Devuelve la lista de acciones del plan que engloba la tarea.
	 * 
	 * @return La lista de acciones de la tarea
	 * 
	 */
	public LinkedList<Accion> getAcciones(){
		return this.acciones;
	}
	
	/**
	 * Auade una nueva acciun a la lista de acciones del plan englobadas por la tarea.
	 * 
	 * @param accion La nueva acciun que incluye la tarea
	 * 
	 */
	public void anyadirAccion(Accion accion){
		this.acciones.add(accion);
	}
	
	/**
	 * Elimina una acciun de la lista de acciones del plan englobadas por la tarea.
	 * 
	 * @param accion La acciun a eliminar de la lista
	 * 
	 */
	public void eliminarAccion(Accion accion){
		this.acciones.remove(accion);
	}
	

}
