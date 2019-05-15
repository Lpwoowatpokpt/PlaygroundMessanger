package com.lpwoowatpokpt.playgroundmessanger.Model;

import android.support.annotation.NonNull;

public class UserId {
    public String userId;

    public <T extends UserId> T widthId(@NonNull final String userId) {
        this.userId = userId;
        return (T)this;
    }
}
