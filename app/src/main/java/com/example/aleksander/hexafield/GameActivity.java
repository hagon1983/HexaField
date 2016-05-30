package com.example.aleksander.hexafield;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.aleksander.hexafield.library.controller.GameController;
import com.example.aleksander.hexafield.library.view.FigureView;
import com.example.aleksander.hexafield.library.view.GameFieldView;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private GameController gameController;
    private final String BUNDLE_KE_GAME_CONTROLLER = "game_controller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init4Grids();
        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KE_GAME_CONTROLLER)){
            Log.e(TAG, "onCreate: can resume game");
            gameController.resumeGame(savedInstanceState.getBundle(BUNDLE_KE_GAME_CONTROLLER));
        }else{
            Log.e(TAG, "onCreate: will start new game");
            gameController.startGame();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: save current game");
        outState.putBundle(BUNDLE_KE_GAME_CONTROLLER, gameController.saveGame());
    }

    private void init4Grids(){
        setContentView(R.layout.activity_game);

        final View rootView = findViewById(R.id.wholeScreen);

        final GameFieldView container = (GameFieldView) findViewById(R.id.gameView);
        FigureView child1 = (FigureView) findViewById(R.id.tool1);
        FigureView child2 = (FigureView) findViewById(R.id.tool2);
        FigureView child3 = (FigureView) findViewById(R.id.tool3);

        gameController = new GameController(rootView, container);
        gameController.addDraggableItem(child1);
        gameController.addDraggableItem(child2);
        gameController.addDraggableItem(child3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.restart){
            gameController.startGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
