# 📚 BibliotekaApp_Java  
Desktop aplikacija za upravljanje bibliotekom (Java, Swing, MySQL)

BibliotekaApp_Java je desktop rješenje razvijeno u **Java Swing** okruženju, povezano sa MySQL bazom podataka.  
Aplikacija omogućava bibliotekarima jednostavno upravljanje knjigama, korisnicima, autorima i izdavanjem knjiga.

---

## 🔧 Tehnologije

- **Java (JDK 8+)**
- **Swing (GUI aplikacija)**
- **MySQL baza podataka**
- JDBC konekcija (`java.sql.*`)
- DbUtils za povezivanje ResultSet-a sa JTable

---

## 🗄️ Struktura baze podataka

Aplikacija je povezana sa MySQL bazom **biblioteka**, koja sadrži sljedeće tabele:

- `AUTORI`
- `BIBLIOTEKAR`
- `IZDAVACI`
- `IZDAVANJE`
- `KNJIGE`
- `KORISNIK`
- `MJESTO`
- `ZANR`

U projektu se nalazi eksportovana baza `biblioteka.sql` za jednostavnu instalaciju.

---

## ✨ Funkcionalnosti aplikacije

### 🔐 1. Login i registracija
- Bibliotekar se može **ulogovati** sa korisničkim imenom i lozinkom.
- Novi bibliotekar se može **registrovati** preko posebne forme.
- Nakon logovanja otvara se **Početna forma**.

---

### 🏠 2. Početna forma
Sa početne forme dostupni su sledeći moduli:

- 📖 **Pregled knjiga**
- 👤 **Pregled korisnika**
- 👔 **Pregled zaposlenih**
- ✍️ **Pregled autora**
- 📕 **Izdavanje knjiga**
- 🏷️ **Pregled izdavača**

---

### 📘 3. Upravljanje knjigama
- Dodavanje nove knjige  
- Izmjena postojećih podataka o knjizi  
- Brisanje knjige  
- Povezivanje knjige sa autorom, žanrom i izdavačem  
- Prikaz broja dostupnih primjeraka  
- Brza pretraga knjiga po nazivu
- Dodavanje autora, žanra i izdavača kroz veze sa odgovarajućim tabelama
- Automatsko popunjavanje `ComboBox`-ova u formama pomoću JDBC upita

---

### 🧑‍💼 4. Upravljanje korisnicima
- Dodavanje korisnika  
- Izmjena i brisanje korisnika  
- Pretraga korisnika  
- Dodavanje mjesta rođenja kroz tabelu `MJESTO`
- Automatsko popunjavanje `ComboBox`-ova u formama pomoću JDBC upita

---

### ✍️ 5. Upravljanje autorima
- Dodavanje autora  
- Izmjena autora  
- Brisanje autora  
- Pretraga autora
- Automatsko popunjavanje `ComboBox`-ova u formama pomoću JDBC upita

---

### 🏷️ 6. Upravljanje žanrovima i izdavačima
- Dodavanje, izmjena i brisanje  
- Automatsko popunjavanje `ComboBox`-ova u formama pomoću JDBC upita  

---

### 📚 7. Izdavanje knjiga
- Pregled dostupnih knjiga  
- Evidencija o tome ko je iznajmio koju knjigu  
- Automatsko smanjivanje broja dostupnih primjeraka

---

## 🔍 Brza pretraga
U svim prikazima (knjige, korisnici, autori…) postoji polje za **real-time pretragu**.

---

## 💻 Pokretanje aplikacije
1. Importuj projekat u **Eclipse IDE**.  
2. Podesi **JDK 8+** ili noviji.  
3. Poveži projekat sa MySQL bazom koristeći `Konekcija.java`.  
4. Pokreni glavnu klasu: `Login.java`.  
5. Koristi GUI forme za upravljanje podacima.

---

## 📸 Screenshots
![image alt](https://github.com/coricmarko2001-ship-it/BibliotekaApp_Java/blob/945294aad94a485bb24d6a7eeea04c8915615c52/Screenshots/Dodavanje%20knjige.png)
![image alt](https://github.com/coricmarko2001-ship-it/BibliotekaApp_Java/blob/07dfb5dc8461c6c4a856ecbe97083ce23e7b0e57/Screenshots/Izmjena%20knjige.png)
![image alt]()
![image alt]()
