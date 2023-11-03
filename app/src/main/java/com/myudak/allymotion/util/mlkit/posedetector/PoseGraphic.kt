/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.kotlin.posedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.airbnb.lottie.LottieProperty.TEXT_SIZE
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.myudak.allymotion.util.mlkit.GraphicOverlay
import com.myudak.allymotion.util.mlkit.exercises.ExerciseLogic
import com.myudak.allymotion.util.mlkit.posedetector.CameraXLiveViewModel
import java.lang.Math.max
import java.lang.Math.min
import java.util.Locale
import kotlin.math.abs
import kotlin.math.atan2

/** Draw the detected pose in preview. */

private const val SECONDS_PER_MINUTE = 60

class PoseGraphic
internal constructor(
  overlay: GraphicOverlay,
  private val pose: Pose,
//  private val showInFrameLikelihood: Boolean,
//  private val visualizeZ: Boolean,
//  private val rescaleZForVisualization: Boolean,
//  private val poseClassification: List<String>,
  private  val viewModel: CameraXLiveViewModel,
  private val olga : String
) : GraphicOverlay.Graphic(overlay) {
  private var zMin = java.lang.Float.MAX_VALUE
  private var zMax = java.lang.Float.MIN_VALUE
  private val classificationTextPaint: Paint
  private val leftPaint: Paint
  private val rightPaint: Paint
  private val whitePaint: Paint
  private val textSizeCoordinateX = 1.5f
  private val textSizeCoordinateY = 6
  private val tipPaint: Paint
  private var toDraw = false

  init {
    classificationTextPaint = Paint()
    classificationTextPaint.color = Color.WHITE
    classificationTextPaint.textSize = POSE_CLASSIFICATION_TEXT_SIZE
    classificationTextPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK)

    tipPaint = Paint()
    tipPaint.color = Color.BLACK
    tipPaint.textSize = 40f

    whitePaint = Paint()
    whitePaint.strokeWidth = STROKE_WIDTH
    whitePaint.color = Color.WHITE
    whitePaint.textSize = IN_FRAME_LIKELIHOOD_TEXT_SIZE
    leftPaint = Paint()
    leftPaint.strokeWidth = STROKE_WIDTH
    leftPaint.color = Color.GREEN
    rightPaint = Paint()
    rightPaint.strokeWidth = STROKE_WIDTH
    rightPaint.color = Color.YELLOW
  }

  override fun draw(canvas: Canvas) {
    val landmarks = pose.allPoseLandmarks
    if (landmarks.isEmpty()) {
      return
    }

    // Draw pose classification text.
//    val classificationX = POSE_CLASSIFICATION_TEXT_SIZE * 0.5f
//    for (i in poseClassification.indices) {
//      val classificationY =
//        canvas.height -
//          (POSE_CLASSIFICATION_TEXT_SIZE * 1.5f * (poseClassification.size - i).toFloat())
//      canvas.drawText(
//        poseClassification[i],
//        classificationX,
//        classificationY,
//        classificationTextPaint
//      )
//    }
//
//    // Draw all the points
//    for (landmark in landmarks) {
//      drawPoint(canvas, landmark, whitePaint)
//      if (visualizeZ && rescaleZForVisualization) {
//        zMin = min(zMin, landmark.position3D.z)
//        zMax = max(zMax, landmark.position3D.z)
//      }
//    }

    val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
    val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
    val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
    val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
    val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
    val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
    val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
    val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
    val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
    val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
    val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)

    val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
    val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
    val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
    val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
    val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
    val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
    val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
    val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
    val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
    val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
    val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
    val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

    val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
    val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
    val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
    val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
    val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
    val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
    val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
    val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
    val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
    val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)


    //  ========================================================


    //Calculate whether the distance between the shoulder and the foot is the same width
    val ratio = ratio(leftShoulder!!.position.x, rightShoulder!!.position.x, leftAnkle!!.position.x, rightAnkle!!.position.x)

    if ( olga == "pushup" ){

          //Calculate whether the hand is in front of the shoulder
      val yRightHand = differenceBetweenCoordinates(rightShoulder.position.y,rightWrist!!.position.y)
      val yLeftHand = differenceBetweenCoordinates(leftShoulder.position.y, leftWrist!!.position.y)

      //defining specific exercise logic
      val exerciseLogic = ExerciseLogic(rightWrist,rightElbow!!,rightShoulder,yRightHand,yLeftHand,ratio,
        rightShoulder.position.y + leftShoulder.position.y , rightShoulder.position.y + leftShoulder.position.y, 25, 0, 0.5,
        2, 12,
        "Posisi pushup", "Tangan lurus depan badan ", "Rentangkan kaki")

      doExerciseLogic(exerciseLogic)
      toDraw = !toDraw
      }
      if ( olga == "squat" ){

          //Calculate whether the hand exceeds the shoulder
          val yRightHand = differenceBetweenCoordinates(rightWrist!!.position.y, rightShoulder.position.y)
          val yLeftHand = differenceBetweenCoordinates(leftWrist!!.position.y, leftShoulder.position.y)

          //defining specific exercise logic
          val exerciseLogic = ExerciseLogic(rightHip!!,rightKnee!!,rightAnkle,yRightHand,yLeftHand,ratio,
              rightShoulder.position.y + leftShoulder.position.y, rightAnkle.position.y - rightHip.position.y, 5, 0, 0.5,
              2, 5,
              "Tegak blok!", "Tangan di belakang kepala", "Rentangkan kaki")

          doExerciseLogic(exerciseLogic)

          toDraw = !toDraw

    }

    //  ========================================================



    if(toDraw) {

      //draw lines between points
      // Face
        drawPoint(canvas, nose!!.position, whitePaint)
        drawPoint(canvas, leftEyeInner!!.position, leftPaint)
        drawPoint(canvas, leftEye!!.position, leftPaint)
        drawPoint(canvas, leftEyeOuter!!.position, leftPaint)
        drawPoint(canvas, leftEar!!.position, leftPaint)
        drawPoint(canvas, leftMouth!!.position, leftPaint)
        drawPoint(canvas, leftShoulder.position, leftPaint)
        drawPoint(canvas, leftElbow!!.position, leftPaint)
        drawPoint(canvas, leftWrist!!.position, leftPaint)
        drawPoint(canvas, leftHip!!.position, leftPaint)
        drawPoint(canvas, leftKnee!!.position, leftPaint)
        drawPoint(canvas, leftAnkle.position, leftPaint)
        drawPoint(canvas, leftPinky!!.position, leftPaint)
        drawPoint(canvas, leftIndex!!.position, leftPaint)
        drawPoint(canvas, leftThumb!!.position, leftPaint)
        drawPoint(canvas, leftHeel!!.position, leftPaint)
        drawPoint(canvas, leftFootIndex!!.position, leftPaint)

        //right
        drawPoint(canvas, rightEyeInner!!.position, rightPaint)
        drawPoint(canvas, rightEye!!.position, rightPaint)
        drawPoint(canvas, rightEyeOuter!!.position, rightPaint)
        drawPoint(canvas, rightEar!!.position, rightPaint)
        drawPoint(canvas, rightMouth!!.position, rightPaint)
        drawPoint(canvas, rightShoulder.position, rightPaint)
        drawPoint(canvas, rightElbow!!.position, rightPaint)
        drawPoint(canvas, rightWrist!!.position, rightPaint)
        drawPoint(canvas, rightHip!!.position, rightPaint)
        drawPoint(canvas, rightKnee!!.position, rightPaint)
        drawPoint(canvas, rightAnkle.position, rightPaint)
        drawPoint(canvas, rightPinky!!.position, rightPaint)
        drawPoint(canvas, rightIndex!!.position, rightPaint)
        drawPoint(canvas, rightThumb!!.position, rightPaint)
        drawPoint(canvas, rightHeel!!.position, rightPaint)
        drawPoint(canvas, rightFootIndex!!.position, rightPaint)


        //draw lines between points
        // Face
        drawLine(canvas, nose.position, leftEyeInner.position, whitePaint)
        drawLine(canvas, leftEyeInner.position, leftEye.position, whitePaint)
        drawLine(canvas, leftEye.position, leftEyeOuter.position, whitePaint)
        drawLine(canvas, leftEyeOuter.position, leftEar.position, whitePaint)
        drawLine(canvas, nose.position, rightEyeInner.position, whitePaint)
        drawLine(canvas, rightEyeInner.position, rightEye.position, whitePaint)
        drawLine(canvas, rightEye.position, rightEyeOuter.position, whitePaint)
        drawLine(canvas, rightEyeOuter.position, rightEar.position, whitePaint)
        drawLine(canvas, leftMouth.position, rightMouth.position, whitePaint)

        drawLine(canvas, leftShoulder.position, rightShoulder.position, whitePaint)
        drawLine(canvas, leftHip.position, rightHip.position, whitePaint)

        // Left body
        drawLine(canvas, leftShoulder.position, leftElbow.position, leftPaint)
        drawLine(canvas, leftElbow.position, leftWrist.position, leftPaint)
        drawLine(canvas, leftShoulder.position, leftHip.position, leftPaint)
        drawLine(canvas, leftHip.position, leftKnee.position, leftPaint)
        drawLine(canvas, leftKnee.position, leftAnkle.position, leftPaint)
        drawLine(canvas, leftWrist.position, leftThumb.position, leftPaint)
        drawLine(canvas, leftWrist.position, leftPinky.position, leftPaint)
        drawLine(canvas, leftWrist.position, leftIndex.position, leftPaint)
        drawLine(canvas, leftIndex.position, leftPinky.position, leftPaint)
        drawLine(canvas, leftAnkle.position, leftHeel.position, leftPaint)
        drawLine(canvas, leftHeel.position, leftFootIndex.position, leftPaint)
        drawLine(canvas, leftAnkle.position, leftFootIndex.position, leftPaint)

        // Right body
        drawLine(canvas, rightShoulder.position, rightElbow.position, rightPaint)
        drawLine(canvas, rightElbow.position, rightWrist.position, rightPaint)
        drawLine(canvas, rightShoulder.position, rightHip.position, rightPaint)
        drawLine(canvas, rightHip.position, rightKnee.position, rightPaint)
        drawLine(canvas, rightKnee.position, rightAnkle.position, rightPaint)
        drawLine(canvas, rightWrist.position, rightThumb.position, rightPaint)
        drawLine(canvas, rightWrist.position, rightPinky.position, rightPaint)
        drawLine(canvas, rightWrist.position, rightIndex.position, rightPaint)
        drawLine(canvas, rightIndex.position, rightPinky.position, rightPaint)
        drawLine(canvas, rightAnkle.position, rightHeel.position, rightPaint)
        drawLine(canvas, rightHeel.position, rightFootIndex.position, rightPaint)
        drawLine(canvas, rightAnkle.position, rightFootIndex.position, rightPaint)


      // Draw inFrameLikelihood for all points
//      if (showInFrameLikelihood) {
//        for (landmark in landmarks) {
//          canvas.drawText(
//            String.format(Locale.US, "%.2f", landmark.inFrameLikelihood),
//            translateX(landmark.position.x),
//            translateY(landmark.position.y),
//            whitePaint
//          )
//        }
//      }

    }
    drawInfoOnScreen(canvas, toDraw)
  }

//  internal fun drawPoint(canvas: Canvas, landmark: PoseLandmark, paint: Paint) {
//    val point = landmark.position3D
//    updatePaintColorByZValue(
//      paint,
//      canvas,
//      visualizeZ,
//      rescaleZForVisualization,
//      point.z,
//      zMin,
//      zMax
//    )
//    canvas.drawCircle(translateX(point.x), translateY(point.y), DOT_RADIUS, paint)
//  }

  private fun drawPoint(canvas: Canvas, point: PointF?, paint: Paint?) {
    if (point == null) return

    canvas.drawCircle(
      translateX(point.x),
      translateY(point.y),
      DOT_RADIUS,
      paint!!
    )
  }

//  internal fun drawLine(
//    canvas: Canvas,
//    startLandmark: PoseLandmark?,
//    endLandmark: PoseLandmark?,
//    paint: Paint
//  ) {
//    val start = startLandmark!!.position3D
//    val end = endLandmark!!.position3D
//
//    // Gets average z for the current body line
//    val avgZInImagePixel = (start.z + end.z) / 2
//    updatePaintColorByZValue(
//      paint,
//      canvas,
//      visualizeZ,
//      rescaleZForVisualization,
//      avgZInImagePixel,
//      zMin,
//      zMax
//    )
//
//    canvas.drawLine(
//      translateX(start.x),
//      translateY(start.y),
//      translateX(end.x),
//      translateY(end.y),
//      paint
//    )
//  }

  private fun drawLine(
    canvas: Canvas,
    start: PointF?,
    end: PointF?,
    paint: Paint?,
    strokeWidth: Float = 7f //to get adjustable and thicker lines
  ) {
    if (start == null || end == null) return

    paint?.strokeWidth = strokeWidth
    canvas.drawLine(
      translateX(start.x), translateY(start.y), translateX(end.x), translateY(end.y), paint!!
    )
  }


  private fun differenceBetweenCoordinates(firstCoordinate :Float, secondCoordinate :Float) = firstCoordinate - secondCoordinate

  /**
   * Returns the ratio between two distances
   * */

  private fun ratio(firstCoordinate :Float, secondCoordinate :Float, thirdCoordinate :Float, fourthCoordinate :Float) =
    differenceBetweenCoordinates(thirdCoordinate,fourthCoordinate) / differenceBetweenCoordinates(firstCoordinate,secondCoordinate)

  private fun getAngle(firstPoint: PoseLandmark?, midPoint: PoseLandmark?, lastPoint: PoseLandmark?): Double {
    var result = Math.toDegrees(
      atan2(
        1.0 * lastPoint!!.position.y - midPoint!!.position.y,
        1.0 * lastPoint.position.x - midPoint.position.x) -
              atan2(
                firstPoint!!.position.y - midPoint.position.y,
                firstPoint.position.x - midPoint.position.x
              )
    )
    result = abs(result) // Angle should never be negative
    if (result > 180) result = 360.0 - result // Always get the acute representation of the angle

    return result
  }

  private fun reInitParams() {
    lineOneText = ""
    lineTwoText = ""
    shoulderHeight = 0f
    minSize = 0f
    isCount = false
    isUp = false
    isDown = false
    upCount = 0
    downCount = 0
  }

  private fun doExerciseLogic(exerciseLogic: ExerciseLogic){

    val angle = getAngle(exerciseLogic.firstPoint, exerciseLogic.midPoint, exerciseLogic.lastPoint)

    if (((180 - abs(angle)) > exerciseLogic.condOne) && !isCount) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondOne
    } else if (exerciseLogic.leftHandPos > exerciseLogic.condTwo || exerciseLogic.rightHandPos > exerciseLogic.condTwo) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondTwo
    } else if (exerciseLogic.ratio < exerciseLogic.condThree && !isCount) {
      reInitParams()
      lineOneText = exerciseLogic.lTextCondThree
    } else {
      val currentHeight =
        if(exerciseLogic.condCurrentWeight != null) exerciseLogic.currentHeight / exerciseLogic.condCurrentWeight //Judging up and down by current height
        else exerciseLogic.currentHeight

      if (!isCount) {
        shoulderHeight = currentHeight
        minSize = exerciseLogic.condMinSize?.let { exerciseLogic.minSize / it } ?: exerciseLogic.minSize
        isCount = true
        lastHeight = currentHeight
        lineOneText = "Ready"
      }
      if (!isDown && (currentHeight - lastHeight) > minSize) {
        isDown = true
        isUp = false
        downCount++
        lastHeight = currentHeight
        lineTwoText = "start down"
      } else if ((currentHeight - lastHeight) > minSize) {
        lineTwoText = "downing"
        lastHeight = currentHeight
      }
      if (!isUp && (upCount < downCount) && (lastHeight - currentHeight) > minSize) {
        isUp = true
        isDown = false
        viewModel.incrementReps()
        upCount++
        lastHeight = currentHeight
        lineTwoText = "start up"
      } else if ((lastHeight - currentHeight) > minSize) {
        lineTwoText = "uping"
        lastHeight = currentHeight
      }
    }
  }

  private fun drawInfoOnScreen(canvas: Canvas, isPoseDetectionSupported: Boolean) {
    if(isPoseDetectionSupported) {
      drawText(canvas, lineOneText, null, -3)
      drawText(canvas, lineTwoText, null, -2)
//      drawText(canvas, "Rep count: ${viewModel.nrRepsDone.value}/${exercise.exeReps}", null, -1)
      drawText(canvas, "SCORE: ${viewModel.nrRepsDone.value}", null, -1)
//    } else drawText(canvas, "Reps to do: ${exercise.exeReps}", null, -1)
    } else drawText(canvas, "Reps to do: {exercise.exeReps}", null, -1)

    drawText(canvas, "SCORE: ${viewModel.nrSetsDone.value - 1}", null, 1)

    if (viewModel.isResting()) {
      drawText(canvas, "Rest Time: ${viewModel.restTime.value.toTimeFormat()}",null, 3)
      drawText(canvas, "Time to REST!",null, 4)
    }

    if(viewModel.isRecording()) {
      drawText(canvas, "Record Time: ${viewModel.recordTime.value.toTimeFormat()}",null, 3)
    }

    if(!viewModel.isResting()) drawText(canvas, "Semangat :)",null, 4)
  }

  private fun drawText(canvas: Canvas, text:String, xline: Int?, yline: Int) {
    if (text.isEmpty()) return

    xline?.let {canvas.drawText(text, TEXT_SIZE * textSizeCoordinateX * it, TEXT_SIZE * textSizeCoordinateY + TEXT_SIZE * yline, tipPaint)}
      ?: canvas.drawText(text, TEXT_SIZE * textSizeCoordinateX, TEXT_SIZE * textSizeCoordinateY + TEXT_SIZE * yline, tipPaint)
  }

  private fun Int.toTimeFormat() : String{
    val minutes = (this % (SECONDS_PER_MINUTE * SECONDS_PER_MINUTE)) / SECONDS_PER_MINUTE
    val seconds = this % SECONDS_PER_MINUTE

    return String.format("%02d:%02d", minutes, seconds)
  }






  companion object {

    private val DOT_RADIUS = 8.0f
    private val IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f
    private val STROKE_WIDTH = 10.0f
    private val POSE_CLASSIFICATION_TEXT_SIZE = 60.0f

    var isUp = false
    var isDown = false
    var upCount = 0 //up times
    var downCount = 0 //down times
    var isCount = false //is counting
    var lineOneText = ""
    var lineTwoText = ""
    var shoulderHeight = 0f
    var minSize = 0f
    var lastHeight = 0f
  }
}
