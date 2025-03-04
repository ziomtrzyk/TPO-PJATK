package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.*;
import static java.nio.file.StandardOpenOption.*;

public class Futil {

    static private List<Path> fileList = new ArrayList<>();

    static void processDir(String dirName, String resultFileName) {

        try {
            Files.walkFileTree(Paths.get(dirName), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    fileList.add(file);
                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Charset cp1250 = Charset.forName("windows-1250");
        Charset utf8 = Charset.forName("UTF-8");

        try (FileChannel oc = FileChannel.open(Paths.get(resultFileName), CREATE, WRITE)) {
            for (Path path : fileList) {
                try (FileChannel fc = FileChannel.open(path, READ, WRITE)) {
                    ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
                    fc.read(buf);
                    buf.flip();

                    CharBuffer cbuf = cp1250.newDecoder().decode(buf);
                    ByteBuffer outBuf = utf8.encode(cbuf + "\n");
                    oc.write(outBuf);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
