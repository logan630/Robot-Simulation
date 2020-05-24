package me.potts.robots;

public class Field {

    private FieldObjects[][] map;
    private int rows;
    private int columns;

    // Generates a blank field
    Field(int rows, int columns) {

        FieldObjects[][] map = new FieldObjects[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                map[i][j] = FieldObjects.FREE_SPACE;
            }
        }

        this.rows = rows;
        this.columns = columns;
        this.map = map;

    }

    // Constructor that utilizes a preset map
    Field(FieldObjects[][] map) {
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }

    public FieldObjects[][] getMap() {
        return this.map;
    }

    public void setMap(FieldObjects[][] map) {
        this.map = map;
        this.rows = map.length;
        this.columns = map[0].length;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    // Adds an obstacle to the field in the given area
    public void addObstacle(int topX, int topY, int bottomX, int bottomY) throws
            PositionOutOfBoundsException,
            ObstructedPathException {

        if (topX < 0 || topY < 0 || bottomX > columns - 1 || bottomY > rows - 1) {
            throw new PositionOutOfBoundsException("Obstacle must be contained within the field.");
        }

        for (int i = topX; i <= bottomX; i++) {
            for (int j = topY; j <= bottomY; j++) {
                if (!this.map[j][i].equals(FieldObjects.FREE_SPACE)) {

                    throw new ObstructedPathException("Objects cannot override existing structures.");

                }

                map[j][i] = FieldObjects.OBSTACLE;

            }
        }

    }

    // Adds a movable object to the field at the given point
    public void addMovable(int x, int y) throws
            PositionOutOfBoundsException,
            ObstructedPathException
    {

        if(x < 0 || y < 0 || x > columns - 1 || y > rows - 1) {
            throw new PositionOutOfBoundsException("Movable Object must be contained within the field.");
        }

        if(!this.map[y][x].equals(FieldObjects.FREE_SPACE)) {
            throw new ObstructedPathException("Movable Object cannot override existing structures.");
        }

        this.map[y][x] = FieldObjects.MOVABLE_OBJECT;

    }

    // Adds a depot to the field at the given point
    public void addDepot(int x, int y) throws
            PositionOutOfBoundsException,
            ObstructedPathException
    {

        if(x < 0 || y < 0 || x > columns - 1 || y > rows - 1) {
            throw new PositionOutOfBoundsException("Depot must be contained within the field.");
        }

        if(!this.map[y][x].equals(FieldObjects.FREE_SPACE)) {
            throw new ObstructedPathException("Depot cannot override existing structures.");
        }

        this.map[y][x] = FieldObjects.DEPOT;

    }

    // Places a robot on the field at a given point
    public void placeRobot(int x, int y) throws
            PositionOutOfBoundsException,
            ObstructedPathException
    {

        // Ensures the robot is within the field
        if(x < 0 || y < 0 || x > columns - 1 || y > rows - 1) {
            throw new PositionOutOfBoundsException("Robot must be contained within the field.");
        }

        // Ensures the robot is being placed inside free space
        if(!this.map[y][x].equals(FieldObjects.FREE_SPACE)) {
            throw new ObstructedPathException("Robot cannot override existing structures.");
        }

        // If there is already a robot on the field, it is removed
        outer:
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {

                if(this.map[i][j].equals(FieldObjects.ROBOT)) {
                    this.map[i][j] = FieldObjects.FREE_SPACE;
                    break outer;
                }

            }
        }

        this.map[y][x] = FieldObjects.ROBOT;

    }

    private String getSymbol(FieldObjects object) {

        switch(object) {

            case FREE_SPACE:
                return "-";
            case OBSTACLE:
                return "X";
            case MOVABLE_OBJECT:
                return "M";
            case DEPOT:
                return "D";
            case ROBOT:
                return "R";

        }

        return "";

    }

    public String toString() {

        String image = "\t";

        // Prints a scale along the sides of the field as a coordinates system
        for(int i = 0; i < columns; i++) {
            image += "" + i + "\t";
        } image += "\n";

        for(int i = 0; i < rows; i++) {

            image += "" + i +"\t";

            for(int j = 0; j < columns; j++) {

                image += getSymbol(this.map[i][j]) + "\t";

            }

            image += "\n";

        }

        return image;

    }

}
