public class Score implements Comparable<Score> {
    // Fields for the player name and time in seconds
    private String name;
    private long time;

    // Constructor to initialize the score with a name and time
    public Score(String name, long time) {
        this.name = name;
        this.time = time;
    }

    // Getter methods for the name and time fields
    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    // Override the compareTo method of the Comparable interface to compare scores based on their time
    @Override
    public int compareTo(Score other) {
        return Long.compare(time, other.time);
    }
}