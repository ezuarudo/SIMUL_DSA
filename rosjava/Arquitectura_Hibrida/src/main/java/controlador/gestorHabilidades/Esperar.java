package controlador.gestorHabilidades;

import infraestructura.Motor;
import infraestructura.Robot;

/**
 * Clase que hereda de Habilidad y define las caracteristicas 
 * propias de esta habilidad especial, que permite un control
 * seguro de las capas inferiores de la arquitectura cuando el 
 * plan esta siendo elaborado.
 * 
 * @author Jose Luis Diaz Cebrian
 * @version 1.0
 * 
 */
public class Esperar extends Habilidad{

	/**
	 * Inicializa la habilidad para su posterior ejecucion.
	 * 
	 * @param robot El robot del sistema que hace uso de la habilidad
	 * 
	 */
	public Esperar(Robot robot, boolean sim){
		super("Esperar", robot, sim);
	}
	
	/**
	 * @see Habilidad#ejecutar()
	 */
	public void ejecutar(){
		int[] ruedas = new int[]{0, 0}; //Izda-Dcha
		Motor m = (Motor) super.getRobot().getActuador("motor");
		m.setRuedas(ruedas[0], ruedas[1]);	
	}
	
	/**
	 * @see Habilidad#comprobarExito()
	 */
	public boolean comprobarExito(){
		return true;
	}
}