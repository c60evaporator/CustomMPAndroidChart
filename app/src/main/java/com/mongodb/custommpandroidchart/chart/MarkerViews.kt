package com.mongodb.custommpandroidchart.chart//プロジェクト構成に合わせ変更

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.mongodb.custommpandroidchart.R//プロジェクト構成に合わせ変更
import java.text.SimpleDateFormat

/**
 * X,Y軸の値をツールチップ表示
 * @param[toolTipDirection]: ツールチップの表示軸, 2項目:日付のフォーマット(日付軸でないときnull))
 * @param[toolTipDateFormat]: ツールチップの日付フォーマット(日付軸でないときnull)
 * @param[unitX]: ツールチップのX軸表示に付加する単位
 * @param[unitY]: ツールチップのY軸表示に付加する単位
 * @param[context]: ツールチップを設置するActivityあるいはFragmentのContext
 * @param[layoutResource]: 表示するツールチップのレイアウト
 * @param[offsetY]: ツールチップ表示位置のY方向オフセット
 */
class SimpleMarkerView(val toolTipDirection: String?,
                       val toolTipDateFormat: SimpleDateFormat?,
                       val unitX: String,
                       val unitY: String,
                       context: Context,
                       layoutResource: Int,
                       val offsetY: Float = 20f
) : MarkerView(context, layoutResource){
    private lateinit var textView: TextView
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        textView = findViewById(R.id.tvSimple)
        //日付軸でないとき
        if(toolTipDateFormat == null) {
            when(toolTipDirection){
                "x" -> textView.text = "${e?.x}${unitX}"//X軸のみ表示
                "y" -> textView.text = "${getYValue(e,highlight)}${unitY}"//Y軸のみ表示
                "xy" -> textView.text = "${e?.x}${unitX}\n${getYValue(e,highlight)}${unitY}"//XY軸両方表示
            }
        }
        //日付軸のとき
        else {
            when(toolTipDirection){
                "x" -> textView.text = "${toolTipDateFormat?.format(e?.data)}${unitX}"//X軸のみ表示
                "y" -> textView.text = "${getYValue(e,highlight)}${unitY}"//Y軸のみ表示
                "xy" -> textView.text = "${toolTipDateFormat?.format(e?.data)}${unitX}\n${getYValue(e,highlight)}${unitY}"//XY軸両方表示
            }
        }
        super.refreshContent(e, highlight)
    }
    override fun getOffset(): MPPointF{
        return MPPointF(-(width / 2f), -height.toFloat() - offsetY)
    }

    fun getYValue(e: Entry?, highlight: Highlight?) : Float?{
        //棒グラフのとき、積み上げグラフ用の処理を実施
        if(e is BarEntry){
            return getBarYValue(e as BarEntry, highlight)
        }
        //棒グラフ以外の時、e.Yをそのまま出力
        else return e?.y
    }
    fun getBarYValue(e: BarEntry, highlight: Highlight?) : Float?{
        //積み上げの時、highlight.stackIndex番目の値を出力
        if(highlight != null && highlight.stackIndex >= 0){
            return e.yVals[highlight.stackIndex]
        }
        //積み上げ以外の時、e.Yをそのまま出力
        else return e.y
    }
}
