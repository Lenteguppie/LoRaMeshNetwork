## NetLink Gettings Started Guide

NetLink is bedacht voor gemakkelijk ophalen van applicatie dat van het LoRa Network. Bij NetLink staan een aantal belangrijke standpunten centraal.

 1. Gemakkelijk integreerbaar
 2. Snelle reactietijd
 3. Platform onafhankelijk 

Om deze 3 standpunten mogelijk te maken is een Web API bedacht die deze punten mogelijk maakt.

### Welke functie zijn mogelijk met dit API

* Heel het applicatie data overzicht ophalen
* Data van een applicatie ophalen node specifiek
* Lijst geregistreerde nodes ophalen

#### Applicatie nodes data overzicht ophalen

U kunt gemakkelijk u applicatie in het geheel inladen in uw eigen applicatie om de data te verwerken. Dit doet u als volgt:

U moet er voor zorgen dat uw applicatie een web request maakt naar de volgende URL: 

    netlink.lora.anothertechproject.com/app?accesskey=[accesskey]&limit=[dataLimit]

De volgende data fields zijn van belang om al uw data op te halen:

  - accesskey (applicatie specifieke sleutel)
  - limit (data limit per node) (meest recente)

Deze velden zijn voor belang om precies op te vragen welke data u nodig heeft en van welke applicatie u de data wilt ophalen.

Als u alles correct heeft ingevoerd zal uw data aanvraag worden beantwoord met een JSON representatie van uw nodes in de applicatie.

#### Voorbeeld

![enter image description here](https://github.com/Lenteguppie/LoRaMeshNetwork/raw/master/doc/Network/media/dataJSONNetLinkNode.PNG)

Wat u hierboven zie is een testapplicatie met twee node genaamd "Test Node 1" en "Test Node 2" wat u hier in kunt zien is wat de locatie is van nodes en alle andere gegevens zie van belang zijn bij deze nodes.
Al de nodes data kunt u terug vinden in de node dataset. hierin staan alle gegevens van het LoRa bericht die zijn ontvangen. 
Omdat er een basis encryptie over de data heen zit kun u dit niet gelijk lezen maar zult u het moeten decoderen d.m.v. base64 decoder. deze encryptie zal voor elke dataset hetzelfde zijn.

#### Node data overzicht ophalen

Voor het gemakkelijk ophalen van data van een specifieke node is het ook mogelijk om specifiek te selecteren van welke node u de data wilt ophalen. dit doet u als volgt:

U moet er voor zorgen dat uw applicatie een web request maakt naar de volgende URL: 

    netlink.lora.anothertechproject.com/node?accesskey=[accesskey]&nodeuid=[nodeUID]&limit=[dataLimit]

De volgende data fields zijn van belang om al uw data op te halen:

  - accesskey (applicatie specifieke sleutel)
  - nodeuid (node specifieke identifier)
  - limit (data limit per node) (meest recente)

Deze velden zijn voor belang om precies op te vragen welke data u nodig heeft en van welke applicatie en van welke node u de data wilt ophalen.
Als u alles correct heeft ingevoerd zal uw data aanvraag worden beantwoord met een JSON representatie.

![NodeSpecificData](https://github.com/Lenteguppie/LoRaMeshNetwork/raw/master/doc/Network/media/dataJSONNetLinkNode.PNG)
In dit voorbeeld ziet u de specifiek node met het daarvoor specifieke dataset. Dit maakt mogelijk om op specifieke locaties data op te halen, en hiermee data te verwerken.

#### Nodes overzicht ophalen

Om al uw nodes van uw applicatie op te halen is het mogelijk om de volgende web request te doen.

U moet er voor zorgen dat uw applicatie een web request maakt naar de volgende URL: 

    netlink.lora.anothertechproject.com/nodes?accesskey=[accesskey]

De volgende data fields zijn van belang om al uw data op te halen:

  - accesskey (applicatie specifieke sleutel)

Deze velden zijn voor belang om precies op te vragen van welke applicatie u de nodes wilt ophalen. Als u alles correct heeft ingevoerd zal uw data aanvraag worden beantwoord met een JSON representatie.
![NodeSpecificData](https://github.com/Lenteguppie/LoRaMeshNetwork/raw/master/doc/Network/media/dataJSONNetLinkNodeList.png)
In dit voorbeeld ziet u een testapplicatie met twee nodes. Met die hierboven afgebeelde JSON wordt het mogelijk gemaakt om van uw applicatie alle nodes in te zien en welke gegevens deze hebben.s