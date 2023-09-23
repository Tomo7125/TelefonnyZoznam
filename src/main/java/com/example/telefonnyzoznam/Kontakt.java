package com.example.telefonnyzoznam;

public class Kontakt {
    String meno;
    String cislo;

    public Kontakt(String meno, String cislo) {
        this.meno = meno;
        this.cislo = cislo;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }
}
