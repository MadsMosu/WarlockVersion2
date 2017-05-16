package data;

import events.Event;
import events.EventType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import States.GameState;

public class GameData {

    private int layerCount;
    private int shrinkTime;
    private float delta;
    private int displayWidth;
    private int displayHeight;
    private final GameKeys keys = new GameKeys();
    private final List<Event> events = new CopyOnWriteArrayList<>();
    private int screenX;
    private int screenY;
    private GameState gameState;
    private float roundTime;
    private int roundNumber;
    private float currentTime;
    private int mapHeight;
    private int mapWidth;
    
    
    private int mousePositionX;
    private int mousePositionY;
    
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

    public float getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(float currentTime) {
        this.currentTime = currentTime;
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

    public void addEvent(Event e) {
        events.add(e);
    }

    public void removeEvent(Event e) {
        events.remove(e);
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Event> getEvents(EventType eventType, String id) {
        List<Event> r = new ArrayList<>();

        for (Event event : events) {
            if (event.getType().equals(eventType) && event.getEntityID().equals(id)) {
                r.add(event);
            }
        }
        return r;
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
