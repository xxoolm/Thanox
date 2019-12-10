package github.tornaco.android.thanos.core.profile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class GlobalVar implements Parcelable {

    private String name;
    private List<String> stringList;

    private GlobalVar(Parcel in) {
        name = in.readString();
        stringList = in.createStringArrayList();
    }

    public static final Creator<GlobalVar> CREATOR = new Creator<GlobalVar>() {
        @Override
        public GlobalVar createFromParcel(Parcel in) {
            return new GlobalVar(in);
        }

        @Override
        public GlobalVar[] newArray(int size) {
            return new GlobalVar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeStringList(stringList);
    }
}
