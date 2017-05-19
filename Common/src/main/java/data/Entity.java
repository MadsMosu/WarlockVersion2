package data;

import data.componentdata.Image;
import java.io.Serializable;
import States.CharacterState;
import States.MovementState;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Entity implements Serializable {

    private final String ID = UUID.randomUUID().toString();
    private EntityType type;
    public float dx;
    public float dy;
    private float angle;
    private float[] shapeX = new float[4];
    private float[] shapeY = new float[4];
    private float speed;
    private float radius;
    private int size;
    private int numPoints;
    private float width;
    private CharacterState charState;
    private MovementState moveState;
    private int level;
    private int expPoints;
    private Image view;
    private Map<Class<?>, Object> data = new ConcurrentHashMap<>();

    public void add(Object data)
    {
        this.data.put(data.getClass(), data);
    }

    public void remove(Class<?> type)
    {
        this.data.remove(type);
    }

    public <T> T get(Class<T> type)
    {
        return (T)data.get(type);
    }

    
    public MovementState getMoveState() {
        return moveState;
    }

    public void setMoveState(MovementState moveState) {
        this.moveState = moveState;
    }

    public CharacterState getCharState() {
        return charState;
    }

    public void setCharState(CharacterState charState) {
        this.charState = charState;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    public void setNumpoints(int numpoints) {
        this.numPoints = numpoints;
    }

    public int getNumpoints() {
        return this.numPoints;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setRadius(float r) {
        this.radius = r;
    }

    public float getRadius() {
        return radius;
    }

    public String getID() {
        return ID.toString();
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return type;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float[] getShapeX() {
        return shapeX;
    }

    public void setShapeX(float[] shapeX) {
        this.shapeX = shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }

    public void setShapeY(float[] shapeY) {
        this.shapeY = shapeY;
    }

    public boolean isType(EntityType entityType) {
        return this.type.equals(entityType);
    }

    public boolean contains(float x, float y) {
        boolean b = false;
        for (int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++) {

            if ((shapeY[i] > y) != (shapeY[j] > y)
                    && (x < (shapeX[j] - shapeX[i])
                    * (y - shapeY[i]) / (shapeY[j] - shapeY[i])
                    + shapeX[i])) {
                b = !b;
            }
        }
        return b;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExpPoints() {
        return expPoints;
    }

    public void setExpPoints(int expPoints) {
        this.expPoints = expPoints;
    }

    public Image getView() {
        return view;
    }

    public void setView(Image view) {
        this.view = view;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }
    
    public void setRunningState(float angle, Entity e) {
        if (angle > -45 && angle < 45) {
            e.setMoveState(MovementState.RUNNINGRIGHT);
        } else if (angle < 135 && angle > 45) {
            e.setMoveState(MovementState.RUNNINGUP);
        } else if (angle > -135 && angle < -45) {
            e.setMoveState(MovementState.RUNNINGDOWN);
        } else {
            e.setMoveState(MovementState.RUNNINGLEFT);
        }
    }
  
    
}
