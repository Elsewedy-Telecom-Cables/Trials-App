module com.elsewedyt.trialsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires itextpdf;
    requires java.naming;
    requires java.desktop;
    requires controlsfx;
    requires mail;
    requires activation;
    requires jbcrypt;
    requires org.apache.logging.log4j;
    requires java.net.http;
    requires org.apache.poi.ooxml;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.javafx;
    //requires de.jensd.fx.glyphs.fontawesome;

    opens com.elsewedyt.trialsapp.controllers to javafx.fxml;
    opens screens to javafx.fxml;
    exports com.elsewedyt.trialsapp.controllers;
    exports com.elsewedyt.trialsapp.models;




}
