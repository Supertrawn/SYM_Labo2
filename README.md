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

```
Aucune idée
```



### 4.3 Threads concurrents
*Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se*
*préoccupent de la préparation, de l'envoi, de la réception et du traitement des données. Quels*
*problèmes cela peut-il poser ?*

Le problème des AsyncTask rédise principalement par le fait qu'elle s'execute les unes après les autres, donc si plusieurs requêtes sont effectuées à la fois et que l'une prend plus de temps, celle-ci va bloquer toutes les autres ce qui nuit à l'expérience utilisateur.

### 4.4 Ecriture différée
*Lorsque l'on implémente l'écriture différée, il arrive que l'on ait soudainement plusieurs transmissions*
*en attente qui deviennent possibles simultanément. Comment implémenter proprement cette*
*situation (sans réalisation pratique) ? Voici deux possibilités :*

- *Effectuer une connexion par transmission différée
- *Multiplexer toutes les connexions vers un même serveur en une seule connexion de transport.*
  *Dans ce dernier cas, comment implémenter le protocole applicatif, quels avantages peut-on*
  *espérer de ce multiplexage, et surtout, comment doit-on planifier les réponses du serveur*
  *lorsque ces dernières s'avèrent nécessaires ?*
  *Comparer les deux techniques (et éventuellement d'autres que vous pourriez imaginer) et discuter des*
  *avantages et inconvénients respectifs.*

```
Trop fatigué pour finir
```

### 4.5 Transmission d’objets
*a. Quel inconvénient y a-t-il à utiliser une infrastructure de type REST/JSON n'offrant aucun*
*service de validation (DTD, XML-schéma, WSDL) par rapport à une infrastructure comme SOAP*
*offrant ces possibilités ? Est-ce qu’il y a en revanche des avantages que vous pouvez citer ?*
*b. L’utilisation d’un mécanisme comme Protocol Buffers8 est-elle compatible avec une*
*architecture basée sur HTTP ? Veuillez discuter des éventuelles avantages ou limitations par*
*rapport à un protocole basé sur JSON ou XML ?*
*c. Par rapport à l’API GraphQL mise à disposition pour ce laboratoire. Avez-vous constaté des*
*points qui pourraient être améliorés pour une utilisation mobile ? Veuillez en discuter, vous*
*pouvez élargir votre réflexion à une problématique plus large que la manipulation effectuée.*

### 4.6 Transmission compressée
*Quel gain peut-on constater en moyenne sur des fichiers texte (xml et json sont aussi du texte) en*
*utilisant de la compression du point 3.4 ? Vous comparerez vos résultats par rapport au gain théorique*
*d’une compression DEFLATE, vous enverrez aussi plusieurs tailles de contenu pour comparer.*