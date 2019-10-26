package github.tornaco.android.thanos.core.app.start;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public final class StartRecord implements Parcelable {

    private int method;
    private String requestPayload;
    private long whenByMills;
    private String packageName;
    private String starterPackageName;
    private String checker;
    private StartResult result;

    private StartRecord(Parcel in) {
        method = in.readInt();
        requestPayload = in.readString();
        whenByMills = in.readLong();
        packageName = in.readString();
        starterPackageName = in.readString();
        checker = in.readString();
        result = in.readParcelable(StartResult.class.getClassLoader());
    }

    public static final Creator<StartRecord> CREATOR = new Creator<StartRecord>() {
        @Override
        public StartRecord createFromParcel(Parcel in) {
            return new StartRecord(in);
        }

        @Override
        public StartRecord[] newArray(int size) {
            return new StartRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(method);
        parcel.writeString(requestPayload);
        parcel.writeLong(whenByMills);
        parcel.writeString(packageName);
        parcel.writeString(starterPackageName);
        parcel.writeString(checker);
        parcel.writeParcelable(result, i);
    }
}
