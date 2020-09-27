package jp.th.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class BoardCellAdapter : BaseAdapter {

    var context : Context? = null
    var boardCellList : ArrayList<BoardCell>

    constructor(context: Context, boardCellList: ArrayList<BoardCell>) : super() {
        this.context = context
        this.boardCellList = boardCellList
    }

    override fun getCount(): Int {
        return boardCellList.size
    }
    override fun getItem(position: Int): Any {
        return boardCellList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(view: View){
        var imageView: ImageView = view.findViewById(R.id.cellImage)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder : ViewHolder
        var convertView = convertView

        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.cell_element, null)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder

        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.imageView.setImageResource(boardCellList[position].image!!)

        return convertView!!
    }

}

