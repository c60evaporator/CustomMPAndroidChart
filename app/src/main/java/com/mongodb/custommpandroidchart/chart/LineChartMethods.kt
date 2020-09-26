package com.mongodb.custommpandroidchart.chart//プロジェクト構成に合わせ変更

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.mongodb.custommpandroidchart.R//プロジェクト構成に合わせ変更
import java.text.SimpleDateFormat
import java.util.*

/**
 * Chartの初期化
 * @param[lineChart]:初期化対象のChart
 */
fun initializeLineChart(lineChart: LineChart){
    lineChart.apply {
        clear()
        background = null//背景色をリセット
        marker = null//マーカーをリセット
        xAxis.valueFormatter = null//X軸ラベルをリセット
        legend.textColor = Color.BLACK
        description.textColor = Color.BLACK
        xAxis.textColor = Color.BLACK
        axisLeft.textColor = Color.BLACK
        axisRight.textColor = Color.BLACK
    }
}

/**
 * 折れ線グラフ用Entryのリスト作成
 * @param[x]:X軸のデータ(List<Float>型)
 * @param[y]:Y軸のデータ(List<Float>型)
 */
fun makeLineChartData(x: List<Float>, y: List<Float>): MutableList<Entry> {
    //出力用のMutableList<Entry>
    var entryList = mutableListOf<Entry>()
    //xとyのサイズが異なるとき、エラーを出して終了
    if(x.size != y.size)
    {
        throw IllegalArgumentException("size of x and y are not equal")
    }
    //軸のデータを全てループしてEntryに格納
    for(i in x.indices){
        entryList.add(
            Entry(x[i], y[i])
        )
    }
    return entryList
}

/**
 * 時系列折れ線グラフ用Entryのリスト作成
 * @param[x]:X軸のデータ(List<Date>型)
 * @param[y]:Y軸のデータ(List<Float>型)
 */
fun makeDateLineChartData(x: List<Date>, y: List<Float>, timeAccuracy: Boolean): MutableList<Entry>{
    //出力用のMutableList<Entry>
    var entryList = mutableListOf<Entry>()
    //xとyのサイズが異なるとき、エラーを出して終了
    if(x.size != y.size)
    {
        throw IllegalArgumentException("size of x and y are not equal")
    }
    //軸のデータを全てループしてEntryに格納
    //全ラベル表示のとき
    if(!timeAccuracy){
        for(i in x.indices){
            entryList.add(
                Entry(i.toFloat(), y[i], x[i])
            )
        }
    }
    //最初と最後のラベルのみ表示するとき（精度重視）
    else{
        //日付をシリアル値に変換して規格化
        val xSerial = x.map{it.time.toFloat()}//シリアル値変換
        val maxSerial = xSerial.max()!!//最大値
        val minSerial = xSerial.min()!!//最小値
        val size = x.size//データの要素数
        val xFloat = xSerial.map { (it - minSerial) / (maxSerial - minSerial) * (size - 1) }//最小値が0、最大値がsize - 1となるよう規格化
        //entryListに入力
        for(i in x.indices){
            entryList.add(
                Entry(xFloat[i], y[i], x[i])
            )
        }
    }
    return entryList
}

/**
 * 折れ線グラフ描画
 * @param[allLinesEntries]:折れ線グラフのデータ本体 ({折れ線1名称:ListOf(折れ線1のEntry), 折れ線2名称:ListOf(折れ線2のEntry), ‥}の構造)
 * @param[lineChart]:描画対象のLineChartビュー
 * @param[lineChartFormat]:Chartのフォーマットを指定
 * @param[lineDataSetFormats]:折れ線ごとのDataSetフォーマットを指定
 * @param[context]:呼び出し元のActivityあるいはFragmentのContext
 */
fun setupLineChart(
    allLinesEntries: MutableMap<String, MutableList<Entry>>,
    lineChart: LineChart,
    lineChartFormat: LineChartFormat,
    lineDataSetFormats: Map<String, LineDataSetFormat>,
    context: Context
) {
    //初期化
    initializeLineChart(lineChart)
    //背景輝度が0.5以下なら文字色を白に
    val luminance = lineChartFormat.invertTextColor()
    lineDataSetFormats.values.map { it.invertTextColor(luminance) }

    //xがdate型だがxAxisDateFormatあるいはtoolTipDateFormatがnullのとき、xAxisDateFormatおよびtoolTipDateFormatに仮フォーマット入力
    val dataType = allLinesEntries[allLinesEntries.keys.first()]?.firstOrNull()?.data?.javaClass
    if(dataType?.name == "java.util.Date") {
        if(lineChartFormat.xAxisDateFormat == null) {
            lineChartFormat.xAxisDateFormat = SimpleDateFormat("M/d H:mm")
        }
        if(lineChartFormat.toolTipDateFormat == null) {
            lineChartFormat.toolTipDateFormat = SimpleDateFormat("M/d H:mm")
        }
    }

    //複数線グラフかつ色が同じ線が存在するとき、UNIVERSAL_COLORS_ACCENTから色を自動抽出
    if(allLinesEntries.size >= 2){
        var colorCnt = lineDataSetFormats.map { it.value.lineColor }.distinct().size
        if(colorCnt < lineDataSetFormats.size){
            for((i, k) in lineDataSetFormats.keys.withIndex()) {
                lineDataSetFormats[k]?.lineColor = UNIVERSAL_COLORS_ACCENT[i]
            }
        }
    }

    //②LineDataSetのリストを作成
    val lineDataSets = mutableListOf<ILineDataSet>()
    for((k, v) in allLinesEntries){
        var lineDataSet: ILineDataSet = LineDataSet(v, k).apply{
            //③DataSetフォーマット適用
            formatLineDataSet(this, lineDataSetFormats[k]!!)
        }
        lineDataSets.add(lineDataSet)
    }

    //④LineDataにLineDataSet格納
    val lineData = LineData(lineDataSets)
    //⑤LineChartにLineDataを格納
    lineChart.data = lineData
    //⑥グラフ全体フォーマットの適用
    formatLineChart(lineChart, lineChartFormat, context)
    //日付軸の設定
    if(lineChartFormat.xAxisDateFormat != null) {
        //全ラベル表示のとき
        if(!lineChartFormat.timeAccuracy){
            //X軸ラベルのリスト取得
            val xLabel = allLinesEntries[allLinesEntries.keys.first()]?.map { lineChartFormat.xAxisDateFormat?.format(it.data) }
            //上記リストをX軸ラベルに設定
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        }
        //精度重視のとき
        else{
            //X軸ラベルのリスト取得
            val size = allLinesEntries[allLinesEntries.keys.first()]?.size!!
            val xLabel = allLinesEntries[allLinesEntries.keys.first()]?.mapIndexed { index, entry ->
                when(index){
                    0 -> lineChartFormat.xAxisDateFormat?.format(entry.data)
                    size - 1 -> lineChartFormat.xAxisDateFormat?.format(entry.data)
                    else -> ""
                }
            }
            //上記リストをX軸ラベルに設定
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        }
    }
    //⑦LineChart更新
    lineChart.invalidate()
}


/**
 * ⑥Chartフォーマットの適用
 * @param[lineChart]:適用対象のlineChart
 * @param[lineChartFormat]:Chartフォーマット
 * @param[context]:呼び出し元のActivityあるいはFragmentのContext
 */
fun formatLineChart(lineChart: LineChart, lineChartFormat: LineChartFormat, context: Context){
    //凡例
    if(lineChartFormat.legendFormat != null){
        //凡例形状
        lineChart.legend.form = lineChartFormat.legendFormat
        //凡例文字色
        if(lineChartFormat.legentTextColor != null) {
            lineChart.legend.textColor = lineChartFormat.legentTextColor!!
        }
        //凡例文字サイズ
        if(lineChartFormat.legendTextSize != null) {
            lineChart.legend.textSize = lineChartFormat.legendTextSize!!
        }
    }
    else lineChart.legend.isEnabled = false //凡例非表示のとき
    //グラフ説明
    if(lineChartFormat.description != null) {
        lineChart.description.isEnabled = true
        lineChart.description.text = lineChartFormat.description
        //グラフ説明の文字色
        if(lineChartFormat.descriptionTextColor != null){
            lineChart.description.textColor = lineChartFormat.descriptionTextColor!!
        }
        //グラフ説明の文字サイズ
        if(lineChartFormat.descriptionTextSize != null){
            lineChart.description.textSize = lineChartFormat.descriptionTextSize!!
        }
        //グラフ説明の横方向位置微調整
        if(lineChartFormat.descriptionXOffset != null){
            lineChart.description.xOffset = lineChartFormat.descriptionXOffset!!
        }
        //グラフ説明の縦方向位置微調整
        if(lineChartFormat.descriptionYOffset != null){
            lineChart.description.yOffset = lineChartFormat.descriptionYOffset!!
        }
    }
    else lineChart.description.isEnabled = false//グラフ説明非表示のとき
    //全体の背景色
    if(lineChartFormat.bgColor != null) {
        lineChart.setBackgroundColor(lineChartFormat.bgColor!!)
    }
    //タッチ動作
    lineChart.setTouchEnabled(lineChartFormat.touch)

    //X軸ラベルの設定
    if(lineChartFormat.xAxisEnabled) {
        lineChart.xAxis.isEnabled = true//X軸ラベル表示
        //文字色
        if(lineChartFormat.xAxisTextColor != null) {
            lineChart.xAxis.textColor = lineChartFormat.xAxisTextColor!!
        }
        //文字サイズ
        if(lineChartFormat.xAxisTextSize != null) {
            lineChart.xAxis.textSize = lineChartFormat.xAxisTextSize!!
        }
    }
    else lineChart.xAxis.isEnabled = false//X軸ラベル非表示のとき

    // 左Y軸ラベルの設定
    if(lineChartFormat.yAxisLeftEnabled){
        lineChart.axisLeft.isEnabled = true//左Y軸ラベル表示
        //文字色
        if(lineChartFormat.yAxisLeftTextColor != null) {
            lineChart.axisLeft.textColor = lineChartFormat.yAxisLeftTextColor!!
        }
        //文字サイズ
        if(lineChartFormat.yAxisLeftTextSize != null) {
            lineChart.axisLeft.textSize = lineChartFormat.yAxisLeftTextSize!!
        }
        //表示範囲の下限
        if(lineChartFormat.yAxisLeftMin != null){
            lineChart.axisLeft.axisMinimum = lineChartFormat.yAxisLeftMin!!
        }
        //表示範囲の上限
        if(lineChartFormat.yAxisLeftMax != null){
            lineChart.axisLeft.axisMaximum = lineChartFormat.yAxisLeftMax!!
        }
    }
    else lineChart.axisLeft.isEnabled = false//左Y軸ラベル非表示のとき

    // 右Y軸ラベルの設定
    if(lineChartFormat.yAxisRightEnabled){
        lineChart.axisRight.isEnabled = true//右Y軸ラベル表示
        //文字色
        if(lineChartFormat.yAxisRightTextColor != null) {
            lineChart.axisRight.textColor = lineChartFormat.yAxisRightTextColor!!
        }
        //文字サイズ
        if(lineChartFormat.yAxisRightTextSize != null) {
            lineChart.axisRight.textSize = lineChartFormat.yAxisRightTextSize!!
        }
        //表示範囲の下限
        if(lineChartFormat.yAxisRightMin != null){
            lineChart.axisRight.axisMinimum = lineChartFormat.yAxisRightMin!!
        }
        //表示範囲の上限
        if(lineChartFormat.yAxisRightMax != null){
            lineChart.axisRight.axisMaximum = lineChartFormat.yAxisRightMax!!
        }
    }
    else lineChart.axisRight.isEnabled = false//右Y軸ラベル非表示のとき

    //ズーム設定
    when(lineChartFormat.zoomDirection){
        null -> lineChart.setScaleEnabled(false)
        "x" -> {
            lineChart.setScaleXEnabled(true)
            lineChart.setScaleYEnabled(false)
        }
        "y" -> {
            lineChart.setScaleXEnabled(false)
            lineChart.setScaleYEnabled(true)
        }
        "xy" -> {
            lineChart.setScaleEnabled(true)
            lineChart.setPinchZoom(lineChartFormat.zoomPinch)
        }
    }

    //ツールチップの表示
    if(lineChartFormat.toolTipDirection != null) {
        val mv: SimpleMarkerView = SimpleMarkerView(
            lineChartFormat.toolTipDirection,
            lineChartFormat.toolTipDateFormat,
            lineChartFormat.toolTipUnitX,
            lineChartFormat.toolTipUnitY,
            context,
            R.layout.simple_marker_view,
            20f
        )
        mv.chartView = lineChart
        lineChart.marker = mv
    }
}

/**
 * ③DataSetフォーマットの適用
 * @param[lineDataSet]:適用対象のLineDataSet
 * @param[lineDataSetFormat]:DataSetフォーマット
 */
fun formatLineDataSet(lineDataSet: LineDataSet, lineDataSetFormat: LineDataSetFormat){
    //値のフォーマット
    //値表示するとき
    if(lineDataSetFormat.drawValue){
        lineDataSet.setDrawValues(true)
        //文字色
        if(lineDataSetFormat.valueTextColor != null) lineDataSet.valueTextColor = lineDataSetFormat.valueTextColor!!
        //文字サイズ
        if(lineDataSetFormat.valueTextSize != null) lineDataSet.valueTextSize = lineDataSetFormat.valueTextSize!!
        //文字書式の適用
        if(lineDataSetFormat.valueTextFormatter != null){
            lineDataSet.valueFormatter = object: ValueFormatter(){
                override fun getFormattedValue(value: Float): String{
                    return lineDataSetFormat.valueTextFormatter!!.format(value)
                }
            }
        }
    }
    //値表示しないとき
    else lineDataSet.setDrawValues(false)
    //使用する軸
    if(lineDataSetFormat.axisDependency != null) {
        lineDataSet.axisDependency = lineDataSetFormat.axisDependency
    }

    //線の色
    if(lineDataSetFormat.lineColor != null) lineDataSet.color = lineDataSetFormat.lineColor!!
    //線の幅
    if(lineDataSetFormat.lineWidth != null) lineDataSet.lineWidth = lineDataSetFormat.lineWidth!!
    //フィッティング法
    if(lineDataSetFormat.fittingMode != null) lineDataSet.mode = lineDataSetFormat.fittingMode!!

    //データ点のフォーマット
    //データ点表示するとき
    if(lineDataSetFormat.drawCircles){
        lineDataSet.setDrawCircles(true)
        //データ点の色
        if(lineDataSetFormat.circleColor != null){
            lineDataSet.setCircleColor(lineDataSetFormat.circleColor!!)
        }
        //データ点の半径
        if(lineDataSetFormat.circleRadius!= null){
            lineDataSet.circleRadius = lineDataSetFormat.circleRadius!!
        }
        //データ点の中央の塗りつぶし色
        if(lineDataSetFormat.circleHoleColor != null){
            lineDataSet.circleHoleColor = lineDataSetFormat.circleHoleColor!!
        }
        //データ点の中央の穴の半径
        if(lineDataSetFormat.circleHoleRadius!= null){
            lineDataSet.circleHoleRadius = lineDataSetFormat.circleHoleRadius!!
        }
    }
    //データ点表示しないとき
    else lineDataSet.setDrawCircles(false)
}