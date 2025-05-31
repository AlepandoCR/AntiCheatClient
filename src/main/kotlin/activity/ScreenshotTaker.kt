package activity

import java.awt.Robot
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object ScreenshotTaker {
    private val robot = Robot()
    private val screenRect = Rectangle(Toolkit.getDefaultToolkit().screenSize)

    fun captureScreenshot(): ByteArray {
        val screenshot: BufferedImage = robot.createScreenCapture(screenRect)
        val output = ByteArrayOutputStream()
        ImageIO.write(screenshot, "jpg", output)
        return output.toByteArray()
    }
}
