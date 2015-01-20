package com.link;

import javafx.application.Platform;

public class AutoUpdate {
    public static void update(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                run_update();
            }
        });
    }

    public static int run_update(){
        int status_code = 0; // 1 = needs update, 0 = up to date
        double newest_version = Double.parseDouble(Main.session.readPage(Main.session.getUrl("CHECK_VERSION")));
        if(newest_version == Main.VERSION_NUMBER || Main.VERSION_NUMBER == -1){
            return status_code;
        }
        // perform update
        String old_jar_path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String new_jar_url = Main.session.getUrl("GET_JAR") + String.valueOf(newest_version) + ".jar";

        old_jar_path = old_jar_path.split("TuneUs.jar")[0] + "TuneUs.jar";
        Main.session.getFile(new_jar_url, old_jar_path);
        ErrorDialog dialog = new ErrorDialog("Updated", "TuneUs has been updated.\nPress okay to exit.", 175, 120, true);
        dialog.show();
        return status_code;
    }
}
