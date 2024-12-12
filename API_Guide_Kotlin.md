
# Guida Completa per Implementare Richieste API

Questa guida descrive passo dopo passo come creare richieste API utilizzando una struttura simile a quella definita nel codice sopra. È pensata per essere comprensibile e riutilizzabile da un'IA o da uno sviluppatore umano.

---

## Struttura Generale

### 1. **Base URL**
Definire l'URL base da cui partiranno tutte le richieste:
```javascript
static BASE_URL = "https://develop.ewlab.di.unimi.it/mc/2425/";
```

### 2. **Funzione Generica per Richieste**
Crea una funzione `genericRequest` che gestisca tutte le richieste HTTP (GET, POST, PUT, ecc.).
Questa funzione accetta:
- `endpoint`: il percorso specifico per l'API.
- `verb`: il tipo di richiesta (GET, POST, PUT, ecc.).
- `queryParams`: eventuali parametri da includere nell'URL.
- `bodyParams`: il corpo della richiesta (se richiesto).

#### Implementazione:
```javascript
static async genericRequest(endpoint, verb, queryParams, bodyParams) {
    const queryParamsFormatted = new URLSearchParams(queryParams).toString();
    const url = this.BASE_URL + endpoint + "?" + queryParamsFormatted;

    let fetchData = {
        method: verb,
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
        },
    };

    if (verb !== "GET") {
        fetchData.body = JSON.stringify(bodyParams);
    }

    let httpResponse = await fetch(url, fetchData);
    const status = httpResponse.status;

    if (status === 200) {
        return await httpResponse.json();
    } else if (status === 204) {
        console.log("Request succeeded, no content returned.");
        return {};
    } else {
        const message = await httpResponse.text();
        throw new Error(`HTTP status: ${status} ${message}`);
    }
}
```

---

## Implementazioni Specifiche

### 1. **Registrazione Utente**
Effettua una richiesta `POST` per registrare un nuovo utente.
```javascript
static async register() {
    const endpoint = "user/";
    const verb = "POST";
    const queryParams = {};
    const bodyParams = {};
    return await this.genericRequest(endpoint, verb, queryParams, bodyParams);
}
```

### 2. **Recupera Dati Utente**
Richiede informazioni di un utente specifico tramite `GET`.
```javascript
static async getUser(sid, uid) {
    const endpoint = `user/${uid}`;
    const verb = "GET";
    const queryParams = { sid: sid };
    return await this.genericRequest(endpoint, verb, queryParams, {});
}
```

### 3. **Recupera Menu**
Richiede una lista di menu basata su posizione e session ID.
```javascript
static async getMenu(lat, lng, sid) {
    const endpoint = "menu/";
    const verb = "GET";
    const queryParams = { lat: lat, lng: lng, sid: sid };
    return await this.genericRequest(endpoint, verb, queryParams, {});
}
```

### 4. **Recupera Immagine del Menu**
Ottieni un'immagine in base64 per un menu specifico.
```javascript
static async getMenuImage(sid, mid) {
    const endpoint = `menu/${mid}/image`;
    const verb = "GET";
    const queryParams = { sid: sid };
    const response = await this.genericRequest(endpoint, verb, queryParams, {});

    if (response.base64) {
        return response.base64;
    } else {
        return -1;
    }
}
```

### 5. **Acquista un Menu**
Effettua una richiesta `POST` per acquistare un menu.
```javascript
static async buyMenu(mid, sid, lat, lng) {
    const endpoint = `menu/${mid}/buy`;
    const verb = "POST";
    const bodyParams = {
        sid: sid,
        deliveryLocation: { lat: lat, lng: lng },
    };
    return await this.genericRequest(endpoint, verb, {}, bodyParams);
}
```

### 6. **Modifica Dati Utente**
Aggiorna i dati di un utente tramite `PUT`.
```javascript
static async putUser(sid, uid, user) {
    const endpoint = `user/${uid}`;
    const verb = "PUT";
    const bodyParams = {
      firstName: user.firstName,
      lastName: user.lastName,
      cardFullName: user.cardFullName,
      cardNumber: user.cardNumber,
      cardExpireMonth: user.cardExpireMonth,
      cardExpireYear: user.cardExpireYear,
      cardCVV: user.cardCVV,
      sid: sid,
    }; // Costruiamo il body della richiesta con i dati dell'utente

    };
    return await this.genericRequest(endpoint, verb, {}, bodyParams);
}
```

### 7. **Recupera Dettagli di un Ordine**
Ottieni informazioni su un ordine specifico.
```javascript
static async getOrders(oid, sid) {
    const endpoint = `order/${oid}`;
    const verb = "GET";
    const queryParams = { sid: sid };
    return await this.genericRequest(endpoint, verb, queryParams, {});
}
```

---

## Best Practices

1. **Gestione degli Errori**: 
   Ogni richiesta dovrebbe gestire errori in modo chiaro.
   ```javascript
   try {
       const data = await CommunicationController.getUser(sid, uid);
   } catch (error) {
       console.error("Errore durante la richiesta:", error);
   }
   ```

2. **Log di Debug**: 
   Usa `console.log` per verificare input e output durante lo sviluppo.

3. **Validazione Input**:
   Controlla i parametri passati alle funzioni prima di effettuare la richiesta.

4. **Crittografia**:
   Per dati sensibili come `sid` o `cardNumber`, assicurati che siano protetti.

---

## Conversione in Kotlin
Quando implementi queste funzioni in Kotlin, usa librerie come Retrofit o Ktor per creare richieste simili. Ecco un esempio per `getUser` in Kotlin usando Retrofit:
```kotlin
@GET("user/{uid}")
suspend fun getUser(
    @Path("uid") uid: String,
    @Query("sid") sid: String
): Response<User>
```

---

## Conclusione
Questo schema fornisce una struttura riutilizzabile per la creazione di richieste API. Adattalo al linguaggio o alla piattaforma di sviluppo specifica.
