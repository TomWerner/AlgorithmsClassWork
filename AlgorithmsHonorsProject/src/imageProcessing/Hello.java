package imageProcessing;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Hello
{
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat sudoku = Highgui.imread("sudoku2.JPG", Highgui.IMREAD_GRAYSCALE);
        Mat outerBox = new Mat(sudoku.size(), CvType.CV_8UC1);
        Imgproc.GaussianBlur(sudoku, sudoku, new Size(11, 11), 0);
        Imgproc.adaptiveThreshold(sudoku, outerBox, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 2);
        Core.bitwise_not(outerBox, outerBox);

        Mat kernel = Mat.zeros(3, 3, CvType.CV_8U);
        kernel.put(0, 0, new byte[] { 0, 1, 0 });
        kernel.put(0, 0, new byte[] { 1, 1, 1 });
        kernel.put(0, 0, new byte[] { 0, 1, 0 });
        Imgproc.dilate(outerBox, outerBox, kernel);

        int count = 0;
        int max = -1;

        Point maxPt = null;
        for (int y = 0; y < outerBox.size().height; y++)
        {
            int[] row = getRow(outerBox, y);
            for (int x = 0; x < outerBox.size().width; x++)
            {
                if (row[x] >= 180)
                {
                    int area = Imgproc.floodFill(outerBox, createFloodMask(outerBox), new Point(x, y), new Scalar(60,
                            60, 60));

                    if (area > max)
                    {
                        maxPt = new Point(x, y);
                        max = area;
                    }
                }
            }
        }

        Imgproc.floodFill(outerBox, createFloodMask(outerBox), maxPt, new Scalar(255, 255, 255));

        for (int y = 0; y < outerBox.size().height; y++)
        {
            int[] row = getRow(outerBox, y);
            for (int x = 0; x < outerBox.size().width; x++)
                if (row[x] == 60 && x != maxPt.x && y != maxPt.y)
                    Imgproc.floodFill(outerBox, createFloodMask(outerBox), new Point(x, y), new Scalar(0, 0, 0));
        }

        Imgproc.erode(outerBox, outerBox, kernel);

        Mat lines = new Mat();
        Imgproc.HoughLines(outerBox, lines, 1, Math.PI / 180, 200);
        mergeRelatedLines(lines, sudoku); // Add this line

        // Now detect the lines on extremes
        double[] topEdge = { 1000, 1000 };
        double topYIntercept = 100000, topXIntercept = 0;
        double[] bottomEdge = { -1000, -1000 };
        double bottomYIntercept = 0, bottomXIntercept = 0;
        double[] leftEdge = { 1000, 1000 };
        double leftXIntercept = 100000, leftYIntercept = 0;
        double[] rightEdge = { -1000, -1000 };
        double rightXIntercept = 0, rightYIntercept = 0;

        for (int i = 0; i < lines.cols(); i++)
        {
            double[] current = getLine(lines, i);

            float p = (float) current[0];
            float theta = (float) current[1];

            if (p == 0 && theta == -100)
                continue;

            double xIntercept, yIntercept;
            xIntercept = p / Math.cos(theta);
            yIntercept = p / (Math.cos(theta) * Math.sin(theta));
            int wiggleRoom = 10;

            if (theta > Math.PI * (90 - wiggleRoom) / 180 && theta < Math.PI * (90 + wiggleRoom) / 180)
            {
                if (p < topEdge[0] && p > 0)
                    topEdge = current;

                if (p > bottomEdge[0])
                    bottomEdge = current;
            }
            else if (theta < Math.PI * wiggleRoom / 180 || theta > Math.PI * (180 - wiggleRoom) / 180)
            {
                if (xIntercept > rightXIntercept)
                {
                    rightEdge = current;
                    rightXIntercept = xIntercept;
                }
                else if (xIntercept <= leftXIntercept)
                {
                    leftEdge = current;
                    leftXIntercept = xIntercept;
                }
            }
        }

        drawLine(topEdge, outerBox, new Scalar(128, 128, 128));
        drawLine(bottomEdge, outerBox, new Scalar(128, 128, 128));
        drawLine(leftEdge, outerBox, new Scalar(128, 128, 128));
        drawLine(rightEdge, outerBox, new Scalar(128, 128, 128));

        Point left1 = new Point();
        Point left2 = new Point();
        Point right1 = new Point();
        Point right2 = new Point();
        Point bottom1 = new Point();
        Point bottom2 = new Point();
        Point top1 = new Point();
        Point top2 = new Point();

        int height = (int) outerBox.size().height;

        int width = (int) outerBox.size().width;

        if (leftEdge[1] != 0)
        {
            left1.x = 0;
            left1.y = leftEdge[0] / Math.sin(leftEdge[1]);
            left2.x = width;
            left2.y = -left2.x / Math.tan(leftEdge[1]) + left1.y;
        }
        else
        {
            left1.y = 0;
            left1.x = leftEdge[0] / Math.cos(leftEdge[1]);
            left2.y = height;
            left2.x = left1.x - height * Math.tan(leftEdge[1]);

        }

        if (rightEdge[1] != 0)
        {
            right1.x = 0;
            right1.y = rightEdge[0] / Math.sin(rightEdge[1]);
            right2.x = width;
            right2.y = -right2.x / Math.tan(rightEdge[1]) + right1.y;
        }
        else
        {
            right1.y = 0;
            right1.x = rightEdge[0] / Math.cos(rightEdge[1]);
            right2.y = height;
            right2.x = right1.x - height * Math.tan(rightEdge[1]);

        }

        bottom1.x = 0;
        bottom1.y = bottomEdge[0] / Math.sin(bottomEdge[1]);

        bottom2.x = width;
        bottom2.y = -bottom2.x / Math.tan(bottomEdge[1]) + bottom1.y;

        top1.x = 0;
        top1.y = topEdge[0] / Math.sin(topEdge[1]);
        top2.x = width;
        top2.y = -top2.x / Math.tan(topEdge[1]) + top1.y;

        // Next, we find the intersection of these four lines
        double leftA = left2.y - left1.y;
        double leftB = left1.x - left2.x;

        double leftC = leftA * left1.x + leftB * left1.y;

        double rightA = right2.y - right1.y;
        double rightB = right1.x - right2.x;

        double rightC = rightA * right1.x + rightB * right1.y;

        double topA = top2.y - top1.y;
        double topB = top1.x - top2.x;

        double topC = topA * top1.x + topB * top1.y;

        double bottomA = bottom2.y - bottom1.y;
        double bottomB = bottom1.x - bottom2.x;

        double bottomC = bottomA * bottom1.x + bottomB * bottom1.y;

        // Intersection of left and top
        double detTopLeft = leftA * topB - leftB * topA;

        Point2D ptTopLeft = new Point2D.Double((topB * leftC - leftB * topC) / detTopLeft,
                (leftA * topC - topA * leftC) / detTopLeft);

        // Intersection of top and right
        double detTopRight = rightA * topB - rightB * topA;

        Point2D ptTopRight = new Point2D.Double((topB * rightC - rightB * topC) / detTopRight, (rightA * topC - topA
                * rightC)
                / detTopRight);

        // Intersection of right and bottom
        double detBottomRight = rightA * bottomB - rightB * bottomA;
        Point2D ptBottomRight = new Point2D.Double((bottomB * rightC - rightB * bottomC) / detBottomRight, (rightA
                * bottomC - bottomA * rightC)
                / detBottomRight);// Intersection of bottom and left
        double detBottomLeft = leftA * bottomB - leftB * bottomA;
        Point2D ptBottomLeft = new Point2D.Double((bottomB * leftC - leftB * bottomC) / detBottomLeft,
                (leftA * bottomC - bottomA * leftC) / detBottomLeft);

        int maxLength = (int) ((ptBottomLeft.getX() - ptBottomRight.getX())
                * (ptBottomLeft.getX() - ptBottomRight.getX()) + (ptBottomLeft.getY() - ptBottomRight.getY())
                * (ptBottomLeft.getY() - ptBottomRight.getY()));
        int temp = (int) ((ptTopRight.getX() - ptBottomRight.getX()) * (ptTopRight.getX() - ptBottomRight.getX()) + (ptTopRight
                .getY() - ptBottomRight.getY()) * (ptTopRight.getY() - ptBottomRight.getY()));

        if (temp > maxLength)
            maxLength = temp;

        temp = (int) ((ptTopRight.getX() - ptTopLeft.getX()) * (ptTopRight.getX() - ptTopLeft.getX()) + (ptTopRight
                .getY() - ptTopLeft.getY()) * (ptTopRight.getY() - ptTopLeft.getY()));

        if (temp > maxLength)
            maxLength = temp;

        temp = (int) ((ptBottomLeft.getX() - ptTopLeft.getX()) * (ptBottomLeft.getX() - ptTopLeft.getX()) + (ptBottomLeft
                .getY() - ptTopLeft.getY()) * (ptBottomLeft.getY() - ptTopLeft.getY()));

        if (temp > maxLength)
            maxLength = temp;

        maxLength = (int) Math.sqrt((double) maxLength);

        Point2D[] src = new Point2D[4];
        Point2D[] dst = new Point2D[4];
        src[0] = ptTopLeft;
        dst[0] = new Point2D.Double(0, 0);
        src[1] = ptTopRight;
        dst[1] = new Point2D.Double(maxLength - 1, 0);
        src[2] = ptBottomRight;
        dst[2] = new Point2D.Double(maxLength - 1, maxLength - 1);
        src[3] = ptBottomLeft;
        dst[3] = new Point2D.Double(0, maxLength - 1);

        Mat undistorted = new Mat(new Size(maxLength, maxLength), CvType.CV_8UC1);
        Mat m1 = point2dToMat(src);
        Mat m2 = point2dToMat(dst);
        System.out.println(m1.cols() + " , " + m1.dump());
        System.out.println(m2.cols() + " , " + m2.dump() + " : " + m2.checkVector(2, CvType.CV_32F));
        Imgproc.warpPerspective(sudoku, undistorted,
                Imgproc.getPerspectiveTransform(point2dToMat(src), point2dToMat(dst)), new Size(maxLength, maxLength));

        displayImage(Mat2BufferedImage(undistorted));
    }

    private static Mat point2dToMat(Point2D[] src)
    {
        Mat mat = new Mat(src.length, 1, CvType.CV_32FC2);
        for (int i = 0; i < src.length; i++)
            setLine(mat, i, new double[]{src[i].getX(), src[i].getY()});
        return mat;
    }

    private static void drawLine(double[] line, Mat img, Scalar rgb)
    {
        if (line[1] != 0)
        {
            float m = (float) (-1 / Math.tan(line[1]));

            float c = (float) (line[0] / Math.sin(line[1]));

            Core.line(img, new Point(0, c), new Point(img.size().width, m * img.size().width + c), rgb);
        }
        else
        {
            Core.line(img, new Point(line[0], 0), new Point(line[0], img.size().height), rgb);
        }

    }

    private static Mat createFloodMask(Mat outerBox)
    {
        return new Mat(outerBox.height() + 2, outerBox.width() + 2, outerBox.type());
    }

    public static int[] getRow(Mat mat, int y)
    {
        String row = mat.row(y).dump();
        row = row.substring(1, row.length() - 1);
        String[] elems = row.split(", ");
        int[] result = new int[elems.length];
        for (int i = 0; i < elems.length; i++)
            result[i] = Integer.parseInt(elems[i]);
        return result;
    }

    private static void mergeRelatedLines(Mat lines, Mat img)
    {
        for (int i = 0; i < lines.cols(); i++)
        {
            double[] current = getLine(lines, i);
            if (current[0] == 0 && current[1] == -100)
                continue;
            float p1 = (float) current[0];
            float theta1 = (float) current[1];

            Point pt1current = new Point();
            Point pt2current = new Point();
            if (theta1 > Math.PI * 45 / 180 && theta1 < Math.PI * 135 / 180)
            {
                pt1current.x = 0;

                pt1current.y = p1 / Math.sin(theta1);

                pt2current.x = img.size().width;
                pt2current.y = -pt2current.x / Math.tan(theta1) + p1 / Math.sin(theta1);
            }
            else
            {
                pt1current.y = 0;

                pt1current.x = p1 / Math.cos(theta1);

                pt2current.y = img.size().height;
                pt2current.x = -pt2current.y / Math.tan(theta1) + p1 / Math.cos(theta1);
            }

            for (int k = 0; k < lines.cols(); k++)
            {
                if (i == k)
                    continue;
                double[] pos = getLine(lines, k);

                if (Math.abs(pos[0] - current[0]) < 20 && Math.abs((pos)[1] - (current)[1]) < Math.PI * 10 / 180)
                {
                    float p = (float) (pos)[0];
                    float theta = (float) (pos)[1];

                    Point pt1 = new Point();
                    Point pt2 = new Point();
                    if ((pos)[1] > Math.PI * 45 / 180 && (pos)[1] < Math.PI * 135 / 180)
                    {
                        pt1.x = 0;
                        pt1.y = p / Math.sin(theta);
                        pt2.x = img.size().width;
                        pt2.y = -pt2.x / Math.tan(theta) + p / Math.sin(theta);
                    }
                    else
                    {
                        pt1.y = 0;
                        pt1.x = p / Math.cos(theta);
                        pt2.y = img.size().height;
                        pt2.x = -pt2.y / Math.tan(theta) + p / Math.cos(theta);
                    }

                    if (((double) (pt1.x - pt1current.x) * (pt1.x - pt1current.x) + (pt1.y - pt1current.y)
                            * (pt1.y - pt1current.y) < 64 * 64)
                            && ((double) (pt2.x - pt2current.x) * (pt2.x - pt2current.x) + (pt2.y - pt2current.y)
                                    * (pt2.y - pt2current.y) < 64 * 64))
                    {
                        // Merge the two
                        (current)[0] = ((current)[0] + (pos)[0]) / 2;

                        (current)[1] = ((current)[1] + (pos)[1]) / 2;

                        (pos)[0] = 0;
                        (pos)[1] = -100;

                    }
                }
            }
        }
    }

    public static double[] getLine(Mat mat, int y)
    {
        String row = mat.col(y).dump();
        row = row.substring(1, row.length() - 1);
        String[] elems = row.split(", ");
        double[] result = new double[elems.length];
        for (int i = 0; i < elems.length; i++)
            result[i] = Double.parseDouble(elems[i]);
        return result;
    }

    public static void setLine(Mat mat, int y, double[] newLine)
    {
        mat.row(y).put(0, 0, newLine);
    }

    public static BufferedImage Mat2BufferedImage(Mat m)
    {
        // source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
        // Fastest code
        // The output can be assigned either to a BufferedImage or to an Image

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1)
        {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;

    }

    public static void displayImage(Image img2)
    {
        ImageIcon icon = new ImageIcon(img2);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null) + 50, img2.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}