package biitworx.db.com.lib.tlt.helper;

import java.util.UUID;

/**
 * Created by marcel.weissgerber on 25.05.2016.
 */
public abstract class BaseDataObject {
    private int pid =-1;
    @DbField(name="uid")
    private UUID uid;

    public void setUID(String uid){
        this.uid=UUID.fromString(uid);
    }

    public BaseDataObject(){
        uid=UUID.randomUUID();
    }


    public UUID getUID() {
        return uid;
    }

    public  void importedEx(int pid){
        this.pid = pid;
        imported();
    }

    public  void importedEx2(){

        imported();
    }
    public  void createdEx(int pid){
        this.pid = pid;

    }
    protected abstract void imported();

    public int getPid(){
        return pid;
    }
}
