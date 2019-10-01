package github.tornaco.android.thanos.core.n

import android.app.Notification
import android.os.Parcel
import android.os.Parcelable

data class NotificationRecord(val pkg: String, val n: Notification) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Notification::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pkg)
        parcel.writeParcelable(n, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationRecord> {
        override fun createFromParcel(parcel: Parcel): NotificationRecord {
            return NotificationRecord(parcel)
        }

        override fun newArray(size: Int): Array<NotificationRecord?> {
            return arrayOfNulls(size)
        }
    }

}