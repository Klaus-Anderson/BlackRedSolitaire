package gms.angus.angussoli.model;

import java.io.Serializable;

/**
 * Created by Harry Cliff on 11/29/17.
 */

public class RankedPlayer implements Serializable{
    private String userName;
    private long avgGame;

    public RankedPlayer(String userName, long avgGame) {
        this.userName = userName;
        this.avgGame = avgGame;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getAvgGame() {
        return avgGame;
    }

    public void setAvgGame(long avgGame) {
        this.avgGame = avgGame;
    }
}
