								

# WebLink Reference

WebLink wordt gebruikt om informatie uit te kunnen wisselen tussen onze servers.
Een aantal voorbeelden zijn:
 - Login
 - Registeren
 - Applicatie aanmaken
 - Applicatie beheren (wijzigingen aanbrengen enz.)
 - Nodes toevoegen en onderhouden
 - Gateways toevoegen en onderhouden
 
In dit document zal de informatie uitwisseling tussen onze servers uitvoerig worden beschreven.


## Login

Bij login is het voor ons van belang dat wij weten wie er toegang heeft tot welke gegevens hiervoor maken wij gebruikt van sessie cookies die bijhouden wie er op welk moment is ingelogd. voor veiligheidsreden, maken wij niet bekend hoe wij deze dat verwerken. Echter word hier beschreven hoe de informatie flow van dit proces werkt. 

![DataFlow](https://drive.google.com/uc?authuser=0&id=1uH9OnSnUf2Hpal254VgFSd8FlSfuiM2t&export=download)

### Voorbeeld: Website --> Server (JSON)
Request URL:  https://[SERVER_IP]/login?email=[USER_EMAIL]&password=[USER_PASSWORD]

Data Fields:

 1. SERVER_URL
 2. USER_EMAIL
 3. USER_PASSWORD

Deze data fields zijn nodig om precies te kunnen weten wij er in logt en of zij toestemming hebben om in te mogen inloggen. vandaar natuurlijk het email adres en de gebruikersnaam.
De server url wordt gebruikt om deze informatie naar de juiste server toe te sturen. Hiermee wordt gevraagd of alle gegevens kloppen en of de gebruiker een sessie mag aanmaken om in het systeem te komen. 

Uit deze conversatie zijn twee uitkomsten mogelijk:

 1. Ongeldige gebruikersnaam of wachtwoord.
 2. Geldige gebruikersnaam en wachtwoord combinatie
 3. Invalide login request (Ongeldige login aanvraag)

De eerste uitkomst is duidelijk als het gebruikersnaam of wachtwoord combinatie incorrect is wordt dit bericht terug gestuurd. Dit ziet er als volgt uit:

    {"error":"Incorrect username or password"}

Met de tweede uitkomst heeft de server alle informatie gevalideerd en een sessie aan de gebruiker gekoppeld. Dit ziet er als volgt uit:

    {"sessionKey":"[UNIQUE ID]","sessionTimeout":"86400"}

Deze sessie maakt mogelijk om acties in het systeem te kunnen uitvoeren. als





