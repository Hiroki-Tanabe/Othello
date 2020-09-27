package jp.th.myapplication

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_othello_game.*


class OthelloGame : AppCompatActivity() {
    private val othelloBoardController: OthelloBoardController = OthelloBoardController(this)
    private var com : Boolean = true
    private var continueTurn : Boolean = true
    val hand = Handler()
    val comRun : Runnable = object :Runnable{
        override fun run() {
            while(com && othelloBoardController.currentTurn == "black" && continueTurn){
                controlFun(othelloBoardController.comTurnPos())
                println("com")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_othello_game)
        val gameMode = intent.getStringExtra("GAME_MODE")
        setMode(gameMode!!)
        boardCell.adapter = othelloBoardController.adapter
        boardCell.setOnItemClickListener { parent, view, position, id ->
            if(othelloBoardController.currentTurn == "white"&& continueTurn){
                controlFun(position)
                println("WhitePlayer")
            }
            if(othelloBoardController.currentTurn == "black" && continueTurn){
                if(com){
                    println("com")
                    hand.postDelayed(comRun,1000)
                }else{
                    controlFun(position)
                    println("BlackPlayer")
                }

            }
        }
        imageViewWSbtn.setOnClickListener {
            othelloBoardController.backTurn()
            turnRefresh()
            continueTurn = true
            resultShow()
        }
    }

    private fun controlFun(position: Int) {
        othelloBoardController.onClickCell(position)
        turnRefresh()
        if (othelloBoardController.numOfEmpty < 1) {
            continueTurn = false
            resultShow()
            othelloBoardController.currentTurn = othelloBoardController.changeColor(othelloBoardController.currentTurn)
        }
    }

    private fun turnRefresh(){
        blackPoints.setText(othelloBoardController.numOfBlack.toString())
        whitePoints.setText(othelloBoardController.numOfWhite.toString())
        currentTurnBS.setImageResource(othelloBoardController.getImageId("black"))
        currentTurnWS.setImageResource(othelloBoardController.getImageId("white"))
    }

    private fun setMode(mode: String){
        when(mode){
            "com"    -> {com = true; playerNameBS.text = "COM" }
            "player" -> {com = false}
        }
    }

    private fun resultShow(){
        when {
            othelloBoardController.numOfWhite > othelloBoardController.numOfBlack -> {
                resultWS.text = "WIN!"
                resultWS.setTextColor(Color.RED)
                resultBS.text = "LOSE"
                resultBS.setTextColor(Color.BLACK)
            }
            othelloBoardController.numOfWhite < othelloBoardController.numOfBlack -> {
                resultBS.text = "WIN!"
                resultBS.setTextColor(Color.RED)
                resultWS.text = "LOSE"
                resultWS.setTextColor(Color.BLACK)
            }
            else -> {
                resultBS.text = "DRAW"
                resultBS.setTextColor(Color.RED)
                resultWS.text = "DRAW"
                resultWS.setTextColor(Color.RED)
            }
        }
        if(continueTurn){
            resultBS.text = ""
            resultWS.text = ""
        }
    }
}