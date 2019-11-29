package github.tornaco.android.thanos.core.profile

import android.os.Parcel
import android.os.Parcelable

data class RuleInfo(
    var name: String,
    var description: String,
    var ruleString: String,
    var author: String,
    var updateTimeMills: Long,
    var enabled: Boolean,
    var format: Int
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(ruleString)
        parcel.writeString(author)
        parcel.writeLong(updateTimeMills)
        parcel.writeByte(if (enabled) 1 else 0)
        parcel.writeInt(format)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RuleInfo> {
        override fun createFromParcel(parcel: Parcel): RuleInfo {
            return RuleInfo(parcel)
        }

        override fun newArray(size: Int): Array<RuleInfo?> {
            return arrayOfNulls(size)
        }
    }

}
