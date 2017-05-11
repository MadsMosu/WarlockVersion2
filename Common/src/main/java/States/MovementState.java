/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import java.io.Serializable;

/**
 *
 * @author mads1
 */
public enum MovementState implements Serializable{
    STANDINGRIGHT, STANDINGLEFT, STANDINGUP, STANDINGDOWN, RUNNINGRIGHT, RUNNINGLEFT, RUNNINGUP, RUNNINGDOWN;
}
