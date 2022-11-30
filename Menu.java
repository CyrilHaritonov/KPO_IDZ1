import java.util.Objects;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;

    private static class MenuOption {
        interface menuOptionMethod {
            void invokable();
        }

        private final String text;
        private final int key;
        private final menuOptionMethod methodToInvoke;

        MenuOption(int key, String text, menuOptionMethod menuOptionMethod) {
            this.key = key;
            this.text = text;
            this.methodToInvoke = menuOptionMethod;
        }
    }

    MenuOption[] options = {new MenuOption(1, "Начать игру против бота на лёгком уровне сложности",
            () -> new GameSession(new RealPlayer("черный"), new EasyAIPLayer("белый"), scanner, false)),
            new MenuOption(2, "Начать игру в режиме игрок против игрока",
                    () -> new GameSession(new RealPlayer("черный"), new RealPlayer("белый"), scanner, false)),
            new MenuOption(3, "Начать игру против бота на лёгком уровне сложности в режиме с оценкой возможных ответных ходов противника",
                    () -> new GameSession(new RealPlayer("черный"), new EasyAIPLayer("белый"), scanner, true)),
            new MenuOption(4, "Начать игру в режиме игрок против игрока с оценкой возможных ответных ходов противника",
                    () -> new GameSession(new RealPlayer("черный"), new RealPlayer("белый"), scanner, true)),
            new MenuOption(5, "Посмотреть наилучший результат", Leaderboard::printLeaderboard),
            new MenuOption(6, "Закрыть игру", () -> System.exit(0))};

    Menu() {
        this.scanner = new Scanner(System.in);
        while (true) {
            for (MenuOption option : options) {
                System.out.println(option.key + ". " + option.text);
            }
            System.out.println("Введите номер нужного пункта меню:");
            String input = this.scanner.next();
            for (MenuOption option : options) {
                if (Objects.equals(input, "stop")) {
                    System.exit(0);
                }
                try {
                    if (Integer.parseInt(input) == option.key) {
                        option.methodToInvoke.invokable();
                        break;
                    }
                } catch (NumberFormatException message) {
                    break;
                }
            }
        }
    }
}
