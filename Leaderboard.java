class Leaderboard {
    private static int LeaderboardValue = -1;

    public static void setLeaderboardValue(int leaderboardValue) {
        if (leaderboardValue > LeaderboardValue) {
            LeaderboardValue = leaderboardValue;
        }
    }

    public static void printLeaderboard() {
        if (LeaderboardValue == -1) {
            System.out.println("На данный момент нет результатов!");
        } else {
            System.out.println("На данный момент лучший результат: " + LeaderboardValue + " очков");
        }
    }
}
