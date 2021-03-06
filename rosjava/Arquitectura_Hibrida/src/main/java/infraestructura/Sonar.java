package infraestructura;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;
/**
 * Clase que hereda de Sensor y define las caractersticas 
 * propias de este tipo de sensor e implementa la funcionalidad 
 * relacionada con ellas.
 * 
 * @author Jos Luis Daz Cebrin
 * @version 1.0
 * 
 */
public class Sonar extends Sensor{

	/**
	 * Nmero de sensores snar que posee el robot.
	 */
	private final int NUM_SONAR;
	
	/**
	 * Valores detectados por todos y cada uno de los sensores snar del robot.
	 */
	private double[] valores;
	
	
	/**
	 * Inicializa el sensor con el nombre "sonar" y un valor inicial de
	 * 0 en cada uno de los "num" sensores snar del robot.
	 * 
	 * @param num El nmero de sensores snar que posee el robot
	 * 
	 */
        private ConnectedNode node;
        Subscriber<std_msgs.String> subscriber;

        public ConnectedNode getNode() {
		return node;
	}


	public void setNode(ConnectedNode node) {
		this.node = node;
	}


	public Sonar(int num,final ConnectedNode node){
		super("sonar");
                this.node=node;
                subscriber = node.newSubscriber("/sonar", std_msgs.String._TYPE);
		this.NUM_SONAR = num;
		this.valores = new double[this.NUM_SONAR];
                subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
                @Override
                  public void onNewMessage(std_msgs.String message) 
                      {
                       String[] temp2;
                       String delimiter=",";
		       temp2=message.getData().split(delimiter);
		       for(int i=0;i<temp2.length;i++)
		         {
		           valores[i]=Double.parseDouble(temp2[i]);			              
		         }
                     //  System.out.println("I heard: \"" + message.getData() + "\"");
                      }
                  });
	}
	
	
	/**
	 * Devuelve el nmero de sensores snar del robot.
	 * 
	 * @return El nmero de sensores snar
	 * 
	 */
	public int getNumSonar(){
		return this.NUM_SONAR;
	}
	
	/**
	 * Devuelve el valor detectado por todos y cada uno de los sensores snar.
	 * 
	 * @return El array con los valores de todos los sensores snar
	 * 
	 */
	public double[] getValores(){
		return this.valores;
	}
	
	/**
	 * Devuelve el valor detectado por un snar en concreto.
	 * 
	 * @param indice El nmero de snar del que se quiere obtener la informacin
	 * @return El valor detectado por dicho snar
	 * 
	 */
	public double getValorSonar(int indice){
		return this.valores[indice];
	}
	
	/**
	 * Asigna un valor detectado a cada uno de los sensores snar.
	 * 
	 * @param valores El array con los valores a asignar a los sensores snar
	 * 
	 */
	public void setValores(double[] valores){
		for(int i=0; i<this.NUM_SONAR; i++){
			this.valores[i] = valores[i];
		}
	}
	
	/**
	 * Asigna un valor detectado a un snar en concreto.
	 * 
	 * @param indice El nmero del snar del que se quiere modificar su informacin
	 * @param valor El valor a asignar a dicho snar
	 * 
	 */
	public void setValorSonar(int indice, double valor){
		this.valores[indice] = valor;
	}


	
}
