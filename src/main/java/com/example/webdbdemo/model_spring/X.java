package com.example.webdbdemo.model_spring;

public class X {
}


class VersenySulycsoport{
     String verseny_id;
     String sulcsoport_id;
     String korcsoport_id;
     String lebonyolitási forma;

     void sorsolas(){
         .....
     }
}

class Nevezes{
    String verseny_id;
    String sulcsoport_id;
    String korcsoport_id;
    String versenyzo_id;
}

class Merkozes{
    String Id;
    String Idoponotja;
    String Tatatmi;
    String tipus // KATA vagy KUMITE
    String csoport? kiesési szint?
}

class Versenyzo{
    String Id;
    String nev;
    String szul_datum;
}

class Kumite_versenyzo{
    String merkozes_id;  // Relációs
    Versenyzo versenyzo; // JAVA
    String versenyzo_id;
    String pontszam;
    .....
}

class Kata_versenyzo{
    String merkozes_id;  // Relációs
    Versenyzo versenyzo; // JAVA
    String versenyzo_id;
    String pontszam;
    .....
}

