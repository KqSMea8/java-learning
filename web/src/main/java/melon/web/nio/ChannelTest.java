package melon.web.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {

    public static void main(String[] args) throws IOException {
        transfer();
    }
    public static void transfer() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile(".gitignore","rw");
        FileChannel fromChannel = fromFile.getChannel();
        RandomAccessFile toFile = new RandomAccessFile("toFile.txt","rw");
        FileChannel toChannel = toFile.getChannel();
        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(toChannel,position,count );

        long size = toChannel.size();
        System.out.println("size:"+size);

    }

    public static void chaTest() throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(".gitignore","rw");

        FileChannel channel = accessFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        ByteBuffer buffer1 = ByteBuffer.allocate(48);
        ByteBuffer[] buffers = new ByteBuffer[] {buffer,buffer1};
        long read = channel.read(buffers);
        while (read != -1) {
            System.out.println("Read..." + read);
            for (ByteBuffer b : buffers) {
                b.flip();
                while (b.hasRemaining()) {
                    System.out.print((char)b.get());
                }
                System.out.println();
                b.clear();
            }
            read = channel.read(buffers);
        }
        accessFile.close();
    }
}
