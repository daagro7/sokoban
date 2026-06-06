package es.upm.pproject.sokoban.models;

public class State {
    private PosChange stateWMan;
    private PosChange stateBox;


    public State(PosChange posWMan) {
        this.stateWMan = posWMan;
        this.stateBox = null;
    }

    public State(PosChange posWMan, PosChange posBox) {
        this.stateWMan = posWMan;
        this.stateBox = posBox;
    }

    public PosChange getWMan() {
        return stateWMan;
    }

    public PosChange getBox() {
        return stateBox;
    }
}
