public class Menu {
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

    MenuOption[] options = {MenuOption(1, "Начать игру на лёгком уровне сложности", ),
            MenuOption(2, "Начать игру на продвинутом уровне сложности", ),
            MenuOption(3, "Начать игру в режиме игрок против игрока", ),
            MenuOption(4, "Посмотреть наилучший результат", )};

    Menu() {
        for (MenuOption option : options) {
            System.out.println(option.key + ". " + option.text);
        }
        System.out.println("Введите номер нужного пункта меню:");
        String input = System.console().readLine();
       for (MenuOption option: options) {
           if (Integer.parseInt(input) == option.key) {
                option.methodToInvoke.invokable();
                break;
           }
       }
    }
}
