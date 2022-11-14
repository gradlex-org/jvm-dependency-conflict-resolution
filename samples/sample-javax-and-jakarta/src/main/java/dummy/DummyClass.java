package dummy;

import io.undertow.servlet.handlers.ServletHandler;

public class DummyClass {

    public static void main(String[] args) {
        System.out.println(ServletHandler.class.getName());
        new ServletHandler(null);
    }
}
