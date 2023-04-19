public class Score implements Comparable<Score> {
    private String name;
    private long time;

    public Score(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int compareTo(Score other) {
        return Long.compare(time, other.time);
    }
}

