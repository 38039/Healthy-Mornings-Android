# HEALTHY MORNINGS - APLIKACJA MOBILNA
**Programowanie Systemów Mobilnych II - Projekt zaliczeniowy**
### ℹ️ Zawartość README stanowi dokumentację projektu


## SPIS TREŚCI
- [Osoby zaangażowane](#autorzy)
- [Wymagania projektowe](#wymagania)
- [Terminy i wymagania](#terminy)
- [Opis działania aplikacji](#opis)
- [Architektura projektu](#architektura)
- [Plan realizacji projektu](#plan)
- [Dane techniczne](#dane)
- [Wykorzystywane technologie](#technologie)
- [Struktura aplikacji](#struktura)


<a id="autorzy"></a>
## OSOBY ZAANGAŻOWANE
| Pełne Imię        | Indeks | Semestr               |
|-------------------|--------|-----------------------|
| Miłosz Fedorczak  | 38039  | Semestr Letni 2024/25 |
| Szymon Pietruszka | 37719  | Semestr Letni 2024/25 |


<a id="wymagania"></a>
## WYMAGANIA PROJEKTOWE
1. *Projekt może zostać zrealizowany w maksymalnie 3 osobowych grupach*
    * [x] Nad projektem pracuje **grupa dwuosobowa**

2. *Technologia Android (Java lub Kotlin) lub iOS (Swift)*
    * [x] Projekt jest bazowany na technologii **Android** (w języku **Java**)

3. *Użycie systemu kontroli wersji GIT*
    * [x] Projekt zawiera system kontroli wersji **GIT** (oraz posiada zdalne repozytorium na platformie **GITHUB**)


<a id="terminy"></a>
## TERMINY I WYMAGANIA
1. [x] 07.03.2025 - **Zatwierdzenie wyboru tematu projektu oraz jego zakresu**
   * Do tego terminu grupy studentów powinni zdecydować się na konkretny temat 
     - [x] Wybór musi być uzgodniony i zaakceptowany przez prowadzącego 
     - [x] Przy zgłoszeniu należy podać 
       * [x] [Imiona i nazwiska](#autorzy) 
       * [x] [Numery albumów](#autorzy)
       * [x] [Temat pracy](#wymagania)
     - [x] [Dodatkowo przygotować krótki opis aplikacji](#opis)
---
2. [x] 04.04.2025 - **Przedstawienie planu realizacji (20 % oceny)**
   * W ramach tego etapu zespół projektowy ma za zadanie przedstawić:
     - [x] [Plan realizacji projektu](#plan)
     - [x] [Listę wykorzystanych technologii](#dane) 
     - [x] [Listę wykorzystywanych bibliotek](#technologie)
     - [x] [Przyjętą architekturę projektu](#architektura)
     - [x] [Diagram klas, Diagram przypadków użycia, Schemat bazy danych (relacji + encji)](#diagramy)
     - [x] [Główne interfejsy aplikacji (szkic ekranów) wraz z opisem, w tym celu wykorzystać Figma lub App Moqups](#interfejs)
     - [x] [Podział projektu na mniejsze podzadania, w tym celu wykorzystać Jira, Trello](#trello)
---
3. [x] 09.05.2025 - **Implementacja projektu (30 % oceny)**
   * Do tego terminu należy zaprezentować postęp projektu w oparciu o utworzoną dokumentacje. Wymagane jest oddanie:
      - [x] Filmu z funkcjonowania aplikacji
      - [x] Archiwum projektu w formacie .zip
      - [x] Pliku PDF zawierającego opis + screeny funkcjonalności z obecnej wersji
---
4. [ ] 13.06.2025 - **Dostarczenie kompletnego projektu (50 % oceny)**
* Na tym etapie zespół ma za zadanie 
  - [ ] Dostarczyć kompletną aplikację
  - [ ] Dostarczyć dokumentacje 
  - [ ] Przedstawić szczegółowy wkład poszczególnych osób w realizację projektu
  - [ ] Wymagane oddanie 
    * [ ] Filmu
    * [ ] Archiwum projektu 
    * [ ] Pliku PDF z opisem aplikacji


<a id="opis"></a>
## OPIS DZIAŁANIA APLIKACJI
Aplikacja mobilna „Heatly Mornings”, przeznaczona dla smartfonów z systemem Android,
zostanie zaprojektowana tak, aby zapewnić użytkownikowi możliwość realizacji zarówno
predefiniowanych, jak i spersonalizowanych zadań. Jej celem będzie ułatwienie porannej rutyny,
poprawa samopoczucia oraz zwiększenie produktywności na cały dzień.

Każdy użytkownik będzie posiadał indywidualne konto, na którym będą przechowywane
wszystkie dane dotyczące wykonanych zadań, w tym ich status, czas realizacji oraz ewentualne
dodatkowe informacje. Dzięki temu możliwe będzie śledzenie postępów, analiza nawyków oraz
personalizacja rekomendacji, co pozwoli na bardziej efektywne planowanie codziennych
aktywności.

Aplikacja będzie oferowała kilka domyślnych zadań do wyboru, a użytkownik będzie miał
również możliwość dodania własnych, dostosowanych do swoich potrzeb i nawyków. Każdego dnia
użytkownik będzie mógł oznaczyć jako wykonane od 4 do 10 zadań, jednak po upływie
wyznaczonego czasu funkcja odznaczania zostanie zablokowana. Wówczas dostępne będą jedynie
statystyki oraz szczegółowe dane dotyczące dotychczasowej aktywności. Dodatkowo aplikacja
zapewni dostęp do inspirujących porad i wskazówek, które mogą pomóc w utrzymaniu motywacji
oraz budowaniu zdrowych nawyków.

W aplikacji zostanie zaimplementowany system poziomów użytkownika, który będzie
odzwierciedlał jego postępy i zaangażowanie w wykonywanie codziennych zadań. Po zdobyciu
każdego nowego poziomu użytkownik otrzyma nagrody, takie jak opcje personalizacji aplikacji (np.
zmiana motywu kolorystycznego, ikon czy tła), dodatkowe funkcje lub unikalne odznaki
motywacyjne. System ten ma na celu zachęcenie do regularnego korzystania z aplikacji oraz
budowanie pozytywnych nawyków poprzez element grywalizacji.


<a id="architektura"></a>
## ARCHITEKTURA PROJEKTU
**Model KLIENT-SERVER z BAZĄ DANYCH w chmurze**
- **FRONTEND (KLIENT)**
    * Aplikacja Mobilna
    * Aplikacja Desktopowa
- **BACKEND (SERVER)**
    * Serwer z Bazą Danych
- **BAZA DANYCH**
    * Lokalna (dev) - PostgreSQL z JDBC
    * Zdalna (docelowa) - PostgreSQL z API REST


<a id="diagramy"></a>
##  DIAGRAMY
Ilustrują strukturę oraz działanie aplikacji. Ułatwiają one zrozumienie funkcjonalności i zależności pomiędzy elementami:
 * [diagram przypadków użycia](https://pwsztaredupl-my.sharepoint.com/:w:/g/personal/37719_student_atar_edu_pl/EbqinKLvKwpNs7HVGeJmRboBnpBxtmuHdyfVTFncYpUhRA?e=95cUoT)
 * [diagram klas](https://pwsztaredupl-my.sharepoint.com/:w:/g/personal/37719_student_atar_edu_pl/EeuofDhrzLlBr6Ej1qLMGDIB6fXx9uuOVE2UpRcw5EOWEw?e=TGPBhN)

[Schemat bazy danych](https://pwsztaredupl-my.sharepoint.com/:w:/g/personal/37719_student_atar_edu_pl/EfVXX4gJQJJPrC7zOuZYNzUBd6hNJ9HXaRRkB1C9XHyFCA?e=PuCvlZ) opisuje encje oraz relacje między nimi. Pozwala on na przechowywanie i organizowanie informacji w sposób spójny, umożliwiający ich łatwe przetwarzanie i analizę.


<a id="interfejs"></a>
##  INTERFEJS
Projekt graficzny aplikacji wykonany w [Figmie](https://www.figma.com/design/qN1IrXdpBG3weqMASqAvhA/HEALTHY-MORNINGS?node-id=0-1&t=fdK453ONO3L7Ufu6-1). Przedstawia on wizualną koncepcję interfejsu użytkownika oraz układ poszczególnych elementów.


<a id="trello"></a>
##  TRELLO
[Tablica](https://trello.com/invite/b/67ee3733e48c55c89dd01caa/ATTI6593999a38090379ecc2931ee600fd0f0835C881/healthy-mornings) została wykorzystana do zarządzania zadaniami w projekcie. Pokazuje aktualny postęp prac oraz zaplanowane etapy rozwoju aplikacji.


<a id="plan"></a>
## PLAN REALIZACJI PROJEKTU

### ETAP 1 - ANALIZA I PROJEKTOWANIE
**Cel:** Opracowanie koncepcji aplikacji, wymagań i architektury
1. **Analiza wymagań**
    * [x] Określenie funkcjonalności aplikacji
    * [x] Zdefiniowanie użytkowników systemu
    * [x] Identyfikacja przypadków użycia
2. **Projektowanie bazy danych**
    * [x] Model ERD bazy PostgreSQL
    * [x] Struktura tabel
    * [x] Relacje między tabelami
3. **Projektowanie UI/UX**
    * [x] Makiety ekranów aplikacji
    * [x] Przepływ użytkownika
4. **Wybór technologii**
    * [x] Zintegrowane środowisko programistyczne
    * [x] Biblioteki i API projektu
    * [x] Technologia bazy danych

### ETAP 2 - IMPLEMENTACJA BACKENDU
**Cel:** Stworzenie API i logiki aplikacji
1. **Konfiguracja środowiska**
    * [x] Instalacja PostgreSQL, Spring Boot, Android Studio
    * [x] Konfiguracja JDBC i połączenia z bazą
2. **Implementacja systemu użytkowników**
    * [x] Rejestracja 
    * [x] Logowanie 
    * [ ] Aautoryzacja
    * [x] Obsługa konta
    * [ ] Role użytkowników
3. **Obsługa zadań**
    * [x] Dodawanie zadań 
    * [x] Edycja zadań 
    * [x] Usuwanie zadań
    * [ ] Śledzenie postępu użytkownika
4. **System poziomów i nagród**
    * [ ] Algorytm naliczania punktów
    * [ ] Przypisywanie poziomów
    * [ ] Obsługa nagród
5. **Statystyki i analiza nawyków**
    * [ ] Tworzenie raportów postępu
6. **API dla aplikacji mobilnej**
    * [ ] RESTful API do komunikacji z Androidem

### ETAP 3 - IMPLEMENTACJA FRONTENDU
**Cel:** Stworzenie interfejsu użytkownika i integracja z backendem
1. **Konfiguracja projektu w Android Studio**
    * [x] Utworzenie pakietu com.nforge.healthymornings
    * [x] Ustawienie języka programowania na Java
    * [x] Ustawienia SDK API 34
    * [x] Ustawienie języka konfiguracji kompilacji na Gradle Kotlin DSL
    * [x] Przygotowanie Wirtualnego Emulowanego Urządzenia do testów
2. **Implementacja ekranów**
    * [ ] **Ekrany ogólnodostępne**
      1. [x] Login
      2. [x] Rejestracja
      3. [ ] Porady
    * [ ] **Ekrany zalogowanego użytkownika**
      1. [x] Konto
      2. [x] Lista zadań
      3. [ ] Statystyki
    * [x] **Ekrany ograniczone czasowo**
      1. [x] Task 
      2. [x] TODO
3. **Obsługa nawigacji**
    * [x] System FragmentManager
    * [x] Przełączanie ekranów w zależności od statusu logowania
4. **Implementacja systemu zadań**
    * [x] Wyświetlanie listy
    * [ ] Oznaczanie zadań jako wykonane
    * [ ] Blokowanie odznaczania po czasie
5. **Integracja z backendem**
    * [ ] Połączenie z API Spring Boot (Retrofit)
    * [ ] Logowanie i uwierzytelnianie użytkownika (JWT)

### ETAP 4 - TESTOWANIE (OPCJONALNE)
**Cel:** Wykrycie i poprawa błędów
1. **Testy integracyjne**
    * [ ] Sprawdzenie komunikacji Android ↔ Backend
    * [ ] Testowanie API
2. **Testy wydajnościowe**
    * [ ] Obciążenie bazy danych
    * [ ] Optymalizacja zapytań SQL
3. **Testy UI/UX**
    * [ ] Użyteczność interfejsu
    * [ ] Poprawność działania ekranów


<a id="dane"></a>
## DANE TECHNICZNE
| Kategoria                     | Nazwa                      |
|-------------------------------|----------------------------|
| Nazwa Aplikacji               | Healthy Mornings           |
| Nazwa Pakietu                 | com.nforge.healthymornings |
| Język Programowania           | Java                       |
| Środowisko programistyczne    | Android Studio             |
| Język konfiguracji kompilacji | Gradle Kotlin DSL          |
| Framework                     | Android SDK 34             |

<a id="technologie"></a>
## WYKORZYSTYWANE BIBLIOTEKI / TECHNOLOGIE
- Baza Danych: **PostgreSQL**
    * Sama Baza Danych realizowana jest na potrzeby innego projektu
    * Planowanym jest jej integracja z Healthy Mornings najpierw lokalnie a potem poprzez serwer
- Zarządzanie UI i interakcja z użytkownikiem: 
  * **Android Jetpack (ViewModel)** - Zarządzanie UI
  * **Material Design** - Nowoczesne komponenty interfejsu
  * **ListView** - Dynamiczne listy zadań
  * **ViewBinding** - Ułatwienie obsługi widoków
- Obsługa sieci i synchronizacja:
    * **Retrofit** - Zaawansowana obsługa API
    * **Firebase Cloud Messaging** - Obługa powiadomień push
    * **Spring Boot/JDBC** - Obsługa Bazy Danych
    * **Bcrypt** - Szyfrowanie hasła
- **Permissions API** - Obsługa uprawnień użytkownika
- **Android WorkManager** - Obsługa zadań w tle
- **JPA** - Przechowywanie użytkowników
- **MVVM** - Struktura aplikacji


<a id="struktura"></a>
## STRUKTURA APLIKACJI (MVVM)
```bash
com.nforge.healtymornings
├── ui            # Interfejs użytkownika (aktywnosci i fragmenty)
├── model         # Klasy reprezentujące dane (np. Task, User)
├── data          # Obsługa bazy danych (Spring Boot)
├── repository    # Logika zarządzania danymi
├── viewmodel     # MVVM (opcjonalne, do zarządzania stanem)
├── utils         # Funkcje pomocnicze
├── services      # Serwisy np. powiadomienia
```