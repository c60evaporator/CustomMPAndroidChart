package com.mongodb.custommpandroidchart.chart//プロジェクト構成に合わせ変更

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.mongodb.custommpandroidchart.R//プロジェクト構成に合わせ変更
import java.text.SimpleDateFormat
import java.util.*

/**
 * Chartの初期化
 * @param[barChart]:初期化対象のChart
 */
fun initializeBarChart(barChart: BarChart){
    barChart.apply {
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
 * 棒グラフ用Entryのリスト作成
 * @param[x]:X軸のデータ(List<Float>型)
 * @param[y]:Y軸のデータ(List<Float>型)
 */
fun makeBarChartData(x: List<Float>, y: List<Float>): MutableList<BarEntry> {
    //出力用のMutableList<Entry>
    var entryList = mutableListOf<BarEntry>()
    //xとyのサイズが異なるとき、エラーを出して終了
    if(x.size != y.size)
    {
        throw IllegalArgumentException("size of x and y are not equal")
    }
    //軸のデータを全てループしてEntryに格納
    for(i in x.indices){
        entryList.add(
            BarEntry(x[i], y[i])
        )
    }
    return entryList
}

/**
 * 積み上げ棒グラフ用Entryのリスト作成
 * @param[x]:X軸のデータ(List<Float>型)
 * @param[y]:Y軸のデータ(List<MutableList<Float>>型)
 */
fun makeStackBarChartData(x: List<Float>, y: List<MutableList<Float>>): MutableList<BarEntry> {
    //出力用のMutableList<Entry>
    var entryList = mutableListOf<BarEntry>()
    //xとyのサイズが異なるとき、エラーを出して終了
    if(x.size != y.size)
    {
        throw IllegalArgumentException("size of x and y are not equal")
    }
    //軸のデータを全てループしてEntryに格納
    for(i in x.indices){
        entryList.add(
            BarEntry(x[i], y[i].toFloatArray())
        )
    }
    return entryList
}

/**
 * 時系列棒グラフ用Entryのリスト作成
 * @param[x]:X軸のデータ(List<Date>型)
 * @param[y]:Y軸のデータ(List<Float>型)
 */
fun makeDateBarChartData(x: List<Date>, y: List<Float>): MutableList<BarEntry>{
    //出力用のMutableList<Entry>, ArrayList<String>
    var entryList = mutableListOf<BarEntry>()
    //xとyのサイズが異なるとき、エラーを出して終了
    if(x.size != y.size)
    {
        throw IllegalArgumentException("size of x and y are not equal")
    }
    //軸のデータを全てループしてEntryに格納
    for(i in x.indices){
        entryList.add(
            BarEntry(i.toFloat(),
                y[i],
                x[i])
        )
    }
    return entryList
}

/**
 * 積み上げ時系列棒グラフ用Entryのリスト作成
 * @param[x]:X軸のデータ(List<Date>型)
 * @param[y]:Y軸のデータ(List<Float>型)
 */
fun makeDateStackBarChartData(x: List<Date>, y: List<MutableList<Float>>): MutableList<BarEntry>{
    //出力用のMutableList<Entry>, ArrayList<String>
    var entryList = mutableListOf<BarEntry>()
    //xとyのサイズが異なるとき、エラーを出して終了
    if(x.size != y.size)
    {
        throw IllegalArgumentException("size of x and y are not equal")
    }
    //軸のデータを全てループしてEntryに格納
    for(i in x.indices){
        entryList.add(
            BarEntry(i.toFloat(),
                y[i].toFloatArray(),
                x[i])
        )
    }
    return entryList
}

/**
 * 棒グラフ描画
 * @param[allBarsEntries]:棒グラフのデータ本体 ({棒1名称:ListOf(棒1のEntry), 棒2名称:ListOf(棒2のEntry), ‥}の構造)
 * @param[barChart]:描画対象のBarChartビュー
 * @param[barChartFormat]:Chartのフォーマットを指定
 * @param[barDataSetFormats]:棒ごとのDataSetフォーマットを指定
 * @param[context]:呼び出し元のActivityあるいはFragmentのContext
 */
fun setupBarChart(
    allBarsEntries: MutableMap<String, MutableList<BarEntry>>,
    barChart: BarChart,
    barChartFormat: BarChartFormat,
    barDataSetFormats: Map<String, BarDataSetFormat>,
    context: Context
) {
    //初期化
    initializeBarChart(barChart)
    //背景輝度が0.5以下なら文字色を白に
    val luminance = barChartFormat.invertTextColor()
    barDataSetFormats.values.map { it.invertTextColor(luminance) }
    //xAxisDateFormatとtoolTipFormat.secondの日付指定有無が一致していないとき、例外を投げる
    if((barChartFormat.xAxisDateFormat == null && barChartFormat.toolTipDateFormat != null)
        || (barChartFormat.xAxisDateFormat != null && barChartFormat.toolTipDateFormat == null))
    {
        throw IllegalArgumentException("xAxisDateFormatとtoolTipFormat.secondのどちらかのみにnullを指定することはできません")
    }
    //xがdate型だがxAxisDateFormatがnullのとき、xAxisDateFormatおよびtoolTipDateFormatに仮フォーマット入力
    val dataType = allBarsEntries[allBarsEntries.keys.first()]?.firstOrNull()?.data?.javaClass
    if(dataType?.name == "java.util.Date" && barChartFormat.xAxisDateFormat == null){
        barChartFormat.xAxisDateFormat = SimpleDateFormat("M/d H:mm")
        barChartFormat.toolTipDateFormat = SimpleDateFormat("M/d H:mm")
    }

    //「単一棒グラフ」「複数棒グラフ」「積み上げ棒グラフ」をデータ形式から判別
    var barType = "single"//ひとまず「単一棒グラフ」とする
    val yType = allBarsEntries[allBarsEntries.keys.first()]?.firstOrNull()?.yVals//積み上げ判定用
    if(allBarsEntries.size >= 2) barType = "multiple"//複数棒グラフ
    else if(yType != null) barType = "stack"//積み上げ
    if(yType != null && allBarsEntries.size >= 2){//複数棒と積み上げの同時指定は禁止
        throw IllegalArgumentException("積み上げと複数棒グラフは同時指定できません")
    }
    //複数棒グラフ用前処理
    var xDiff: Float? = null
    if(barType == "multiple") {
        xDiff = multipleBarPreProcessing(allBarsEntries, barDataSetFormats)
    }

    //②BarDataSetのリストを作成
    val barDataSets = mutableListOf<IBarDataSet>()
    for((k, v) in allBarsEntries){
        var barDataSet: IBarDataSet = BarDataSet(v, k).apply{
            //③DataSetフォーマット適用
            formatBarDataSet(this, barDataSetFormats[k]!!, barType)
        }
        barDataSets.add(barDataSet)
    }

    //④BarDataにBarDataSet格納
    val barData = BarData(barDataSets)
    //⑤BarChartにBarDataを格納
    barChart.data = barData
    //⑥グラフ全体フォーマットの適用
    formatBarChart(barChart, barChartFormat, barType, context)
    //日付軸の設定
    if(barChartFormat.xAxisDateFormat != null) {
        //X軸ラベルのリスト取得
        val xLabel = allBarsEntries[allBarsEntries.keys.first()]?.map { barChartFormat.xAxisDateFormat?.format(it.data) }
        //上記リストをX軸ラベルに設定
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
    }
    //複数棒グラフのとき、グルーピング処理を実行
    if(barType == "multiple"){
        var xAxisSpan = 1f
        if(xDiff != null) xAxisSpan = xDiff//データのX軸間隔
        val barNumber = allBarsEntries.size//棒の本数
        val groupSpace = xAxisSpan / 5f//グループの間隔
        val barSpace = xAxisSpan / 20f//同グループの棒同士の間隔
        val barWidth = (xAxisSpan - groupSpace)/barNumber.toFloat() - barSpace//棒の幅
        barData.barWidth = barWidth//棒の幅をbarDataに指定
        val startX = barChart.barData.dataSets.first().xMin - xAxisSpan / 2f//Xの最小値 - X軸間隔/2
        barChart.groupBars(startX, groupSpace, barSpace)//棒のグルーピング
    }
    //⑦BarChart更新
    barChart.invalidate()
}

/*
**
* ⑥Chartフォーマットの適用
* @param[barChart]:適用対象のbarChart
* @param[barChartFormat]:Chartフォーマット
* @param[context]:呼び出し元のActivityあるいはFragmentのContext
*/
fun formatBarChart(barChart: BarChart, barChartFormat: BarChartFormat, barType:String, context: Context){
    //凡例
    if(barChartFormat.legendFormat != null){
        //凡例形状
        barChart.legend.form = barChartFormat.legendFormat
        //凡例文字色
        if(barChartFormat.legentTextColor != null) {
            barChart.legend.textColor = barChartFormat.legentTextColor!!
        }
        //凡例文字サイズ
        if(barChartFormat.legendTextSize != null) {
            barChart.legend.textSize = barChartFormat.legendTextSize!!
        }
    }
    else barChart.legend.isEnabled = false //凡例非表示のとき
    //グラフ説明
    if(barChartFormat.description != null) {
        barChart.description.isEnabled = true
        barChart.description.text = barChartFormat.description
        //グラフ説明の文字色
        if(barChartFormat.descriptionTextColor != null){
            barChart.description.textColor = barChartFormat.descriptionTextColor!!
        }
        //グラフ説明の文字サイズ
        if(barChartFormat.descriptionTextSize != null){
            barChart.description.textSize = barChartFormat.descriptionTextSize!!
        }
        //グラフ説明の横方向位置微調整
        if(barChartFormat.descriptionXOffset != null){
            barChart.description.xOffset = barChartFormat.descriptionXOffset!!
        }
        //グラフ説明の縦方向位置微調整
        if(barChartFormat.descriptionYOffset != null){
            barChart.description.yOffset = barChartFormat.descriptionYOffset!!
        }
    }
    else barChart.description.isEnabled = false//グラフ説明非表示のとき
    //全体の背景色
    if(barChartFormat.bgColor != null) {
        barChart.setBackgroundColor(barChartFormat.bgColor!!)
    }
    //タッチ動作
    barChart.setTouchEnabled(barChartFormat.touch)

    //X軸ラベルの設定
    if(barChartFormat.xAxisEnabled) {
        barChart.xAxis.isEnabled = true//X軸ラベル表示
        //文字色
        if(barChartFormat.xAxisTextColor != null) {
            barChart.xAxis.textColor = barChartFormat.xAxisTextColor!!
        }
        //文字サイズ
        if(barChartFormat.xAxisTextSize != null) {
            barChart.xAxis.textSize = barChartFormat.xAxisTextSize!!
        }
    }
    else barChart.xAxis.isEnabled = false//X軸ラベル非表示のとき

    // 左Y軸ラベルの設定
    if(barChartFormat.yAxisLeftEnabled){
        barChart.axisLeft.isEnabled = true//左Y軸ラベル表示
        //文字色
        if(barChartFormat.yAxisLeftTextColor != null) {
            barChart.axisLeft.textColor = barChartFormat.yAxisLeftTextColor!!
        }
        //文字サイズ
        if(barChartFormat.yAxisLeftTextSize != null) {
            barChart.axisLeft.textSize = barChartFormat.yAxisLeftTextSize!!
        }
        //表示範囲の下限
        if(barChartFormat.yAxisLeftMin != null){
            barChart.axisLeft.axisMinimum = barChartFormat.yAxisLeftMin!!
        }
        //表示範囲の上限
        if(barChartFormat.yAxisLeftMax != null){
            barChart.axisLeft.axisMaximum = barChartFormat.yAxisLeftMax!!
        }
    }
    else barChart.axisLeft.isEnabled = false//左Y軸ラベル非表示のとき

    // 右Y軸ラベルの設定
    if(barChartFormat.yAxisRightEnabled){
        barChart.axisRight.isEnabled = true//右Y軸ラベル表示
        //文字色
        if(barChartFormat.yAxisRightTextColor != null) {
            barChart.axisRight.textColor = barChartFormat.yAxisRightTextColor!!
        }
        //文字サイズ
        if(barChartFormat.yAxisRightTextSize != null) {
            barChart.axisRight.textSize = barChartFormat.yAxisRightTextSize!!
        }
        //表示範囲の下限
        if(barChartFormat.yAxisRightMin != null){
            barChart.axisRight.axisMinimum = barChartFormat.yAxisRightMin!!
        }
        //表示範囲の上限
        if(barChartFormat.yAxisRightMax != null){
            barChart.axisRight.axisMaximum = barChartFormat.yAxisRightMax!!
        }
    }
    else barChart.axisRight.isEnabled = false//右Y軸ラベル非表示のとき

    //ズーム設定
    when(barChartFormat.zoomDirection){
        null -> barChart.setScaleEnabled(false)
        "x" -> {
            barChart.setScaleXEnabled(true)
            barChart.setScaleYEnabled(false)
        }
        "y" -> {
            barChart.setScaleXEnabled(false)
            barChart.setScaleYEnabled(true)
        }
        "xy" -> {
            barChart.setScaleEnabled(true)
            barChart.setPinchZoom(barChartFormat.zoomPinch)
        }
    }

    //ツールチップの表示
    if(barChartFormat.toolTipDirection != null) {
        val mv: SimpleMarkerView = SimpleMarkerView(
            barChartFormat.toolTipDirection,
            barChartFormat.toolTipDateFormat,
            barChartFormat.toolTipUnitX,
            barChartFormat.toolTipUnitY,
            context,
            R.layout.simple_marker_view,
            20f
        )
        mv.chartView = barChart
        barChart.marker = mv
    }
}

/**
 * ③DataSetフォーマットの適用
 * @param[barDataSet]:適用対象のBarDataSet
 * @param[barDataSetFormat]:DataSetフォーマット
 */
fun formatBarDataSet(barDataSet: BarDataSet, barDataSetFormat: BarDataSetFormat, barType:String){
    //値のフォーマット
    //値表示するとき
    if(barDataSetFormat.drawValue){
        barDataSet.setDrawValues(true)
        //文字色
        if(barDataSetFormat.valueTextColor != null) barDataSet.valueTextColor = barDataSetFormat.valueTextColor!!
        //文字サイズ
        if(barDataSetFormat.valueTextSize != null) barDataSet.valueTextSize = barDataSetFormat.valueTextSize!!
        //文字書式の適用
        if(barDataSetFormat.valueTextFormatter != null){
            barDataSet.valueFormatter = object: ValueFormatter(){
                override fun getFormattedValue(value: Float): String{
                    return barDataSetFormat.valueTextFormatter!!.format(value)
                }
            }
        }
    }
    //値表示しないとき
    else barDataSet.setDrawValues(false)
    //使用する軸
    if(barDataSetFormat.axisDependency != null) {
        barDataSet.axisDependency = barDataSetFormat.axisDependency
    }

    //棒の色
    when(barType){
        //積み上げのとき、colorsを指定
        "stack" -> {
            val colorNum = barDataSet.stackSize
            barDataSet.colors = barDataSetFormat.barColors.slice(0 until colorNum)
        }
        //それ以外のとき、colorを指定
        else -> if(barDataSetFormat.barColor != null) barDataSet.color = barDataSetFormat.barColor!!
    }

    //積み上げ棒のカテゴリ名
    if(barType == "stack" && barDataSetFormat.stackLabels != null){
        barDataSet.stackLabels = barDataSetFormat.stackLabels!!.toTypedArray()
    }
}

/**
 * 複数棒グラフ用前処理
 * @param[allBarsEntries]:棒グラフのデータ本体
 * @param[barDataSetFormats]:棒ごとのDataSetフォーマットを指定
 */
fun multipleBarPreProcessing(
    allBarsEntries: MutableMap<String, MutableList<BarEntry>>,
    barDataSetFormats: Map<String, BarDataSetFormat>
): Float? {
    var xDiff: Float? = null//X軸の間隔
    //X軸が全て一定間隔であるかどうかを確認
    val xList = allBarsEntries[allBarsEntries.keys.first()]!!.map { it.x }
    if(xList.size >= 2) {
        xDiff = xList[1] - xList[0]
        for (i in 2 until xList?.size!!) {
            if (xList[i] - xList[i - 1] != xDiff){
                throw IllegalArgumentException("複数棒グラフでは、X軸の間隔が一定の必要があります")
            }
        }
    }
    //色が同じ棒が存在するとき、UNIVERSAL_COLORS_ACCENTから色を自動抽出
    var colorCnt = barDataSetFormats.map { it.value.barColor }.distinct().size
    if(colorCnt < barDataSetFormats.size){
        for((i, k) in barDataSetFormats.keys.withIndex()) {
            barDataSetFormats[k]?.barColor = UNIVERSAL_COLORS_ACCENT[i]
        }
    }
    return xDiff
}