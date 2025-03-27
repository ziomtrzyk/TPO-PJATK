package zad1;

public class NBPEchangeRates {
    TabelaKursow tabela_kursow; // Zagnieżdżony obiekt "tabela_kursow"

    static class TabelaKursow {
        String uid;
        String data_publikacji;
        String numer_tabeli;
        String typ;
        Pozycja[] pozycja; // Tablica pozycji
    }

    static class Pozycja {
        String kurs_sredni;
        String kod_waluty;
        String nazwa_waluty;
        int przelicznik;
    }
}
