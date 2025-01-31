package com.aghakhani.memorycardgame;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvScore;
    private int score = 0;
    private List<MemoryCard> cardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        tvScore = findViewById(R.id.tvScore);

        int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6};
        cardList = new ArrayList<>();
        for (int img : images) {
            cardList.add(new MemoryCard(img));
            cardList.add(new MemoryCard(img));
        }
        Collections.shuffle(cardList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new MemoryAdapter(this, cardList, () -> {
            score += 10;
            tvScore.setText("امتیاز: " + score);
            if (score == images.length * 10) {
                Toast.makeText(this, "تبریک! شما برنده شدید!", Toast.LENGTH_LONG).show();
            }
        }));
    }
}