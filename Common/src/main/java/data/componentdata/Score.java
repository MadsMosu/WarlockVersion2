/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

/**
 *
 * @author Aleksander
 */
public class Score {

    private int kills = 0;
    private int hits = 0;
    private int totalKills = 0;
    private int totalHits = 0;

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        if (this.totalKills != totalKills) {
            this.totalKills = totalKills;
        }
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        if (this.totalHits != totalHits) {
            this.totalHits = totalHits;
        }
    }
}
