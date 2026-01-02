package com.etc.trials.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.etc.trials.service.WindowUtils.ALERT;
import static com.etc.trials.service.WindowUtils.ALERT_WARNING;

public class SettingService {

    // XML DATA
    public static String APP_LANG = "EN";
    public static String DATABASE_NAME;
    public static String DATABASE_USER;
    public static String DATABASE_PASS;
    public static String DATABASE_IP;
    public static String COMPANY_NAME;
    public static String COMPANY_SPECIALITY;
    public static int STORE_ALARM;
    public static String BARCODE_PRINTER_NAME;
    public static String REPORT_PARTATION;
    public static String BACKP_FOLDER = "G://BACKUP";


    // General


    public static void getXmlFile() {
        Element element = null;
        try {
            File xmlFile = new File("settings.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = (Document) builder.parse(xmlFile);

            NodeList studentNodes = doc.getElementsByTagName("config");
            org.w3c.dom.Node studentNode = studentNodes.item(0);
            if (studentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                element = (Element) studentNode;
            }
            getXmlData(element);

        } catch (Exception ex) {
            System.out.println(SettingService.class.getName() + " ** initialize > - Error open settings file : " + " # " + ex.getMessage());
        }
    }

    private static void getXmlData(Element element) {
        COMPANY_NAME = element.getElementsByTagName("companyName").item(0).getTextContent();
        COMPANY_SPECIALITY = element.getElementsByTagName("companySpecialty").item(0).getTextContent();
        STORE_ALARM = Integer.parseInt(element.getElementsByTagName("StoreAlarm").item(0).getTextContent());
        BARCODE_PRINTER_NAME = element.getElementsByTagName("BarcodePrinterName").item(0).getTextContent();
        DATABASE_NAME = element.getElementsByTagName("database").item(0).getTextContent();
        DATABASE_USER = element.getElementsByTagName("databaseUser").item(0).getTextContent();
        DATABASE_PASS = element.getElementsByTagName("databasePass").item(0).getTextContent();
        DATABASE_IP = element.getElementsByTagName("databaseIp").item(0).getTextContent();
        REPORT_PARTATION = element.getElementsByTagName("ReportPartation").item(0).getTextContent();
        BACKP_FOLDER = element.getElementsByTagName("backUpPath").item(0).getTextContent();
        APP_LANG = element.getElementsByTagName("Lang").item(0).getTextContent();

    }

    public static ResourceBundle APP_BUNDLE() {
        Locale.setDefault(new Locale("ar"));
        if (APP_LANG.equals("AR")) {
            Locale.setDefault(new Locale("ar"));
        } else if (APP_LANG.equals("EN")) {
            Locale.setDefault(Locale.ENGLISH);
        }
        return ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
    }

    /**
     * Checks license expiration date.
     */
    public static void checkLicense() {
        // The expiration date for the license
        LocalDate licenseExpirationDate = LocalDate.of(2026, 5, 25);
        // Check the license validity
        if (!isLicenseValid(licenseExpirationDate)) {
            ALERT("", APP_BUNDLE().getString("EXPIRED_ERROR"), ALERT_WARNING);
            System.exit(0); // Close the application
        }
    }

    private static boolean isLicenseValid(LocalDate expirationDate) {
        // Get today's date
        LocalDate today = LocalDate.now();

        // If today's date is after the expiration date, return false (expired)
        return !today.isAfter(expirationDate);
    }



}
