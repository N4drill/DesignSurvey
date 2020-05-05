package com.example.androidsampleconfiguration.app.dataaccess

import com.example.androidsampleconfiguration.app.dataaccess.model.AspectFirestore
import com.example.androidsampleconfiguration.app.dataaccess.model.QuestionFirestore
import com.example.androidsampleconfiguration.app.dataaccess.model.TypeFirestore
import com.example.androidsampleconfiguration.app.dataaccess.model.UserFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import de.aaronoe.rxfirestore.addDocumentSingle
import de.aaronoe.rxfirestore.getSingle
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getAllQuestions(): Single<List<QuestionFirestore>> = firestore.collection(QUESTION_COLLECTION).also {
        Timber.d("Running getAllQuestions")
    }.getQuestionsSingle()

    fun getAspect(aspectReference: DocumentReference): Single<AspectFirestore> =
        firestore.collection(ASPECT_COLLECTION).document(aspectReference.path.split("/")[1]).also {
            Timber.d("Running getAspect with path: ${aspectReference.path}")
        }.getSingle()

    fun getType(typeReference: DocumentReference): Single<TypeFirestore> =
        firestore.collection(TYPE_COLLECTION).document(typeReference.path.split("/")[1]).also {
            Timber.d("Running getType with path: ${typeReference.path}")
        }.getSingle()

    fun insertUser(userFirestore: UserFirestore): Single<DocumentReference> {
        val toInsert = HashMap<String, Any>()
        with(userFirestore) {
            toInsert["gender"] = gender
            toInsert["age"] = age
            toInsert["profession"] = profession
            toInsert["designExperience"] = designExperience
            toInsert["answeredQuestions"] = answeredQuestions
        }

        val usersRef = firestore.collection(USER_COLLECTION)
        return usersRef.addDocumentSingle(toInsert)
    }

    private fun CollectionReference.getQuestionsSingle(): Single<List<QuestionFirestore>> = Single.create { emitter ->
        get()
            .addOnSuccessListener {
                try {
                    val results = mutableListOf<QuestionFirestore>()
                    it.forEach { document ->
                        val res = document.toObject(QuestionFirestore::class.java)
                        results.add(res.copy(id = document.id))
                    }
                    emitter.onSuccess(results)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
            .addOnFailureListener { emitter.onError(it) }
    }

    companion object {
        private const val QUESTION_COLLECTION = "Question"
        private const val ASPECT_COLLECTION = "Aspect"
        private const val TYPE_COLLECTION = "Type"
        private const val USER_COLLECTION = "User"
    }
}
