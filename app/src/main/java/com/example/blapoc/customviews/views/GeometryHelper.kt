package com.leroymerlin.enki.main.control.customs

import android.graphics.PointF
import kotlin.math.*

/**
 * More informations
 * https://archidevlm.atlassian.net/wiki/spaces/MC/pages/2787966997/Geometry+Helper
 */

fun convertValueToDegree(value: Int) = value * Math.PI / 180
fun convertRadianToDegree(value: Double) = value * 180 / Math.PI

/**
 * angle : value in degree
 */
fun computePositionOnCircle(angle: Double, pointStart: PointF, pointCenter: PointF): PointF {
    val vectorX = (pointStart.x - pointCenter.x)
    val vectorY = (pointStart.y - pointCenter.y)

    val x: Double = vectorX * cos(angle) + vectorY * sin(angle) + pointCenter.x
    val y: Double = -vectorX * sin(angle) + vectorY * cos(angle) + pointCenter.y

    return PointF(round(x).toFloat(), round(y).toFloat())
}

fun computeAngleAOB(pointO: PointF, pointA: PointF, pointB: PointF): Double {
    val distanceAO = computeDistanceSegment(pointA, pointO)
    val distanceBO = computeDistanceSegment(pointB, pointO)
    val angle =
            (((pointA.x - pointO.x) * (pointB.x - pointO.x)) + ((pointA.y - pointO.y) * (pointB.y - pointO.y))) / (distanceAO * distanceBO)
    return convertRadianToDegree(acos(angle.toDouble()))
}

fun computeDistanceSegment(p1: PointF, p2: PointF) =
        sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))