package game.tennis;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatorParcelable  implements Parcelable.Creator<Packet> {
  
  public Packet createFromParcel(Parcel source) {
        return new Packet(source);
  }
  
  public Packet[] newArray(int size) {
        return new Packet[size];
  }

}
