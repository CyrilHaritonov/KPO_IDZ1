import java.util.List;
import java.util.Objects;
import java.util.Scanner;

abstract class Player {
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    void printPossibleMoves(Field currentField) {
        currentField.getPossibleMoves(this.getColor()).forEach(cellWithR -> {
            System.out.print("(" + cellWithR.getCell().getX() + " " + cellWithR.getCell().getY() + ") ");
        });
        System.out.print("\n");
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
        System.out.print("Возможные ходы: ");
        printPossibleMoves(currentField);
        System.out.println("Введите координаты точки, в которую хотите совершить ход через пробел в формате x y (игрок " + this.getColor() + "):");
        int[] coordinates;
        try {
            coordinates = new int[]{Integer.parseInt(scanner.next()), Integer.parseInt(scanner.next())};
        } catch (NumberFormatException a) {
            System.out.println("Неверные координаты клетки!");
            getNextMove(currentField, scanner);
            return;
        }
        List<Field.CellWithR> possibleMoves = currentField.getPossibleMoves(this.getColor());
        if (possibleMoves.size() == 0) {
            System.out.println("Нет возможных ходов для игрока " + this.getColor());
        }
        if (possibleMoves.stream().map(Field.CellWithR::getCell).anyMatch(cell -> cell.getX() == coordinates[0] && cell.getY() == coordinates[1])) {
            String move = currentField.paintCell(currentField.getCell(coordinates[0], coordinates[1]), this.getColor());
            if (!Objects.equals(move, "Ок!")) {
                System.out.println("Неверные координаты клетки!");
                getNextMove(currentField, scanner);
            }
        } else {
            System.out.println("Неверные координаты клетки!");
            getNextMove(currentField, scanner);
        }
    }
}

class EasyAIPLayer extends Player {
    EasyAIPLayer(String color) {
        super(color);
    }

    @Override
    void getNextMove(Field currentField, Scanner scanner) {
        System.out.println("Противник (лёгкий бот) делает ход...");
        List<Field.CellWithR> possibleMoves = currentField.getPossibleMoves(this.getColor());
        if (possibleMoves.size() > 0) {
            possibleMoves.stream().reduce((firstCell, secondCell) -> firstCell.getR() > secondCell.getR() ? firstCell : secondCell)
                    .ifPresent(cellWithR -> {
                        cellWithR.getCell().setColor(this.getColor());
                        currentField.paintCellsFromClosure(cellWithR.getCell(), this.getColor());
                        System.out.println("Противник (лёгкий бот) сделал ход (" + cellWithR.getCell().getX() + " " + cellWithR.getCell().getY() + ")");
                    });
        } else {
            System.out.println("Нет ходов для противника (лёгкий бот).");
        }
    }
}
