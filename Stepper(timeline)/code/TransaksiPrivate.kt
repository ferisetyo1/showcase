package com.llc.thelegionpt.data.network.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.llc.thelegionpt.utils.toDateorNull
import com.llc.thelegionpt.utils.toFormattedDate
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class TransaksiPrivate(
    @SerializedName("alamatprivate")
    val alamatprivate: AlamatPrivate? = null,
    @SerializedName("ap_create_at")
    val apCreateAt: String? = null,
    @SerializedName("ap_update_at")
    val apUpdateAt: String? = null,
    @SerializedName("harga_trainer")
    val hargaTrainer: HargaTrainer? = null,
    @SerializedName("tp_harga")
    val tpHarga: Double? = null,
    @SerializedName("tp_ap_id")
    val tpApId: Int? = null,
    @SerializedName("tp_generate_url")
    val tpGenerateUrl: String? = null,
    @SerializedName("tp_ht_id")
    val tpHtId: Int? = null,
    @SerializedName("tp_id")
    val tpId: Int? = null,
    @SerializedName("tp_invoice")
    val tpInvoice: String? = null,
    @SerializedName("tp_is_cancel")
    val tpIsCancel: Boolean? = null,
    @SerializedName("tp_is_confirm")
    val tpIsConfirm: Boolean? = null,
    @SerializedName("tp_is_done")
    val tpIsDone: Boolean? = null,
    @SerializedName("tp_is_mulai")
    val tpIsMulai: Boolean? = null,
    @SerializedName("tp_is_paid")
    val tpIsPaid: Boolean? = null,
    @SerializedName("tp_jam_private")
    val tpJamPrivate: String? = null,
    @SerializedName("tp_jam_private_end")
    val tpJamPrivateEnd: String? = null,
    @SerializedName("tp_date_private_end")
    val tpDatePrivateEnd: String? = null,
    @SerializedName("tp_meet_url")
    val tpMeetUrl: String? = null,
    @SerializedName("tp_metode_pembayaran")
    val tpMetodePembayaran: String? = null,
    @SerializedName("tp_nama_gym")
    val tpNamaGym: String? = null,
    @SerializedName("tp_nama_ongkir_lib")
    val tpNamaOngkirLib: String? = null,
    @SerializedName("tp_nama_payment_lib")
    val tpNamaPaymentLib: String? = null,
    @SerializedName("tp_pt_id")
    val tpPtId: Int? = null,
    @SerializedName("tp_status")
    val tpStatus: StatusTransaksi? = null,
    @SerializedName("tp_tgl_private")
    val tpTglPrivate: String? = null,
    @SerializedName("tp_token_payment")
    val tpTokenPayment: String? = null,
    @SerializedName("tp_user_id")
    val tpUserId: Int? = null,
    @SerializedName("tp_waktu_expired")
    val tpWaktuExpired: String? = null,
    @SerializedName("trainer")
    val trainer: Trainer? = null,
    @SerializedName("customer")
    val customer: User? = null,
    @SerializedName("log")
    val log: List<LogTransaksiPrivate>? = null
) : Parcelable {
    fun getTimeEnd() = tpDatePrivateEnd.toDateorNull("yyyy-MM-dd HH:mm:ss")?.time
        ?: 0L

    fun getTimeStart() = "$tpTglPrivate $tpJamPrivate".toDateorNull("yyyy-MM-dd HH:mm")?.time
        ?: 0L

    fun getDiffMilis(): Long {
        val timespriv = getTimeStart()
        val timesnow = Date().time
        return timespriv - timesnow
    }

    fun getDiffMilisBerlangsung(): Long {
        val timesprivstart = getTimeStart()
        val timesprivend = getTimeEnd()
        val timesnow = Date().time
        if (timesprivstart > timesnow) return 0
        return timesprivend - timesnow
    }

    fun getProgress(): Float {
        val timesprivstart = getTimeStart()
        val timesprivend = getTimeEnd() - timesprivstart
        val timesnow = getTimeEnd() - Date().time
        return (timesnow.toDouble() / timesprivend.toDouble()).toFloat()
    }

    fun getJadwalWithWIB() = "${
        tpTglPrivate.toFormattedDate(
            inputPattern = "yyyy-MM-dd",
            outputPattern = "dd-MMMM-yyyy"
        )
    }. ${tpJamPrivate.orEmpty()} - ${tpJamPrivateEnd.orEmpty()} WIB."
}