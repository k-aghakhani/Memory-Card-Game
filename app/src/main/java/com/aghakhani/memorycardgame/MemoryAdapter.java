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

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private Context context;
    private List<MemoryCard> cards;
    private int flippedIndex = -1;
    private OnMatchListener matchListener;

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

        holder.imgCard.setImageResource(card.isFlipped() ? card.getImageId() : R.drawable.card_back);
        holder.itemView.setOnClickListener(v -> {
            if (card.isMatched() || card.isFlipped()) return;

            card.setFlipped(true);
            notifyItemChanged(position);

            if (flippedIndex == -1) {
                flippedIndex = position;
            } else {
                MemoryCard previousCard = cards.get(flippedIndex);
                if (previousCard.getImageId() == card.getImageId()) {
                    previousCard.setMatched(true);
                    card.setMatched(true);
                    matchListener.onMatchFound();
                } else {
                    previousCard.setFlipped(false);
                    card.setFlipped(false);
                }
                flippedIndex = -1;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return cards.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.imgCard);
        }
    }
}
