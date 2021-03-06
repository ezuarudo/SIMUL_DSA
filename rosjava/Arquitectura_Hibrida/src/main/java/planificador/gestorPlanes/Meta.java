package planificador.gestorPlanes;

/**
 * Clase que representa un objetivo del robot, que debe cumplirse para 
 * considerar el exito de una tarea o del plan completo. Sirve como indice 
 * o identificador para los diferentes RAPs que forman parte del sistema.
 * 
 * @author Jose Luis Diaz Cebrian
 * @version 1.0
 * 
 */
public class Meta {
	
	/**
	 * Identificador unico de la meta, correspondiente al nombre dadao para esa meta.
	 */
	private String nombre;
	
	/**
	 * Predicado que define el objetivo de la meta.
	 */
	private Predicado predicado;
	

	/**
	 * Inicializa con los atributos a null.
	 */
	public Meta(){}
	
	/**
	 * Inicializa la meta, asignando un identificador de nombre y el predicado 
	 * que define el objetivo de la meta.
	 * 
	 * @param nom El nombre de la meta
	 * @param pred El predicado que define la meta
	 */
	public Meta(String nom, Predicado pred){
		this.nombre = nom;
		this.predicado = pred;
	}
	
	
	/**
	 * Devuelve el identificado (nombre) de la meta.
	 * 
	 * @return El nombre de la meta
	 */
	public String getNombre(){
		return this.nombre;
	}
	
	/**
	 * Devuelve el objetivo que se pretende alcanzar como meta.
	 * 
	 * @return El predicado que define la meta
	 */
	public Predicado getPredicado() {
		return predicado;
	}
	
	/**
	 * Asigna un nuevo identificador (nombre) a la meta.
	 * 
	 * @param nombre El nuevo nombre de la meta
	 */
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	
	/**
	 * Asigna un nuevo objetivo para redefinir la meta.
	 * 
	 * @param pred El nuevo predicado de la meta
	 */
	public void setPredicado(Predicado pred){
		this.predicado = pred;
	}
	
	/**
	 * Indica si una meta es igual a otra. Dos metas son iguales si poseen el mismo 
	 * nombre.
	 * 
	 * @param meta La meta con la que se desea comparar
	 * @return "true" si las dos metas son iguales, "false" en caso contrario
	 */
	public boolean esIgual(Meta meta) {
		boolean igual = false;
		if(this.nombre.compareTo(meta.getNombre())==0)
			igual = true;
		return igual;
	}

	
}
