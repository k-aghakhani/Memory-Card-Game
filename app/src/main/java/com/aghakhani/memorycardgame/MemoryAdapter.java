package com.aghakhani.memorycardgame;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private Context context;
    private List<MemoryCard> cards;
    private int flippedIndex = -1; // Stores the index of the first flipped card
    private int score = 100; // ✅ Start score at 100
    private TextView tvScore; // ✅ Score TextView
    private OnGameEndListener gameEndListener; // Listener for game end

    // Interface for handling game completion
    public interface OnGameEndListener {
        void onGameEnd(boolean isWin);
    }

    // ✅ Modified constructor to receive tvScore
    public MemoryAdapter(Context context, List<MemoryCard> cards, TextView tvScore, OnGameEndListener gameEndListener) {
        this.context = context;
        this.cards = cards;
        this.tvScore = tvScore;
        this.gameEndListener = gameEndListener;
        updateScore(0); // Initialize score display
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemoryCard card = cards.get(position);

        // Show front image if flipped, otherwise show back
        holder.imgCard.setImageResource(card.isFlipped() ? card.getImageId() : R.drawable.card_back);

        holder.itemView.setOnClickListener(v -> {
            // Ignore clicks on matched or already flipped cards
            if (card.isMatched() || card.isFlipped()) return;

            // Flip the selected card
            card.setFlipped(true);
            notifyItemChanged(position);

            if (flippedIndex == -1) {
                // First card is selected
                flippedIndex = position;
            } else {
                // Second card is selected
                MemoryCard previousCard = cards.get(flippedIndex);

                if (previousCard.getImageId() == card.getImageId()) {
                    // ✅ Correct match: +50 points
                    previousCard.setMatched(true);
                    card.setMatched(true);
                    updateScore(50);
                } else {
                    // ❌ Incorrect match: -30 points (After delay)
                    new Handler().postDelayed(() -> {
                        previousCard.setFlipped(false);
                        card.setFlipped(false);
                        notifyDataSetChanged();
                        updateScore(-30);

                        // If score reaches 0, show loss dialog
                        if (score == 0) {
                            gameEndListener.onGameEnd(false); // Lose condition
                        }
                    }, 1000);
                }
                flippedIndex = -1;

                // Check if all cards are matched
                if (isGameOver()) {
                    gameEndListener.onGameEnd(true); // Win condition
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.imgCard);
        }
    }

    // ✅ Method to update score
    private void updateScore(int points) {
        score += points;
        if (score < 0) score = 0; // Prevent negative score
        tvScore.setText("Score: " + score);
    }

    // Check if all cards are matched
    private boolean isGameOver() {
        for (MemoryCard card : cards) {
            if (!card.isMatched()) {
                return false;
            }
        }
        return true;
    }
}
