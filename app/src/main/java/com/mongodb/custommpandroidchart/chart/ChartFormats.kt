package com.mongodb.custommpandroidchart.chart

import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat

/**
 * Chartフォーマット指定の基底クラス
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[legendFormat]:凡例形状(nullなら凡例表示なし)
 * @param[legendTextColor]:凡例文字色(nullならデフォルト)
 * @param[legendTextSize]:凡例文字サイズ(nullならデフォルト)
 * @param[description]:グラフ説明(nullなら表示なし)
 * @param[descriptionTextColor]:グラフ説明の文字色(nullならデフォルト)
 * @param[descriptionTextSize]:グラフ説明の文字サイズ(nullならデフォルト)
 * @param[descriptionXOffset]:グラフ説明の横方向オフセット(nullならデフォルト)
 * @param[descriptionYOffset]:グラフ説明の縦方向オフセット(nullならデフォルト)
 * @param[bgColor]:背景色(nullならデフォルト)
 * @param[touch]:タッチ操作の有効(trueなら有効、falseなら無効)
 */
open class ChartFormat (
    //プロパティ
    var legendFormat: Legend.LegendForm? = Legend.LegendForm.DEFAULT,//凡例形状(nullなら非表示)
    var legentTextColor: Int? = null,//凡例文字色(nullならデフォルト)
    var legendTextSize: Float? = null,//凡例文字サイズ(nullならデフォルト)
    var description: String? = null,//グラフ説明(nullなら表示なし)
    var descriptionTextColor: Int? = null,//グラフ説明の文字色(nullならデフォルト)
    var descriptionTextSize: Float? = null,//グラフ説明の文字サイズ(nullならデフォルト)
    var descriptionXOffset: Float? = null,//グラフ説明の横方向オフセット(nullならデフォルト)
    var descriptionYOffset: Float? = null,//グラフ説明の縦方向オフセット(nullならデフォルト)
    var bgColor: Int? = null,//背景色(nullならデフォルト)
    var touch: Boolean = true//タッチ操作の有効(trueなら有効、falseなら無効)
){
    //背景輝度が0.5以下なら文字色を白に
    open fun invertTextColor(): Float?{
        if(bgColor != null)
        {
            val luminance = Color.luminance(bgColor!!)
            if(luminance < 0.5f){
                if(legentTextColor == null) legentTextColor = Color.WHITE
                if(descriptionTextColor == null) descriptionTextColor = Color.WHITE
            }
            return luminance
        }
        else return null
    }
}

/**
 * XY軸系Chartフォーマット指定用クラス(ChartFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[xAxisEnabled]:X軸の有効無効
 * @param[xAxisTextColor]:X軸文字色(nullならデフォルト)
 * @param[xAxisTextSize]:X軸文字サイズ(nullならデフォルト)
 * @param[xAxisDateFormat]:X軸の日付軸フォーマット(nullなら日付軸ではない)
 * @param[yAxisLeftEnabled]:左Y軸の有効無効
 * @param[yAxisLeftTextColor]:左Y軸の文字色(nullならデフォルト)
 * @param[yAxisLeftTextSize]:左Y軸の文字サイズ(nullならデフォルト)
 * @param[yAxisLeftMin]:左Y軸の表示範囲下限(nullならデフォルト)
 * @param[yAxisLeftMax]:左Y軸の表示範囲上限(nullならデフォルト)
 * @param[yAxisRightEnabled]:右Y軸の有効無効
 * @param[yAxisRightTextColor]:右Y軸の文字色(nullならデフォルト)
 * @param[yAxisRightTextSize]:右Y軸の文字サイズ(nullならデフォルト)
 * @param[yAxisRightMin]:右Y軸の表示範囲下限(nullならデフォルト)
 * @param[yAxisRightMax]:右Y軸の表示範囲上限(nullならデフォルト)
 * @param[zoomDirection]:ズームの方向(null, x, y, xy)
 * @param[zoomPinch]:ズームのピンチ動作をXY同時にするか(trueなら同時、falseなら1軸に限定)
 * @param[toolTipDirection]:ツールチップに表示する軸内容(null, x, y, xy)
 * @param[toolTipDateFormat]:ツールチップX軸表示の日付フォーマット(日付軸のときのみ、それ以外ならnull)
 * @param[toolTipUnitX]:ツールチップのX軸表示に付加する単位
 * @param[toolTipUnitY]:ツールチップのY軸表示に付加する単位
 */
open class XYChartFormat(
    legendFormat: Legend.LegendForm? = Legend.LegendForm.DEFAULT,
    legentTextColor: Int? = null,
    legendTextSize: Float? = null,
    description: String? = null,
    descriptionTextColor: Int? = null,
    descriptionTextSize: Float? = null,
    descriptionXOffset: Float? = null,
    descriptionYOffset: Float? = null,
    bgColor: Int? = null,
    touch: Boolean = true,
    var xAxisEnabled: Boolean = true,//X軸の有効無効
    var xAxisTextColor: Int? = null,//X軸文字色(nullなら表示なし)
    var xAxisTextSize: Float? = null,//X軸文字サイズ(nullならデフォルト)
    var xAxisDateFormat: SimpleDateFormat? = null,//X軸の日付軸フォーマット(nullなら日付軸ではない)
    var yAxisLeftEnabled: Boolean = true,//左Y軸の有効無効
    var yAxisLeftTextColor: Int? = null,//左Y軸の文字色(nullなら表示なし)
    var yAxisLeftTextSize: Float? = null,//左Y軸の文字サイズ(nullならデフォルト)
    var yAxisLeftMin: Float? = null,//左Y軸の表示範囲下限(nullならデフォルト)
    var yAxisLeftMax: Float? = null,//左Y軸の表示範囲上限(nullならデフォルト)
    var yAxisRightEnabled: Boolean = true,//右Y軸の有効無効
    var yAxisRightTextColor: Int? = null,//右Y軸の文字色(nullなら表示なし)
    var yAxisRightTextSize: Float? = null,//右Y軸の文字サイズ(nullならデフォルト)
    var yAxisRightMin: Float? = null,//右Y軸の表示範囲下限(nullならデフォルト)
    var yAxisRightMax: Float? = null,//右Y軸の表示範囲上限(nullならデフォルト)
    var zoomDirection: String? = "xy",//ズームの方向(null, x, y, xy)
    var zoomPinch: Boolean = false,//ズームのピンチ動作をXY同時にするか(trueなら同時、falseなら1軸に限定)
    var toolTipDirection: String? = null,//ツールチップに表示する軸内容(null, x, y, xy)
    var toolTipDateFormat: SimpleDateFormat? = null,//ツールチップX軸表示の日付フォーマット(日付軸以外ならnull)
    var toolTipUnitX: String = "",//ツールチップのX軸内容表示に付加する単位
    var toolTipUnitY: String = ""//ツールチップのY軸内容表示に付加する単位
): ChartFormat(legendFormat, legentTextColor, legendTextSize,
    description, descriptionTextColor, descriptionTextSize, descriptionXOffset, descriptionYOffset,
    bgColor, touch){
    override fun invertTextColor(): Float?{
        if (bgColor != null) {
            val luminance = Color.luminance(bgColor!!)
            if (luminance < 0.5f) {
                if (legentTextColor == null) legentTextColor = Color.WHITE
                if (descriptionTextColor == null) descriptionTextColor = Color.WHITE
                if (xAxisTextColor == null) xAxisTextColor = Color.WHITE
                if (yAxisLeftTextColor == null) yAxisLeftTextColor = Color.WHITE
                if (yAxisRightTextColor == null) yAxisRightTextColor = Color.WHITE
            }
            return luminance
        }
        else return null
    }
}

/**
 * 円グラフChartフォーマット指定用クラス(ChartFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[labelColor]:ラベルの文字色(nullならデフォルト)
 * @param[labelTextSize]:ラベルの文字サイズ(nullならデフォルト)
 * @param[centerText]:中央に表示するテキスト(nullなら表示なし)
 * @param[centerTextColor]:中央に表示するテキストカラー(nullならデフォルト)
 * @param[centerTextSize]:中央に表示するテキストサイズ(nullならデフォルト)
 * @param[holeRadius]:中央の穴の半径(nullならデフォルト)
 * @param[transparentCircleRadius]:中央付近の色が薄くなっている部分の半径(nullならデフォルト)
 * @param[centerColor]:中央の塗りつぶし色(nullならデフォルト)
 */
class PieChartFormat(
    legendFormat: Legend.LegendForm? = Legend.LegendForm.DEFAULT,
    legentTextColor: Int? = null,
    legendTextSize: Float? = null,
    description: String? = null,
    descriptionTextColor: Int? = null,
    descriptionTextSize: Float? = null,
    descriptionXOffset: Float? = null,
    descriptionYOffset: Float? = null,
    bgColor: Int? = null,
    touch: Boolean = true,
    var labelColor: Int? = null,//ラベルの文字色(nullならデフォルト)
    var labelTextSize: Float? = null,//ラベルの文字サイズ(nullならデフォルト)
    var centerText: String? = null,//中央に表示するテキスト(nullなら表示なし)
    var centerTextColor: Int? = null,//中央に表示するテキストカラー(nullならデフォルト)
    var centerTextSize: Float? = null,//中央に表示するテキストサイズ(nullならデフォルト)
    var holeRadius: Float? = null,//中央の穴の半径(nullならデフォルト)
    var transparentCircleRadius: Float? = null,//中央付近の色が薄くなっている部分の半径(nullならデフォルト)
    var holeColor: Int? = null//中央の塗りつぶし色(nullならデフォルト)
): ChartFormat(legendFormat, legentTextColor, legendTextSize,
    description, descriptionTextColor, descriptionTextSize, descriptionXOffset, descriptionYOffset,
    bgColor, touch)

/**
 * ローソク足グラフChartフォーマット指定用クラス(XYChartFormatと同じ)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 */
class CandleChartFormat(
    legendFormat: Legend.LegendForm? = Legend.LegendForm.DEFAULT,
    legentTextColor: Int? = null,
    legendTextSize: Float? = null,
    description: String? = null,
    descriptionTextColor: Int? = null,
    descriptionTextSize: Float? = null,
    descriptionXOffset: Float? = null,
    descriptionYOffset: Float? = null,
    bgColor: Int? = null,
    touch: Boolean = true,
    xAxisEnabled: Boolean = true,
    xAxisTextColor: Int? = null,
    xAxisTextSize: Float? = null,
    xAxisDateFormat: SimpleDateFormat? = null,
    yAxisLeftEnabled: Boolean = true,
    yAxisLeftTextColor: Int? = null,
    yAxisLeftTextSize: Float? = null,
    yAxisLeftMin: Float? = null,
    yAxisLeftMax: Float? = null,
    yAxisRightEnabled: Boolean = false,
    yAxisRightTextColor: Int? = null,
    yAxisRightTextSize: Float? = null,
    yAxisRightMin: Float? = null,
    yAxisRightMax: Float? = null,
    zoomDirection: String? = "xy",
    zoomPinch: Boolean = false,
    toolTipDirection: String? = null,
    toolTipDateFormat: SimpleDateFormat? = null,
    toolTipUnitX: String = "",
    toolTipUnitY: String = ""
): XYChartFormat(legendFormat, legentTextColor, legendTextSize,
    description, descriptionTextColor, descriptionTextSize, descriptionXOffset, descriptionYOffset,
    bgColor, touch,
    xAxisEnabled, xAxisTextColor, xAxisTextSize, xAxisDateFormat,
    yAxisLeftEnabled, yAxisLeftTextColor, yAxisLeftTextSize, yAxisLeftMin, yAxisLeftMax,
    yAxisRightEnabled, yAxisRightTextColor, yAxisRightTextSize, yAxisRightMin, yAxisRightMax,
    zoomDirection, zoomPinch,
    toolTipDirection, toolTipDateFormat, toolTipUnitX, toolTipUnitY)

/**
 * 折れ線グラフChartフォーマット指定用クラス(XYChartFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[timeAccuracy]:時系列グラフのX軸を正確にプロットするか(trueなら正確に表示するがラベルは最大最小値のみ、falseなら正確性落ちるが全ラベル表示)
 */
class LineChartFormat(
    legendFormat: Legend.LegendForm? = Legend.LegendForm.DEFAULT,
    legentTextColor: Int? = null,
    legendTextSize: Float? = null,
    description: String? = null,
    descriptionTextColor: Int? = null,
    descriptionTextSize: Float? = null,
    descriptionXOffset: Float? = null,
    descriptionYOffset: Float? = null,
    bgColor: Int? = null,
    touch: Boolean = true,
    xAxisEnabled: Boolean = true,
    xAxisTextColor: Int? = null,
    xAxisTextSize: Float? = null,
    xAxisDateFormat: SimpleDateFormat? = null,
    yAxisLeftEnabled: Boolean = true,
    yAxisLeftTextColor: Int? = null,
    yAxisLeftTextSize: Float? = null,
    yAxisLeftMin: Float? = null,
    yAxisLeftMax: Float? = null,
    yAxisRightEnabled: Boolean = false,
    yAxisRightTextColor: Int? = null,
    yAxisRightTextSize: Float? = null,
    yAxisRightMin: Float? = null,
    yAxisRightMax: Float? = null,
    zoomDirection: String? = "xy",
    zoomPinch: Boolean = false,
    toolTipDirection: String? = null,
    toolTipDateFormat: SimpleDateFormat? = null,
    toolTipUnitX: String = "",
    toolTipUnitY: String = "",
    val timeAccuracy: Boolean = false//時系列グラフのX軸を正確にプロットするか
): XYChartFormat(legendFormat, legentTextColor, legendTextSize,
    description, descriptionTextColor, descriptionTextSize, descriptionXOffset, descriptionYOffset,
    bgColor, touch,
    xAxisEnabled, xAxisTextColor, xAxisTextSize, xAxisDateFormat,
    yAxisLeftEnabled, yAxisLeftTextColor, yAxisLeftTextSize, yAxisLeftMin, yAxisLeftMax,
    yAxisRightEnabled, yAxisRightTextColor, yAxisRightTextSize, yAxisRightMin, yAxisRightMax,
    zoomDirection, zoomPinch,
    toolTipDirection, toolTipDateFormat, toolTipUnitX, toolTipUnitY)

/**
 * 棒グラフChartフォーマット指定用クラス(XYChartFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 */
class BarChartFormat(
    legendFormat: Legend.LegendForm? = Legend.LegendForm.DEFAULT,
    legentTextColor: Int? = null,
    legendTextSize: Float? = null,
    description: String? = null,
    descriptionTextColor: Int? = null,
    descriptionTextSize: Float? = null,
    descriptionXOffset: Float? = null,
    descriptionYOffset: Float? = null,
    bgColor: Int? = null,
    touch: Boolean = true,
    xAxisEnabled: Boolean = true,
    xAxisTextColor: Int? = null,
    xAxisTextSize: Float? = null,
    xAxisDateFormat: SimpleDateFormat? = null,
    yAxisLeftEnabled: Boolean = true,
    yAxisLeftTextColor: Int? = null,
    yAxisLeftTextSize: Float? = null,
    yAxisLeftMin: Float? = null,
    yAxisLeftMax: Float? = null,
    yAxisRightEnabled: Boolean = false,
    yAxisRightTextColor: Int? = null,
    yAxisRightTextSize: Float? = null,
    yAxisRightMin: Float? = null,
    yAxisRightMax: Float? = null,
    zoomDirection: String? = "xy",
    zoomPinch: Boolean = false,
    toolTipDirection: String? = null,
    toolTipDateFormat: SimpleDateFormat? = null,
    toolTipUnitX: String = "",
    toolTipUnitY: String = "",
): XYChartFormat(legendFormat, legentTextColor, legendTextSize,
    description, descriptionTextColor, descriptionTextSize, descriptionXOffset, descriptionYOffset,
    bgColor, touch,
    xAxisEnabled, xAxisTextColor, xAxisTextSize, xAxisDateFormat,
    yAxisLeftEnabled, yAxisLeftTextColor, yAxisLeftTextSize, yAxisLeftMin, yAxisLeftMax,
    yAxisRightEnabled, yAxisRightTextColor, yAxisRightTextSize, yAxisRightMin, yAxisRightMax,
    zoomDirection, zoomPinch,
    toolTipDirection, toolTipDateFormat, toolTipUnitX, toolTipUnitY)

/**
 * DataSetフォーマット指定の規定クラス
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[drawValue]://値の表示有無
 * @param[valueTextColor]:値表示の文字色(nullなら非表示)
 * @param[valueTextSize]:値表示の文字サイズ(nullならデフォルト)
 * @param[valueTextFormatter]:値表示の文字書式(nullならデフォルト)
 * @param[axisDependency]:使用する軸(LEFT or RIGHT)
 */
open class DataSetFormat(
    var drawValue: Boolean = true,//値の表示有無
    var valueTextColor: Int? = null,//値表示の文字色(nullならデフォルト)
    var valueTextSize: Float? = null,//値表示の文字サイズ(nullならデフォルト)
    var valueTextFormatter: String? = null,//値表示の文字書式(nullならデフォルト)
    var axisDependency: YAxis.AxisDependency? = YAxis.AxisDependency.LEFT//使用する軸(nullはデフォルト)
){
    //背景輝度が0.5以下なら文字色を白に
    open fun invertTextColor(luminance: Float?) {
        if (luminance != null && luminance < 0.5f) {
            if (valueTextColor == null) valueTextColor = Color.WHITE
        }
    }
}

/**
 *
 * 円グラフDataSetフォーマット指定用クラス(DataSetFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[colors]:グラフの色(dimensionsの数だけ指定が必要)
 */
class PieDataSetFormat(
    drawValue: Boolean = true,
    valueTextColor: Int? = null,
    valueTextSize: Float? = null,
    valueTextFormatter: String? = null,
    axisDependency: YAxis.AxisDependency? = null,
    var colors: List<Int> = UNIVERSAL_COLORS_ACCENT//グラフの色
): DataSetFormat(drawValue, valueTextColor, valueTextSize, valueTextFormatter, axisDependency)

/**
 * ローソク足グラフDataSetフォーマット指定用クラス(DataSetFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[shadowColor]:細線部分の色
 * @param[shadowWidth]:細線部分の太さ(nullならデフォルト)
 * @param[decreasingColor]:Open>Close時の太線部分の色、箱ひげとして使用するときはこちらを使用
 * @param[decreasingPaint]:Open>Close時の太線部分の塗りつぶし形式、箱ひげとして使用するときはこちらを使用(nullならデフォルト)
 * @param[increasingColor]:Open<Close時の太線部分の色、箱ひげとして使用するときは不使用(nullなら非表示)
 * @param[increasingPaint]:Open<Close時の太線部分の塗りつぶし形式、箱ひげとして使用するときは不使用(nullならデフォルト)
 */
class CandleDataSetFormat(
    drawValue: Boolean = true,
    valueTextColor: Int? = null,
    valueTextSize: Float? = null,
    valueTextFormatter: String? = null,
    axisDependency: YAxis.AxisDependency? = YAxis.AxisDependency.LEFT,
    var shadowColor: Int = UNIVERSAL_BROWN,//細線部分の色
    var shadowWidth: Float? = null,//細線部分の太さ(nullならデフォルト)
    var decreasingColor: Int = UNIVERSAL_GREEN,//Open>Close時の太線部分の色、箱ひげとして使用するときはこちらを使用
    var decreasingPaint: Paint.Style? = Paint.Style.FILL,//Open>Close時の太線部分の塗りつぶし形式(nullならデフォルト)
    var increasingColor: Int? = UNIVERSAL_RED,//Open<Close時の太線部分の色(nullなら非表示)
    var increasingPaint: Paint.Style? = Paint.Style.FILL,//Open<Close時の太線部分の色(nullならデフォルト)
): DataSetFormat(drawValue, valueTextColor, valueTextSize, valueTextFormatter, axisDependency)

/**
 * 折れ線グラフDataSetフォーマット指定用クラス(DataSetFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[lineColor]:線の色(nullならデフォルト)
 * @param[lineWidth]:線の太さ(nullならデフォルト)
 * @param[fittingMode]:線のフィッティング法(nullならデフォルト)
 * @param[drawCircles]:データ点のプロット有無(falseなら表示なし)
 * @param[circleColor]:データ点の色(nullならデフォルト)
 * @param[circleRadius]:データ点の半径(nullならデフォルト)
 * @param[circleHoleColor]:データ点の中央の塗りつぶし色(nullならデフォルト)
 * @param[circleHoleRadius]:データ点の中央の穴の半径(nullならデフォルト)
 */
class LineDataSetFormat(
    drawValue: Boolean = false,
    valueTextColor: Int? = null,
    valueTextSize: Float? = null,
    valueTextFormatter: String? = null,
    axisDependency: YAxis.AxisDependency? = YAxis.AxisDependency.LEFT,
    var lineColor: Int? = UNIVERSAL_COLORS_ACCENT.first(),//線の色(nullならデフォルト)
    var lineWidth: Float? = null,//線の太さ(nullならデフォルト)
    var fittingMode: LineDataSet.Mode? = null,//線のフィッティング法(nullならデフォルト)
    var drawCircles: Boolean = false,//データ点のプロット有無(falseなら表示なし)
    var circleColor: Int? = null, //データ点の色(nullならデフォルト)
    var circleRadius: Float? = null,//データ点の半径(nullならデフォルト)
    var circleHoleColor: Int? = null,//データ点の中央の塗りつぶし色(nullならデフォルト)
    var circleHoleRadius: Float? = null//データ点の中央の穴の半径(nullならデフォルト)
): DataSetFormat(drawValue, valueTextColor, valueTextSize, valueTextFormatter, axisDependency)

/**
 * 棒グラフDataSetフォーマット指定用クラス(DataSetFormatを継承)
 * @constructor 指定なしなら、全てデフォルト設定を使用
 * @param[barColor]:棒の色(nullならデフォルト)
 * @param[barColors]:積み上げ棒の色
 */
class BarDataSetFormat(
    drawValue: Boolean = false,
    valueTextColor: Int? = null,
    valueTextSize: Float? = null,
    valueTextFormatter: String? = null,
    axisDependency: YAxis.AxisDependency? = YAxis.AxisDependency.LEFT,
    var barColor: Int? = UNIVERSAL_COLORS_ACCENT.first(),//棒の色(nullならデフォルト)
    var barColors: List<Int> = UNIVERSAL_COLORS_ACCENT,//積み上げ棒の色
    var stackLabels: List<String>? = null//積み上げ棒のカテゴリ名
): DataSetFormat(drawValue, valueTextColor, valueTextSize, valueTextFormatter, axisDependency)

//カラーユニバーサルデザインに基づいた色リスト(アクセント)
var UNIVERSAL_GREEN = Color.rgb(3,175,122)//緑
var UNIVERSAL_RED = Color.rgb(255,75,0)//赤
var UNIVERSAL_BLUE = Color.rgb(0,90,255)//青
var UNIVERSAL_SKYBLUE = Color.rgb(77,196,255)//空色
var UNIVERSAL_BROWN = Color.rgb(128,64,0)//茶色
var UNIVERSAL_ORANGE = Color.rgb(246,170,0)//オレンジ
var UNIVERSAL_PURPLE = Color.rgb(153,0,153)//紫
var UNIVERSAL_PINK = Color.rgb(255,128,130)//ピンク
var UNIVERSAL_YELLOW = Color.rgb(255,241,0)//黄色
var UNIVERSAL_COLORS_ACCENT = listOf(
    UNIVERSAL_GREEN,//緑
    UNIVERSAL_RED,//赤
    UNIVERSAL_BLUE,//青
    UNIVERSAL_SKYBLUE,//空色
    UNIVERSAL_BROWN,//茶色
    UNIVERSAL_ORANGE,//オレンジ
    UNIVERSAL_PURPLE,//紫
    UNIVERSAL_PINK,//ピンク
    UNIVERSAL_YELLOW//黄色
)