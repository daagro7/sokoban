package es.upm.pproject.sokoban.models;

/**
 * Represents a state in the game, change in position of the player and box if
 * any.
 */
public class State {
    private PosChange stateWMan;
    private PosChange stateBox;

    /**
     * Constructs a State with only the player position change.
     *
     * @param posWMan position change of the player
     */
    public State(PosChange posWMan) {
        this.stateWMan = posWMan;
        this.stateBox = null;
    }

    /**
     * Constructs a State with the player position change and the box position
     * change.
     *
     * @param posWMan position change of the player
     * @param posBox  position change of the box
     */
    public State(PosChange posWMan, PosChange posBox) {
        this.stateWMan = posWMan;
        this.stateBox = posBox;
    }

    /**
     * Returns the position change of the player.
     *
     * @return position change of the player
     */
    public PosChange getWMan() {
        return stateWMan;
    }

    /**
     * Returns the position change of the box.
     *
     * @return position change of the box
     */
    public PosChange getBox() {
        return stateBox;
    }
}
