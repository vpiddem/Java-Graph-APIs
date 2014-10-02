/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dgraphs;

/**
 *
 * @author Vikas
 */
import java.io.File;

public class Desktop {

    // hide the constructor.
    Desktop() {
    }

    // Created the appropriate instance
    public static Desktop getDesktop() {
        String os = System.getProperty("os.name").toLowerCase();
        Desktop desktop = new Desktop();
        // This uf/elseif/else code is used only once: here
        if (os.indexOf("windows") != -1 || os.indexOf("nt") != -1) {

            desktop = new WindowsDesktop();

        } else if (os.equals("windows 95") || os.equals("windows 98")) {

            desktop = new Windows9xDesktop();

        } else if (os.indexOf("mac") != -1) {

            desktop = new OSXDesktop();

        } else if (os.indexOf("linux") != -1) {

            desktop = new GnomeDesktop();

        } else {
            throw new UnsupportedOperationException(String.format("The platform %s is not supported ", os));
        }
        return desktop;
    }

    // default implementation :( 
    public void open(File file) {
        throw new UnsupportedOperationException();
    }

    // default implementation :( 
    public void imaginaryAction(File file) {
        throw new UnsupportedOperationException();
    }
}

// One subclass per platform below:
// Each one knows how to handle its own platform   
class GnomeDesktop extends Desktop {

    public void open(File file) {
        // Runtime.getRuntime().exec: execute gnome-open <file>
    }

    public void imaginaryAction(File file) {
        // Runtime.getRuntime().exec:gnome-something-else <file>
    }

}

class KdeDesktop extends Desktop {

    public void open(File file) {
        // Runtime.getRuntime().exec: kfmclient exec <file>
    }

    public void imaginaryAction(File file) {
        // Runtime.getRuntime().exec: kfm-imaginary.sh  <file>
    }
}

class OSXDesktop extends Desktop {

    public void open(File file) {
        // Runtime.getRuntime().exec: open <file>
    }

    public void imaginaryAction(File file) {
        // Runtime.getRuntime().exec: wow!! <file>
    }
}

class WindowsDesktop extends Desktop {

    public void open(File file) {
        // Runtime.getRuntime().exec: cmd /c start <file>
    }

    public void imaginaryAction(File file) {
        // Runtime.getRuntime().exec: ipconfig /relese /c/d/e
    }
}

class Windows9xDesktop extends Desktop {

    public void open(File file) {
        //Runtime.getRuntime().exec: command.com /C start <file>
    }

    public void imaginaryAction(File file) {
        //Runtime.getRuntime().exec: command.com /C otherCommandHere <file>
    }
}
