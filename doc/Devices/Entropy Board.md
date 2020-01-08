## Inleiding

Het Entropy LoRa Board is een handige module met een RN2483(LoRa) microchip die LoRa-signalen kan versturen. Dit bordje is vooral handig om te testen, te debuggen en vooral om het LoRa-proces te ontdekken en uit te zoeken.
Dit bordje is oorspronkelijk bedoeld om via The Things Network (TTN) te werken, maar wij laten dat achterwege en hebben onze eigen manier bedacht om het bordje te laten werken met LoRa.
> Er wordt LoRa gebruikt, **geen** LoRaWAN!

## Het gebruik van het bordje

In principe werkt het bordje hetzelfde als de Arduino, met de pinout daarbij inbegrepen. Wat wel anders is is de kabel, er is een USB naar microUSB kabel voor nodig. Er kunnen makkelijk, net als bij de Arduino Uno, sensoren of lampjes worden aangesloten.

Voordat je met dit bordje aan de slag kunt, moet het board type in de Arduino IDE nog wel correct worden ingesteld. Het board `Lilypad Arduino USB` is nodig om met dit bordje het gewenste resultaat te behalen.

Om dit bordje te gebruiken zonder The Things Network is er een library geschreven, zie `Documentatie HRLoRa Library` voor alle informatie daarover met een voorbeeld inbegrepen.
> Let op! De HRLora Library is exclusief voor het Entropy LoRa Board.
## Voorbeeld aansluiting met hardware

Om het bordje te testen met de hardware, is er een voorbeeld code meegegeven in de HRLora Library. Het wordt aangeraden eerst de documentatie van de HRLora Library door te nemen, daar staat tevens ook aangegeven hoe de library geïmplementeerd kan worden. Heb je daar al ervaring mee, dan kan je de library [hier](https://github.com/MJKoedam/HRLoraLibrary) downloaden.
  
### De componenten
Eerst moet de hardware op het bordje worden aangesloten, aangezien er geen bestaand ontwerp is van het Entropy bordje, zal de aansluiting niet visueel gemaakt kunnen worden.

Het is belangrijk dat deze 3 pins gevonden worden op het bordje:

1.   `5V`, de power supply pin
    
2.  `GND`, de ground
    
3.   `D7`, in dit geval gebruiken we pin 7
    

Pak daarna de DHT11-sensor en sluit het zo aan met kabeltjes aan het Entropy bord:

1.   De `+` moet naar `5V`
    
2.   De `OUT` moet naar `D7` (hier komt alle data doorheen)
    
3.   De `-` moet naar de `GND`
    
Hieronder is een voorbeelddiagram te zien van de opstelling:

< insert Fritzing >

> In dit diagram wordt een Arduino Uno gebruikt i.p.v. het Entropy LoRa Board, de pinout is hetzelfde behalve dat er bij het Entropy LoRa Board een 'D' voor de nummers van de pins 2-13 staat.
### De code

Wanneer dat is gelukt gaan we kijken naar de code;
Zorg dat de library geïmplementeerd is en dat de voorbeeldcode `AddSensorExample` geopend is.
```arduino
#include <HRLora.h>
```
Dit implementeert de header van de HRLora library, de header zorgt er tevens voor dat de hele library met de code kan samenwerken.
```arduino
#include <dht.h>
```
Dit implementeert de header van de [DHT library](https://github.com/adafruit/DHT-sensor-library). 
> Zorg dat deze is gedownload voor je verder gaat.
```arduino
#define DHT11_PIN 7
```
Hiermee wordt pin D7 als de datapin voor de DHT11 sensor gedefiniëerd.
```arduino
dht DHT;
```
De dht library kan nu worden aangeroepen met `DHT.[functie]`.
```arduino
HRLora LoRa("####################################");
```
Dit is het stukje code waar je je uid-key kan invoeren. De key wordt voor je gegenereerd op onze [Web App]([https://anothertechproject.com/](https://anothertechproject.com/)).
 > In de documentatie van de Web App wordt uitgelegd hoe je de uid key kan ophalen. 
 
 Voer de gegenereerde uid-key in op de plek waar nu hashtags(#) staan.
```arduino
void setup() {
```
Dit is een standaardfunctie van Arduino en tussen de ‘{ }’-haken staat de code die eenmalig wordt uitgevoerd. Dit gebruiken we om alle waarden te instantiëren.
```arduino
Serial.begin(9600);
```
DIt is het eerste stuk code wat we willen gebruiken. Dit zorgt ervoor dat er een Seriële Poort wordt geopend met een baudrate van 9600 waar wij, als gebruikers, gemakkelijk dingen kunnen laten zien van de code wat erg handig is voor debuggen. Hierdoor kunnen we straks de waardes van de DHT11 sensor aflezen.
```arduino
LoRa.begin();
```
Door deze functie wordt er in de library een hele serie code uitgevoerd om de setup van de LoRa chip(RN2483) correct in te stellen. Deze functie checkt ook of er wel een uid key is ingevoerd, zonder de key zal de code niet verder gaan.

```arduino
}

void loop() {
```
De setup wordt gesloten met een curly bracket en er start een nieuwe functie, de `void loop()`. De code die tussen de ‘{ }’-haken staat wordt oneindig herhaaldelijk aangeroepen.
```arduino
int chk = DHT.read11(DHT11_PIN);
```
Hier worden alle waardes van de DHT11 sensor uitgelezen in bewaard in de variabele `chk`.
```arduino
int t = DHT.temperature;

int h = DHT.humidity;
```
Hier worden de temperatuur en de vochtigheid allebei apart bewaard in de variable `t` (temperatuur) en de variabele `h` (humidity/vochtigheid).
> Het variabeltype is een integer (int) die geheeltallige informatie bevat .
```arduino
Serial.print("Temperature = ");
Serial.print(t);
Serial.println(" Degrees");
Serial.print("Humidity = ");
Serial.print(h);
Serial.println("%");
```
Hier wordt de temperatuur en de vochtigheid overzichtelijk afgebeeld op de Seriële Monitor. De output ziet er bijvoorbeeld zo uit:
```
Temperature = 26 Degrees
Humidity = 40%
```

```arduino
delay(1000);
```
En er wordt een delay toegevoegd om alles netjes en georganiseerd te houden
```arduino
LoRa.send("field1="+(String)t+"&field2="+(String)h);
```
Hier worden de gegevens van de DHT11 sensor verstuurd in een bericht via radiosignalen. Stel dat de temperatuur 26 graden is en de vochtigheid 40%, dan ziet het bericht er zo uit:
```
uid=123456-abcdef-789-ghi&field1=26&field2=40
```
> Hier wordt een willekeurige uid key gebruikt.
```arduino
}
```
Daarna wordt de loop nog gesloten met een curly bracket.

  
Nu kan je het programma runnen en als alles goed is gegaan zal het Entropy bordje de temperatuur en de vochtigheid versturen in radiosignalen!

## De volledige code
```arduino
#include <HRLora.h>                                     //Include the HRLora library

#include <dht.h>                                        //Include the DHT library to work with the DHT11 sensor
#define DHT11_PIN 7                                     //Define the datapin of the DHT to pin 7 of the node
dht DHT;

HRLora LoRa("123456-abcdef-789-ghi&field1=26&field2=40");    //Insert your uid key here

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
```
> Ook hier wordt een willekeurige uid key gebruikt.


_2020, 7 januari door Mart-Jan Koedam_
