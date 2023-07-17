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
    private final int MIN_NUM_PLAYER = 1;
    private final long DURATION = 5000;
    private final HashMapIndexedComponentStorage<Player> players = new HashMapIndexedComponentStorage<>();
    private final CircleController circleController = new CircleController(NUM_CATEGORY);

    public Game() {
    }

    public boolean canStart() {
        return players.getNumInstances() >= MIN_NUM_PLAYER && players.getNumInstances() <= MAX_NUM_PLAYER;
    }

    @Override
    public void start(long timeStamp) {
        super.start(timeStamp);
        for(Player player: players) {
            player.start(timeStamp);
        }
        circleController.start(timeStamp);
    }

    @Override
    public void end(long timeStamp) {
        super.end(timeStamp);
        for(Player player: players) {
            player.end(timeStamp);
        }
        circleController.end(timeStamp);
    }

    @Override
    public void update(long newTimeStamp) {
        System.out.println("jflsdjljflsjflk");
        long maxTimeStamp = getStartTime() + DURATION;
        if(newTimeStamp > maxTimeStamp) {
            newTimeStamp = maxTimeStamp;
        }

        for(Player player: players) {
            player.update(newTimeStamp);
        }
        circleController.update(newTimeStamp);

        super.update(newTimeStamp);

        if(newTimeStamp >= maxTimeStamp) {
            end(newTimeStamp);
        }
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


    public boolean canAddPlayer() {
        return !hasStarted() && players.getNumInstances() < MAX_NUM_PLAYER;
    }

    public int addPlayer(JSONObject userInfo) {
        assert(!hasStarted());
        Player player = new Player(players.getNumInstances(), NUM_CATEGORY, userInfo);
        players.addInstance(player);
        return player.getId();
    }
}
