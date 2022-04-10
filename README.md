# Zadanie 3 - Knihy
B-OOP 2022

Vašou úlohou je naprogramovať aplikačný server v jazyku Java s pomocou frameworku Spring.

Aplikačný server umožňuje správu kníh, ich autorov a ich požičiavania (napr. z knižnice).
Webové rozhranie (API), ako aj objekty, ktoré sa používajú na komunikáciu s vonkajším svetom sú definované v zadaní a musi byť použité na komunikáciu cez webové služby. Mimo webového rozhrania môžete použiť ďalšie objekty podľa vášho návrhu, ak to uznáte za vhodné.

Špecifikáciu webového rozhrania, ktoré má aplikácia poskytovať nájdete tu: --------------------------------------------------------------

Pokiaľ je v API uvedené, že sa má vrátiť kód 404 a v popise nie je povedané kedy, je ho potrebné vrátiť v prípade, že poskytnuté ID v systéme neexistuje.

### Popis systému

Podrobný popis jednotlivých operácii je uvedený v špecifikácii API.

Systém umožňuje pridávanie a odoberanie kníh z knižnice. Dalej umožnuje úpravu existujúcich knih (aj zmenu autora, autor môže byť len jeden), ako aj navýšenie počtu stavu kníh v knižnici.

Systém umožňuje vytváranie a vymazávanie listov s požičanými knihami. Do listov je možné pridávať a odoberať knihy (vždy v jednom liste môže byt len jedna kniha). Systém ďalej umožnuje požičanie ešte nevypožičaných listov. Do už vypožičaných listov kníh nie je možné prídávať knihy.

## Automatizované testy

Projekt obsahuje automatizované testy. Testy sa **NESPÚŠŤAJÚ** automaticky pri git push. Pokiaľ si chcete overiť na koľko vaša implementácia spĺňa testy musíte si ich spustiť sami. Testy sa dajú spustiť 2 spôsobmi:
* cez Maven, spustením _test_ lifecycle-u (bočné menu > maven > _projekt_ > lifecycle > test)
* spustením testov rovno z triedy ktorá ich obsahuje (nachádza sa v src/test/sk/.../oop/assignment3/Assignment3ApplicationTests.java)

## Hodnotenie

Zadanie je hodnotené 15 bodmi. **Odovzdaný program musí byť skompilovateľný, inak je
hodnotený 0 bodmi**. Skompilovateľnosť zadania kontroluje aj github pipeline. Hlavný dôraz v hodnotení sa kladie na objektový prístup a princípy OOP,
okrem iného:

* vhodné pomenovanie tried, metód a premenných v jednotnom jazyku (názvy tried s veľkým počiatočným písmenom, názvy metód s malým),
* vhodné použitie modifikátorov prístupu (public, private, poprípade protected) na obmedzenie prístupu k metódam a atribútom,
* využitie dedenia a polymorfizmu,
* použitie výnimiek na ošetrenie nedovoleného správania (nehádzať a nezachytávať všeobecnú triedu Exception),
* nepoužívajte nested classy,
* vo vašich triedach nevytvárajte statické metódy ani nekonštantné statické premenné (v zadaní nie sú potrebné),
* hlavná trieda (main) môže obsahovať len kód potrebný na inicializáciu aplikácie pomocou Spring frameworku,
* vo svojom riešení môžete použiť knižnicu lombok a jej anotácie. Potrebná dependencia je už pridaná v _pom.xml_ súbore.

Niektoré z vecí, za ktoré sme minulý rok strhli po 0,5 - 1 bode:
* Pouzivanie service cez Autowire
* Rovnaka trieda pre response aj pre DB
* Unused kod
* Nepouzivanie Interface pre service
* Nepouzitie repozitarov
* Jeden controller
* Zle konvencie a pomenovania

Prípadne sú pri nedostatočnej implementácií struhnuté body za OOP za nedostatočnú implementáciu.

**Pri zadaní sa kontroluje originalita zadaní, a všetky zadania so zhodou vyššou ako 80% sú hodnotené 0 bodmi.**

## Odovzdávanie
Zadanie si naklonujte z repozitára zadania výhradne pomocou poskytnutej linky cez GitHub Classroom (pokiaľ si vygenerujete vlastný repozitár pomocou tlačidla "Use this template" z template repozitára, my váš repozitár neuvidíme a nebudeme ho hodnotiť!). Svoje vypracovanie nahrajte do pre vás vytvoreného repozitára pre toto zadanie pomocou programu Git (git commit + git push).
Skontrolujte si, či sa váš repozitár nachádza pod skupinov **Interes-Group**, inak nemáme prístup ku vášmu repozitáru a zadanie sa nepovažuje za odovzdané. Vypracovanie môžete "pusho-vať" priebežne. Hodnotiť sa bude iba _master_ branch. Zadanie je nutné vypracovať do **6.5.2022 23:00**.

V projekte upravujte iba súbory v priečinku _src/main/java_ a jeho podpriečinkoch. Ostatné súbory je upravovať zakázané (predovšetkým súbory _pom.xml_, súbory obsahujúce github pipeline-y a súbory obsahujúce automatizované testy, pri zmene týchto súborov môžte byť ohodnotený 0 bodmi).

Vo svojom github účte si nastavte svoje meno (settings > profile > name), aby bolo možné priradiť riešenie ku študentovy. **Pokiaľ nebude možné spárovať študenta s riešením je zadanie hodnotené 0 bodmi!**


# Assignment 3 - Books

B-OOP 2021

Your task is to create an application server using Java with the Spring framework.

Application server  enables management of the books, authors and lending list of books (for example like in library). Web Interface(API), and also the objects that are used to communicate with the outside world are defined in the specification and must be used to communicate through web services. Outside the web interface you can use any other objects according to your design, if you deem it appropriate.

Web interface specification that the application has to provide can be found here: -----------------------------------------------------

If the specification states that a 404 code should be returned, and the description does not say when, it is necessary to return if given ID does not exist in the system.

### System description

A detailed description of each operation is given in the API specification.

---------------------------------------------------------------------------------

## Automatic tests
Project contains automatic tests. Tests **DO NOT** run automatically on git push. If you want to verify how many of your implementations pass the tests, you must run them yourself. Tests can be run in 2 ways:

* using Maven, by launching lifecycle (side menu> maven> project> lifecycle> test)
* by running tests directly from the class that contains them (located in rc/test/sk/.../oop/assignment3/Assignment3ApplicationTests.java))

## Valuation

You can get 15 points for this assignment. **The program must be able to compile, otherwise 0 points are given for the assigment**.

The github pipeline checks whether the program can be compiled. The main focus during grading is put on object-oriented approach and OOP principles used by the solution.

Including, but not limited to:

* appropriate naming of classes, methods and variables in a single language (class names starting with a capital letter, method names starting with a lowercase letter),
* appropriate use of access modifiers (public, private, or protected) when restricting access to class methods and attributes,
* the use of inheritance and polymorphism,
* usage of exceptions when handling undesired behavior (do not catch or throw the instances of the generic Exception class),
* don't use nested classes,
* don't use static methods, or non-constant static variables (you don't need them to complete the assignment),
* don't put any logic into the main method and its class. The main method should only be used to initialize application using the Spring framework,
* you can use the lombok library and its annotations in your solution. The neccessary dependency is already present in the _pom.xml_ file.

Niektoré z vecí, za ktoré sme minulý rok strhli po 0,5 - 1 bode:

* Pouzivanie service cez Autowire
* Rovnaka trieda pre response aj pre DB
* Unused kod
* Nepouzivanie Interface pre service
* Nepouzitie repozitarov
* Jeden controller
* Zle konvencie a pomenovania

Prípadne sú pri nedostatočnej implementácií struhnuté body za OOP za nedostatočnú implementáciu.

**Pri zadaní sa kontroluje originalita zadaní, a všetky zadania so zhodou vyššou ako 80% sú hodnotené 0 bodmi.**

## Handing in the assigment

Clone the assignment from the repository created from this template by the provided link trough GitHub Classroom (if you create your own repository with the "use this template" button, we won't be able to see your repository and we won't be able to grade it!). Upload your solutions to your repository using the Git version control system (git commit + git push).

Make sure, that your repository was created under the **Interes-Group** group, otherwise we won't be able to access your repository, and the assignment will not be graded.

You can push commits to the repository while you work - you don't have to push everything at once. Only the code in the _master_ branch will be graded. You have until **6.5.2022 23:00** to complete the assignment.

Only edit files in the _src/main_ folder or its sub-folders. You mustn't change any other files in the repository (especially the _pom.xml_ file, and the github pipeline files).

You have to have your name set in your github account (settings > profile > name), so that we can match students with their assignments. **If we are unable to match a student with their assignment, the student will receive 0 points for the assignment!**
