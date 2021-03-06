package data;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import States.GameState;

public class GameData {

    private int layerCount;
    private int shrinkTime;
    private float delta;
    private int FPS;
    private int displayWidth;
    private int displayHeight;
    private final GameKeys keys = new GameKeys();
    private int screenX;
    private int screenY;
    private GameState gameState;
    private float roundTime;
    private float nextRoundCountdown;
    private int roundNumber;
    private int mapHeight;
    private int mapWidth;
    private String whoWinsRound = "";
    private String winner = "";
    private int maxRounds;
    
    private int mousePositionX;
    private int mousePositionY;

    public int getFPS() {
        return FPS;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
    
    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }
    
    public String getWhoWinsRound() {
        return whoWinsRound;
    }

    public void setWhoWinsRound(String whoWinsRound) {
        this.whoWinsRound = whoWinsRound;
    }

    public float getNextRoundCountdown() {
        return nextRoundCountdown;
    }

    public void setNextRoundCountdown(float nextRoundCountdown) {
        this.nextRoundCountdown = nextRoundCountdown;
    }
    
    public int getMousePositionX()
    {
        return mousePositionX;
    }

        public int getMousePositionY()
    {
        return mousePositionY;
    }
    
    public void setMousePosition(int x, int y)
    {
        mousePositionX = x;
        mousePositionY = y;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public float getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(float roundTime) {
        this.roundTime = roundTime;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(int layerCount) {
        this.layerCount = layerCount;
    }

    public int getShrinkTime() {
        return shrinkTime;
    }

    public void setShrinkTime(int shrinkTime) {
        this.shrinkTime = shrinkTime;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public int getScreenY() {
        return screenY;
    }

    public int getScreenX() {
        return screenX;
    }
    public void setMapHeight(int mapHeight){
        this.mapHeight = mapHeight;
    }
    public void setMapWidth(int mapWidth){
        this.mapWidth = mapWidth;
    }
    public int getMapHeight(){
        return mapHeight;
    }
    public int getMapWidth(){
        return mapWidth;
    }

}
