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
        currentField.getPossibleMoves(this.getColor()).forEach(cellWithR -> System.out.print("(" + cellWithR.getCell().getX() + " " + cellWithR.getCell().getY() + ") "));
        System.out.print("\n");
    }

    Player(String color) {
        this.setColor(color);
    }

    abstract Boolean getNextMove(GameSession currentSession, Scanner scanner);
}

class RealPlayer extends Player {
    RealPlayer(String color) {
        super(color);
    }

    @Override
    Boolean getNextMove(GameSession currentSession, Scanner scanner) {
        List<Field.CellWithR> possibleMoves = currentSession.getGamingField().getPossibleMoves(this.getColor());
        if (possibleMoves.size() == 0) {
            System.out.println("Нет возможных ходов для игрока " + this.getColor());
            return true;
        }
        System.out.print("Возможные ходы: ");
        printPossibleMoves(currentSession.getGamingField());
        System.out.println("Введите координаты точки, в которую хотите совершить ход через пробел в формате x y (игрок " + this.getColor() + ") или введите \"отмена\", чтобы отменить текущий ход:");
        int[] coordinates;
        try {
            String[] scannerValues = {scanner.next(), null};
            if (Objects.equals(scannerValues[0], "отмена")) {
                currentSession.CancelMove();
                scanner.reset();
                return false;
            } else {
                scannerValues[1] = scanner.next();
                coordinates = new int[]{Integer.parseInt(scannerValues[0]), Integer.parseInt(scannerValues[1])};
            }
        } catch (NumberFormatException error) {
            System.out.println("Неверные координаты клетки!");
            scanner.reset();
            return getNextMove(currentSession, scanner);
        }
        if (Field.checkForCell(possibleMoves.stream().map(Field.CellWithR::getCell).toList(), coordinates[0], coordinates[1])) {
            currentSession.getPreviousFields().push(currentSession.getGamingField().copyField());
            currentSession.getGamingField().paintCell(currentSession.getGamingField().getCell(coordinates[0], coordinates[1]), this.getColor());
        } else {
            System.out.println("Неверные координаты клетки!");
            getNextMove(currentSession, scanner);
        }
        return true;
    }
}

class EasyAIPLayer extends Player {
    EasyAIPLayer(String color) {
        super(color);
    }

    @Override
    Boolean getNextMove(GameSession currentSession, Scanner scanner) {
        System.out.println("Противник (лёгкий бот) делает ход...");
        List<Field.CellWithR> possibleMoves = currentSession.getGamingField().getPossibleMoves(this.getColor());
        if (possibleMoves.size() > 0) {
            possibleMoves.stream().reduce((firstCell, secondCell) -> firstCell.getR() > secondCell.getR() ? firstCell : secondCell)
                    .ifPresent(cellWithR -> {
                        cellWithR.getCell().setColor(this.getColor());
                        currentSession.getGamingField().paintCellsFromClosure(cellWithR.getCell(), this.getColor());
                        System.out.println("Противник (лёгкий бот) сделал ход (" + cellWithR.getCell().getX() + " " + cellWithR.getCell().getY() + ")");
                    });
        } else {
            System.out.println("Нет ходов для противника (лёгкий бот).");
        }
        return true;
    }
}
