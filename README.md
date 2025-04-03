# HEALTHY MORNINGS - APLIKACJA MOBILNA
**Programowanie Systemów Mobilnych II - Projekt zaliczeniowy**
### ℹ️ Zawartość README stanowi dokumentację projektu

## SPIS TREŚCI
- [Osoby zaangażowane](#autorzy)
- [Wymagania projektowe](#wymagania)
- [Terminy i wymagania](#terminy)
- [Opis działania](#opis)
- [Dane techniczne](#dane)
- [Struktura aplikacji](#struktura)

<a id="autorzy"></a>
## OSOBY ZAANGAŻOWANE W PROJEKT
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
* Do tego terminu grupy studentów powinni zdecydować się na konkretny temat. Wybór musi być uzgodniony i zaakceptowany przez prowadzącego. Przy zgłoszeniu należy podać imiona i nazwiska, numery albumów, temat pracy. Dodatkowo przygotować krótki opis aplikacji - 2 strony w formacie PDF. W przypadku niektórych projektów konieczne może być doprecyzowanie zakresu.
2. [ ] 04.04.2025 - **Przedstawienie planu realizacji (20 % oceny)**
  * W ramach tego etapu zespół projektowy ma za zadanie przedstawić:
    - [ ] Plan realizacji projektu
    - [ ] Listę wykorzystanych technologii oraz bibliotek
    - [ ] Przyjętą architekturę projektu
    - [ ] Diagram klas, Diagram przypadków użycia, Schemat bazy danych (relacji + encji)
    - [ ] Główne interfejsy aplikacji (szkic ekranów) wraz z opisem, w tym celu wykorzystać Figma lub App Moqups
    - [ ] Podział projektu na mniejsze podzadania, w tym celu wykorzystać Jira, Trello
3. [ ] 09.05.2025 - **Implementacja projektu (30 % oceny)**
   * Do tego terminu należy zaprezentować postęp projektu w oparciu o utworzoną dokumentacje. Wymagane jest oddanie:
    - [ ] Filmu z funkcjonowania aplikacji
    - [ ] Archiwum projektu w formacie .zip
    - [ ] Pliku PDF zawierającego opis + screeny funkcjonalności z obecnej wersji
4. [ ] 13.06.2025 - **Dostarczenie kompletnego projektu (50 % oceny)**
   * Na tym etapie zespół ma za zadanie dostarczyć kompletną aplikację wraz z dokumentacją i przedstawić szczegółowy wkład poszczególnych osób w realizację projektu. Wymagane oddanie filmu, archiwum projektu oraz pliku PDF z opisem aplikacji

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

<a id="dane"></a>
## DANE TECHNICZNE I WYKORZYSTYWANE TECHNOLOGIE
- Nazwa Aplikacji: **Healthy Mornings**
- Nazwa Pakietu: **com.nforge.healthymornings**
- Język Programowania: **Java**
- Framework: **Android SDK 34**
- Baza Danych: **PostgreSQL (lokalnie i na serwerze)**
- Dostęp do Bazy Danych: **JDBC**
- Zarządzanie UI: **Android ViewModel**
- Język konfiguracji kompilacji: **Kotlin DSL**

<a id="struktura"></a>
## Struktura Aplikacji
```bash
com.nforge.healtymornings
├── ui            # Interfejs użytkownika (aktywnosci i fragmenty)
├── model         # Klasy reprezentujące dane (np. Task, User)
├── data          # Obsługa bazy danych (SQLite / Room)
├── repository    # Logika zarządzania danymi
├── viewmodel     # MVVM (opcjonalne, do zarządzania stanem)
├── utils         # Funkcje pomocnicze
├── services      # Serwisy np. powiadomienia
```