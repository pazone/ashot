package pazone.ashot.util;

import pazone.ashot.Screenshot;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 * @author <a href="frolic@yandex-team.ru">Viacheslav Frolov</a>
 */
public final class ImageBytesDiffer {

    private ImageBytesDiffer() {
        throw new UnsupportedOperationException();
    }

    public static boolean areImagesEqual(Screenshot expected, Screenshot actual) {
        return areImagesEqual(expected.getImage(), actual.getImage());
    }

    public static boolean areImagesEqual(BufferedImage expected, BufferedImage actual) {
        return  expected.getHeight() == actual.getHeight() &&
                expected.getWidth() == actual.getWidth() &&
                actual.getColorModel().equals(expected.getColorModel()) &&
                areImagesBuffersEqual(expected.getRaster().getDataBuffer(), actual.getRaster().getDataBuffer());
    }

    private static boolean areImagesBuffersEqual(DataBuffer expected, DataBuffer actual) {
        return actual.getDataType() == expected.getDataType() &&
                actual.getNumBanks() == expected.getNumBanks() &&
                actual.getSize() == expected.getSize() &&
                areImagesBytesEqual(actual, expected);
    }

    private static boolean areImagesBytesEqual(DataBuffer expected, DataBuffer actual) {
        for (int bank = 0; bank < expected.getNumBanks(); bank++) {
            for (int i = 0; i < expected.getSize(); i++) {
                if (expected.getElem(bank, i) != actual.getElem(bank, i)) {
                    return false;
                }
            }
        }
        return true;
    }
}
