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
- `factories/` - factory pentru useri
- tipurile (AlertType, Severity etc)

## Entitatile principale

**Server** - reprezinta un server cu:
- Campuri obligatorii: ipAddress, location, users
- Campuri optionale: hostname, status (ServerStatus), cpuCores, ramGb, storageGb

**Location** - locatia unui server cu:
- Camp obligatoriu: country
- Campuri optionale: city, address, latitude, longitude

**User** - utilizator basic cu: name, role, email

**Operator** - extinde User si adauga: department

**Admin** - extinde Operator si adauga: clearanceLevel (int)

**ResourceGroup** - grup de monitorizare cu: ipAddress si o lista de membri (User)

**Alert** - alerta de sistem cu: type, severity, message, ipAddress

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

### 2. Builder pentru Server si Location

Am doua clase cu multe campuri, dintre care unele sunt optionale: Server si Location.

**Server** are campuri obligatorii (IP, locatie, utilizatori) si campuri optionale (hostname, status, cpuCores, ramGb, storageGb). 

**Location** are un camp obligatoriu (country) si campuri optionale (city, address, latitude, longitude).

Daca as face constructori cu toti parametrii ar fi foarte lung si greu de folosit. Cu Builder pot construi obiectele pas cu pas si pun doar ce am nevoie:

```java
// pentru server
Server server = new Server.ServerBuilder()
    .ipAddress("192.168.1.1")
    .location(location)
    .users(userList)
    .hostname("server1")
    .status(ServerStatus.UP)
    .cpuCores(68)
    .ramGb(1240)
    .storageGb(880000)
    .build();

// pentru location
Location location = new Location.LocationBuilder()
    .country("Romania")
    .city("Bucharest")
    .address("Strada Polizu")
    .build();
```

Fiecare metoda (ipAddress, location, hostname etc) seteaza un camp si returneaza builder-ul inapoi, asa pot chain-ui apelurile. La final apelez build() care face efectiv obiectul. Daca un camp e optional si nu il setez, ramane null.

Avantajul e ca pot omite campurile optionale fara probleme si codul ramane lizibil chiar daca am multi parametri.

### 3. Factory pentru User

Am 3 tipuri de utilizatori: User simplu, Operator (care are departament) si Admin (care mai are si clearance level). In loc sa creez manual fiecare tip, am facut un UserFactory care primeste datele si decide singur ce tip de user sa creeze:

```java
User user = UserFactory.createUser(name, role, email, department, clearanceLevel);
```

Factory-ul se uita la rol si decide ce sa creeze. De exemplu:
- Daca role e "Admin" face `new Admin(...)` cu toate campurile
- Daca role e "Operator" face `new Operator(...)` cu departament
- Altfel face `new User(...)` simplu

Asa nu trebuie sa verific in fiecare comanda ce tip de user sa creez.

### 4. Observer pentru alerte

Cand un server genereaza o alerta, grupul de monitorizare asociat trebuie notificat automat. Am folosit Observer: serverul anunta grupul ca are o alerta noua.

Cum functioneaza:
1. Gasesc serverul cu IP-ul din alerta
2. Gasesc grupul de monitorizare cu acelasi IP
3. Adaug grupul ca observer al serverului: `server.addObserver(group)`
4. Cand vine alerta, serverul notifica grupul: `server.notifyObservers(alert)`
5. Grupul primeste alerta si o scrie intr-un fisier

```java
server.addObserver(group);
server.notifyObservers(event);
```

Avantajul e ca serverul nu stie cine il asculta. Pot adauga sau sterge grupuri de monitorizare fara sa modific codul serverului.

### 5. Command pentru Comenzi

Fiecare comanda (adaugare server, cautare membru etc) e o clasa care implementeaza interfata Command. Astfel toate comenzile au aceeasi metoda `execute()` dar fac lucruri diferite:

Interfata Command:
```java
public interface Command {
    void execute(String[] tokens, int lineNumber, BufferedWriter writer);
}
```

Fiecare comanda implementeaza aceasta interfata:
```java
public class AddMember implements Command {
    public void execute(String[] tokens, int lineNumber, BufferedWriter writer) {
        // extrage datele din tokens
        String ip = tokens[1].trim();
        String name = tokens[2].trim();
        
        // cauta grupul si adauga membrul
        // scrie rezultatul in writer
    }
}
```

## Beneficii ale Design Patterns

1. **Singleton pentru Database**: 
   - Asigura ca exista o singura instanta a bazei de date in toata aplicatia.
   - Simplifica gestionarea datelor si evita conflictele intre multiple instante.

2. **Builder pentru Server si Location**:
   - Usureaza crearea obiectelor complexe cu multi parametri, atat obligatorii cat si optionali.
   - Imbunatateste lizibilitatea codului si reduce erorile cauzate de constructori lungi.
   - Permite omiterea campurilor optionale fara probleme (hostname, status, cpuCores, ramGb, storageGb pentru Server si city, address, latitude, longitude pentru Location).
   - Codul ramane curat si usor de inteles chiar daca am multe campuri.

3. **Factory pentru User**:
   - Automatizeaza crearea diferitelor tipuri de utilizatori.
   - Permite extinderea usoara pentru noi tipuri de utilizatori fara a modifica codul existent.

4. **Observer pentru Alerte**:
   - Decupleaza serverele de grupurile de monitorizare.
   - Permite adaugarea sau eliminarea observatorilor fara a modifica logica serverului.

5. **Command pentru Comenzi**:
   - Centralizeaza logica fiecarei comenzi intr-o clasa separata.
   - Usureaza adaugarea de noi comenzi fara a modifica Main-ul.
   - Imbunatateste organizarea si mentenabilitatea codului.

## Cum functioneaza comenzile

Toate comenzile functioneaza la fel:
1. Primesc linia impartita dupa | (tokens) si numarul liniei
2. Extrag datele din tokens (IP, nume, rol etc)
3. Validez datele - daca lipseste ceva arunc exceptie
4. Caut in baza de date ce imi trebuie (server, grup etc)
5. Fac operatia (adaug, sterg, caut)
6. Scriu rezultatul in fisier

De exemplu FindMember:
```java
// extrag datele
String ip = tokens[1].trim();
String name = tokens[2].trim();
String role = tokens[3].trim();

// caut grupul
ResourceGroup group = null;
for (ResourceGroup g : db.getResourceGroups()) {
    if (g.getIpAddress().equals(ip)) {
        group = g;
        break;
    }
}

// caut membrul
boolean found = false;
for (User u : group.getMembers()) {
    if (u.getName().equals(name) && u.getRole().equalsIgnoreCase(role)) {
        found = true;
        break;
    }
}
```

## Cum tratez erorile

Am 3 exceptii custom:
- `MissingIpAddressException` - cand lipseste IP-ul
- `UserException` - cand nume sau rol lipsesc
- `LocationException` - cand tara lipseste

Toate comenzile au try-catch. Daca apare o eroare o prind si scriu in output cu numarul liniei:
```
ADD SERVER: MissingIpAddressException: Server IP Address was not provided. ## line no: 2
```

## Cum functioneaza Main-ul

1. Citeste argumentele (tip fisier si cai)
2. Daca e "LISTENERS" proceseaza 3 fisiere (servers, groups, listeners)
3. Pentru fiecare tip de fisier (SERVERS, GROUPS, LISTENERS):
   - Deschide fisierul de input si pe cel de output
   - Sare peste prima linie (header-ul cu titlurile coloanelor)
   - Pentru fiecare linie:
     - Imparte linia dupa | ca sa fac tokens
     - Prima valoare e tipul comenzii (ADD SERVER, FIND MEMBER etc)
     - Determin ce comanda e si apelez execute()
   - Scrie rezultatul in fisierul .out

De exemplu daca prima valoare e "ADD MEMBER" creez un obiect AddMember si apelez execute cu tokens-urile.
