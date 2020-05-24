package me.potts.robots;

import javax.swing.text.Position;

public class Robot {

    private Field field;
    private int x = 0;
    private int y = 0;

    private boolean isHolding = false;
    private int heldScore = 0;
    private int accumulatedScore = 0;

    Robot() {}

    Robot(int x, int y, Field field) {

        this.x = x;
        this.y = y;
        try {
            this.field = field;
            field.placeRobot(x, y);
        } catch(PositionOutOfBoundsException e) {
            System.out.println("Your robot generated out of bounds.");
        } catch(ObstructedPathException e) {
            System.out.println("Your robot generated inside an obstacle.");
        }

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Field getField() {
        return this.field;
    }

    public boolean getIsHolding() {
        return this.isHolding;
    }

    public int getHeldScore() {
        return this.heldScore;
    }

    public int getAccumulatedScore() {
        return this.accumulatedScore;
    }

    public int[] getPosition() {
        return new int[]{this.x,this.y};
    }

    // TO DO: Implement LOS,
    // Determine a list of all grid squares the path will pass through,
    // Test if any of those squares are obstructed,
    // Move the robot if none are

    // UNFINISHED
    public void drive(int x, int y) throws
            PositionOutOfBoundsException,
            ObstructedPathException
    {
        if(x < 0 || y < 0 || x > this.field.getColumns() - 1 || y > this.field.getRows() - 1) {
            throw new PositionOutOfBoundsException("Path must be contained within the field.");
        }

        double lineX = this.x;
        double lineY = this.y;

        double deltaX = Math.abs(x - lineX);
        double deltaY = Math.abs(y - lineY);

        double cellTotal = (1 + deltaX + deltaY)*2;

        // Increments are set to half integers because lines connect
        // the centers of grid squares
        double incrementX = (x > this.x) ? 0.5 : -0.5;
        double incrementY = (y > this.y) ? 0.5 : -0.5;

        double error = deltaY - deltaX;

//        deltaX *= 2;
//        deltaY *= 2;

        for(; cellTotal > 0; --cellTotal) {

            if(!this.field.getMap()[(int) lineY][(int) lineX].equals(FieldObjects.FREE_SPACE)&&(lineX==this.x&&lineY==this.y)) {
                throw new ObstructedPathException("The robot hit an obstacle.");
            }

            if(error > 0) {

                lineY += incrementY;
                error += deltaX;

            } else {

                lineX += incrementX;
                error -= deltaY;

            }

        }

        this.x = x;
        this.y = y;
        this.field.placeRobot(x,y);

        System.out.println(this.field);

    }

    // Grabs any movable object adjacent to the robot
    // Only considers cells directly adjacent to the robot, not diagonally
    // Cells are selected in the following order: E, N, W, S
    // The robot can only hold one object at any given time
    // My efficient way of doing things with a loop didn't work so here's
    // the version where I check every case
    public void grab() {

        if(isHolding) {
            System.out.println("The robot can not hold more than one object at a time.");
            return;
        }

        if(this.x+1 < this.field.getColumns()) {
            if(this.field.getMap()[this.y][this.x+1].equals(FieldObjects.MOVABLE_OBJECT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y][this.x+1] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 1;
                this.isHolding = true;
                System.out.println(this.field);
                return;
            }
        }

        if(this.y+1 < this.field.getRows()) {
            if(this.field.getMap()[this.y+1][this.x].equals(FieldObjects.MOVABLE_OBJECT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y+1][this.x] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 1;
                this.isHolding = true;
                System.out.println(this.field);
                return;
            }
        }

        if(this.x-1 > 0) {
            if(this.field.getMap()[this.y][this.x-1].equals(FieldObjects.MOVABLE_OBJECT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y][this.x-1] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 1;
                this.isHolding = true;
                System.out.println(this.field);
                return;
            }
        }

        if(this.y-1 > 0) {
            if(this.field.getMap()[this.y-1][this.x].equals(FieldObjects.MOVABLE_OBJECT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y-1][this.x] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 1;
                this.isHolding = true;
                System.out.println(this.field);
                return;
            }
        }

        System.out.println("There are not any movable objects to grab.");

    }

    public void deposit() {

        if(!isHolding) {
            System.out.println("The robot is not holding anything.");
            return;
        }

        if(this.x+1 < this.field.getColumns()) {
            if(this.field.getMap()[this.y][this.x+1].equals(FieldObjects.DEPOT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y][this.x+1] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 0;
                this.isHolding = false;
                this.accumulatedScore += 1;
                System.out.println(this.field);
                return;
            }
        }

        if(this.y+1 < this.field.getRows()) {
            if(this.field.getMap()[this.y+1][this.x].equals(FieldObjects.DEPOT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y+1][this.x] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 0;
                this.isHolding = false;
                this.accumulatedScore += 1;
                System.out.println(this.field);
                return;
            }
        }

        if(this.x-1 > 0) {
            if(this.field.getMap()[this.y][this.x-1].equals(FieldObjects.DEPOT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y][this.x-1] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 0;
                this.isHolding = false;
                this.accumulatedScore += 1;
                System.out.println(this.field);
                return;
            }
        }

        if(this.y-1 > 0) {
            if(this.field.getMap()[this.y-1][this.x].equals(FieldObjects.DEPOT)) {
                FieldObjects[][] map = this.field.getMap();
                map[this.y-1][this.x] = FieldObjects.FREE_SPACE;
                Field field = new Field(map);
                this.field = field;
                this.heldScore = 0;
                this.isHolding = false;
                this.accumulatedScore += 1;
                System.out.println(this.field);
                return;
            }
        }

        System.out.println("There are not any depots around.");

    }

    public void endTask() {

        System.out.println(this.field);
        System.out.println("Final Score: "+this.accumulatedScore);
        System.exit(0);

    }

}
