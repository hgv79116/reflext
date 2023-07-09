//package org.example.survi.players;
//
//import org.example.message.JsonMessage;
//import org.example.survi.GameComponent;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class PlayerList implements GameComponent {
//    private HashMap<Integer, Player> players;
//    private int maxPlayerNum;
//
//    public PlayerList(List<Player> playerList, int maxPlayerNum) {
//        for(Player player: playerList) {
//            players.put(player.getPlayerId(), player);
//        }
//    }
//
//    public void addPlayer(Player player) {
//        players.put(player.getPlayerId(), player);
//    }
//
//    public void removePlayer(int playerId) {
//        players.remove(playerId);
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
