package br.com.ufu;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class Publisher {

    private static final String TOPIC = "sensor-temperature";

    private static int getRandomByInterval(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static void main(String[] args) {
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "Mosquitto";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);

            while (true) {
                String value = String.valueOf(getRandomByInterval(15, 45));
                MqttMessage message = new MqttMessage(value.getBytes());
                message.setQos(qos);
                sampleClient.publish(TOPIC, message);
                System.out.println("Message published [" + value + "] into " + TOPIC);
                Thread.sleep(1000);
            }
        } catch(MqttException me) {
            System.out.println("Reason " + me.getReasonCode());
            System.out.println("Message " + me.getMessage());
            System.out.println("Localized " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("Exception " + me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
