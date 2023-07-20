package de.jcm.discordgamesdk.impl.channel;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.Result;

public class NoSuchInstanceException extends GameSDKException {

    private int i;

    public NoSuchInstanceException(int i) {
        super(Result.NOT_RUNNING);
        this.i = i;
    }

    @Override
    public String toString() {
        return "no such instance: "+i;
    }
}
