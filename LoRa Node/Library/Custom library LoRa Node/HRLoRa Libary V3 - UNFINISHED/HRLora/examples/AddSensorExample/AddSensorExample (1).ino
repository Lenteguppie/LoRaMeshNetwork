
/*
  HRLora Send Example
  ---
  The sketch sends the value of the tempurature and the humidity with the uid key on a frequenty of 868MHz
  You only have to insert the uid key and
  which you can generate on https://anothertechproject.com/

  This sketch requires the HRLora library
  This sketch is made specifically for the LoRa Entropy board
  This sketch requires a DHT11 sensor and three jumper cables

  Created 7 Jan 2019
  by Mart-Jan Koedam

  */

#include <HRLora.h>                                     //Include the HRLora library

#include <dht.h>                                        //Include the DHT library to work with the DHT11 sensor
#define DHT11_PIN 7                                     //Define the datapin of the DHT to pin 7 of the node
dht DHT;

HRLora LoRa("####################################");    //Insert your uid key here

void setup() {
  Serial.begin(9600);                                   //Serialbegin for checking the configuration and confirming that the message has been sent
  LoRa.begin();                                         //Initiate the LoRa setup
}

void loop() {                                           //Everything between these brackets ({,}) will be repeated for an infinite time

  int chk = DHT.read11(DHT11_PIN);                      //Reads the temperature and humidity of the DHT11
  int t = DHT.temperature;                              //The tempurature value gets assigned to 't'
  int h = DHT.humidity;                                 //The humidity value gets assigned to 'h'
  Serial.print("Temperature = ");                       //The tempurature gets displayed to the Serial Monitor
  Serial.print(t);
  Serial.println(" Degrees");
  Serial.print("Humidity = ");                          //The humidity gets displayed to the Serial Monitor
  Serial.print(h);
  Serial.println("%");
  delay(1000);                                          //A delay of 1000ms to keep everything organized and clean

  LoRa.send("field1="+(String)t+"&field2="+(String)h);  //Repeatedly send custom message with the previously initiated uid key
}
