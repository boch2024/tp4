# TP 4 : Sockets en mode non-connecté UDP

## Service de chat client/serveur avec UDP

| | |
|---|---|
| **Faculté** | Faculté des Sciences de Sfax |
| **Département** | Informatique et des Communications |
| **Matière** | Développement d'applications réparties |
| **Auditoire** | LSI 3 |
| **Année universitaire** | 2026 - 2027 |
| **Enseignante des TPs** | Zeineb DAOUD |
| **Réalisé par** | Bechir Ben Hammouda |

---

## 1. Objectif

Développer en Java une application de chat client/serveur utilisant le protocole **UDP** (`DatagramSocket` et `DatagramPacket`). Les clients envoient et reçoivent des messages texte de manière asynchrone, en temps quasi-réel, via un serveur qui écoute sur le port **1234**.

## 2. Structure du projet

```
BenHammoudaBechir_LSI3_DevAppReparties/
├── etape2/
│   ├── ServeurUDP.java             serveur : reçoit et affiche les messages
│   └── ClientUDP.java              client : envoie des messages
├── etape3/
│   ├── ServeurChat.java            serveur : enregistre les clients et diffuse
│   ├── ClientChat.java             client bidirectionnel avec thread
│   └── ClientChatSansThread.java   variante sans thread (sondage + timeout)
├── captures/
│   ├── etape2.png
│   └── etape3.png
└── README.md
```

## 3. Prérequis

- JDK 1.8 ou supérieur
- Un terminal par programme à lancer (ou un IDE : Eclipse, IntelliJ, VS Code)
- Git

## 4. Compilation et exécution

**Étape 2**

```bash
cd etape2
javac *.java
java ServeurUDP     # terminal 1, à lancer en premier
java ClientUDP      # terminal 2
```

**Étape 3**

```bash
cd etape3
javac *.java
java ServeurChat    # terminal 1, à lancer en premier
java ClientChat     # terminaux 2, 3, 4... un par client
```

Pour quitter un client, taper `exit`. Pour arrêter un serveur : `Ctrl + C`.

## 5. Étape 2 : chat unidirectionnel

**Serveur (`ServeurUDP`)**

- Crée un `DatagramSocket` non lié, puis le lie au port 1234 avec `InetSocketAddress`.
- Boucle infinie sur `receive()` (appel bloquant).
- Pour chaque datagramme : affiche l'adresse IP, le port de l'expéditeur et le contenu du message.

**Client (`ClientUDP`)**

- Demande le nom d'utilisateur.
- Lit les messages au clavier et les envoie au serveur dans un `DatagramPacket`, préfixés par le nom : `[NomUtilisateur] : message`.

**Capture d'écran 1**

![Étape 2](captures/etape2.png)

**Synthèse** : UDP est un protocole non-connecté : aucune connexion n'est établie avant l'échange. Chaque datagramme est indépendant et porte l'adresse de son expéditeur, ce qui permet au serveur de savoir qui a écrit.

## 6. Étape 3 : chat bidirectionnel et diffusion

**Serveur (`ServeurChat`)**

- Garde un `Set<SocketAddress>` contenant les clients ayant envoyé au moins un message (la "connexion" est simulée).
- À chaque message reçu :
  1. enregistre l'adresse de l'expéditeur s'il est nouveau ;
  2. retransmet le message à tous les autres clients, sauf l'expéditeur.
- Une seule boucle suffit : UDP n'a pas de connexion par client, donc pas besoin d'un thread par client.

**Client (`ClientChat`)**

- Un thread dédié à la réception affiche en temps réel les messages diffusés.
- Le thread principal gère la saisie clavier et l'envoi.
- Au démarrage, il envoie `[Nom] a rejoint le chat` pour s'enregistrer auprès du serveur (sinon il ne recevrait rien tant qu'il n'a pas écrit).

**Variante (`ClientChatSansThread`)**

- Même comportement avec une seule boucle : lecture du clavier si une ligne est disponible, puis réception avec un timeout de 100 ms (`setSoTimeout`).
- Inconvénients : attente active (plus de CPU) et léger délai d'affichage.

**Capture d'écran 2**

![Étape 3](captures/etape3.png)

**Synthèse** : l'envoi et la réception simultanés côté client nécessitent la concurrence (threads). Le serveur diffuse en parcourant la liste des adresses enregistrées. UDP ne garantit ni la livraison, ni l'ordre, ni l'absence de doublons : un chat fiable demanderait des accusés de réception. Ici, la simplicité et la rapidité du protocole suffisent.

## 7. Limites et améliorations possibles

- Un client qui quitte n'est pas retiré de la liste du serveur.
- Pas de garantie de livraison des messages (propre à UDP).
- Taille des messages limitée à 1024 octets.
- Le serveur est supposé sur `localhost` : l'adresse pourrait être passée en argument pour un usage sur plusieurs machines.

## 8. Dépôt GitHub

Lien du repository : https://github.com/boch2024/tp4
