/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import com.badlogic.gdx.utils.PauseableThread;
import java.io.Serializable;
import javafx.animation.PauseTransition;

/**
 *
 * @author mads1
 */
public enum GameState implements Serializable{
    PAUSE, RUN, STOPPED, ROUNDEND
}
