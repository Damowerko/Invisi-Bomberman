/**
 * This file holds an enumeration called Direction, which is used in TileObject.java to indicate the
 * direction an object should move after it collides with another object.
 * 
 * One can make a method take in or return a Direction (thus limiting the possible cases of the
 * input to the enum cases)
 */
public enum Direction {
    RIGHT(1,0), UP(0,1), LEFT(-1,0), DOWN(0,-1);
    private final int x, y;
    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int[] getVector(){
        return new int[]{x,y};
    }
}
