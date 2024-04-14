package com.example.plswork;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button[][] buttons = new Button[5][5];

    Button btn1_1;
    Button btn1_2;
    Button btn1_3;
    Button btn1_4;
    Button btn1_5;

    Button btn2_1;
    Button btn2_2;
    Button btn2_3;
    Button btn2_4;
    Button btn2_5;

    Button btn3_1;
    Button btn3_2;
    Button btn3_3;
    Button btn3_4;
    Button btn3_5;

    Button btn4_1;
    Button btn4_2;
    Button btn4_3;
    Button btn4_4;
    Button btn4_5;

    Button btn5_1;
    Button btn5_2;
    Button btn5_3;
    Button btn5_4;
    Button btn5_5;

    Button reset;

    static int[][] currentstate = new int[5][5];
    int player_coin = 1;
    int playerColor = android.R.color.holo_purple;
    boolean hasWon = false;

    TextView current_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ActionBar b = getSupportActionBar();
        assert b != null;
        b.hide();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                String temp = "btn_" + (row+1) + "_" + (col+1);
                buttons[row][col] = findViewById(this.getResources().getIdentifier(temp, "id",this.getPackageName()));
            }
        }

        int[] currentsizes = new int[5];

        for (int i = 0 ; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                currentstate[i][j] = 0;
            }
        }

        reset = (Button) findViewById(R.id.btn_reset);

        current_player = (TextView) findViewById(R.id.player);

        for (int i = 0; i < 5; i++) {
            int ccol = i;
            buttons[0][i].setOnClickListener(new View.OnClickListener() {
                int col = ccol;
                @Override
                public void onClick(View view) {
                    int target = 4;
                    while (currentstate[target][ccol] != 0) {
                        if (target == 0) {
                            return;
                        }
                        target--;
                    }

                    buttons[target][ccol].setBackgroundColor(getResources().getColor(playerColor));
                    currentstate[target][ccol] = player_coin;

                    //recursively find any strings of coins from the dropped coin
                    for (int i = 0; i < 10; i++) {
                        String direction = "";
                        switch (i) {
                            case 0:
                                direction = "down";
                                break;
                            case 1:
                                direction = "left";
                                break;
                            case 2:
                                direction = "right";
                                break;
                            case 3:
                                direction = "diagonal-left";
                                break;
                            case 4:
                                direction = "diagonal-right";
                                break;
                            case 5:
                                direction = "diagonal-up-left";
                                break;
                            case 6:
                                direction = "diagonal-up-right";
                                break;
                            case 7:
                                direction = "middle-across";
                                break;
                            case 8:
                                direction = "middle-dleft";
                                break;
                            case 9:
                                direction = "middle-dright";
                                break;
                        }

                        if (coinChecker(target, ccol, player_coin, direction) == 3) {
                            String WinMessage = "PLAYER " + player_coin + " WINS!";
                            hasWon = true;
                            Toast.makeText(MainActivity.this, WinMessage, Toast.LENGTH_SHORT).show();
                            System.out.println("Winner: " + player_coin + "\n direction: " + direction);
                            current_player.setText(WinMessage);

                            for (int g = 0; g < 5; g++) {
                                buttons[0][g].setClickable(false);
                            }
                        }
                    }

                    //switch the player turn
                    if (!hasWon) {
                        if (player_coin == 1) {
                            player_coin = 2;
                            playerColor = android.R.color.holo_orange_light;
                        } else {
                            player_coin = 1;
                            playerColor = android.R.color.holo_purple;
                        }

                        String player_turn = "PLAYER " + player_coin;
                        current_player.setText(player_turn);
                    }
                }
            });
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        buttons[i][j].setBackgroundColor(getResources().getColor(android.R.color.white));
                        currentstate[i][j] = 0;
                    }
                }

                for (int k = 0; k < 5; k++) {
                    buttons[0][k].setClickable(true);
                }

                hasWon = false;
                player_coin = 1;
                playerColor = android.R.color.holo_purple;
                String playerText = "PLAYER " + player_coin;
                current_player.setText(playerText);
            }
        });
    }

    private int coinChecker(int row, int col, int player_coin, String direction) {
        if (row > 4 || row < 0 || col < 0 || col > 4) {
            return 0;
        } else {
            switch(direction) {
                case "down":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row+1, col, player_coin, "down") + 1;
                    } else {
                        return 0;
                    }
                case "left":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row, col-1, player_coin, "left") + 1;
                    } else {
                        return 0;
                    }
                case "right":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row, col+1, player_coin, "right") + 1;
                    } else {
                        return 0;
                    }
                case "diagonal-left":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row+1, col-1, player_coin, "diagonal-left") + 1;
                    } else {
                        return 0;
                    }
                case "diagonal-right":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row+1, col+1, player_coin, "diagonal-right") + 1;
                    } else {
                        return 0;
                    }
                case "diagonal-up-left":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row-1, col-1, player_coin, "diagonal-up-left") + 1;
                    } else {
                        return 0;
                    }
                case "diagonal-up-right":
                    if (currentstate[row][col] == player_coin) {
                        return coinChecker(row-1, col+1, player_coin, "diagonal-up-right") + 1;
                    } else {
                        return 0;
                    }
                case "middle-across":
                    if (!(col-1 < 0) && !(col+1 > 4) ) {
                        if (currentstate[row][col - 1] == player_coin && currentstate[row][col + 1] == player_coin) {
                            return 3;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                case "middle-dleft":
                    if (!(col-1 < 0) && !(col+1 > 4) && !(row-1 < 0) && !(row+1 > 4)) {
                        if (currentstate[row-1][col-1] == player_coin && currentstate[row+1][col+1] == player_coin) {
                            return 3;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                case "middle-dright":
                    if (!(col-1 < 0) && !(col+1 > 4) && !(row-1 < 0) && !(row+1 > 4)) {
                        if (currentstate[row-1][col+1] == player_coin && currentstate[row+1][col-1] == player_coin) {
                            return 3;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
            }
        }

        return -1;
    }
}