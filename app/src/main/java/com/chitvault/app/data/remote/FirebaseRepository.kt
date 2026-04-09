package com.chitvault.app.data.remote

import com.chitvault.app.data.local.PersonDao
import com.chitvault.app.data.local.toEntity
import com.chitvault.app.data.local.toModel
import com.chitvault.app.data.model.PersonModel
import com.chitvault.app.data.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val personDao: PersonDao,
) {
    fun observePersons(): Flow<List<PersonModel>> {
        return personDao.observePersons().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun userExists(email: String): Boolean {
        val usersSnapshot = firebaseDatabase.reference.child(USERS_NODE).awaitOnce()
        return usersSnapshot.children.any { child ->
            child.getValue(UserModel::class.java)?.mail.equals(email, ignoreCase = true)
        }
    }

    suspend fun signUp(email: String, username: String): Result<Unit> = runCatching {
        val alreadyExists = userExists(email)
        if (alreadyExists) error("An account with this email already exists.")
        val newUser = UserModel(username = username, mail = email)
        suspendCancellableCoroutine { continuation ->
            val ref = firebaseDatabase.reference.child(USERS_NODE).push()
            ref.setValue(newUser)
                .addOnSuccessListener { if (continuation.isActive) continuation.resume(Unit) }
                .addOnFailureListener { if (continuation.isActive) continuation.resumeWithException(it) }
        }
    }

    suspend fun addPerson(person: PersonModel): Result<Unit> = runCatching {
        suspendCancellableCoroutine { continuation ->
            val ref = firebaseDatabase.reference.child(PERSONS_NODE).push()
            val personWithId = person.copy(id = ref.key ?: person.id)
            ref.setValue(personWithId)
                .addOnSuccessListener { if (continuation.isActive) continuation.resume(Unit) }
                .addOnFailureListener { if (continuation.isActive) continuation.resumeWithException(it) }
        }
    }

    suspend fun syncPersons(): Result<Unit> {
        return runCatching {
            val snapshot = firebaseDatabase.reference.child(PERSONS_NODE).awaitOnce()
            val persons = snapshot.children.mapNotNull { child ->
                child.toPersonModel()
            }
            personDao.clearAll()
            personDao.insertAll(persons.map { it.toEntity() })
        }
    }

    private fun DataSnapshot.toPersonModel(): PersonModel? {
        val person = getValue(PersonModel::class.java) ?: return null
        return person.copy(id = key ?: person.id.ifBlank { person.mail.ifBlank { person.username } })
    }

    private suspend fun com.google.firebase.database.DatabaseReference.awaitOnce(): DataSnapshot =
        suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (continuation.isActive) {
                        continuation.resume(snapshot)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }

            addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation { removeEventListener(listener) }
        }

    companion object {
        private const val USERS_NODE = "users"
        private const val PERSONS_NODE = "persons"
    }
}
