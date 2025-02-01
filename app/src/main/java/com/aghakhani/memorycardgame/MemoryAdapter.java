package com.aghakhani.memorycardgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.os.Handler;
import android.content.Context;
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
    private OnMatchListener matchListener;

    // Interface to notify when a match is found
    public interface OnMatchListener {
        void onMatchFound();
    }

    public MemoryAdapter(Context context, List<MemoryCard> cards, OnMatchListener matchListener) {
        this.context = context;
        this.cards = cards;
        this.matchListener = matchListener;
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

        // Show the front image if the card is flipped, otherwise show the back
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
                    matchListener.onMatchFound();
                } else {
                    // ❌ Cards do not match, flip both back after 1 second
                    new Handler().postDelayed(() -> {
                        previousCard.setFlipped(false);
                        card.setFlipped(false);
                        notifyDataSetChanged();
                    }, 1000); // Delay of 1 second before flipping back
                }
                flippedIndex = -1; // Reset flipped index
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
}
