package com.multimed.minesweeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnBoxClickListener {
    RecyclerView boardRecyclerView;
    BoardAdapter boardAdapter;
    Game game;
    TextView smiley, timer, flagsCount;
    ImageView bombSelected;
    CountDownTimer countDownTimer;
    int secondsElapsed, bombselector;
    int[] bombs = {R.drawable.goblin_bomb_cut, R.drawable.bomb_full, R.drawable.spirit_bomb_full, R.drawable.darkbomb_full};
    boolean timerStarted;
    MediaPlayer background_music;
    MediaPlayer defeat_music;
    MediaPlayer victory_music;
    int bombDrawable = R.drawable.goblin_bomb_cut;
    private Activity thisActivity = this;
    private int size = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smiley = findViewById(R.id.activiy_main_smiley);
        smiley.setOnClickListener(view -> {
                createNewGame(10,10);
        });

        bombSelected = findViewById(R.id.activity_main_bomb);
        bombselector = 0;


        timer = findViewById(R.id.activity_main_timer);

        flagsCount = findViewById(R.id.activiy_main_flagsleft);

        background_music = MediaPlayer.create(MainActivity.this, R.raw.bgrntheme);
        background_music.seekTo(0);
        background_music.start();
        background_music.setLooping(true);

        defeat_music = MediaPlayer.create(MainActivity.this, R.raw.defeat);
        victory_music = MediaPlayer.create(MainActivity.this, R.raw.victory_theme);

        timerStarted = false;
        countDownTimer = new CountDownTimer(999000L, 1000) {
            @Override
            public void onTick(long currentMillis) {
                secondsElapsed += 1;
                timer.setText(String.format("%03d", secondsElapsed));
            }

            @Override
            public void onFinish() {

            }
        };

        boardRecyclerView = findViewById(R.id.activity_main_grid);
        boardRecyclerView.setLayoutManager(new GridLayoutManager(this, 10));
        game = new Game(10, 10);
        boardAdapter = new BoardAdapter(game.getBoard().getBoxes(), this, bombDrawable);
        boardRecyclerView.setAdapter(boardAdapter);
        flagsCount.setText(String.format("%03d", game.getNumberOfBombs() - game.getFlagCount()));

        bombSelected.setOnClickListener(view -> {
            if (bombselector == 3) {
                bombselector = 0;
            } else {
                bombselector++;
            }
            bombDrawable = bombs[bombselector];
            bombSelected.setImageResource(bombDrawable);
            boardAdapter = new BoardAdapter(game.getBoard().getBoxes(), this, bombDrawable);
            boardRecyclerView.setAdapter(boardAdapter);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.ab_submenu) {

            View menuItemView = findViewById(R.id.ab_submenu);
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_instructions:
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Instrucciones");
                            alertDialog.setMessage("Para poner una bandera manten pulsada la casilla durante 3sec");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            break;
                        case R.id.menu_new_game:
                                createNewGame(size, (int) Math.floor(size*size/10));
                            break;
                        case R.id.menu_change_level:
                            String level = "Normal";
                            if (size == 10) {
                                level = "Dificil";
                                size = 12;
                            } else if (size == 12) {
                                level = "Facil";
                                size = 7;
                            } else {
                                level = "Normal";
                                size = 10;
                            }
                            createNewGame(size, (int) Math.floor(size*size/10));
                            Toast.makeText(thisActivity.getApplicationContext(), "Jugando en modo " + level, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.menu_exit:
                            defeat_music.pause();
                            victory_music.pause();
                            background_music.pause();
                            thisActivity.finish();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
            popupMenu.inflate(R.menu.game_menu);
            popupMenu.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBoxClick(Box box) {
        game.handleBoxClick(box);

        flagsCount.setText(String.format("%03d", game.getNumberOfBombs() - game.getFlagCount()));

        if (!timerStarted) {
            countDownTimer.start();
            timerStarted = true;
        }

        if (game.isGameOver()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Perdiste Pelotudo", Toast.LENGTH_SHORT).show();
            game.getBoard().revBombs();
            background_music.pause();
            defeat_music.seekTo(0);
            defeat_music.start();
        } else if (game.isGameWon()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Ganaste!! No me lo creo..", Toast.LENGTH_SHORT).show();
            background_music.stop();
            game.getBoard().revBombs();
            background_music.pause();
            victory_music.seekTo(0);
            victory_music.start();
        }

        boardAdapter.setBoxes(game.getBoard().getBoxes());
    }

    public void onBoxLongClick(Box box) {
        game.handleBoxLongClick(box);

        flagsCount.setText(String.format("%03d", game.getNumberOfBombs() - game.getFlagCount()));

        if (game.isGameOver()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Perdiste Pelotudo", Toast.LENGTH_SHORT).show();
            game.getBoard().revBombs();
        } else if (game.isGameWon()) {
            countDownTimer.cancel();
            Toast.makeText(getApplicationContext(), "Ganaste!! No me lo creo..", Toast.LENGTH_SHORT).show();
            game.getBoard().revBombs();
        }

        boardAdapter.setBoxes(game.getBoard().getBoxes());
    }

    public void createNewGame(int size, int numBoms){
        game = new Game(size, numBoms);
        boardAdapter.setBoxes(game.getBoard().getBoxes());
        timerStarted = false;
        countDownTimer.cancel();
        secondsElapsed = 0;
        timer.setText(R.string.default_count);
        flagsCount.setText(String.format("%03d", game.getNumberOfBombs() - game.getFlagCount()));
        defeat_music.pause();
        background_music.seekTo(0);
        background_music.start();
    }
}