package ru.yandex.qatools.ashot.util;

import ru.yandex.qatools.ashot.Screenshot;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.util.Arrays;

/**
 * @author <a href="frolic@yandex-team.ru">Viacheslav Frolov</a>
 */
public class ImageBytesDiffer {

    public static boolean areImagesEqual(Screenshot expected, Screenshot actual) {
        return areImagesEqual(expected.getImage(), actual.getImage());
    }

    public static boolean areImagesEqual(BufferedImage imageExpected, BufferedImage imageActual) {
        return  imageExpected.getHeight() == imageActual.getHeight() &&
                imageExpected.getWidth() == imageActual.getWidth() &&
                imageActual.getColorModel().equals(imageExpected.getColorModel()) &&
                areImagesBuffersEqual(imageExpected.getRaster().getDataBuffer(), imageActual.getRaster().getDataBuffer());
    }

    private static boolean areImagesBuffersEqual(DataBuffer expected, DataBuffer actual) {
        return actual.getDataType() == expected.getDataType() &&
                actual.getNumBanks() == expected.getNumBanks() &&
                areImagesBytesEqual(actual, expected);
    }

    private static boolean areImagesBytesEqual(DataBuffer actual, DataBuffer expected) {
        int dataType = actual.getDataType();
        boolean areEqual = true;

        int i = 0;
        while (areEqual && i < expected.getNumBanks()) {
            switch(dataType) {
                case DataBuffer.TYPE_BYTE:
                    areEqual = Arrays.equals(((DataBufferByte) actual).getData(i), ((DataBufferByte) expected).getData(i));
                    break;
                case DataBuffer.TYPE_SHORT:
                    areEqual = Arrays.equals(((DataBufferShort) actual).getData(i), ((DataBufferShort) expected).getData(i));
                    break;
                case DataBuffer.TYPE_USHORT:
                    areEqual = Arrays.equals(((DataBufferUShort) actual).getData(i), ((DataBufferUShort) expected).getData(i));
                    break;
                case DataBuffer.TYPE_INT:
                    areEqual = Arrays.equals(((DataBufferInt) actual).getData(i), ((DataBufferInt) expected).getData(i));
                    break;
                case DataBuffer.TYPE_FLOAT:
                    areEqual = Arrays.equals(((DataBufferFloat) actual).getData(i), ((DataBufferFloat) expected).getData(i));
                    break;
                case DataBuffer.TYPE_DOUBLE:
                    areEqual = Arrays.equals(((DataBufferDouble) actual).getData(i), ((DataBufferDouble) expected).getData(i));
                    break;
                default:
                    return false;
            }
            i++;
        }
        return areEqual;
    }
}
