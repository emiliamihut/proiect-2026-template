# Proiect POO 2026 - Management Centre de Date

#### Mihut Maria-Emilia 321CA

## Ce face aplicatia

Am facut un sistem pentru gestionarea centrelor de date care imi permite sa adaug servere, sa creez grupuri de monitorizare si sa generez alerte. Aplicatia citeste comenzi din fisiere CSV (separate cu |) si genereaza output-uri care arata ce s-a intamplat dupa fiecare comanda.

## Cum am organizat codul

Am impartit tot pe pachete ca sa fie mai usor de gasit lucrurile:
- `commands/` - toate comenzile (adauga server, cauta membru etc)
- `models/` - clasele pentru entitati (Server, User, Alert)
- `database/` - baza de date (am facut-o singleton)
- `exceptions/` - erorile custom
- `observers/` - pentru pattern-ul Observer
- `enums/` - tipurile (AlertType, Severity etc)
- `factories/` - factory pentru useri

## Design Patterns

### 1. Singleton pentru Database

Am vrut sa am o singura baza de date in toata aplicatia. Nu avea sens sa am mai multe baze de date care sa nu stie una de alta, asa ca am facut Database ca singleton. Practic am facut constructorul privat si oricine vrea instanta apeleaza `Database.getInstance()`. Prima data cand e apelat se creeaza, apoi intotdeauna primeste aceeasi instanta.

```java
public class Database {
    private static Database instance;
    
    private Database() {}
    
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}
```

E util pentru ca toate comenzile lucreaza cu aceleasi date si am un singur loc unde tin tot.

### 2. Builder pentru Server

Clasa Server are multe campuri (IP, locatie, utilizatori, status etc) si constructorul devenea prea lung si greu de citit. Am facut un Builder ca sa pot construi serverul pas cu pas:

```java
Server server = new Server.ServerBuilder()
    .ipAddress("192.168.1.1")
    .location(location)
    .users(userList)
    .build();
```

### 3. Factory pentru User

Am 3 tipuri de utilizatori: User simplu, Operator (care are departament) si Admin (care mai are si clearance level). In loc sa creez manual fiecare tip, am facut un UserFactory care primeste datele si decide singur ce tip de user sa creeze:

```java
User user = UserFactory.createUser(name, role, email, department, clearance);
```

Factory-ul se uita la rol si decide:
- Daca e "Admin" - creeaza Admin cu toate campurile
- Daca e "Operator" - creeaza Operator cu departament
- Altfel - creeaza User simplu

### 4. Observer pentru alerte

Cand un server genereaza o alerta, grupul de monitorizare asociat trebuie notificat automat. Am folosit Observer: Server-ul e Subject (cel care notifica) si ResourceGroup e Observer (cel care primeste notificari).

Cand vine o alerta:
1. Gasesc serverul cu IP-ul respectiv
2. Gasesc grupul de monitorizare
3. Server-ul ii spune grupului: "am o alerta noua"
4. Grupul primeste alerta si o scrie intr-un fisier

```java
server.addObserver(group);
server.notifyObservers(event);
```

Avantajul e ca serverul nu stie care grupuri il asculta si pot adauga/sterge observatori fara sa modific serverul.

### Comenzile

Toate comenzile functioneaza la fel:
1. Impart linia dupa | ca sa extrag datele
2. Validez datele (IP-ul trebuie sa existe, numele nu poate fi gol etc)
3. Caut in baza de date ce imi trebuie (serverul, grupul etc)
4. Fac operatia (adaug, sterg, caut)
5. Scriu rezultatul in fisierul de output

De exemplu pentru FindMember:
```java
// impart linia
String[] tokens = line.split("\\|");
String ip = tokens[1].trim();
String name = tokens[2].trim();

// caut grupul
ResourceGroup group = null;
for (ResourceGroup g : db.getResourceGroups()) {
    if (g.getIpAddress().equals(ip)) {
        group = g;
        break;
    }
}

// caut membrul in grup
boolean found = false;
for (User u : group.getMembers()) {
    if (u.getName().equals(name)) {
        found = true;
        break;
    }
}
```

### Erori

- `MissingIpAddressException` - cand lipseste IP-ul
- `UserException` - cand nume sau rol lipsesc
- `LocationException` - cand tara lipseste

Toate erorile sunt prinse si scrise in output cu numarul liniei:
```
ADD SERVER: MissingIpAddressException: Server IP Address was not provided. ## line no: 2
```

### Main-ul

1. Citeste argumentele (tip si cai fisiere)
2. Daca e "listeners" proceseaza 3 fisiere in ordine (servers, groups, apoi listeners)
3. Pentru fiecare fisier:
   - Deschide input si output
   - Sare peste linia header (prima linie cu titlurile)
   - Pentru fiecare linie determina ce comanda e si o executa
   - Scrie rezultatul

