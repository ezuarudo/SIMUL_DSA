package controlador.navegacion;

import controlador.gestorHabilidades.Habilidad;
import infraestructura.*;

/**
 * Clase que hereda de Habilidad y define las caracteristicas 
 * propias de esta habilidad e implementa su bucle de ejecucion 
 * y la condicion del evento de finalizacion.
 * 
 * @author Jose Luis Diaz Cebrian
 * @version 1.0
 * 
 */
public class CubrirDistancia extends Habilidad{
	
	/**
	 * Indica la velocidad lineal de las ruedas que permiten el avance en linea
	 * recta del robot.
	 */
	private final double VELOCIDAD;

	/**
	 * Umbral maximo de acercamiento del robot a un obstaculo. Si el robot se encuentra a menor distancia, 
	 * es necesario evitar el obstaculo.
	 */
	private final double UMBRAL;
	
	/**
	 * Indica la direccion por la que se va a rodear el obstaculo. Esta a "true" si se evita por
	 * la izquierda, y a "false" en caso contrario.
	 */
	private boolean izquierda;
	
	/**
	 * Indica la distancia en centimetros que debe cubrir el robot realizando un 
	 * movimiento rectilineo.
	 */
	private double distancia;

	/**
	 * Indica la distancia actual en centimetros que ha recorrido el robot hasta el 
	 * momento.
	 */
	private double dist_actual;
	
	/**
	 * Indica el nivel de desvio con respecto a su orientacion inicial que realiza 
	 * el robot al rodear un obstaculo.
	 */
	private int desvio;
	
	/**
	 * Indica cuanto ha girado el robot desde su orientacion inicial para rodear 
	 * un posible obstaculo.
	 */
	private int giro;

	/**
	 * Indica si se esta rodeando un obstaculo encontrado en la trayectoria.
	 */
	private boolean rodeo;
	
	/**
	 * Indica si se esta reorientando el robot tras rodear un obstaculo.
	 */
	private boolean reorientarse;
	
	
	/**
	 * Inicializa los atributos de la habilidad segun resultados obtenidos en experimentaciones
	 * previas de robotica.
	 * 
	 * @param robot El robot del sistema que hace uso de la habilidad
	 * @param sim A "true" si se utiliza la arquitectura sobre simulador
	 * @param distancia La distancia en metros que se desea avanzar
	 * 
	 */
	public CubrirDistancia(Robot robot, boolean sim, int distancia){
		super("CubrirDistancia", robot, sim);
		this.rodeo = false;
		this.reorientarse = false;
		this.izquierda = false;
		if(sim){
			this.VELOCIDAD = 30.0;
			this.UMBRAL = 100.0;
			this.dist_actual = (robot.getMotor().getEncoderIzdo() + robot.getMotor().getEncoderIzdo())/2;
		} else{
			this.VELOCIDAD = 60.0;
			this.UMBRAL = 300.0;
			this.dist_actual = robot.getMotor().getDistancia();
		}
		this.giro = 0;
		this.desvio = 0;
		this.distancia = distancia + this.dist_actual;
	}
	
	
	/**
	 * Devuelve el valor actual de la velocidad asignada a las ruedas del robot.
	 * 
	 * @return La velocidad lineal del robot
	 * 
	 */
	public double getVelocidad(){
		return this.VELOCIDAD;
	}

	/**
	 * Devuelve la distancia total que el robot debe cubrir en linea recta.
	 * 
	 * @return La distancia a cubrir
	 * 
	 */
	public double getDistancia(){
		return this.distancia;
	}

	/**
	 * Devuelve la distancia actual que el robot lleva recorrida.
	 * 
	 * @return La distancia actual cubierta
	 * 
	 */
	public double getDistanciaActual(){
		return this.dist_actual;
	}
	
	/**
	 * @see Habilidad#ejecutar()
	 */
	public void ejecutar(){
		int[] ruedas = new int[2]; //Izda-Dcha
		Motor m = (Motor) super.getRobot().getActuador("motor");
		
		if(super.getSim()){
			// ruedas = ejecSimulador(m);
                        ruedas = ejecRobotReal(m);
                        this.dist_actual = (int) m.getDistancia();  
		//	this.dist_actual = (m.getEncoderIzdo() + m.getEncoderIzdo())/2;
		} else{
			ruedas = ejecRobotReal(m);
			this.dist_actual = (int) m.getDistancia();
		}
		
		m.setRuedas(ruedas[0], ruedas[1]);	
	}
	
	/**
	 * Da un valor a las ruedas del robot teniendo en cuenta los umbrales
	 * y pequenas diferencias en el algoritmo para el simulador.
	 * 
	 * @return Las velocidades a asignar a las ruedas del robot
	 */
	public int[] ejecSimulador(Motor m){
		
		int ruedas[] = new int[2]; //Izda-Dcha
		Sonar s = (Sonar) super.getRobot().getSensor("sonar");
		double ds_iz = (s.getValorSonar(2)+s.getValorSonar(3))/2;
		double ds_de = (s.getValorSonar(4)+s.getValorSonar(5))/2;
		double ds_fr_izda = Math.max(s.getValorSonar(0), s.getValorSonar(1));
		double ds_fr_dcha = Math.max(s.getValorSonar(6), s.getValorSonar(7));
		double guia_dcha = s.getValorSonar(3);
		double guia_izda = s.getValorSonar(0);
		double ds_fr = Math.max(ds_fr_izda, ds_fr_dcha);
		
		if(ds_fr_izda>ds_fr_dcha) //Esquivar por la derecha
			this.izquierda = false;
		else //Esquivar por la izquierda
			this.izquierda = true;
		
		/* Avanza por defecto */
		ruedas[0] = 30;
		ruedas[1] = 30;
		
		/* Condiciones de control de la habilidad */
		if(ds_fr > UMBRAL){
			this.rodeo = true;
		}else if(izquierda){
			if(m.getEncoderIzdo()==m.getEncoderDcho()+this.desvio+2){
				this.rodeo = false;
				this.reorientarse = true;
			}
		} else{
			if(this.desvio+2+m.getEncoderIzdo()==m.getEncoderDcho()){
				this.rodeo = false;
				this.reorientarse = true;
			}
		}
		if(this.desvio==0)
			this.reorientarse = false;
  
		/* Rodeo reactivo del obstaculo */
		if(this.rodeo){ //Si se ha detectado un obstaculo, el comportamiento cambia para esquivarlo
			if(izquierda){
				if(ds_fr > UMBRAL) { //Si tengo el objeto delante, giro a la izquierda
					ruedas[0] = -10;
					ruedas[1] = 50;
					this.desvio = (int) (m.getEncoderDcho() - m.getEncoderIzdo());
				} else if(ds_de > UMBRAL){ //Si no, si estoy cerca del objeto, avanzo
					ruedas[0] = 30;
					ruedas[1] = 30;
				} else if(guia_dcha < UMBRAL){ //Si no, giro a la derecha
					ruedas[0] = 50/2;
					ruedas[1] = 10;
				}
			} else{
				if(ds_fr > UMBRAL) { //Si tengo el objeto delante, giro a la derecha
					ruedas[0] = 50;
					ruedas[1] = -10;
					this.desvio = (int) (m.getEncoderIzdo() - m.getEncoderDcho());
				} else if(ds_iz > UMBRAL){ //Si no, si estoy cerca del objeto, avanzo
					ruedas[0] = 30;
					ruedas[1] = 30;
				} else if(guia_izda < UMBRAL){ //Si no, giro a la izquierda
					ruedas[0] = 10;
					ruedas[1] = 50/2;
				}
			}
		}
		
		/* Reorientacion tras el rodeo */
		if(this.reorientarse){
			if(this.izquierda){
				ruedas[0] = 0;
				ruedas[1] = 30;
				this.desvio = (int) (m.getEncoderIzdo() - m.getEncoderDcho())-1;
			} else{
				ruedas[0] = 30;
				ruedas[1] = 0;
				this.desvio = (int) (m.getEncoderDcho() - m.getEncoderIzdo())-1;
			}
		}

		return ruedas;
	}
	
	/**
	 * Da un valor a las ruedas del robot teniendo en cuenta los umbrales
	 * y pequenas diferencias en el algoritmo para el robot real sobre ARIA.
	 * 
	 * @return Las velocidades a asignar a las ruedas del robot
	 */
	public int[] ejecRobotReal(Motor m){
		
		int ruedas[] = new int[2]; //Izda-Dcha
		int giro_inicio = 0;
		Sonar s = (Sonar) super.getRobot().getSensor("sonar");
		double ds_iz = (s.getValorSonar(0)+s.getValorSonar(1))/2;
		double ds_de = (s.getValorSonar(3)+s.getValorSonar(4))/2;
		double ds_fr_izda = Math.min(s.getValorSonar(2), s.getValorSonar(3));
		double ds_fr_dcha = Math.min(s.getValorSonar(4), s.getValorSonar(5));
		double guia_dcha = s.getValorSonar(7);
		double guia_izda = s.getValorSonar(0);
		double ds_fr = Math.min(ds_fr_izda, ds_fr_dcha);
		
		if(ds_fr < UMBRAL){
			if(ds_fr_izda<ds_fr_dcha) //Esquivar por la derecha
				this.izquierda = false;
			else //Esquivar por la izquierda
				this.izquierda = true;
		}
		
		/* Avanza por defecto */
		ruedas[0] = 60;
		ruedas[1] = 60;
		
		/* Condiciones de control de la habilidad */
		if(ds_fr < UMBRAL && !this.reorientarse){
			this.rodeo = true;
		} else{
			if(this.giro<=this.desvio*(-1)-4){
				this.rodeo = false;
				this.reorientarse = true;	
			}
		}
		if(this.reorientarse && this.giro==4)
			this.reorientarse = false;
  
		/* Rodeo reactivo del obstaculo */
		if(this.rodeo){ //Si se ha detectado un obstaculo, el comportamiento cambia para esquivarlo
			if(this.izquierda){
				if (ds_fr < UMBRAL) { //Si tengo el objeto delante, giro a la izquierda
					ruedas[0] = -20;
					ruedas[1] = 140;
					this.giro++;
					this.desvio = this.giro - giro_inicio;
				} else if(ds_de < 700){ //Si no, si estoy cerca del objeto, avanzo
					ruedas[0] = 60;
					ruedas[1] = 60;
				} else if(guia_dcha > 1000){ //Si no, giro a la derecha
					ruedas[0] = 140;
					ruedas[1] = -20;
					this.giro--;
					/*this.giro = (int) m.getGiro();*/
				}
			} else{
				if (ds_fr < UMBRAL) { //Si tengo el objeto delante, giro a la derecha
					ruedas[0] = 140;
					ruedas[1] = -20;
					this.giro++;
					this.desvio = this.giro - giro_inicio;
				} else if(ds_iz < 700){ //Si no, si estoy cerca del objeto, avanzo
					ruedas[0] = 60;
					ruedas[1] = 60;
				} else if(guia_izda > 1000){ //Si no, giro a la izquierda
					ruedas[0] = -20;
					ruedas[1] = 140;
					this.giro--;
				}
			}
		}
		
		/* Reorientacion tras el rodeo */
		if(this.reorientarse){
			if(this.izquierda){
				ruedas[0] = 0;
				ruedas[1] = 60;	
			} else{
				ruedas[0] = 60;
				ruedas[1] = 0;
			}
			this.giro++;
		}
		
		return ruedas;
	}

	/**
	 * @see Habilidad#comprobarExito()
	 */
	public boolean comprobarExito(){
		boolean exito = false;
		
		if(this.dist_actual>=this.distancia)
			exito = true;
		
		return exito;
	}
	
	
}
