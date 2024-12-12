package com.example.ktorexample

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.androidktorexample.CommunicationController
import com.example.ktorexample.ui.theme.KtorExampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Estensione per il DataStore
val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    // Chiavi per il DataStore
    private val SID_KEY = stringPreferencesKey("sid")
    private val UID_KEY = stringPreferencesKey("uid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KtorExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp) // Spazio tra i pulsanti
                    ) {
                        // Passa la funzione `checkAndFetchSid` come lambda
                        Greeting(name = "Android", onCheckAndFetchSid = { checkAndFetchSid() })
                    }
                }
            }
        }
    }

    // Recupera il SID dal DataStore
    private suspend fun getSid(): String? {
        return dataStore.data.map { preferences ->
            preferences[SID_KEY]
        }.firstOrNull()
    }

    // Salva il SID nel DataStore
    private suspend fun saveSid(sid: String) {
        dataStore.edit { preferences ->
            preferences[SID_KEY] = sid
        }
    }

    // Recupera l'UID dal DataStore
    private suspend fun getUid(): Int? {
        return dataStore.data.map { preferences ->
            preferences[UID_KEY]?.toInt()
        }.firstOrNull()
    }

    // Salva l'UID nel DataStore
    private suspend fun saveUid(uid: Int) {
        dataStore.edit { preferences ->
            preferences[UID_KEY] = uid.toString()
        }
    }

    // Funzione per verificare se il SID e UID sono presenti, altrimenti li crea
    private fun checkAndFetchSid() {
        CoroutineScope(Dispatchers.IO).launch {
            val sid = getSid()
            val uid = getUid()

            if (sid == null || uid == null) {
                Log.d("MainActivity", "SID or UID not found. Creating user...")
                try {
                    val userResponse = CommunicationController.createUser()
                    saveSid(userResponse.sid)
                    saveUid(userResponse.uid)
                    Log.d("MainActivity", "User created and saved: SID=${userResponse.sid}, UID=${userResponse.uid}")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error creating user: ${e.message}")
                }
            } else {
                Log.d("MainActivity", "User already exists: SID=$sid, UID=$uid")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onCheckAndFetchSid: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pulsante per controllare o creare l'utente
        Button(onClick = { onCheckAndFetchSid() }) {
            Text(text = "Check or Create User")
        }

        // Pulsante per creare un nuovo utente (esempio)
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val userResponse = CommunicationController.createUser()
                    Log.d("MainActivity", "User created: $userResponse")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error creating user: ${e.message}")
                }
            }
        }) {
            Text(text = "Create New User")
        }

        // Pulsante per ottenere i dati di un utente specifico
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val uid = 35223
                    val userResponse = CommunicationController.getUser(uid)
                    if (userResponse != null) {
                        Log.d("MainActivity", "User: $userResponse")
                    } else {
                        Log.e("MainActivity", "User not found.")
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error fetching user: ${e.message}")
                }
            }
        }) {
            Text(text = "Get User")
        }

        // Pulsante per ottenere la lista dei menu
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val menuList = CommunicationController.getMenu()
                    Log.d("MainActivity", "Menu List: $menuList")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error fetching menu: ${e.message}")
                }
            }
        }) {
            Text(text = "Get Menu")
        }

        // Pulsante per ottenere un'immagine base64
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val base64Image = CommunicationController.getImage()
                    Log.d("MainActivity", "Image: $base64Image")
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error fetching image: ${e.message}")
                }
            }
        }) {
            Text(text = "Get Image")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KtorExampleTheme {
        Greeting(name = "Android", onCheckAndFetchSid = {})
    }
}