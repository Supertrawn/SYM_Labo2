# SYM Laboratoire 2 - Protocoles applicatifs
Thomas Léchaire, Michaèel Brouchoud et Kevin Pradervand
16.11.2018

## Introduction
Dans ce laboratoire, il est demandé de tester les techniques de programmation réparties asynchrones dont 
- La transmission asynchrone
- La transmission différée
- La transmission au format JSON
- La transmission au format XML
- La transmission avec GraphQL en JSON
- La transmission compressée

## Questions
### 4.1 Traitement des erreurs
*Les interfaces AsyncSendRequest et CommunicationEventListener utilisées au point 3.1 restent très*
*(et certainement trop) simples pour être utilisables dans une vraie application : que se passe-t-il si le*
*serveur n’est pas joignable dans l’immédiat ou s’il retourne un code HTTP d’erreur ? Veuillez proposer*
*une nouvelle version, mieux adaptée, de ces deux interfaces pour vous aider à illustrer votre réponse.*

Tout d'abord, cela limite la souplesse du code, en effet, nous nous sommes retrouvé avec beaucoup de code dupliqué, une solution serait celle proposée dans notre code, à savoir de faire en sorte que le AsyncSendRequest délègue l'envoi de la requête à un ARequestOperation. Cela à permis de factoriser le code entre les classes AsyncRequestOperation et CompressedRequestOperation.

Si on prend uniquement le principe de base énoncé au point 3.1, il est facile de constater qu'il manque un élément principal permettant de vérifier l'état de la connectivité du téléphone mobile avant d'effectuer la requête. Le plus simple serait de vérifier celui-ci avant d'effectuer l'envoi pour être ainsi sûre qu'il soit possible de le faire et dans le cas contraire d'attendre le moment opportun pour retenter un envoie après une certaine période d'attente. (avec une méthode isConnectedToNetwork() par exemple)

En ce qui concerne les erreurs HTTP, il faudrait ajouter un méchanisme pour relancer la requête N fois si une erreur est detectée avant d'afficher un message d'erreur à l'utilisateur suite aux nombreuses tentative par exemple.

### 4.2 Authentification
*Si une authentification par le serveur est requise, peut-on utiliser un protocole asynchrone ? Quelles*
*seraient les restrictions ? Peut-on utiliser une transmission différée ?*

Il y a deux restriction importante à prendre en compte aussi bien en asynchrone qu'en différée. Celles-ci sont le fait de mettre en place un mécanisme permettant de valider que le mobile soit connecté au réseau avant d'envoyer une requête et de mettre en place un mécanisme d'attente de réponse et ceci pour chaque interaction entre le client et le serveur.

Il faudrait également utilisé un mécanisme afin de conserver la session du coté serveur comme par exemple un cookie.

Les Asynktask utilisés pour faire de l'asynchrone et du différé garantissent l'ordre des envois des messages, il est donc impossible que ceux-ci arrivent dans le mauvais ordre. Il n'y a donc pas besoin de s'occuper de ce point.

Donc théoriquement, il est également possible d'utiliser une transmission différé pour l'authentification.

### 4.3 Threads concurrents
*Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se*
*préoccupent de la préparation, de l'envoi, de la réception et du traitement des données. Quels*
*problèmes cela peut-il poser ?*

Il peut y avoir des problèmes de concurrence entre les threads et celui du thread UI dans le cas ou les threads essayent de modifier des éléments de l'UI. 

Si le thread d'envoi ne reçoit de réponse, il peut potentiellement se retrouver dans un état ou il fonctionne à l'infini.

De plus tant que les threads n'auront pas fini leur execution l'Activité ayant executée les threads ne sera pas garbage-collecté.

### 4.4 Ecriture différée
*Lorsque l'on implémente l'écriture différée, il arrive que l'on ait soudainement plusieurs transmissions*
*en attente qui deviennent possibles simultanément. Comment implémenter proprement cette*
*situation (sans réalisation pratique) ? Voici deux possibilités :*

- *Effectuer une connexion par transmission différée*
- *Multiplexer toutes les connexions vers un même serveur en une seule connexion de transport.**

*Quels avantages peut-on  espérer de ce multiplexage ? Dans ce dernier cas, comment implémenter le protocole applicatif ? Comment doit-on planifier les réponses du serveur lorsque ces dernières s'avèrent nécessaires ?*

*Comparer les deux techniques (et éventuellement d'autres que vous pourriez imaginer) et discuter des avantages et inconvénients respectifs.*

- Le fait d'envoyer toutes les données en une seule fois peut poser problème dans le cas de coupure lors de l'envoi. Il faudrait à ce moment renvoyer une nouvelle fois toutes les données. Cela peut donc devenir problématique dans le cas de grand volumes. 
  - L'avantage serait d'envoyer toutes les données en une seule requêtes. Utile dans le cas de petit volume de données.
  - Le fait d'utiliser une connexion par transmission serait donc plus judicieux dans le cas ou il y aurait un grand volume de données à envoyer.
  - Il faudrait utiliser du XML ou du JSON pour séparer les données envoyée au serveur
- Etant donné qu'il s'agit d'une écriture, il faudrait avoir un système de quittance afin de valider la réception des données du serveur.
  - Le plus simple serait de faire de l'Active Poll ou le serveur profiterait pour quittancer les données reçues ou d'utiliser le service de notification et notifier l'utilisateur une fois toutes les données reçues.

### 4.5 Transmission d’objets
*a. Quel inconvénient y a-t-il à utiliser une infrastructure de type REST/JSON n'offrant aucun*
*service de validation (DTD, XML-schéma, WSDL) par rapport à une infrastructure comme SOAP*
*offrant ces possibilités ? Est-ce qu’il y a en revanche des avantages que vous pouvez citer ?*

L'inconvéniant c'est qu'il n'y a aucun contrôle du format de transmission des données contrairement au SOAP qui impose de définir une structure.

Le fait de ne pas avoir de structure à l'avantage d'avoir un poids moins elevée et d'avoir une structure moins complexe.

*b. L’utilisation d’un mécanisme comme Protocol Buffers8 est-elle compatible avec une*
*architecture basée sur HTTP ? Veuillez discuter des éventuelles avantages ou limitations par*
*rapport à un protocole basé sur JSON ou XML ?*

```
On compte sur toi Thomas ;)
```

*c. Par rapport à l’API GraphQL mise à disposition pour ce laboratoire. Avez-vous constaté des*
*points qui pourraient être améliorés pour une utilisation mobile ? Veuillez en discuter, vous*
*pouvez élargir votre réflexion à une problématique plus large que la manipulation effectuée.*

```
On compte sur toi Thomas ;)
```

### 4.6 Transmission compressée
*Quel gain peut-on constater en moyenne sur des fichiers texte (xml et json sont aussi du texte) en*
*utilisant de la compression du point 3.4 ? Vous comparerez vos résultats par rapport au gain théorique*
*d’une compression DEFLATE, vous enverrez aussi plusieurs tailles de contenu pour comparer.*
```
On compte sur toi Thomas ;)
```