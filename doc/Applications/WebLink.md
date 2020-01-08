								

# WebLink Reference

WebLink wordt gebruikt om informatie uit te kunnen wisselen tussen onze servers.
Een aantal voorbeelden zijn:
 - Login
 - Registeren
 - Applicatie aanmaken & beheren (Nodes Beheren)
 - Gateways toevoegen en onderhouden
 
In dit document zal de informatie uitwisseling tussen onze servers uitvoerig worden beschreven.
Onze informatie flow is kan worden gevisualiseerd als volgt:
![DataFlow](https://drive.google.com/uc?authuser=0&id=1uH9OnSnUf2Hpal254VgFSd8FlSfuiM2t&export=download)
1. Request with fields
2. Awnser with fields JSON

## Registeren

Het maken van een account is nodig om alle instellingen op te slaan van de gebruiker.
dit is van belang omdat er per gebruiker bijvoorbeeld nodes and gateways gekoppeld kunnen worden. Om te kunnen weten door wie deze in beheer zijn, is het nodig om een account aan te maken.

### Voorbeeld:

Request URL:  *https://[SERVER_URL]/login?email=[USER_EMAIL]&password=[USER_PASSWORD]&firstname=[USER_FIRSTNAME]
&lastname=[USER_PASSWORD]&username=[USER_USERNAME]*

Data Fields:
 1. SERVER_URL (Adres waarnaar de informatie wordt gestuurd)
 2. USER_EMAIL
 3. USER_PASSWORD
 4. USER_FIRSTNAME
 5. USER_LASTNAME
 6. USER_USERNAME

Om een account van een gebruiker op te kunnen stellen zijn deze gegevens hierboven nodig.
Met deze gegevens kan een account geworden gepersonaliseerd. dit is nodig om elke gebruiker van elkaar te kunnen onderscheiden. alle informatie wordt verstuurd naar de server via de SERVER URL. 

De server kan de volgende responses terug sturen:

1. Gebruiker succesvol aangemaakt.
2. Gebruiker bestaat al met dit gebruikernaam of email
3. Invalid register request  (Ongeldige register aanvraag)

Als de server met een response antwoord dat de "Gebruiker succesvol is aangemaakt", ziet dit eruit als volgt:

    {"registration":"User created successfully"}
  
Ook kan de server antwoord gegeven dat "Gebruiker bestaat al met dit gebruikernaam of email", dit ziet er als volgt uit:

    {"error":"User already exists with this email and/or username"}

Als alle informatie niet volledig of met een fout formaat is overgekomen antwoord de server "Invalid register request", dit ziet er als volgt uit:

    {"error":"invalid register request"}

## Login

Bij login is het voor ons van belang dat wij weten wie er toegang heeft tot welke gegevens hiervoor maken wij gebruikt van sessie cookies die bijhouden wie er op welk moment is ingelogd. voor veiligheidsreden, maken wij niet bekend hoe wij deze dat verwerken. Echter word hier beschreven hoe de informatie flow van dit proces werkt. 

### Voorbeeld:
Request URL:  *https://[SERVER_URL]/login?email=[USER_EMAIL]&password=[USER_PASSWORD]*

Data Fields:
 1. SERVER_URL (Adres waarnaar de informatie wordt gestuurd)
 2. USER_EMAIL
 3. USER_PASSWORD

Deze data fields zijn nodig om precies te kunnen weten wij er in logt en of zij toestemming hebben om in te mogen inloggen. vandaar natuurlijk het email adres en de gebruikersnaam.
De server url wordt gebruikt om deze informatie naar de juiste server toe te sturen. Hiermee wordt gevraagd of alle gegevens kloppen en of de gebruiker een sessie mag aanmaken om in het systeem te komen. 

Uit deze conversatie zijn twee uitkomsten mogelijk:

 1. Ongeldige gebruikersnaam of wachtwoord.
 2. Geldige gebruikersnaam en wachtwoord combinatie
 3. Invalid login request (Ongeldige login aanvraag)

De eerste uitkomst is duidelijk als het gebruikersnaam of wachtwoord combinatie incorrect is wordt dit bericht terug gestuurd. Dit ziet er als volgt uit:

    {"error":"Incorrect username or password"}

Met de tweede uitkomst heeft de server alle informatie gevalideerd en een sessie aan de gebruiker gekoppeld. Dit ziet er als volgt uit:

    {"sessionKey":"[UNIQUE ID]","sessionTimeout":"86400"}

Deze sessie maakt mogelijk om acties in het systeem te kunnen uitvoeren en acties die de gebruiker doet te kunnen toepassen. 

Met de derde uitkomst heeft de server niet alle informatie volledig of in het goede formaat verkregen. Dit ziet er als volgt uit:

    {"error":"invalid login request"}


## Applicatie aanmaken & beheren

> Deze methode wordt gebruikt voor de hoofdwebsite en dashboard, niet voor NetLink API

Het hele project draait om informatie visualisering, dit met LoRa data. Om deze visualisering overzichtelijk te houden hebben wij gekozen om informatie onder te verdelen in Applicaties met allerlei data verzamelpunten "Nodes".

Informatie Hiërarchie:

* Applicatie
	* Nodes
		* Data

Door deze structuur te gebruiken blijft het voor de gebruiker overzichtelijk om iets uit de data op te maken en weer te verwerken. Deze filosofie wordt door heel WebLink gebruikt dat het overzichtelijk moet zijn en makkelijk verwerkbaar moet zijn 

Functie Applicatie aanmaken & beheren:

* Applicatie aanmaken 
* Nodes beheren
* Intergratie gegevens ([NetLink API](http://192.168.1.19:8000/network/netlink.html))
* Applicatie verwijderen

### Applicatie aanmaken

#### Voorbeeld:

Request URL:  *https://[SERVER_URL]/createApp?name=[APP_NAME]&description=[APP_DESCRIPTION]&sessionKey=[USER_SESSIONKEY]*

Data Fields:
 1. SERVER_URL (Adres waarnaar de informatie wordt gestuurd)
 2. APP_NAME
 3. APP_DESCRIPTION
 4. USER_SESSIONKEY

Voor het aanmaken van een applicatie zijn twee gegevens nodig een APP_NAME en een APP_DESCRIPTION.

De APP_NAME is de naar die wordt gebruikt te identificatie van de applicatie in de lijst van gebruiker applicaties, de APP_DESCRIPTION geeft een beschrijving van de applicatie te verdere verduidelijking van het applicatie doel.

De USER_SESSIONKEY wordt gebruikt om de actie van het aanmaken van de applicatie te valideren en te koppelen aan het account van de gebruiker zodat zij hier later naar terug kunnen komen.

De server kan met de volgende antwoorden terug sturen:

 - List item

