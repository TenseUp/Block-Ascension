import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HighScore {
    // Fields for the list of scores and the file name
    private ArrayList<Score> scores;
    private final String SCORES_FILE = "scores.json";

    // Constructor to initialize the high score list and load the scores from the file
    public HighScore() {
        scores = new ArrayList<>();
        loadScores();
    }

    // Method to get the list of scores
    public ArrayList<Score> getScores() {
        return scores;
    }

    // Method to add a new score to the list and save it to the file
    public void addScore(Score score) {
        // Add the score to the list and sort it in descending order based on the time
        scores.add(score);
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score s1, Score s2) {
                return Long.compare(s2.getTime(), s1.getTime());
            }
        });

        // Only keep the top 5 scores
        if (scores.size() > 5) {
            scores.remove(scores.size() - 1);
        }

        //Save the updated scores to the file
        saveScores();
    }

    // Method to clear all scores from the list and the file
    public void clearScores() {
        // Clear the list of scores
        scores.clear();

        // Create an empty JSONArray and write it to the file to clear all scores
        JSONArray emptyArray = new JSONArray();

        try (FileWriter writer = new FileWriter(SCORES_FILE)) {
            writer.write(emptyArray.toJSONString());
        } catch (IOException e) {
            System.err.println("Error clearing scores: " + e.getMessage());
        }
    }

    // Method to load the scores from the file and add them to the list
    private void loadScores() {
        // Create a new JSON parser to parse the scores file
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(SCORES_FILE)) {
            // Parse the JSON array of scores from the file
            JSONArray scoreArray = (JSONArray) parser.parse(reader);

            // Iterate over each score object in the array and create a new Score object from its name and time values
            for (Object obj : scoreArray) {
                JSONObject scoreObj = (JSONObject) obj;
                String name = (String) scoreObj.get("name");
                long time = (long) scoreObj.get("time");
                scores.add(new Score(name, time));
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error loading scores: " + e.getMessage());
        }
    }

    // Method to save the current list of scores to the file
    private void saveScores() {
        // Create a new JSONArray and add a JSONObject for each score in the list
        JSONArray scoreArray = new JSONArray();

        for (Score score : scores) {
            JSONObject scoreObj = new JSONObject();
            scoreObj.put("name", score.getName());
            scoreObj.put("time", score.getTime());
            scoreArray.add(scoreObj);
        }

        try (FileWriter writer = new FileWriter(SCORES_FILE)) {
            // Write the JSONArray to the scores file
            writer.write(scoreArray.toJSONString());
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }
}