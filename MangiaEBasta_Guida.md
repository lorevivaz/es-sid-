
# Guida alla Progettazione dell'App "Mangia e Basta"

Questa guida descrive la progettazione dell'app **Mangia e Basta**, un'applicazione per la consegna di pasti con droni. È destinata a un'intelligenza artificiale che gestirà il processo di sviluppo.

---

## **1. Descrizione Generale**
L'app permette agli utenti di:
- Acquistare menù unici senza personalizzazioni.
- Monitorare lo stato di consegna tramite una mappa in tempo reale.
- Salvare e gestire i dettagli del profilo e dello stato degli ordini.

Il sistema utilizza un server dedicato per gestire dati e transazioni, con comunicazione basata su API.

---

## **2. Funzionalità Principali**
### **2.1. Registrazione implicita**
- **Descrizione**: L'app richiede un numero di sessione (`SID`) al server alla prima apertura.
- **Persistenza**: Il `SID` deve essere memorizzato localmente per tutte le comunicazioni future.

### **2.2. Gestione Profilo**
- **Campi richiesti**:
  - Nome
  - Cognome
  - Nome sulla carta di credito
  - Numero carta di credito (16 cifre)
  - Data di scadenza (mese/anno)
  - Codice CVV (3 cifre)
- **Funzionalità aggiuntive**:
  - Visualizzare l'ultimo ordine effettuato.

### **2.3. Lista dei Menù**
- Mostra:
  - Nome del menù
  - Immagine quadrata (in formato Base64)
  - Costo
  - Breve descrizione
  - Tempo di consegna stimato.

### **2.4. Dettagli Menù**
- Include:
  - Immagine ingrandita.
  - Descrizione dettagliata.
  - Pulsante per acquistare il menù.
  - **Restrizioni**: Non consentire l'acquisto se:
    - Il profilo non è completato.
    - Esiste un ordine in corso.

### **2.5. Stato Consegna**
- **Elementi da visualizzare**:
  - Stato (`consegnato` o `in consegna`).
  - Nome del menù.
  - Orario dell'ordine.
  - Mappa con:
    - Punto di consegna.
    - Punto di partenza (ristorante).
    - Posizione attuale del drone.
- **Aggiornamento automatico**: Ogni 5 secondi.

### **2.6. Salvataggio dello Stato**
- Ricordare la pagina visualizzata per ricaricarla al riavvio dell'app.

---

## **3. Specifiche Tecniche**
### **3.1. Comunicazione con il Server**
- **Endpoint base**: [API Server](https://develop.ewlab.di.unimi.it/mc/2425/)
- **Formato immagine**: Base64 (senza prefisso HTML).
- **Ottimizzazione**:
  - Scaricare e salvare localmente le immagini.
  - Controllare la versione dell'immagine (`imageVersion`) prima di richiederla al server.

### **3.2. Validazione Dati**
- La carta di credito deve iniziare con "1" (per il testing).
- Verifica sintattica lato client:
  - Numero di carta: 16 cifre.
  - CVV: 3 cifre.
  - Data di scadenza valida.

### **3.3. Limiti del Sistema**
- Lingua: Italiano o Inglese (consistente in tutta l'app).
- Ordine singolo per volta.
- Dimensione massima dei campi:
  - Nome/Cognome: 15 caratteri.
  - Nome carta: 31 caratteri.

---

## **4. Passaggi di Progettazione**
### **4.1. Struttura del Progetto**
- **Moduli principali**:
  - **Autenticazione**: Gestione `SID`.
  - **Profilo**: Gestione dati utente e validazione.
  - **Catalogo**: Visualizzazione lista menù e dettagli.
  - **Ordini**: Stato consegna e aggiornamento mappa.
  - **Persistenza**: Salvataggio locale di immagini e stato.

### **4.2. Navigazione**
- Disegnare lo schema di navigazione:
  - **Home**: Lista dei menù.
  - **Dettagli Menù**: Acquisto.
  - **Profilo**: Gestione dati e ultimo ordine.
  - **Stato Consegna**: Mappa e aggiornamenti.

### **4.3. Interfaccia Grafica**
- **Design richiesto**:
  - Personalizzabile, coerente, e intuitivo.
  - Supporto per immagini quadrate.

---

## **5. Testing**
### **5.1. Validazione API**
- Testare manualmente le API con strumenti come Postman.
- Simulare errori comuni per verificare la robustezza.

### **5.2. Simulazioni**
- Utilizzare un dispositivo Pixel 7 con API 34 per testare l'app.

---

## **6. Note Importanti**
- Non includere dati personali o protetti da copyright.
- Ogni studente deve utilizzare un proprio `SID`.
- Rispettare tutte le restrizioni del progetto per evitare sanzioni accademiche.

---

## **7. Risultato Finale**
L'app deve:
1. Essere completamente funzionale e testata.
2. Supportare un design reattivo e intuitivo.
3. Adattarsi alle specifiche fornite, mantenendo una comunicazione stabile con il server.
