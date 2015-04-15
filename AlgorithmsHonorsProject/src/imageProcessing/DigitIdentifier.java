package imageProcessing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvKNearest;

public class DigitIdentifier
{
    /** the following constants are defined as per the values described at http://yann.lecun.com/exdb/mnist/ **/

    private static final int OFFSET_SIZE              = 4;             // in bytes

    private static final int LABEL_MAGIC              = 2049;
    private static final int IMAGE_MAGIC              = 2051;

    private static final int NUMBER_ITEMS_OFFSET      = 4;
    private static final int ITEMS_SIZE               = 4;

    private static final int NUMBER_OF_ROWS_OFFSET    = 8;
    private static final int ROWS_SIZE                = 4;
    public static final int  ROWS                     = 28;

    private static final int NUMBER_OF_COLUMNS_OFFSET = 12;
    private static final int COLUMNS_SIZE             = 4;
    public static final int  COLUMNS                  = 28;

    private static final int IMAGE_OFFSET             = 16;
    private static final int IMAGE_SIZE               = ROWS * COLUMNS;

    private CvKNearest       knn;
    private int              numRows = ROWS;
    private int              numCols = COLUMNS;

    public DigitIdentifier()
    {
        knn = new CvKNearest();
    }

    private class Pair
    {
        public int     label;
        public float[] data;

        public Pair(int label, float[] data)
        {
            this.label = label;
            this.data = data;
        }
    }

    public boolean train2(String trainingDataPath)
    {
        Scanner scan = null;
        try
        {
            scan = new Scanner(new File(trainingDataPath));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        ArrayList<Pair> data = new ArrayList<Pair>();

        numRows = ROWS;
        numCols = COLUMNS;
        int size = numRows * numCols;

        while (scan.hasNextLine())
        {
            int label = scan.nextInt();
            String[] dataStr = scan.nextLine().substring(1).split(" ");
            float[] imageData = new float[dataStr.length];
            for (int i = 0; i < dataStr.length; i++)
                imageData[i] = Integer.parseInt(dataStr[i]);
            if (imageData.length > 0)
                data.add(new Pair(label, imageData));
        }

        Mat trainingVectors = new Mat();
        trainingVectors.create(9020, size, CvType.CV_32FC1);

        Mat trainingClasses = new Mat();
        trainingClasses.create(9020, 1, CvType.CV_32FC1);

        for (int i = 0; i < data.size(); i++)
        {
            Pair pair = data.get(i);
            trainingClasses.put(i, 0, pair.label);
            trainingVectors.put(i, 0, pair.data);
        }

        boolean result = knn.train(trainingVectors, trainingClasses);
        System.out.println("Trained: " + result);
        scan.close();
        return result;
    }

    public boolean train(String imageFileName, String labelFileName) throws IOException
    {
        ByteArrayOutputStream labelBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();

        InputStream labelInputStream = new FileInputStream(new File(labelFileName));// this.getClass().getResourceAsStream(labelFileName);
        InputStream imageInputStream = new FileInputStream(new File(imageFileName));// this.getClass().getResourceAsStream(imageFileName);

        int read;
        byte[] buffer = new byte[16384];

        while ((read = labelInputStream.read(buffer, 0, buffer.length)) != -1)
        {
            labelBuffer.write(buffer, 0, read);
        }

        labelBuffer.flush();

        while ((read = imageInputStream.read(buffer, 0, buffer.length)) != -1)
        {
            imageBuffer.write(buffer, 0, read);
        }
        labelInputStream.close();
        imageInputStream.close();

        imageBuffer.flush();

        byte[] labelBytes = labelBuffer.toByteArray();
        byte[] imageBytes = imageBuffer.toByteArray();

        byte[] labelMagic = Arrays.copyOfRange(labelBytes, 0, OFFSET_SIZE);
        byte[] imageMagic = Arrays.copyOfRange(imageBytes, 0, OFFSET_SIZE);

        if (ByteBuffer.wrap(labelMagic).getInt() != LABEL_MAGIC)
        {
            System.out.println(ByteBuffer.wrap(labelMagic).getInt());
            throw new IOException("Bad magic number in label file!");
        }

        if (ByteBuffer.wrap(imageMagic).getInt() != IMAGE_MAGIC)
        {
            throw new IOException("Bad magic number in image file!");
        }

        int numberOfLabels = ByteBuffer.wrap(
                Arrays.copyOfRange(labelBytes, NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE)).getInt();
        int numberOfImages = ByteBuffer.wrap(
                Arrays.copyOfRange(imageBytes, NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE)).getInt();

        if (numberOfImages != numberOfLabels)
        {
            throw new IOException("The number of labels and images do not match!");
        }

        numRows = ByteBuffer.wrap(
                Arrays.copyOfRange(imageBytes, NUMBER_OF_ROWS_OFFSET, NUMBER_OF_ROWS_OFFSET + ROWS_SIZE)).getInt();
        numCols = ByteBuffer.wrap(
                Arrays.copyOfRange(imageBytes, NUMBER_OF_COLUMNS_OFFSET, NUMBER_OF_COLUMNS_OFFSET + COLUMNS_SIZE))
                .getInt();

        if (numRows != ROWS && numRows != COLUMNS)
        {
            throw new IOException("Bad image. Rows and columns do not equal " + ROWS + "x" + COLUMNS);
        }

        // ////////////////////////////////////////////////////////////////
        // Go through each training data entry and save a
        // label for each digit

        int size = numRows * numCols;

        Mat trainingVectors = new Mat();
        trainingVectors.create(numberOfImages, size, CvType.CV_32FC1);

        Mat trainingClasses = new Mat();
        trainingClasses.create(numberOfImages, 1, CvType.CV_32FC1);

        PrintWriter writer = new PrintWriter(new File("training_data.dat"));

        for (int i = 0; i < numberOfLabels; i++)
        {
            int label = labelBytes[OFFSET_SIZE + ITEMS_SIZE + i];
            byte[] imageData = Arrays.copyOfRange(imageBytes, (i * IMAGE_SIZE) + IMAGE_OFFSET, (i * IMAGE_SIZE)
                    + IMAGE_OFFSET + IMAGE_SIZE);
            float[] imageData2 = new float[imageData.length];

            writer.write(label + " ");
            for (int k = 0; k < imageData.length; k++)
            {
                int temp = Byte.toUnsignedInt(imageData[k]);
                writer.write("" + temp);
                if (k < imageData.length - 1)
                    writer.write(' ');
                imageData2[k] = (float) temp;
            }
            writer.write("\n");
            trainingClasses.put(i, 0, label);
            trainingVectors.put(i, 0, imageData2);
        }
        // System.out.println(trainingVectors.dump());
        writer.close();
        boolean result = knn.train(trainingVectors, trainingClasses);
        System.out.println("Trained: " + result);

        return result;
    }

    public int classify(Mat img, FloodFillBounds maxBounds)
    {
        Mat cloneImg = preprocessImage(img, maxBounds);
        float value = knn.find_nearest(cloneImg, 1, new Mat(), new Mat(), new Mat());
        System.out.println(value);
        return (int) value;
    }

    public Mat preprocessImage(Mat img, FloodFillBounds maxBounds)
    {
        int rowTop = -1, rowBottom = -1, colLeft = -1, colRight = -1;

        rowBottom = maxBounds.maxX;
        rowTop = maxBounds.minX;
        colLeft = maxBounds.minY;
        colRight = maxBounds.maxY;

//        Core.line(img, new Point(0, rowTop), new Point(img.cols(), rowTop), new Scalar(255, 0, 0));
//        Core.line(img, new Point(0, rowBottom), new Point(img.cols(), rowBottom), new Scalar(255, 0, 0));
//        Core.line(img, new Point(colLeft, 0), new Point(colLeft, img.rows()), new Scalar(255, 0, 0));
//        Core.line(img, new Point(colRight, 0), new Point(colRight, img.rows()), new Scalar(255, 0, 0));
//        SudokuRecognizer.displayImage(SudokuRecognizer.Mat2BufferedImage(img));

        // Now, position this into the center

        Mat newImg;
        newImg = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC1);

        int startAtX = (newImg.cols() / 2) - (colRight - colLeft) / 2;
        int startAtY = (newImg.rows() / 2) - (rowBottom - rowTop) / 2;

        for (int y = startAtY; y < (newImg.rows() / 2) + (rowBottom - rowTop) / 2; y++)
        {
            byte[] row = SudokuRecognizer.getRowBytes(newImg, y);
            byte[] imgRow = SudokuRecognizer.getRowBytes(img, rowTop + (y - startAtY));
            for (int x = startAtX; x < (newImg.cols() / 2) + (colRight - colLeft) / 2; x++)
            {
                row[x] = imgRow[colLeft + (x - startAtX)];
            }
            newImg.put(y, 0, row);
        }

        Mat cloneImg = new Mat(numRows, numCols, CvType.CV_8UC1);
        Imgproc.resize(newImg, cloneImg, new Size(numCols, numRows));

        // Now fill along the borders
        for (int i = 0; i < cloneImg.rows(); i++)
        {
            Imgproc.floodFill(cloneImg, SudokuRecognizer.createFloodMask(cloneImg), new Point(0, i),
                    new Scalar(0, 0, 0));
            Imgproc.floodFill(cloneImg, SudokuRecognizer.createFloodMask(cloneImg), new Point(cloneImg.cols() - 1, i),
                    new Scalar(0, 0, 0));

            Imgproc.floodFill(cloneImg, SudokuRecognizer.createFloodMask(cloneImg), new Point(i, 0), new Scalar(0));
            Imgproc.floodFill(cloneImg, SudokuRecognizer.createFloodMask(cloneImg), new Point(i, cloneImg.rows() - 1),
                    new Scalar(0));
        }

//        SudokuRecognizer.displayImage(SudokuRecognizer.Mat2BufferedImage(cloneImg));
        
        cloneImg = cloneImg.reshape(1, 1);
        cloneImg.convertTo(cloneImg, CvType.CV_32F);
        return cloneImg;
    }
}
