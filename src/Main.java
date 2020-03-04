/*
* @author Divine Odazie
* */

import com.model.FileSearcher;

public class Main {
    public static void main(String[] args) {
        FileSearcher app = new FileSearcher();

//  Main method is the only place command line arguments can be passed
        switch (Math.min(args.length, 3)){
            case 0:
                System.out.println("USAGE: TextSearcher path [regex] [zipfile]");
                return;
            case 3:
                app.setZipFileName(args[2]);
            case 2:
                app.setPath(args[1]);
            case 1:
                app.setPath(args[0]);


        }

        try {
            app.walkDirectoryJava8(app.getPath());

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
