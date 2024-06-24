package com.example.gridviewrealm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class City extends RealmObject {

    private String name;
    private long votes;

    @Ignore private int sessionId;

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long _votes) {
        this.votes = _votes;
    }


}
