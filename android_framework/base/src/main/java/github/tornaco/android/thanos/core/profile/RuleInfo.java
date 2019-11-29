package github.tornaco.android.thanos.core.profile;

import android.os.Parcel;
import android.os.Parcelable;

public final class RuleInfo implements Parcelable {
    private String name;
    private String description;
    private String ruleString;
    private String author;
    private String updateTimeMills;
    private boolean enabled;
    private int format;

    private RuleInfo(Parcel in) {
        name = in.readString();
        description = in.readString();
        ruleString = in.readString();
        author = in.readString();
        updateTimeMills = in.readString();
        enabled = in.readByte() != 0;
        format = in.readInt();
    }

    public RuleInfo(String name,
                    String description,
                    String ruleString,
                    String author,
                    String updateTimeMills,
                    boolean enabled,
                    int format) {
        this.name = name;
        this.description = description;
        this.ruleString = ruleString;
        this.author = author;
        this.updateTimeMills = updateTimeMills;
        this.enabled = enabled;
        this.format = format;
    }

    public static final Creator<RuleInfo> CREATOR = new Creator<RuleInfo>() {
        @Override
        public RuleInfo createFromParcel(Parcel in) {
            return new RuleInfo(in);
        }

        @Override
        public RuleInfo[] newArray(int size) {
            return new RuleInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(ruleString);
        parcel.writeString(author);
        parcel.writeString(updateTimeMills);
        parcel.writeByte((byte) (enabled ? 1 : 0));
        parcel.writeInt(format);
    }
}
