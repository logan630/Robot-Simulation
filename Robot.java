package me.potts.robots;

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

    // This is the single worst... THING I've ever written in Java
    // DO NOT use this as an example of how to do anything, this is garbage
    // It took me forever to figure out the logical way to do this, and when
    // it came time to address edge cases, I was so burned out I just
    // through together a ton of IF statements to handle them all
    // This is bad. It's bad. I don't like it, and neither should you.
    public void drive(int x, int y) throws
            PositionOutOfBoundsException,
            ObstructedPathException
    {
        if(x < 0 || y < 0 || x > this.field.getColumns() - 1 || y > this.field.getRows() - 1) {
            throw new PositionOutOfBoundsException("Path must be contained within the field.");
        }

        int lineX = this.x;
        int lineY = this.y;

        // This is a stupidly inefficient way to account for these edge cases but it's been months and I'm tired
        boolean horizontal = false;
        boolean vertical = false;

        if(y - this.y == 0){
            vertical = true;
        }

        if(x - this.x == 0) {
            horizontal = true;
        }

        // The robot will enter new cells in a certain order according to the slope of the vector dictating its translation
        // Let m be the slope in question. Then floor(m) will be the number of cells the robot enters when travelling in the-
        // -y direction before entering another cell along the x axis.

        double m;

        try {
            m = (y - this.y) / (x - this.x);
        } catch(ArithmeticException e) {
            m = 0;
        }
        boolean xIncrement = false;

        if(m < 1) {
            m = 1/m;
            xIncrement = true;
        }

        m = Math.floor(m);

        if(horizontal) {
            for(;lineX < x; lineX++) {
                this.field.placeRobot(lineX, lineY);
            }
        }

        else if(vertical) {
            for(; lineY < y; lineY++) {
                this.field.placeRobot(lineX, lineY);
            }
        }

        else {
            while (lineX < x-1 && lineY < y-1) {

                double placeholderM = m;
                while (placeholderM >= 1) {

                    if (xIncrement) {
                        if(lineX == x) {
                            break;
                        }
                        if(!(lineX == this.x && lineY == this.y)) {
                            this.field.placeRobot(++lineX, lineY);
                        }
                    } else {
                        if(lineY == y){
                            break;
                        }
                        if(!(lineX == this.x && lineY == this.y)) {
                            this.field.placeRobot(lineX, ++lineY);
                        }
                    }
                }

                if (xIncrement) {
                    if(lineY == y) {
                        break;
                    }
                    if(!(lineX == this.x && lineY == this.y)) {
                        this.field.placeRobot(lineX, ++lineY);
                    }
                } else {
                    if(lineX == x) {
                        break;
                    }
                    if(!(lineX == this.x && lineY == this.y)) {
                        this.field.placeRobot(++lineX, lineY);
                    }
                }

                placeholderM = m;

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
