# HEALTHY MORNINGS - APLIKACJA MOBILNA
**Programowanie Systemów Mobilnych II - Projekt zaliczeniowy**
[!] Zawartość README.md stanowi także dokumentację projektu.

## SPIS TREŚCI
- [Osoby zaangażowane](#autorzy)
- [Wymagania projektowe](#wymagania)
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
- projekt może zostać zrealizowany w maksymalnie 3 osobowych grupach
    * ✅ Nad projektem pracuje **grupa dwuosobowa**
- technologia Android (Java lub Kotlin) lub iOS (Swift)
    * ✅ Projekt jest bazowany na technologii **Android** (w języku **Java**)
- użycie systemu kontroli wersji GIT
    * ✅ Projekt zawiera system kontroli wersji **GIT** (oraz posiada zdalne repozytorium na platformie **GITHUB**)

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
## DANE TECHNICZNE APLIKACJI
- **Nazwa Aplikacji:** Healthy Mornings
- **Nazwa Pakietu:** com.nforge.healthymornings
- **Język programowania:** Java
- **Minimalne SDK:** API 34
- **Język konfiguracji kompilacji:** Kotlin DSL

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