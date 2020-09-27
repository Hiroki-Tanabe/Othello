package jp.th.myapplication

import android.content.Context

class OthelloBoardController {

    var numOfBlack : Int = 2
    var numOfWhite : Int = 2
    var numOfEmpty : Int = 60
    var adapter : BoardCellAdapter
    var com : Boolean = true
    private var boardMatrix = Array(61){Array(8){arrayOfNulls<String>(8)}}
//    private var boardMatrix = Array(8){arrayOfNulls<String>(8)}
    var currentTurn : String = "white"
    private var reverseSuccess: Boolean = false
    private var searchSuccess: Boolean = false
    private var reversibleCellList = Array(8){Array(8){arrayListOf<Int>()}}
    private var boardCellList : ArrayList<BoardCell> = arrayListOf()
    private val pieceDict = mapOf(
        "black"     to BoardCell(R.drawable.cell_black),
        "white"     to BoardCell(R.drawable.cell_white),
        "empty"     to BoardCell(R.drawable.cell_empty)
    )


    constructor(context: Context){
        this.initPosition()
        for (i in 0..7) {
            for (j in 0..7) {
                boardCellList.add(pieceDict[boardMatrix[numOfEmpty][i][j]]!!)
                searchReversibleCells()
            }
        }
        adapter = BoardCellAdapter(context, boardCellList)
    }


    fun onClickCell(clickedPos : Int){
        checkAndReverse(clickedPos)
        if(reverseSuccess) {
            if(numOfEmpty in 1..60) {
                numOfEmpty -= 1
            }
            updateBoard()
            currentTurn = changeColor(currentTurn)
            searchReversibleCells()
            if (!searchSuccess){
                currentTurn = changeColor(currentTurn)
                searchReversibleCells()
            }
            val results = pointCalculate()
            numOfBlack = results.first
            numOfWhite = results.second
        }
    }

    private fun searchReversibleCells(){
        resetReversibleCellList()
        for(searchPos in 0..63){
            if (boardMatrix[numOfEmpty][searchPos / 8][searchPos % 8] == "empty") {
                searchReversibleCell(searchPos, "Horizontal")
                searchReversibleCell(searchPos, "Vertical")
                searchReversibleCell(searchPos, "RightUp")
                searchReversibleCell(searchPos, "RightDown")
            }
        }
    }

    private fun resetReversibleCellList(){
        reversibleCellList = Array(8){Array(8){arrayListOf<Int>()}}
        searchSuccess = false
    }


    private fun initPosition(){
        for(h in 0..60) {
            for (i in 0..7) {
                for (j in 0..7) {
                    boardMatrix[h][i][j] = "empty"
                }
                boardMatrix[h][3][3] = "black"
                boardMatrix[h][4][4] = "black"
                boardMatrix[h][3][4] = "white"
                boardMatrix[h][4][3] = "white"
//
//            boardMatrix[h][0][1] = "black"
//            boardMatrix[h][1][1] = "black"
//            boardMatrix[h][1][2] = "black"
//            boardMatrix[h][0][0] = "white"
//            boardMatrix[h][1][0] = "white"
            }
        }
    }


    private fun updateBoard(){
        for(i in 0..7){
            for(j in 0..7){
                boardCellList[8*i + j] = pieceDict[boardMatrix[numOfEmpty][i][j]]!!
            }
        }
        adapter.notifyDataSetChanged()
    }


    fun changeColor(currentColor:String): String {
        return if(currentColor=="white"){
            "black"
        }else{
            "white"
        }
    }



    private fun searchReversibleCell(searchPos:Int, searchDir:String){

        var xPos : Int = searchPos%8
        var yPos : Int = searchPos/8
        var xDir : Int = 0
        var yDir : Int = 0
        var oppositeListX : ArrayList<Int> = arrayListOf()
        var oppositeListY : ArrayList<Int> = arrayListOf()


        fun setReversibleCell(){
            searchSuccess = true
            val ite = oppositeListX.size - 1
            for(i in 0..ite){
                reversibleCellList[searchPos/8][searchPos%8].add(oppositeListX[i])
                reversibleCellList[searchPos/8][searchPos%8].add(oppositeListY[i])
            }
        }

        when(searchDir){
            "Horizontal" -> { xDir = 1; yDir = 0 }
            "Vertical"   -> { xDir = 0; yDir = 1 }
            "RightUp"    -> { xDir = 1; yDir = 1 }
            "RightDown"  -> { xDir = 1; yDir = -1}
        }

        while(xPos+xDir in 0..7 && yPos+yDir in 0..7){
            when(boardMatrix[numOfEmpty][yPos+yDir][xPos+xDir]){
                currentTurn -> {
                    if(xPos!=searchPos%8 || yPos!=searchPos/8) {
                        setReversibleCell()
                    }
                    yPos = -10
                }
                "empty" -> {
                    yPos = -10
                }
                else -> {
                    xPos += xDir
                    yPos += yDir
                    oppositeListX.add(xPos)
                    oppositeListY.add(yPos)
                }
            }
        }
        
        xPos = searchPos%8
        yPos = searchPos/8
        xDir = -xDir
        yDir = -yDir
        oppositeListX = arrayListOf()
        oppositeListY = arrayListOf()

        while(xPos+xDir in 0..7 && yPos+yDir in 0..7){
            when(boardMatrix[numOfEmpty][yPos+yDir][xPos+xDir]){
                currentTurn -> {
                    if(xPos!=searchPos%8 || yPos!=searchPos/8) {
                        setReversibleCell()
                    }
                    yPos = -10
                }
                "empty" -> {
                    yPos = -10
                }
                else -> {
                    xPos += xDir
                    yPos += yDir
                    oppositeListX.add(xPos)
                    oppositeListY.add(yPos)
                }
            }
        }
    }


    private fun pointCalculate():Pair<Int, Int>{
        var numOfBlack : Int = 0
        var numOfWhite : Int = 0
        for(i in 0..7) {
            for (j in 0..7) {
                when(boardMatrix[numOfEmpty][i][j]){
                    "black" -> { numOfBlack += 1 }
                    "white" -> { numOfWhite += 1 }
                }
            }
        }
        return numOfBlack to numOfWhite
    }

    private fun checkAndReverse(clickedPos:Int){
        reverseSuccess = false
        fun reverseOpposite(){
            for(i in 0..7){
                for(j in 0..7){
                    boardMatrix[numOfEmpty-1][i][j] = boardMatrix[numOfEmpty][i][j]
                }
            }
            boardMatrix[numOfEmpty-1][clickedPos/8][clickedPos%8] = currentTurn
            val ite = (reversibleCellList[clickedPos/8][clickedPos%8].size)/2-1
            for(i in 0..ite){
                boardMatrix[numOfEmpty-1][reversibleCellList[clickedPos/8][clickedPos%8][2*i+1]][reversibleCellList[clickedPos/8][clickedPos%8][2*i]] = currentTurn
            }
        }

        if (reversibleCellList[clickedPos/8][clickedPos%8].isNotEmpty()){
            reverseSuccess = true
            reverseOpposite()
        }
    }

    fun getImageId(myCol:String):Int{
        var img : Int = R.drawable.cell_off
        if(currentTurn == myCol){
            when(myCol){
                "black" -> img = R.drawable.cell_black
                "white" -> img = R.drawable.cell_white
            }
        }
        return img
    }

    fun comTurnPos():Int{
        var max: Int = 0
        var comPos: Int = 0
        for(i in 0..7) {
            for (j in 0..7) {
                if(reversibleCellList[i][j].size > max){
                    comPos = 8*i + j
                    max = reversibleCellList[i][j].size
                }
            }
        }
    return comPos
    }

    fun backTurn(){
        if(numOfEmpty in 0..59) {
            numOfEmpty += 1
            updateBoard()
            currentTurn = changeColor(currentTurn)
            searchReversibleCells()
            val results = pointCalculate()
            numOfBlack = results.first
            numOfWhite = results.second
        }
    }
}
