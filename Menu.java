import java.util.Objects;
import java.util.Scanner;

public class Menu {

    Scanner scanner;

    public Scanner getScanner() {
        return scanner;
    }

    class MenuOption {
        interface menuOptionMethod {
            void invokable();
        }

        String text;
        int key;
        menuOptionMethod methodToInvoke;

        MenuOption(int key, String text, menuOptionMethod menuOptionMethod) {
            this.key = key;
            this.text = text;
            this.methodToInvoke = menuOptionMethod;
        }
    }

    MenuOption[] options = {new MenuOption(1, "Начать игру против бота на лёгком уровне сложности",
            () -> new GameSession(new RealPlayer("белый"), new EasyAIPLayer("черный"), scanner)),
//            MenuOption(2, "Начать игру на продвинутом уровне сложности", ),
            new MenuOption(3, "Начать игру в режиме игрок против игрока", () -> new GameSession(new RealPlayer("белый"), new RealPlayer("черный"), scanner))};
//            MenuOption(4, "Посмотреть наилучший результат", )};

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
                if (Integer.parseInt(input) == option.key) {
                    option.methodToInvoke.invokable();
                    break;
                }
            }
        }
        //scanner.close();
    }
}
