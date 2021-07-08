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
A lib/javax.persistence.jar fájlt törölni kell.

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

JSP példák: https://www3.ntu.edu.sg/home/ehchua/programming/java/JSPByExample.html#zz-6.



