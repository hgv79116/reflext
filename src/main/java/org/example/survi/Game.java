package org.example.survi;

// a hard design choice is whether I should include information about time
// in the components.
//

import org.example.message.client_message.ClientMessage;
import org.example.survi.circle.Circle;
import org.example.survi.circle.CircleController;
import org.example.survi.player.Player;
import org.json.JSONObject;

public class Game extends GameStateBase{
    private final int NUM_CATEGORY = 10;
    private final int MAX_NUM_PLAYER = 2;
    private final long DURATION = 100000;
    private int numPlayer = 0;
    private final HashMapIndexedComponentStorage<Player> players = new HashMapIndexedComponentStorage<>();
    private final CircleController circleController = new CircleController(NUM_CATEGORY);
    private PlayerRemovedListener onPlayerRemoved;

    public Game(
                PlayerRemovedListener onPlayerRemoved) {
        this.onPlayerRemoved = onPlayerRemoved;
    }


    @Override
    public boolean hasEnded() {
        return super.hasEnded();
    }

    @Override
    public void start() {
        super.start();
        for(Player player: players) {
            player.start();
        }
        circleController.start();
    }

    @Override
    public void end() {
        super.end();
        for(Player player: players) {
            player.end();
        }
        circleController.end();
    }

    @Override
    public void update(long newTimeStamp) {
        long maxTimeStamp = startTime + DURATION;
        if(newTimeStamp > maxTimeStamp) {
            newTimeStamp = maxTimeStamp;
        }

        super.update(newTimeStamp);
        for(Player player: players) {
            player.update(newTimeStamp);
        }
        circleController.update(newTimeStamp);
    }

    @Override
    public void update(ClientMessage clientMessage) {
        super.update(clientMessage);

        assert (clientMessage.getHeader().getString("clientMessageCategory") == "GAME_ACTION");
        assert (clientMessage.getBody().getString("action") == "HIT");
        int circleId = clientMessage.getBody().getInt("circleId");
        int playerId = clientMessage.getBody().getInt("playerId");
        Circle circle = circleController.getCircleById(circleId);
        Player player = players.getInstanceById(playerId);
        player.hit(clientMessage.getTimeStamp(), circle.getCategory());
        circle.hit(clientMessage.getTimeStamp());
    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONPlayers = new JSONObject();
        for(Player player: players) {
            JSONPlayers.put(String.valueOf(player.getId()), player.getLastState());
        }

        JSONObject JSONContent = new JSONObject();
        JSONContent.put("players", JSONPlayers);
        JSONContent.put("circles", circleController.getLastState());
        return JSONContent;
    }

    private void removePlayer(int playerId) {
        players.removeInstanceById(playerId);
        onPlayerRemoved.onPlayerRemoved(playerId);
    }

    public boolean canAddNewPlayer() {
        return players.getNumInstances() < MAX_NUM_PLAYER;
    }

    public int addPlayer() {
        assert(startTime != -1);
        Player player = new Player(players.getNumInstances(), NUM_CATEGORY);
        players.addInstance(player);
        return player.getId();
    }
}
