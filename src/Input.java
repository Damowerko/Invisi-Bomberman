public enum Input {
    UP, DOWN, LEFT, RIGHT, BOMB;
    public Direction getDirection(){
        switch(this){
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            case LEFT:
                return Direction.LEFT;
            case RIGHT:
                return Direction.RIGHT;
        }
        return null;
    }
}
