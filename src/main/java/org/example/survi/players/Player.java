//package org.example.survi.players;
//
//import org.example.message.JsonMessage;
//import org.example.survi.GameComponent;
//import org.example.survi.utils.ContinuousCoordinate;
//import org.json.JSONObject;
//
//public class Player implements GameComponent {
//    private final int playerId;
//    private ContinuousCoordinate position;
//    private int health;
//    private final int defaultHealth;
//    private final int maxHealth;
//    private final double hitboxX;
//    private final double hitboxY;
//
//    // constructors
//
//    public Player(int playerId,
//                  ContinuousCoordinate position,
//                  int maxHealth,
//                  int defaultHealth,
//                  int initialHealth,
//                  double hitboxX,
//                  double hitboxY) {
//        this.playerId = playerId;
//        this.position = position;
//        this.maxHealth = maxHealth;
//        this.defaultHealth = defaultHealth;
//        this.health = initialHealth;
//        this.hitboxX = hitboxX;
//        this.hitboxY = hitboxY;
//    }
//
//    public int getPlayerId() {
//        return this.playerId;
//    }
//
//    public void move(ContinuousCoordinate directionVector) {
//        position.translate(directionVector);
//    }
//
//    public void resetHealth() {
//        this.health = defaultHealth;
//    }
//
//    public void changeHealth(int deltaHealth) throws HealthOutOfBoundException{
//        int newHealth = this.health + deltaHealth;
//        if(newHealth < 0 || newHealth > this.maxHealth) {
//            throw new HealthOutOfBoundException("Health out of bound");
//        }
//
//        this.health = newHealth;
//    }
//
//    @Override
//    public int hashCode() {
//        return this.playerId;
//    }
//
//    @Override
//    public void update(long newTimeStamp) {
//
//    }
//
//    @Override
//    public void update(JsonMessage jsonMessage) {
//
//    }
//
//    @Override
//    public JSONObject getState(long timeStamp) {
//        return null;
//    }
//}
