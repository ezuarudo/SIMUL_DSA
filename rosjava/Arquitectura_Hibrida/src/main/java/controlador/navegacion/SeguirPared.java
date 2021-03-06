package controlador.navegacion;

import controlador.gestorHabilidades.Habilidad;
import infraestructura.*;

/**
 * Clase que hereda de Habilidad y define las caracter�sticas 
 * propias de esta habilidad e implementa su bucle de ejecuci�n 
 * y la condici�n del evento de finalizaci�n.
 * 
 * @author Jos� Luis D�az Cebri�n
 * @version 1.0
 * 
 */
public class SeguirPared extends Habilidad{

	/**
	 * Indica la direcci�n por la que se va a seguir la pared. Est� a "true" si la pared se
	 * va a dejar a la izquierda (el robot sigue paredes en sentido horario), y a "false" en 
	 * caso contrario.
	 */
	private boolean izquierda;
	
	/**
	 * Umbral m�nimo de distancia entre la pared y el robot. Junto con el UMBRAL_MAX, se 
	 * crea un "pasillo" virtual que permite al robot no separarse de la pared ni chocarse
	 * con ella.
	 */
	private final double UMBRAL_MIN;
	
	/**
	 * Umbral m�ximo de distancia entre el obst�culo y el robot. Junto con el UMBRAL_MIN, se 
	 * crea un "pasillo" virtual que permite al robot no separarse de la pared ni chocarse
	 * con ella.
	 */
	private final double UMBRAL_MAX;
	
	/**
	 * Umbral m�ximo de acercamiento del robot a un obst�culo. Si el robot se encuentra a menor distancia, 
	 * es necesario evitar el obst�culo.
	 */
	private final double UMBRAL_CHOQUE;
	
	
	/**
	 * Inicializa los atributos de la habilidad seg�n resultados obtenidos en experimentaciones
	 * previas de rob�tica.
	 * 
	 * @param izda A "true" si se desea que el robot siga paredes por la izquierda (sentido horario)
	 * @param robot El robot del sistema que hace uso de la habilidad
	 * @param sim A "true" si se utiliza la arquitectura sobre simulador
	 * 
	 */
	public SeguirPared(boolean izda, Robot robot, boolean sim){
		super("SeguirPared", robot, sim);
		this.izquierda = izda;
		if(sim){
			this.UMBRAL_MIN = 100.0;
			this.UMBRAL_MAX = 500.0;
			this.UMBRAL_CHOQUE = 100.0;
		} else{
			this.UMBRAL_MIN = 300.0;
			this.UMBRAL_MAX = 400.0;
			this.UMBRAL_CHOQUE = 300.0;
		}
	}
	
	/**
	 * Devuelve el sentido que toma el robot para seguir paredes.
	 * 
	 * @return "true" si sigue paredes en sentido horario, "false" en caso contrario
	 */
	public boolean getIzda(){
		return this.izquierda;
	}
	
	/**
	 * Devuelve el valor m�nimo de la distancia dejada por el robot respecto a la pared.
	 * 
	 * @return La distancia m�nima con la pared
	 * 
	 */
	public double getUmbralMin(){
		return this.UMBRAL_MIN;
	}
	
	/**
	 * Devuelve el valor m�ximo de la distancia dejada por el robot respecto a la pared.
	 * 
	 * @return La distancia m�xima con la pared
	 * 
	 */
	public double getUmbralMax(){
		return this.UMBRAL_MAX;
	}
	
	/**
	 * Devuelve el valor m�ximo de acercamiento del robot al obst�culo.
	 * 
	 * @return La distancia m�nima con el obst�culo
	 * 
	 */
	public double getUmbralChoque(){
		return this.UMBRAL_CHOQUE;
	}
	
	/**
	 * Asigna un nuevo sentido a la hora de seguir paredes.
	 * 
	 * @param izda A "true" si se va a seguir paredes en sentido horario, a "false" en caso contrario
	 * 
	 */
	public void setIzda(boolean izda){
		this.izquierda = izda;
	}
	
	/**
	 * @see Habilidad#ejecutar()
	 */
	public void ejecutar(){
			
		int[] ruedas = new int[2]; //Izda-Dcha
		
		if(super.getSim())
			ruedas = ejecSimulador();
		else
			ruedas = ejecRobotReal();
		
		Motor m = (Motor) super.getRobot().getActuador("motor");
		m.setRuedas(ruedas[0], ruedas[1]);
		
	}
	
	/**
	 * Da un valor a las ruedas del robot teniendo en cuenta los umbrales
	 * y peque�as diferencias en el algoritmo para el simulador.
	 * 
	 * @return Las velocidades a asignar a las ruedas del robot
	 */
	public int[] ejecSimulador(){
		
		int[] ruedas = new int[2];
		Sonar s = (Sonar) super.getRobot().getSensor("sonar");
		double ds_iz = s.getValorSonar(2);
		double guia = s.getValorSonar(3);
		double ds_fr = Math.max(s.getValorSonar(0), Math.max(s.getValorSonar(1), s.getValorSonar(7)));
   
		if (ds_iz>UMBRAL_MIN && ds_iz<UMBRAL_MAX && ds_fr<UMBRAL_CHOQUE){ //Si voy por la pared y no hay nada delante, avanzo
			ruedas[0] = 30;
			ruedas[1] = 30;
		} else if (ds_fr>UMBRAL_CHOQUE){ //Si tengo algo delante, rodeo el objeto
			ds_fr = Math.max(Math.max(s.getValorSonar(0), s.getValorSonar(1)), Math.max(s.getValorSonar(6), s.getValorSonar(7)));
			if (ds_fr > UMBRAL_CHOQUE) { //Si tengo el objeto delante, giro a la derecha
				ruedas[0] = 50;
				ruedas[1] = -10;
			} else if((s.getValorSonar(0)+s.getValorSonar(7))/2 < UMBRAL_CHOQUE){ //Si no, si estoy cerca de la pared, avanzo
				ruedas[0] = 30;
				ruedas[1] = 30;
			} else{ //Si no, giro a la izquierda
				ruedas[0] = -10;
				ruedas[1] = 50;
			}
		} else if (ds_iz<UMBRAL_MIN && guia>UMBRAL_MIN){ //Si voy por la pared y sigo la guia, avanzo
			ruedas[0] = 30;
			ruedas[1] = 30;
		} else if (ds_iz<UMBRAL_MIN && guia<UMBRAL_MIN){ //Si voy por la pared y pierdo la guia, giro a la izquierda
			ruedas[0] = 10;
			ruedas[1] = 50;
		} else if (ds_iz>UMBRAL_MIN){ //Si me pego a la pared, giro a la derecha
			ruedas[0] = 10;
			ruedas[1] = -10;
		} else if (ds_iz<UMBRAL_MAX-20){ //Si me alejo de la pared, giro a la izquierda
			ruedas[0] = -10;
			ruedas[1] = 10;
		}
		
		return ruedas;
	}
	
	/**
	 * Da un valor a las ruedas del robot teniendo en cuenta los umbrales
	 * y peque�as diferencias en el algoritmo para el robot real sobre ARIA.
	 * 
	 * @return Las velocidades a asignar a las ruedas del robot
	 */
	public int[] ejecRobotReal(){
		
		int[] ruedas = new int[2];
		Sonar s = (Sonar) super.getRobot().getSensor("sonar");
		double ds_iz = s.getValorSonar(1);
		double guia = s.getValorSonar(0);
		double ds_fr = Math.min(s.getValorSonar(3), Math.min(s.getValorSonar(2), s.getValorSonar(4)));
   
		if (ds_fr<UMBRAL_CHOQUE){ //Si tengo algo delante, rodeo el objeto
			if (ds_fr < UMBRAL_CHOQUE) { //Si tengo el objeto delante, giro a la derecha
				ruedas[0] = 100;
				ruedas[1] = -20;
			} else if((s.getValorSonar(3)+s.getValorSonar(4))/2 < UMBRAL_CHOQUE){ //Si no, si estoy cerca de la pared, avanzo
				ruedas[0] = 50;
				ruedas[1] = 50;
			} else{ //Si no, giro a la izquierda
				ruedas[0] = -20;
				ruedas[1] = 100;
			}
		} else if (ds_iz>UMBRAL_MIN && ds_iz<UMBRAL_MAX && ds_fr>UMBRAL_CHOQUE){ //Si voy por la pared y no hay nada delante, avanzo
			ruedas[0] = 60;
			ruedas[1] = 60;
		} else if (ds_iz>UMBRAL_MIN && guia<UMBRAL_MIN){ //Si voy por la pared y sigo la guia, avanzo
			ruedas[0] = 60;
			ruedas[1] = 60;
		} else if (ds_iz>UMBRAL_MIN && guia>UMBRAL_MIN){ //Si voy por la pared y pierdo la guia, giro a la izquierda
			ruedas[0] = 20;
			ruedas[1] = 100;
		} else if (ds_iz<UMBRAL_MIN){ //Si me pego a la pared, giro a la derecha
			ruedas[0] = 40;
			ruedas[1] = -20;
		} else if (ds_iz>UMBRAL_MAX+20){ //Si me alejo de la pared, giro a la izquierda
			ruedas[0] = -20;
			ruedas[1] = 40;
		}
		
		return ruedas;
	}
	
	/**
	 * @see Habilidad#comprobarExito()
	 */
	public boolean comprobarExito(){
		boolean exito = false;
		
		if(super.getSim())
			exito = exitoSimulador();
		else
			exito = exitoRobotReal();
		
		return exito;
	}
	
	/**
	 * Comprueba la condici�n de �xito de la habilidad teniendo en cuenta los 
	 * umbrales y peque�as diferencias en el algoritmo para el simulador.
	 * 
	 * @return "true" si se ha ejecutado con �xito la habilidad, "false" en caso contrario
	 */
	public boolean exitoSimulador(){
		boolean exito = false;
		
		/* TODO Incluir como funci�n de �xito alcanzar un punto del mapa */
		
		return exito;
	}
	
	/**
	 * Comprueba la condici�n de �xito de la habilidad teniendo en cuenta los 
	 * umbrales y peque�as diferencias en el algoritmo para el robot real sobre ARIA.
	 * 
	 * @return "true" si se ha ejecutado con �xito la habilidad, "false" en caso contrario
	 */
	public boolean exitoRobotReal(){
		boolean exito = false;
		
		/* TODO Incluir como funci�n de �xito alcanzar un punto del mapa */
		
		return exito;
	}
	
	
}
