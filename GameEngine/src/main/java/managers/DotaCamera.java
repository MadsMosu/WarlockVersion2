package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class DotaCamera extends OrthographicCamera {

    private float xmin;
    private float xmax;
    private float ymin;
    private float ymax;

    private float x;
    private float y;

    private int Width = Gdx.graphics.getWidth();;
    private int Height = Gdx.graphics.getHeight();

    private int camSpeedMax = 16;
    private float camAcceleration = 0.3f;
    private int camSpeedSmoother = 3;

    private float camVelocityX = 0;
    private float camVelocityY = 0;

    private float fZoomMax = 1f;
    private float fZoomMin = 0.5f;
    private float fZoomSpeed = 0.03f;

    public DotaCamera() {
        this(0, 0, 0, 0);
    }

    public DotaCamera(float xmin, float xmax, float ymin, float ymax) {
        super();
        setBounds(xmin, xmax, ymin, ymax);
    }

    public void setBounds(float xmin, float xmax, float ymin, float ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public void setPosition(float x, float y) {
        setPosition(x, y, 0);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        this.x = x;
        this.y = y;
        fixBounds();
    }

    private void fixBounds() {
        if (position.x < xmin + viewportWidth / 2) {
            position.x = xmin + viewportWidth / 2;
        }
        if (position.x > xmax - viewportWidth / 2) {
            position.x = xmax - viewportWidth / 2;
        }
        if (position.y < ymin + viewportHeight / 2) {
            position.y = ymin + viewportHeight / 2;
        }
        if (position.y > ymax - viewportHeight / 2) {
            position.y = ymax - viewportHeight / 2;
        }
    }

    /**
     * Controls the zoom of the of the camera.
     */
//    public void updateZoom() {
//        int mouseWheelMovement = Mouse.getDWheel();
//        if (mouseWheelMovement > 0) {
//            if (this.zoom > fZoomMin) {
//                this.zoom -= fZoomSpeed;
//            } else {
//                this.zoom = fZoomMin;
//            }
//        }else if(mouseWheelMovement < 0){
//            if (this.zoom < fZoomMax) {
//                this.zoom += fZoomSpeed;
//            } else {
//                this.zoom = fZoomMax;
//            }
//        }
//    }

    /**
     * Update And move the Camera DOTA Stylized movement.
     */
    public void updateAndMove() {
        float dt = Gdx.graphics.getDeltaTime();

        int MouseX = Gdx.input.getX(); // Get MouseX
        int MouseY = Gdx.input.getY(); // Get MouseY

        int camSpeedX = 0;
        int camSpeedY = 0;

        String horizontalDirection = getMoveLeftRight(MouseX); // Get
                                                                // horizontalDirection
        String verticalDirection = getMoveUpDown(MouseY); // Get
                                                            // verticalDirection

        /* * * * * * * *
         * Decide what to do with the horizontalDirection.
         */
        switch (horizontalDirection) {
        case "left":
            camSpeedX = ((Width / 2) - (MouseX + (Width / 4)))
                    / camSpeedSmoother; // Create Speed -X

            camSpeedX = ((camSpeedX > camSpeedMax) ? camSpeedMax : camSpeedX); // Limit
                                                                                // the
                                                                                // speed.
            if (camVelocityX < camSpeedX)
                camVelocityX += camAcceleration;
            break;
        case "right":
            camSpeedX = (((MouseX + (Width / 4)) - ((Width / 4) * 3)) - (Width / 4))
                    / camSpeedSmoother; // Create speed +X.

            camSpeedX = ((camSpeedX > camSpeedMax) ? camSpeedMax : camSpeedX); // Limit
                                                                                // the
                                                                                // speed.

            if (camVelocityX < camSpeedX)
                camVelocityX += camAcceleration; // Accelerate

            camSpeedX *= -1; // To negate the speed.
            break;
        case "":
            camVelocityX = 0;
            break;
        }

        /* * * * * * * *
         * Decide what to do with the verticalDirection.
         */
        switch (verticalDirection) {
        case "up":
            camSpeedY = (Height / 4) - MouseY; // Create speed -Y

            camSpeedY = ((camSpeedY > camSpeedMax) ? camSpeedMax : camSpeedY); // Limit
                                                                                // the
                                                                                // speed.

            if (camVelocityY < camSpeedY)
                camVelocityY += camAcceleration;

            camSpeedY *= -1;
            break;
        case "down":
            camSpeedY = (((MouseY + (Height / 4)) - ((Height / 4) * 3)) - (Height / 4))
                    / camSpeedSmoother; // Create speed +Y.

            camSpeedY = ((camSpeedY > camSpeedMax) ? camSpeedMax : camSpeedY); // Limit
                                                                                // the
                                                                                // speed.
            if (camVelocityY < camSpeedY)
                camVelocityY += camAcceleration;
            break;
        case "":
            camVelocityY = 0;
            break;
        }

        // System.out.println("vX:" +camVelocityX+ "vY: " +camVelocityY+ "sX: "
        // +camSpeedX+ "sY: " +camSpeedY);

        this.position.x -= (camVelocityX * camSpeedX) * dt;
        this.position.y -= (camVelocityY * camSpeedY) * dt;
        this.update();
    }

    /**
     * Get the X-Axial Direction.
     * 
     * @param MouseX
     * @return Direction
     */
    private String getMoveLeftRight(int MouseX) {
        if (MouseX + (Width / 4) < Width / 2) {// Needs to move left?
            return "left";
        } else if (MouseX > (Width / 4) * 3) {// Needs to move right?
            return "right";
        }
        return "";
    }

    /**
     * Get the Y-Axial Direction.
     * 
     * @param MouseY
     * @return Direction
     */
    private String getMoveUpDown(int MouseY) {
        if (MouseY < Height / 4) {// Needs to move up?
            return "up";
        } else if (MouseY > (Height / 4) * 3) {// Needs to move down?
            return "down";
        }
        return "";
    }
}
