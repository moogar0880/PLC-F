package tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Fake {@link Socket} used for unit tests.
 *
 * @author Shardul Deo
 */
public class FakeSocket extends Socket {

    private boolean closed = false;

    public FakeSocket() {
    }

    private ByteArrayInputStream input;
    private ByteArrayOutputStream output;

    // Methods that can be used for client or server socket

    public FakeSocket withInputBytes(byte[] inputBytes) {
        input = new ByteArrayInputStream(inputBytes);
        return this;
    }

    public byte[] getOutputBytes() {
        return output.toByteArray();
    }

    // Overriden methods
    @Override
    public InputStream getInputStream() {
        input.reset();
        return input;
    }

    @Override
    public OutputStream getOutputStream() {
        output = new ByteArrayOutputStream();
        return output;
    }

    @Override
    public void shutdownOutput() {
        // no-op
    }

    @Override
    public synchronized void close() {
        closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}