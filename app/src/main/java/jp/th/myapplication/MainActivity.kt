package jp.th.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu)

        val comBtn = findViewById<Button>(R.id.comMuchButton)
        comBtn.setOnClickListener{
            startOthello("com")
        }
        val playerBtn = findViewById<Button>(R.id.playerMuchButton)
        playerBtn.setOnClickListener{
            startOthello("player")
        }
    }

    private fun startOthello(mode: String){
        val intent = Intent(this@MainActivity, OthelloGame::class.java)
        intent.putExtra("GAME_MODE",mode)
        startActivity(intent)
    }
}

