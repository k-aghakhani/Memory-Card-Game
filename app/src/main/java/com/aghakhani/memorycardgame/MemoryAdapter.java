package com.aghakhani.memorycardgame;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private Context context;
    private List<MemoryCard> cards;
    private int flippedIndex = -1; // Stores the index of the first flipped card
    private OnGameEndListener gameEndListener; // Listener for game end

    // Interface for handling game completion
    public interface OnGameEndListener {
        void onGameEnd();
    }

    public MemoryAdapter(Context context, List<MemoryCard> cards, OnGameEndListener gameEndListener) {
        this.context = context;
        this.cards = cards;
        this.gameEndListener = gameEndListener;
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
                    // ✅ Cards match, keep them flipped
                    previousCard.setMatched(true);
                    card.setMatched(true);
                } else {
                    // ❌ Cards do not match, flip both back after 1 second
                    new Handler().postDelayed(() -> {
                        previousCard.setFlipped(false);
                        card.setFlipped(false);
                        notifyDataSetChanged();
                    }, 1000);
                }
                flippedIndex = -1;

                // Check if all cards are matched
                if (isGameOver()) {
                    gameEndListener.onGameEnd();
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