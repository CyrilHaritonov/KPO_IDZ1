import java.util.List;
import java.util.Objects;
import java.util.Scanner;

abstract class Player {
    String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    Player(String color) {
        this.setColor(color);
    }

    abstract void getNextMove(Field currentField, Scanner scanner);
}

class RealPlayer extends Player {
    RealPlayer(String color) {
        super(color);
    }

    @Override
    void getNextMove(Field currentField, Scanner scanner) {
        // TODO надо проеерять есть ли рядом хоть одна из фишек противника, фишка должна ставиться так, чтобы хотя бы одна из фишек противника
        System.out.println("Введите координаты точки, в которую хотите совершить ход через пробел в формате x y(игрок " + this.getColor() + "):");
        int[] coordinates;
        while (true) {
            try {
                coordinates = new int[]{Integer.parseInt(scanner.next()), Integer.parseInt(scanner.next())};
            }
            catch (NumberFormatException a) {
                System.out.println("Неверные координаты клетки!");
                continue;
            }
            if (!(coordinates[0] > 0 && coordinates[0] < 9) || !(coordinates[1] > 0 && coordinates[1] < 9)) {
                System.out.println("Неверные координаты клетки!");
            } else {
                break;
            }
        }
        String move = currentField.playerMove(currentField.getCell(coordinates[1], coordinates[0]), this.getColor());
        if (!Objects.equals(move, "Ок!")) {
            getNextMove(currentField, scanner);
        }
        System.out.println(move);
    }

    ;
}

class EasyAIPLayer extends Player {
    EasyAIPLayer(String color) {
        super(color);
    }

    @Override
    void getNextMove(Field currentField, Scanner scanner) {
        System.out.println("Противник (лёгкий бот) делает ход...");
        String move = currentField.AIMove(this.getColor());
        System.out.println(move);
    }
}
