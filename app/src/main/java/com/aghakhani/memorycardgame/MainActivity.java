package com.aghakhani.memorycardgame;

import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MemoryAdapter adapter;
    private List<MemoryCard> cards;
    private TextView tvScore; // TextView for displaying score
    private MediaPlayer gameOverSound, winSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        tvScore = findViewById(R.id.tvScore); // Get the score TextView
        gameOverSound = MediaPlayer.create(this, R.raw.lose_sound);
        winSound = MediaPlayer.create(this, R.raw.win_sound);

        // Show difficulty selection dialog when the activity starts
        showDifficultyDialog();
    }

    // Show dialog to select difficulty level
    private void showDifficultyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Difficulty Level");
        String[] levels = {"Easy (6 cards)", "Medium (12 cards)", "Hard (18 cards)"};
        builder.setItems(levels, (dialog, which) -> {
            int pairCount; // Number of pairs to generate
            int rowCount; // Number of rows in the grid (calculated)

            switch (which) {
                case 0: // Easy
                    pairCount = 3; // 3 pairs = 6 cards
                    rowCount = 2; // 6 cards / 3 columns = 2 rows
                    break;
                case 1: // Medium
                    pairCount = 6; // 6 pairs = 12 cards
                    rowCount = 4; // 12 cards / 3 columns = 4 rows
                    break;
                case 2: // Hard
                    pairCount = 9; // 9 pairs = 18 cards
                    rowCount = 6; // 18 cards / 3 columns = 6 rows
                    break;
                default:
                    pairCount = 6; // Default to Medium
                    rowCount = 4;
            }

            // Set up the grid with a fixed 3-column layout
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            // Start the game with the chosen number of pairs
            setupGame(pairCount);
        });
        builder.setCancelable(false); // Prevent closing the dialog without selection
        builder.show();
    }

    private void setupGame(int pairCount) {
        cards = new ArrayList<>();
        // Add pairs of cards (Example images)
        int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3,
                R.drawable.img4, R.drawable.img5, R.drawable.img6,
                R.drawable.img7, R.drawable.img8, R.drawable.img9}; // Ensure you have enough images
        for (int i = 0; i < pairCount; i++) {
            int imageIndex = i % images.length; // Cycle through images if needed
            cards.add(new MemoryCard(images[imageIndex]));
            cards.add(new MemoryCard(images[imageIndex]));
        }
        // Shuffle cards
        Collections.shuffle(cards);

        // Setup adapter with game end listener & send tvScore
        adapter = new MemoryAdapter(this, cards, tvScore, this::handleGameEnd);
        recyclerView.setAdapter(adapter);
    }

    // Handle game end based on win or lose
    private void handleGameEnd(boolean isWin) {
        if (isWin) {
            showWinDialog();
        } else {
            showLoseDialog();
        }
    }

    // Show win dialog
    private void showWinDialog() {
        runOnUiThread(() -> {
            winSound.start(); // Play win sound
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Congratulations 🎉");
            builder.setMessage("You matched all cards! Well done.");
            builder.setCancelable(false);

            // Reset button
            builder.setPositiveButton("Reset", (dialog, which) -> {
                showDifficultyDialog(); // Show difficulty dialog again to restart
            });

            // Exit button
            builder.setNegativeButton("Exit", (dialog, which) -> {
                finish(); // Close the app
            });

            // Show dialog
            builder.show();
        });
    }

    // Show lose dialog
    private void showLoseDialog() {
        runOnUiThread(() -> {
            gameOverSound.start(); // Play lose sound
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Game Over ❌");
            builder.setMessage("You lost! Try again.");
            builder.setCancelable(false);

            // Reset button
            builder.setPositiveButton("Reset", (dialog, which) -> {
                showDifficultyDialog(); // Show difficulty dialog again to restart
            });

            // Exit button
            builder.setNegativeButton("Exit", (dialog, which) -> {
                finish(); // Close the app
            });

            // Show dialog
            builder.show();
        });
    }
}