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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MemoryAdapter adapter;
    private List<MemoryCard> cards;
    private TextView tvScore; // ✅ Added TextView for score tracking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        tvScore = findViewById(R.id.tvScore); // ✅ Find the score TextView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Initialize game
        setupGame();
    }

    private void setupGame() {
        cards = new ArrayList<>();

        // Add pairs of cards (Example images)
        int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3,
                R.drawable.img4, R.drawable.img5, R.drawable.img6};

        for (int img : images) {
            cards.add(new MemoryCard(img));
            cards.add(new MemoryCard(img));
        }

        // Shuffle cards
        Collections.shuffle(cards);

        // Setup adapter with game end listener & send tvScore
        adapter = new MemoryAdapter(this, cards, tvScore, this::showGameEndDialog);
        recyclerView.setAdapter(adapter);
    }

    // Show dialog when game is over
    private void showGameEndDialog() {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Game Over 🎉");
            builder.setMessage("Congratulations! You matched all cards.");
            builder.setCancelable(false);

            // Reset button
            builder.setPositiveButton("Reset", (dialog, which) -> {
                setupGame(); // Restart game
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
