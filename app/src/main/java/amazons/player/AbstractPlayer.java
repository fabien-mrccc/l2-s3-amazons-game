package amazons.player;

import amazons.board.Position;

import java.util.List;

public abstract class AbstractPlayer implements Player{
    protected PlayerID playerID;
    protected int boardWidth;
    protected int boardHeight;
    protected List<Position> initialPositions;

    @Override
    public void initialize(int boardWidth, int boardHeight,  PlayerID playerID, List<Position>[] initialPositions) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.playerID = playerID;
        this.initialPositions = initialPositions[playerID.index];
    }

    @Override
    public PlayerID getPlayerID() {
        return playerID;
    }

    @Override
    public boolean isGUIControlled() {
        return false;
    }

    @Override
    public abstract Move play(Move opponentMove);

    public void setPlayerID(PlayerID playerID){
        this.playerID = playerID;
    }
}