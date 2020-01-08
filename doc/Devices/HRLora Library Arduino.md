![Arduino Library logo](https://github.com/Lenteguppie/LoRaMeshNetwork/raw/master/doc/Devices/Media/Library%20Logo.png)

## Inleiding

De code voor het aansturen van de LoRa node is best lang, ingewikkeld en niet heel erg overzichtelijk. Daarom hebben wij een library opgesteld om het coderen en het algemeen gebruiken van de node erg makkelijk te maken.

Wat is een library? Een library zorgt ervoor dat lange, ingewikkelde code netjes verwerkt kan worden achter de schermen. De code blijft hetzelfde, maar zit verstopt in de library en kan eenvoudig worden opgeroepen met functies, daar komen we later op terug.  
> Er wordt LoRa gebruikt, **geen** LoRaWAN!
> 
## Implementatie van de library

> Let op! De HRLora Library is exclusief voor het Entropy LoRa Board.

Om de library te gebruiken moet je die eerst downloaden. De library kan nu nog worden gedownload via [deze link](https://github.com/MJKoedam/HRLoraLibrary) maar er wordt verwacht dat de library binnenkort binnen de Arduino IDE beschikbaar is.

Wanneer de library is gedownload kan je die in de libraries-path van Arduino plaatsen, maar de gebruiksvriendelijke manier is om eerst Arduino IDE te openen, naar het kopje `Schets` te gaan, daar klik je op `Bibliotheek toevoegen` en daarna klik je op `voeg .ZIP bibliotheek toe` en dan selecteer je het zipbestand dat je zojuist hebt gedownload. Een iets korter voorbeeld:

`Open Arduino IDE` > `Schets` > `Bibliotheek toevoegen` > `voeg .ZIP bibliotheek toe` > `selecteer HRLora.zip`

## Het gebruik van de library

>Zorg er eerst voor dat het LoRa Entropy Board is aangesloten aan je apparaat!

Let op: Het LoRa Entropy Board werkt niet zoals de normale Arduino-bordjes dus er zullen nog wat instellingen veranderd moeten worden voordat je de code kunt gebruiken. Kort gezegd moet het board gewijzigd worden naar LilyPad Arduino USB(zie hiervoor Documentatie HRLora Entropy LoRa Board). Ook staat hier hoe je de juiste poort selecteert.

Als de library is gedownload en het juiste board is ingesteld kunnen we beginnen met het coderen, hiervoor gebruiken we de meegeleverde voorbeeldcode `SendExample`. Om deze code te openen volg je deze stappen: Ga naar het kopje  `Bestand`, ga door naar `Voorbeelden`, dan zoek je `HRLora` op en selecteer je `SendExample`. Opnieuw hier een korter voorbeeld:

`Bestand` > `Voorbeelden` > `HRLora` > `SendExample`

Wanneer je dat hebt gedaan staat de voorbeeldcode op je scherm. Maak je geen zorgen, we gaan langs elke regel code om je op weg te helpen! Als je dit niet meer nodig hebt ben je good to go!

 
## Code example
```arduino
#include <HRLora.h>
```
Dit implementeert de header van de HRLora library, de header zorgt er tevens voor dat de hele library met de code kan samenwerken, voor meer informatie hierover zie Onderzoeksverslag HRLora Library. 

```arduino
HRLora LoRa("####################################");
```
Dit is het stukje code waar je je uid-key kan invoeren, hier is ook de variabelenaam geinstantiëerd om straks de functies mee op te roepen (_LoRa_). De key wordt voor je gegenereerd op onze [Web App]([https://anothertechproject.com/](https://anothertechproject.com/)).
 > In de documentatie van de Web App wordt uitgelegd hoe je de uid key kan ophalen. 

Voer de gegenereerde uid-key in op de plek waar nu hashtags(#) staan.
```arduino
void setup() {
```
Dit is een standaardfunctie van Arduino en tussen de { }-haken staat de code die eenmalig wordt uitgevoerd. Dit gebruiken we om alle waarden te instantiëren
> Hier gaan we dieper op in bij de Werking van de library.

```arduino
Serial.begin(9600);
```
DIt is het eerste stuk code wat we willen gebruiken. Dit zorgt ervoor dat er een Seriële Poort wordt geopend met een baudrate van 9600 waar wij, als gebruikers, gemakkelijk dingen kunnen laten zien van de code wat erg handig is voor debuggen.

```arduino
LoRa.begin();
```
Dit is 1 van de belangrijkste functies, deze functie komt namelijk van de LoRa library vandaan, dat kan je zien aan de tekst _LoRa_ wat we eerder hebben ingevuld. In de library wordt er een hele serie code uitgevoerd om de setup van de LoRa chip(RN2483) correct in te stellen.
> Hier gaan we straks nog wat dieper op in.

Deze functie checkt ook of er wel een uid key is ingevoerd, zonder de key zal de code niet verder gaan.

```arduino
}

void loop() {
```
De setup wordt gesloten met een curly bracket en er start een nieuwe functie, de `void loop()`. De code die tussen de { }-haken staat wordt oneindig herhaaldelijk aangeroepen.

```arduino
LoRa.send("Hello LoRa!");
```
Dit is de tweede functie van de library, deze functie zorgt ervoor dat er daadwerkelijk een bericht wordt verzonden. De functie neemt de eerder ingevoerde uid-key en zet daar het bericht achter en dat wordt in een hexadecimale string in radiosignalen verstuurd. Tussen de haakjes en de dubbele komma kan een willekeurig bericht worden toegevoegd.
#### Voorbeeld:
```arduino
LoRa.send("<custom message>");
```
 Dus bijvoorbeeld: 
 ```arduino
 LoRa.send('Dit is een willekeurig bericht.');
}
```
De loop wordt gesloten met een curly bracket.

Dit is de code om een bericht te verzenden met de frequentie op 868000000Hz.

En that's it! Nu hoef je de code alleen maar te uploaden en dan zal (als alles goed is gegaan) de LoRa node jouw persoonlijke bericht gaan versturen!

## Werking van de library

> We gaan hier iets dieper in op de twee functies van de library, en wat er daadwerkelijk gebeurd 'achter de schermen'.
```arduino
LoRa.begin();
```
Deze functie, zoals al eerder was uitgelegd, is om de LoRa chip correct te instantiëren. Het eerste wat gebeurt is een stukje code om te kijken of de uid-key wel is ingevoerd, als dat niet het geval is dan blijft het programma doorgaan in een infinite while-loop:
```arduino
while (_uid == "") {
Serial.println("[HRLoRa] Error: UID is empty");
}
```

Dan wordt de seriële poort van Serial1 geopend, met een baudrate van 57600. Via Serial1 worden alle commando's naar de LoRa chip gestuurd. Ook wordt vanaf deze poort gecheckt op de "ok-response":
```arduino
Serial1.begin(57600);
```

Hierna worden alle commando's voor de LoRa chip 1 voor 1 uitgevoerd.
> Zie Documentatie LoRa node of de [_command reference user guide_](https://ww1.microchip.com/downloads/en/DeviceDoc/40001784B.pdf) voor meer informatie over de commando's die worden gebruikt.

Na elk commando wordt er gecheckt of er een "ok-response" wordt teruggegeven. Dat wordt gecontroleerd door de te kijken of er seriële waarden beschikbaar zijn in Serial1. Is dat niet het geval, dan is er geen "ok-response" teruggekomen en moet het commando opnieuw verstuurd worden, is dat wel het geval, dan wordt er gecontroleerd om de beschikbare waarde in Serial1 daadwerkelijk de "ok-response" is. De "ok-response" is in decimalen gegeven en zou "_1111071310_" moeten zijn, wanneer dat niet het geval is dan is er geen "ok-response" teruggekomen en moet het commando opnieuw verstuurd worden.

```arduino
LoRa.send("Hello LoRa!");
```
Deze functie, zoals ook al eerder was uitgelegd, is om de LoRa chip oneindig herhaaldelijk een bericht te laten versturen met radiosignalen. De uid-key wordt gepakt en in een string gezet, dan wordt er een "&"-teken in de string gezet, wat betekent dat hierna het persoonlijk bericht komt, en daarachter komt het persoonlijke bericht. Dat is de volledige string en dat ziet er met dit voorbeeld zo uit:
```arduino
HRLora LoRa("123456-abcdef-789-ghi"); //Dit is een willekeurige uid-key
LoRa.send("HRLora voorbeeld");  	  //Dit is het persoonlijke bericht
```

Hieronder zie je hoe de uid-key en het persoonlijk bericht wordt samengevoegd:

`'uid=123456-abcdef-789-ghi&HRLora voorbeeld'`

Het "&"-teken scheidt de uid-key van het bericht.
Dit bericht wordt omgezet naar een hexadecimale string en het hexadecimale bericht wordt verstuurd via radiosignalen.

## De volledige code

```arduino
#include <HRLora.h>                                   //Include the HRLora library

HRLora LoRa("123456-abcdef-789-ghi");  //Insert your uid key here

void setup() {
  Serial.begin(9600);                                 //Serialbegin for checking the configuration and confirming that the message has been sent
  LoRa.begin();                                       //Initiate the LoRa setup
}

void loop() {
  LoRa.begin();
  LoRa.send("Hello LoRa!");                           //Repeatedly send custom message with the previously initiated uid key
  delay(1000);
}
```
> De uid key is willekeurig



_2019, 11 december door Mart-Jan Koedam_
## Inleiding

De code voor het aansturen van de LoRa node is best lang, ingewikkeld en niet heel erg overzichtelijk. Daarom hebben wij een library opgesteld om het coderen en het algemeen gebruiken van de node erg makkelijk te maken.

Wat is een library? Een library zorgt ervoor dat lange, ingewikkelde code netjes verwerkt kan worden "achter de schermen". De code blijft hetzelfde, maar zit verstopt in de library en kan eenvoudig worden opgeroepen met functies, daar komen we later op terug.  
> Er wordt LoRa gebruikt, **geen** LoRaWAN!
> 
## Implementatie van de library

> Let op! De HRLora Library is exclusief voor het Entropy LoRa Board.

Om de library te gebruiken moet je die eerst downloaden. De library kan nu nog worden gedownload via [deze link](https://github.com/MJKoedam/HRLoraLibrary) maar er wordt verwacht dat de library binnenkort binnen de Arduino IDE beschikbaar is.

Wanneer de library is gedownload kan je die in de libraries-path van Arduino plaatsen, maar de gebruiksvriendelijke manier is om eerst Arduino IDE te openen, naar het kopje `Schets` te gaan, daar klik je op `Bibliotheek toevoegen` en daarna klik je op `voeg .ZIP bibliotheek toe` en dan selecteer je het zipbestand dat je zojuist hebt gedownload. Een iets korter voorbeeld:

`Open Arduino IDE` > `Schets` > `Bibliotheek toevoegen` > `voeg .ZIP bibliotheek toe` > `selecteer HRLora.zip`

## Het gebruik van de library

>Zorg er eerst voor dat het LoRa Entropy Board is aangesloten aan je apparaat!

Let op: Het LoRa Entropy Board werkt niet zoals de normale Arduino-bordjes dus er zullen nog wat instellingen veranderd moeten worden voordat je de code kunt gebruiken. Kort gezegd moet het board gewijzigd worden naar LilyPad Arduino USB(zie hiervoor Documentatie HRLora Entropy LoRa Board). Ook staat hier hoe je de juiste poort selecteert.

Als de library is gedownload en het juiste board is ingesteld kunnen we beginnen met het coderen, hiervoor gebruiken we de meegeleverde voorbeeldcode `SendExample`. Om deze code te openen volg je deze stappen: Ga naar het kopje  `Bestand`, ga door naar `Voorbeelden`, dan zoek je `HRLora` op en selecteer je `SendExample`. Opnieuw hier een korter voorbeeld:

`Bestand` > `Voorbeelden` > `HRLora` > `SendExample`

Wanneer je dat hebt gedaan staat de voorbeeldcode op je scherm. Maak je geen zorgen, we gaan langs elke regel code om je op weg te helpen! Als je dit niet meer nodig hebt ben je good to go!

 
## Code example
```arduino
#include <HRLora.h>
```
Dit implementeert de header van de HRLora library, de header zorgt er tevens voor dat de hele library met de code kan samenwerken, voor meer informatie hierover zie Onderzoeksverslag HRLora Library. 

```arduino
HRLora LoRa("####################################");
```
Dit is het stukje code waar je je uid-key kan invoeren, hier is ook de variabelenaam geinstantiëerd om straks de functies mee op te roepen (_LoRa_). De key wordt voor je gegenereerd op onze [Web App]([https://anothertechproject.com/](https://anothertechproject.com/)).
 > In de documentatie van de Web App wordt uitgelegd hoe je de uid key kan ophalen. 

Voer de gegenereerde uid-key in op de plek waar nu hashtags(#) staan.
```arduino
void setup() {
```
Dit is een standaardfunctie van Arduino en tussen de "{ }"-haken staat de code die eenmalig wordt uitgevoerd. Dit gebruiken we om alle waarden te instantiëren
> Hier gaan we dieper op in bij de Werking van de library.

```arduino
Serial.begin(9600);
```
DIt is het eerste stuk code wat we willen gebruiken. Dit zorgt ervoor dat er een Seriële Poort wordt geopend met een baudrate van 9600 waar wij, als gebruikers, gemakkelijk dingen kunnen laten zien van de code wat erg handig is voor debuggen.

```arduino
LoRa.begin();
```
Dit is 1 van de belangrijkste functies, deze functie komt namelijk van de LoRa library vandaan, dat kan je zien aan de tekst _LoRa_ wat we eerder hebben ingevuld. In de library wordt er een hele serie code uitgevoerd om de setup van de LoRa chip(RN2483) correct in te stellen.
> Hier gaan we straks nog wat dieper op in.

Deze functie checkt ook of er wel een uid key is ingevoerd, zonder de key zal de code niet verder gaan.

```arduino
}

void loop() {
```
De setup wordt gesloten met een curly bracket en er start een nieuwe functie, de `void loop()`. De code die tussen de �{ }�-haken staat wordt oneindig herhaaldelijk aangeroepen.

```arduino
LoRa.send("Hello LoRa!");
```
Dit is de tweede functie van de library, deze functie zorgt ervoor dat er daadwerkelijk een bericht wordt verzonden. De functie neemt de eerder ingevoerde uid-key en zet daar het bericht achter en dat wordt in een hexadecimale string in radiosignalen verstuurd. Tussen de haakjes en de dubbele komma kan een willekeurig bericht worden toegevoegd.
#### Voorbeeld:
```arduino
LoRa.send("<custom message>");
```
 Dus bijvoorbeeld: 
 ```arduino
 LoRa.send("Dit is een willekeurig bericht.");
}
```
De loop wordt gesloten met een curly bracket.

Dit is de code om een bericht te verzenden met de frequentie op 868000000Hz.

En that�s it! Nu hoef je de code alleen maar te uploaden en dan zal (als alles goed is gegaan) de LoRa node jouw persoonlijke bericht gaan versturen!

## Werking van de library

> We gaan hier iets dieper in op de twee functies van de library, en wat er daadwerkelijk gebeurd "achter de schermen".
```arduino
LoRa.begin();
```
Deze functie, zoals al eerder was uitgelegd, is om de LoRa chip correct te instantiëren. Het eerste wat gebeurt is een stukje code om te kijken of de uid-key wel is ingevoerd, als dat niet het geval is dan blijft het programma doorgaan in een infinite while-loop:
```arduino
while (_uid == "") {
Serial.println("[HRLoRa] Error: UID is empty");
}
```

Dan wordt de seriële poort van Serial1 geopend, met een baudrate van 57600. Via Serial1 worden alle commando's naar de LoRa chip gestuurd. Ook wordt vanaf deze poort gecheckt op de "ok-response":
```arduino
Serial1.begin(57600);
```

Hierna worden alle commando's voor de LoRa chip 1 voor 1 uitgevoerd.
> Zie Documentatie LoRa node of de [_command reference user guide_](https://ww1.microchip.com/downloads/en/DeviceDoc/40001784B.pdf) voor meer informatie over de commando's die worden gebruikt.

Na elk commando wordt er gecheckt of er een "ok-response" wordt teruggegeven. Dat wordt gecontroleerd door de te kijken of er seriële waarden beschikbaar zijn in Serial1. Is dat niet het geval, dan is er geen "ok-response" teruggekomen en moet het commando opnieuw verstuurd worden, is dat wel het geval, dan wordt er gecontroleerd om de beschikbare waarde in Serial1 daadwerkelijk de "ok-response" is. De "ok-response" is in decimalen gegeven en zou "_1111071310_" moeten zijn, wanneer dat niet het geval is dan is er geen "ok-response" teruggekomen en moet het commando opnieuw verstuurd worden.

```arduino
LoRa.send("Hello LoRa!");
```
Deze functie, zoals ook al eerder was uitgelegd, is om de LoRa chip oneindig herhaaldelijk een bericht te laten versturen met radiosignalen. De uid-key wordt gepakt en in een string gezet, dan wordt er een "&"-teken in de string gezet, wat betekent dat hierna het persoonlijk bericht komt, en daarachter komt het persoonlijke bericht. Dat is de volledige string en dat ziet er met dit voorbeeld zo uit:
```arduino
HRLora LoRa("123456-abcdef-789-ghi"); //Dit is een willekeurige uid-key
LoRa.send("HRLora voorbeeld");  	  //Dit is het persoonlijke bericht
```

Hieronder zie je hoe de uid-key en het persoonlijk bericht wordt samengevoegd:

`'uid=123456-abcdef-789-ghi&HRLora voorbeeld'`

Het "&"-teken scheidt de uid-key van het bericht.
Dit bericht wordt omgezet naar een hexadecimale string en het hexadecimale bericht wordt verstuurd via radiosignalen.

## De volledige code

```arduino
#include <HRLora.h>                                   //Include the HRLora library

HRLora LoRa("123456-abcdef-789-ghi");  //Insert your uid key here

void setup() {
  Serial.begin(9600);                                 //Serialbegin for checking the configuration and confirming that the message has been sent
  LoRa.begin();                                       //Initiate the LoRa setup
}

void loop() {
  LoRa.begin();
  LoRa.send("Hello LoRa!");                           //Repeatedly send custom message with the previously initiated uid key
  delay(1000);
}
```
> De uid key is willekeurig



_2019, 11 december door Mart-Jan Koedam_