A projektben MySQL adatbázis használatára és web alkalmazásfelület készítésére találhatók példák.

Szükséges szoftvertelepítések
=============================

MYSQL adatbáziskezelő
---------------------

A telepítőcsomag innen töltehető le: https://dev.mysql.com/downloads/installer/,
az oldalon a teljes telepítőcsomagot kell választani: https://dev.mysql.com/downloads/file/?id=505213
A telepítési paraméterek:
    Custom install, telepítendő komponensek
        Server
        Workbench
        Shell
        Router
        Connector/J
        Documentation
        Samples
Configuráció: mindenhol default maradhat
   TCP/IP 3306
   Root account password:XDR5432ws (most még nem foglalkozunk jogosultság kezeléssel, mindenre a rootot használjuk)
   Windows Service Name:MySQL80


Az adatbázisszerveren a legtöbb tevékenység elvégezhető a MySQL Workbench szoftverrel
    Connectálás a serverhez: Database/Connect to database
    Új modelt a File/New model-el lehet létehozni
    A physical Schemas lapon a mydb-t át kell nevezni olyan séma névra, ahova tenni akarjuk a táblákat: webdbdemo
    Add Diagramm-al lehet digrammot létrehozni, azon vizuálisan lehet a táblákat kapcoslatokat felvenni
    A Database/Forward Engineer-el lehet az adatbázisban kigenerálni a megtervezett táblákat

Tomcat alkalmazásszerver
========================
Letöltési link: https://downloads.apache.org/tomcat/tomcat-9/v9.0.50/bin/apache-tomcat-9.0.50.exe
A jogosultságbeállításokat konfigurálni kell új sorok hozzáadásával a
C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0\conf\tomcat-users.xml fájlban:
    <role rolename="manager-gui"/>
    <user username="tomcat" password="s3cret" roles="manager-gui"/>
Manager GUI: localhost:8080

IntelliJ IDEA Ultimate Edition
==============================

Csak ez a verzió támogatja a Java EE-t
Letöltési link: https://www.jetbrains.com/idea/download/download-thanks.html

Új project létrehozása
================================
File/New project
    Új Java EE project létrehozása
    Project template:Web Application
    Application Server: New, tomcat Server, Home könyvtár:C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0
View/Tool windows/Database
    +-al hozzunk létre egy mysql data source-t, a database nevéhez írjuk a webdbdemo-t, így IDEA-n belül is látjuk az
    adatbázis tábláit, amit az IDEA ellenőrzésre is tud használni
pom.xml fájlba fel kell venni a függőságeket
    Code/Generate/Dependencies/Add
        mysql-connector-java
    jobb klikk Maven/Reload Project

Az alkalmazás több absztrakciós rétegből áll:
Fapados (JDBC) módszer:
model_jdbc.User osztály: A tábla egy sorának JAVA implementációja, a tábla oszlopai attributumként jelennek meg,
    setter, getter metódusokkal lehet kezelni őket. Az osztály ki lehet egészíteni egyéb metódussal is.
model_jdbc.UserRepository: Az adatbázistáblaból lekérdezést, adatbázistáblán történő módosításokat végzi el,
    JDBC-m keresztül SQL parancsokat ad ki.
    JDBC példák: https://www.tutorialspoint.com/jdbc/jdbc-sample-code.htm
    Alt+Enter Create Test-el tesztosztált lehet készíteni (UserRepositoryTest)

Spring módszer:
További függőségek beállítása: pom.xml
    Code/Generate/Dependencies/Add:
        spring-boot-starter-data-jpa
        spring-boot-starter-test
    jobb klikk Maven/Reload Project
A lib/javax.persistence.jar és lib/javax.servlet.jsp.jar fájlt törölni kell.

model_spring.User osztály: Hasonló az előző User osztályhoz, csak annotációkkal van kiegészítve,
    melyek az attributumok, osztályok, oszlopokhoz, táblákhoz rendeléaét definiálják.
model_spring.UserRepostorty interface: Ez csak egy interface, az implementáció nem nekünk kell megírni. Teljesen
    elvégzi helyettüönk az adatbázisműveleteket, és csomó plussz szolgáltatása van.
    A teszt osztályban speciális annotációkkal de3finiálhatjuk, hogy a sping környezet teszteléshez szükséges komponensei
    inicializálódjanak (UserRepositoryTest2)
    A TestApplication osztály is a sping incializáláshoz kell.
application.properties: Az adatbázis kapcsolódási paramétereket tartalmazza
    A spring képes arra, hogy az adatbázisszerkezetet az osztályokhoz igazítsa
Az alkalmazás még nem futóképes, csak a teszt osztályok próbálhatók ki.

Felület
=======
Vaadin konfigurálása:
0) A VAadin start felület legenerál egy komplett projektet: https://start.vaadin.com/app/?ads_cmpid=700075176&ads_adid=66379531830&ads_matchtype=b&ads_network=g&ads_creative=314259357538&utm_term=vaadin+download&ads_targetid=kwd-377068347267&utm_source=adwords&utm_medium=ppc&ttv=2&gclid=CjwKCAjw9uKIBhA8EiwAYPUS3GgJ9oVl2hOePgT_oQS-gHobZC5zs6WZI6hEtvmaxMz78S_o-pxZTBoC5MEQAvD_BwE
Ebből a starter projektből másoltam át a legszükségesebb részeket.
1) POM.XML-be be kell tenni a vaadin specifikus részeket.
Utánna Load Maven Changes (ctrl+shift+O) kell, ha ez után is lesz hiba a pom.xml-ben, akkor: File/Invalidate Cache segít.
2) package.json: változatlanul a starter kitből
3) application.properties-ben a vaadin specifikus paramétereket be kell állítani
4) Application.java: az alkalmazás belépési pontja
5) UserView class: egy egyszerű grid, ami megjeleníti egy tábla tartalmát
- FONTOS: Először fut a contructor, utánna az autowired, utána az init, tehát a contstructorban még nem lehet hesználani a repo-kat.

Futtatni az Application class melletti/vagy a felső sorban lévő zöld nyilacskával lehet

További adatműveletekre példák: https://www.baeldung.com/spring-boot-vaadin

Néha beragad az alkalmazásszerver, kilövése:
netstat -ano | findstr 8080
taskkill  /F  /PID  <Process Id>


Security
================
Az összefoglaló a https://vaadin.com/learn/tutorials/securing-your-app-with-spring-security leglényegesebb részeinek kivonata.
A linken részletesebb magyarázatok it találhatók.

Az alkalmazás minden view-ja a saját url-jén keresztül önállóan elérhető, ezért a webalkalmazások jogosultságkezelése bonyolultabb,
mint a vastag klienses alkalmazásoké, mert minden request esetén kell jogosultságot ellenőrizni.

A springnek van ehhez is támogatása:
pom.xml:
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
        </dependency>


A security.SecurityConfiguration class-ban írunk le minden lényeges információt a jogosultságkezeléshez, ronda kód, de szerencsére nem nagyon kell módosítani:
1) a configure method-okkal definiáljuk, hogy mit lehet szabadon elérni, mihez kell bejelentkezés. Itt definiáljuk, hogy melyik a login képernyőnk.
2) A UserDettailsService a user lista kezelőt adja vissza. A példában egy egyszerű memóriában lévő, kódolatlan jelszóval rendelkező userlista van.
Rendees alkalmazásban ilyen nem csinálunk,
a jelszót nem tároljuk sem adatbázisban, sem kódban olvasható formában, hanem encodoljuk. A login
folyamat során a begépelt jelszó encodolt formáját hasonlítjuk az adatbázisban tárolt encodolt jelszóval.
További informnációk:
https://docs.spring.io/spring-security/site/docs/5.4.6/api/org/springframework/security/core/userdetails/User.html#withDefaultPasswordEncoder
Adatbázisban tárolt userek kezelése: https://www.baeldung.com/spring-security-authentication-with-a-database
3) isFrameworkInternalRequest: A framework belső hívásait azonosítja, csak azért kell, hoyg ezeket ne logoljuk.
4) isUserLoggedIn: eldönti, hogy valaki be van-e jelentkezve.
5) isAccessGranted: A view-knál megadott Secured annotációk alapján eldönti, hogy a bejelentkezett user jogosult-e megnézni a view-t vagy nem.
Pl. Secured("ROLE_Admin") annotációval rendelkező view-t csak az admin felhasználók érhetik el. A
 Secured annotáció nélküli view-kat bárki láthatja. Ezt csak a példa kedvéért hagytam bent, biztonságosabb gyakorlatt, hogyha
annotáció nélküli view-kat senki sem lát. (Methogy a programozó ugyis elfelejti kitenni az anntációt).

LoginView.class: A bejelentkezési képernyő. Van kész kéeprnyő is, amit néhány sorból lehet implementálni, azért ezt választottam, mert ezt
kedvedre csinosíthatod.

ConfigureUIServiceInitListener.class: Ez csipi el a kéréseket, és mielőtt továbbítani a kérést az adott view-nak, elvégzi a jogosultság ellenőrzést.
Ezen sem kell módosítani.

A példában a /about bárki számára elérhető, a /clock / user joggal, a /user admin joggal érhető csak el.
