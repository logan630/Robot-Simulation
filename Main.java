package me.potts.robots;

public class Main {

    public static void main(String[] args) {

        Robot robot = preset1();
        System.out.println(robot.getField());
        try{
            robot.drive(0,2);
            robot.grab();
            robot.drive(1,3);
            robot.drive(3,1);
            robot.drive(3,0);
            robot.deposit();
            robot.endTask();
        } catch(ObstructedPathException e) {
            System.out.println(robot.getPosition()[0]);
            System.out.println(robot.getPosition()[1]);
            System.out.println("The robot encountered an obstacle.");
        } catch(PositionOutOfBoundsException e) {
            System.out.println("The robot attempted to leave the field.");
        }

//        Robot robot = preset2();
//        System.out.println(robot.getField());

    }

    public static Robot preset1() {

        Field field = new Field(4,4);
        try {
            field.addMovable(0,3);
            field.addObstacle(1,0,1,2);
            field.addObstacle(2,1,2,1);
            field.addDepot(2,0);
            field.addObstacle(3,3,3,3);
        } catch(PositionOutOfBoundsException e) {
            System.out.println("An object tried to generate out of bounds.");
        } catch(ObstructedPathException e) {
            System.out.println("Two objects overlapped.");
        }

        return new Robot(0,0,field);

    }

    public static Robot preset2() {

        Field field = new Field(20,20);
        try {
            field.addObstacle(0, 1, 10, 1);
            field.addObstacle(0,2,0,7);
            field.addMovable(1,2);
            field.addDepot(19,0);
            field.addObstacle(18,0,18,3);
            field.addObstacle(0,8,14,8);
            field.addObstacle(12,3,15,3);
            field.addObstacle(15,4,15,5);
            field.addObstacle(2,9,2,15);
            field.addMovable(1,10);
            field.addObstacle(16,5,19,5);
            field.addObstacle(16,6,16,15);
            field.addDepot(18,9);
            field.addDepot(4,10);
            field.addObstacle(3,12,6,12);
            field.addObstacle(6,16,16,16);
            field.addMovable(14,14);
            field.addObstacle(5,16,5,17);
        } catch(PositionOutOfBoundsException e) {
            System.out.println("An object tried to generate out of bounds.");
        } catch(ObstructedPathException e) {
            System.out.println("Two objects overlapped.");
        }

        return new Robot(0,0,field);

    }

}
