package com.llc.thelegionpt.fitur.chatting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.llc.thelegionpt.data.network.response.Pesan
import com.llc.thelegionpt.data.network.response.TransaksiPrivate
import com.llc.thelegionpt.data.repository.LegionRepository
import com.llc.thelegionpt.utils.toFormattedString
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RoomChatViewModel @Inject constructor(val repository: LegionRepository) : ViewModel() {
    val listChat = mutableStateOf<List<Pesan>>(emptyList())
    val data = mutableStateOf<TransaksiPrivate?>(null)
    val database = Firebase.database.getReference("message")
    fun initPesan(data: TransaksiPrivate) {
        this.data.value = data
        val myRef = database.child(data.tpInvoice.orEmpty())
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue<Pesan>()?.let {
                    listChat.value += listOf(it)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun sendMessage(input: String,onSuccess:()->Unit,onFailure: (String) -> Unit) {
        data.value?.let {
            database.child(it.tpInvoice.orEmpty()).child(Date().time.toString() + "_pt")
                .setValue(
                    Pesan(
                        it.tpPtId.toString(),
                        "trainer",
                        input,
                        Date().time,
                        Date().toFormattedString("yyyy-MM-dd")
                    )
                )
                .addOnFailureListener {
                    onFailure(it.message.orEmpty())
                    it.printStackTrace()
                }.addOnSuccessListener {
                    onSuccess()
                    println("success")
                }
        }
    }

}
