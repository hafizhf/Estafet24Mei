package andlima.hafizhfy.estafet24mei.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "userpref")

    companion object {
        val ID = preferencesKey<String>("KEY_ID")
        val USERNAME = preferencesKey<String>("KEY_USERNAME")
        val PASSWORD = preferencesKey<String>("KEY_PASSWORD")
        val NAME = preferencesKey<String>("KEY_NAME")
        val AGE = preferencesKey<String>("KEY_AGE")
        val ADDRESS = preferencesKey<String>("KEY_ADDRESS")
    }

    val id: Flow<String> = dataStore.data.map {
        it[ID] ?: ""
    }

    val username: Flow<String> = dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val password: Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    val name: Flow<String> = dataStore.data.map {
        it[NAME] ?: ""
    }

    val age: Flow<String> = dataStore.data.map {
        it[AGE] ?: ""
    }

    val address: Flow<String> = dataStore.data.map {
        it[ADDRESS] ?: ""
    }

    suspend fun userLogin(
        id: String,
        username: String,
        password: String,
        name: String,
        age: String,
        address: String
    ) {
        dataStore.edit {
            it[ID] = id
            it[USERNAME] = username
            it[PASSWORD] = password
            it[NAME] = name
            it[AGE] = age
            it[ADDRESS] = address
        }
    }

    suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }
}